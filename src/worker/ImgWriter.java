package worker;

import price.PriceGetter;
import setting.Setting;
/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：ImageWriter.java
 * Description：
 * History：
 * 1.0 Denverhan 2013-4-11 Create
 */

/**
 * 生产者线程，负责生产公共资源
 */
public class ImgWriter extends ImgWorker implements Runnable
{
	public ImgWriter(PriceGetter resource)
	{
		this.mPriceGetter = resource;
	}

	@Override
	public void run()
	{
		while (mPriceGetter.isEnableRun())
		{
			try
			{
				Thread.sleep((long) (Setting.getInstance().getPriceGetterSleep()));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			mPriceGetter.WriteImage();
		}
	}
}
