package com.ec.nr.runners.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.ec.nr.NREnvironment;

public class TestAppExtract extends BaseTestClass {

	@Autowired NREnvironment env;
	
	@Test
	public void testScriptExtract() throws Exception {
		
		Resource[] scripts = 
				(new PathMatchingResourcePatternResolver()).getResources("classpath:/scripts/*.sh");
		
		for (int scriptIndex = 0; scriptIndex < scripts.length; scriptIndex++) {
			Resource scriptReference = scripts[scriptIndex];
			
			File script = new File(env.SCRIPTS_DIR + "/" + scriptReference.getFilename());
			assertTrue("Script " + scriptReference.getFilename() + " does not exist", script.exists());
		}
		
	}
	
	@Test
	public void testDataExtract() throws Exception {
		Resource[] dataResources = 
				(new PathMatchingResourcePatternResolver()).getResources("classpath:/data/*.*");
		
		for (int dataResoruceIndex = 0; dataResoruceIndex < dataResources.length; dataResoruceIndex++) {
			Resource dataFileReference = dataResources[dataResoruceIndex];
			
			File dataFile = new File(env.DATA_DIR + "/" + dataFileReference.getFilename());
			assertTrue("Data file " + dataFileReference.getFilename() + " does not exist", dataFile.exists());
		}
	}
	
}
