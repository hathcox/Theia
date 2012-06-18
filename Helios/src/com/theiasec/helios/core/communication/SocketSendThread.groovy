package com.theiasec.helios.core.communication

class SocketSendThread extends SendThread {

	@Override
	public void pushPacket(Packet packet) {
	}

	@Override
	public boolean writePacket(Packet packet) {
		try {
			packet.source << "$packet.rawData\n"
			packet.source.getOutputStream().flush()
		} catch (SocketException e) {
			if(e.getMessage() == "Socket closed") {
				//Do nothing
			} else {
				throw e
			}
		}
	}

	@Override
	public void popPacket() {

	}
}
