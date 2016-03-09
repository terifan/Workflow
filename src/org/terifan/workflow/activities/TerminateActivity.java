package org.terifan.workflow.activities;

import org.terifan.workflow.core.Leaf;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.WorkflowEngine;


public class TerminateActivity extends Leaf
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
