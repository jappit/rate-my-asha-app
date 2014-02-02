Rate my Asha app
================

Rate my Asha app is a component for Java apps asking the user to review the app or send a feedback to the developer.

The component is compatible with the Nokia Asha software platform from version 1.0 onwards.

Example usage:

```
RateMyApp.init(this, appID, new MyRateMyAppListener());
```

Where the MyRateMyAppListener class is defined as follows:

```
class MyRateMyAppListener implements RateMyAppListener
{
	public void rmaVisibilityChanged(boolean visible)
	{
		// component visibility changed
	}
	public void rmaComponentReady()
	{
	  // component ready
	
		RateMyApp rma = RateMyApp.getInstance();
		
		rma.setFeedbackEmailAddress("feedback@email.com");
		
		rma.launch();
	}
}
```
