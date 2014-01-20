package com.ratemyapp;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import com.nokia.mid.ui.locale.LocaleManager;
import com.ratemyapp.helpers.FeedbackHelper;
import com.ratemyapp.helpers.LocalizedStrings;

public class RateMyApp
{
	public static final String STRING_FEEDBACK_NO = "FeedbackNo";
	public static final String STRING_FEEDBACK_MESSAGE_1 = "FeedbackMessage1";
	public static final String STRING_FEEDBACK_TITLE = "FeedbackTitle";
	public static final String STRING_FEEDBACK_YES = "FeedbackYes";
	public static final String STRING_RATING_NO = "RatingNo";
	public static final String STRING_RATING_MESSAGE_1 = "RatingMessage1";
	public static final String STRING_RATING_MESSAGE_2 = "RatingMessage2";
	public static final String STRING_RATING_TITLE = "RatingTitle";
	public static final String STRING_RATING_YES = "RatingYes";
	public static final String STRING_FEEDBACK_BODY = "FeedbackBody";
	public static final String STRING_FEEDBACK_SUBJECT = "FeedbackSubject";
	
	private static RateMyApp instance = null;
	
	private String appID = null;
	
	private MIDlet midlet = null;
	
	private FeedbackHelper feedbackHelper;
	
	private LocalizedStrings localizedStrings;
	
	private String feedbackEmailAddress = null;
	
	private Displayable appDisplayable = null;
	
	private RateMyApp(MIDlet midlet, String appID)
	{
		this.midlet = midlet;
		
		this.appID = appID;
		
		this.feedbackHelper = new FeedbackHelper();
		
		//System.out.println(System.getProperty("microedition.locale"));
		
		this.localizedStrings = new LocalizedStrings(LocaleManager.getInstance().getDefaultLocale());
	}
	public MIDlet getMIDlet()
	{
		return midlet;
	}
	public String getAppID()
	{
		return appID;
	}
	public static RateMyApp getInstance()
	{
		return instance;
	}
	
	public static RateMyApp init(MIDlet midlet, String appId)
	{
		instance = new RateMyApp(midlet, appId);
		
		return instance;
	}
	public void setFeedbackEmailAddress(String emailAddress)
	{
		this.feedbackEmailAddress = emailAddress;
	}
	public void setLaunchCount(int firstCount, int secondCount, boolean countDays)
	{
		feedbackHelper.setFirstCount(firstCount);
		feedbackHelper.setSecondCount(secondCount);
		feedbackHelper.setCountDays(countDays);
	}
	public void setStringResource(String key, String value)
	{
		localizedStrings.setString(key, value);
	}
	private void showRatingDialog(boolean showFeedbackOnCancel)
	{
		final boolean showFeedback = showFeedbackOnCancel;
		
		appDisplayable = Display.getDisplay(midlet).getCurrent();
		
		Alert a = new Alert(
			formatText(localizedStrings.getString(STRING_RATING_TITLE), midlet.getAppProperty("MIDlet-Name")), 
			localizedStrings.getString(STRING_RATING_MESSAGE_1) + "\n" + localizedStrings.getString(STRING_RATING_MESSAGE_2), 
			null, 
		AlertType.INFO);
		
		final Command okCommand = new Command(localizedStrings.getString(STRING_RATING_YES), Command.OK, 1);
		final Command cancelCommand = new Command(localizedStrings.getString(STRING_RATING_NO), Command.CANCEL, 1);
		a.addCommand(okCommand);
		a.addCommand(cancelCommand);

		a.setCommandListener(new CommandListener()
		{
			public void commandAction(Command c, Displayable d)
			{
				if(c == okCommand)
				{
					Display.getDisplay(midlet).setCurrent(appDisplayable);
					
					feedbackHelper.review();
				}
				else
				{
					if(showFeedback && feedbackEmailAddress != null)
						showFeedbackDialog();
					else
						Display.getDisplay(midlet).setCurrent(appDisplayable);
						
				}
				
			}
		});
		Display.getDisplay(midlet).setCurrent(a, appDisplayable);
	}
	private void showFeedbackDialog()
	{
		Alert a = new Alert(
			formatText(localizedStrings.getString(STRING_FEEDBACK_TITLE), midlet.getAppProperty("MIDlet-Name")), 
			formatText(localizedStrings.getString(STRING_FEEDBACK_MESSAGE_1), midlet.getAppProperty("MIDlet-Name")), 
			null, 
			AlertType.INFO
		);
		
		final Command okCommand = new Command(localizedStrings.getString(STRING_FEEDBACK_YES), Command.OK, 1);
		final Command cancelCommand = new Command(localizedStrings.getString(STRING_FEEDBACK_NO), Command.CANCEL, 1);
		a.addCommand(okCommand);
		a.addCommand(cancelCommand);

		a.setCommandListener(new CommandListener()
		{
			public void commandAction(Command c, Displayable d)
			{
				Display.getDisplay(midlet).setCurrent(appDisplayable);
				
				if(c == okCommand)
				{
					try
					{
						RateMyApp.getInstance().getMIDlet().platformRequest(
							"mailto:" + feedbackEmailAddress + 
							"?subject=" + urlEncode(formatText(localizedStrings.getString(STRING_FEEDBACK_SUBJECT), midlet.getAppProperty("MIDlet-Name"))) + 
							"&body=" + urlEncode(formatText(localizedStrings.getString(STRING_FEEDBACK_BODY), initPlatformProperties())));
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				
			}
		});
		Display.getDisplay(midlet).setCurrent(a, appDisplayable);
	}
	private String[] initPlatformProperties()
	{
		String[] properties = new String[6];
		
		String platform = System.getProperty("microedition.platform");
		
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
		properties[4] = midlet.getAppProperty("MIDlet-Version");
		properties[5] = midlet.getAppProperty("MIDlet-Vendor");
		
		return properties;
	}
	private String formatText(String text, String replacement)
	{
		return formatText(text, new String[]{replacement});
	}
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
	public void reset()
	{
		feedbackHelper.reset();
	}
	public void launch()
	{
		if(true || feedbackHelper.getState() == FeedbackHelper.FeedbackState_FirstReview)
		{
			showRatingDialog(true);
		}
		else if(feedbackHelper.getState() == FeedbackHelper.FeedbackState_SecondReview)
		{
			showRatingDialog(false);
		}
	}
	private static String urlEncode(String str)
	{
		StringBuffer buf = new StringBuffer();
		char c;
		int cInt;
		for(int i = 0; i < str.length(); i++)
		{ 
			c = str.charAt(i);
			if ((c >= '0' && c <= '9')||
				(c >= 'A' && c <= 'Z')||
				(c >= 'a' && c <= 'z'))
			{
				buf.append(c);
			}
			else
			{
				cInt = (int) str.charAt(i);
				buf.append("%");
				if(cInt < 16)
					buf.append('0');
				buf.append(Integer.toHexString(cInt));
			}
		} 
		return buf.toString();
	}
}
