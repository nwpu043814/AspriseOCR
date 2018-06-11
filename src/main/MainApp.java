package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import javax.imageio.ImageIO;

import network.HttpFactory;
import network.IHttpServer;
import price.PriceHolder;
import price.PriceWorkerManager;
import setting.Constant;
import setting.Setting;
import util.Logger;
import util.Util;
import win32.ActionManager;
import win32.IActionManager;
import worker.Worker;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：MainApp.java
 * Description：
 * History：
 * 1.0 Administrator 2013-4-13 Create
 */

public class MainApp
{
	public static final int	USER_RQUEST_TYPE_START_ALL_SERVICE					= 1;
	public static final int	USER_RQUEST_TYPE_STOP_ALL_SERVICE					= 2;
	public static final int	USER_RQUEST_TYPE_START_HTTP_SERVER					= 3;
	public static final int	USER_RQUEST_TYPE_STOP_HTTP_SERVER					= 4;
	public static final int	USER_RQUEST_TYPE_START_PRICE_UPDATE_SERVICE			= 5;
	public static final int	USER_RQUEST_TYPE_STOP_PRICE_UPDATE_SERVICE			= 6;
	public static final int	USER_RQUEST_TYPE_SET_CAPTURE_COORDINATE				= 7;
	public static final int	USER_RQUEST_TYPE_DISPLAY_ALL_SERVICES_STATUS		= 8;
	public static final int	USER_RQUEST_TYPE_QUIT								= 30;
	public static final int	USER_RQUEST_TYPE_TEST_OCR							= 11;
	public static final int	USER_RQUEST_TYPE_SET_HTTP_SERVER_PORT				= 9;
	public static final int	USER_RQUEST_TYPE_SET_OCR_TEST_RECT					= 10;
	public static final int	USER_RQUEST_TYPE_COPY_OCR_TEST_RECT_TO_CAPTURE		= 12;
	public static final int	USER_RQUEST_TYPE_SET_IMAGE_ZOOM_RATE				= 13;
	public static final int	USER_RQUEST_TYPE_TRUNK_PRICE						= 14;
	public static final int	USER_RQUEST_TYPE_SET_PRICE_GETTER_SLEEP_INTERVAL	= 15;
	public static final int	USER_RQUEST_TYPE_SET_PRICE_GETTER_NUMBER			= 16;
	public static final int	USER_RQUEST_TYPE_SET_ORIGIN_TO_DOHIGH_BUTTON		= 17;
	public static final int	USER_RQUEST_TYPE_SET_ORIGIN_TO_DOLOW_BUTTON			= 18;
	public static final int	USER_RQUEST_TYPE_SET_PREPARE_TIME					= 19;
	public static final int	USER_RQUEST_TYPE_SET_MAX_CHASE_TIME_SPAN			= 20;
	public static final int	USER_RQUEST_TYPE_SET_MAX_CHASE_PRICE_DIFF			= 21;
	public static final int	USER_RQUEST_TYPE_SET_CHASE_THRESHOLD				= 22;
	private Worker			mPriceWorker										= null;
	private static MainApp	mInstance											= null;
	private IActionManager	mActionManager										= new ActionManager();


