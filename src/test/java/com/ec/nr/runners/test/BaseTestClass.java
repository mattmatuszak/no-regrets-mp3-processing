package com.ec.nr.runners.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.ec.nr.NREnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TestRunScript.class })
@TestPropertySource(locations={"classpath:testapplication.properties"})
public abstract class BaseTestClass extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		super.beforeTestClass(testContext);
		
		NREnvironment env = testContext.getApplicationContext().getBean(NREnvironment.class);
		
		FileUtils.cleanDirectory(new File(env.PRE_EDIT_DIR));
		FileUtils.cleanDirectory(new File(env.USER_EDIT_DIR));
		FileUtils.cleanDirectory(new File(env.POST_EDIT_DIR));
		FileUtils.cleanDirectory(new File(env.LOGS_DIR));
		FileUtils.cleanDirectory(new File(env.FINAL_AUDIO_DIR));
		//FileUtils.cleanDirectory(new File(env.SCRIPTS_DIR));
	}
	
	
	
}
