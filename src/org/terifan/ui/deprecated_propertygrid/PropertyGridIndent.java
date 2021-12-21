package org.terifan.ui.deprecated_propertygrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.terifan.ui.Utilities;


class PropertyGridIndent extends JComponent
{
	protected Property mProperty;


	public PropertyGridIndent(Property aProperty)
	{
		mProperty = aProperty;

		addMouseListener(new PropertyClickListener(mProperty, true));
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		PropertyGrid propertyGrid = mProperty.getPropertyGrid();
		StyleSheet style = propertyGrid.getStyleSheet();
		int indent = mProperty.getIndent() + 1;
		int indentWidth = style.getInt("indent_width");
		boolean selected = propertyGrid.getSelectedProperty() == mProperty;

		Color background;

		if (mProperty.isGroup())
		{
			aGraphics.setFont(style.getFont("group_font"));
			background = style.getColor("indent_background");
		}
		else if (selected && mProperty.isEditable() && !(mProperty instanceof PropertyList))
		{
			boolean focusOwner = Utilities.isWindowFocusOwner(propertyGrid);
			aGraphics.setFont(style.getFont("item_font"));
			background = focusOwner ? style.getColor("text_background_selected") : style.getColor("grid");
		}
		else
		{
			aGraphics.setFont(style.getFont("item_font"));
			background = style.getColor("text_background");
		}

		aGraphics.setColor(style.getColor("indent_background"));
		aGraphics.fillRect(0, 0, indentWidth, getHeight());

		aGraphics.setColor(background);
		aGraphics.fillRect(indentWidth, 0, getWidth() - indentWidth, getHeight());

		if (mProperty instanceof PropertyList)
		{
			int x = indent * indentWidth;
			BufferedImage image = ((PropertyList)mProperty).isCollapsed() ? style.getImage("expand_button") : style.getImage("collapse_button");
			aGraphics.drawImage(image, x - indentWidth + (indentWidth - image.getWidth()) / 2, (getHeight() - image.getHeight()) / 2, null);
		}
	}
}
