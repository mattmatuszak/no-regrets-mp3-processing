package com.ec.nr.runners.script;

public class DirectoryInfo {

	private String workingDirectory;
	private String sourceDirectory;
	private String scriptDirectory;
	private String logsDirectory;
	
	/**
	 * 
	 * @param sourceDirectory
	 * @param workingDirectory
	 * @param scriptDirectory
	 * @param logsDirectory
	 */
	public DirectoryInfo(String sourceDirectory, String workingDirectory, String scriptDirectory, String logsDirectory) {
		this.workingDirectory = workingDirectory;
		this.sourceDirectory = sourceDirectory;
		this.scriptDirectory = scriptDirectory;
		this.logsDirectory = logsDirectory;
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public String getScriptDirectory() {
		return scriptDirectory;
	}

	public String getLogsDirectory() {
		return logsDirectory;
	}
	
	
	
}
