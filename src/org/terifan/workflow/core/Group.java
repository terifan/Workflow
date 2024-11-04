package org.terifan.workflow.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.terifan.bundle.Array;
import org.terifan.bundle.Bundle;


public abstract class Group extends AbstractActivity implements Iterable<AbstractActivity>
{
	protected ArrayList<AbstractActivity> mChildren;


	public Group()
	{
		mChildren = new ArrayList<>();
	}


	@Override
	public void initialize()
	{
		super.initialize();

		for (AbstractActivity activity : mChildren)
		{
			activity.initialize();
		}
	}


	public AbstractActivity getChild(int aIndex)
	{
		return mChildren.get(aIndex);
	}


	public void addChild(AbstractActivity aActivity)
	{
		if (aActivity.getParent() != null)
		{
			aActivity.getParent().removeChild(aActivity);
		}
		mChildren.add(aActivity);
		aActivity.setParent(this);
	}


	public void addChild(int aIndex, AbstractActivity aActivity)
	{
		if (aActivity.getParent() != null)
		{
			aActivity.getParent().removeChild(aActivity);
		}
		mChildren.add(aIndex, aActivity);
		aActivity.setParent(this);
	}


	public void removeChild(AbstractActivity aActivity)
	{
		mChildren.remove(aActivity);
		aActivity.setParent(null);
	}


	public int indexOfChild(AbstractActivity aActivity)
	{
		return mChildren.indexOf(aActivity);
	}


	public int getChildCount()
	{
		return mChildren.size();
	}


	@Override
	public Iterator<AbstractActivity> iterator()
	{
		return mChildren.iterator();
	}


	@Override
	public boolean compile()
	{
		for (int i = 0; i < getChildCount(); i++)
		{
			if (!getChild(i).compile())
			{
				return false;
			}
		}

		return true;
	}


	@Override
	public void serialize(Bundle aBundle)
	{
		super.serialize(aBundle);

		Array list = new Array();

		for (AbstractActivity activity : mChildren)
		{
			Bundle bundle = new Bundle();
			activity.serialize(bundle);
			list.add(bundle);
		}

		aBundle.putArray("Children", list);
	}


	@Override
	public void deserialize(Bundle aBundle)
	{
		super.deserialize(aBundle);

		mChildren.clear();

		if (aBundle.containsKey("Children"))
		{
		for (Bundle bundle : aBundle.getArray("Children").iterable(Bundle.class))
		{
			try
			{
				AbstractActivity activity = (AbstractActivity)Class.forName(bundle.getString("Class")).newInstance();
				activity.deserialize(bundle);
				activity.setParent(this);

				mChildren.add(activity);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}
		}
	}


	@Override
	public void visit(WorkflowVisitor aVisitor)
	{
		aVisitor.visit(this);

		for (AbstractActivity activity : mChildren)
		{
			activity.visit(aVisitor);
		}
	}


	public AbstractActivity getFirstChild()
	{
		return mChildren.get(0);
	}


	public AbstractActivity getLastChild()
	{
		return mChildren.get(mChildren.size()-1);
	}
}
