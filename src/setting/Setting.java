package setting;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import price.PriceHolder;
import util.Util;



/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName��Setting.java
 * Description��
 * History��
 * 1.0 Denverhan 2013-4-11 Create
 */

public class Setting implements Serializable
{
	private static final long		serialVersionUID		= 3050285319497830578L;
	private Rectangle				mCaptureRect;
	private Rectangle				mOcrTestRect;
	private static final Setting	mInstance				= new Setting();

	// ͼƬ������
	private float					mImageZoomRate			= 1.0f;
	private int						mHttpServerPort			= Constant.HTTP_SERVER_DEFAULT_PORT;
	private boolean					mIsTrunkPrice			= false;
	private int						mPriceWorkerNumner		= 1;
	private int						mPriceGetterSleep		= 200;

	private Point					mOrigin2DoLowButton		= new Point(0, 0);
	private Point					mOrigin2DoHighButton	= new Point(0, 0);
	private Calendar				mPrepareTime			= Calendar.getInstance();
	private int						mMaxChasePriceDiff		= 20;
	private int						mMaxChaseTimeSpan		= 30;
	private float					mPreparePrice			= 0.0F;
	private float					mFirePrice				= 0.0F;
	private float					mChasePriceThreshold	= 5;
	private long					mChaseTime				= 0L;

	private Setting()
	{
	}

	public void copy(Setting s)
	{
		if (s != null && s != this)
		{
			mCaptureRect = s.getCaptureRect();
			mOcrTestRect = s.getOcrTestRect();
			mImageZoomRate = s.getImageZoomRate();
			mIsTrunkPrice = s.isTrunkPrice();
			mPriceGetterSleep = s.getPriceGetterSleep();
			mHttpServerPort = s.getHttpServerPort();
			mPriceWorkerNumner = s.getPriceWorkerNumber();
			setOrigin2Button(false, s.getOrigin2Button(false));
			setOrigin2Button(true, s.getOrigin2Button(true));
			mPrepareTime = s.getPrepareTime();
			mMaxChasePriceDiff = s.getMaxChasePriceDiff();
			mMaxChaseTimeSpan = s.getMaxChaseTimeSpan();
			mChasePriceThreshold = s.getChasePriceThreshold();
			mPreparePrice = s.getPreparePrice();
			mChaseTime = s.getChaseTime();
			mFirePrice = s.getFirePrice();
		}
	}

	public float getPreparePrice()
	{
		return mPreparePrice;
	}

	public long getChaseTime()
	{
		return mChaseTime;
	}

	public void setChaseTime(long chaseTime)
	{
		mChaseTime = chaseTime;
	}

	public void setCaptureRect(Rectangle rect)
	{
		mCaptureRect = rect;
	}

	public Rectangle getCaptureRect()
	{
		return mCaptureRect;
	}

	@Override
	public String toString()
	{
		return "Setting[\nCaptureRect=" + mCaptureRect + "\nHttpServerPort=" + mHttpServerPort + "\nOcrTestRect=" + mOcrTestRect + "\nImageZoomRate="
				+ mImageZoomRate + "\nIsTrunkPrice=" + mIsTrunkPrice + "\nPriceGetterSleep=" + mPriceGetterSleep + "\nPriceWorkerNumner="
				+ mPriceWorkerNumner + "\nOrigin2High=" + getOrigin2Button(true) + "\nOrigin2Low=" + getOrigin2Button(false) + "\nMaxChaseTimeSpan="
				+ mMaxChaseTimeSpan + "\nMaxChasePriceDiff=" + mMaxChasePriceDiff + "\nChasePriceThreshold=" + mChasePriceThreshold
				+ "\nPrePareTime=" + Util.formatDate(mPrepareTime.getTime()) + "\nPreparePrice=" + mPreparePrice + "\nFireTime=" + mChaseTime + ","
				+ Util.seconds2Time(mChaseTime) + "\nFirePrice=" + mFirePrice + "]";
	}

	public static Setting getInstance()
	{
		return mInstance;
	}
	
