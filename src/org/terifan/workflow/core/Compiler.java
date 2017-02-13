package org.terifan.workflow.core;

import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.terifan.util.compiler.MemoryCompiler;
import org.terifan.util.log.Log;


public class Compiler
{
	public static boolean IS_RUNTIME;

	public static Executable compile(AbstractActivity aActivity, String aSourceCode)
	{
		try
		{
			MemoryCompiler compiler = new MemoryCompiler();
			compiler.add("Solution", aSourceCode);
			DiagnosticCollector<JavaFileObject> diagnostics = compiler.compile(System.out);

			String output = "";

			int errorCount = 0;
			int warningCount = 0;
			for (Diagnostic diagnostic : diagnostics.getDiagnostics())
			{
				if (output.length() > 0)
				{
					output += "\n";
				}

				output += diagnostic.getMessage(Locale.ENGLISH)+"\n";
				if (diagnostic.getLineNumber() > 1)
				{
					output += aSourceCode.split("\n")[(int)diagnostic.getLineNumber()-1]+"\n";
				}
				for (int i = 0; i < diagnostic.getColumnNumber(); i++)
				{
					output += " ";
				}
				output += "^\n";

				switch (diagnostic.getKind())
				{
					case ERROR:
						errorCount++;
						break;
					default:
						warningCount++;
						break;
				}
			}

			if (errorCount > 0)
			{
				output += errorCount+" error"+(errorCount>1?"s":"")+"\n";
			}
			if (warningCount > 0)
			{
				output += warningCount+" warning"+(warningCount>1?"s":"")+"\n";
			}

//			if (IS_RUNTIME)
//			{
				if (output.length() > 0)
				{
					Log.out.println(output);
				}
//			}
//			else
//			{
//				aActivity.setTextBubble(output);
//			}

			aActivity.setState(errorCount != 0 ? ActivityState.COMPILER_ERROR : ActivityState.NONE);

			Executable exe = null;

			if (errorCount == 0)
			{
				Class cs = compiler.loadClass("Solution");
				Object instance = cs.newInstance();
				if (!(instance instanceof Executable))
				{
					Log.out.println("Class not an instance of Executable.");
					return null;
				}
				exe = (Executable)instance;
				exe.setIdentity(aActivity.getLabel());
			}

			return exe;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace(Log.out);

			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace(Log.out);

			throw new IllegalStateException(e);
		}
	}
}
