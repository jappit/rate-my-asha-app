package com.ratemyapp.sample;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.ratemyapp.RateMyApp;
import com.ratemyapp.RateMyAppListener;

public class RateMyAppSample extends MIDlet implements RateMyAppListener
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
		
		RateMyApp.init(this, "398118", this);
	}
	public void rmaVisibilityChanged(boolean visible)
	{
		form.append("Rate My App visibile? " + visible);
	}
	public void rmaComponentReady()
	{
		form.append("Rate My App ready");
		
		RateMyApp rma = RateMyApp.getInstance();
		
		rma.setFeedbackEmailAddress("feedback@email.com");
		
		rma.setStringResource(RateMyApp.STRING_RATING_TITLE, "Do you like this app?");
		
		rma.setFirstCount(3);
		rma.setSecondCount(6);
		rma.setCountDays(false);
		
		//rma.reset();
		
		rma.launch();
	}
}
