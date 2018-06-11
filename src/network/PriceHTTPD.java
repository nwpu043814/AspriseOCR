package network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import main.MainApp;

import org.json.JSONException;
import org.json.JSONObject;

import price.PriceHolder;
import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Util;
import worker.Worker;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºPriceHTTPD.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-7-7 Create
 */

public class PriceHTTPD extends NanoHTTPD implements IHttpServer
{
	private static final String			TAG					= "PriceHTTPD";
	private static final IHttpServer	mInstance			= new PriceHTTPD(Setting.getInstance().getHttpServerPort());
	private String						mStartTime;
	private String						mStopTime;
	public boolean						mRun				= false;
	private PriceHolder					mPriceHolder		= PriceHolder.getInstance();
	private int							mCount				= 0;
	private long						mStartCount			= 0L;
	private float						mLastQueryUseTime	= 0.0F;
	private	String						mPriceWorkerRate	= "0.0";
	
	private JSONObject mJsn = new JSONObject();

	/**
	 * Default threading strategy for NanoHttpd.
	 * 
	 * <p>
	 * By default, the server spawns a new Thread for every incoming request.
	 * These are set to <i>daemon</i> status, and named according to the request
	 * number. The name is useful when profiling the application.
	 * </p>
	 */
	private static class DefaultAsyncRunner implements AsyncRunner
	{
		private long	requestCount;

		@Override
		public void exec(Runnable code)
		{
			++requestCount;
			Thread t = new Thread(code);
			Logger.p("requestCount=" + requestCount);
			t.setDaemon(true);
			t.setPriority(Thread.MAX_PRIORITY);
			t.setName("PriceHTTPD Request Processor (#" + requestCount + ")");
			t.start();
		}
	}
	
	private PriceHTTPD(int port)
	{
		super(port);
		setAsyncRunner(new DefaultAsyncRunner());
	}

	public static IHttpServer getInstance()
	{
		return mInstance;
	}

	/**
	 * This is the main fucntion to run the class
	 * 
	 * @param args is never used
	 */
	public void startHttpServer()
	{
		if (!mRun)
		{
			mStartTime = Util.getCurrentTime();
			try
			{
				start();
				mRun = true;
				Logger.p("HttpServer has started.");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Logger.d(TAG, "HttpServer is always Running.");
		}
	}

	/**
	 * stop the http server thread.
	 */
	public void stopHttpServer()
	{
		mRun = false;
		mStopTime = Util.getCurrentTime();
		stop();
		Logger.p("HttpServer has stopped");
	}

	/**
	 * tell whether the http thread is running
	 * 
	 * @return
	 */
	public boolean isHttpServerRunning()
	{
		return mRun;
	}

	@Override
	public String toString()
	{
		String startTime = mStartTime != null ? mStartTime : "";
		String stopTime = mStopTime != null ? mStopTime : "";
		return "ThreadedServer [mRun=" + mRun + ", startTime=" + startTime + ", stopTime=" + stopTime + ", httpPort="
				+ Setting.getInstance().getHttpServerPort() + "]";
	}

	public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("<html><body><h1>Hello server</h1>\n");
		if (parms.get("username") == null)
		{
			if (parms.get(Constant.HTML_REQUEST_CURRENT_TIME) != null)
			{
				msg.setLength(0);
				
				try
				{
					mJsn.put(Constant.NETWORK_KEY_CHASE_DIRECT, mPriceHolder.getChaseDirect());
					mJsn.put(Constant.NETWORK_KEY_PRICE, mPriceHolder.getPrice());
					mJsn.put(Constant.NETWORK_KEY_MILLION_SECOND, mPriceHolder.getMillionSeconds());
					mJsn.put(Constant.NETWORK_KEY_TIME, new String(mPriceHolder.getPriceTime().getBytes(Constant.ENCODE_UTF_8), Constant.ENCODE_UTF_8));
					mJsn.put(Constant.NETWORK_KEY_QUERY_TIME, new String(String.format("%.2f" ,mLastQueryUseTime).getBytes(Constant.ENCODE_UTF_8), Constant.ENCODE_UTF_8));
					mJsn.put(Constant.NETWORK_KEY_CAPTURE_TIME, new String(mPriceWorkerRate.getBytes(Constant.ENCODE_UTF_8), Constant.ENCODE_UTF_8));
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				catch (UnsupportedEncodingException ue)
				{
					ue.printStackTrace();
				}
				
				updateQueryTime();
				msg.append(mJsn.toString());
			}
			else
			{
				msg.append("<form action='?' method='get'>\n").append("  <p>Your name: <input type='text' name='username'></p></form>\n");
				msg.append("</body></html>\n");
			}
		}
		else
		{
			msg.append("<p>Hello, ").append(parms.get("username")).append("!</p>");
			msg.append("</body></html>\n");
		}

		return new NanoHTTPD.Response(msg.toString());
	}

	private void updateQueryTime()
	{
		if (mCount++ == 1)
		{
			mStartCount = System.currentTimeMillis();
		}
		
		if (mCount >= Constant.QUERY_TIME_MAX_NUMBER)
		{
			mLastQueryUseTime = (System.currentTimeMillis() - mStartCount + 0.001F) / mCount;
			mCount = 0;
			
			Worker priceWorker = MainApp.getInstance().getPriceWorker();
			if (priceWorker != null)
			{
				String text = priceWorker.toString();
				int end = text.indexOf("(ms/count)");
				int start = text.indexOf("mAverageTime=");
				mPriceWorkerRate = text.substring(start + 13, end);
			}
		}
	}

}
