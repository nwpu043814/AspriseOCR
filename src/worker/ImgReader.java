package worker;

import price.PriceGetter;
import setting.Setting;
/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºImageReader.java
 * Description£º
 * History£º
 * 1.0 Denverhan 2013-4-11 Create
 */

public class ImgReader extends ImgWorker implements Runnable
{
	public ImgReader(PriceGetter resource)
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
			mPriceGetter.ReadImage();
		}
	}
}
