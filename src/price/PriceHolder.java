package price;

import java.text.SimpleDateFormat;
import java.util.Date;

import setting.Constant;
import util.Logger;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName��PriceHolder.java
 * Description��
 * History��
 * 1.0 Denverhan 2013-4-11 Create
 */


public class PriceHolder
{
	private float						mPrice			= -1.0F;
	private long						mMillionSeconds	= 0L;
	private float						mMaxPrice;
	private float						mMinPrice;

	private static final PriceHolder	mHolder			= new PriceHolder();

	public static PriceHolder getInstance()
	{
		return mHolder;
	}

	public float getPrice()
	{
		return mPrice;
	}

	public String getPriceTime()
	{
		return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(mMillionSeconds));
	}

	/**
	 * 
	 * @param price �۸�
	 * @param time ����Ϊ��λ��ʱ��
	 */
	public void updatePrice(float price, long time)
	{
		if (mPrice < 0 && price > 0)
		{
			// �״����ü۸�
			setPrice(price, time);
			mMaxPrice = price;
			mMinPrice = price;
		}
		else
		{
			if (Math.abs(price - mPrice) <= Constant.PRICE_MAX_CHANGE)
			{
				setPrice(price, time);
			}
			else
			{
				Logger.p("error price=" + price);
			}
		}
	}

	private void setPrice(float price, long time)
	{
		// ��֤�������ݲŸ��¼۸�
		if (time <= getMillionSeconds())
		{
			return;
		}

		mPrice = price;

		if (mPrice > mMaxPrice)
		{
			mMaxPrice = mPrice;
		}
		else if (mPrice < mMinPrice)
		{
			mMinPrice = mPrice;
		}

		setMillionSeconds(time);
	}

	public void resetPrice()
	{
		mPrice = -1.0F;
	}

	public long getMillionSeconds()
	{
		return mMillionSeconds;
	}

	public void setMillionSeconds(long millionSeconds)
	{
		if (millionSeconds > mMillionSeconds)
		{
			mMillionSeconds = millionSeconds;
		}
	}

	@Override
	public String toString()
	{
		return "PriceHolder [mPrice=" + mPrice + ", mMillionSeconds=" + mMillionSeconds + ", mPriceTime=" + getPriceTime() + ", maxPrice="
				+ mMaxPrice + ", minPrice=" + mMinPrice + "]";
	}
}
