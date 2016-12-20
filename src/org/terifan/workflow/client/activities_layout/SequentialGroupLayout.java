package org.terifan.workflow.client.activities_layout;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.terifan.workflow.activities.MapReduceActivity;
import org.terifan.workflow.client.workflow_pane.DropRegion;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.client.workflow_pane.RegionAction;
import org.terifan.workflow.client.workflow_pane.RegionData;
import org.terifan.ui.StyleSheet;
import org.terifan.ui.vectorgraphics.Layer;
import org.terifan.ui.vectorgraphics.Anchor;


public class SequentialGroupLayout extends AbstractGroupLayout
{
	public SequentialGroupLayout()
	{
	}


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
		StyleSheet style = aStyleSheet.getRoot().getStyleSheet(getStyleName());
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
			height -= style.getInt("spady");
			for (AbstractActivity activity : mGroup)
			{
				AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);

				height += style.getInt("spady");
				layout.layoutSize(aStyleSheet);
				width = Math.max(width, layout.getBounds().width);
				height += layout.getBounds().height;
			}
			width += style.getInt("ipadx");
			for (AbstractActivity activity : mGroup)
			{
				AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
				layout.getBounds().x += (width-layout.getBounds().width)/2;
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
		Dimension dim = getBounds().getSize();

		y += style.getInt("headerSize");

		for (AbstractActivity activity : mGroup)
		{
			AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
			int ix = x+(dim.width-layout.getBounds().width)/2;
			layout.layoutPosition(aStyleSheet, ix, y);
			y += layout.getBounds().height+style.getInt("spady");
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

		aLayer.setColor(style.getColor("line"));

		int y1 = bounds.y+style.getInt("headerSize");
		int y2 = y1+40;
		int y3 = bounds.y+bounds.height-style.getInt("padbottom");

		aLayer.drawLine(x0, y0+2+2, x0, y1-1);

		aLayer.setBackground(null);
		aLayer.setColor(style.getColor("text"));
		aLayer.drawString("Drop Activities\nHere", bounds.x+1, y1, bounds.width-2, 40, Anchor.CENTER, true);

		aLayer.add(new DropRegion("drop", bounds.x, y0, bounds.width, y3-y0, new RegionData(RegionAction.DROP, mActivity, null)));

		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aLayer.drawLine(x0, y2+2, x0, y3-6);
		aLayer.fillPolygon(new int[]{x0,x0-4,x0,x0+5,x0+1}, new int[]{y3,y3-9,y3-7,y3-9,y3}, 5);
		aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}


	private void renderList(Layer aLayer, StyleSheet aStyleSheet)
	{
		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();
		int labelHeight = style.getInt("labelHeight");

		int x0 = bounds.x+bounds.width/2;
		int y0 = bounds.y+labelHeight;
		boolean first = true;

		aLayer.setColor(style.getColor("line"));

		int x = getBounds().x;
		int w = getBounds().width;

		for (AbstractActivity activity : mGroup)
		{
			AbstractActivityLayout layout = mWorkflowPane.getActivityLayout(activity);
			int y1 = layout.getBounds().y-1;

			aLayer.add(new DropRegion("drop", x, y0, w, y1-y0, new RegionData(RegionAction.DROP, mActivity, activity)));

			if (first)
			{
				aLayer.drawLine(x0, y0+2+2, x0, y1-1);
				first = false;
			}
			else
			{
				aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				aLayer.drawLine(x0, y0+2, x0, y1-6);
				aLayer.fillPolygon(new int[]{x0,x0-4,x0,x0+5,x0+1}, new int[]{y1,y1-9,y1-7,y1-9,y1}, 5);
				aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			y0 = layout.getBounds().y+layout.getBounds().height;
		}

		int y1 = bounds.y+bounds.height-style.getInt("padbottom");

		aLayer.add(new DropRegion("drop", x, y0, w, y1-y0, new RegionData(RegionAction.DROP, mActivity, null)));

		if (!(mActivity.getParent() instanceof MapReduceActivity))
		{
			aLayer.setColor(style.getColor("line"));
			aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			aLayer.drawLine(x0, y0+2, x0, y1-6);
			aLayer.fillPolygon(new int[]{x0,x0-4,x0,x0+5,x0+1}, new int[]{y1,y1-9,y1-7,y1-9,y1}, 5);
			aLayer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}
}