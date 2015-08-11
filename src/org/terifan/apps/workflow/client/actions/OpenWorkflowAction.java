package org.terifan.apps.workflow.client.actions;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.terifan.apps.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.apps.workflow.core.Workflow;
import org.terifan.bundle.BinaryDecoder;


public class OpenWorkflowAction extends AbstractAction
{
	private WorkflowPane mWorkflowPane;


	public OpenWorkflowAction(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;

		putValue(SMALL_ICON, aWorkflowPane.getStyleSheet().getStyleSheet("toolbar").getIcon("button_open"));
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

			if (chooser.showOpenDialog(mWorkflowPane) == JFileChooser.APPROVE_OPTION)
			{
				Workflow w = new Workflow();

				try (InputStream in = new FileInputStream(chooser.getSelectedFile()))
				{
					w.deserialize(new BinaryDecoder().unmarshal(in));
				}

				mWorkflowPane.setWorkflowFile(chooser.getSelectedFile());
				mWorkflowPane.setWorkflow(w);
				mWorkflowPane.rebuild();
			}
		}
		catch (Throwable e)
		{
			mWorkflowPane.getApplication().handleError(e);
		}
	}
}
