package org.terifan.workflow.activities;

import org.terifan.workflow.core.Executable;


public abstract class WhileExecutable extends Executable
{
	public abstract boolean next();


	@Override
	public void run()
	{
	}
}
