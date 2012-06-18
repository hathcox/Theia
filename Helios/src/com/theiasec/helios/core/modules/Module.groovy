package com.theiasec.helios.core.modulesimport com.theiasec.helios.core.communication.Packet;import com.theiasec.helios.core.managers.CommunicationManager;

abstract class Module {	CommunicationManager communicationManager	boolean isInitialized = false	boolean deviceIdSet = false	boolean printed = false	String deviceId = "0000"	String lastStatus = "0000"	String currentStatus = "0000"	Socket location	Date lastPacket		//Maps device id's to their actual Java class	static deviceTypeMap = [		"0000" : TestModule.class				]		public static Module createNewModule(Packet packet) {		Module newModule = this.deviceTypeMap[packet.deviceId].newInstance()		newModule.location = packet.source		return newModule	}		//This is passed a packet and confirms whether or not it id s valid packet for this module	abstract Packet filterPacket(Packet packet)		//This should start a module and pass various information to the communication manager	abstract void initialize(String deviceId) 
}
