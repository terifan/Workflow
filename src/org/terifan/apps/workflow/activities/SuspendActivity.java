package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.Leaf;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.WorkflowEngine;


public class SuspendActivity extends Leaf
{
	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
	}


	@Override
	public boolean compile()
	{
		return true;
	}
}