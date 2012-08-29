package com.theiasec.helios.core

import com.theiasec.helios.core.alerts.AlertListener
import com.theiasec.helios.core.managers.SocketPacketManager
import com.theiasec.helios.core.managers.TestCommunicationManager
import com.theiasec.helios.core.managers.ModuleManager
import com.theiasec.helios.core.managers.AlertManager
import com.theiasec.helios.core.managers.ConfigurationManager
import com.theiasec.helios.core.managers.ZoneManager

public class Helios {
	//All managers, created for us using singleton 
	def packetManager = SocketPacketManager.getInstance()
	def communicationManager = new TestCommunicationManager()
	def moduleManager = ModuleManager.getInstance()
	def alertManager = AlertManager.getInstance()
	def configurationManager = new ConfigurationManager("configs/config.xml")
	def zoneManager = ZoneManager.getInstance()
	
	void start() {
		//Do all the things
		moduleManager.start()

	}
	
	//Conveince method to add listeners
	public void addAlertListener(AlertListener listener) {
		alertManager.addAlertListener(listener)
	}
	
}
