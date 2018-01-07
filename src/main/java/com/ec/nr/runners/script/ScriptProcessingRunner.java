package com.ec.nr.runners.script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.runners.MP3Runnable;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;
import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;

public class ScriptProcessingRunner implements Runnable, MP3Runnable {

	private static Logger logger = LogManager.getLogger(ScriptProcessingRunner.class);

	private MP3SpreadsheetService spreadsheetService;
	
	private DirectoryInfo directoryInfo;
	private ScriptInfo scriptInfo;

	private File currentFile;
	private File nextFile;

	protected ScriptProcessingRunner(DirectoryInfo directoryInfo, ScriptInfo info, MP3SpreadsheetService spreadsheet) {
		super();
		this.directoryInfo = directoryInfo;
		this.scriptInfo = info;
		this.spreadsheetService = spreadsheet;
		setupWorkingFiles();
		updateStatus(this.scriptInfo.getScriptAliasName() + " Queued");
	}
	
	protected void updateStatus(String value) {
		spreadsheetService.updateField(this.getId(), SpreadsheetDataRow.Field.MP3_STATE, value);
	}
	
	protected void updateDuration(Long duration) {
		
		Field field = Field.fromUserFriendlyName(this.getScriptInfo().getScriptAliasName());
		if (field != null)
			spreadsheetService.updateField(this.getId(), field, String.valueOf(duration));
	}
	
	protected DirectoryInfo getDirectoryInfo() {
		return directoryInfo;
	}
	
	protected ScriptInfo getScriptInfo() {
		return scriptInfo;
	}
	
	protected void setCurrentFile(File f) {
		this.currentFile = f;
	}
	
	protected File getCurrentFile() {
		return currentFile;
	}
	
	protected void setNextFile(File f) {
		this.nextFile = f;
	}
	
	protected File getNextFile() {
		return nextFile;
	}
	
	protected void copyBaseFileIfNotExists(int fileIndex) {
		if (!new File(directoryInfo.getWorkingDirectory() + "/" + this.scriptInfo.getId() + "-" + fileIndex + ".mp3").exists()) {
			try {
				FileUtils.copyFile
				(
					new File(directoryInfo.getSourceDirectory() + "/" + this.scriptInfo.getId() + ".mp3")
					, new File(directoryInfo.getWorkingDirectory() + "/" + this.scriptInfo.getId() + "-" + fileIndex + ".mp3")
				);
			} catch (IOException e) {
				throw new RuntimeException("problems copying file to directory", e);
			}
		}
	}
	
	protected void setupCurrentAndNextFiles(int fileIndex) {
		while (nextFile == null) {
			nextFile = new File(directoryInfo.getWorkingDirectory() + "/" + this.scriptInfo.getId() + "-" + ++fileIndex + ".mp3");
			if (!nextFile.exists()) {
				currentFile = new File(
						directoryInfo.getWorkingDirectory() + "/" + this.scriptInfo.getId() + "-" + (fileIndex - 1) + ".mp3");
				break;
			} else {
				nextFile = null;
			}

			if (fileIndex >= 100)
				throw new RuntimeException("I'm giving up looking for file with id:" + this.scriptInfo.getId());
		}
	}

	protected void setupWorkingFiles() {
		int fileIndex = -1;
		copyBaseFileIfNotExists(++fileIndex);
		setupCurrentAndNextFiles(fileIndex);
	}
	
	protected String[] buildCommand() {
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, directoryInfo.getScriptDirectory() + "/" + this.scriptInfo.getScriptToRun() 
					+ " -s " + currentFile.getAbsolutePath()
					+ " -t " + nextFile.getAbsolutePath() 
					+ ((this.scriptInfo.getExtraScriptParameters() != null) ? this.scriptInfo.getExtraScriptParameters() : "")
					+ " > " + directoryInfo.getLogsDirectory() + "/" + this.scriptInfo.getId() + "_" + this.scriptInfo.getScriptAliasName().replaceAll(" ",  "") + ".log" 
			};
		
		return cmd;
	}

	@Override
	public void run() {
		try {
			logger.info("Running command:" + buildCommand()[2]);
			updateStatus(this.scriptInfo.getScriptAliasName() + " In Progress");
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();
			Process p = Runtime.getRuntime().exec(buildCommand());
			int returnCode = p.waitFor();
			long endTime = GregorianCalendar.getInstance().getTimeInMillis();
			logger.info("Completed running shell script.  Exit Value:" + returnCode + " after " + (endTime-startTime));
			updateDuration((endTime-startTime));
			
			if (returnCode == 0) {
				// status = "Completed MP3 to WAV Conversion";
				updateStatus(this.scriptInfo.getScriptAliasName() + " Complete");
			} else {
				logger.error("problems running command:" + Arrays.toString(buildCommand()));
			}

		} catch (Throwable e) {
			logger.error("problems scrubbing the audio file with LAME: " + "", e);
			updateStatus(this.scriptInfo.getScriptAliasName() + " Errored");
			throw new Error("problems scrubbing the audio file with LAME: " + "", e);
		}

	}

	@Override
	public String getId() {
		return this.scriptInfo.getId();
	}

}
