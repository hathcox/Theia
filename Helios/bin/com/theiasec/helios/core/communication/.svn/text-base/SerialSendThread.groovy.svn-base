package com.theiasec.helios.core.communication

import com.theiasec.helios.core.managers.PacketManager

import gnu.io.SerialPort

class SerialSendThread extends SendThread {
	def output
	def packetManager
	
	
	public SerialSendThread(SerialPort serialPort, PacketManager packetManager) {
		this.packetManager = packetManager
		output = serialPort.getOutputStream()
	}
	
	@Override
	public void pushPacket(Packet packet) {
		// TODO Implement

	}

	@Override
	public boolean writePacket(Packet packet) {
		try {
			for(charector in packet.rawData) {
				output.write((charector as char) as byte)
			}
			output.flush()
		} catch(e) {
			print e.printStackTrace()
			return false
		}
		return true
	}

	@Override
	public void popPacket() {
		// TODO Implement

	}

}
