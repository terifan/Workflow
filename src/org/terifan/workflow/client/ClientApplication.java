package org.terifan.workflow.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import org.terifan.workflow.client.actions.Actions;
import org.terifan.workflow.client.activity_toolbox.ActivityToolbox;
import org.terifan.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.workflow.core.ConnectionPool;
import org.terifan.workflow.server.ServerApplication;
import org.terifan.ui.StyleSheet;
import org.terifan.ui.Utilities;
import org.terifan.ui.propertygrid.PropertyGrid;
import org.terifan.ui.propertygrid.PropertyGridModel;
import org.terifan.util.ErrorReportWindow;


public class ClientApplication extends JFrame
{
	private WorkflowPane mWorkflowPane;
	private JSplitPane mSplitPane0;
	private JSplitPane mSplitPane1;
	private JSplitPane mSplitPane2;
	private ActivityToolbox mActivityToolbox;
	private StyleSheet mStyleSheet;
	private PropertyGrid mPropertyGrid;
	private ConnectionPool mConnectionManager;
	private HashMap<ServerApplication,JTextArea> mLocalServers;


	public ClientApplication() throws IllegalAccessException, InstantiationException, MalformedURLException
	{
		Utilities.setSystemLookAndFeel();

		mConnectionManager = new ConnectionPool();
		mLocalServers = new HashMap<>();

		mStyleSheet = new StyleSheet("Workflow", getClass(), "stylesheet.xml", "resources", 1024*128);

		mWorkflowPane = new WorkflowPane(this);

		PropertyGridModel mPropertyGridModel = new PropertyGridModel();
		mPropertyGrid = new PropertyGrid(mPropertyGridModel);

		JToolBar toolbar = new JToolBar();
		toolbar.add(new JButton(mWorkflowPane.getAction(Actions.SaveWorkflow)));
		toolbar.add(new JButton(mWorkflowPane.getAction(Actions.OpenWorkflow)));
		toolbar.addSeparator();
		toolbar.add(new JButton(mWorkflowPane.getAction(Actions.RunWorkflow)));

		mActivityToolbox = new ActivityToolbox(this);

		mSplitPane2 = Utilities.stripBorder(new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(new JLabel("")), mPropertyGrid));
		mSplitPane2.setResizeWeight(0.75);

		mSplitPane1 = Utilities.stripBorder(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(mActivityToolbox), new JScrollPane(mWorkflowPane)));
		mSplitPane1.setResizeWeight(0.0);

		mSplitPane0 = Utilities.stripBorder(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mSplitPane1, mSplitPane2));
		mSplitPane0.setResizeWeight(0.75);

		JTabbedPane tabbedPane = new JTabbedPane();

		addLocalServer("http://127.0.0.1:47801", tabbedPane);
		addLocalServer("http://127.0.0.1:47802", tabbedPane);
		addLocalServer("http://127.0.0.1:47803", tabbedPane);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mSplitPane0, tabbedPane);
		splitPane.setResizeWeight(0.9);

		super.setLayout(new BorderLayout());
		super.add(toolbar, BorderLayout.NORTH);
		super.add(splitPane, BorderLayout.CENTER);
	}


	public void setSelectedEditor(JComponent aComponent)
	{
		int loc = mSplitPane2.getDividerLocation();
		mSplitPane2.setLeftComponent(aComponent);
		mSplitPane2.setDividerLocation(loc);
		mSplitPane2.repaint();
	}


	public WorkflowPane getWorkflowPane()
	{
		return mWorkflowPane;
	}


	public StyleSheet getStyleSheet()
	{
		return mStyleSheet;
	}


	public void handleError(Throwable e)
	{
		ErrorReportWindow.show(e);
	}


	public ConnectionPool getConnectionManager()
	{
		return mConnectionManager;
	}


	public void clearLogConsoles()
	{
		for (JTextArea textArea : mLocalServers.values())
		{
			textArea.setText("");
		}
	}


	public void close()
	{
		for (ServerApplication serverApplication : mLocalServers.keySet())
		{
			serverApplication.stop();
		}
	}


	private void addLocalServer(String aAddress, JTabbedPane aTabbedPane)
	{
		try
		{
			URL url = new URL(aAddress);

			JTextArea textArea = new JTextArea();
			JScrollPane scrollPane = new JScrollPane(textArea);

			OutputStream outputStream = new OutputStream()
			{
				@Override
				public void write(int c) throws IOException
				{
					textArea.append(Character.toString((char)c));
				}
			};

			PrintStream printStream = new PrintStream(outputStream);

			ServerApplication server = new ServerApplication(printStream, url);
			server.start();

			aTabbedPane.addTab("Local Server (" + url.getPort() + ")", scrollPane);

			mConnectionManager.addEndPoint(url, "patrik", "mypassword", printStream);

			mLocalServers.put(server, textArea);
		}
		catch (MalformedURLException | UnknownHostException e)
		{
			new ErrorReportWindow(e, false).show();
		}
	}
}
