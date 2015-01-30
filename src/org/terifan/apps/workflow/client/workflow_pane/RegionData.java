package org.terifan.apps.workflow.client.workflow_pane;

import org.terifan.apps.workflow.core.AbstractActivity;


public class RegionData
{
	private AbstractActivity mActivity;
	private Object mUserObject;
	private RegionAction mRegionAction;


	public RegionData()
	{
	}


	public RegionData(RegionAction aRegionAction, AbstractActivity aActivity, Object aUserObject)
	{
		mRegionAction = aRegionAction;
		mActivity = aActivity;
		mUserObject = aUserObject;
	}


	public AbstractActivity getActivity()
	{
		return mActivity;
	}


	public RegionAction getAction()
	{
		return mRegionAction;
	}


	public Object getUserObject()
	{
		return mUserObject;
	}
}