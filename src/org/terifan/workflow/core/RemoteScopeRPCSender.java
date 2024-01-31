package org.terifan.workflow.core;

import org.terifan.bundle.Bundle;
import org.terifan.net.rpc.client.RemoteServiceStub;
import org.terifan.net.rpc.shared.ServiceName;


@ServiceName("RPCRemoteEngine")
public class RemoteScopeRPCSender extends RemoteServiceStub
{
	public State call(Bundle aSubgraph, State aInState, State aOutState)
	{
		Object response = super.invoke("call", aSubgraph, aInState, aOutState);

		if (response instanceof RuntimeException)
		{
			throw (RuntimeException)response;
		}
		else if (response instanceof Exception)
		{
			throw new IllegalStateException((Exception)response);
		}

		System.out.println("rpc-response="+response);

		return (State)response;
	}
}