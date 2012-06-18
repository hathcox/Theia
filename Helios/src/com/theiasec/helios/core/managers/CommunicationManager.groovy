package com.theiasec.helios.core.managers

import com.theiasec.helios.core.communication.Packet;

abstract class CommunicationManager {
	PacketManager packetManager
	static CommunicationManager communicationManager
	
	//This should send the packet through the packetManager
	abstract void sendPacket(Packet packet)
	
	//This should be overloaded for anything you do with pullPacket()
	abstract Packet recievePacket()
	
	//This is for singleton
	public static CommunicationManager getInstance() {
		//This should be overwritten
	}

}
