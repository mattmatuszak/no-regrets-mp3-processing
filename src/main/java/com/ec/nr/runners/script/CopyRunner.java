package com.ec.nr.runners.script;

import java.io.File;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;

public class CopyRunner extends ScriptProcessingRunner {

	protected CopyRunner(DirectoryInfo dirInfo, ScriptInfo info, MP3SpreadsheetService spreadsheet) {
		super(dirInfo, info, spreadsheet);
	}
	
	@Override
	protected void copyBaseFileIfNotExists(int fileIndex) {};
	
	@Override
	protected void setupCurrentAndNextFiles(int fileIndex) {
		setNextFile(new File(getDirectoryInfo().getWorkingDirectory() + "/" + getId() + ".mp3"));
		File currentFile = getCurrentFile();
		
		while (currentFile == null) {
			currentFile =  new File(getDirectoryInfo().getSourceDirectory() + "/" + getId() + "-" + ++fileIndex + ".mp3");
			if (!currentFile.exists()) {
				currentFile = new File(
						getDirectoryInfo().getSourceDirectory() + "/" + getId() + "-" + (fileIndex - 1) + ".mp3");
				break;
			} else {
				currentFile = null;
			}

			if (fileIndex >= 100)
				throw new RuntimeException("I'm giving up looking for file with id:" + getId());
		}
		
		setCurrentFile(currentFile);
	}
	
	
	@Override
	protected String[] buildCommand() {
		
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, getDirectoryInfo().getScriptDirectory() + "/" + getScriptInfo().getScriptToRun() 
					+  " -s " + getCurrentFile().getPath()
					+  " -t " + getNextFile().getPath()
					+ " > " + getDirectoryInfo().getLogsDirectory() + "/" + getScriptInfo().getId() + "_" + getScriptInfo().getScriptAliasName().replaceAll(" ", "") + ".log" 
			};
		
		return cmd;
	}

	
	
}
