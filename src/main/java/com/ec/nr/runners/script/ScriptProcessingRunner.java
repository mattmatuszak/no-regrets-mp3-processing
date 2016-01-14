package com.ec.nr.runners.script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ec.nr.NREnvironment;
import com.ec.nr.runners.MP3Runnable;
import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;

public class ScriptProcessingRunner implements Runnable, MP3Runnable {

	private static Logger logger = LogManager.getLogger(ScriptProcessingRunner.class);

	private SpeakerSpreadsheet spreadsheet;
	
	private NREnvironment nrEnvironment;
	private ScriptInfo scriptInfo;

	private File currentFile;
	private File nextFile;

	protected ScriptProcessingRunner(NREnvironment env, ScriptInfo info, SpeakerSpreadsheet spreadsheet) {
		super();
		this.nrEnvironment = env;
		this.scriptInfo = info;
		this.spreadsheet = spreadsheet;
		setupWorkingFiles();
		updateStatus(this.scriptInfo.getId(), this.scriptInfo.getScriptAliasName() + " Queued");
	}
	
	private void updateStatus(String id, String value) {
		spreadsheet.updateField(id, SpreadsheetDataRow.Field.MP3_STATE, value);
	}
	
	protected NREnvironment getNREnvironment() {
		return nrEnvironment;
	}
	
	protected ScriptInfo getScriptInfo() {
		return scriptInfo;
	}
	
	protected File getCurrentFile() {
		return currentFile;
	}

	protected void setupWorkingFiles() {
		int fileIndex = -1;

		if (!new File(this.nrEnvironment.WORKING_DIR + "/" + this.scriptInfo.getId() + "-" + ++fileIndex + ".mp3").exists()) {
			try {
				FileUtils.copyFile
				(
					new File(this.nrEnvironment.LANDING_PAD_DIR + "/" + this.scriptInfo.getId() + ".mp3")
					, new File(this.nrEnvironment.WORKING_DIR + "/" + this.scriptInfo.getId() + "-" + fileIndex + ".mp3")
				);
			} catch (IOException e) {
				throw new RuntimeException("problems copying file to directory", e);
			}
		} 

		while (nextFile == null) {
			nextFile = new File(this.nrEnvironment.WORKING_DIR + "/" + this.scriptInfo.getId() + "-" + ++fileIndex + ".mp3");
			if (!nextFile.exists()) {
				currentFile = new File(
						this.nrEnvironment.WORKING_DIR + "/" + this.scriptInfo.getId() + "-" + (fileIndex - 1) + ".mp3");
				break;
			} else {
				nextFile = null;
			}

			if (fileIndex >= 100)
				throw new RuntimeException("I'm giving up looking for file with id:" + this.scriptInfo.getId());
		}
	}
	
	protected String[] buildCommand() {
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, nrEnvironment.SCRIPTS_DIR + "/" + this.scriptInfo.getScriptToRun() 
					+ " -s " + currentFile.getAbsolutePath()
					+ " -t " + nextFile.getAbsolutePath() 
					+ ((this.scriptInfo.getExtraScriptParameters() != null) ? this.scriptInfo.getExtraScriptParameters() : "")
					+ " > " + nrEnvironment.LOGS_DIR + "/" + this.scriptInfo.getId() + "_" + this.scriptInfo.getScriptAliasName() + ".log" 
			};
		
		return cmd;
	}

	@Override
	public void run() {
		try {
			logger.info("Running command:" + buildCommand()[2]);
			updateStatus(this.scriptInfo.getId(), this.scriptInfo.getScriptAliasName() + " In Progress");
			Process p = Runtime.getRuntime().exec(buildCommand());
			int returnCode = p.waitFor();
			logger.info("Completed running shell script.  Exit Value:" + returnCode);

			if (returnCode == 0) {
				// status = "Completed MP3 to WAV Conversion";
				updateStatus(this.scriptInfo.getId(), this.scriptInfo.getScriptAliasName() + " Complete");
			} else {
				logger.error("problems running command:" + Arrays.toString(buildCommand()));
			}

		} catch (Throwable e) {
			logger.error("problems scrubbing the audio file with LAME: " + "", e);
			updateStatus(this.scriptInfo.getId(), this.scriptInfo.getScriptAliasName() + " Errored");
			throw new Error("problems scrubbing the audio file with LAME: " + "", e);
		}

	}

	@Override
	public String getId() {
		return this.scriptInfo.getId();
	}

}
