package com.ratemyapp;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import com.nokia.mid.ui.locale.LocaleManager;

/**
 * The RateMyApp class allows to create prompts appearing at the desired
 * intervals, asking the user to review the app and to provide feedback 
 * to the developer.
 *
 * <p>
 * The RateMyApp component is initialized by passing the current MIDlet 
 * instance and the Nokia Store app ID as shown below.
 * </p>
 * 
 * <p>
 * <pre>
 * <code>
 * RateMyApp rma = RateMyApp.init(RateMyAppSample.this, "398118");
 * </code>
 * </pre>
 * </p>
 * 
 * <p>
 * It is possible to customize the prompts display intervals as shown below.
 * </p>
 *
 * <p>
 * <pre>
 * <code>
 * // sets the interval of the first rating dialog
 * rma.setFirstCount(3);
 * 
 * // sets the interval of the second rating dialog
 * rma.setSecondCount(6);
 * 
 * // tells the component to count only one app launch per day
 * rma.setCountDays(true);
 * </code>
 * </pre>
 * </p>
 *
 * <p>
 * The component uses localized resources for all displayed text. The device 
 * default locale is used, but can be overriden via the 
 * {@link com.ratemyapp.RateMyApp#setLanguageOverride(String)} method.
 * </p>
 * 
 * <p>
 * <pre>
 * <code>
 * // sets the component locale to 'en-US'
 * rma.setLanguageOverride('en-US');
 * 
 * </code>
 * </pre>
 * </p>
 * 
 * <p>
 * Resources are defined in XML files named with the following convention:
 * {@code AppResources[.<LANGUAGE_CODE>[-<COUNTRY_CODE>]].resx}.
 * The component comes with multiple localization already available, but new 
 * languages can be added, or the available ones can be overridden, by adding 
 * the relative resource XML files to the Java app project.
 * </p>
 * 
 * <p>
 * If the specified locale, or the device locale if it is not programmatically 
 * overridden, is not available among the component's resources, the default 
 * 'AppResources.xml' file is used.
 * <p>
 * 
 * <p>
 * Individual resource strings can be programmatically set by using the 
 * {@link com.ratemyapp.RateMyApp#setStringResource(String, String)} method.
 * This can be useful, for instance, when a Java app already has localized strings 
 * in its own format.
 * <pre>
 * <code>
 * // sets the rating dialog's title
 * rma.setStringResource(RateMyApp.STRING_RATING_TITLE, "Do you like this app?");
 * </code>
 * </pre>
 * </p>
 * 
 * @author Alessandro La Rosa
 */
public class RateMyApp
{
	/**
	 * Constant for the feedback dialog's cancel button label resource key
	 */
	public static final String STRING_FEEDBACK_NO = "FeedbackNo";
	/**
	 * Constant for the feedback dialog's message resource key
	 */
	public static final String STRING_FEEDBACK_MESSAGE_1 = "FeedbackMessage1";
	/**
	 * Constant for the feedback dialog's title resource key
	 */
	public static final String STRING_FEEDBACK_TITLE = "FeedbackTitle";
	/**
	 * Constant for the feedback dialog's confirm button label resource key
	 */
	public static final String STRING_FEEDBACK_YES = "FeedbackYes";
	/**
	 * Constant for the rating dialog's cancel button label resource key
	 */
	public static final String STRING_RATING_NO = "RatingNo";
	/**
	 * Constant for the first rating dialog's message resource key
	 */
	public static final String STRING_RATING_MESSAGE_1 = "RatingMessage1";
	/**
	 * Constant for the second rating dialog's message resource key
	 */
	public static final String STRING_RATING_MESSAGE_2 = "RatingMessage2";
	/**
	 * Constant for the rating dialog's title resource key
	 */
	public static final String STRING_RATING_TITLE = "RatingTitle";
	/**
	 * Constant for the rating dialog's confirm button label resource key
	 */
	public static final String STRING_RATING_YES = "RatingYes";
	/**
	 * Constant for the feedback email message's text resource key
	 */
	public static final String STRING_FEEDBACK_BODY = "FeedbackBody";
	/**
	 * Constant for the feedback email message's subject resource key
	 */
	public static final String STRING_FEEDBACK_SUBJECT = "FeedbackSubject";
	
