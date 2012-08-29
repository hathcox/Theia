package com.theiasec.helios.core.modules

import com.theiasec.helios.core.communication.Packet;
import com.theiasec.helios.core.managers.ModuleManager
import com.theiasec.helios.core.managers.SocketPacketManager
import com.theiasec.helios.core.managers.TestCommunicationManager

/**
 * This is the Authentication module that recieves usb keys
 * @author haddaway
 *
 */
class UsbKeyModule extends Module{
	
	private String CORRECT_VALUE = "PENIS"
	
	public BuzzerModule() {
		moduleType = ModuleType.AUTHENTICATION
	}
	
	@Override
	public Packet filterPacket(Packet packet) {
		//Update heartbeat timer
		lastPacket = new Date()
		//If we got a initialize packet from helios
		if(packet.status == "00FF") {
			//Check and make sure that the device Id is the same we have on record
			this.deviceType = packet.deviceType
			this.isInitialized = true
		} else if (packet.status == "DEAD") {
			//Unregister the the module
			println "Unregistering module [${this.deviceId}-${this.class}]"
			unregister(packet)
		} 
		return null;
	}

	@Override
	public void initialize(String deviceId) {
		//In the future this should send the packet [0000 DEVICEID 1 1000 DATA]
		this.deviceId = deviceId
		this.deviceIdSet = true
		//Make a packet with the device Id and send it to our shit
		Packet packet = new Packet("0000"+deviceId+"1100000000000000000000000", SocketPacketManager.getInstance(), this.location)
		TestCommunicationManager.getInstance().sendPacket(packet)
	}

	/**
	* This unregisters a module from the module manager and closes the socket connection
	* @param packet
	*/
   void unregister(Packet packet){
	   ModuleManager.getInstance().removeModule(this)
	   this.location.close()
   }
   
	@Override
	public void performAction() {
		//Send the packet from helios to the buzzer to turn it on
		Packet packet = new Packet(deviceType+deviceId+SEND_ALARM+BLANK_DATA, SocketPacketManager.getInstance(), this.location)
		TestCommunicationManager.getInstance().sendPacket(packet)
	}

}