	public static MainApp getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new MainApp();
		}

		return mInstance;
	}

	public Worker getPriceWorker()
	{
		return mPriceWorker;
	}

	private MainApp()
	{
		loadConfig();
	}

	public void start()
	{
		startPriceGetter();
		startHttpServer();
	}

	public void stop()
	{
		stopPriceGetter();
		stopHttpServer();
	}

	public void startPriceGetter()
	{
		if (mPriceWorker != null && mPriceWorker.isEnableRun())
		{
			mPriceWorker.stopWork();
		}
		mPriceWorker = new PriceWorkerManager(Setting.getInstance().getPriceWorkerNumber());
		mPriceWorker.startWork();
	}

	public void stopPriceGetter()
	{
		if (mPriceWorker != null && mPriceWorker.isEnableRun())
		{
			mPriceWorker.stopWork();
		}
	}

	public void stopHttpServer()
	{
		HttpFactory.getInstance().getHttpServer(Constant.HTTP_SERVER_TYPE).stopHttpServer();
	}

	public void startHttpServer()
	{
		HttpFactory.getInstance().getHttpServer(Constant.HTTP_SERVER_TYPE).startHttpServer();
	}

	public boolean isHttpServerRunning()
	{
		return HttpFactory.getInstance().getHttpServer(Constant.HTTP_SERVER_TYPE).isHttpServerRunning();
	}

	public void dipslayMenu()
	{
		Logger.p("\r\nDataServer 1.8.1");
		Logger.p("1. Start all service");
		Logger.p("2. Stop all service");
		Logger.p("3. Start HTTP server");
		Logger.p("4. Stop HTTP server");
		Logger.p("5. Start Price Update Service");
		Logger.p("6. Stop Price Update Service");
		Logger.p("7. Set capture coordinate");
		Logger.p("8. Display all services status");
		Logger.p("9. Set Http Server port");
		Logger.p("10. Set OCR Test rectangle");
		Logger.p("11. Test OCR");
		Logger.p("12. Copy OCR Test rect to capture rect");
		Logger.p("13. Set image zoom rate");
		Logger.p("14. Set trunk price");
		Logger.p("15. Set Price Getter sleep interval");
		Logger.p("16. Set Price Getter number");
		Logger.p("17. Set origin to do high button");
		Logger.p("18. Set origin to do low button");
		Logger.p("19. Set prepare time");
		Logger.p("20. Set max chase time pan");
		Logger.p("21. Set max chase price diff");
		Logger.p("22. Set chase price threshold");
		Logger.p("30. Quit");
		Logger.pcn("Please input select:");
	}

	public int dispatchUserInput(String input)
	{
		int userInput = 0;

		if (input == null || input.trim().length() == 0)
		{
			Logger.d("User Menu", "Invalid user input.");
			return userInput;
		}

		try
		{
			userInput = Integer.parseInt(input);
		}
		catch (Exception e)
		{
			Logger.d("User Menu", "Invalid user input.");
			return userInput;
		}

		IHttpServer httpServer = HttpFactory.getInstance().getHttpServer(HttpFactory.SERVER_TYPE_NANOHTTPD);
		switch (userInput)
		{
			case USER_RQUEST_TYPE_START_ALL_SERVICE:
			{
				start();
				break;
			}
			case USER_RQUEST_TYPE_STOP_ALL_SERVICE:
			{
				stop();
				break;
			}
			case USER_RQUEST_TYPE_START_HTTP_SERVER:
			{
				startHttpServer();
				break;
			}
			case USER_RQUEST_TYPE_STOP_HTTP_SERVER:
			{
				stopHttpServer();
				break;
			}
			case USER_RQUEST_TYPE_START_PRICE_UPDATE_SERVICE:
			{
				startPriceGetter();
				break;
			}
			case USER_RQUEST_TYPE_STOP_PRICE_UPDATE_SERVICE:
			{
				stopPriceGetter();
				break;
			}
			case USER_RQUEST_TYPE_SET_CAPTURE_COORDINATE:
			{
				Logger.pcn("Please input new coordinate:");
				String param = Util.getConsoleInput();
				String[] arg = param.split(" ");
				final Rectangle rect = Util.parseInputParams(arg);
				if (rect != null)
				{
					Logger.p("rect=" + rect.toString());
					Setting.getInstance().setCaptureRect(rect);
				}
				else
				{
					Logger.p("Invalid input.");
				}

				break;
			}
			case USER_RQUEST_TYPE_DISPLAY_ALL_SERVICES_STATUS:
			{
				Logger.p(Setting.getInstance().toString());
				Logger.p(PriceHolder.getInstance().toString());
				Logger.p(httpServer.toString());
				if (mPriceWorker != null)
				{
					Logger.p(mPriceWorker.toString());
				}
				break;
			}
			case USER_RQUEST_TYPE_QUIT:
			{
				stop();
				saveConfig();
				Logger.p("Bye");
				break;
			}
			case USER_RQUEST_TYPE_SET_HTTP_SERVER_PORT:
			{
				Logger.pcn("Please input new http port:");
				String port = Util.getConsoleInput();
				if (Util.isEmpty(port))
				{
					Logger.p("Invalid input");
				}
				else
				{
					int newPort = Integer.parseInt(port);
					if (newPort <= 0 || newPort > 65535)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setHttpServerPort(newPort);

					restartHttpService(httpServer);

					Logger.p("Http Server port has updated, the new port is " + newPort);
				}

				break;
			}
			case USER_RQUEST_TYPE_TEST_OCR:
			{
				Rectangle rect = Setting.getInstance().getOcrTestRect();
				if (rect == null || rect.isEmpty())
				{
					Logger.p("CaptureRect is invalid:" + rect);
				}

				Logger.pcn("Please input test times:");
				String times = Util.getConsoleInput();
				if (Util.isEmpty(times))
				{
					Logger.p("Invalid test times");
				}
				else
				{
					int count = Integer.parseInt(times);
					long startClock = System.currentTimeMillis();
					if (count > 0)
					{
						BufferedImage captureImg = null;
						int i = 0;
						while (i++ < count)
						{
							captureImg = Util.capture(Setting.getInstance().getOcrTestRect(), Constant.CAPTURE_FILE_NAME, false);
							if (captureImg != null)
							{
								String text = Util.doOCR(captureImg);
								Logger.p("Test[" + i + "] text="
										+ Util.exchangeChar(Setting.getInstance().isTrunkPrice() ? Util.trunkFloat(text) : text));
							}
							else
							{
								Logger.p("Test[" + i + "] capture sceen failed.");
							}
						}

						long total = System.currentTimeMillis() - startClock;
						Logger.p("Total time:" + total + ", Average time:" + total / count);
						if (captureImg != null)
						{
							try
							{
								ImageIO.write(captureImg, "jpg", new File(Constant.CAPTURE_FILE_NAME));
							}
							catch (IOException e)
							{
								Logger.p("Save image failed.");
								e.printStackTrace();
							}
						}
					}
					else
					{
						Logger.p("Invalid test times");
					}
				}

				break;
			}
			case USER_RQUEST_TYPE_SET_OCR_TEST_RECT:
			{
				Logger.pcn("Please input OCR test rectangle:");
				String param = Util.getConsoleInput();
				String[] arg = param.split(" ");
				final Rectangle rect = Util.parseInputParams(arg);
				if (rect != null)
				{
					Logger.p("rect=" + rect.toString());
					Setting.getInstance().setOcrTestRect(rect);
				}
				else
				{
					Logger.p("Invalid input.");
				}
				break;
			}
			case USER_RQUEST_TYPE_COPY_OCR_TEST_RECT_TO_CAPTURE:
			{
				Setting.getInstance().setCaptureRect(Setting.getInstance().getOcrTestRect());
				Logger.p("Copy successfully");
				break;
			}
			case USER_RQUEST_TYPE_SET_IMAGE_ZOOM_RATE:
			{
				Logger.pcn("Please input zoom rate:");
				String zoom = Util.getConsoleInput();
				if (Util.isEmpty(zoom))
				{
					Logger.p("Invalid input");
				}
				else
				{
					float rate = Float.parseFloat(zoom);
					if (rate <= 0.9 || rate > 100)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setImageZoomRate(rate);
					Logger.p("Current image zoom rate is " + rate);
				}
				break;
			}
			case USER_RQUEST_TYPE_TRUNK_PRICE:
			{
				Logger.pcn("Trunk price(Y/N?):");
				String trunk = Util.getConsoleInput();
				if (Util.isEmpty(trunk))
				{
					Logger.p("Invalid input");
				}
				else
				{

					if (trunk.toLowerCase().equalsIgnoreCase("y"))
					{
						Setting.getInstance().setTrunkPrice(true);
					}
					else if (trunk.toLowerCase().equalsIgnoreCase("n"))
					{
						Setting.getInstance().setTrunkPrice(false);
					}
					Logger.p("trunkPrice=" + Setting.getInstance().isTrunkPrice());
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_PRICE_GETTER_SLEEP_INTERVAL:
			{
				Logger.pcn("Please input price getter sleep time:");
				String sleepTime = Util.getConsoleInput();
				if (Util.isEmpty(sleepTime))
				{
					Logger.p("Invalid input");
				}
				else
				{
					int rate = Integer.parseInt(sleepTime);
					if (rate < 0)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setPriceGetterSleep(rate);
					Logger.p("Current sleep time is " + rate);
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_PRICE_GETTER_NUMBER:
			{
				Logger.pcn("Please input the numner of the price getter:");
				String num = Util.getConsoleInput();
				if (Util.isEmpty(num))
				{
					Logger.p("Invalid input");
				}
				else
				{
					int count = Integer.parseInt(num);
					if (count <= 0)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setPriceWorkerNumber(count);
					startPriceGetter();
					Logger.p("Current price getter count is " + count);
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_ORIGIN_TO_DOHIGH_BUTTON:
			{
				Logger.pcn("Please input the shift from origin to high:");
				String param = Util.getConsoleInput();
				String[] arg = param.split(" ");
				Setting.getInstance().setOrigin2Button(true, Util.parsePoint(arg));
				break;
			}
			case USER_RQUEST_TYPE_SET_ORIGIN_TO_DOLOW_BUTTON:
			{
				Logger.pcn("Please input the shift from origin to low:");
				String param = Util.getConsoleInput();
				String[] arg = param.split(" ");
				Setting.getInstance().setOrigin2Button(false, Util.parsePoint(arg));
				break;
			}
			case USER_RQUEST_TYPE_SET_PREPARE_TIME:
			{
				Logger.pcn("Please input the prepare time(such as \"13:45:3.5\"):");
				String param = Util.getConsoleInput();
				Calendar calendar = Util.stringToCalendar(param);
				if (calendar != null)
				{
					Setting.getInstance().setPrepareTime(calendar);
					PriceHolder.getInstance().clearPreparePrice();
					Logger.pcn("set prepare time successfully!");
				}
				else
				{
					Logger.pcn("set prepare time failed!");
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_MAX_CHASE_TIME_SPAN:
			{
				Logger.pcn("Please input the max chase time span (in secconds):");
				String num = Util.getConsoleInput();
				if (Util.isEmpty(num))
				{
					Logger.p("Invalid input");
				}
				else
				{
					int count = Integer.parseInt(num);
					if (count <= 0)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setMaxChaseTimeSpan(count);
					Logger.p("Current max chase time span is " + count);
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_MAX_CHASE_PRICE_DIFF:
			{
				Logger.pcn("Please input the max chase price diff:");
				String num = Util.getConsoleInput();
				if (Util.isEmpty(num))
				{
					Logger.p("Invalid input");
				}
				else
				{
					int diff = Integer.parseInt(num);
					if (diff <= 0)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setMaxChasePriceDiff(diff);
					Logger.p("The current max price diff is " + diff);
				}
				break;
			}
			case USER_RQUEST_TYPE_SET_CHASE_THRESHOLD:
			{
				Logger.pcn("Please input the price threshold:");
				String num = Util.getConsoleInput();
				if (Util.isEmpty(num))
				{
					Logger.p("Invalid input");
				}
				else
				{
					float threshold = Float.parseFloat(num);
					if (threshold <= 0)
					{
						Logger.p("Invalid input");
					}

					Setting.getInstance().setChasePriceThreshold(threshold);
					Logger.p("The current pirce threshold is " + threshold);
				}
				break;
			}
		}

		return userInput;
	}

	private void restartHttpService(IHttpServer httpServer)
	{
		// restart http server.
		if (httpServer.isHttpServerRunning())
		{
			httpServer.stopHttpServer();
			httpServer.startHttpServer();
		}
	}

	public boolean loadConfig()
	{
		InputStream is;
		try
		{
			is = new FileInputStream(new File(Constant.CONFIG_FILE_NAME));
			ObjectInputStream ois = new ObjectInputStream(is);
			Object readObject = ois.readObject();
			if (readObject instanceof Setting)
			{
				Setting.getInstance().copy((Setting) readObject);
			}
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public boolean saveConfig()
	{
		File f = new File(Constant.CONFIG_FILE_NAME);
		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		try
		{
			if (f.exists())
			{
				f.delete();
			}
			os = new FileOutputStream(f);
			oos = new ObjectOutputStream(os);
			oos.writeObject(Setting.getInstance());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (oos != null)
				{
					oos.close();
				}
				if (os != null)
				{
					os.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return true;
	}


	/**
	 * 
	 * @return 做单行为管理器
	 */
	public IActionManager getActionManager()
	{
		return mActionManager;
	}
}
