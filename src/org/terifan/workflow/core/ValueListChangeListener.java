package org.terifan.workflow.core;


public interface ValueListChangeListener<K,V>
{
	public void valueChanged(ValueList<K,V> aValueList, K aKey, V aValue);

	public void valueAdded(ValueList<K,V> aValueList, K aKey, V aValue);

	public void valueRemoved(ValueList<K,V> aValueList, K aKey, V aValue);
}
