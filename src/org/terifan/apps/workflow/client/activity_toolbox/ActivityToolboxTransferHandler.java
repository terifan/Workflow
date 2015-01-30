package org.terifan.apps.workflow.client.activity_toolbox;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import org.terifan.apps.workflow.core.AbstractActivity;


public class ActivityToolboxTransferHandler extends TransferHandler
{
	private ActivityToolbox mActivityPane;


	public ActivityToolboxTransferHandler(ActivityToolbox aActivityPane)
	{
		mActivityPane = aActivityPane;
	}
	
	
	@Override
	public int getSourceActions(JComponent c)
	{
		return COPY_OR_MOVE;
	}


	@Override
	protected Transferable createTransferable(JComponent c)
	{
		return new ActivityTypeTransferable();
	}


	private class ActivityTypeTransferable implements Transferable
	{
		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[] { AbstractActivity.mObjectDataFlavor };
		}


		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return AbstractActivity.mObjectDataFlavor.equals(flavor);
		}


		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if (AbstractActivity.mObjectDataFlavor.equals(flavor))
			{
				try
				{
					return (ActivityType)mActivityPane.getSelectionPath().getLastPathComponent();
				}
				catch (Exception e)
				{
					mActivityPane.getApplication().handleError(e);
				}
			}
			return null;
		}
	}
}
