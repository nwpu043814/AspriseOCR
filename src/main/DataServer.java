package main;

import java.io.IOException;

import util.Logger;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºMainDriver.java
 * Description£º
 * History£º
 * 1.0 Denverhan 2013-4-11 Create
 */

public class DataServer
{
	public static void main(String[] arg)
	{
		MainApp app = MainApp.getInstance();
		boolean continues = true;
		while (continues)
		{
			app.dipslayMenu();
			String userInput;
			int userRequest = 0;
			try
			{
				userInput = Logger.stdIn.readLine();
				userRequest = app.dispatchUserInput(userInput);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (userRequest == MainApp.USER_RQUEST_TYPE_QUIT)
			{
				continues = false;
			}
		}
	}
}
