package org.terifan.apps.workflow.client.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.terifan.apps.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.WorkflowEngine;


public class RunWorkflowAction extends AbstractAction
{
	private WorkflowPane mWorkflowPane;


	public RunWorkflowAction(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;

		putValue(SMALL_ICON, aWorkflowPane.getStyleSheet().getStyleSheet("toolbar").getIcon("button_run"));
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		try
		{
			mWorkflowPane.getApplication().clearLogConsoles();
			
			mWorkflowPane.updateActivityState();
			
			WorkflowEngine engine = new WorkflowEngine(mWorkflowPane.getWorkflow(), mWorkflowPane.getApplication().getConnectionManager());

			boolean success = engine.compile();

			if (!success)
			{
				mWorkflowPane.rebuild();
				return;
			}

			new Thread(new Worker(engine)).start();
		}
		catch (Throwable e)
		{
			mWorkflowPane.getApplication().handleError(e);
		}
	}
	
	
	private class Worker implements Runnable
	{
		private WorkflowEngine mEngine;


		public Worker(WorkflowEngine aEngine)
		{
			mEngine = aEngine;
		}
		
		
		@Override
		public void run()
		{
			mEngine.start(new State());

			mWorkflowPane.rebuild();
		}
	}
}
