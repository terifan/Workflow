package org.terifan.apps.workflow.client.activity_toolbox;

import org.terifan.apps.workflow.core.AbstractActivity;
import org.terifan.apps.workflow.client.activities_layout.AbstractActivityLayout;


public class ActivityType extends ActivityToolboxTreeNode
{
	private Class<? extends AbstractActivity> mActivityClass;
	private Class<? extends AbstractActivityLayout> mLayoutClass;
	private String mSource;

	private static int mCounter;


	public ActivityType(Class<? extends AbstractActivity> aType, Class<? extends AbstractActivityLayout> aLayout)
	{
		this(aType, aLayout, getLabel(aType), null);
	}

	
	public ActivityType(Class<? extends AbstractActivity> aType, Class<? extends AbstractActivityLayout> aLayout, String aLabel, String aSource)
	{
		super(aLabel);
		
		mActivityClass = aType;
		mLayoutClass = aLayout;
		mSource = aSource;
	}


	public Class<? extends AbstractActivity> getActivity()
	{
		return mActivityClass;
	}


	public Class<? extends AbstractActivityLayout> getLayout()
	{
		return mLayoutClass;
	}


	public AbstractActivity newInstance() throws IllegalAccessException, InstantiationException
	{
		AbstractActivity activity = mActivityClass.newInstance();
		
		activity.setLabel(toString());

		if (mSource != null)
		{
			activity.setSource(mSource);
		}

		return activity;
	}
	
	
	private static String getLabel(Class aType)
	{
		String name = aType.getSimpleName();

		if (name.endsWith("Activity"))
		{
			return name.substring(0, name.length()-8);
		}

		return name;
	}
}
