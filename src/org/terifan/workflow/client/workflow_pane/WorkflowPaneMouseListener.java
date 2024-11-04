package org.terifan.workflow.client.workflow_pane;

import org.terifan.vectorgraphics.Region;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.TransferHandler;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.client.activities_layout.AbstractActivityLayout;
import org.terifan.util.ErrorReportWindow;


class WorkflowPaneMouseListener extends MouseAdapter
{
	private WorkflowPane mWorkflowPane;
	private Point mPressPoint;


	public WorkflowPaneMouseListener(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		try
		{
			mPressPoint = aEvent.getPoint();
			mWorkflowPane.getComponent().requestFocus();

			Region region = mWorkflowPane.getRegionAt(RegionFilter.STATE, mPressPoint);

			if (region != null)
			{
				clickStateRegion(region);
			}
			else
			{
				region = mWorkflowPane.getRegionAt(RegionFilter.SELECT, mPressPoint);

				if (region != null)
				{
					clickSelectRegion(aEvent, region);
				}
				else
				{
					clickRegionBlank(aEvent);
				}
			}
		}
		catch (Exception e)
		{
			new ErrorReportWindow(e, true).show();
		}
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		try
		{
			if (mPressPoint != null)
			{
				int dist = Math.abs(aEvent.getX() - mPressPoint.x) + Math.abs(aEvent.getY() - mPressPoint.y);

				if (dist > 5)
				{
					WorkflowPaneTransferHandler handler = (WorkflowPaneTransferHandler)mWorkflowPane.getComponent().getTransferHandler();
					handler.setDragPoint(mPressPoint);
					handler.exportAsDrag(mWorkflowPane.getComponent(), aEvent, TransferHandler.MOVE);

					mPressPoint = null;
				}
			}
		}
		catch (Throwable e)
		{
			new ErrorReportWindow(e, true).show();
		}
	}


	private void clickStateRegion(Region region)
	{
		AbstractActivity activity = ((RegionData)region.getUserObject()).getActivity();
		AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);

		layout.setCollapsed(!layout.isCollapsed());
		mWorkflowPane.mSelections.clear();
		mWorkflowPane.mSelections.add(activity);
		mWorkflowPane.rebuild();

		mWorkflowPane.setSelectedActivityLayout(layout);
	}


	private void clickSelectRegion(MouseEvent aEvent, Region region)
	{
		if (!aEvent.isControlDown())
		{
			mWorkflowPane.mSelections.clear();
		}

		AbstractActivity activity = ((RegionData)region.getUserObject()).getActivity();

		if (activity.getParent() != null)
		{
			AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);

			if (mWorkflowPane.mSelections.containsValue(activity))
			{
				if (aEvent.isControlDown())
				{
					mWorkflowPane.mSelections.containsValue(activity);
					mWorkflowPane.setSelectedActivityLayout(layout);
				}
			}
			else
			{
				mWorkflowPane.mSelections.add(activity);
				mWorkflowPane.setSelectedActivityLayout(layout);
			}
		}

		mWorkflowPane.repaint();
	}


	private void clickRegionBlank(MouseEvent aEvent)
	{
		if (!aEvent.isControlDown())
		{
			mWorkflowPane.setSelectedActivityLayout(null);
			mWorkflowPane.mSelections.clear();
			mWorkflowPane.repaint();
		}
	}
}
