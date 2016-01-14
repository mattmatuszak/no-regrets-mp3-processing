package com.ec.nr.runners.script;

import com.ec.nr.NREnvironment;
import com.ec.nr.sheets.creds.SpeakerSpreadsheet;

public class CopyToFinalRunner extends ScriptProcessingRunner {

	protected CopyToFinalRunner(NREnvironment env, ScriptInfo info, SpeakerSpreadsheet spreadsheet) {
		super(env, info, spreadsheet);
	}
	
	
	@Override
	protected String[] buildCommand() {
		
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, getNREnvironment().SCRIPTS_DIR + "/" + getScriptInfo().getScriptToRun() 
					+  " -s " + getCurrentFile().getPath()
					+ ((getScriptInfo().getExtraScriptParameters() != null) ? getScriptInfo().getExtraScriptParameters() : "")
					+ " > " + getNREnvironment().LOGS_DIR + "/" + getScriptInfo().getId() + "_" + getScriptInfo().getScriptAliasName() + ".log" 
			};
		
		return cmd;
	}

	
	
}
