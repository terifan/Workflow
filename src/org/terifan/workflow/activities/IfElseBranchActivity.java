package org.terifan.workflow.activities;

import org.terifan.workflow.core.ActivityState;
import org.terifan.workflow.core.Compiler;
import org.terifan.workflow.core.Executable.Name;
import org.terifan.workflow.core.SequentialGroup;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.WorkflowEngine;


public class IfElseBranchActivity extends SequentialGroup
{
	private transient IfElseBranchExecutable mExecutable;


	public IfElseBranchActivity()
	{
	}


	@Override
	public void initialize()
	{
		setSource("import org.terifan.workflow.activities.IfElseBranchExecutable;\n"
				+ "\n"
				+ "public class Solution extends IfElseBranchExecutable\n"
				+ "{\n"
				+ "	@Override\n"
				+ "	public boolean checkCondition()\n"
				+ "	{\n"
				+ "		return false;\n"
				+ "	}\n"
				+ "}");
	}


	@Override
	public boolean compile()
	{
		setState(ActivityState.NONE);

		mExecutable = (IfElseBranchExecutable)Compiler.compile(this, getSource());

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


	public boolean checkCondition(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		if (mExecutable == null)
		{
			throw new RuntimeException("Not compiled");
		}

//		FieldMap map = new FieldMap(mExecutable).setIncludeAnnotations(Executable.In.class, Executable.Out.class, Executable.InOut.class);
//
//		for (String name : map.getFieldNames())
//		{
//			Field field = map.getField(name);
//			Executable.In in = field.getAnnotation(Executable.In.class);
//			Executable.InOut inOut = field.getAnnotation(Executable.InOut.class);
//			Executable.Out out = field.getAnnotation(Executable.Out.class);
//
//			if (inOut != null || out != null)
//			{
//				throw new RuntimeException("IfElseBranchActivity is not allowed to change the state.");
//			}
//
//			if (in != null)
//			{
//				String alias = name;
//				if (in.value() != null && !in.value().isEmpty())
//				{
//					alias = in.value();
//				}
//
//				System.out.println("Setting field \"" + name + "\" to property \"" + alias + "\" = \"" + aState.get(alias) + "\"");
//
//				map.putObject(name, aState.get(alias));
//			}
//		}

		mExecutable.pushState(aWorkflowEngine, aInState);

		mExecutable.setActivity(this);
		return mExecutable.checkCondition();
	}
}