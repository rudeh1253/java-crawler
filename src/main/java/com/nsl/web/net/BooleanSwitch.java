package com.nsl.web.net;

class BooleanSwitch {
    private boolean b;

	public BooleanSwitch(boolean startState) {
	    this.b = startState;
	}
	
	public boolean getBool() {
	    return this.b;
	}
	
	public void change() {
	    this.b = !(this.b);
	}
	
	public void setBool(boolean bool) {
	    this.b = bool;
	}
}
