package org.terifan.apps.workflow.client.workflow_pane;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import org.terifan.ui.vectorgraphics.Region;


class WorkflowPaneDropTargetListener implements DropTargetListener
{
	private Region mHighlightedRegion;
	private WorkflowPane mWorkflowPane;


	public WorkflowPaneDropTargetListener(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		DropRegion.setDragActive(true);
		mWorkflowPane.repaint();
	}


	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		Region region = mWorkflowPane.getRegionAt(RegionFilter.DROP, dtde.getLocation());

		if (mHighlightedRegion != region && (mHighlightedRegion != null || region != null))
		{
			if (mHighlightedRegion != null)
			{
				((DropRegion)mHighlightedRegion).setDragHover(false);
			}
			if (region != null)
			{
				((DropRegion)region).setDragHover(true);
			}
			mWorkflowPane.repaint();
			mHighlightedRegion = region;
		}
	}


	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}


	@Override
	public void dragExit(DropTargetEvent dte)
	{
		DropRegion.setDragActive(false);
		mWorkflowPane.repaint();
	}


	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		DropRegion.setDragActive(false);
		mWorkflowPane.repaint();
	}
}
