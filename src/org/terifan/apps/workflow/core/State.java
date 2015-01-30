package org.terifan.apps.workflow.core;

import java.io.Serializable;
import java.util.HashMap;


public class State implements Serializable
{
	private HashMap<String, Object> mMap = new HashMap<>();


	public void put(String aKey, Object aValue)
	{
		mMap.put(aKey, aValue);
	}


	public Object get(String aKey)
	{
		return mMap.get(aKey);
	}


	public boolean containsKey(String aKey)
	{
		return mMap.containsKey(aKey);
	}


	@Override
	public String toString()
	{
		return mMap.toString();
	}
	

	@Override
	public State clone()
	{
		State other;

		try
		{
			other = (State)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			other = new State();
		}

		other.mMap.putAll(this.mMap);
		
		return other;
	}


	public HashMap getMap()
	{
		return mMap;
	}


	public void merge(State aOther)
	{
		mMap.putAll(aOther.mMap);
	}
}