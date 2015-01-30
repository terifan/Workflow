package org.terifan.apps.workflow.client;

public class Launcher
{
//	private JFrame mFrame;
//
//
//	private void startInternalDevelopmentEnvironment()
//	{
//		ServerApplication sa1 = new ServerApplication();
//		ServerApplication sa2 = new ServerApplication();
//		ServerApplication sa3 = new ServerApplication();
//
//		sa1.start("127.0.0.1:47806");
//		sa2.start("127.0.0.1:47807");
//		sa3.start("127.0.0.1:47808");
//
//		ClientApplication ca = new ClientApplication();
//
//		ca.start("127.0.0.1:47806", "127.0.0.1:47807", "127.0.0.1:47808");
//
//		mFrame = new JFrame("MDS Workflow Developer");
//
//		JTabbedPane tabbedPane = new JTabbedPane();
//		tabbedPane.addTab("127.0.0.1:47806", sa1.getUI());
//		tabbedPane.addTab("127.0.0.1:47807", sa2.getUI());
//		tabbedPane.addTab("127.0.0.1:47808", sa3.getUI());
//
//		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ca.getUI(), tabbedPane);
//		splitPane.setResizeWeight(0.9);
//
//		mFrame.add(splitPane);
//		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mFrame.setSize(1200, 692);
//		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		mFrame.setLocationRelativeTo(null);
//		mFrame.setVisible(true);
//	}
//
//
//	private void startLocalDevelopmentEnvironment()
//	{
//		ClientApplication ca = new ClientApplication();
//
//		ca.start("127.0.0.1:47806", "127.0.0.1:47807", "127.0.0.1:47808");
//
//		mFrame = new JFrame("MDS Workflow Developer");
//
//		mFrame.add(ca.getUI());
//		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mFrame.setSize(1200, 692);
//		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		mFrame.setLocationRelativeTo(null);
//		mFrame.setVisible(true);
//	}
//
//
//	private void startRemoteDevelopmentEnvironment()
//	{
////		ServerApplication sa1 = new ServerApplication();
////		ServerApplication sa2 = new ServerApplication();
////		ServerApplication sa3 = new ServerApplication();
////
////		sa1.start("172.16.40.77:47806"); // selma
////		sa2.start("172.16.40.76:47806"); // patty
//
//		ClientApplication ca = new ClientApplication();
//
//		ca.start("172.16.40.76:47806","172.16.40.77:47806");
//
//		mFrame = new JFrame("MDS Workflow Developer");
//
////		JTabbedPane tabbedPane = new JTabbedPane();
////		tabbedPane.addTab("127.0.0.1:47806", sa1.getUI());
////		tabbedPane.addTab("127.0.0.1:47807", sa2.getUI());
////		tabbedPane.addTab("127.0.0.1:47808", sa3.getUI());
//
////		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ca.getUI(), tabbedPane);
////		splitPane.setResizeWeight(0.9);
//
////		mFrame.add(splitPane);
//		mFrame.add(ca.getUI());
//		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mFrame.setSize(1200, 692);
//		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		mFrame.setLocationRelativeTo(null);
//		mFrame.setVisible(true);
//	}
//
//
//	private void startRuntimeEnvironment()
//	{
//		ServerApplication sa1 = new ServerApplication();
//
//		String address = (String)JOptionPane.showInputDialog(null, "Listen to address:", "Start runtime", JOptionPane.QUESTION_MESSAGE, null, new String[]{"127.0.0.1:47806","172.16.40.77:47806 (selma)","172.16.40.76:47806 (patty)"}, "127.0.0.1:47806");
//		if (address == null || address.isEmpty())
//		{
//			System.exit(-1);
//		}
//
//		if (address.contains(" "))
//		{
//			address = address.substring(0, address.indexOf(" "));
//		}
//
//		sa1.start(address);
//
//		mFrame = new JFrame("MDS Workflow Runtime");
//		mFrame.add(sa1.getUI());
//		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mFrame.setSize(1200, 692);
//		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		mFrame.setLocationRelativeTo(null);
//		mFrame.setVisible(true);
//	}
//
//
//	public static void main(String... args)
//	{
//		try
//		{
//			if (!MemoryCompiler.isInstalled())
//			{
//				JOptionPane.showMessageDialog(null, "This Java Runtime Environment don't have a Java compiler available.", "Error", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//
//			String [] envs = new String[]{"Development - internal execution", "Development - local execution", "Development - remote execution", "Runtime environment"};
//
//			String env = (String)JOptionPane.showInputDialog(null, "What type of instance to start:", "Start", JOptionPane.QUESTION_MESSAGE, null, envs, envs[0]);
//			if (env == null || env.isEmpty())
//			{
//				System.exit(-1);
//			}
//
//			if (env.equals(envs[0]))
//			{
//				new Launcher().startInternalDevelopmentEnvironment();
//			}
//			else if (env.equals(envs[1]))
//			{
//				new Launcher().startLocalDevelopmentEnvironment();
//			}
//			else if (env.equals(envs[2]))
//			{
//				new Launcher().startRemoteDevelopmentEnvironment();
//			}
//			else
//			{
//				new Launcher().startRuntimeEnvironment();
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace(System.out);
//		}
//	}
}
