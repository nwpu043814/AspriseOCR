package price;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.MainApp;
import setting.Constant;
import setting.Setting;
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

	private float						mPreparePrice	= -0.0F;

	private static final PriceHolder	mHolder			= new PriceHolder();

	private boolean						mHasChased		= false;

	// 2 �� 1�� 4Ϊδ֪
	private int							mChaseDirect	= Constant.DO_UNKNOWN;

	public static PriceHolder getInstance()
	{
		return mHolder;
	}

	public float getPreparePrice()
	{
		return mPreparePrice;
	}

	public float getPrice()
	{
		return mPrice;
	}

	public int getChaseDirect()
	{
		return mChaseDirect;
	}
	
	/**
	 * 
	 * @param direct ȡֵΪConstant.DO_LOW Constant.DO_HIGH �� Constant.DO_UNKNOWN
	 */
	private void setChaseDirect(int direct)
	{
		mChaseDirect = direct;
	}
	
	public String getPriceTime()
	{
		return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(mMillionSeconds));
	}

	private static class TellChaseResult
	{
		public boolean	mShouldDoChaseNow;
		public boolean	mIsDoHigh;

		public TellChaseResult(boolean should, boolean direction)
		{
			mShouldDoChaseNow = should;
			mIsDoHigh = direction;
		}
	};

	/**
	 * �ж��Ƿ�����ִ��׷���Լ�����
	 * 
	 * @param price
	 * @param time
	 * @return
	 */
	private TellChaseResult shouldDoChaseNow(float price, long time)
	{
		Calendar prepareTime = Setting.getInstance().getPrepareTime();
		if (prepareTime == null)
		{
			return new TellChaseResult(false, false);
		}

		long timeSpan = time - prepareTime.getTimeInMillis();
		float priceDiff = Math.abs(price - mPreparePrice);
		boolean should = timeSpan > 0 && timeSpan < Setting.getInstance().getMaxChaseTimeSpan() * 1000
				&& priceDiff >= Setting.getInstance().getChasePriceThreshold() && priceDiff <= Setting.getInstance().getMaxChasePriceDiff();

		return new TellChaseResult(should, price - mPreparePrice > 0);
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

				if (Math.abs(time - Setting.getInstance().getPrepareTime().getTimeInMillis()) < 50 && mPreparePrice < 0.1F)
				{
					Setting.getInstance().setPreparePrice(price);
					mPreparePrice = price;
					mHasChased = false;
					Logger.p("preparePrice=" + price);
				}

				TellChaseResult shouldDoChaseNow = shouldDoChaseNow(price, time);
				if (shouldDoChaseNow.mShouldDoChaseNow && !mHasChased)
				{
					Setting.getInstance().setChaseTime(time);
					Setting.getInstance().setFirePrice(price);
					setChaseDirect(shouldDoChaseNow.mIsDoHigh? Constant.DO_HIGH: Constant.DO_LOW);
					mHasChased = MainApp.getInstance().getActionManager()
							.doImediateTrade(shouldDoChaseNow.mIsDoHigh, Setting.getInstance().getOrigin2Button(shouldDoChaseNow.mIsDoHigh));
				}
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
		return "PriceHolder [mPrice=" + mPrice + "\nmMillionSeconds=" + mMillionSeconds + "\nmPriceTime=" + getPriceTime() + "\nmaxPrice="
				+ mMaxPrice + "\nminPrice=" + mMinPrice + "\nmPreparePrice=" + mPreparePrice + "]";
	}

	/**
	 * ���Ԥ�ü۸����·���׷��
	 */
	public void clearPreparePrice()
	{
		mPreparePrice = -0.0F;
	}

}
