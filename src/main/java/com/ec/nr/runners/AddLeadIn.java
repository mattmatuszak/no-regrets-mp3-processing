package com.ec.nr.runners;

import java.io.File;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddLeadIn extends AbstractAudioRunnable {
	
	private static Logger logger = LogManager.getLogger(AddLeadIn.class);
	
	private File wavAudio;
	private File audioWithLeadin;
	private AudioInputStream leadIn;
	
	private String status = "Failed Adding Leadin";

	public AddLeadIn(String audioName) {
		super(null, null, null);
		/*		super(audioName);
		
		wavAudio = Paths.get(workingAudioDirectory + "/" + audioName + ".wav").toFile();
		audioWithLeadin = Paths.get(workingAudioDirectory + "/" + audioName + "_withLeadin.wav").toFile();
		
		if (!wavAudio.exists()) {
			throw new Error("audio does not exist:" + wavAudio.getAbsolutePath());
		}
		
		try {
		leadIn = AudioSystem.getAudioInputStream(Paths.get(dataDirectory + "/LeadIn.wav").toFile());
		} catch (Throwable t) {
			logger.error("problems referencing teh lead in file:" + dataDirectory + "/LeadIn.wav", t);
			throw new Error("problems referencing teh lead in file:" + dataDirectory + "/LeadIn.wav", t);
		}
*/		
	}

	@Override
	public void execute() {
		
		logger.info("combining our audio files...");
		
		try {
		    AudioInputStream targeWave = AudioSystem.getAudioInputStream(wavAudio);
		    
		    logger.debug("Readable F: " + targeWave.getFormat());
		    logger.debug("Length    : " + targeWave.getFrameLength());
		    logger.debug("Channel   : " + targeWave.getFormat().getChannels());
		    logger.debug("Frame Rate: " + targeWave.getFormat().getFrameRate());
		    logger.debug("Frame Size: " + targeWave.getFormat().getFrameSize());
		    logger.debug("Encoding  : " + targeWave.getFormat().getEncoding());
		    logger.debug("----------------------------------------------------------");
		    logger.debug("Readable F: " + leadIn.getFormat());
		    logger.debug("Length    : " + leadIn.getFrameLength());
		    logger.debug("Channel   : " + leadIn.getFormat().getChannels());
		    logger.debug("Frame Rate: " + leadIn.getFormat().getFrameRate());
		    logger.debug("Frame Size: " + leadIn.getFormat().getFrameSize());
		    logger.debug("Encoding  : " + leadIn.getFormat().getEncoding());
	    	
	    	AudioInputStream appendedFiles = 
                    new AudioInputStream(
                        new SequenceInputStream(leadIn, targeWave),     
                        leadIn.getFormat(), 
                        leadIn.getFrameLength() + targeWave.getFrameLength());

		    AudioSystem.write(appendedFiles, 
	                        AudioFileFormat.Type.WAVE, 
	                        audioWithLeadin);

		    status = "Completed Adding Leadin";

	    } catch (Throwable e) {
		    throw new Error("Problems with joining the wave file with the lead-in!  Investigate!", e);
	    }
		
		logger.info("Completed combining our audio files.");

	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getSpreadsheetDurationField() {
		return "addleadinduration";
	}

}
