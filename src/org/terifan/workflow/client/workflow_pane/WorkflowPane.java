package org.terifan.workflow.client.workflow_pane;

import java.awt.event.KeyEvent;
import java.io.File;
import org.terifan.workflow.client.activities_layout.AbstractActivityLayout;
import java.util.HashMap;
import java.util.TooManyListenersException;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.terifan.workflow.client.ClientApplication;
import org.terifan.workflow.client.actions.Actions;
import org.terifan.workflow.client.actions.DeleteActivityAction;
import org.terifan.workflow.client.actions.OpenWorkflowAction;
import org.terifan.workflow.client.actions.RunWorkflowAction;
import org.terifan.workflow.client.actions.SaveWorkflowAction;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.core.ValueList;
import org.terifan.workflow.core.Workflow;
import org.terifan.workflow.client.activities_layout.WorkflowLayout;
import org.terifan.workflow.client.activity_toolbox.ActivityToolbox;
import org.terifan.workflow.core.ActivityState;
import org.terifan.workflow.core.Group;
import org.terifan.ui.StyleSheet;
import org.terifan.vectorgraphics.Canvas;


public class WorkflowPane extends Canvas
{
	private ClientApplication mApplication;
	private Workflow mWorkflow;
	private WorkflowPaneRenderStateListener mRenderStateListener;
	private File mWorkflowFile;
	private HashMap<AbstractActivity,AbstractActivityLayout> mLayouts;

	public final ValueList<String,AbstractActivity> mSelections = new ValueList<>();


	public WorkflowPane(ClientApplication aApplication) throws IllegalAccessException, InstantiationException
	{
		mApplication = aApplication;
		
		mWorkflow = new Workflow();
		mLayouts = new HashMap<>();

		super.getActionMap().put(Actions.SaveWorkflow, new SaveWorkflowAction(this));
		super.getActionMap().put(Actions.OpenWorkflow, new OpenWorkflowAction(this));
		super.getActionMap().put(Actions.RunWorkflow, new RunWorkflowAction(this));
		super.getActionMap().put(Actions.DeleteActivity, new DeleteActivityAction(this));

		super.getInputMap().put(KeyStroke.getKeyStroke("ctrl S"), Actions.SaveWorkflow);
		super.getInputMap().put(KeyStroke.getKeyStroke("ctrl O"), Actions.OpenWorkflow);
		super.getInputMap().put(KeyStroke.getKeyStroke("ctrl R"), Actions.RunWorkflow);
		super.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), Actions.DeleteActivity);

		addActivity(mWorkflow);

		WorkflowPaneMouseListener weml = new WorkflowPaneMouseListener(this);

		mRenderStateListener = new WorkflowPaneRenderStateListener(this);

		super.setTransferHandler(new WorkflowPaneTransferHandler(this));
		super.addKeyListener(new WorkflowPaneKeyListener(this));
		super.addMouseListener(weml);
		super.addMouseMotionListener(weml);
		super.setFocusable(true);
		super.addRenderStateListener(mRenderStateListener);

		try
		{
			super.getDropTarget().addDropTargetListener(new WorkflowPaneDropTargetListener(this));
		}
		catch (TooManyListenersException e)
		{
			e.printStackTrace();
		}
		
		mSelections.addChangeListener(mRenderStateListener);
	}


	public Workflow getWorkflow()
	{
		return mWorkflow;
	}


	public void rebuild()
	{
		WorkflowPaneRenderer renderer = new WorkflowPaneRenderer(this);
		renderer.start();
	}


	public StyleSheet getStyleSheet()
	{
		return mApplication.getStyleSheet();
	}


	public void setWorkflow(Workflow aWorkflow) throws IllegalAccessException, InstantiationException
	{
		mWorkflow = aWorkflow;

		mLayouts.clear();

		addActivity(aWorkflow);
	}

	
	public File getWorkflowFile()
	{
		return mWorkflowFile;
	}


	public void setWorkflowFile(File aFile)
	{
		mWorkflowFile = aFile;
	}
	

	public AbstractActivityLayout getActivityLayout(AbstractActivity aActivity)
	{
		AbstractActivityLayout layout = mLayouts.get(aActivity);
		
		if (layout == null)
		{
			throw new IllegalStateException("No layout of activity: " + aActivity);
		}
		
		return layout;
	}


	public ClientApplication getApplication()
	{
		return mApplication;
	}


	public void addActivity(AbstractActivity aActivity) throws IllegalAccessException, InstantiationException
	{
		addActivityLayout(aActivity);
	}


	private void addActivityLayout(AbstractActivity aActivity) throws InstantiationException, IllegalAccessException
	{
		AbstractActivityLayout layout;
		
		if (aActivity instanceof Workflow)
		{
			layout = new WorkflowLayout();
		}
		else
		{
			layout = ActivityToolbox.findLayoutClass(aActivity.getClass()).newInstance();
		}

		mLayouts.put(aActivity, layout);

		layout.bind(this, aActivity);

		if (aActivity instanceof Group)
		{
			for (AbstractActivity child : (Group)aActivity)
			{
				addActivityLayout(child);
			}
		}
	}


	public void setSelectedActivityLayout(AbstractActivityLayout aLayout)
	{
		JComponent comp = null;

		if (aLayout != null)
		{
			comp = aLayout.getSwingComponent();
		}

		if (comp == null)
		{
			comp = new JScrollPane(new JLabel("", JLabel.CENTER));
		}

		mApplication.setSelectedEditor(comp);
	}


	public void removeActivityEditor(AbstractActivity aActivity)
	{
		mApplication.setSelectedEditor(new JScrollPane(new JLabel("", JLabel.CENTER)));
	}


	public void updateActivityState()
	{
		updateActivityState(mWorkflow);
	}


	private void updateActivityState(AbstractActivity aActivity)
	{
		AbstractActivityLayout layout = getActivityLayout(aActivity);

		aActivity.setState(ActivityState.NONE);
		aActivity.setSource(layout.getSource().toString());

		layout.updateActivityState();

		if (aActivity instanceof Group)
		{
			for (AbstractActivity child : (Group)aActivity)
			{
				updateActivityState(child);
			}
		}
	}
	
	
	public Action getAction(Actions aAction)
	{
		return super.getActionMap().get(aAction);
	}
}
