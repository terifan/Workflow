package org.terifan.workflow.client.activities_layout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.terifan.ui.StyleSheet;
import org.terifan.vectorgraphics.Layer;
import org.terifan.vectorgraphics.Anchor;
import org.terifan.workflow.util.GradientStyleFactory;
import org.terifan.workflow.util.StrokeStyleFactory;


public abstract class LeafLayout extends AbstractActivityLayout
{
	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();

		switch (mActivity.getState())
		{
			case RUNNING:
				aLayer.setColor(new Color(255,245,116,128));
				aLayer.fillRoundRect(bounds.x-5, bounds.y-5, bounds.width+10, bounds.height+10, 10, 10);
				break;
			case FINISHED:
				aLayer.setColor(new Color(197,255,173,128));
				aLayer.fillRoundRect(bounds.x-5, bounds.y-5, bounds.width+10, bounds.height+10, 10, 10);
				break;
			case EXCEPTION:
				aLayer.setColor(new Color(255,198,199,128));
				aLayer.fillRoundRect(bounds.x-5, bounds.y-5, bounds.width+10, bounds.height+10, 10, 10);
				break;
		}

		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aLayer.setPaint(Enum.valueOf(GradientStyleFactory.class, style.getString("backStyle").toUpperCase()).createGradientPaint(bounds, style.getColor("backStart"), style.getColor("backEnd")));
		aLayer.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);
		aLayer.setColor(style.getColor("border"));
		aLayer.setStroke(Enum.valueOf(StrokeStyleFactory.class, style.getString("borderStyle").toUpperCase()).createBasicStroke(1));
		aLayer.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);
		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		BufferedImage icon = style.getImage("icon");
		aLayer.drawImage(icon, bounds.x+(24-icon.getWidth())/2, bounds.y+(bounds.height-icon.getHeight())/2);

		aLayer.setBackground(null);
		aLayer.setColor(style.getColor("text"));
//		g.drawString(getLabel(), bounds.x+25, bounds.y+1, bounds.width-26, bounds.height, Anchor.WEST, false);
		aLayer.drawString(mActivity.getLabel(), bounds.x+25, bounds.y+1, bounds.width-26, bounds.height, Anchor.WEST, true);
	}
}
