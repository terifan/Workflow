package org.terifan.apps.workflow.client.activity_toolbox;

import javax.swing.tree.DefaultMutableTreeNode;


class ActivityToolboxTreeNode extends DefaultMutableTreeNode
{
	private String mLabel;


	public ActivityToolboxTreeNode(String aLabel)
	{
		mLabel = aLabel;
	}


	@Override
	public String toString()
	{
		return mLabel;
	}


	public String getStyleId()
	{
		if (this instanceof ActivityType)
		{
			return ((ActivityType)this).getActivity().getSimpleName();
		}
		return mLabel;
	}
}
