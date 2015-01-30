package org.terifan.apps.workflow.core;

import java.awt.datatransfer.DataFlavor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.terifan.util.bundle.Bundle;
import org.terifan.util.log.Log;


public abstract class AbstractActivity
{
	public final static DataFlavor mObjectDataFlavor;

	private String mLabel;

	private transient ActivityState mState;
	private transient String mSource;
	private transient Group mParent;

	private transient static int mCounter;
	private HashMap<Class,ArrayList<String>> mAccessorFields;
	private long mCacheKey;


	static
	{
		try
		{
			mObjectDataFlavor = new DataFlavor("application/x-java-jvm-local-objectref; class=java.io.InputStream");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	public AbstractActivity()
	{
		mState = ActivityState.NONE;

		mSource = "";
		mAccessorFields = new HashMap<>();

		mLabel = getClass().getSimpleName() + (++mCounter);
		mLabel = mLabel.substring(0,1).toLowerCase()+mLabel.substring(1);
	}


	public abstract boolean compile();


	public void execute(WorkflowEngine aEngine, State aState)
	{
		execute(aEngine, aState, aState);
	}


	public abstract void execute(WorkflowEngine aEngine, State aInState, State aOutState);


	public void initialize()
	{
	}


	public ArrayList<String> getAccessorFields(Class aAnnotation)
	{
		if (mSource.hashCode() != mCacheKey)
		{
			mCacheKey = mSource.hashCode();
			mAccessorFields.clear();

			Class [] types = new Class[]{Executable.In.class, Executable.Out.class, Executable.InOut.class};

			for (Class type : types)
			{
				mAccessorFields.put(type, new ArrayList<String>());
			}

			if (!mSource.isEmpty())
			{
				Executable executable = Compiler.compile(this, mSource.toString());
				FieldMap map = new FieldMap(executable).setIncludeAnnotations(types);

				for (String name : map.getFieldNames())
				{
					Field field = map.getField(name);
					for (Class type : types)
					{
						if (field.getAnnotation(type) != null)
						{
							mAccessorFields.get(type).add(name);
						}
					}
				}
			}
		}

		return mAccessorFields.get(aAnnotation);
	}


	protected void setParent(Group aParent)
	{
		mParent = aParent;
	}


	public Group getParent()
	{
		return mParent;
	}


	public void setLabel(String aLabel)
	{
		mLabel = aLabel;
	}


	public String getLabel()
	{
		return mLabel;
	}


	public void setState(ActivityState aState)
	{
		mState = aState;
	}


	public ActivityState getState()
	{
		return mState;
	}


	public String getSource()
	{
		return mSource;
	}


	public void setSource(String aSource)
	{
		mSource = aSource;
	}


	public void serialize(Bundle aBundle)
	{
		aBundle.putString("Class", getClass().getName());
		aBundle.putString("Label", mLabel);
		aBundle.putInt("State", mState.ordinal());
		aBundle.putString("SourceCode", mSource.toString());
	}


	public void deserialize(Bundle aBundle)
	{
		mLabel = aBundle.getString("Label");
		mState = ActivityState.values()[aBundle.getInt("State", 0)];
		mSource = aBundle.getString("SourceCode", "");
	}


	public void visit(WorkflowVisitor aVisitor)
	{
		aVisitor.visit(this);
	}
	
	
	@Override
	public AbstractActivity clone()
	{
		AbstractActivity other;

		try
		{
			other = getClass().newInstance();
		}
		catch (IllegalAccessException | InstantiationException e)
		{
			throw new IllegalStateException(e);
		}

		Bundle bundle = new Bundle();
		this.serialize(bundle);
		other.deserialize(bundle);

		return other;
	}
}