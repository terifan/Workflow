package org.terifan.ui.deprecated_propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundle;


public class StyleSheet
{
	private Class mClass;
	private Bundle mBundle;


	public StyleSheet(Class aClass, URL aScript)
	{
		try
		{
			try (InputStream in = aScript.openStream())
			{
				if (in == null)
				{
					throw new IllegalArgumentException("Script not found: " + aScript);
				}
				load(aClass, new InputStreamReader(in));
			}
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException();
		}
	}


	public StyleSheet(Class aClass, InputStream aScript)
	{
		try
		{
			load(aClass, new InputStreamReader(aScript));
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException();
		}
	}


	private void load(Class aClass, Reader aScript) throws IOException
	{
		mClass = aClass;

		try (Reader in = aScript)
		{
			mBundle = new Bundle();
			mBundle.unmarshalJSON(in);
		}
	}


	public Color getColor(String aKey)
	{
		Object s = mBundle.get(aKey);
		if (s instanceof String)
		{
			return new Color(Integer.parseInt((String)s, 16));
		}

		Array values = mBundle.getArray(aKey);
		if (values == null)
		{
			throw new IllegalArgumentException("Key not found: " + aKey);
		}
		if (values.size() == 3)
		{
			return new Color(values.getInt(0), values.getInt(1), values.getInt(2));
		}
		return new Color(values.getInt(0), values.getInt(1), values.getInt(2), values.getInt(3));
	}


	public Font getFont(String aKey)
	{
		Array values = mBundle.getArray(aKey);
		return new Font(values.getString(0), "bold".equals(values.getString(1))?Font.BOLD:"bold-italic".equals(values.getString(1))?Font.BOLD|Font.ITALIC:"italic".equals(values.getString(1))?Font.ITALIC:Font.PLAIN, values.getInt(2));
	}


	public int getInt(String aKey)
	{
		return mBundle.getInt(aKey);
	}


	public boolean getBoolean(String aKey)
	{
		return mBundle.getBoolean(aKey);
	}


	public boolean getBoolean(String aKey, boolean aDefault)
	{
		return mBundle.getBoolean(aKey, aDefault);
	}


	public BufferedImage getImage(String aKey)
	{
		try
		{
			String path = mBundle.getString(aKey);

			if (path == null)
			{
				throw new IllegalArgumentException("Key not found: " + aKey);
			}

			return ImageIO.read(mClass.getResource(path));
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
