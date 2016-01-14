package com.ec.nr.runners.script;

import com.ec.nr.NREnvironment;
import com.ec.nr.sheets.creds.SpeakerSpreadsheet;

public class FTPRunner extends ScriptProcessingRunner {

	protected FTPRunner(NREnvironment env, ScriptInfo info, SpeakerSpreadsheet spreadsheet) {
		super(env, info, spreadsheet);
	}
	
	
	@Override
	protected String[] buildCommand() {
		
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, getNREnvironment().SCRIPTS_DIR + "/" + getScriptInfo().getScriptToRun() 
					+ ((getScriptInfo().getExtraScriptParameters() != null) ? getScriptInfo().getExtraScriptParameters() : "")
					+ " -u " + getNREnvironment().FTP_U
					+ " -p " + getNREnvironment().FTP_P
					+ " > " + getNREnvironment().LOGS_DIR + "/" + getScriptInfo().getId() + "_" + getScriptInfo().getScriptAliasName() + ".log" 
			};
		
		return cmd;
	}

	
	
}
