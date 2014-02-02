Rate My Asha App
================

Rate My Asha app is the porting of the original Rate My App component for Windows Phone to the Asha platform: https://github.com/nokia-developer/rate-my-app

Rate My Asha App is a component for Java apps asking the user to review the app or send a feedback to the developer.

The component is compatible with the Nokia Asha software platform from version 1.0 onwards.


Behavior
--------
The component is designed to prompt the user to rate the app on the Nokia Store. The rating dialog appears a maximum of two times: if after two prompts, the user still declines to rate the app, the prompt is not displayed anymore (the component can be programmatically reset: in this case the prompts appear again).

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_rating_dialog.png)

If the user accepts to rate the app, he's redirected to the Nokia Store review page for the app he's currently using. On current software versions of the Asha software platform the Nokia Store native client fails to correctly open the app page: for this reason the component is designed to open the app page within the device Web browser.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_rating_webbrowser.png)

The first time the rating prompt is shown, if the user chooses not to rate the app, he's asked to provide feedback to the developer with an email message.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_feedback_dialog.png)

If the user accepts to send the feedback, the device email client is opened with a preformatted email message, that the user can complete with its own feedback about the app.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_feedback_email.png)


Usage example
-------------
The component can be initialized by using the RateMyApp.init() static method with the following three arguments:
* the MIDlet instance
* the Nokia Store app ID
* a listener to be notified when the component in ready

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

Localization
------------

Rate My Asha App uses the same resources used for the original Windows Phone version. For this reason, it supports the same languages, and further localizations can be easily added by using the newly added Windows Phone resource files.

The current list of supported languages is: In v1.0 the component includes strings localized in the following languages: Arabic, German, English US, English UK, Spanish, Finnish, French, Hebrew, Croatian, Hungarian, Italian, Lithuanian, Dutch, Polish, Portuguese BR, Portuguese PT, Romanian, Slovenian and Chinese Simplified.

### Overriding device locale

The component uses the device locale to load the corresponding localized resources. A Java app can override this behavior by programmatically setting the desired language code as shown below.

```
rma.setLanguageOverride('en-US');
```

### Custom localization

If a Java app already uses localized resources, and wants to use its own localization mechanism, the library can be built without the AppResources.resx files, and then individual string resources can be programmatically set as shown below.

```
// localizes the rating dialog's title
rma.setStringResource(RateMyApp.STRING_RATING_TITLE, "Sample rating title");
```

Customization
-------------

The number of launches needed by the component to display the first and the second rating dialogs can be customized as shown below.

```
// sets the interval of the first rating dialog
rma.setFirstCount(3);
 
// sets the interval of the second rating dialog
rma.setSecondCount(6);
 
// tells the component to count only one app launch per day
rma.setCountDays(true);
```

Other features
--------------
The component offers a convenient method that can be called by a Java app in order to open the Nokia Store review page.

```
rma.review();
```
