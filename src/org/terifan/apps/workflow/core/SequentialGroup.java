package org.terifan.apps.workflow.core;


public abstract class SequentialGroup extends Group
{
	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		for (AbstractActivity child : this)
		{
			aWorkflowEngine.executeActivity(child, aInState, aOutState);
		}
	}
}