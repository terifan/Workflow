package org.terifan.apps.workflow.core;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;


public class ValueList<K,V> implements Iterable<V>
{
	private List<K> mKeys;
	private List<V> mValues;
	private List<ValueListChangeListener> mChangeListeners;
	private long mFailFastCounter;


	public ValueList()
	{
		mKeys = new ArrayList<>();
		mValues = new ArrayList<>();
		mChangeListeners = null;
		mFailFastCounter = 0;
		mChangeListeners = new ArrayList<>();
	}


	public void addChangeListener(ValueListChangeListener aListener)
	{
		mChangeListeners.add(aListener);
	}


	public void removeChangeListener(ValueListChangeListener aListener)
	{
		mChangeListeners.remove(aListener);
	}


	public void clear()
	{
		while (mValues.size() > 0)
		{
			remove(0);
		}
	}


	public int size()
	{
		return mValues.size();
	}


	public boolean isEmpty()
	{
		return mValues.isEmpty();
	}


	public void add(V aValue)
	{
		add(null, aValue);
	}


	public void add(int aIndex, V aValue)
	{
		add(aIndex, null, aValue);
	}


	public void add(K aKey, V aValue)
	{
		mKeys.add(aKey);
		mValues.add(aValue);
		mFailFastCounter++;

		for (ValueListChangeListener<K,V> listener : mChangeListeners)
		{
			listener.valueAdded(this, aKey, aValue);
		}
	}


	public void add(int aIndex, K aKey, V aValue)
	{
		mKeys.add(aIndex, aKey);
		mValues.add(aIndex, aValue);
		mFailFastCounter++;

		for (ValueListChangeListener<K,V> listener : mChangeListeners)
		{
			listener.valueAdded(this, aKey, aValue);
		}
	}


	public void addOrSet(K aKey, V aValue)
	{
		int i = mKeys.indexOf(aKey);
		if (i == -1)
		{
			add(aKey, aValue);
		}
		else
		{
			set(i, aValue);
		}
	}


	public void set(int aIndex, V aValue)
	{
		mValues.set(aIndex, aValue);
		mFailFastCounter++;

		K key = mKeys.get(aIndex);
		for (ValueListChangeListener<K,V> listener : mChangeListeners)
		{
			listener.valueChanged(this, key, aValue);
		}
	}


	public void set(K aKey, V aValue)
	{
		int i = mKeys.indexOf(aKey);
		if (i != -1)
		{
			mValues.set(i, aValue);
			mFailFastCounter++;

			for (ValueListChangeListener<K,V> listener : mChangeListeners)
			{
				listener.valueChanged(this, aKey, aValue);
			}
		}
	}


	public V get(int aIndex)
	{
		return mValues.get(aIndex);
	}


	public V get(K aKey)
	{
		int i = mKeys.indexOf(aKey);
		if (i == -1)
		{
			return null;
		}
		return mValues.get(i);
	}


	public int indexOfValue(V aValue)
	{
		return mValues.indexOf(aValue);
	}


	public int indexOfKey(K aKey)
	{
		return mKeys.indexOf(aKey);
	}


	public boolean containsValue(V aValue)
	{
		return mValues.contains(aValue);
	}


	public boolean containsKey(K aKey)
	{
		return mKeys.contains(aKey);
	}


	public void remove(K aKey)
	{
		int i = mKeys.indexOf(aKey);
		if (i != -1)
		{
			remove(i);
		}
	}


	public void remove(int aIndex)
	{
		K key = mKeys.remove(aIndex);
		V value = mValues.remove(aIndex);
		mFailFastCounter++;

		for (ValueListChangeListener<K,V> listener : mChangeListeners)
		{
			listener.valueRemoved(this, key, value);
		}
	}


//	public V [] toArray(Class<V> aType)
//	{
//		Object [] arr = (Object[])Array.newInstance(aType, mValues.size());
//		for (int i = 0, sz = mValues.size(); i < sz; i++)
//		{
//			arr[i] = mValues.get(i);
//		}
//		return (V[])arr;
//	}


	@Override
	public Iterator<V> iterator()
	{
		return new Iterator<V>() {
			int index;
			long failFast;

			{
				failFast = mFailFastCounter;
			}

			@Override
			public boolean hasNext()
			{
				if (failFast != mFailFastCounter)
				{
					throw new ConcurrentModificationException();
				}
				return index < size();
			}


			@Override
			public V next()
			{
				if (failFast != mFailFastCounter)
				{
					throw new ConcurrentModificationException();
				}
				return get(index++);
			}


			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
