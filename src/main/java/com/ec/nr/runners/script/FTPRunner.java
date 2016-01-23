package com.ec.nr.runners.script;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;

public class FTPRunner extends ScriptProcessingRunner {

	protected FTPRunner(DirectoryInfo dirInfo, ScriptInfo info, MP3SpreadsheetService spreadsheet) {
		super(dirInfo, info, spreadsheet);
	}
	
	@Override
	protected void setupWorkingFiles() {};
	
	@Override
	protected String[] buildCommand() {
		
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, getDirectoryInfo().getScriptDirectory() + "/" + getScriptInfo().getScriptToRun() 
					+ ((getScriptInfo().getExtraScriptParameters() != null) ? getScriptInfo().getExtraScriptParameters() : "")
					+ " > " + getDirectoryInfo().getLogsDirectory() + "/" + getScriptInfo().getId() + "_" + getScriptInfo().getScriptAliasName().replaceAll(" ",  "") + ".log" 
			};
		
		return cmd;
	}

	
	
}