	private static RateMyApp instance = null;
	
	private String appID = null;
	
	private MIDlet midlet = null;
	
	private FeedbackHelper feedbackHelper;
	
	private LocalizedStrings localizedStrings;
	
	private String feedbackEmailAddress = null;
	
	private Displayable appDisplayable = null;
	
	private String languageOverride = null;
	
	private RateMyAppListener listener = null;
	
	private RateMyAppCommandListener commandListener = null;
	
	private boolean visible = false;
	
	private RateMyApp(MIDlet midlet, String appID)
	{
		this.midlet = midlet;
		
		this.appID = appID;
		
		this.commandListener = new RateMyAppCommandListener();
		
		this.feedbackHelper = new FeedbackHelper();
		
//		if(this.appID == null || this.appID.length() == 0)
//		{
//			throw new Exception("You have specified an invalid App ID.");
//		}
	}
	/**
	 * Gets the MIDlet instance the component is running into
	 * @return the MIDlet instance
	 */
	public MIDlet getMIDlet()
	{
		return midlet;
	}
	/**
	 * Gets the Nokia Store content ID of the app
	 * @return the Nokia Store content ID
	 */
	public String getAppID()
	{
		return appID;
	}
	/**
	 * Gets the instance of the RateMyApp component
	 * @return the RateMyApp instance, or null if the component has not been initialized
	 */
	public static RateMyApp getInstance()
	{
		return instance;
	}
	
	/**
	 * Initializes the RateMyApp component
	 * @param midlet the MIDlet instance the component is running into
	 * @param appId the Nokia Store content ID of the app
	 * @return the RateMyApp instance
	 */
	public static RateMyApp init(MIDlet midlet, String appId)
	{
		instance = new RateMyApp(midlet, appId);
		
		return instance;
	}
	/**
	 * Sets a listener for visibility changes of this RateMyApp component, replacing any previous {@link com.ratemyapp.RateMyAppListener}
	 * @param listener the new listener, on null
	 */
	public void setHandler(RateMyAppListener listener)
	{
		this.listener = listener;
	}
	/**
	 * Sets the culture code to be used for the component's string resources, overriding the device default locale.
	 * @param language the culture code
	 */
	public void setLanguageOverride(String language)
	{
		this.languageOverride = language;
	}
	/**
	 * Sets the email address for sending feedback to
	 * @param emailAddress the email address
	 */
	public void setFeedbackEmailAddress(String emailAddress)
	{
		this.feedbackEmailAddress = emailAddress;
	}
	/**
	 * Sets the number of launches needed to show the first review dialog.
	 * @param firstCount the number of launches
	 */
	public void setFirstCount(int firstCount)
	{
		feedbackHelper.setFirstCount(firstCount);
	}
	/**
	 * Sets the number of launches needed to show the second review dialog.
	 * @param secondCount the number of launches
	 */
	public void setSecondCount(int secondCount)
	{
		feedbackHelper.setSecondCount(secondCount);
	}
	/**
	 * Controls whether only one launch per day is counted against the first and second count.
	 * @param countDays true if only one launch per day is counted, false otherwise
	 */
	public void setCountDays(boolean countDays)
	{
		feedbackHelper.setCountDays(countDays);
	}
	/**
	 * Sets the value of a string resource.
	 * @param key the string resource key, must be one of the RateMyApp STRING_* constants
	 * @param value the string resource value
	 */
	public void setStringResource(String key, String value)
	{
		localizedStrings.setString(key, value);
	}
	
