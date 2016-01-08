package com.ec.nr.runners.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ec.nr.NRMP3AppConfig;
import com.ec.nr.runners.script.ScriptRunner;
import com.ec.nr.runners.script.RunScriptFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(NRMP3AppConfig.class)
public class TestRunScript {

	@Autowired RunScriptFactory runScriptFactory;
	
	
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
		ScriptRunner removeSilenceRunner = runScriptFactory.getScriptRunner("Convert To Mono Complete", "");
		removeSilenceRunner.run();
	}
	
	@Test
	public void testClassic() {
		ScriptRunner amplifyRunner = runScriptFactory.getScriptRunner("Remove Silence Complete", "");
		amplifyRunner.run();
	}
	
	@Test
	public void testCrazy() {
		ScriptRunner amplifyRunner = runScriptFactory.getScriptRunner("Remove Silence Complete", "");
		amplifyRunner.run();
	}
	
}
