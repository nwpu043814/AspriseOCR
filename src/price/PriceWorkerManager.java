package price;

import util.Logger;
import util.Statistics;
import worker.Worker;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：PriceCapturer.java
 * Description：
 * History：
 * 1.0 Administrator 2013-7-7 Create
 */

public class PriceWorkerManager extends Worker
{

	private Statistics		mStatistics;

	private Worker[]		mThreads		= null;

	// 线程数目
	private int				mWorkerNumber	= 1;

	private AsyRunPolicy	mThreadPolicy	= null;

	public static interface AsyRunPolicy
	{
		void exec(Runnable r, String name);
	}

	private static class DefaultThreadPolicy implements AsyRunPolicy
	{

		public void exec(Runnable r, String name)
		{
			if (r != null)
			{
				Thread thread = new Thread(r, name);
				thread.setDaemon(true);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.start();
			}
		}
	}

	public void setAsyRunPolicy(AsyRunPolicy p)
	{
		mThreadPolicy = p;
	}

	public PriceWorkerManager(int number)
	{
		if (number > 0)
		{
			mThreads = new Worker[number];
			mWorkerNumber = number;
		}

		mThreadPolicy = new DefaultThreadPolicy();
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

	@Override
	public void startWork()
	{
		super.startWork();
		Statistics st = new Statistics();
		st.setStartSeconds(System.currentTimeMillis() / 1000);
		setStatistics(st);

		for (int i = 0; i < mWorkerNumber; i++)
		{
			mThreads[i] = new PriceSingleWorker();
			((PriceSingleWorker) mThreads[i]).setStatistics(st);
			mThreads[i].startWork();
			mThreadPolicy.exec(mThreads[i], "PriceCapturer[" + i + "]");
		}
		Logger.p("PriceWorkerManager has started, worker number is " + mWorkerNumber);
	}

	@Override
	public void stopWork()
	{
		if (mStatistics != null)
		{
			mStatistics.setEndSeconds(System.currentTimeMillis() / 1000);
		}

		for (Worker w : mThreads)
		{
			if (w != null)
			{
				w.stopWork();
			}
		}
		Logger.p("PriceWorkerManager has stoped");
		super.stopWork();
	}

	@Override
	public String toString()
	{
		return "PriceCapturer [mRun="
				+ mEnableRun
				+ ", mAverageCount="
				+ (mStatistics == null ? 0 : (mEnableRun ? mStatistics.getAverageCount(System.currentTimeMillis() / 1000) : mStatistics
						.getAverageCount()))
				+ "(count/sec), mCount="
				+ (mStatistics != null ? mStatistics.getCount() : 0)
				+ ", mAverageTime="
				+ (mStatistics == null ? 0 : String.format("%.2f", (mEnableRun ? mStatistics.getAverageTime(System.currentTimeMillis() / 1000)
						: mStatistics.getAverageTime()))) + "(ms/count)" + "]";

	}

}
