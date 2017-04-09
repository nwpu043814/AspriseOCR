package price;

import java.awt.image.BufferedImage;

import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Util;
import worker.Worker;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName��PublicResource.java
 * Description��
 * History��
 * 1.0 Denverhan 2013-4-11 Create
 */

/**
 * ������Դ��
 */
public class PriceGetter extends Worker
{
	private String			TAG				= "PriceGetter";
	private int				number			= 0;
	private boolean			mHasWriteImage	= false;
	private BufferedImage	mCaptureImage	= null;

	/**
	 * ���ӹ�����Դ
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
	 * ���ٹ�����Դ
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
	 * @return ץȡ��ͼƬ
	 */
	public BufferedImage getCaptureImage()
	{
		return mCaptureImage;
	}
}
