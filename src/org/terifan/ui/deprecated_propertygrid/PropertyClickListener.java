package org.terifan.ui.deprecated_propertygrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class PropertyClickListener extends MouseAdapter
{
	private Property mProperty;
	private boolean mIndentComponent;


	PropertyClickListener(Property aProperty, boolean aIndentComponent)
	{
		mProperty = aProperty;
		mIndentComponent = aIndentComponent;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		if (mIndentComponent && (mProperty instanceof PropertyList))
		{
			PropertyGrid propertyGrid = mProperty.getPropertyGrid();

			int indentWidth = propertyGrid.getStyleSheet().getInt("indent_width");
			int x = mProperty.getIndent() * indentWidth;

			if (aEvent.getX() >= x && aEvent.getX() <= x + indentWidth)
			{
				PropertyList prop = (PropertyList)mProperty;
				prop.setCollapsed(!prop.isCollapsed());
				propertyGrid.setModel(propertyGrid.getModel());
				propertyGrid.redisplay();
			}
		}

		if (mProperty.getValueComponent() != null)
		{
			mProperty.getValueComponent().requestFocus();
		}
	}
}
