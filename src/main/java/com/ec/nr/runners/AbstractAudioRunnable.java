package com.ec.nr.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;
import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;
import com.ec.nr.workq.WorkQManager;

public abstract class AbstractAudioRunnable extends AudioRunnersBase implements
		MP3Runnable {
	
	private static Logger logger = LogManager.getLogger(AbstractAudioRunnable.class);
	private String id;
	
	public abstract void doWork();
	public abstract String getStartingStatus();
	public abstract String getEndingStatus();
	
	public AbstractAudioRunnable(MP3SpreadsheetService ss, WorkQManager wqm, String audioName) {
		super(ss, wqm);
		
		id = audioName;
	}

	@Override
	public void run() {
		
		logger.info("Executing runnable for mp3: " + getId() + "...");
		
		execute();
		
		getWorkQManager().audioComplete(this);
		
		logger.info("Completed executing runnable for mp3: " + getId() + ".");
		
		
	}
	
	public final void execute() {
		
		try {
			logger.info("About to update the spreadsheet to 'Ready'...");
			SpreadsheetDataRow data = getSpeakerSS().getAudioDetails(getId());
			logger.trace("spreadsheet data row:{}", data);
			if (data.getFieldValue(Field.MP3_STATE) == null || data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("")
					|| data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("Not Received")
					|| data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("Done")) {
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, getStartingStatus());
				doWork();
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, getEndingStatus());
			} else {
				logger.debug("Ignoring MP3:" + getId());
			}
			
			logger.info("finished updating spreadsheet");
		} catch (Throwable th) {
			logger.error("problems executing runnable for audio id:" + this.id, th);
			throw new Error("problems executing runnable for audio id:" + this.id, th);
		}
				
	}
	
	
	@Override
	public String getId() {
		
		return id;
	}

}
