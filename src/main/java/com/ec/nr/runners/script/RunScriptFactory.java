package com.ec.nr.runners.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ec.nr.NREnvironment;

@Component
public class RunScriptFactory {

	@Autowired private NREnvironment env;
	
	public ScriptRunner getScriptRunner(String scriptToRun, String scriptAliasName, String fileId) {
		return new ScriptRunner(env, scriptToRun, scriptAliasName, fileId);
	}
	
}
