package com.ec.nr.runners;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;

public class NewMP3AudoReceivedRunnable extends AbstractAudioRunnable {

	private static Logger logger;
	
	private File audioFile;
	
	public NewMP3AudoReceivedRunnable(String audio) {
		super(audio);
		
		audioFile = Paths.get(landingPadDirectory + "/" + audio + ".mp3").toFile();
		
		if (!audioFile.exists()) {
			throw new Error("audio does not exist:" + audioFile.getAbsolutePath());
		}
		
		logger = LogManager.getLogger(this.getClass().getSimpleName() + "|" + audio);
		logger.trace("Landing Pad Runnable Received For:" + audio);
	}
	

	@Override
	public void execute() {
					
		try {
			logger.info("About to update the spreadsheet to 'Ready'...");
			HashMap<String, String> data = null;// getSpeakerSS().getAudioDetails(getId());
			if (data.get("mp3State") == null || data.get("mp3State").equalsIgnoreCase("")) {
				getSpeakerSS().updateField(getId(), SpreadsheetDataRow.Field.MP3_STATE, "In Landing Pad");
			} else {
				logger.debug("Ignoring MP3:" + getId());
			}
			
			logger.info("finished updating spreadsheet");
		} catch (Throwable th) {
			logger.error("problems updating spreadsheet for MP3:" + audioFile.getAbsolutePath(), th);
			throw new Error("problems updating spreadsheet for MP3:" + audioFile.getAbsolutePath(), th);
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
