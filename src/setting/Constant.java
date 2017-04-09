package setting;

import network.HttpFactory;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName£ºconstant.java
 * Description£º
 * History£º
 * 1.0 Denverhan 2013-4-11 Create
 */

public interface Constant
{
	public static final String	CAPTURE_CORDINATE_START_X		= "x";
	public static final String	CAPTURE_CORDINATE_START_Y		= "y";
	public static final String	CAPTURE_CORDINATE_WIDTH			= "w";
	public static final String	CAPTURE_CORDINATE_HEIGHT		= "h";
	public static final String	CONFIG_FILE_NAME				= ".\\config";
	public static final String	CAPTURE_FILE_NAME				= ".\\price_cap.jpg";

	public static final int		PRICE_LENGTH					= 4;
	public static final String	KEY_VAULE_SPLITER				= "=";
	public static final int		INPUT_PARAM_NUMBER				= 4;

	public static final int		HTTP_SERVER_POOL_SIZE			= 50;
	public static final int		HTTP_SERVER_DEFAULT_PORT		= 80;

	public static final String	HTML_CURRENT_PRICE				= "CPRICE";
	public static final String	HTML_CURRENT_TIME				= "CTIME";
	public static final String	HTML_REQUEST_CURRENT_TIME		= "req_price";

	public static final float	PRICE_MAX_CHANGE				= 50.0F;
	public static final int		INITIALIZE_RETRY_TIMES			= 5;
	public static final int		QUERY_TIME_MAX_NUMBER			= 1000;
	public static final int		PRICE_GETTER_USE_TIME_COUNT_MAX	= 8000;
	public static final int		HTTP_SERVER_TYPE				= HttpFactory.SERVER_TYPE_NANOHTTPD;

	final String				NETWORK_KEY_PRICE				= "PRICE";
	final String				NETWORK_KEY_MILLION_SECOND		= "MILLION_SECOND";
	final String				NETWORK_KEY_TIME				= "TIME";
	final String				NETWORK_KEY_QUERY_TIME			= "QUERY_TIME";
	final String				NETWORK_KEY_CAPTURE_TIME		= "CAPTURE_TIME";
	
	final String				ENCODE_UTF_8					= "UTF-8";
}
