package org.terifan.apps.workflow.client.workflow_pane;

import org.terifan.ui.vectorgraphics.Region;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.terifan.apps.workflow.client.activity_toolbox.ActivityType;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.Group;


class WorkflowPaneTransferHandler extends TransferHandler
{
	private Point mDragPoint;
	private WorkflowPane mWorkflowPane;


	public WorkflowPaneTransferHandler(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	public void setDragPoint(Point aPoint)
	{
		mDragPoint = aPoint;
	}


	@Override
	public int getSourceActions(JComponent aComponent)
	{
		return COPY_OR_MOVE;
	}


	@Override
	public Transferable createTransferable(JComponent aComponent)
	{
		return mTransferable;
	}


	@Override
	public boolean canImport(TransferSupport support)
	{
		return true;
	}


	@Override
	public boolean importData(TransferSupport support)
	{
		if (!support.isDataFlavorSupported(AbstractActivity.mObjectDataFlavor))
		{
			return false;
		}

		try
		{
			Region region = mWorkflowPane.getRegionAt(RegionFilter.DROP, support.getDropLocation().getDropPoint());

			if (region != null)
			{
				Object value = support.getTransferable().getTransferData(AbstractActivity.mObjectDataFlavor);

				AbstractActivity activity;

				if (value instanceof ActivityType)
				{
					ActivityType activityType = (ActivityType)value;

					activity = activityType.newInstance();
					activity.initialize();

					mWorkflowPane.addActivity(activity);
				}
				else
				{
					activity = (AbstractActivity)value;
				}

				Group target = (Group)((RegionData)region.getUserObject()).getActivity();
				AbstractActivity after = (AbstractActivity)((RegionData)region.getUserObject()).getUserObject();

				if (after == null)
				{
					target.addChild(activity);
				}
				else
				{
					target.addChild(target.indexOfChild(after), activity);
				}

				mWorkflowPane.rebuild();
			}

			return true;
		}
		catch (Throwable e)
		{
			mWorkflowPane.getApplication().handleError(e);
			return false;
		}
	}


	private transient Transferable mTransferable = new Transferable()
	{
		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]
			{
				AbstractActivity.mObjectDataFlavor
			};
		}


		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return AbstractActivity.mObjectDataFlavor.equals(flavor);
		}


		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			final Region region = mWorkflowPane.getRegionAt(RegionFilter.SELECT, mDragPoint);
			if (region != null)
			{
				if (AbstractActivity.mObjectDataFlavor.equals(flavor))
				{
					return ((RegionData)region.getUserObject()).getActivity();
				}
			}
			return null;
		}
	};
}
