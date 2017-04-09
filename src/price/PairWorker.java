package price;

import java.awt.Rectangle;

import setting.Setting;
import util.Logger;
import worker.ImgReader;
import worker.ImgWriter;
import worker.Worker;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºPairWorker.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-7-7 Create
 */

public class PairWorker extends Worker
{
	private PriceGetter	mPriceGetter;
	private Thread		mImgWriter;
	private Thread		mImgReader;

	public PairWorker()
	{
		mPriceGetter = new PriceGetter();
	}

	@Override
	public void startWork()
	{

		Rectangle captureRect = Setting.getInstance().getCaptureRect();
		if (!mPriceGetter.isEnableRun() && captureRect != null && !captureRect.isEmpty())
		{
			PriceHolder.getInstance().resetPrice();
			mPriceGetter.enableRun(true);
			mImgWriter = new Thread(new ImgWriter(mPriceGetter), "ImgWriter");
			mImgReader = new Thread(new ImgReader(mPriceGetter), "ImgReader");
			mImgWriter.start();
			mImgReader.start();
			super.startWork();
			Logger.p("Price Getter has started");
		}
		else
		{
			if (mPriceGetter.isEnableRun())
			{
				Logger.p("Price Getter is always Working");
			}
			else
			{
				Logger.p("CapureRect is valid:" + captureRect);
			}
		}
	}

	@Override
	public void stopWork()
	{
		super.stopWork();
		mPriceGetter.enableRun(false);
		Logger.p("Price Getter has stopped");
	}
}
