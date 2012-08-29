package com.theiasec.helios.core.alerts

/**
 * This is be implemented by any class that wants to receive alerts from the Helios System
 * @author haddaway
 *
 */
interface AlertListener {

	// This is called after an alert is created
	public void onAlert(Alert alert)
}
