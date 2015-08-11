package org.terifan.apps.workflow.activities;

import org.terifan.apps.workflow.core.ParallelGroup;
import org.terifan.bundle.Bundle;


public class ParallelActivity extends ParallelGroup
{
	private int mMaxThreadPercent;


	public ParallelActivity()
	{
	}


	@Override
	public void initialize()
	{
		addChild(new SequenceActivity());
		addChild(new SequenceActivity());
	}


	public void setMaxThreadCount(int aMaxThreadCount)
	{
		mMaxThreadPercent = aMaxThreadCount;
	}


	@Override
	public int getMaxThreadPercent()
	{
		return mMaxThreadPercent;
	}


	@Override
	public void serialize(Bundle aBundle)
	{
		super.serialize(aBundle);

		aBundle.putInt("MaxThreadPercent", mMaxThreadPercent);
	}


	@Override
	public void deserialize(Bundle aBundle)
	{
		super.deserialize(aBundle);

		mMaxThreadPercent = aBundle.getInt("MaxThreadPercent", 100);
	}
}
