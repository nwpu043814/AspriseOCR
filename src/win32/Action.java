package win32;

import java.awt.Point;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.		
 * 
 * FileName��Action.java
 * 
 * Description��
 * 
 * History��
 * 1.0 Administrator 2013-8-4 Create
 */

public interface Action
{
	/**
	 * �ּ�����
	 * @param isHigh -true���࣬�������ա�
	 * @return �Ƿ�ִ�гɹ�
	 */
	public boolean doImediateTrade(boolean isHigh, Point origin2Button);

}
