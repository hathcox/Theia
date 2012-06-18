package com.theiasec.helios.core.managers

import com.theiasec.helios.core.communication.RecieveThread
import com.theiasec.helios.core.communication.SendThread

abstract class PacketManager {

	SendThread sendThread
	RecieveThread recieveThread
	static PacketManager packetManager
	
	def deviceType = []
	def deviceId = []
	def priority = []
	def status = []
	def data = []
	def baseSize
	
	//For singleton
	public static PacketManager getInstance() {
		//Do nothing, this should be implemented
	}
	
}
