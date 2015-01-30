package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.Executable;


public abstract class MapReduceExecutable extends Executable
{
	public abstract boolean next();


	@Override
	public final void run()
	{
	}
}
