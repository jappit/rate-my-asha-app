package com.ratemyapp.sample.lwuit;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.midlet.MIDlet;

import javax.microedition.midlet.MIDletStateChangeException;

import com.ratemyapp.RateMyApp;
import com.ratemyapp.RateMyAppListener;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;

public class LWUITRateMyApp extends MIDlet {

	Form form = null;
	
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException
	{
		Display.init(this);
		
		form = new Form("Rate My App");
		form.show();

		RateMyApp.init(this, "398118", new MyRateMyAppListener());
	}
	
	
	class MyRateMyAppListener implements RateMyAppListener
	{
		public void rmaVisibilityChanged(boolean visible)
		{
			form.addComponent(new Label("Rate My App visibile? " + visible));
		}
		public void rmaComponentReady()
		{
			form.addComponent(new Label("Rate My App ready"));
			
			RateMyApp rma = RateMyApp.getInstance();
			
			rma.setFeedbackEmailAddress("feedback@email.com");
			
			rma.setStringResource(RateMyApp.STRING_RATING_TITLE, "Do you like this app?");
			
			rma.setFirstCount(1);
			rma.setSecondCount(2);
			rma.setCountDays(false);
			
			rma.reset();
			
			rma.launch();
		}
	}

}
