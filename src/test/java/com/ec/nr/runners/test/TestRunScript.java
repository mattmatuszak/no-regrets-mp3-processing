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
	public void testRunScript() {
		ScriptRunner convertToMonoRunner = runScriptFactory.getScriptRunner("convertToMono.sh", "convertToMono", "nrSilence");
		convertToMonoRunner.run();
	}
	
	@Test
	public void testRemoveSilence() {
		ScriptRunner removeSilenceRunner = runScriptFactory.getScriptRunner("removeSilence.sh", "removeSilence.sh", "nrSilence");
		removeSilenceRunner.run();
	}
	
	@Test
	public void testAmplify() {
		ScriptRunner amplifyRunner = runScriptFactory.getScriptRunner("amplify.sh", "amplify.sh", "nrQuiet");
		amplifyRunner.run();
	}
	
}
