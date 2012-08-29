package com.theiasec.helios.core.alerts

/**
 * This is created and thrown through the system every time an alert occurs from the modules
 * @author haddaway
 *
 */
class Alert {
	int priority
	int errorCode
	String errorReason
	
	static ERROR_CODE_TO_REASON = [
		1 : "general reason"
		
		
		]
	
	// This is the contructor that you should use to make alerts
	Alert(int code) {
		errorCode = code
		errorReason = ERROR_CODE_TO_REASON[code]
	}
}
