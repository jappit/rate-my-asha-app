package com.ratemyapp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.nokia.mid.ui.locale.Locale;

class LocalizedStrings
{
	private Hashtable strings = null;
	
	public LocalizedStrings()
	{
		strings = new Hashtable();
	}
	public void setString(String key, String value)
	{
		strings.put(key, value);
	}
	public String getString(String key)
	{
		if(strings.containsKey(key))
			return (String)strings.get(key);
		return null;
	}
	public boolean readResources(String locale)
	{
		boolean resourceLoaded = false;
		
		String filename = "AppResources";
		
		if(locale != null)
		{
			filename += "." + locale;
		}
		
		filename += ".resx";
		
		InputStream is = null;
		InputStreamReader isr = null;
		
		System.out.println("FILE: " + filename);
		
		try
		{
			is = getClass().getResourceAsStream("/" + filename);
			
			isr = new InputStreamReader(is);
			
			KXmlParser parser = new KXmlParser();
				
			parser.setInput(isr);
			
			strings = new Hashtable();
			
			parser.nextTag();
			
			parser.require(XmlPullParser.START_TAG, null, "root");
			
			parser.nextTag();
			
			while(parser.getEventType() != XmlPullParser.END_TAG)
			{
				String nodeName = parser.getName();
				
				if(nodeName.compareTo("data") == 0)
				{
					String resourceName = parser.getAttributeValue("", "name");
					
					parser.nextTag();
					
					while(parser.getEventType() != XmlPullParser.END_TAG)
					{
						if(parser.getName().compareTo("value") == 0)
						{
							String resourceValue = parser.nextText();
							
							strings.put(resourceName, resourceValue);
						}
						else
						{
							parser.skipSubTree();
						}
						parser.nextTag();
					}
				}
				else
				{
					parser.skipSubTree();
				}
				parser.nextTag();
			}
			resourceLoaded = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(isr != null)
					isr.close();
				if(is != null)
					is.close();
			}
			catch(Exception e)
			{
			}
		}
		return resourceLoaded;
	}
}
