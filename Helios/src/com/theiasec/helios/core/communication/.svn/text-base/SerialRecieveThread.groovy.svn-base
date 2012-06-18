package com.theiasec.helios.core.communication

import com.theiasec.helios.core.managers.PacketManager;

import gnu.io.SerialPort
import gnu.io.SerialPortEvent
import gnu.io.SerialPortEventListener

class SerialRecieveThread extends RecieveThread implements SerialPortEventListener{
	def input
	def packetManager
	def buffer = []
	
	public SerialRecieveThread(SerialPort serialPort, PacketManager packetManager) {
		this.packetManager = packetManager
		input = serialPort.getInputStream()
		serialPort.addEventListener(this)
		serialPort.notifyOnDataAvailable(true)
	}
	
	public synchronized void serialEvent(SerialPortEvent evt) {
		if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				byte singleData = (byte)input.read();

				if (singleData != 10)
				{
					def stack = new String(singleData as byte[]);
					buffer.add(stack)
				}
				else
				{
					StringBuilder sb = new StringBuilder()
					for(charector in buffer) {
						sb.append(charector)
					}
					queue.add(sb.toString())
					buffer = []
				}
			}
			catch (Exception e)
			{
				logText = "Failed to read data. (" + e.toString() + ")";
				window.txtLog.setForeground(Color.red);
				window.txtLog.append(logText + "\n");
			}
		}
	}
	
    //This is a destructive read!!
	@Override
	public Packet readPacket() {
		if(this.queue.size() > 0) {
			def currentPacket = queue[0]
			//Make a packet from what we got
			Packet packet = new Packet(currentPacket, packetManager)
			queue.remove(0)
			return packet
		}
	}

	@Override
	public Packet pullPacket() {
		// TODO Implement
		return null;
	}


}
