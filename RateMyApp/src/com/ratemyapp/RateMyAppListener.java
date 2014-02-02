package com.ratemyapp;

/**
 * The RateMyAppListener interface is responsible for being notified
 * of visibility changes of the {@link com.ratemyapp.RateMyApp} component. These notifications 
 * can be used by the app to take appropriate actions on the underlying 
 * Displayable, for instance stopping animations and suspending timers. 
 * 
 * @author Alessandro La Rosa
 */
public interface RateMyAppListener
{
	/**
	 * Notifies visibility changes of the {@link com.ratemyapp.RateMyApp} component
	 * @param visible true if the component did become visible, false otherwise
	 */
	public void rmaVisibilityChanged(boolean visible);
}
