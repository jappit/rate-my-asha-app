package com.ratemyapp.sample;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.ratemyapp.RateMyApp;
import com.ratemyapp.RateMyAppListener;

public class RateMyAppSample extends MIDlet
{
	Form form = null;
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException
	{
	
	}

	protected void pauseApp()
	{
	
	}

	protected void startApp() throws MIDletStateChangeException
	{
		form = new Form("Rate My App");
		form.append("Testing Rate My App in a LCDUI Java app");
		
		Display.getDisplay(this).setCurrent(form);
		
		RateMyApp.init(this, "398118", "feedback@email.com", null);
	}
}
