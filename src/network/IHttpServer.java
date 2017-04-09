package network;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºIHttpServer.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-7-7 Create
 */

public interface IHttpServer
{
	public void startHttpServer();

	/**
	 * stop the http server thread.
	 */
	public void stopHttpServer();

	/**
	 * tell whether the http thread is running
	 * 
	 * @return
	 */
	public boolean isHttpServerRunning();

	@Override
	public String toString();
}
