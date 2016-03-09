package org.terifan.workflow.client.activities_layout;

import java.awt.Rectangle;
import org.terifan.workflow.client.workflow_pane.DropRegion;
import org.terifan.workflow.client.workflow_pane.RegionAction;
import org.terifan.workflow.client.workflow_pane.RegionData;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.ui.vectorgraphics.Layer;
import org.terifan.ui.StyleSheet;



public class MapReduceLayout extends ParallelGroupLayout
{
	public MapReduceLayout()
	{
	}


	@Override
	protected void renderList(Layer aLayer, StyleSheet aStyleSheet)
	{
		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();
		int labelHeight = style.getInt("labelHeight");

		AbstractActivityLayout first = mWorkflowPane.getActivityLayout(mGroup.getFirstChild());
		AbstractActivityLayout last = mWorkflowPane.getActivityLayout(mGroup.getLastChild());

		int y0 = bounds.y+labelHeight;
		int y1 = bounds.y+bounds.height;
		int xc = bounds.x+bounds.width/2;
		int xf = first.getBounds().x + first.getBounds().width/2;
		int xl = last.getBounds().x + last.getBounds().width/2;
		int hs = style.getInt("headerSize")-labelHeight;
		int fs = style.getInt("footerSize");

		aLayer.setColor(style.getColor("line"));
		aLayer.drawLine(xc, y0+2, xc, y0+hs/2);
		aLayer.drawLine(xf, y0+hs/2, xc, y0+hs/2);
		aLayer.drawLine(xc, y1-fs/2, xc, y1-1);
		aLayer.drawLine(xc, y1-fs/2, xl, y1-fs/2);

		Rectangle ab = first.getBounds();
		int abx = ab.x+ab.width/2;
		aLayer.drawLine(abx, y0+hs/2, abx, y0+hs);

		Rectangle cd = last.getBounds();
		int cdx = cd.x+cd.width/2;
		int cdy = cd.height;
		aLayer.drawLine(cdx, y0+cdy, cdx, y1-fs/2);

		xc=(last.getBounds().x + first.getBounds().x+first.getBounds().width) / 2 - 5;
		y0+=hs+Math.min(ab.height, cd.height)/2-5;
		aLayer.drawLine(xc+10, y0, xc, y0);
		aLayer.drawLine(xc, y0, xc+5, y0+5);
		aLayer.drawLine(xc+5, y0+5, xc, y0+10);
		aLayer.drawLine(xc, y0+10, xc+10, y0+10);
	}
}