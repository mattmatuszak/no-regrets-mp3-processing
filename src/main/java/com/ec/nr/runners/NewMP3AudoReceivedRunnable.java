package com.ec.nr.runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.workq.WorkQManager;

public class NewMP3AudoReceivedRunnable extends AbstractAudioRunnable {

	private Logger logger;
	
	public NewMP3AudoReceivedRunnable(MP3SpreadsheetService ss, WorkQManager wqm, String audio) {
		super(ss, wqm, audio);
		
		logger = LogManager.getLogger(this.getClass().getSimpleName() + "|" + audio);
		logger.trace("Landing Pad Runnable Received For:" + audio);
	}
	

/*	@Override
	public void execute() {
					
		try {
			logger.info("About to update the spreadsheet to 'Ready'...");
			SpreadsheetDataRow data = getSpeakerSS().getAudioDetails(getId());
			logger.trace("spreadsheet data row:{}", data);
			if (data.getFieldValue(Field.MP3_STATE) == null || data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("")
					|| data.getFieldValue(Field.MP3_STATE).equalsIgnoreCase("Not Received")) {
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, "Receiving in Progress");
				doWork();
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
*/

	@Override
	public void doWork() {
		try {
			Thread.sleep(45000);
		} catch (InterruptedException e) {
			logger.info("Caught error while sleeping on this thread!", e);
		}
	}


	@Override
	public String getStartingStatus() {
		return "Receiving in Progress";
	}


	@Override
	public String getEndingStatus() {
		return "Upload Complete";
	}
	
	

}
