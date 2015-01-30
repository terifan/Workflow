package org.terifan.tools.compiler;

import java.util.HashMap;


public class MemoryClassLoader extends ClassLoader
{
	private HashMap<String,MemoryFileObject> mFiles;


	public MemoryClassLoader()
	{
		mFiles = new HashMap<>();
	}


	public void addFile(MemoryFileObject aFile)
	{
		//System.out.println("MemoryClassLoader::addFile "+aFile.getName());
		mFiles.put(aFile.getName(), aFile);
	}


	@Override
	protected Class<?> findClass(String aName) throws ClassNotFoundException
	{
		//System.out.println("MemoryClassLoader::findClass "+aName);

		try
		{
			if (!mFiles.containsKey(aName))
			{
				throw new IllegalArgumentException("No class with name "+aName+" has been generated.");
			}

			CharSequence temp = mFiles.get(aName).getCharContent(true);
			byte [] content = new byte[temp.length()];
			for (int i = 0; i < temp.length(); i++)
			{
				content[i] = (byte)temp.charAt(i);
			}
			return defineClass(aName, content, 0, content.length);
		}
		catch (Exception e)
		{
			throw new ClassNotFoundException("Failed to locate class "+aName, e);
		}
	}
}
