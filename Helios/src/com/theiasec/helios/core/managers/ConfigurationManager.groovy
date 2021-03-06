package com.theiasec.helios.core.managers

import com.theiasec.helios.core.zones.Zone

class ConfigurationManager {
	//Loads all the config files
	//Passes sections to managers
	String configurationLocation
	def xmlContent
	def packetManager
	def zoneManager
	def moduleManager
	def communicationManager
	
	
	public ConfigurationManager(String configurationLocation) {
		zoneManager = ZoneManager.getInstance()
		this.moduleManager = ModuleManager.getInstance()
		this.configurationLocation = configurationLocation
		this.communicationManager = TestCommunicationManager.getInstance()
		File configurationFile = new File(configurationLocation)
		xmlContent = new XmlParser().parseText(configurationFile.getText())
		loadConfigurations()
	}
	
	void loadConfigurations() {
		loadPacketConfig()
		//loadZoneConfig()
	}
	
	void loadPacketConfig() {
		packetManager = SocketPacketManager.getInstance()
		//Load base Size		
		packetManager.baseSize = xmlContent.packetConfig[0].'@baseSize' as int
		//Load device Type
		packetManager.deviceType.add(xmlContent.packetConfig[0].deviceType.'@start'[0] as int)
		packetManager.deviceType.add(xmlContent.packetConfig[0].deviceType.'@end'[0] as int)
		//Load device Id
		packetManager.deviceId.add(xmlContent.packetConfig[0].deviceId.'@start'[0] as int)
		packetManager.deviceId.add(xmlContent.packetConfig[0].deviceId.'@end'[0] as int)
		//Load priority
		packetManager.priority.add(xmlContent.packetConfig[0].priority.'@start'[0] as int)
		packetManager.priority.add(xmlContent.packetConfig[0].priority.'@end'[0] as int)
		//Load status
		packetManager.status.add(xmlContent.packetConfig[0].status.'@start'[0] as int)
		packetManager.status.add(xmlContent.packetConfig[0].status.'@end'[0] as int)
		//Load data
		packetManager.data.add(xmlContent.packetConfig[0].data.'@start'[0] as int)
	}
	
	void loadZoneConfig() {
		//Subtract one for zero index
		int numberOfZones = xmlContent.zones.zone.size() - 1
		
		//Create all of the zones and name them
		for(zoneIndex in 0..numberOfZones) {
			Zone newZone = new Zone()
			newZone.name = xmlContent.zones[0].zone[zoneIndex].'@name'
			newZone.nextZoneName = xmlContent.zones[0].zone[zoneIndex].'@next'
			def authenticationModulesSize = xmlContent.zones[0].zone[zoneIndex].authenticationModules.size() - 1
			def sensorModulesSize = xmlContent.zones[0].zone[zoneIndex].sensorModules.size() - 1
			def actionModulesSize = xmlContent.zones[0].zone[zoneIndex].actionModules.size() - 1
			
			//Create Authentication Modules
			for(authenticationModuleIndex in 0..authenticationModulesSize) {
				def moduleType = xmlContent.zones[0].zone[zoneIndex].authenticationModules[0].module[authenticationModuleIndex].'@type'
				def module = moduleManager.moduleMap[moduleType].newInstance()
				module.communicationManager = this.communicationManager
				moduleManager.modules.add(module)
				newZone.authenticationModules.add(module)
			}
			
			//Create Sensor Modules
			for(sensorModulesIndex in 0..sensorModulesSize) {
				def moduleType = xmlContent.zones[0].zone[zoneIndex].sensorModules[0].module[sensorModulesIndex].'@type'
				def module = moduleManager.moduleMap[moduleType].newInstance()
				module.communicationManager = this.communicationManager
				moduleManager.modules.add(module)
				newZone.sensorModules.add(module)
			}
			
			//Create Action Modules
			for(actionModulesIndex in 0..actionModulesSize) {
				def moduleType = xmlContent.zones[0].zone[zoneIndex].actionModules[0].module[actionModulesIndex].'@type'
				def module = moduleManager.moduleMap[moduleType].newInstance()
				module.communicationManager = this.communicationManager
				moduleManager.modules.add(module)
				newZone.actionModules.add(module)
			}
			
			zoneManager.zones.add(newZone)
		}
		
		//Link all of the zones to one another
		for(zone in zoneManager.zones) {
			//If we havent tied a zone to it, try to 
			if(zone.nextZone == null) {
				//If we should have a next zone
				if(zone.nextZoneName != '') {
					def nextZone = zoneManager.zones.find {
						it.name == zone.nextZoneName
					}
					zone.nextZone = nextZone
				}
			}
		}
	}
	
}
