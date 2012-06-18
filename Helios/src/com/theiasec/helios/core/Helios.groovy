package com.theiasec.helios.core

import com.google.inject.Guice
import com.theiasec.helios.core.managers.SocketPacketManager
import com.theiasec.helios.core.managers.TestCommunicationManager
import com.theiasec.helios.core.managers.ModuleManager
import com.theiasec.helios.core.managers.AlertManager
import com.theiasec.helios.core.managers.ConfigurationManager
import com.theiasec.helios.core.managers.ZoneManager

@com.google.inject.Singleton
public class Helios {
	//Used for dependency injection for managers
	def injector = Guice.createInjector()
	//All managers, created for us using singleton 
	def packetManager = SocketPacketManager.getInstance()
	def communicationManager = new TestCommunicationManager()
	def moduleManager = ModuleManager.getInstance()
	def alertManager = injector.getInstance(AlertManager.class)
	def configurationManager = new ConfigurationManager("configs/config.xml", injector, moduleManager, communicationManager)
	def zoneManager = injector.getInstance(ZoneManager.class)
	
	void start() {
		//Do all the things
		moduleManager.start()

	}
	
}
