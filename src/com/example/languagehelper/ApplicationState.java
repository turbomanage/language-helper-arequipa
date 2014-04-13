package com.example.languagehelper;

/**
 * Singleton to hold application state.
 * See http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java/71399#71399
 * 
 * @author david
 */
public class ApplicationState {

	public enum Model {
		INSTANCE;
		private boolean tradOnLeft = true;
		
		public boolean getDirection() {
			return this.tradOnLeft;
		}

		public void swapDirection() {
			this.tradOnLeft = !this.tradOnLeft;
		};
	}

}
