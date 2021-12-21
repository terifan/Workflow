package org.terifan.ui.deprecated_propertygrid;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


class PropertyGridEditorListener implements FocusListener
{
	private Property mProperty;


	public PropertyGridEditorListener(Property aProperty)
	{
		mProperty = aProperty;
	}


	@Override
	public void focusGained(FocusEvent aEvent)
	{
		if (mProperty.getValueComponent() == aEvent.getComponent())
		{
			synchronized (this)
			{
				mProperty.getPropertyGrid().setSelectedProperty(mProperty);
			}

			mProperty.getPropertyGrid().repaint();
		}
	}


	@Override
	public void focusLost(FocusEvent aEvent)
	{
		if (mProperty.getValueComponent() == aEvent.getComponent())
		{
			PropertyGrid propertyGrid = mProperty.getPropertyGrid();

			synchronized (this)
			{
				if (propertyGrid.getSelectedProperty() == mProperty)
				{
					propertyGrid.updateValue(mProperty);
					propertyGrid.setSelectedProperty(null);
				}
			}

			propertyGrid.repaint();
		}
	}
}