	/**
	 * Sets the visibility of the RateMyApp component.
	 * @param visible true if visible, false otherwise
	 */
	private void setVisible(boolean visible)
	{
		if(this.visible != visible)
		{
			this.visible = visible;
			
			if(listener != null)
				listener.rmaVisibilityChanged(visible);
		}
	}
	
	/**
	 * Shows the rating dialog.
	 * @param ratingMessageKey the key of the string resource to be used as text for the dialog
	 */
	private void showRatingDialog(String ratingMessageKey)
	{
		appDisplayable = Display.getDisplay(midlet).getCurrent();
		
		showAlert(
			formatText(localizedStrings.getString(STRING_RATING_TITLE), midlet.getAppProperty("MIDlet-Name")), 
			localizedStrings.getString(ratingMessageKey), 
			localizedStrings.getString(STRING_RATING_YES),
			localizedStrings.getString(STRING_RATING_NO)
		);
	}
	
	/**
	 * Shows the feedback dialog.
	 */
	private void showFeedback()
	{
		feedbackHelper.setState(FeedbackHelper.FeedbackState_Feedback);
		
		showAlert(
			formatText(localizedStrings.getString(STRING_FEEDBACK_TITLE), midlet.getAppProperty("MIDlet-Name")), 
			formatText(localizedStrings.getString(STRING_FEEDBACK_MESSAGE_1), midlet.getAppProperty("MIDlet-Name")), 
			localizedStrings.getString(STRING_FEEDBACK_YES),
			localizedStrings.getString(STRING_FEEDBACK_NO)
		);
	}
	
	/**
	 * Shows an Alert dialog using the parameters as resources.
	 * @param title the dialog title
	 * @param message the dialog message
	 * @param okLabel the label for the confirm button
	 * @param cancelLabel the label for the cancel button
	 */
	private void showAlert(String title, String message, String okLabel, String cancelLabel)
	{
		Alert a = new Alert(title, message, null, AlertType.INFO);
			
		a.addCommand(new Command(okLabel, Command.OK, 1));
		a.addCommand(new Command(cancelLabel, Command.CANCEL, 1));
		a.setCommandListener(commandListener);
		Display.getDisplay(midlet).setCurrent(a, appDisplayable);
		
		setVisible(true);
	}
	
