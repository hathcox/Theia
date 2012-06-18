package com.theiasec.helios.core.managers

import gnu.io.CommPortIdentifier
import gnu.io.SerialPort

import com.theiasec.helios.core.communication.RecieveThread
import com.theiasec.helios.core.communication.SendThread
import com.theiasec.helios.core.communication.SerialRecieveThread
import com.theiasec.helios.core.communication.SerialSendThread

//TODO: This should now be abstract, and called SerialPacketManager
@com.google.inject.Singleton
class SerialPacketManager extends PacketManager {
	// Connect to the serial port
	//Sorry this is hardcoded
	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0")
	SerialPort serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), TIME_OUT)
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	
	SendThread sendThread = new SerialSendThread(serialPort, this)
	RecieveThread recieveThread = new SerialRecieveThread(serialPort, this)
	
	public SerialPacketManager() {
		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE)
	}
	
	/**
	* This should be called when you stop using the port.
	* This will prevent port locking on platforms like Linux.
	*/
   public synchronized void close() {
	   if (serialPort != null) {
		   serialPort.removeEventListener();
		   serialPort.close();
	   }
   }

	@Override
	public static PacketManager getInstance() {
		if(packetManager == null) {
			packetManager = new SerialPacketManager()
		}
		return packetManager
	}
}
