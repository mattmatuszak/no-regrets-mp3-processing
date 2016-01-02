package com.ec.nr.watcher;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ec.nr.runners.AddLeadIn;
import com.ec.nr.runners.ConvertMP3ToWav;
import com.ec.nr.runners.ConvertToMP3;
import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.workq.WorkQManager;

@Service
public class SpreadsheetWatcher extends Thread {

	private static Logger logger = LogManager.getLogger(SpreadsheetWatcher.class);
	
	@Autowired private WorkQManager workQ;
	@Autowired private SpeakerSpreadsheet speakerSS;
	
	private int sleepTimeInterval;
	
	
	public SpreadsheetWatcher(int sleepTimeInterval) {
		this.sleepTimeInterval = sleepTimeInterval;
	}
	
	private void watch() {
		
		logger.info("Starting to watch the spreadsheet...");
		
		while (true) {
			
			
			Map<String, String> mp3Statuses = speakerSS.getAllMP3Statuses();
			
			for (String mp3 : mp3Statuses.keySet()) {
				
				String mp3Status = (mp3Statuses.get(mp3) == null) ? "" : mp3Statuses.get(mp3);
				logger.trace("mp3 being evaluated:" + mp3 + "|" + mp3Status);
				
				switch (mp3Status) {
					case "x":
					case "X":
						workQ.addAudio(new ConvertMP3ToWav(mp3));
						break;
						
					case "Completed MP3 to WAV Conversion":
						workQ.addAudio(new AddLeadIn(mp3));
						break;
					
					case "Completed Adding Leadin":
						workQ.addAudio(new ConvertToMP3(mp3));
						break;
	
					default:
						break;
				}
			}
			
			
			
			try {
				Thread.currentThread().sleep(sleepTimeInterval);
			} catch (InterruptedException e) {
				logger.error("interrupted sleep!", e);
			}
			
		}
		
		
	}


	public WorkQManager getWorkQ() {
		return workQ;
	}


	public void setWorkQ(WorkQManager workQ) {
		this.workQ = workQ;
	}

	public void run() {
		watch();
		
	}

	public void setSpeakerSS(SpeakerSpreadsheet speakerSS) {
		this.speakerSS = speakerSS;
	}

	public void setSleepTimeInterval(int sleepTimeInterval) {
		this.sleepTimeInterval = sleepTimeInterval;
	}

}
