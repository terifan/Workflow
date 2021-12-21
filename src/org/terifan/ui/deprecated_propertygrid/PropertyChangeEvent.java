package org.terifan.ui.deprecated_propertygrid;


public class PropertyChangeEvent
{
	private PropertyGrid mPropertyGrid;
	private Property mProperty;


	public PropertyChangeEvent(PropertyGrid aPropertyGrid, Property aProperty)
	{
		mPropertyGrid = aPropertyGrid;
		mProperty = aProperty;
	}


	public PropertyGrid getPropertyGrid()
	{
		return mPropertyGrid;
	}


	public Property getProperty()
	{
		return mProperty;
	}


	@Override
	public String toString()
	{
		return mProperty.getLabel() + (mProperty.getKey() == null ? "" : "[" + mProperty.getKey() + "]") + "=" + mProperty.getValue();
	}
}
