package samples;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.terifan.apps.workflow.server.ServerApplication;


public class StartServer
{
	public static void main(String ... args)
	{
		try
		{
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

			ServerApplication server = new ServerApplication(printStream, new URL("http://127.0.0.1:8080"));

			JFrame frame = new JFrame("Workflow Runtime");
			frame.add(scrollPane);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent aE)
				{
					server.stop();
					System.exit(0);
				}
			});
			frame.setSize(1200, 692);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			server.start();
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
