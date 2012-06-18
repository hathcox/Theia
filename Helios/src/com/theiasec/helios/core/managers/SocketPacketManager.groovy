package com.theiasec.helios.core.managers

import com.theiasec.helios.core.communication.SocketReceiveThread;
import com.theiasec.helios.core.communication.SocketSendThread

@com.google.inject.Singleton
class SocketPacketManager extends PacketManager {

	private SocketPacketManager() {
		this.recieveThread = new SocketReceiveThread(5300, this);
		this.sendThread = new SocketSendThread();
	}
	
	@Override
	public static PacketManager getInstance() {
		if(packetManager == null) {
			packetManager = new SocketPacketManager()
		}
		return packetManager
	}
}
