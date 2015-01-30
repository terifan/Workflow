package org.terifan.apps.workflow.server;

import org.terifan.apps.workflow.core.Compiler;
import org.terifan.apps.workflow.core.State;
import org.terifan.apps.workflow.core.Workflow;
import org.terifan.apps.workflow.core.WorkflowEngine;
import org.terifan.util.bundle.Bundle;
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
