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
import com.ec.nr.runners.script.ScriptRunner;
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
		ScriptRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrQuiet-sample");
		convertToMono.run();
		ScriptRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrQuiet-sample");
		amplify.run();
		ScriptRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrQuiet-sample");
		removeSilence.run();
		ScriptRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrQuiet-sample");
		addLeadIn.run();
		ScriptRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrQuiet-sample");
		tagMP3.run();
	}
	
	@Test
	public void testSilence() {
		ScriptRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrSilence-sample");
		convertToMono.run();
		ScriptRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrSilence-sample");
		amplify.run();
		ScriptRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrSilence-sample");
		removeSilence.run();
		ScriptRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrSilence-sample");
		addLeadIn.run();
		ScriptRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrSilence-sample");
		tagMP3.run();
	}
	
	@Test
	public void testClassic() {
		ScriptRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrClassic-sample");
		convertToMono.run();
		ScriptRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrClassic-sample");
		amplify.run();
		ScriptRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrClassic-sample");
		removeSilence.run();
		ScriptRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrClassic-sample");
		addLeadIn.run();
		ScriptRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrClassic-sample");
		tagMP3.run();
	}
	
	@Test
	public void testCrazy() {
		ScriptRunner convertToMono = runScriptFactory.getScriptRunner("UPC", "nrCrazy-sample");
		convertToMono.run();
		ScriptRunner amplify = runScriptFactory.getScriptRunner("Remove Silence Complete", "nrCrazy-sample");
		amplify.run();
		ScriptRunner removeSilence = runScriptFactory.getScriptRunner("Convert To Mono Complete", "nrCrazy-sample");
		removeSilence.run();
		ScriptRunner addLeadIn = runScriptFactory.getScriptRunner("Amplify Complete", "nrCrazy-sample");
		addLeadIn.run();
		ScriptRunner tagMP3 = runScriptFactory.getScriptRunner("Add Lead In Complete", "nrCrazy-sample");
		tagMP3.run();
	}
	
}
