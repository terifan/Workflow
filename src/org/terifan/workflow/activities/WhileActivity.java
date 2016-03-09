package org.terifan.workflow.activities;

import org.terifan.workflow.core.Compiler;
import org.terifan.workflow.core.SequentialGroup;
import org.terifan.workflow.core.WorkflowEngine;
import org.terifan.workflow.core.ActivityState;
import org.terifan.workflow.core.Executable.Name;
import org.terifan.workflow.core.State;


public class WhileActivity extends SequentialGroup
{
	private transient WhileExecutable mExecutable;


	public WhileActivity()
	{
	}


	@Override
	public void initialize()
	{
		setSource("import org.terifan.workflow.activities.WhileExecutable;\n"
				+ "\n"
				+ "public class Solution extends WhileExecutable\n"
				+ "{\n"
				+ "\tprivate int counter;\n"
				+ "\n"
				+ "\t@Override\n"
				+ "\tpublic boolean next()\n"
				+ "\t{\n"
				+ "\t\t//super.setTextBubble(counter<5?\"\"+(counter+1):\"\");\n"
				+ "\t\treturn counter++ < 5;\n"
				+ "\t}\n"
				+ "}");
	}


	@Override
	public boolean compile()
	{
		setState(ActivityState.NONE);

		mExecutable = (WhileExecutable)Compiler.compile(this, getSource());

		if (mExecutable == null)
		{
			return false;
		}

		Name name = mExecutable.getClass().getAnnotation(Name.class);
		if (name != null)
		{
			setLabel(name.value());
		}

		if (!super.compile())
		{
			return false;
		}

		return true;
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		try
		{
			mExecutable = (WhileExecutable)mExecutable.getClass().newInstance();
			mExecutable.setActivity(this);

			for (;;)
			{
				if (!mExecutable.next())
				{
					break;
				}

				for (int i = 0; i < getChildCount(); i++)
				{
					aWorkflowEngine.executeActivity(getChild(i), aInState, aOutState);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}