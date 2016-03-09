package org.terifan.workflow.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import org.terifan.net.http.server.HttpServerHandler;
import org.terifan.net.http.server.HttpServerRequest;
import org.terifan.net.http.server.HttpServerResponse;
import org.terifan.net.http.server.HttpStatusCode;
import org.terifan.net.rpc.server.RPCServer;
import org.terifan.net.http.server.SimpleHttpServer;
import org.terifan.net.rpc.server.ServiceFactory;
import org.terifan.util.Calendar;


public class ServerApplication
{
	private PrintStream mLog;
	private URL mAddress;
	private RemoteScopeRPCAuthenticator mAuthenticator;
	private RPCServer mServer;
	private SimpleHttpServer mHttpServer;


	public ServerApplication(PrintStream aLog, URL aAddress)
	{
		mLog = aLog;
		mAddress = aAddress;

		mAuthenticator = new RemoteScopeRPCAuthenticator();
	}


    public void start() throws UnknownHostException
    {
		if (mServer != null)
		{
			throw new IllegalStateException("Server already started.");
		}

		mServer = new RPCServer().setAuthenticator(mAuthenticator);
		mServer.setServiceFactory(new ServiceFactory().register(RemoteScopeRPCReceiver.class));
		mServer.setLogOutput(System.out);

		mLog.println("Initializing runtime listening to " + mAddress);

		mHttpServer = new SimpleHttpServer(mAddress.getPort(), InetAddress.getByName(mAddress.getHost()), mRequestHandler);
		mHttpServer.start(false);

		mLog.println("Runtime started, ready to receive work.");
    }


	public void stop()
	{
		mHttpServer.close(true);
	}


	private transient HttpServerHandler mRequestHandler = new HttpServerHandler()
	{
		@Override
		public void service(HttpServerRequest aRequest, HttpServerResponse aResponse) throws IOException
		{
			byte [] request = aRequest.getContent();

			mLog.println(Calendar.now()+" Receive request ("+request.length+" bytes) from "+aRequest.getRemoteAddress().getHostAddress());

			long timer = System.nanoTime();

			byte [] response = mServer.processRequest(request);

			timer = (System.nanoTime()-timer)/1000000;

			mLog.println(Calendar.now()+" Sending response ("+response.length+" bytes) to "+aRequest.getRemoteAddress().getHostAddress()+" - processing time "+timer+"ms");

			aResponse.setStatusCode(HttpStatusCode.OK);
			aResponse.setContent(response);
		}
	};
}