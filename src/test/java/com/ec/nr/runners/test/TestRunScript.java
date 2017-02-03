package com.ec.nr.runners.test;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ec.nr.runners.MP3Runnable;
import com.ec.nr.runners.script.RunScriptFactory;
import com.ec.nr.runners.script.ScriptProcessingRunner;


@Ignore
public class TestRunScript extends BaseTestClass {

	@Autowired RunScriptFactory runScriptFactory;
	
	@Test
	public void testQuiet() {
		MP3Runnable convertToMono = runScriptFactory.getScriptRunner("Upload Complete", "nrQuiet-sample");
		convertToMono.run();
		MP3Runnable amplify = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrQuiet-sample");
		amplify.run();
		MP3Runnable removeSilence = runScriptFactory.getScriptRunner("Amplify Complete", "nrQuiet-sample");
		removeSilence.run();
		MP3Runnable copyToUserEdit = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrQuiet-sample");
		copyToUserEdit.run();
		MP3Runnable addLeadIn = runScriptFactory.getScriptRunner("D", "nrQuiet-sample");
		addLeadIn.run();
		MP3Runnable tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrQuiet-sample");
		tagMP3.run();
		MP3Runnable copy = runScriptFactory.getScriptRunner("Tag MP3 Complete", "nrQuiet-sample");
		copy.run();
		MP3Runnable ftp = runScriptFactory.getScriptRunner("Copy To Final Complete", "nrQuiet-sample");
		ftp.run();
	}
	
	@Test
	public void testSilence() {
		MP3Runnable convertToMono = runScriptFactory.getScriptRunner("Upload Complete", "nrSilence-sample");
		convertToMono.run();
		MP3Runnable amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrSilence-sample");
		amplify.run();
		MP3Runnable removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrSilence-sample");
		removeSilence.run();
		MP3Runnable addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrSilence-sample");
		addLeadIn.run();
		MP3Runnable tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrSilence-sample");
		tagMP3.run();
	}
	
	@Test
	public void testClassic() {
		MP3Runnable convertToMono = runScriptFactory.getScriptRunner("Upload Complete", "nrClassic-sample");
		convertToMono.run();
		MP3Runnable amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrClassic-sample");
		amplify.run();
		MP3Runnable removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrClassic-sample");
		removeSilence.run();
		MP3Runnable addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrClassic-sample");
		addLeadIn.run();
		MP3Runnable tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrClassic-sample");
		tagMP3.run();
	}
	
	@Test
	public void testCrazy() {
		MP3Runnable convertToMono = runScriptFactory.getScriptRunner("Upload Complete", "nrCrazy-sample");
		convertToMono.run();
		MP3Runnable amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrCrazy-sample");
		amplify.run();
		MP3Runnable removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrCrazy-sample");
		removeSilence.run();
		MP3Runnable addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrCrazy-sample");
		addLeadIn.run();
		MP3Runnable tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrCrazy-sample");
		tagMP3.run();
	}
	
}
