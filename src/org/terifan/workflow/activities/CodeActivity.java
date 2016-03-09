package org.terifan.workflow.activities;

import org.terifan.workflow.core.ActivityState;
import org.terifan.workflow.core.Compiler;
import org.terifan.workflow.core.Executable;
import org.terifan.workflow.core.Executable.Name;
import org.terifan.workflow.core.Leaf;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.WorkflowEngine;
import org.terifan.util.log.Log;


public class CodeActivity extends Leaf
{
	private transient Executable mExecutable;


	public CodeActivity()
	{
	}


	@Override
	public void initialize()
	{
		if (getSource() == null || getSource().isEmpty())
		{
			setSource(
			  "import org.terifan.apps.workflow.core.Executable;\n"
			+ "\n"
			+ "public class Solution extends Executable\n"
			+ "{\n"
			+ "	@Override\n"
			+ "	public void run()\n"
			+ "	{\n"
			+ "	}\n"
			+ "}");
		}
	}


	@Override
	public boolean compile()
	{
		Log.out.println("Compiling " + this.getClass().getSimpleName());

		setState(ActivityState.NONE);

		mExecutable = Compiler.compile(this, getSource());

		if (mExecutable == null)
		{
			return false;
		}

		Name name = mExecutable.getClass().getAnnotation(Name.class);
		if (name != null)
		{
			setLabel(name.value());
		}

		return true;
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		mExecutable.pushState(aWorkflowEngine, aInState);

		mExecutable.setActivity(this);
		mExecutable.run();

		mExecutable.popState(aWorkflowEngine, aOutState);
	}
}
