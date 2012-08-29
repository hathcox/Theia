package com.theiasec.helios.core.tests;

import com.theiasec.helios.core.managers.TestCommunicationManager
import com.theiasec.helios.core.modules.TestModule

import groovy.util.GroovyTestCase;

class TestModules extends GroovyTestCase {

	TestModule testModuleOne
	TestModule testModuleTwo
	
	
	protected void setUp() throws Exception {
		super.setUp();
		testModuleOne = new TestModule()
		testModuleTwo = new TestModule()
	}
	
	public void testAllModules() {
		assert testModuleOne.communicationManager == testModuleTwo.communicationManager
	}

}
