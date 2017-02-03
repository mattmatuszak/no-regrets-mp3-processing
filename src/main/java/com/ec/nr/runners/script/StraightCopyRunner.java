package com.ec.nr.runners.script;

import java.io.File;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;

public class StraightCopyRunner extends CopyRunner {

	protected StraightCopyRunner(DirectoryInfo dirInfo, ScriptInfo info, MP3SpreadsheetService spreadsheet) {
		super(dirInfo, info, spreadsheet);
	}
	
	@Override
	protected void setupCurrentAndNextFiles(int fileIndex) {
		setCurrentFile(new File(getDirectoryInfo().getSourceDirectory() + "/" + getId() + ".mp3"));
		setNextFile(new File(getDirectoryInfo().getWorkingDirectory() + "/" + getId() + ".mp3"));
	}

}
