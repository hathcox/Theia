package com.theiasec.helios.core

import com.theiasec.helios.core.alerts.AlertListener
import com.theiasec.helios.core.managers.SocketPacketManager
import com.theiasec.helios.core.managers.TestCommunicationManager
import com.theiasec.helios.core.managers.ModuleManager
import com.theiasec.helios.core.managers.AlertManager
import com.theiasec.helios.core.managers.ConfigurationManager
import com.theiasec.helios.core.managers.ZoneManager

public class Helios {
	
	static Helios helios
	Date lastDisarm = new Date()
	
	// How long until we should consider the system re-armed in milliseconds
	long disarmDurration = 30 * 1000
	
	//All managers, created for us using singleton 
	def packetManager = SocketPacketManager.getInstance()
	def communicationManager = new TestCommunicationManager()
	def moduleManager = ModuleManager.getInstance()
	def alertManager = AlertManager.getInstance()
	def configurationManager = new ConfigurationManager("configs/config.xml")
	def zoneManager = ZoneManager.getInstance()
	
	/**
	 * This will check if time has passed enough and wheter or not the system is still disarmed
	 *  true -> system disarmed
	 *  false -> system enabled
	 * @return wether or not helios is disarmed
	 */
	public boolean checkDisarmed() {
		Date now = new Date()
		//If we still have time before the system is disarmed
		if (lastDisarm?.time+disarmDurration > now.time) {
			return true
		}
		return false
	}
	
	private Helios() {
		//Private for singleton
	}
	
	public static Helios getInstance() {
		if(helios == null) {
			helios = new Helios()
		}
		return helios
	}
	
	void start() {
		//Do all the things
		moduleManager.start()

	}
	
	//Disarm the system
	void disarm() {
		println "[**] Helios disarmed for ["+disarmDurration/1000+"] seconds!"
		lastDisarm = new Date()
	}
	
	//Conveince method to add listeners
	public void addAlertListener(AlertListener listener) {
		alertManager.addAlertListener(listener)
	}
	
}
