package org.terifan.workflow.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.terifan.ui.Icon;
import org.terifan.ui.ImageIcon;
import org.terifan.ui.Utilities;
import org.terifan.xml.XmlReader;


/**
 * Sample usage:
 *
 * StyleSheet style = new StyleSheet("MyComponent", getClass(), "stylesheet.xml", "resources", 1000000);
 * JLabel label = new JLabel("hello world");
 * label.setFont(style.getFont("font"));
 * label.setBackground(style.getColor("background"));
 *
 * Sample StyleSheet file:
 *
 * <?xml version="1.0" encoding="iso-8859-1"?>
 * <stylesheet>
 *   <component id="MyComponent" relative-file-path="my_images">
 *     <int id="intValue" value="19"/>
 *     <double id="doubleValue" value="3.14"/>
 *     <boolean id="booleanValue" value="true"/>
 *     <string id="StringValue" value="hello world"/>
 *     <color id="background" value="255,0,255"/>
 *     <color id="background2" r="255" g="0" b="255" a="0"/>
 *     <font id="font" family="Segoe UI,plain,11"/>
 *     <font id="font2" family="Segoe UI" style="plain" size="11"/>
 *     <image id="header" value="header.png"/>
 *   </component>
 * </stylesheet>
 *
 * Notice: Using the samples above and loading an image using
 * style.getImage("header") will be the same as executing
 * getClass().getResourceAsStream("resources/my_images/header.png")
 */
public class StyleSheet
{
	private final static boolean DEBUG = false;
	private long mReloadTime;

	protected Class mRootClass;
	protected String mRelativeFilePath;
	protected String mComponentID;
	protected String mStyleSheetFileName;

	protected HashMap<String,Color> mColors = new HashMap<>();
	protected HashMap<String,Font> mFonts = new HashMap<>();
	protected HashMap<String,String> mImages = new HashMap<>();
	protected HashMap<String,Integer> mIntegers = new HashMap<>();
	protected HashMap<String,Double> mDoubles = new HashMap<>();
	protected HashMap<String,Boolean> mBooleans = new HashMap<>();
	protected HashMap<String,String> mStrings = new HashMap<>();

	protected HashMap<String,BufferedImage> mCachedImages;
	private HashMap<String,ComponentStyleSheet> mCachedStyleSheets;


	/**
	 * Constructs a StyleSheet class from an XML file.
	 *
	 * new StyleSheet("my.component.Name", "resources/blue.style", getClass(), "resources/blue_files", 1000000);
	 *
	 * @param aComponentID
	 *   the component StyleSheet inside the StyleSheet file to load.
	 * @param aRootClass
	 *   the resource root path.
	 * @param aStyleSheetFileName
	 *   the StyleSheet file relative to the root class.
	 * @param aRelativeFilePath
	 *   the root path for files. This path is relative to the root class.
	 * @param aImageCacheSizeBytes
	 *   maximum allowed size of the image caches.
	 */
	public StyleSheet(String aComponentID, Class aRootClass, String aStyleSheetFileName, String aRelativeFilePath, int aImageCacheSizeBytes)
	{
		mRootClass = aRootClass;
		mRelativeFilePath = aRelativeFilePath;
		mStyleSheetFileName = aStyleSheetFileName;
		mCachedImages = new HashMap<>(); //aImageCacheSizeBytes
		mComponentID = aComponentID;
		mCachedStyleSheets = new HashMap<>();

		if (mRelativeFilePath.length() > 0 && !mRelativeFilePath.endsWith("/") && !mRelativeFilePath.endsWith("\\"))
		{
			mRelativeFilePath += "/";
		}

		load();
	}


	public StyleSheet getStyleSheet(String aComponent)
	{
//		if (this instanceof ComponentStyleSheet)
//		{
//			throw new IllegalArgumentException("This is not a root stylesheet; a component stylesheet must be loaded from a root stylesheet.");
//		}

		ComponentStyleSheet style = mCachedStyleSheets.get(aComponent);
		if (style == null)
		{
			style = new ComponentStyleSheet(aComponent, mRootClass, mStyleSheetFileName, mRelativeFilePath, 0);
			style.mCachedImages = this.mCachedImages;
			style.mParent = this;

			style.load();

			mCachedStyleSheets.put(aComponent, style);
		}
		return style;
	}


