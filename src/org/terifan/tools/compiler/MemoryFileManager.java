package org.terifan.tools.compiler;

import java.io.IOException;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;


public class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
{
	private MemoryClassLoader mClassLoader;


	public MemoryFileManager(StandardJavaFileManager sjfm, MemoryClassLoader aClassLoader)
	{
		super(sjfm);

		mClassLoader = aClassLoader;
	}


	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException
	{
		//System.out.println("MemoryFileManager::getFileForInput");
		return super.getFileForInput(location, packageName, relativeName);
	}


	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException
	{
		//System.out.println("MemoryFileManager::getFileForOutput");
		//return super.getFileForOutput(location, packageName, relativeName, sibling);

		throw new RuntimeException();
	}


	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException
	{
		//System.out.println("MemoryFileManager::getJavaFileForInput");
		return super.getJavaFileForInput(location, className, kind);
	}


	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException
	{
		//System.out.println("MemoryFileManager::getJavaFileForOutput location: "+location+", className: "+className+", kind: "+kind+", sibling: "+sibling);

		MemoryFileObject file = new MemoryFileObject(className, kind, "");

		if (Kind.CLASS.equals(kind))
		{
			mClassLoader.addFile(file);
		}
		else
		{
			throw new RuntimeException();
		}

		return file;
	}


	@Override
	public boolean isSameFile(FileObject a, FileObject b)
	{
		//System.out.println("MemoryFileManager::isSameFile");
		return super.isSameFile(a, b);
	}


	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException
	{
		//System.out.println("MemoryFileManager::list location: "+location+", packageName: "+packageName+", kinds: "+kinds+", recurse: "+recurse);
		return super.list(location, packageName, kinds, recurse);
	}
}
