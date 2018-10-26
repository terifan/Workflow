package org.terifan.workflow.activities;

import org.terifan.workflow.core.Leaf;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.WorkflowEngine;
import org.terifan.bundle.old.Bundle;


public class DelayActivity extends Leaf
{
	private int mDelay;


	public void setDelay(int aDelay)
	{
		mDelay = aDelay;
	}


	public int getDelay()
	{
		return mDelay;
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		try
		{
			Thread.sleep(mDelay);
		}
		catch (InterruptedException e)
		{
			throw new IllegalStateException(e);
		}
	}


	@Override
	public boolean compile()
	{
		return true;
	}


	@Override
	public void serialize(Bundle aBundle)
	{
		super.serialize(aBundle);

		aBundle.putInt("Delay", mDelay);
	}


	@Override
	public void deserialize(Bundle aBundle)
	{
		super.deserialize(aBundle);

		mDelay = aBundle.getInt("Delay");
	}
}