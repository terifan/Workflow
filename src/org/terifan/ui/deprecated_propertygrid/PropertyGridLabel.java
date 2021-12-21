package org.terifan.ui.deprecated_propertygrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.ui.Utilities;


class PropertyGridLabel extends JComponent
{
	protected Property mProperty;


	public PropertyGridLabel(Property aProperty)
	{
		mProperty = aProperty;

		if (mProperty.getIndent() == 0)
		{
			setFocusable(true);
		}

		addMouseListener(new PropertyClickListener(mProperty, false));
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		PropertyGrid propertyGrid = mProperty.getPropertyGrid();
		StyleSheet style = propertyGrid.getStyleSheet();
		boolean selected = propertyGrid.getSelectedProperty() == mProperty;

		Utilities.enableTextAntialiasing(aGraphics);
		
		Color foreground;
		Color background;
		Font font;

		if (mProperty.isGroup())
		{
			foreground = style.getColor("indent_foreground_label");
			background = style.getColor("indent_background");
			font = style.getFont("group_font");
		}
		else
		{
			if (selected && mProperty.isEditable() && !(mProperty instanceof PropertyList))
			{
				boolean focusOwner = Utilities.isWindowFocusOwner(propertyGrid);
				foreground = focusOwner ? style.getColor("text_foreground_selected") : style.getColor("text_background");
				background = focusOwner ? style.getColor("text_background_selected") : style.getColor("grid");
			}
			else
			{
				foreground = style.getColor("text_foreground");
				background = style.getColor("text_background");
			}
			font = style.getFont("item_font");
		}

		Rectangle dim = new TextBox(mProperty.getLabel()).setPadding(0, 2, 0, 2).setBounds(0,0,getWidth(),getHeight()).setFont(font).setAnchor(Anchor.WEST).setForeground(foreground).setBackground(background).render(aGraphics).measure().getBounds();

		if (selected && (mProperty instanceof PropertyList || !mProperty.isEditable()))
		{
			aGraphics.setColor(foreground);
			Utilities.drawDottedRect(aGraphics, 0, 0, dim.width + 2, dim.height + 2, true);
		}
	}
}
