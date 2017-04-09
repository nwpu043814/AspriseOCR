package worker;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºworker.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-7-7 Create
 */

public class Worker implements Runnable
{
	protected boolean	mEnableRun	= true;

	public void enableRun(boolean run)
	{
		mEnableRun = run;
	}

	public boolean isEnableRun()
	{
		return mEnableRun;
	}

	public void startWork()
	{
		mEnableRun = true;
	}

	public void stopWork()
	{
		mEnableRun = false;
	}

	public String toString()
	{
		return null;
	}

	@Override
	public void run()
	{
	}
}
