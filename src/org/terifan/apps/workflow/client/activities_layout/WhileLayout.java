package org.terifan.apps.workflow.client.activities_layout;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.terifan.ui.StyleSheet;
import org.terifan.ui.vectorgraphics.Layer;


public class WhileLayout extends SequentialGroupLayout
{
	public WhileLayout()
	{
	}


	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		if (!isCollapsed())
		{
			StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
			Rectangle bounds = getBounds();

			// draw loop-back line and arrow

			int x0 = bounds.x+bounds.width/2+10;
			int y0 = bounds.y+38;
			int x1 = bounds.x+bounds.width-5;
			int y1 = bounds.y+bounds.height-6;
			int x2 = bounds.x+bounds.width/2;
			int y2 = bounds.y+bounds.height-1;
			aLayer.setColor(style.getColor("line"));
			aLayer.drawLine(x0, y0, x1, y0);
			aLayer.drawLine(x1, y0, x1, y1);
			aLayer.drawLine(x1, y1, x2, y1);
			aLayer.drawLine(x2, y1, x2, y2);
			aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			aLayer.fillPolygon(new int[]{x0,x0+9,x0+7,x0+9}, new int[]{y0,y0-4,y0,y0+5}, 4);
			aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}
}