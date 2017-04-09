package network;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：ThreadedServer.java
 * Description：
 * History：
 * 1.0 Administrator 2013-7-7 Create
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import price.PriceHolder;
import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Util;


/**
 * This class simulate the Server of http/1.0 in thread
 * 
 * @author HanLian
 * @version 1.0
 */
public class ThreadedServer implements IHttpServer
{
	private static final String			TAG			= "ThreadedServer";
	public boolean						mRun		= false;
	private ServerSocket				mSocketServer;
	private static final IHttpServer	mInstance	= new ThreadedServer();
	private String						mStartTime;
	private String						mStopTime;


	private ThreadedServer()
	{
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
			mRun = true;
			mSocketServer = null;
			mStartTime = Util.getCurrentTime();
			Runnable run = new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						mSocketServer = new ServerSocket(Setting.getInstance().getHttpServerPort());
						Logger.d(TAG, "Server has run...");

						while (mRun)
						{
							Socket client = null;
							try
							{
								client = mSocketServer.accept();
								Logger.d(TAG, "Received from: " + client.getInetAddress() + ":" + client.getPort());
								new Handler(client);
							}
							catch (Exception e)
							{
								Logger.d(TAG, e.getMessage());
							}
						}
					}
					catch (IOException e)
					{
						Logger.d(TAG, "start HttpServer failed");
						e.printStackTrace();
					}
				}
			};

			new Thread(run, "ThreadedServer").start();
			Logger.p("HttpServer has started");
		}
		else
		{
			Logger.d(TAG, "HttpServer has already Run");
		}
	}

	/**
	 * stop the http server thread.
	 */
	public void stopHttpServer()
	{
		mRun = false;
		mStopTime = Util.getCurrentTime();
		if (mSocketServer != null && !mSocketServer.isClosed())
		{
			try
			{
				mSocketServer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Logger.p("HttpServer has stopped");
	}

	/**
	 * tell whether the http thread is running
	 * 
	 * @return
	 */
	public boolean isHttpServerRunning()
	{
		return mRun && mSocketServer != null && !mSocketServer.isClosed();
	}

	@Override
	public String toString()
	{
		String startTime = mStartTime != null ? mStartTime : "";
		String stopTime = mStopTime != null ? mStopTime : "";
		return "ThreadedServer [mRun=" + mRun + ", startTime=" + startTime + ", stopTime=" + stopTime + ", httpPort="
				+ Setting.getInstance().getHttpServerPort() + "]";
	}
}

/*
 * This clas implements the interface Runnanle to respond user's request
 */
class Handler implements Runnable
{
	private Socket	socket;

	/*
	 * construct a Handler with a Socket object
	 * @param s is a Socket object
	 */
	public Handler(Socket s)
	{

		Thread t;
		socket = s;
		t = new Thread(this, "Socket Handler Thread");
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{

		try
		{
			String request = "";

			// Get string from client through BufferedWreader inString
			BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

			// Get a request from client
			request = inStream.readLine();

			if (Util.isEmpty(request))
			{
				return;
			}

			StringTokenizer token = new StringTokenizer(request, " ");
			Logger.d("SocketHandler", request);

			// Get the name of the client's request file
			request = token.nextToken();

			if (request.toUpperCase().equals("GET"))
			{
				// If the commend is "GET"
				try
				{
					request = token.nextToken();
					if (request.startsWith("/"))
					{
						// If the commend is "GET"
						request = request.substring(1);
					}
					outStream.writeBytes(getResponse(request));
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					outStream.writeBytes("\r\n");
					outStream.writeBytes("HTTP/1.0 500 Internal Server Error");
					Logger.d("SocketHandler", "Error in respond()!\n");
					e.printStackTrace();
				}

				socket.close();
			}
			else
			{
				try
				{
					outStream.writeBytes("HTTP/1.0 400 Bad Request\r\n");
					socket.close();
				}
				catch (RuntimeException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			Logger.d("SocketHandler", "Error in run()");
			e.printStackTrace();
		}
	}

	/**
	 * 根据请求返回文本到客户端。
	 * 
	 * @param text
	 * @return
	 */
	private String getResponse(String text)
	{
		String ret = "";
		if (text.toLowerCase().contains(Constant.HTML_REQUEST_CURRENT_TIME))
		{
			PriceHolder price = PriceHolder.getInstance();
			ret = price.getPrice() + ":" + price.getMillionSeconds() + ":" + price.getPriceTime();
		}

		return ret;
	}
}
