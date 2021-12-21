package org.terifan.ui.deprecated_propertygrid;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JComponent;


class PropertyGridLayout implements LayoutManager
{
	public PropertyGridLayout()
	{
	}


	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
	}


	@Override
	public Dimension preferredLayoutSize(Container aTarget)
	{
		return ((PropertyGridListPane)aTarget).getPreferredSize();
	}


	@Override
	public Dimension minimumLayoutSize(Container aTarget)
	{
		return ((PropertyGridListPane)aTarget).getPreferredSize();
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		synchronized (aTarget.getTreeLock())
		{
			PropertyGrid propertyGrid = ((PropertyGridListPane)aTarget).mPropertyGrid;
			PropertyGridModel model = propertyGrid.getModel();
			StyleSheet style = propertyGrid.getStyleSheet();
			int rowHeight = propertyGrid.getRowHeight();

			int y = 0;
			int dividerX = propertyGrid.getDividerPosition();
			int width = aTarget.getWidth();
			int indentWidth = style.getInt("indent_width");

			for (Property item : model.getAllProperties())
			{
				layoutPropertyComponents(propertyGrid, item, dividerX, y, width, indentWidth, rowHeight);
				y += rowHeight;
			}
		}
	}


	protected void layoutPropertyComponents(PropertyGrid aPropertyGrid, Property aItem, int aDividerX, int aY, int aWidth, int aIndentWidth, int aRowHeight)
	{
		aRowHeight--;
		
		int indent = aItem.getIndent() + 1;

		aItem.getIndentComponent().setBounds(0, aY, indent * aIndentWidth, aRowHeight);

		int btnWidth = 0;

		JButton button = aItem.getActionButton();
		if (button != null)
		{
			btnWidth = aRowHeight;
			button.setBounds(aWidth - btnWidth, aY, btnWidth, aRowHeight);
		}

		JComponent component = aItem.getValueComponent();
		component.setBounds(aDividerX + 1, aY, aWidth - aDividerX - btnWidth - 1, aRowHeight);

		component = aItem.getLabelComponent();
		component.setBounds(indent * aIndentWidth, aY, aDividerX - (indent * aIndentWidth), aRowHeight);
	}
}
