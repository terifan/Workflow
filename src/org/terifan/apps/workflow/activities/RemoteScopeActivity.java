package org.terifan.apps.workflow.activities;

import java.util.ArrayList;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.ActivityState;
import org.terifan.apps.workflow.core.Compiler;
import org.terifan.apps.workflow.core.Executable;
import org.terifan.apps.workflow.core.Executable.Name;
import org.terifan.apps.workflow.core.SequentialGroup;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.WorkflowEngine;
import org.terifan.apps.workflow.core.WorkflowVisitor;
import org.terifan.util.bundle.Bundle;


public class RemoteScopeActivity extends SequentialGroup
{
	private transient Executable mExecutable;
	private transient Bundle mSubgraph;


	public RemoteScopeActivity()
	{
	}


	@Override
	public void initialize()
	{
		setSource("import org.terifan.apps.workflow.activities.RemoteScopeExecutable;\n"
				+ "\n"
				+ "public class Solution extends RemoteScopeExecutable\n"
				+ "{\n"
				+ "}");
	}


	@Override
	public boolean compile()
	{
		setState(ActivityState.NONE);

		mExecutable = Compiler.compile(this, getSource().toString());

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

		super.visit(new WorkflowVisitor()
		{
			@Override
			public void visit(AbstractActivity aActivity)
			{
			}
		});

		ArrayList<Bundle> list = new ArrayList<>();

		for (AbstractActivity activity : mChildren)
		{
			Bundle bundle = new Bundle();
			activity.serialize(bundle);
			list.add(bundle);
		}

		Bundle bundle = new Bundle();
		bundle.putBundleArrayList("Children", list);
		mSubgraph = bundle;

		return true;
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		assert mSubgraph != null;

		long timer = System.nanoTime();

		aOutState.merge(aWorkflowEngine.invokeRemote(mSubgraph, aInState, aOutState));

		timer = (System.nanoTime()-timer)/1000000;

//		setTextBubble("Time: " + timer + "ms");
	}
}
