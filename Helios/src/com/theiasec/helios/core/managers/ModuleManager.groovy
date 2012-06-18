package com.theiasec.helios.core.managers

import com.theiasec.helios.core.communication.Packet
import com.theiasec.helios.core.modules.Module
import com.theiasec.helios.core.modules.TestModule

class ModuleManager {
	
	//For Singleton
	static ModuleManager moduleManager
	
	//Blank list for all the modules
	def moduleMap = [
		"Test" : TestModule.class
		]
	
	def modules = []
	
	int highestModuleId = 65536
	
	//Timeout in seconds before we disconnect a module
	int MAX_TIMEOUT = 30
	
	CommunicationManager communicationManager
	Random randomGenerator
	
	private ModuleManager(CommunicationManager communicationManager, Random randomGenerator) {
		this.communicationManager = communicationManager
		this.randomGenerator = randomGenerator
	}
	
	public static ModuleManager getInstance() {
		if (moduleManager == null) {
			moduleManager = new ModuleManager(TestCommunicationManager.getInstance(), new Random())
		} 
		return moduleManager
	}
	
	void start() {
		//Catch packets in the queue and deal with them
		while(true) {
			//Grab the top packet
			Packet currentPacket = communicationManager.recievePacket()
			if(currentPacket != null && currentPacket.rawData != null) {
				//Send the packet to the module that its dealing with
				println currentPacket.rawData + "-" + currentPacket.source
				Module currentModule = modules.find {
					//Find the device with matching Ids
					it.deviceId == currentPacket.deviceId
				}
				//If we found an actual module with that id
				if(currentModule){
					//Tell the module to deal with it
					currentModule.filterPacket(currentPacket)
				//Attempt to add a new module
				} else {
					if(currentPacket.isInitializationPacket()) {
						Module newModule = Module.createNewModule(currentPacket)
						modules.add(newModule)
					}
				}
			}
			//Loop through modules dealing with shit, packets and so forth
			for(module in modules) {
				//If the module isn't initialized
				if(!module.isInitialized) {
					String randomId = null
					def existingModule = 1
					if(!module.deviceIdSet) {
						while(existingModule != null) {
							randomId = Integer.toHexString(randomGenerator.nextInt(highestModuleId)).toUpperCase()
							println "Random number $randomId"
							existingModule = modules.find { it.deviceId == randomId }
						}
					} else {
						randomId = module.deviceId
					}
					//println "Sending random Id: $randomId"
					module.initialize(randomId)
				}
				else {
					if(!module.printed) {
						println "Module initialized $module"
						module.printed = true
					}
					//Check to see if the module has heartbeaten soon enough
					checkForTimeout(module)
				}
			}
		}
	}
	
	void checkForTimeout(Module module) {
		Date currentTime = new Date()
		if((module.lastPacket - currentTime) >= MAX_TIMEOUT) {
			println "Timeout!!"
		}
	}
	
	void addModule(Module module) {
		//Check if initialized 
		if(module.isInitialized) {

		}
		//if not, initialize
		else {
			module.initialize()
		}		
		//pre add code
		modules.add(module)
	}
	
	void removeModule(Module module) {
		modules.remove(module)
	}
}
