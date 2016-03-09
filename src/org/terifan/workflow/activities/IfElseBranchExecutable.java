package org.terifan.workflow.activities;

import org.terifan.workflow.core.Executable;


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