	public StyleSheet getRoot()
	{
		StyleSheet style = this;
		while (style instanceof ComponentStyleSheet)
		{
			style = ((ComponentStyleSheet)style).mParent;
		}
		return style;
	}


	protected void load()
	{
		if (mReloadTime > 0 && mReloadTime > System.nanoTime())
		{
			return;
		}

		mReloadTime = System.nanoTime()+100000000000L;

		try
		{
			InputStream in = mRootClass.getResourceAsStream(mStyleSheetFileName);

			if (in == null)
			{
				throw new IllegalStateException("Failed to find stylesheet file: '" + mStyleSheetFileName + "' with root " + mRootClass+" at path '"+getStyleSheetFile()+"'");
			}

			try
			{
				XmlReader reader = new XmlReader(new InputStreamReader(in));

				boolean read = false;
				boolean wasRead = false;
				String relativeFilePath = "";

				while (reader.moveToNextElement())
				{
					if (reader.getName().equals("component"))
					{
						String id = "";
						relativeFilePath = "";

						while (reader.moveToNextAttribute())
						{
							if (reader.getName().equals("id")) id = reader.getValue();
							if (reader.getName().equals("relative-file-path")) relativeFilePath = reader.getValue();
						}

						if (DEBUG) System.out.println("Found component: " + id);

						read = id.equals(mComponentID);
						wasRead |= read;

						if (read)
						{
							if (DEBUG) System.out.println("Reading stylesheet: \"" + mComponentID + "\"");

							if (relativeFilePath != null && relativeFilePath.length() > 0)
							{
								relativeFilePath = relativeFilePath.replace('\\','/');
								if (relativeFilePath.startsWith("/"))
								{
									relativeFilePath = relativeFilePath.substring(1);
								}
								if (!relativeFilePath.endsWith("/"))
								{
									relativeFilePath += "/";
								}
							}
						}
					}
					else if (read)
					{
						String type = reader.getName();
						String id = reader.readAttributeValue("id");

						if (DEBUG) System.out.println(getStyleSheetFile()+"::"+type+"::"+id);

						if (type.equals("image"))
						{
							String path = relativeFilePath + reader.readAttributeValue("value");
							mImages.put(id, path);
						}
						else if (type.equals("font"))
						{
							String family = "arial";
							String style = "plain";
							int size = 12;
							while (reader.moveToNextAttribute())
							{
								if (reader.getName().equals("family")) family = reader.getValue();
								if (reader.getName().equals("style")) style = reader.getValue().toLowerCase();
								if (reader.getName().equals("size")) size = Integer.parseInt(reader.getValue());
								if (reader.getName().equals("value"))
								{
									String [] t = reader.getValue().split(",");
									if (t.length >= 1) family = t[0];
									if (t.length >= 2) style = t[1].toLowerCase();
									if (t.length >= 3) size = Integer.parseInt(t[2].trim());
								}
							}
							int s = 0;
							if (style.indexOf("plain")!=-1) s |= Font.PLAIN;
							if (style.indexOf("bold")!=-1) s |= Font.BOLD;
							if (style.indexOf("italic")!=-1) s |= Font.ITALIC;
							mFonts.put(id, new Font(family,s,size));
						}
						else if (type.equals("color"))
						{
							int r = 0, g = 0, b = 0, a = 255;
							while (reader.moveToNextAttribute())
							{
								if (reader.getName().equals("r")) r = Integer.parseInt(reader.getValue());
								if (reader.getName().equals("g")) g = Integer.parseInt(reader.getValue());
								if (reader.getName().equals("b")) b = Integer.parseInt(reader.getValue());
								if (reader.getName().equals("a")) a = Integer.parseInt(reader.getValue());
								if (reader.getName().equals("value"))
								{
									String [] t = reader.getValue().split(",");
									if (t.length >= 1) r = Integer.parseInt(t[0]);
									if (t.length >= 2) g = Integer.parseInt(t[1]);
									if (t.length >= 3) b = Integer.parseInt(t[2]);
									if (t.length >= 4) a = Integer.parseInt(t[3]);
								}
							}
							mColors.put(id, new Color(r,g,b,a));
						}
						else if (type.equals("boolean"))
						{
							mBooleans.put(id, Boolean.parseBoolean(reader.readAttributeValue("value")));
						}
						else if (type.equals("string"))
						{
							mStrings.put(id, reader.readAttributeValue("value"));
						}
						else if (type.equals("int"))
						{
							int value = Integer.parseInt(reader.readAttributeValue("value"));
							mIntegers.put(id, value);
						}
						else if (type.equals("double"))
						{
							mDoubles.put(id, Double.parseDouble(reader.readAttributeValue("value")));
						}
					}
				}

				if (!wasRead)
				{
					in = mRootClass.getResourceAsStream(mStyleSheetFileName);
					if (in.read() == -1)
					{
						throw new IllegalStateException("StyleSheet file not found.");
					}
					else
					{
						throw new IllegalStateException("When loading stylesheet: No matching component was found: \"" + mComponentID + "\", location: "+getStyleSheetFile());
					}
				}
			}
			finally
			{
				in.close();
			}
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}


	/**
	 * Loads an Icon.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @return
	 *   the image specified wrapped in an ImageIcon object.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public Icon getIcon(String aName)
	{
		return new ImageIcon(getImage(aName, 0));
	}


	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @param aWidth
	 *   scaled width
	 * @param aHeight
	 *   scaled height
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public Icon getScaledIcon(String aName, int aWidth, int aHeight)
	{
		return getScaledIcon(aName, aWidth, aHeight, false);
	}

	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @param aWidth
	 *   scaled width
	 * @param aHeight
	 *   scaled height
	 * @param aMaintainAspectRation
	 *   maintain aspect ration
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public Icon getScaledIcon(String aName, int aWidth, int aHeight, boolean aMaintainAspectRation)
	{
		return new ImageIcon(getScaledImage(aName, aWidth, aHeight, aMaintainAspectRation));
	}

	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public BufferedImage getImage(String aName)
	{
		return getImage(aName, 0);
	}


	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @param aWidth
	 *   scaled width
	 * @param aHeight
	 *   scaled height
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public BufferedImage getScaledImage(String aName, int aWidth, int aHeight)
	{
		return getScaledImage(aName, aWidth, aHeight, false);
	}

	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @param aWidth
	 *   scaled width
	 * @param aHeight
	 *   scaled height
	 * @param aMaintainAspectRation
	 *   maintain aspect ration
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public BufferedImage getScaledImage(String aName, int aWidth, int aHeight, boolean aMaintainAspectRation)
	{
		if (DEBUG) load();

		String key = aWidth+","+aHeight+"::"+aName;

		BufferedImage image = mCachedImages.get("scaled#"+mComponentID+"#"+key);

		if (image != null)
		{
			return image;
		}

		image = getImage(aName, 0);

		if (aMaintainAspectRation)
		{
			image = resizeAspect(image, aWidth, aHeight);
		}
		else
		{
			image = resize(image, aWidth, aHeight);
		}

		mCachedImages.put("scaled#"+mComponentID+"#"+key, image); //, 4L*aWidth*aHeight);

		return image;
	}


	/**
	 * Loads an Image.
	 *
	 * @param aName
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null.
	 * @param aWidth
	 *   scaled width
	 * @param aHeight
	 *   scaled height
	 * @param aFrameTop
	 *   number of pixels that make out the top frame height
	 * @param aFrameLeft
	 *   number of pixels that make out the left frame width
	 * @param aFrameBottom
	 *   number of pixels that make out the bottom frame height
	 * @param aFrameRight
	 *   number of pixels that make out the right frame width
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public BufferedImage getScaledImage(String aName, int aWidth, int aHeight, int aFrameTop, int aFrameLeft, int aFrameBottom, int aFrameRight)
	{
		if (DEBUG) load();

		String key = ((aFrameTop<<16)+aFrameLeft)+","+((aFrameBottom<<16)+aFrameRight)+","+((aWidth<<16)+aHeight)+"::"+aName;

		BufferedImage image = mCachedImages.get("scaled#"+mComponentID+"#"+key);

		if (image != null)
		{
			return image;
		}

		image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		Utilities.drawScaledImage(g, getImage(aName, 0), 0, 0, aWidth, aHeight, aFrameTop, aFrameLeft, aFrameBottom, aFrameRight);
		g.dispose();

		mCachedImages.put("scaled#"+mComponentID+"#"+key, image); //, 4L*aWidth*aHeight);

		return image;
	}


	/**
	 * Loads an Image with an optional alpha color.
	 *
	 * @param aKey
	 *   a relative path to the image or an absolute path if the RootClass
	 *   specified in the constructor is null or if the path starts with either
	 *   a slash or backslash.
	 * @param aAlphaColor
	 *   the alpha color or zero if no alpha color exists.
	 * @return
	 *   the image specified.
	 * @throws RuntimeException
	 *   this method wraps all Exceptions in a RuntimeException thus simplifying
	 *   exception handling.
	 */
	public BufferedImage getImage(String aKey, int aAlphaColor)
	{
		if (DEBUG) load();

		if (!mImages.containsKey(aKey))
		{
			throw new IllegalStateException("Image not found: key: \""+aKey+"\" available: " + mImages.keySet());
		}

		String path = mRelativeFilePath + mImages.get(aKey);

		BufferedImage image = mCachedImages.get(mComponentID+"#"+aKey);

		if (image == null)
		{
			try
			{
				if (DEBUG) System.out.println("Loading image: "+path+", root class: "+mRootClass);

				InputStream in = mRootClass.getResourceAsStream(path);
				try
				{
					image = ImageIO.read(in);
				}
				finally
				{
					in.close();
				}

				if (aAlphaColor != 0)
				{
					image = replaceColor(image, aAlphaColor, 0xFFFFFF & aAlphaColor);
				}
				else
				{
					image = convertNativeImage(image);
				}

				mCachedImages.put(mComponentID+"#"+aKey, image); //, 4*image.getWidth()*image.getHeight());
			}
			catch (Exception e)
			{
				throw new IllegalStateException("Failed to load: key: \""+aKey+"\", path: "+path, e);
			}
		}

		return image;
	}


	public Color getColor(String aKey)
	{
		if (DEBUG) load();

		Color value = mColors.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("Color not found: key: \""+aKey+"\"");
		}
		return value;
	}


