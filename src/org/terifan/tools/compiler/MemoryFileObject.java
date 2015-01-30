package org.terifan.tools.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;


public class MemoryFileObject implements JavaFileObject
{
	private char [] mContent;
	private Kind mKind;
	private long mLastModified;
	private String mName;


	public MemoryFileObject(String aName, Kind aKind, String aSource)
	{
		mKind = aKind;
		mLastModified = System.currentTimeMillis();
		mName = aName;
		mContent = aSource.toCharArray();
	}


	public boolean delete()
	{
		//System.out.println("MemoryFileObject::delete");
		return true;
	}


	public Modifier getAccessLevel()
	{
		//System.out.println("MemoryFileObject::getAccessLevel");
		return Modifier.PUBLIC;
	}


	public Kind getKind()
	{
		//System.out.println("MemoryFileObject::getKind");
		return mKind;
	}


	public long getLastModified()
	{
		//System.out.println("MemoryFileObject::getLastModified");
		return mLastModified;
	}


	public String getName()
	{
		//System.out.println("MemoryFileObject::getName");
		return mName;
	}


	public NestingKind getNestingKind()
	{
		//System.out.println("MemoryFileObject::getNestingKind");
		return NestingKind.TOP_LEVEL;
	}


	public InputStream openInputStream() throws IOException
	{
		//System.out.println("MemoryFileObject::openInputStream");
		byte [] temp = new byte[mContent.length];
		for (int i = 0; i < temp.length; i++) temp[i] = (byte)mContent[i];
		return new ByteArrayInputStream(temp);
	}


	public OutputStream openOutputStream() throws IOException
	{
		//System.out.println("MemoryFileObject::openOutputStream");
		return new ByteArrayOutputStream()
		{
			@Override
			public void close() throws IOException
			{
				super.close();
				byte [] temp = toByteArray();
				mContent = new char[temp.length];
				for (int i = 0; i < mContent.length; i++) mContent[i] = (char)temp[i];
				//System.out.println("MemoryFileObject::~openOutputStream");
			}
		};
	}


	public Reader openReader(boolean ignoreEncodingErrors) throws IOException
	{
		//System.out.println("MemoryFileObject::openReader");
		return new CharArrayReader(mContent);
	}


	public Writer openWriter() throws IOException
	{
		//System.out.println("MemoryFileObject::openWriter");
		return new CharArrayWriter()
		{
			@Override
			public void close()
			{
				super.close();
				mContent = toCharArray();
				//System.out.println("MemoryFileObject::~openWriter");
			}
		};
	}


	public URI toUri()
	{
		//System.out.println("MemoryFileObject::toUri");
		try
		{
			return new URI(mName);
		}
		catch (URISyntaxException e)
		{
			throw new RuntimeException();
		}
	}


	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
	{
		//System.out.println("MemoryFileObject::getCharContent");
		return new String(mContent);
	}


	public boolean isNameCompatible(String simpleName, Kind kind)
	{
		//System.out.println("MemoryFileObject::isNameCompatible name:" + simpleName+", Kind:"+ kind);
		return simpleName.equals(mName);
	}
}
