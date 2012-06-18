package com.theiasec.helios.core.tests;

import com.google.inject.Guice
import com.theiasec.helios.core.managers.TestCommunicationManager
import com.theiasec.helios.core.modules.TestModule

import groovy.util.GroovyTestCase;

class TestModules extends GroovyTestCase {

	TestModule testModuleOne
	TestModule testModuleTwo
	def injector = Guice.createInjector()
	
	
	protected void setUp() throws Exception {
		super.setUp();
		testModuleOne = new TestModule(injector.getInstance(TestCommunicationManager.class))
		testModuleTwo = new TestModule(injector.getInstance(TestCommunicationManager.class))
	}
	
	public void testAllModules() {
		assert testModuleOne.communicationManager == testModuleTwo.communicationManager
	}

}
