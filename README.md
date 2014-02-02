Rate My Asha App
================

Rate my Asha app is the porting of the original Rate My App component for Windows Phone to the Asha platform: https://github.com/nokia-developer/rate-my-app

Rate MY Asha App is a component for Java apps asking the user to review the app or send a feedback to the developer.

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
