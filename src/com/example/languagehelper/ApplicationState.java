package com.example.languagehelper;

import com.squareup.otto.Bus;

/**
 * Singleton to hold application state. See
 * http://stackoverflow.com/questions/70689
 * /what-is-an-efficient-way-to-implement
 * -a-singleton-pattern-in-java/71399#71399
 * 
 * @author david
 */
public class ApplicationState {

	private static final Model MODEL = new Model();
	private static final Bus BUS = new Bus();

	public static Model getModel() {
		return MODEL;
	}

	public static Bus getBus() {
		return BUS;
	}

	private ApplicationState() {
		// TODO Auto-generated constructor stub
	}

}
