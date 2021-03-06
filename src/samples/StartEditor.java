package samples;

import javax.swing.JFrame;
import org.terifan.workflow.client.ClientApplication;
import org.terifan.util.ErrorReportWindow;


public class StartEditor
{
	public static void main(String... args)
	{
		try
		{
			ClientApplication app = new ClientApplication();
			app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			app.setSize(1200, 692);
			app.setExtendedState(JFrame.MAXIMIZED_BOTH);
			app.setLocationRelativeTo(null);
			app.setVisible(true);
		}
		catch (Exception e)
		{
			new ErrorReportWindow(e, false).show();
		}
	}
}
