package util;

import java.util.concurrent.atomic.AtomicInteger;

import setting.Constant;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：Statistics.java
 * Description：
 * History：
 * 1.0 Administrator 2013-7-7 Create
 */


public class Statistics
{
	private long			mStartSeconds	= 0L;					// 开始时间
	private long			mEndSeconds		= 0L;					// 开始时间
	private AtomicInteger	mCount			= new AtomicInteger(0);

	public void increaseCount()
	{
		if(mCount.getAndIncrement() > Constant.PRICE_GETTER_USE_TIME_COUNT_MAX)
		{
			mStartSeconds = System.currentTimeMillis()/1000;
			mCount.set(0);
		}
	}

	public void setStartSeconds(long time)
	{
		if (time > 0)
		{
			mStartSeconds = time;
		}
	}

	public void setEndSeconds(long endTime)
	{
		if (endTime > mStartSeconds)
		{
			mEndSeconds = endTime;
		}
	}

	public int getCount()
	{
		return mCount.get();
	}

	public long getAverageCount()
	{
		return getAverageCount(mEndSeconds);
	}

	public long getAverageCount(long endSeconds)
	{
		if (endSeconds > mStartSeconds)
		{
			return getCount() / (endSeconds - mStartSeconds);
		}

		return Integer.MAX_VALUE;
	}

	public float getAverageTime()
	{
		return getAverageTime(mEndSeconds);
	}

	public float getAverageTime(long endSeconds)
	{
		if (endSeconds > mStartSeconds && getCount() > 0)
		{
			return (endSeconds - mStartSeconds) * 1000.00F / getCount();
		}

		return Integer.MAX_VALUE;
	}
}
