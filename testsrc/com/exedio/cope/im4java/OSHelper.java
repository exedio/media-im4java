package com.exedio.cope.im4java;

final class OSHelper
{
	private OSHelper()
	{
	}

	static boolean isWindows()
	{
		return System.getProperty("os.name").startsWith("Windows");
	}

	static String getProgramName()
	{
		if (isWindows())
		{
			return "convert.exe";
		}
		else
		{
			return "convert-im6.q16";
		}
	}
}
