package win32;


import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.		
 * 
 * FileName£ºMFCProxy.java
 * 
 * Description£º
 * 
 * History£º
 * 1.0 Administrator 2013-8-4 Create
 */

public interface MFCProxy extends StdCallLibrary, User32, WinUser {

	   static MFCProxy INSTANCE = (MFCProxy) Native.loadLibrary("user32",

			   MFCProxy.class, W32APIOptions.DEFAULT_OPTIONS);
}