	public void setPreparePrice(float p)
	{
		mPreparePrice = p;
	}

	public int getHttpServerPort()
	{
		return mHttpServerPort;
	}

	public void setHttpServerPort(int mHttpServerPort)
	{
		this.mHttpServerPort = mHttpServerPort;
	}

	public Rectangle getOcrTestRect()
	{
		return mOcrTestRect;
	}

	public void setOcrTestRect(Rectangle ocrTestRect)
	{
		mOcrTestRect = ocrTestRect;
	}

	public float getImageZoomRate()
	{
		return mImageZoomRate;
	}

	public void setImageZoomRate(float imageZoomRate)
	{
		mImageZoomRate = imageZoomRate;
	}

	public boolean isTrunkPrice()
	{
		return mIsTrunkPrice;
	}

	public void setTrunkPrice(boolean isTrunkPrice)
	{
		mIsTrunkPrice = isTrunkPrice;
	}

	public int getPriceGetterSleep()
	{
		return mPriceGetterSleep;
	}

	public void setPriceGetterSleep(int priceGetterSleep)
	{
		mPriceGetterSleep = priceGetterSleep;
	}

	/**
	 * ���ü۸��ȡ�߳���Ŀ
	 * 
	 * @param number
	 */
	public void setPriceWorkerNumber(int number)
	{
		mPriceWorkerNumner = number;
	}

	/**
	 * 
	 * @return��ȡ�۸���߳���Ŀ
	 */
	public int getPriceWorkerNumber()
	{
		return mPriceWorkerNumner;
	}


	/**
	 * ���ԭ�㵽��ť��λ�ơ�
	 * 
	 * @param isDoHigh -true ���� ������Ϊ����
	 * @return
	 */
	public Point getOrigin2Button(boolean isDoHigh)
	{
		return isDoHigh ? mOrigin2DoHighButton : mOrigin2DoLowButton;
	}

	/**
	 * ����ԭ�㵽��ť��λ�ơ�
	 * 
	 * @param isDoHigh -true ���� ������Ϊ����
	 * @param point �µ�ԭ�㵽�µ���ť�ľ��롣
	 */
	public void setOrigin2Button(boolean isDoHigh, Point point)
	{
		if (point == null)
		{
			return;
		}

		if (isDoHigh)
		{
			mOrigin2DoHighButton = new Point(point);
		}
		else
		{
			mOrigin2DoLowButton = new Point(point);
		}
	}

	/**
	 * �������׷�����
	 * 
	 * @param time
	 */
	public void setMaxChaseTimeSpan(int time)
	{
		mMaxChaseTimeSpan = time;
	}

	/**
	 * �������׷�����
	 * 
	 * @return
	 */
	public int getMaxChaseTimeSpan()
	{
		return mMaxChaseTimeSpan;
	}

	/**
	 * �������׷���۲�
	 * 
	 * @param diff
	 */
	public void setMaxChasePriceDiff(int diff)
	{
		mMaxChasePriceDiff = diff;
	}

	/**
	 * ������׷���۲�
	 * 
	 * @return
	 */
	public int getMaxChasePriceDiff()
	{
		return mMaxChasePriceDiff;
	}


	/**
	 * ����׷���۸���ֵ
	 * 
	 * @param threshold
	 */
	public void setChasePriceThreshold(float threshold)
	{
		if (threshold > 0.1F)
		{
			mChasePriceThreshold = threshold;
		}
	}

	/**
	 * 
	 * @return ����׷���۸���ֵ
	 */
	public float getChasePriceThreshold()
	{
		return mChasePriceThreshold;
	}

	/**
	 * �Զ�׷����ʼʱ��
	 */
	public Calendar getPrepareTime()
	{
		return mPrepareTime;
	}

	/**
	 * �Զ�׷����ʼʱ��
	 */
	public void setPrepareTime(Calendar cal)
	{
		mPrepareTime = cal;
	}

	public void setFirePrice(float pr)
	{
		mFirePrice = pr;
	}

	public float getFirePrice()
	{
		return mFirePrice;
	}
}
