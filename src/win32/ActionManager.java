package win32;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

import setting.Constant;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºActionManager.java
 * Description£º
 * History£º
 * 1.0 Administrator 2013-8-4 Create
 */

public class ActionManager implements IActionManager
{
	public static final int				ACTION_HUIFENG	= 1;
	public static final int				ACTION_ZHONGXIN	= 2;
	private Robot						mRobot			= null;
	private Action						mActionImpl		= null;

	private static Map<Integer, Action>	mActions		= new HashMap<Integer, Action>();

	public void setActionImpl(Action action)
	{
		mActionImpl = action;
	}

	public Action getAutoAction(int type)
	{
		if (mActions.containsKey(Integer.valueOf(type)))
		{
			return mActions.get(Integer.valueOf(type));
		}
		else if (type == ACTION_ZHONGXIN)
		{
			mActions.put(Integer.valueOf(type), new Action()
			{
				@Override
				public boolean doImediateTrade(boolean isHigh, Point origin2Button)
				{
					Point coor = getSunAwtDialogOriginCoordinate();
					if (coor == null)
					{
						return false;
					}
					mRobot.mouseMove(origin2Button.x + coor.x, origin2Button.y + coor.y);
					mRobot.mousePress(InputEvent.BUTTON1_MASK);
					mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					return true;
				}
			});
		}
		else if (type == ACTION_HUIFENG)
		{
			mActions.put(Integer.valueOf(type), new Action()
			{
				@Override
				public boolean doImediateTrade(boolean isHigh, Point origin2Button)
				{
					Point coor = getHFDialogOriginCoordinate();
					if (coor == null)
					{
						return false;
					}

					if (isHigh)
					{
						origin2Button.x = 60;
						origin2Button.y = 230;
					}
					else
					{
						origin2Button.x = 122;
						origin2Button.y = 230;
					}

					nextHop(coor.x += origin2Button.x, coor.y += origin2Button.y);

					if (isHigh)
					{
						origin2Button.x = 76;
						origin2Button.y = 248;
					}
					else
					{
						origin2Button.x = 14;
						origin2Button.y = 248;
					}

					nextHop(coor.x += origin2Button.x, coor.y += origin2Button.y);
					return true;
				}
			});
		}

		return mActions.get(Integer.valueOf(type));
	}

	public ActionManager()
	{
		try
		{
			mRobot = new Robot();

			setActionImpl(getAutoAction(ACTION_ZHONGXIN));
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	public boolean doImediateTrade(boolean isHigh, Point origin2Button)
	{
		if (mActionImpl != null)
		{
			return mActionImpl.doImediateTrade(isHigh, origin2Button);
		}

		return false;
	}

	private Point getSunAwtDialogOriginCoordinate()
	{
		MFCProxy user = MFCProxy.INSTANCE;

		HWND findWindow = user.FindWindow(Constant.DO_TRADE_DIALOG_CLASS_NAME, null);

		if (findWindow == null)
		{
			System.out.println("findwindow failed");
			return null;
		}
		RECT wndRect = new RECT();
		if (user.GetWindowRect(findWindow, wndRect))
		{
			return new Point(wndRect.left, wndRect.top);
		}

		return null;
	}

	private Point getHFDialogOriginCoordinate()
	{
		MFCProxy user = MFCProxy.INSTANCE;

		HWND findWindow = user.FindWindow(null, "½¨²Öµ¥");

		if (findWindow == null)
		{
			System.out.println("findwindow failed");
			return null;
		}
		RECT wndRect = new RECT();
		if (user.GetWindowRect(findWindow, wndRect))
		{
			return new Point(wndRect.left, wndRect.top);
		}

		return null;
	}

	private void nextHop(int x, int y)
	{
		mRobot.mouseMove(x, y);
		click();
	}

	private void moveTo(int x, int y)
	{
		mRobot.mouseMove(x, y);
	}

	private void click()
	{
		mRobot.mousePress(InputEvent.BUTTON1_MASK);
		mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}
