package org.terifan.workflow.client.activities_layout;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.terifan.ui.StyleSheet;
import org.terifan.vectorgraphics.Layer;


public class IfElseLayout extends HorizontalGroupLayout
{
	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();

		int labelHeight = style.getInt("labelHeight");

		int y0 = bounds.y + labelHeight;
		int y1 = bounds.y + bounds.height;
		int x0 = bounds.x + bounds.width / 2;
		int hs = style.getInt("headerSize") - labelHeight;
		int fs = style.getInt("footerSize");

		y0 += hs / 2;
		y1 -= fs / 2;

		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aLayer.fillPolygon(new int[]{x0 - 6, x0, x0 + 6, x0}, new int[]{y0, y0 + 6, y0, y0 - 6}, 4);
		aLayer.fillPolygon(new int[]{x0 - 6, x0, x0 + 6, x0}, new int[]{y1, y1 + 6, y1, y1 - 6}, 4);
		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}