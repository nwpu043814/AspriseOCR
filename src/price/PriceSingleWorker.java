package price;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Statistics;
import util.Util;
import worker.Worker;

import com.asprise.util.ocr.OCR;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：PriceSingleWorker.java
 * Description：
 * History：
 * 1.0 Administrator 2013-7-11 Create
 */

public class PriceSingleWorker extends Worker
{
	private BufferedImage	mCaptureImage	= null;
	private OCR				mOcr			= null;
	private Robot			mRobot			= null;
	private Statistics		mStatistics;



	@Override
	public void run()
	{
		if (mRobot == null)
		{
			return;
		}

		while (mEnableRun)
		{
			mCaptureImage = capture(Setting.getInstance().getCaptureRect(), Constant.CAPTURE_FILE_NAME, false);
			if (null != mCaptureImage)
			{
				String text = doOCR(mCaptureImage);
				mCaptureImage = null;
				if (text != null && text.trim().length() >= Constant.PRICE_LENGTH)
				{
					float price;
					try
					{
						mStatistics.increaseCount();
						price = Float.parseFloat(Util.exchangeChar(Setting.getInstance().isTrunkPrice() ? Util.trunkFloat(text) : text));
						PriceHolder.getInstance().updatePrice(price, System.currentTimeMillis());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}


	public PriceSingleWorker()
	{
		try
		{
			mRobot = new Robot();
			mOcr = new OCR();
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 设置统计结构体
	 * 
	 * @param st
	 */
	public void setStatistics(Statistics st)
	{
		mStatistics = st;
	}

	private BufferedImage capture(Rectangle rect, String fileName, boolean doTwoValue)
	{
		try
		{
			BufferedImage bi = mRobot.createScreenCapture(rect);

			if (bi != null)
			{
				return zoomInImage(bi, Setting.getInstance().getImageZoomRate());
			}
			else
			{
				Logger.d("Util", "save img failed");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 对图片进行放大
	 * 
	 * @param originalImage 原始图片
	 * @param times 放大倍数
	 * @return
	 */
	public BufferedImage zoomInImage(BufferedImage originalImage, float times)
	{

		// 加速处理
		if (!(times <= 0.9F || times >= 1.1))
		{
			return originalImage;
		}

		int width = (int) (originalImage.getWidth() * times);
		int height = (int) (originalImage.getHeight() * times);
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g = null;
		return newImage;
	}

	private String doOCR(BufferedImage image)
	{
		synchronized (PriceSingleWorker.class)
		{
			return mOcr.recognizeEverything(image);
		}
	}

}
