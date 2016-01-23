package com.ec.nr.watcher;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.nr.runners.NewMP3AudoReceivedRunnable;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.workq.WorkQManager;

@Service
public class LandingPadWatcher extends Thread {
	
	
	private static Logger logger = LogManager.getLogger(LandingPadWatcher.class);
	
	@Autowired private WorkQManager workQ;
	@Autowired private MP3SpreadsheetService speakerSpreadsheet;
	
	private String pathToWatch;

	public LandingPadWatcher(String pathToWatch) {
		this.pathToWatch = pathToWatch;
	}
	
	private void watch() {
		
		logger.info("Watching path: " + pathToWatch);

		// We obtain the file system of the Path
		Path path = Paths.get(pathToWatch);
		FileSystem fs = path.getFileSystem();
		WatchService service = null;

		try {
			service = fs.newWatchService();

			// We register the path to the service
			// We watch for creation events
			path.register(service, StandardWatchEventKinds.ENTRY_CREATE);

			// Start the infinite polling loop
			WatchKey key = null;
			
			while (true) {
				logger.trace("Top of while loop...waiting for files...");
				key = service.take();

				// Dequeueing events
				Kind<?> kind = null;
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					// Get the type of the event
					kind = watchEvent.kind();
					if (java.nio.file.StandardWatchEventKinds.OVERFLOW == kind) {
						continue; // loop
					//} else if (StandardWatchEventKinds.ENTRY_CREATE == kind) {
					} else if (StandardWatchEventKinds.ENTRY_CREATE == kind) {
						// A new Path was created
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						String newPathString = newPath.toString();
						String mp3Name = (newPathString.lastIndexOf("/") > 0) ? newPathString.substring(newPathString.lastIndexOf("/")+1,  newPathString.indexOf(".mp3")) : newPathString.substring(0,  newPathString.indexOf(".mp3"));
						
						workQ.addAudio(new NewMP3AudoReceivedRunnable(speakerSpreadsheet, workQ, newPathString, mp3Name));
						
						logger.debug("New audio created: " + newPath);
					} else if (StandardWatchEventKinds.ENTRY_MODIFY == kind) {
						logger.debug("MODIFY for:" + ((WatchEvent<Path>) watchEvent).context());
					}
				}

				if (!key.reset()) {
					break; // loop
				}
			}

		} catch (IOException ioe) {
			logger.error("IO Exeption Caught!", ioe);
		} catch (InterruptedException ie) {
			logger.error("Interrupt Exception Caught!", ie);
		} finally {
			
			if (service != null)
				try {
					service.close();
				} catch (IOException e) {
					logger.error("Problems closing the watch service!", e);
				}
		}
	}

	public void run() {
		watch();
	}

}
