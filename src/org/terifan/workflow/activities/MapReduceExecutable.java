package org.terifan.workflow.activities;

import org.terifan.workflow.core.Executable;


public abstract class MapReduceExecutable extends Executable
{
	public abstract boolean next();


	@Override
	public final void run()
	{
	}
}
