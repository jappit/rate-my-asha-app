package com.ratemyapp;

import java.util.Date;


class FeedbackHelper
{
	public static final int FeedbackState_Inactive = 0;
	public static final int FeedbackState_Active = 1;
	public static final int FeedbackState_FirstReview = 2;
	public static final int FeedbackState_SecondReview = 3;
	public static final int FeedbackState_Feedback = 4;
	
	private StorageHelper storageHelper = null;

    private final String LaunchCountKey = "RATE_MY_APP_LAUNCH_COUNT";
    private final String ReviewedKey = "RATE_MY_APP_REVIEWED";
    private final String LastLaunchDateKey = "RATE_MY_APP_LAST_LAUNCH_DATE";

    private int firstCount = 5;
    private int secondCount = 10;
    private boolean countDays = false;
    private int state;
    private int launchCount = 0;
    private boolean reviewed = false;
    private Date lastLaunchDate = new Date();
    private boolean reset = false;
    
    public FeedbackHelper()
    {
    	setState(FeedbackState_Active);
    }

    public Date getLastLaunchDate()
    {
        return lastLaunchDate;
	}
    public void setLastLaunchDate(Date value)
    {
    	lastLaunchDate = value;
    }

    public boolean getIsReviewed()
    {
        return reviewed;
    }
    public void setIsReviewed(boolean value)
    {
    	reviewed = value;
    }

    public int getState()
    {
        return state;
    }
    public void setState(int value)
    {
    	state = value;
    }

    public int getLaunchCount()
    {
        return launchCount;
    }
    public void setLaunchCount(int value)
    {
        launchCount = value;
    }

    public int getFirstCount()
    {
        return firstCount;
    }
    public void setFirstCount(int value)
    {
    	firstCount = value;
    }

    public int getSecondCount()
    {
        return secondCount;
    }
    public void setSecondCount(int value)
    {
    	secondCount = value;
     }

    public boolean getCountDays()
    {
        return countDays;
    }
    public void setCountDays(boolean value)
    {
        countDays = value;
    }

    public void launching()
    {
        if(state == FeedbackState_Active)
        {
            loadState();
        }
    }

    /// <summary>
    /// Call when user has reviewed.
    /// </summary>
    public void reviewed()
    {
        setIsReviewed(true);
        storeState();
    }

    /// <summary>
    /// Reset review and feedback launch counter and review state.
    /// </summary>
    public void reset()
    {
        setLaunchCount(0);
        setIsReviewed(false);
        setLastLaunchDate(new Date());
        storeState();
    }

    /// <summary>
    /// Loads last state from storage and works out the new state.
    /// </summary>
    public void loadState()
    {
        try
        {
        	this.storageHelper = new StorageHelper();
        	
            setLaunchCount(Integer.parseInt(storageHelper.getSetting(LaunchCountKey), 10));
            setIsReviewed(Integer.parseInt(storageHelper.getSetting(ReviewedKey), 10) == 1);
            setLastLaunchDate(new Date(Long.parseLong(storageHelper.getSetting(LastLaunchDateKey))));
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            System.out.println("FeedbackHelper.loadState - Failed to load state, Exception: " + e.getMessage());
        }
    }
    public void launch()
    {
    	System.out.println("SETTINGS: " + getLaunchCount() + ", " + getIsReviewed() + ", " + getLastLaunchDate() + ", " + getFirstCount() + ", " + getSecondCount());
        
        long lastLaunchDay = getLastLaunchDate().getTime() / 86400000;
        long thisLaunchDay = new Date().getTime() / 86400000;

        if (!getIsReviewed())
        {
            if (!getCountDays() || lastLaunchDay != thisLaunchDay)
            {
                setLaunchCount(getLaunchCount() + 1);
                setLastLaunchDate(new Date());
            }

            if (getLaunchCount() == getFirstCount())
            {
                setState(FeedbackState_FirstReview);
            }
            else if (getLaunchCount() == getSecondCount())
            {
                setState(FeedbackState_SecondReview);
            }	
            new Thread(){
            	public void run()
            	{
            		storeState();
            	}
            }.start();
        }
    }

    /// <summary>
    /// Stores current state.
    /// </summary>
    private void storeState()
    {
        try
        {
            storageHelper.storeSetting(LaunchCountKey, String.valueOf(getLaunchCount()), true);
            storageHelper.storeSetting(ReviewedKey, String.valueOf(getIsReviewed() ? 1 : 0), true);
            storageHelper.storeSetting(LastLaunchDateKey, String.valueOf(lastLaunchDate.getTime()), true);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            System.out.println("FeedbackHelper.storeState - Failed to store state, Exception: " + e.getMessage());
        }
    }

    public void review() throws Exception
    {
    	RateMyApp.getInstance().getMIDlet().platformRequest("http://www.store.ovi.mobi/content/" + RateMyApp.getInstance().getAppID() + "/comments/add");
			
		reviewed();
    }
}
