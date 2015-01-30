package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.ParallelGroup;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.WorkflowEngine;


public class IfElseActivity extends ParallelGroup
{
	public IfElseActivity()
	{
		addChild(new IfElseBranchActivity());
		addChild(new IfElseBranchActivity());
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		for (AbstractActivity child : mChildren)
		{
			IfElseBranchActivity branch = (IfElseBranchActivity)child;

			if (branch.checkCondition(aWorkflowEngine, aInState, aOutState))
			{
				branch.execute(aWorkflowEngine, aInState, aOutState);
				return;
			}
		}
	}
}