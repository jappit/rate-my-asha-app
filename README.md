Rate My Asha App
================

Rate My Asha app is the porting of the original Rate My App component for Windows Phone to the Asha platform: https://github.com/nokia-developer/rate-my-app

Rate My Asha App is a component for Java apps asking the user to review the app or send a feedback to the developer.

The component is compatible with the Nokia Asha software platform from version 1.0 onwards.


How does it work?
-----------------
The component is designed to prompt the user to rate the app on the Nokia Store. The rating dialog appears a maximum of two times: if after two prompts, the user still declines to rate the app, the prompt is not displayed anymore (the component can be programmatically reset: in this case the prompts appear again).

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_rating_dialog.png)

If the user accepts to rate the app, he's redirected to the Nokia Store review page for the app he's currently using. On current software versions of the Asha software platform the Nokia Store native client fails to correctly open the app page: for this reason the component is designed to open the app page within the device Web browser.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_rating_webbrowser.png)

The first time the rating prompt is shown, if the user chooses not to rate the app, he's asked to provide feedback to the developer with an email message.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_feedback_dialog.png)

If the user accepts to send the feedback, the device email client is opened with a preformatted email message, that the user can complete with its own feedback about the app.

![](http://jappit.com/m/asha/ratemyapp/images/screens/asha_ratemyapp_feedback_email.png)


Usage instructions
-------------
### Quick launch

The component can be initialized and launched by using the `RateMyApp.init` static method with the following four arguments:
* the MIDlet instance
* the Nokia Store app ID
* the email address for sending feedback to
* a null listener

```
RateMyApp.init(midlet, appID, feedbackEmailAddress, null);
```

The component initialization, having set a null listener, automatically launches the component once the necessary component's resources are loaded.

### Using a listener

A sample RateMyAppListener implementation is show below.

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
		
		rma.launch();
	}
}
```

When a non-null listener is passed to the init method, the component is not automatically launched. This means that the Java app needs to wait for the component to be ready, as notified by the `rmaComponentReady` method of the RateMyAppListener interface, and then can launch it via its `launch` method.

```
RateMyApp.init(midlet, appID, feedbackEmailAddress, myListener);
```

By using a listener, a Java app can perform customizations of the component, such as overriding individual text resources or setting the number of launches needed before prompting the user, as shown by the following sections.


Localization
------------

Rate My Asha App uses the same resources used for the original Windows Phone version. For this reason, it supports the same languages, and further localizations can be easily added by using the newly added Windows Phone resource files.

Currently supported languages are: Arabic, German, English US, English UK, Spanish, Finnish, French, Hebrew, Croatian, Hungarian, Italian, Lithuanian, Dutch, Polish, Portuguese BR, Portuguese PT, Romanian, Slovenian and Chinese Simplified.

### Overriding device locale

The component uses the device locale to load the corresponding localized resources. A Java app can override this behavior by programmatically setting the desired language code as shown below.

```
RateMyApp.setLanguageOverride('en-US');
```

The above static method must be called before the `RateMyApp.init` method, otherwise it will have no effects on the locale used by the component.

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

Related content
---------------
An overview of the porting process is available on the Nokia Developer Wiki: http://developer.nokia.com/community/wiki/Porting_the_Rate_My_App_component_to_the_Asha_software_platform

The original Windows Phone Rate My App component can be found here: https://github.com/nokia-developer/rate-my-app
