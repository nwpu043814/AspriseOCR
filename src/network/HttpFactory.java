package network;

import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºHttpFactory.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-7-7 Create
 */

public class HttpFactory
{
	public static final int				SERVER_TYPE_THREADED	= 0X1;
	public static final int				SERVER_TYPE_NANOHTTPD	= 0X2;

	private static HttpFactory			mInstance				= new HttpFactory();

	private Map<Integer, IHttpServer>	mHttpServers;

	public static HttpFactory getInstance()
	{
		return mInstance;
	}

	private HttpFactory()
	{
		mHttpServers = new HashMap<Integer, IHttpServer>();
	}

	public IHttpServer getHttpServer(int type)
	{
		switch (type)
		{
			case SERVER_TYPE_THREADED:
			{
				IHttpServer server = mHttpServers.get(Integer.valueOf(type));
				if (server == null)
				{
					server = ThreadedServer.getInstance();
					mHttpServers.put(Integer.valueOf(type), server);
				}
				return server;
			}
			case SERVER_TYPE_NANOHTTPD:
			{
				IHttpServer server = mHttpServers.get(Integer.valueOf(type));
				if (server == null)
				{
					server = PriceHTTPD.getInstance();
					mHttpServers.put(Integer.valueOf(type), server);
				}
				return server;
			}
			default:
			{
				return null;
			}
		}
	}
}
