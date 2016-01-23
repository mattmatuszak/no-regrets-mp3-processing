package com.ec.nr.runners;

import org.apache.logging.log4j.Logger;

public class ConvertMP3ToWav extends AbstractAudioRunnable {

	private static Logger logger;
	
	private String mp3Audio;
	private String wavAudio;
	
	private String status = "Failed MP3 to WAV Conversion";
	
	
	public ConvertMP3ToWav(String audioId) {
		super(null, null, null);
/*		super(audioId);
		
		File audio = Paths.get(landingPadDirectory + "/" + audioId + ".mp3").toFile();
		
		if (!audio.exists()) {
			throw new Error("audio does not exist:" + audio.getAbsolutePath());
		}
		
		logger = LogManager.getLogger(this.getClass().getSimpleName() + "|" + audioId);
		logger.trace("Landing Pad Runnable Received For:" + audio.getName());
		
		mp3Audio = audio.getAbsolutePath();
		wavAudio = workingAudioDirectory + "/" + audioId + ".wav";
*/		
	}
	
	@Override
	public void execute() {
/*		logger.debug("converting mp3 to wav...");
		
		String[] cmd = 
			{ 
				"/bin/sh"
				, "-c", scriptsDirectory + "/convertMP3ToWAV.sh "
					+ " -m " + mp3Audio
					+ " -w " + wavAudio
					+ " > " + logsDirectory + "/" + getId() + "_mp3ToWave.log"
			};
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			int returnCode = p.waitFor();
			logger.info("Completed scrubbing with lame via our shell script.  Exit Value:" + returnCode);
			
			if (returnCode == 0) {
				status = "Completed MP3 to WAV Conversion";
			}
			
			
		} catch (Throwable e) {
			logger.error("problems scrubbing the audio file with LAME: " + mp3Audio, e);
			throw new Error("problems scrubbing the audio file with LAME: " + mp3Audio, e);
		}
		
		logger.debug("finsished converting mp3 to wave.");
*/		
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getSpreadsheetDurationField() {
		return "mp3towavconversionduration";
	}

}
