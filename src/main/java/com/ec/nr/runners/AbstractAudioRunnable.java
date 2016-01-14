package com.ec.nr.runners;

import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.workq.WorkQManager;

public abstract class AbstractAudioRunnable extends AudioRunnersBase implements
		MP3Runnable {
	
	private static Logger logger = LogManager.getLogger(AbstractAudioRunnable.class);
	private String id;
	
	public abstract void execute();
	public abstract String getStatus();
	
	public abstract String getSpreadsheetDurationField();
	
	public AbstractAudioRunnable(SpeakerSpreadsheet ss, WorkQManager wqm, String audioName) {
		super(ss, wqm);
		
		id = audioName;
	}

	@Override
	public void run() {
		
		logger.info("Executing runnable for mp3: " + getId() + "...");
		
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		execute();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		
		String newStatus = getStatus();
		logger.info("New MP3 Status:" + newStatus);
		
		if (newStatus != null) {
		
			//getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, newStatus);
			//getSpeakerSS().updateField(getId(), getSpreadsheetDurationField(), String.valueOf(endTime-startTime));
			//getSpeakerSS().updateBreakoutStatus(getId(), newStatus);
			//getSpeakerSS().updateBreakoutStatus(getId(), newStatus);
		}
		
		getWorkQManager().audioComplete(this);
		
		
		logger.info("Completed executing runnable for mp3: " + getId() + ".");
		
		
	}
	
	
	@Override
	public String getId() {
		
		return id;
	}

}
