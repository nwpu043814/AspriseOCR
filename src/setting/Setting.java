package setting;

import java.awt.Rectangle;
import java.io.Serializable;



/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：Setting.java
 * Description：
 * History：
 * 1.0 Denverhan 2013-4-11 Create
 */

public class Setting implements Serializable
{
	private static final long		serialVersionUID	= 3050285319497830578L;
	private Rectangle				mCaptureRect;
	private Rectangle				mOcrTestRect;
	private static final Setting	mInstance			= new Setting();

	// 图片缩放率
	private float					mImageZoomRate		= 1.0f;
	private int						mHttpServerPort		= Constant.HTTP_SERVER_DEFAULT_PORT;
	private boolean					mIsTrunkPrice		= false;
	private int						mPriceWorkerNumner	= 1;
	private int						mPriceGetterSleep	= 200;

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
		}
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
		return "Setting [mCaptureRect=" + mCaptureRect + ", HttpServerPort=" + mHttpServerPort + ", OcrTestRect=" + mOcrTestRect + ", imageZoomRate="
				+ mImageZoomRate + ", isTrunkPrice=" + mIsTrunkPrice + ", mPriceGetterSleep=" + mPriceGetterSleep + ", mPriceWorkerNumner="
				+ mPriceWorkerNumner + "]";
	}

	public static Setting getInstance()
	{
		return mInstance;
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
	 * 设置价格获取线程数目
	 * 
	 * @param number
	 */
	public void setPriceWorkerNumber(int number)
	{
		mPriceWorkerNumner = number;
	}

	/**
	 * 
	 * @return获取价格的线程数目
	 */
	public int getPriceWorkerNumber()
	{
		return mPriceWorkerNumner;
	}
}
