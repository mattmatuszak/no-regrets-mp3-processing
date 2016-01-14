package com.ec.nr.runners.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.ec.nr.NREnvironment;
import com.ec.nr.NRMP3AppConfig;
import com.ec.nr.runners.script.ScriptProcessingRunner;
import com.ec.nr.runners.script.RunScriptFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(NRMP3AppConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TestRunScript.class })
public class TestRunScript extends AbstractTestExecutionListener {

	@Autowired RunScriptFactory runScriptFactory;
	
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		super.beforeTestClass(testContext);
		
		NREnvironment env = testContext.getApplicationContext().getBean(NREnvironment.class);
		
		FileUtils.cleanDirectory(new File(env.WORKING_DIR));
		FileUtils.cleanDirectory(new File(env.LOGS_DIR));
	}

	@Test
	public void testQuiet() {
		ScriptProcessingRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrQuiet-sample");
		convertToMono.run();
		ScriptProcessingRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrQuiet-sample");
		amplify.run();
		ScriptProcessingRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrQuiet-sample");
		removeSilence.run();
		ScriptProcessingRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrQuiet-sample");
		addLeadIn.run();
		ScriptProcessingRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrQuiet-sample");
		tagMP3.run();
		ScriptProcessingRunner copy = runScriptFactory.getScriptRunner("Tag MP3 Complete", "nrQuiet-sample");
		copy.run();
		ScriptProcessingRunner ftp = runScriptFactory.getScriptRunner("Copy To Final Complete", "nrQuiet-sample");
		ftp.run();
	}
	
	@Test
	public void testSilence() {
		ScriptProcessingRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrSilence-sample");
		convertToMono.run();
		ScriptProcessingRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrSilence-sample");
		amplify.run();
		ScriptProcessingRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrSilence-sample");
		removeSilence.run();
		ScriptProcessingRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrSilence-sample");
		addLeadIn.run();
		ScriptProcessingRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrSilence-sample");
		tagMP3.run();
	}
	
	@Test
	public void testClassic() {
		ScriptProcessingRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrClassic-sample");
		convertToMono.run();
		ScriptProcessingRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrClassic-sample");
		amplify.run();
		ScriptProcessingRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrClassic-sample");
		removeSilence.run();
		ScriptProcessingRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrClassic-sample");
		addLeadIn.run();
		ScriptProcessingRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrClassic-sample");
		tagMP3.run();
	}
	
	@Test
	public void testCrazy() {
		ScriptProcessingRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrCrazy-sample");
		convertToMono.run();
		ScriptProcessingRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrCrazy-sample");
		amplify.run();
		ScriptProcessingRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrCrazy-sample");
		removeSilence.run();
		ScriptProcessingRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrCrazy-sample");
		addLeadIn.run();
		ScriptProcessingRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrCrazy-sample");
		tagMP3.run();
	}
	
}
