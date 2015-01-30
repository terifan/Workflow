package org.terifan.apps.workflow.util;

import java.lang.management.ManagementFactory;


public class Helper 
{
	public static int getAvailableCpuCount()
	{
		try
		{
			return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		}
		catch (Throwable e)
		{
			return 1;
		}
	}
}
