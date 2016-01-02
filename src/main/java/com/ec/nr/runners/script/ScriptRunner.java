package com.ec.nr.runners.script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.NREnvironment;

public class ScriptRunner implements Runnable {

	private static Logger logger = LogManager.getLogger(ScriptRunner.class);

	private NREnvironment nrEnvironment;

	private String scriptToRun;
	private String scriptAliasName;

	private String id;
	private File currentFile;
	private File nextFile;

	protected ScriptRunner(NREnvironment env, String scriptToRun, String scriptenAliasName, String fileId) {
		super();
		this.nrEnvironment = env;
		this.scriptToRun = scriptToRun;
		this.scriptAliasName = scriptenAliasName;
		this.id = fileId;
		setupFile();
	}

	private void setupFile() {
		int fileIndex = -1;

		if (!new File(this.nrEnvironment.WORKING_DIR + "/" + this.id + "-" + ++fileIndex + ".mp3").exists()) {
			try {
				FileUtils.copyFile
				(
					new File(this.nrEnvironment.LANDING_PAD_DIR + "/" + this.id + ".mp3")
					, new File(this.nrEnvironment.WORKING_DIR + "/" + this.id + "-" + fileIndex + ".mp3")
				);
			} catch (IOException e) {
				throw new RuntimeException("problems copying file to directory", e);
			}
		} 

		while (nextFile == null) {
			nextFile = new File(this.nrEnvironment.WORKING_DIR + "/" + this.id + "-" + ++fileIndex + ".mp3");
			if (!nextFile.exists()) {
				currentFile = new File(
						this.nrEnvironment.WORKING_DIR + "/" + this.id + "-" + (fileIndex - 1) + ".mp3");
				break;
			} else {
				nextFile = null;
			}

			if (fileIndex >= 100)
				throw new RuntimeException("I'm giving up looking for file with id:" + id);
		}
	}
	
	private String[] buildCommand() {
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, nrEnvironment.SCRIPTS_DIR + "/" + scriptToRun 
					+ " -s " + currentFile.getAbsolutePath()
					+ " -t " + nextFile.getAbsolutePath() 
					+ " > " + nrEnvironment.LOGS_DIR + "/" + this.id + "_" + this.scriptAliasName + ".log" 
			};
		
		return cmd;
	}

	@Override
	public void run() {
		try {
			Process p = Runtime.getRuntime().exec(buildCommand());
			int returnCode = p.waitFor();
			logger.info("Completed scrubbing with lame via our shell script.  Exit Value:" + returnCode);

			if (returnCode == 0) {
				// status = "Completed MP3 to WAV Conversion";
			} else {
				logger.error("problems running command:" + Arrays.toString(buildCommand()));
			}

		} catch (Throwable e) {
			logger.error("problems scrubbing the audio file with LAME: " + "", e);
			throw new Error("problems scrubbing the audio file with LAME: " + "", e);
		}

	}

}
