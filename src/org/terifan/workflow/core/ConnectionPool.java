package org.terifan.workflow.core;

import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import org.terifan.util.Pool;
import org.terifan.net.rpc.client.RPCConnection;
import org.terifan.util.log.Log;


public class ConnectionPool extends Pool<RPCConnection>
{
	private ArrayList<EndPoint> mEndPoints;
	private int mNextIndex;


	public ConnectionPool()
	{
		super(3, 60);

		mEndPoints = new ArrayList<>();
	}


	@Override
	protected RPCConnection create()
	{
		int i = mNextIndex++ % mEndPoints.size();
		
		EndPoint endPoint = mEndPoints.get(i);

		RPCConnection conn = new RPCConnection(endPoint.url, endPoint.userName, endPoint.password);
		conn.setConnectionPool(this);
		conn.setLogOutput(endPoint.log);
		conn.setAutoCommit(false);

		return conn;
	}


	@Override
	protected void destroy(RPCConnection aConnection)
	{
		aConnection.close();
	}


	@Override
	protected boolean prepare(RPCConnection aConnection)
	{
		return aConnection.isValid();
	}


	public void addEndPoint(URL aURL, String aUserName, String aPassword, PrintStream log)
	{
		mEndPoints.add(new EndPoint(aURL, aUserName, aPassword, log));
	}
	
	
	class EndPoint
	{
		URL url;
		String userName;
		String password;
		PrintStream log;


		public EndPoint(URL url, String userName, String password, PrintStream log)
		{
			this.url = url;
			this.userName = userName;
			this.password = password;
			this.log = log;
		}
	}
}
