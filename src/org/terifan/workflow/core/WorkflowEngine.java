package org.terifan.workflow.core;

import org.terifan.bundle.old.Bundle;
import org.terifan.net.rpc.client.RPCConnection;
import org.terifan.util.log.Log;


public class WorkflowEngine
{
	private Workflow mWorkflow;
	private ConnectionPool mConnectionManager;


	public WorkflowEngine(Workflow aWorkflow, ConnectionPool aConnectionManager)
	{
		mWorkflow = aWorkflow;
		mConnectionManager = aConnectionManager;
	}


	public boolean compile()
	{
		Log.out.println("Start compiling workflow");

		boolean result = mWorkflow.compile();

		Log.out.println("Finished compiling workflow");

		return result;
	}


	public State invokeRemote(Bundle aSubGraph, State aInState, State aOutState)
	{
		if (!mConnectionManager.isOpen())
		{
			throw new IllegalStateException("This engine isn't connected to any servers.");
		}

		try (RPCConnection connection = mConnectionManager.claim())
		{
			RemoteScopeRPCSender caller = connection.create(RemoteScopeRPCSender.class);

			return caller.call(aSubGraph, aInState, aOutState);
		}
	}


	public void start(State aState)
	{
		start(aState, aState);
	}
	
	
	public void start(State aInState, State aOutState)
	{
		Log.out.println("Start executing workflow");

		try
		{
			mWorkflow.execute(this, aInState, aOutState);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		finally
		{
			Log.out.println("Finished executing workflow");
		}
	}


	public void executeActivity(AbstractActivity aActivity, State aInState, State aOutState)
	{
		try
		{
			aActivity.setState(ActivityState.RUNNING);
			aActivity.execute(this, aInState, aOutState);
			aActivity.setState(ActivityState.FINISHED);
		}
		catch (RuntimeException e)
		{
			aActivity.setState(ActivityState.EXCEPTION);
			throw e;
		}
	}
}
