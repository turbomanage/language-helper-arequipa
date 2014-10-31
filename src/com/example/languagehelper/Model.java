package com.example.languagehelper;


public class Model {
	private boolean tradOnLeft = true;

	public boolean getDirection() {
		return this.tradOnLeft;
	}

	public void swapAndNotify() {
		this.tradOnLeft = !this.tradOnLeft;
		App.getEventBus().post(new OrderChangedEvent());
	};
}
