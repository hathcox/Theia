package com.theiasec.helios.core.modules


import com.theiasec.helios.core.communication.Packet
import com.theiasec.helios.core.modules.Module
import com.theiasec.helios.core.managers.AlertManager;
import com.theiasec.helios.core.managers.ModuleManager;
import com.theiasec.helios.core.managers.SocketPacketManager;
import com.theiasec.helios.core.managers.TestCommunicationManager;

class DoorIsOpenModule extends Module {
/*
 * This is the door is open module, this will recieve an update each time the door status changes
 */
	/* false means the door is closed
	 * true means the door is open
	 */
	boolean doorStatus

	public DoorIsOpenModule() {
		moduleType = ModuleType.SENSOR
	}
	
	//This module should deal with the packet and return a packet should it need to be sent to Zone Manager
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
		} else if (packet.status == "0301") {
			if(doorStatus == false) {
				println "[~~]WARNING: Setting the Door to Open, if this wasn't already open an alert will be raised"
				//Raise alert
				AlertManager.getInstance().addAlert(1)
				doorStatus = true 
			}
		}
		else if (packet.status == "0302") {
			if(doorStatus == true) {
				println "[~~]WARNING: Setting the Door to Closed, if this wasn't already open an alert will be raised"
				//Raise alert
				AlertManager.getInstance().addAlert(1)
				doorStatus = false
			}
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
		//Do nothing
	}

}
