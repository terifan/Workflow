package org.terifan.apps.workflow.client.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.terifan.apps.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.apps.workflow.core.AbstractActivity;


public class DeleteActivityAction extends AbstractAction
{
	private WorkflowPane mWorkflowPane;


	public DeleteActivityAction(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < mWorkflowPane.mSelections.size(); i++)
		{
			AbstractActivity activity = mWorkflowPane.mSelections.get(i);
			if (activity.getParent() != null)
			{
				activity.getParent().removeChild(activity);
			}

			mWorkflowPane.removeActivityEditor(activity);
		}
		mWorkflowPane.mSelections.clear();
		mWorkflowPane.repaint();
	}
}
