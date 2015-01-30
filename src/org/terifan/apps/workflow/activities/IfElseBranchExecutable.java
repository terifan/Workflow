package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.Executable;


public abstract class IfElseBranchExecutable extends Executable
{
	public boolean checkCondition()
	{
		return true;
	}


	@Override
	public void run()
	{
	}
}
