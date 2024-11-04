package org.terifan.workflow.client.actions;

import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.terifan.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.bundle.Bundle;


public class SaveWorkflowAction extends AbstractAction
{
	private WorkflowPane mWorkflowPane;


	public SaveWorkflowAction(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;

		putValue(SMALL_ICON, aWorkflowPane.getStyleSheet().getStyleSheet("toolbar").getIcon("button_save"));
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		try
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Workflow file (*.wff)", "wff");
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(mWorkflowPane.getWorkflowFile());

			if (chooser.showSaveDialog(mWorkflowPane.getComponent()) == JFileChooser.APPROVE_OPTION)
			{
				mWorkflowPane.updateActivityState();

				Bundle bundle = new Bundle();
				mWorkflowPane.getWorkflow().serialize(bundle);
				byte [] buffer = bundle.marshal();

				try (FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile()))
				{
					fos.write(buffer);
				}

				mWorkflowPane.setWorkflowFile(chooser.getSelectedFile());
			}
		}
		catch (Throwable e)
		{
			mWorkflowPane.getApplication().handleError(e);
		}
	}
}
