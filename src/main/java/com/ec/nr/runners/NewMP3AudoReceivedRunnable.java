package com.ec.nr.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;
import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;
import com.ec.nr.workq.WorkQManager;

public class NewMP3AudoReceivedRunnable extends AbstractAudioRunnable {

	private Logger logger;
	
	private String audioFileReference;
	
	public NewMP3AudoReceivedRunnable(MP3SpreadsheetService ss, WorkQManager wqm, String audioFileReference, String audio) {
		super(ss, wqm, audio);
		this.audioFileReference = audioFileReference;
		
		logger = LogManager.getLogger(this.getClass().getSimpleName() + "|" + audio);
		logger.trace("Landing Pad Runnable Received For:" + audio);
	}
	

	@Override
	public void execute() {
					
		try {
			logger.info("About to update the spreadsheet to 'Ready'...");
			SpreadsheetDataRow data = getSpeakerSS().getAudioDetails(getId());
			if (data.getFieldValue(Field.MP3_STATE) == null || data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("")) {
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, "Receiving in Progress");
				Thread.sleep(45000);
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, "Upload Complete");
			} else {
				logger.debug("Ignoring MP3:" + getId());
			}
			
			logger.info("finished updating spreadsheet");
		} catch (Throwable th) {
			logger.error("problems updating spreadsheet for MP3:" + this.audioFileReference, th);
			throw new Error("problems updating spreadsheet for MP3:" + this.audioFileReference, th);
		}
				
	}


	@Override
	public String getStatus() {
		return null;
	}


	@Override
	public String getSpreadsheetDurationField() {
		return null;
	}
	
	

}
