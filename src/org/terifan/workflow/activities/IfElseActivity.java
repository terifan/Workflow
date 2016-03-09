package org.terifan.workflow.activities;

import org.terifan.workflow.core.ParallelGroup;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.WorkflowEngine;


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