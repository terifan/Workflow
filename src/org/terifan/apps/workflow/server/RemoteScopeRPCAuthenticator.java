package org.terifan.apps.workflow.server;

import java.io.IOException;
import java.lang.reflect.Method;
import org.terifan.net.rpc.server.Authenticator;
import org.terifan.net.rpc.server.AbstractRemoteService;
import org.terifan.net.rpc.server.Session;
import org.terifan.net.rpc.shared.Password;
import org.terifan.util.Convert;


public class RemoteScopeRPCAuthenticator implements Authenticator
{
	@Override
	public byte[] getUserSalt(Session aSession) throws IOException
	{
		if (aSession.getUserName().equals("patrik"))
		{
			return Convert.hexToBytes("709A454156EC42BA62F2BBE4BC439FCD");
		}
		return null;
	}


	@Override
	public byte[] getUserPassword(Session aSession) throws IOException
	{
		if (aSession.getUserName().equals("patrik"))
		{
			return Password.expandPassword(getUserSalt(aSession), "mypassword");
		}
		return null;
	}


	@Override
	public boolean permitInvocation(Session aSession, AbstractRemoteService aService, Method aMethod) throws IOException
	{
		return true;
	}
}