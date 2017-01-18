package com.ec.nr.watcher;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.nr.runners.script.RunScriptFactory;
import com.ec.nr.runners.script.ScriptProcessingRunner;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.workq.WorkQManager;

@Service
public class SpreadsheetWatcher extends Thread {

	private static Logger logger = LogManager.getLogger(SpreadsheetWatcher.class);
	
	@Autowired private WorkQManager workQ;
	@Autowired private MP3SpreadsheetService speakerSS;
	@Autowired private RunScriptFactory scriptFactory;
	
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
				//if (mp3Status != null && !"".equals(mp3Status))
				//	logger.trace("mp3 being evaluated:" + mp3 + "|" + mp3Status);
				
				ScriptProcessingRunner runner = scriptFactory.getScriptRunner(mp3Status, mp3);
				
				if (runner != null) {
					workQ.addAudio(runner);
				} else {
					//if (mp3Status != null && !"".equals(mp3Status))
					//	logger.error("Not sure what to do with MP3 Id {} with status {}", mp3, mp3Status);
				}
			}
			
			
			
			try {
				Thread.currentThread().sleep(sleepTimeInterval);
			} catch (InterruptedException e) {
				logger.error("interrupted sleep!", e);
			}
			
		}
		
		
	}

	public void run() {
		watch();
		
	}


}
