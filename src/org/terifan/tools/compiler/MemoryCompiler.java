package org.terifan.tools.compiler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;


public class MemoryCompiler
{
	private MemoryClassLoader mClassLoader;
	private ArrayList<MemoryFileObject> mSourceFiles;


	public MemoryCompiler()
	{
		mSourceFiles = new ArrayList<MemoryFileObject>();
		mClassLoader = new MemoryClassLoader();
	}


	/**
	 * Checks if a Java compiler is available.
	 *
	 * @return
	 *   true if a Java compiler is available.
	 */
	public static boolean isInstalled()
	{
		return ToolProvider.getSystemJavaCompiler() != null;
	}


	/**
	 * Compile all source files provided through the add method.
	 *
	 * An exception is thrown if a compiler is not available.
	 *
	 * @param aErrorOutput
	 *   a Writer for additional output from the compiler; use System.err if null
	 * @return
	 *   a list of diagnostic information.
	 */
	public DiagnosticCollector<JavaFileObject> compile(Writer aCompilerOutput) throws IOException
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		if (compiler == null)
		{
			throw new RuntimeException("A Java compiler is not installed in this Java Runtime Environment.");
		}

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(diagnostics, null, null), mClassLoader);

		compiler.getTask(aCompilerOutput, fileManager, diagnostics, null, null, mSourceFiles).call();

		fileManager.close();

		return diagnostics;
	}


	/**
	 * Compile all source files provided through the add method.
	 *
	 * @param aErrorOutput
	 *   a Writer for additional output from the compiler; use System.err if null
	 * @return
	 *   a list of diagnostic information.
	 */
	public DiagnosticCollector<JavaFileObject> compile(OutputStream aCompilerOutput) throws IOException
	{
		Writer out = new OutputStreamWriter(aCompilerOutput);
		try
		{
			return compile(out);
		}
		finally
		{
			out.flush();
		}
	}


	/**
	 * Add a source file to the compiler.
	 *
	 * @param aName
	 *   the name of the public class contained in the file.
	 * @param aSource
	 *   the class source code.
	 */
	public void add(String aPublicClassName, String aSourceCode)
	{
		add(new MemoryFileObject(aPublicClassName, JavaFileObject.Kind.SOURCE, aSourceCode));
	}


	/**
	 * Add a source file to the compiler.
	 *
	 * @param aMemoryFileObject
	 *   the memory file
	 */
	public void add(MemoryFileObject aMemoryFileObject)
	{
		mSourceFiles.add(aMemoryFileObject);
	}


	/**
	 * Get a class instance.
	 *
	 * @param aName
	 *   the name of the class
	 * @return
	 *   a class instance
	 */
	public Class loadClass(String aPublicClassName) throws ClassNotFoundException
	{
		return mClassLoader.loadClass(aPublicClassName);
	}


	public ClassLoader getClassLoader()
	{
		return mClassLoader;
	}
}