package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：Logger.java
 * Description：
 * History：
 * 1.0 Administrator 2013-4-13 Create
 */

public class Logger
{
	public static BufferedReader	stdIn	= new BufferedReader(new InputStreamReader(System.in));
	private static PrintWriter		stdOut	= new PrintWriter(System.out, true);
	private static PrintWriter		stdErr	= new PrintWriter(System.err, true);

	public static void d(String tag, String text)
	{
		stdErr.println(tag + ":" + text);
	}

	/**
	 * 打印text后换行
	 * 
	 * @param text
	 */
	public static void p(String text)
	{
		stdOut.println(text);
	}

	/**
	 * 打印text后不换行
	 * 
	 * @param text
	 */
	public static void pcn(String text)
	{
		stdOut.print(text);
		stdOut.flush();
	}
}
