package org.terifan.apps.workflow.client.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.terifan.apps.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.apps.workflow.core.AbstractActivity;


public class RenameActivityAction extends AbstractAction
{
	private WorkflowPane mWorkflowPane;


	public RenameActivityAction(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (mWorkflowPane.mSelections.size() > 0)
		{
			AbstractActivity activity = mWorkflowPane.mSelections.get(0);
			
			activity.setLabel(NAME);

			mWorkflowPane.repaint();
		}
	}
}
