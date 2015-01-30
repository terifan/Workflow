package org.terifan.apps.workflow.client.activity_toolbox;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.terifan.apps.workflow.activities.IfElseActivity;
import org.terifan.apps.workflow.activities.ParallelActivity;
import org.terifan.apps.workflow.activities.SequenceActivity;
import org.terifan.apps.workflow.activities.WhileActivity;
import org.terifan.apps.workflow.activities.CodeActivity;
import org.terifan.apps.workflow.activities.DelayActivity;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import org.terifan.apps.workflow.activities.MapReduceActivity;
import org.terifan.apps.workflow.activities.IfElseBranchActivity;
import org.terifan.apps.workflow.activities.RemoteScopeActivity;
import org.terifan.apps.workflow.activities.SuspendActivity;
import org.terifan.apps.workflow.activities.SynchronizationScopeActivity;
import org.terifan.apps.workflow.activities.TerminateActivity;
import org.terifan.apps.workflow.client.ClientApplication;
import org.terifan.apps.workflow.client.activities_layout.AbstractActivityLayout;
import org.terifan.apps.workflow.client.activities_layout.DelayLayout;
import org.terifan.apps.workflow.client.activities_layout.IfElseLayout;
import org.terifan.apps.workflow.client.activities_layout.DefaultLayout;
import org.terifan.apps.workflow.client.activities_layout.MapReduceLayout;
import org.terifan.apps.workflow.client.activities_layout.ParallelLayout;
import org.terifan.apps.workflow.client.activities_layout.SequentialGroupLayout;
import org.terifan.apps.workflow.client.activities_layout.WhileLayout;
import org.terifan.ui.StyleSheet;


public class ActivityToolbox extends JTree
{
	private ClientApplication mApplication;

	private final static TreeMap<Class,Class> mBasicActivites;

	static
	{
		mBasicActivites = new TreeMap<>(new Comparator() {
			@Override
			public int compare(Object o1, Object o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});

		mBasicActivites.put(CodeActivity.class, DefaultLayout.class);
		mBasicActivites.put(DelayActivity.class, DelayLayout.class);
		mBasicActivites.put(IfElseActivity.class, IfElseLayout.class);
		mBasicActivites.put(IfElseBranchActivity.class, SequentialGroupLayout.class);
		mBasicActivites.put(ParallelActivity.class, ParallelLayout.class);
		mBasicActivites.put(RemoteScopeActivity.class, SequentialGroupLayout.class);
		mBasicActivites.put(SequenceActivity.class, SequentialGroupLayout.class);
		mBasicActivites.put(SuspendActivity.class, DefaultLayout.class);
		mBasicActivites.put(SynchronizationScopeActivity.class, SequentialGroupLayout.class);
		mBasicActivites.put(TerminateActivity.class, DefaultLayout.class);
		mBasicActivites.put(MapReduceActivity.class, MapReduceLayout.class);
		mBasicActivites.put(WhileActivity.class, WhileLayout.class);

//		root.add(new ActivityType(CallExternalMethodActivity.class));
//		root.add(new ActivityType(CompensateActivity.class));
//		root.add(new ActivityType(CompensatableSequenceActivity.class));
//		root.add(new ActivityType(ConditionedActivityGroupActivity.class));
//		basicActivites.add(new ActivityType(EventDrivenActivity.class));
//		root.add(new ActivityType(EventHandlingScopeActivity.class));
//		root.add(new ActivityType(FaultHandlerActivity.class));
//		root.add(new ActivityType(HandleExternalEventActivity.class));
//		root.add(new ActivityType(InvokeWebSerivceActivity.class));
//		root.add(new ActivityType(InvokeWorkflowActivity.class));
//		basicActivites.add(new ActivityType(ListenActivity.class)); // kan innehålla HandleExternalEventActivity, DelayActivity
//		root.add(new ActivityType(PolicyActivity.class));
//		root.add(new ActivityType(ReceiveActivityActivity.class));
//		root.add(new ActivityType(ReplicatorActivity.class));
//		root.add(new ActivityType(SendActivityActivity.class));
//		root.add(new ActivityType(ThrowActivity.class));
//		basicActivites.add(new ActivityType(TransactionScopeActivity.class));
//		root.add(new ActivityType(CompensatableTransactionScopeActivity.class));
//		root.add(new ActivityType(WebServiceInputActivity.class));
//		root.add(new ActivityType(WebServiceOutputActivity.class));
//		root.add(new ActivityType(WebServiceFaultActivity.class));
	}


	public ActivityToolbox(ClientApplication aApplication)
	{
		mApplication = aApplication;

		StyleSheet styleSheet = mApplication.getStyleSheet().getStyleSheet("ActivityToolbox");

		ActivityToolboxTreeNode root = new ActivityToolboxTreeNode("ActivityToolbox");
		ActivityToolboxTreeNode basicActivites = new ActivityToolboxTreeNode("Activities");
		ActivityToolboxTreeNode prefabActivites = new ActivityToolboxTreeNode("PrefabActivities");

		for (Entry<Class,Class> entry : mBasicActivites.entrySet())
		{
			basicActivites.add(new ActivityType(entry.getKey(), entry.getValue()));
		}

		String s1 =
"import org.terifan.apps.workflow.core.Executable;\n" +
"\n" +
"public class Solution extends Executable\n" +
"{\n" +
"	@In(\"wordCounts\") Object message;\n" +
"\n" +
"	@Override\n" +
"	public void run()\n" +
"	{\n" +
"		javax.swing.JOptionPane.showMessageDialog(null, \"\" + message);\n" +
"	}\n" +
"}";

//		prefabActivites.add(new ActivityType(CodeActivity.class, DefaultLayout.class, "LoadFileList", "xxx"));
//		prefabActivites.add(new ActivityType(CodeActivity.class, DefaultLayout.class, "SendMail", "xxx"));
//		prefabActivites.add(new ActivityType(CodeActivity.class, DefaultLayout.class, "ReadMail", "xxx"));
//		prefabActivites.add(new ActivityType(CodeActivity.class, DefaultLayout.class, "XsltTransformation", "xxx"));
		prefabActivites.add(new ActivityType(CodeActivity.class, DefaultLayout.class, "MessageBox", s1));

		root.add(basicActivites);
		root.add(prefabActivites);

		DefaultTreeModel model = new DefaultTreeModel(root);

		DefaultTreeSelectionModel trm = new DefaultTreeSelectionModel();
		trm.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		super.setSelectionModel(trm);

		super.setBackground(new Color(240,240,240));
		super.setCellRenderer(new ActivityToolboxTreeCellRenderer(styleSheet));
		super.setModel(model);
		super.setDragEnabled(true);
		super.setTransferHandler(new ActivityToolboxTransferHandler(this));
		super.setRowHeight(20);
		super.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		super.setRootVisible(false);

		super.expandPath(new TreePath(basicActivites.getPath()));
		super.expandPath(new TreePath(prefabActivites.getPath()));
	}


	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}


	public static Class<? extends AbstractActivityLayout> findLayoutClass(Class aActivityClass)
	{
		return mBasicActivites.get(aActivityClass);
	}


	public ClientApplication getApplication()
	{
		return mApplication;
	}
}