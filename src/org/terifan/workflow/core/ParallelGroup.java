package org.terifan.workflow.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.terifan.workflow.util.Helper;


public abstract class ParallelGroup extends Group
{
	@Override
	public void execute(final WorkflowEngine aWorkflowEngine, final State aInState, final State aOutState)
	{
		try
		{
			ExecutorService service = Executors.newFixedThreadPool(computeNumThreads());

			for (final AbstractActivity child : this)
			{
				service.execute(new Runnable()
				{
					@Override
					public void run()
					{
						aWorkflowEngine.executeActivity(child, aInState, aOutState);
					}
				});
			}

			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException e)
		{
			throw new IllegalStateException(e);
		}
	}
	

	/**
	 * Return the number of percent of available CPUs should be used by this
	 * parallel group. Override this implementation to customize the count.
	 * Default is 100%.
	 *
	 * @return
	 *   always return the value 100.
	 */
	public int getMaxThreadPercent()
	{
		return 100;
	}

	
	private int computeNumThreads()
	{
		return Math.min(Math.max(1, Helper.getAvailableCpuCount() * getMaxThreadPercent() / 100), getChildCount());
	}
}