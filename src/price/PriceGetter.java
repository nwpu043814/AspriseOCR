package price;

import java.awt.image.BufferedImage;

import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Util;
import worker.Worker;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：PublicResource.java
 * Description：
 * History：
 * 1.0 Denverhan 2013-4-11 Create
 */

/**
 * 公共资源类
 */
public class PriceGetter extends Worker
{
	private String			TAG				= "PriceGetter";
	private int				number			= 0;
	private boolean			mHasWriteImage	= false;
	private BufferedImage	mCaptureImage	= null;

	/**
	 * 增加公共资源
	 */
	public synchronized void WriteImage()
	{
		if (number > 0)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		number++;
		mCaptureImage = Util.capture(Setting.getInstance().getCaptureRect(), Constant.CAPTURE_FILE_NAME, false);
		mHasWriteImage = mCaptureImage != null;
		if (!mHasWriteImage)
		{
			Logger.d(TAG, "WriteImage mHasWriteImage=false");
		}
		notify();
	}

	/**
	 * 减少公共资源
	 */
	public synchronized void ReadImage()
	{
		if (number <= 0)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		number--;
		if (mHasWriteImage)
		{
			String text;
			try
			{
				text = Util.doOCR(mCaptureImage);
				if (text != null && text.trim().length() >= Constant.PRICE_LENGTH)
				{
					float price = Float.parseFloat(Util.exchangeChar(Setting.getInstance().isTrunkPrice() ? Util.trunkFloat(text) : text));
					if (price > 1000.0F)
					{
						PriceHolder.getInstance().updatePrice(price, System.currentTimeMillis());
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		notify();
	}

	@Override
	public String toString()
	{
		return "PriceGetter [mRun=" + mEnableRun + "]";
	}

	/**
	 * 
	 * @return 抓取的图片
	 */
	public BufferedImage getCaptureImage()
	{
		return mCaptureImage;
	}
}