	public Color getColor(String aKey, Color aDefaultValue)
	{
		if (DEBUG) load();

		Color value = mColors.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	public Font getFont(String aKey)
	{
		if (DEBUG) load();

		Font value = mFonts.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("Font not found: key: \""+aKey+"\"");
		}
		return value;
	}


	public Font getFont(String aKey, Font aDefaultValue)
	{
		if (DEBUG) load();

		Font value = mFonts.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	public boolean getBoolean(String aKey)
	{
		if (DEBUG) load();

		Boolean value = mBooleans.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("Boolean not found: key: \""+aKey+"\"");
		}
		return value;
	}


	public boolean getBoolean(String aKey, boolean aDefaultValue)
	{
		if (DEBUG) load();

		Boolean value = mBooleans.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	public int getInt(String aKey)
	{
		if (DEBUG) load();

		Integer value = mIntegers.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("Integer '" + aKey + "' not found in stylesheet " + getStyleSheetFile());
		}
		return value;
	}


	public int getInt(String aKey, int aDefaultValue)
	{
		if (DEBUG) load();

		Integer value = mIntegers.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	public double getDouble(String aKey)
	{
		if (DEBUG) load();

		Double value = mDoubles.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("Double not found: key: \""+aKey+"\"");
		}
		return value;
	}


