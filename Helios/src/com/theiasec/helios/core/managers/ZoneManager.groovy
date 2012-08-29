package com.theiasec.helios.core.managers

class ZoneManager {
	
	static ZoneManager zoneManager

	//Singleton
	public static ZoneManager getInstance() {
		if (zoneManager == null) {
			zoneManager = new ZoneManager()
		}
		return zoneManager
	}
		
	private ZoneManager() {

	}
	//Load the xml
	//Logic
	def zones = []
}
