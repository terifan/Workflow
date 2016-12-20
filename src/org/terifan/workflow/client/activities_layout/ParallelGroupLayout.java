package org.terifan.workflow.client.activities_layout;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.terifan.workflow.client.workflow_pane.DropRegion;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.client.workflow_pane.RegionAction;
import org.terifan.workflow.client.workflow_pane.RegionData;
import org.terifan.ui.StyleSheet;
import org.terifan.vectorgraphics.Layer;
import org.terifan.vectorgraphics.Anchor;


public class ParallelGroupLayout extends AbstractGroupLayout
{
	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		if (isCollapsed())
		{
			renderCollapsed(aLayer, aStyleSheet);
		}
		else if (mGroup.getChildCount() == 0)
		{
			renderEmpty(aLayer, aStyleSheet);
		}
		else
		{
			renderList(aLayer, aStyleSheet);
		}
	}


	@Override
	public void layoutSize(StyleSheet aStyleSheet)
	{
		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		int width = 0;
		int height = 0;

		if (isCollapsed())
		{
			width += 130;
			height += 50;
		}
		else if (mGroup.getChildCount() == 0)
		{
			width += 130;
			height += 40;
			height += style.getInt("headerSize")+style.getInt("footerSize");
		}
		else
		{
			width -= style.getInt("spadx");
			for (AbstractActivity activity : mGroup)
			{
				AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
				width += style.getInt("spadx");
				layout.layoutSize(aStyleSheet);
				width += layout.getBounds().width;
				height = Math.max(height, layout.getBounds().height);
			}
			width += style.getInt("ipadx");
			for (AbstractActivity activity : mGroup)
			{
				AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
				layout.getBounds().x += style.getInt("spadx");
			}
			height += style.getInt("headerSize")+style.getInt("footerSize");
		}

		getBounds().setSize(width, height);
	}


	@Override
	public void layoutPosition(StyleSheet aStyleSheet, int x, int y)
	{
		getBounds().setLocation(x, y);

		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());

		y += style.getInt("headerSize");

		x += style.getInt("ipadx")/2;
		for (AbstractActivity activity : mGroup)
		{
			AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
			layout.layoutPosition(aStyleSheet, x, y);
			x += layout.getBounds().width+style.getInt("spadx");
		}
	}


	private void renderCollapsed(Layer aLayer, StyleSheet aStyleSheet)
	{
	}


	private void renderEmpty(Layer aLayer, StyleSheet aStyleSheet)
	{
		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();
		int labelHeight = style.getInt("labelHeight");

		int x0 = bounds.x+bounds.width/2;
		int y0 = bounds.y+labelHeight;
		int y1 = bounds.y+style.getInt("headerSize");
		int y2 = y1+40;
		int y3 = bounds.y+bounds.height;

		aLayer.setColor(style.getColor("line"));
		aLayer.drawLine(x0, y0+2+2, x0, y1-1);

		aLayer.setBackground(null);
		aLayer.setColor(style.getColor("text"));
		aLayer.drawString("Drop Activities\nHere", bounds.x+1, y1, bounds.width-2, 40, Anchor.CENTER, true);

		aLayer.add(new DropRegion("drop", bounds.x, bounds.y+labelHeight, bounds.width, bounds.height-labelHeight, new RegionData(RegionAction.DROP, mActivity, null)));

		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aLayer.drawLine(x0, y2+2, x0, y3-6);
		aLayer.fillPolygon(new int[]{x0,x0-4,x0,x0+5,x0+1}, new int[]{y3,y3-9,y3-7,y3-9,y3}, 5);
		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}


	protected void renderList(Layer aLayer, StyleSheet aStyleSheet)
	{
		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();
		int labelHeight = style.getInt("labelHeight");

		AbstractActivityLayout first = mWorkflowPane.getActivityLayout(mGroup.getFirstChild());
		AbstractActivityLayout last = mWorkflowPane.getActivityLayout(mGroup.getLastChild());

		int y0 = bounds.y+labelHeight;
		int y1 = bounds.y+bounds.height;
		int x0 = bounds.x+bounds.width/2;
		int x1 = first.getBounds().x + first.getBounds().width/2;
		int x2 = last.getBounds().x + last.getBounds().width/2;
		int hs = style.getInt("headerSize")-labelHeight;
		int fs = style.getInt("footerSize");
		int pad = style.getInt("ipadx");

		aLayer.setColor(style.getColor("line"));
		aLayer.drawLine(x0, y0+2, x0, y0+hs/2);
		aLayer.drawLine(x1, y0+hs/2, x2, y0+hs/2);
		aLayer.drawLine(x0, y1-fs/2, x0, y1-1);
		aLayer.drawLine(x1, y1-fs/2, x2, y1-fs/2);

		for (int i = 0, sz = mGroup.getChildCount(), px = 0; i < sz; i++)
		{
			AbstractActivity activity = mGroup.getChild(i);
			AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);

			Rectangle ab = layout.getBounds();
			int x = ab.x+ab.width/2;
			int y = ab.height;
			aLayer.drawLine(x, y0+hs/2, x, y0+hs);
			aLayer.drawLine(x, y0+y, x, y1-fs/2);

			if (i == 0)
			{
				aLayer.add(new DropRegion("drop", ab.x-pad/2, y0+hs, pad/2, y1-fs-y0-hs, new RegionData(RegionAction.DROP, mActivity, activity)));
			}
			if (i == sz-1)
			{
				aLayer.add(new DropRegion("drop", ab.x+ab.width, y0+hs, pad/2, y1-fs-y0-hs, new RegionData(RegionAction.DROP, mActivity, null)));
			}
			if (i > 0)
			{
				aLayer.add(new DropRegion("drop", px, y0+hs, x-px-ab.width/2, y1-fs-y0-hs, new RegionData(RegionAction.DROP, mActivity, activity)));
			}

			px = x+ab.width/2;
		}
	}
}