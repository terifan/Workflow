package org.terifan.apps.workflow.activities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.core.Compiler;
import org.terifan.apps.workflow.core.SequentialGroup;
import org.terifan.apps.workflow.core.WorkflowEngine;
import org.terifan.apps.workflow.core.ActivityState;
import org.terifan.apps.workflow.core.Executable.Name;
import org.terifan.apps.workflow.core.State;
import org.terifan.util.log.Log;


public class MapReduceActivity extends SequentialGroup
{
	private transient MapReduceExecutable mExecutable;


	public MapReduceActivity()
	{
	}


	@Override
	public void initialize()
	{
//		setSource(
//"import org.terifan.apps.workflow.activities.MapReduceExecutable;\n" +
//"import java.util.*;\n" +
//"\n" +
//"public class Solution extends MapReduceExecutable\n" +
//"{\n" +
//"	String text = \"Deer Bear River\\nCar Car River\\nDeer Car Bear\";\n" +
//"\n" +
//"	@Out HashMap<String,Integer> wordCounts = new HashMap<>();\n" +
//"\n" +
//"	@Output(\"in\")\n" +
//"	@Override\n" +
//"	public Iterable split()\n" +
//"	{\n" +
//"		return Arrays.asList(text.split(\"\\n\"));\n" +
//"	}\n" +
//"\n" +
//"	@Override\n" +
//"	public void reduce(Map aMap)\n" +
//"	{\n" +
//"		Map<String,Integer> map = (Map<String,Integer>)aMap.get(\"counts\");\n" +
//"\n" +
//"		for (String w : map.keySet())\n" +
//"		{\n" +
//"			int n = map.get(w);\n" +
//"			if (wordCounts.containsKey(w))\n" +
//"			{\n" +
//"				wordCounts.put(w, wordCounts.get(w) + n);\n" +
//"			}\n" +
//"			else\n" +
//"			{\n" +
//"				wordCounts.put(w, n);\n" +
//"			}\n" +
//"		}\n" +
//"	}\n" +
//"}");
//
//		CodeActivity code = new CodeActivity();
//		code.setSource(
//"import org.terifan.apps.workflow.core.Executable;\n" +
//"import java.util.*;\n" +
//"\n" +
//"public class Solution extends Executable\n" +
//"{\n" +
//"	@In String in;\n" +
//"	@Out HashMap<String,Integer> counts = new HashMap<>();\n" +
//"\n" +
//"	@Override\n" +
//"	public void run()\n" +
//"	{\n" +
//"		for (String w : in.split(\" \"))\n" +
//"		{\n" +
//"			if (counts.containsKey(w))\n" +
//"			{\n" +
//"				counts.put(w, counts.get(w) + 1);\n" +
//"			}\n" +
//"			else\n" +
//"			{\n" +
//"				counts.put(w, 1);\n" +
//"			}\n" +
//"		}\n" +
//"	}\n" +
//"}");
//
//		SequenceActivity seq = new SequenceActivity();
//		seq.addChild(code);
//
//		this.addChild(seq);

		setSource(
"import org.terifan.apps.workflow.activities.MapReduceExecutable;\n" +
"\n" +
"public class Solution extends MapReduceExecutable\n" +
"{\n" +
"	String[] elements = {\"Deer Bear River\",\"Car Car River\",\"Deer Car Bear\"};\n" +
"	int index;\n" +
"\n" +
"	@Out String in;\n" +
"\n" +
"	@Override\n" +
"	public boolean next()\n" +
"	{\n" +
"		if (index < elements.length)\n" +
"		{\n" +
"			in = elements[index++];\n" +
"			return true;\n" +
"		}\n" +
"		return false;\n" +
"	}\n" +
"}");

		CodeActivity code1 = new CodeActivity();
		code1.setSource(
"import org.terifan.apps.workflow.core.Executable;\n" +
"import java.util.*;\n" +
"\n" +
"public class Solution extends Executable\n" +
"{\n" +
"	@In String in;\n" +
"	@Out HashMap<String,Integer> counts = new HashMap<>();\n" +
"\n" +
"	@Override\n" +
"	public void run()\n" +
"	{\n" +
"		for (String w : in.split(\" \"))\n" +
"		{\n" +
"			counts.put(w, counts.getOrDefault(w, 0) + 1);\n" +
"		}\n" +
"	}\n" +
"}");

		CodeActivity code2 = new CodeActivity();
		code2.setSource(
"import org.terifan.apps.workflow.core.Executable;\n" +
"import java.util.*;\n" +
"\n" +
"public class Solution extends Executable\n" +
"{\n" +
"	@In Map<String,Integer> counts;\n" +
"	@Out HashMap<String,Integer> wordCounts = new HashMap<>();\n" +
"\n" +
"	@Override\n" +
"	public void run()\n" +
"	{\n" +
"		for (String w : counts.keySet())\n" +
"		{\n" +
"			wordCounts.put(w, wordCounts.getOrDefault(w, 0) + counts.get(w));\n" +
"		}\n" +
"	}\n" +
"}");

		SequenceActivity seq1 = new SequenceActivity();
		seq1.setLabel("Map");
		seq1.addChild(code1);

		SequenceActivity seq2 = new SequenceActivity();
		seq2.setLabel("Reduce");
		seq2.addChild(code2);

		this.addChild(seq1);
		this.addChild(seq2);
	}


	@Override
	public boolean compile()
	{
		setState(ActivityState.NONE);

		mExecutable = (MapReduceExecutable)Compiler.compile(this, getSource());

		if (mExecutable == null)
		{
			return false;
		}

		Name name = mExecutable.getClass().getAnnotation(Name.class);
		if (name != null)
		{
			setLabel(name.value());
		}

		if (!super.compile())
		{
			return false;
		}

		return true;
	}


	@Override
	public void execute(WorkflowEngine aWorkflowEngine, State aInState, State aOutState)
	{
		Log.out.println("Exeucting " + getClass().getSimpleName());
		Log.out.println("State=" + aInState);

		mExecutable.setActivity(this);

		mExecutable.pushState(aWorkflowEngine, aInState);

		State initialState = aInState.clone();

		try
		{
			ExecutorService service = Executors.newFixedThreadPool(4);

			AbstractActivity reducer = getChild(1).clone();
			reducer.compile();

			while (mExecutable.next())
			{
				State localState = initialState.clone();

				mExecutable.popState(aWorkflowEngine, localState);

				service.execute(new Task(aWorkflowEngine, localState, reducer, aOutState));
			}

			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}


	private class Task implements Runnable
	{
		private State mLocalState;
		private State mOutState;
		private WorkflowEngine mWorkflowEngine;
		private AbstractActivity mReducer;


		public Task(WorkflowEngine aWorkflowEngine, State aLocalState, AbstractActivity aReducer, State aOutState)
		{
			mLocalState = aLocalState;
			mWorkflowEngine = aWorkflowEngine;
			mReducer = aReducer;
			mOutState = aOutState;
		}


		@Override
		public void run()
		{
			AbstractActivity child = getChild(0).clone();
			child.compile();

			mWorkflowEngine.executeActivity(child, mLocalState, mLocalState);

			synchronized (MapReduceActivity.class)
			{
				mWorkflowEngine.executeActivity(mReducer, mLocalState, mOutState);
			}
		}
	}
}