package com.theiasec.helios.core.managers

import com.theiasec.helios.core.Helios
import com.theiasec.helios.core.alerts.Alert
import com.theiasec.helios.core.alerts.AlertListener
import com.theiasec.helios.core.modules.Module.ModuleType


class AlertManager {

	static AlertManager alertManager
	LinkedList<Alert> alertQueue
	ArrayList<AlertListener> alertListeners
		
	//Singleton
	public static AlertManager getInstance() {
		if (alertManager == null) {
			alertManager = new AlertManager()
		}
		return alertManager
	}

	private AlertManager() {
		alertListeners = new ArrayList<AlertListener>()
		alertQueue = new LinkedList<Alert>()
		//Nothing to do here
	}
	
	//This creates and alert and fires an event to everyone listening
	public addAlert(int errorCode) {
		Alert alert = new Alert(errorCode)
		sendAlertToModules(alert)
		fireAlert(alert)
	}
	
	/**
	 * This will call performAction on every registered action module
	 * @param alert
	 * @return
	 */
	private sendAlertToModules(Alert alert) {
		// The system is not disarmed
		if(!Helios.getInstance().checkDisarmed()) {
			//Iterate over every module
			ModuleManager.getInstance().modules.each {
				//Find all modules that are of type action
				if (it?.moduleType == ModuleType.ACTION) {
					//Call perform action
					it?.performAction()
				}
			}
		}
	}
	
	//Add this to the list
	public addAlertListener(AlertListener listener) {
		alertListeners.add(listener)
	}
	
	/**
	 * This iterates over every listener and gives them the alert
	 * @param alert
	 * @return
	 */
	private fireAlert(Alert alert) {
		//Clean the list
		alertListeners = alertListeners.findAll { it != null }
		//Iterate and fire events
		for (listener in alertListeners) {
			listener.onAlert(alert)
		}
	}
		
}