	/**
	 * Gets the platform properties to be sent along with the feedback email message.
	 * @return a String array containing the platform properties
	 */
	private String[] getPlatformProperties()
	{
		String[] properties = new String[6];
		
		String platform = System.getProperty("microedition.platform");
		
		if(platform != null)
		{
			int modelSeparator = platform.indexOf('/');
			
			if(modelSeparator >= 0)
			{
				String manufacturer = null;
				String hardwareVersion = null;
				String model = platform.substring(0, modelSeparator);
				String firmware = platform.substring(modelSeparator + 1);
				
				int hardwareVersionSeparator = model.indexOf('-');
				
				if(hardwareVersionSeparator >= 0)
				{
					hardwareVersion = model.substring(hardwareVersionSeparator + 1);
					model = model.substring(0, hardwareVersionSeparator);
				}
				
				if(model.indexOf("Nokia") == 0)
				{
					manufacturer = "Nokia";
					model = model.substring(5);
				}
				
				properties[0] = model;
				properties[1] = manufacturer;
				properties[2] = firmware;
				properties[3] = hardwareVersion;
			}
		}
		
		properties[4] = midlet.getAppProperty("MIDlet-Version");
		properties[5] = midlet.getAppProperty("MIDlet-Vendor");
		
		return properties;
	}
	/**
	 * Formats a source text by replacing the {0} text with the given replacement text 
	 * @param text the source text
	 * @param replacement the replacement text
	 * @return the formatted text
	 */
	private String formatText(String text, String replacement)
	{
		return formatText(text, new String[]{replacement});
	}
	/**
	 * Formats a source text by replacing the {n} matches, with n being an integer between 0 and texts.length - 1, with the corresponding replacement text 
	 * @param text the source text
	 * @param replacements the replacement texts
	 * @return the formatted text
	 */
	private String formatText(String text, String[] replacements)
	{
		for(int i = 0; i < replacements.length; i++)
		{
			int matchIndex = text.indexOf("{" + i + "}");
			
			if(matchIndex >= 0)
			{
				text = text.substring(0, matchIndex) + (replacements[i] != null ? replacements[i] : "") + text.substring(matchIndex + String.valueOf(i).length() + 2);
			}
		}
		return text;
	}
	/**
	 * Resets the counters of this component. This behavior could be useful, for instance, after an app update.
	 */
	public void reset()
	{
		feedbackHelper.reset();
	}
	/**
	 * Opens the review page within the Nokia Store client or, if not supported, within the device Web browser.
	 */
	public void review()
	{
		try
		{
			feedbackHelper.review();
		}
		catch(Exception e)
		{
			Alert a = new Alert("Error", "The Nokia Store client could not be correctly opened", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(a, appDisplayable);
		}
	}
	/**
	 * Launches the component. The launch operation is asynchronous, since it performs blocking operations that could otherwise block the app UI.
	 */
	public void launch()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				asynchronousLaunch();
			}
		}).start();
	}
	/**
	 * The component's asynchronous launch code
	 */
	private void asynchronousLaunch()
	{
		this.feedbackHelper.loadState();
		
		if(this.languageOverride != null)
			this.localizedStrings = new LocalizedStrings(languageOverride);
		else
			this.localizedStrings = new LocalizedStrings(LocaleManager.getInstance().getDefaultLocale());
		
		while(Display.getDisplay(midlet).getCurrent() == null)
		{
			try
			{
				synchronized(this)
				{
					wait(100L);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(feedbackHelper.getState() == FeedbackHelper.FeedbackState_FirstReview)
		{
			showRatingDialog(STRING_RATING_MESSAGE_1);
		}
		else if(feedbackHelper.getState() == FeedbackHelper.FeedbackState_SecondReview)
		{
			showRatingDialog(STRING_RATING_MESSAGE_2);
		}
		else
		{
			feedbackHelper.setState(FeedbackHelper.FeedbackState_Inactive);
		}
	}
	/**
	 * Opens the device's email client to allow the user to send the feedback email message
	 */
	private void feedback()
	{
		try
		{
			RateMyApp.getInstance().getMIDlet().platformRequest(
				new StringBuffer().
				append("mailto:").append(feedbackEmailAddress) 
				.append("?subject=").append(UrlEncoder.encode(formatText(localizedStrings.getString(STRING_FEEDBACK_SUBJECT), midlet.getAppProperty("MIDlet-Name")))) 
				.append("&body=").append(UrlEncoder.encode(formatText(localizedStrings.getString(STRING_FEEDBACK_BODY), getPlatformProperties())))
				.toString()
			);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	class RateMyAppCommandListener implements CommandListener
	{
		/**
		 * Handles the Commands of the review and feedback Alert dialogs presents
		 */
		public void commandAction(Command c, Displayable d)
		{
			int state = feedbackHelper.getState();
			
			if(c.getCommandType() == Command.OK)
			{
				Display.getDisplay(midlet).setCurrent(appDisplayable);
				setVisible(false);
				
				switch(state)
				{
				case FeedbackHelper.FeedbackState_FirstReview:
				case FeedbackHelper.FeedbackState_SecondReview:
					review();
					break;
				case FeedbackHelper.FeedbackState_Feedback:
					feedback();
					break;
				}
				feedbackHelper.setState(FeedbackHelper.FeedbackState_Inactive);
			}
			else
			{
				switch(state)
				{
				case FeedbackHelper.FeedbackState_FirstReview:
					if(feedbackEmailAddress != null)
					{
						showFeedback();
						break;
					}
				default:
					feedbackHelper.setState(FeedbackHelper.FeedbackState_Inactive);
					Display.getDisplay(midlet).setCurrent(appDisplayable);
					setVisible(false);
				}
			}
		}
	}
}
