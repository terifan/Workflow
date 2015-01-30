package org.terifan.apps.workflow.client.workflow_pane;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Stroke;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.ValueList;
import org.terifan.apps.workflow.core.Workflow;
import org.terifan.apps.workflow.client.activities_layout.AbstractActivityLayout;
import org.terifan.ui.vectorgraphics.Layer;
import org.terifan.ui.Anchor;
import org.terifan.ui.StrokeStyle;
import org.terifan.ui.StyleSheet;


public class WorkflowPaneRenderer extends Thread
{
	private WorkflowPane mWorkflowPane;


	public WorkflowPaneRenderer(WorkflowPane aWorkflowPane)
	{
		mWorkflowPane = aWorkflowPane;
	}


	@Override
	public void run()
	{
		Workflow mWorkflow = mWorkflowPane.getWorkflow();
		StyleSheet mStyleSheet = mWorkflowPane.getStyleSheet();
		ValueList<String,AbstractActivity> mSelections = mWorkflowPane.mSelections;

		mWorkflowPane.lock();
		try
		{
			mWorkflowPane.clear();

			Layer graphLayer = mWorkflowPane.createLayer("graph");
			mWorkflowPane.createLayer("overlay");
			Layer focusLayer = mWorkflowPane.createLayer("focus");
			mWorkflowPane.createLayer("bubbles");

			AbstractActivityLayout workflowLayout = mWorkflowPane.getActivityLayout(mWorkflow);
			workflowLayout.layoutSize(mStyleSheet);
			workflowLayout.layoutPosition(mStyleSheet, 0, 0);
			workflowLayout.render(mStyleSheet, graphLayer);

			mWorkflowPane.setBackground(Color.WHITE);
			mWorkflowPane.setAnchor(Anchor.NORTH);
			mWorkflowPane.setOpaque(true);

			Stroke oldStroke = focusLayer.getStroke();
			Stroke dotStroke = StrokeStyle.DOT.createBasicStroke(1);
			for (AbstractActivity activity : mSelections)
			{
				AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
				Rectangle r = layout.getBounds();
				focusLayer.setStroke(dotStroke);
				focusLayer.setColor(new Color(96,96,96));
				focusLayer.drawRect(r.x-4, r.y-4, r.width+8, r.height+8);
				focusLayer.setStroke(oldStroke);
				focusLayer.setColor(Color.WHITE);
				focusLayer.fillRect(r.x-4-2, r.y-4-2, 4, 4);
				focusLayer.setColor(Color.BLUE);
				focusLayer.drawRect(r.x-4-2, r.y-4-2, 4, 4);
			}
		}
		catch (Throwable e)
		{
			mWorkflowPane.getApplication().handleError(e);
		}
		finally
		{
			mWorkflowPane.unlock();
		}

		mWorkflowPane.invalidate();
		mWorkflowPane.validate();
		mWorkflowPane.repaint();
	}
}
