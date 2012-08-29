package com.theiasec.helios.graphics.main;

import com.theiasec.helios.core.Helios;
import com.theiasec.helios.core.alerts.Alert;
import com.theiasec.helios.core.alerts.AlertListener;


public class Main implements AlertListener {

	private static Helios helios;

	public Main() {
		helios = Helios.getInstance();
		helios.addAlertListener(this);
		helios.start();
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void onAlert(Alert alert) {
		System.out.println(alert.getErrorReason());
	}


}
