package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.Executable;


public abstract class WhileExecutable extends Executable
{
	public abstract boolean next();


	@Override
	public void run()
	{
	}
}
