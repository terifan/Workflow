package org.terifan.workflow.server;

import org.terifan.workflow.core.Compiler;
import org.terifan.workflow.core.State;
import org.terifan.workflow.core.Workflow;
import org.terifan.workflow.core.WorkflowEngine;
import org.terifan.bundle.Bundle;
import org.terifan.net.rpc.server.AbstractRemoteService;
import org.terifan.net.rpc.server.AbstractRemoteService.RemoteMethod;
import org.terifan.net.rpc.shared.ServiceName;


@ServiceName("RPCRemoteEngine")
public class RemoteScopeRPCReceiver extends AbstractRemoteService
{
	@RemoteMethod
	public State call(Bundle aSubgraph, State aInState, State aOutState)
	{
		Compiler.IS_RUNTIME = true;

		Workflow workflow = new Workflow();
		workflow.deserialize(aSubgraph);

		WorkflowEngine engine = new WorkflowEngine(workflow, null);
		engine.compile();
		engine.start(aInState);

		return aInState;
	}
}
