package com.theiasec.helios.core.managers

import com.theiasec.helios.core.alerts.Alert
import com.theiasec.helios.core.alerts.AlertListener

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
		fireAlert(new Alert(errorCode))
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
