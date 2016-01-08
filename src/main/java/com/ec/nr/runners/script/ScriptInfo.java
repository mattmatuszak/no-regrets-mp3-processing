package com.ec.nr.runners.script;

public class ScriptInfo {

	private String scriptToRun;
	private String scriptAliasName;
	private String id;
	private String extraScriptParameters;
	public ScriptInfo(String scriptToRun, String scriptAliasName, String id, String extraScriptParameters) {
		super();
		this.scriptToRun = scriptToRun;
		this.scriptAliasName = scriptAliasName;
		this.id = id;
		this.extraScriptParameters = extraScriptParameters;
	}
	public String getScriptToRun() {
		return scriptToRun;
	}
	public void setScriptToRun(String scriptToRun) {
		this.scriptToRun = scriptToRun;
	}
	public String getScriptAliasName() {
		return scriptAliasName;
	}
	public void setScriptAliasName(String scriptAliasName) {
		this.scriptAliasName = scriptAliasName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExtraScriptParameters() {
		return extraScriptParameters;
	}
	public void setExtraScriptParameters(String extraScriptParameters) {
		this.extraScriptParameters = extraScriptParameters;
	}
	
	
	
}
