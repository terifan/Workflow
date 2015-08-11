package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.Leaf;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.WorkflowEngine;
import org.terifan.bundle.Bundle;


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
			throw new RuntimeException(e);
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