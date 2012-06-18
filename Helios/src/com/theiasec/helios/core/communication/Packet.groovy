package com.theiasec.helios.core.communication

import com.theiasec.helios.core.managers.PacketManager

class Packet {
	
	Date timeStamp = null
	UUID id = null
	int length = 0
	String rawData = null
	String deviceType = null
	String deviceId = null
	String priority = null
	String status = null 
	String data = null
	Socket source = null
	//Possibly irrelevant
	//Only useful if we want module <-> module communication
	//def source
	//def destination
	
	//Default constructor
	public Packet(String rawData, PacketManager packetManager, Socket source) {
		if(rawData.size() > packetManager.baseSize) {
			this.source = source
			this.rawData = rawData
			this.length = rawData.size()
			this.id = UUID.randomUUID()
			this.timeStamp = new Date()
			this.deviceType = rawData.substring(packetManager.deviceType[0],packetManager.deviceType[1])
			this.deviceId = rawData.substring(packetManager.deviceId[0],packetManager.deviceId[1])
			this.priority = rawData.substring(packetManager.priority[0],packetManager.priority[1])
			this.status = rawData.substring(packetManager.status[0],packetManager.status[1])
			this.data = rawData.substring(packetManager.data[0])
		} else {
			println "Recieved smaller than accepted packet"
		}
	}
	
	String toString() {
		return "$timeStamp - Packet - Source [\"$source\"]-[$id:$length] : $data"
	}
	
	boolean isInitializationPacket() {
		if (this.deviceId == "0000" && this.status == "0001") {
			return true
		} else {
			return false
		}
	}

}
