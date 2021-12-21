package org.terifan.ui.deprecated_propertygrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import org.terifan.bundle.Bundle;


public class PropertyGridModel implements Iterable<Property>, Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;

	protected ArrayList<Property> mChildren;
	protected Object mUserObject;


	public PropertyGridModel()
	{
		mChildren = new ArrayList<>();
	}


	public PropertyGridModel addProperty(Property aProperty)
	{
		mChildren.add(aProperty);
		return this;
	}


	public Property getProperty(int aIndex)
	{
		return mChildren.get(aIndex);
	}


	public Object getUserObject()
	{
		return mUserObject;
	}


	public PropertyGridModel setUserObject(Object aUserObject)
	{
		mUserObject = aUserObject;
		return this;
	}


	public void removeAll()
	{
		mChildren.clear();
	}


	public int size()
	{
		return mChildren.size();
	}


	public ArrayList<Property> getChildren()
	{
		return mChildren;
	}


	@Override
	public Iterator<Property> iterator()
	{
		ArrayList<Property> list = new ArrayList<>(mChildren);
		Collections.sort(list);
		return list.iterator();
	}


	ArrayList<Property> getAllProperties()
	{
		ArrayList<Property> out = new ArrayList<>();
		visit(out::add);
		return out;
	}


	void visit(Consumer<Property> aConsumer)
	{
		for (Property item : mChildren)
		{
			aConsumer.accept(item);

			if (item instanceof PropertyList && !((PropertyList)item).isCollapsed())
			{
				((PropertyList)item).visit(aConsumer);
			}
		}
	}


	@Override
	public PropertyGridModel clone() throws CloneNotSupportedException
	{
		PropertyGridModel clone = new PropertyGridModel();

		for (Property item : mChildren)
		{
			clone.mChildren.add(item.clone());
		}

		return clone;
	}


	public Bundle toBundle()
	{
		Bundle bundle = new Bundle();

		for (Property item : mChildren)
		{
			item.marshal(bundle);
		}

		return bundle;
	}
}