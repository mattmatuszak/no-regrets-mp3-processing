package com.ec.nr.runners;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertToMP3 extends AbstractAudioRunnable {
	
	private static Logger logger = LogManager.getLogger(ConvertToMP3.class);
	
	private String status = "Failed to Convert to MP3";
	
	private File audioWithLeadin;

	public ConvertToMP3(String audioName) {
		super(null, null, null);
		/*super(audioName);
		
		audioWithLeadin = Paths.get(workingAudioDirectory + "/" + audioName + "_withLeadin.wav").toFile();
		
		if (!audioWithLeadin.exists()) {
			throw new Error("audio does not exist:" + audioWithLeadin.getAbsolutePath());
		}*/
	}

	@Override
	public void execute() {
		
		/*logger.info("finalizing our audio via our shell script...");
		
		HashMap<String, String> audioTags = getSpeakerSS().getAudioDetails(getId());
		
		String[] cmd = 
			{ 
				"/bin/sh"
				, "-c", scriptsDirectory + "/finalizeScrubbedAudio.sh "
					+ " -t '" + audioTags.get("seminartitle") + "'"
					+ " -a '" + audioTags.get("seminaralbum") + "'"
					+ " -r '" + audioTags.get("seminarartist") + "'"
					+ " -v " + audioWithLeadin.getAbsolutePath()
					+ " -i " + dataDirectory + "/NR_Image.PNG" 
					+ " -f " + finalAudioDirectory + "/" + getId() + ".mp3"
					+ " > " + logsDirectory + "/" + getId() + "_finalizeMP3.log"
			};
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			int exitCode = p.waitFor();
			logger.info("Completed finalizing our audio via our shell script.  Exit Value:" + exitCode);
			
			if (exitCode == 0) {
				status = "Completed Converting to MP3";
			}
		} catch (Throwable e) {
			logger.error("problems finalizing the audio file with LAME: " + audioWithLeadin.getAbsolutePath(), e);
			throw new Error("problems scrubbing the audio file with LAME: " + audioWithLeadin.getAbsolutePath(), e);
		}
		
		logger.info("finished finalizing our audio via our shell script.");
		*/
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getSpreadsheetDurationField() {
		return "convertbacktomp3duration";
	}

}
