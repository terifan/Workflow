package org.terifan.apps.workflow.client.workflow_pane;

import java.awt.Graphics2D;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.ValueList;
import org.terifan.apps.workflow.core.ValueListChangeListener;
import org.terifan.ui.vectorgraphics.RenderStateListener;


class WorkflowPaneRenderStateListener implements RenderStateListener, ValueListChangeListener<String, AbstractActivity>
{
	private boolean mRequestRebuild;
	private WorkflowPane mWorkflowPane;


	public WorkflowPaneRenderStateListener(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
		mRequestRebuild = true;
	}


	@Override
	public void initializeRendering(Graphics2D aGraphics)
	{
	}


	@Override
	public void beginRendering(Graphics2D aGraphics)
	{
		if (mRequestRebuild)
		{
			mRequestRebuild = false;
			mWorkflowPane.rebuild();
		}
	}


	@Override
	public void finishRendering(Graphics2D aGraphics)
	{
	}


	@Override
	public void valueAdded(ValueList aValueList, String aKey, AbstractActivity aValue)
	{
		mRequestRebuild = true;
	}


	@Override
	public void valueRemoved(ValueList aValueList, String aKey, AbstractActivity aValue)
	{
		mRequestRebuild = true;
	}


	@Override
	public void valueChanged(ValueList aValueList, String aKey, AbstractActivity aValue)
	{
		mRequestRebuild = true;
	}
}
