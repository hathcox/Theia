package com.theiasec.helios.core.managers

import com.theiasec.helios.core.communication.Packet


/*
 * This communication manager connects to a port and should model some basic behavior
 */

@com.google.inject.Singleton
class TestCommunicationManager extends CommunicationManager {

	private TestCommunicationManager() {
		//Private for singleton
		this.packetManager = SocketPacketManager.getInstance()
	}
	
	@Override
	public static CommunicationManager getInstance() {
		if(communicationManager == null) {
			communicationManager = new TestCommunicationManager()
		}
		return communicationManager
	}
	
	@Override
	public void sendPacket(Packet packet) {
		//println "Sending Packet... [$packet]"
		packetManager.sendThread.writePacket(packet)
		Thread.sleep(100);
	}

	@Override
	public Packet recievePacket() {
		//Grab a packet from the queue
		//This is destructive
		return packetManager.recieveThread.readPacket()
	}

}