	public double getDouble(String aKey, double aDefaultValue)
	{
		if (DEBUG) load();

		Double value = mDoubles.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	public String getString(String aKey)
	{
		if (DEBUG) load();

		String value = mStrings.get(aKey);
		if (value == null)
		{
			throw new IllegalStateException("String not found: key: \""+aKey+"\"");
		}
		return value;
	}


	public String getString(String aKey, String aDefaultValue)
	{
		if (DEBUG) load();

		String value = mStrings.get(aKey);
		if (value == null)
		{
			return aDefaultValue;
		}
		return value;
	}


	private static class ComponentStyleSheet extends StyleSheet
	{
		protected StyleSheet mParent;

		public ComponentStyleSheet(String aComponentID, Class aRootClass, String aStyleSheetFile, String aFileRootPath, int aImageCacheSizeBytes)
		{
			super(aComponentID, aRootClass, aStyleSheetFile, aFileRootPath, aImageCacheSizeBytes);
		}
	}


	private String getStyleSheetFile()
	{
		return mRootClass.getResource(".") + mStyleSheetFileName;
	}


	public static BufferedImage replaceColor(BufferedImage aImage, int aSourceColor, int aDestColor)
	{
		BufferedImage dst = convertNativeImage(aImage);

		int [] raster = ((DataBufferInt)dst.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < raster.length; i++)
		{
			if (raster[i] == aSourceColor)
			{
				raster[i] = aDestColor;
			}
		}

		return dst;
	}


	public static BufferedImage convertNativeImage(BufferedImage aImage)
	{
		BufferedImage dst = new BufferedImage(aImage.getWidth(), aImage.getHeight(), aImage.getTransparency() != Transparency.OPAQUE ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics g = dst.getGraphics();
		g.drawImage(aImage, 0, 0, null);
		g.dispose();

		return dst;
	}


	private static BufferedImage resizeAspect(BufferedImage aImage, int aWidth, int aHeight)
	{
		if (aImage == null)
		{
			throw new IllegalArgumentException("Image is null");
		}

		double f = Math.max(aImage.getWidth() / (double) aWidth, aImage.getHeight() / (double) aHeight);

		int dw = (int) (aImage.getWidth() / f);
		int dh = (int) (aImage.getHeight() / f);

		// make sure one direction has specified dimension
		if (dw != aWidth && dh != aHeight)
		{
			if (Math.abs(aWidth - dw) < Math.abs(aHeight - dh))
			{
				dw = aWidth;
			}
			else
			{
				dh = aHeight;
			}
		}

		return resize(aImage, Math.max(dw,1), Math.max(dh,1));
	}


	private static BufferedImage resize(BufferedImage aImage, int aWidth, int aHeight)
	{
		if (aWidth < aImage.getWidth() || aHeight < aImage.getHeight())
		{
			aImage = resizeDown(aImage, Math.min(aWidth, aImage.getWidth()), Math.min(aHeight, aImage.getHeight()));
		}

		if (aWidth > aImage.getWidth() || aHeight > aImage.getHeight())
		{
			BufferedImage temp = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g = temp.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(aImage, 0, 0, aWidth, aHeight, 0, 0, aImage.getWidth(), aImage.getHeight(), null);
			g.dispose();

			aImage = temp;
		}

		return aImage;
	}


	private static BufferedImage resizeDown(BufferedImage aImage, int aTargetWidth, int aTargetHeight)
	{
		if (aTargetWidth <= 0 || aTargetHeight <= 0)
		{
			throw new IllegalArgumentException("Width or height is zero or less: width: " + aTargetWidth + ", height: " + aTargetHeight);
		}

		int w = aImage.getWidth();
		int h = aImage.getHeight();
		BufferedImage ret = aImage;

		do
		{
			if (w > aTargetWidth)
			{
				w = Math.max(w / 2, aTargetWidth);
			}
			if (h > aTargetHeight)
			{
				h = Math.max(h / 2, aTargetHeight);
			}

			BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = tmp.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(ret, 0, 0, w, h, null);
			g.dispose();

			ret = tmp;
		}
		while (w != aTargetWidth || h != aTargetHeight);

		return ret;
	}
}