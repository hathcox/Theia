package com.theiasec.helios.core.communication

import com.theiasec.helios.core.managers.PacketManager

class SocketReceiveThread extends RecieveThread {
	ServerSocket server
	PacketManager packetManager

	public SocketReceiveThread(int port = 5300, PacketManager packetManager) {
		this.packetManager = packetManager
		//bind to an address
		server = new ServerSocket(port)
		println "Socket Recive Thread started on [$port]"
		this.thread.start {
			//Just pull input from the socket
			while(true) {
				server.accept { socket ->
					try {
						socket.withStreams { input, output ->
							input.eachLine {
								//Dont take null packets
								if(it != null) {
									this.queue.add(new RawPacket(data:it, source:socket))
								}
							}
						}
					} catch (SocketException e) {
						if(e.getMessage() == "Socket closed") {
							//Do nothing
						} else {
							throw e
						}
					}
				}
			}
		}
	}


	//This currently only grabs the top element
	//THIS IS A DESTRUCTIVE READ
	@Override
	public Packet readPacket() {
		if(this.queue.size() > 0) {
			def currentPacket = queue[0]
			//Make a packet from what we got
			Packet packet = new Packet(currentPacket.data, packetManager, currentPacket.source)
			queue.remove(0)
			return packet
		}
	}

	@Override
	public Packet pullPacket() {
		//TODO: Implement
		return null;
	}

}
