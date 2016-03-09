package org.terifan.workflow.client.activities_layout;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.workflow.core.Group;
import org.terifan.workflow.client.workflow_pane.RegionAction;
import org.terifan.workflow.client.workflow_pane.RegionData;
import org.terifan.ui.StyleSheet;
import org.terifan.ui.vectorgraphics.Layer;
import org.terifan.ui.vectorgraphics.Region;
import org.terifan.ui.Anchor;
import org.terifan.ui.GradientStyle;
import org.terifan.ui.StrokeStyle;


public abstract class AbstractGroupLayout extends AbstractActivityLayout // implements Iterable<ActivityLayout>
{
	protected Group mGroup;


	public AbstractGroupLayout()
	{
	}


	@Override
	public void bind(WorkflowPane aWorkflowPane, AbstractActivity aActivity)
	{
		super.bind(aWorkflowPane, aActivity);

		mGroup = (Group)aActivity;
	}


	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		if (this instanceof WorkflowLayout)
		{
			return;
		}

		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();
		int labelHeight = style.getInt("labelHeight");

		boolean drawCollapse = true; //!((this instanceof SequenceActivity) && (getParent() instanceof ParallelGroup));

		BufferedImage button = isCollapsed() ? style.getImage("expand") : style.getImage("collapse");
		int textIndent = drawCollapse ? button.getWidth()+10 : 0;

		Stroke oldStroke = aLayer.getStroke();

		aLayer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		aLayer.setPaint(Enum.valueOf(GradientStyle.class, style.getString("backStyle").toUpperCase()).createGradientPaint(bounds, style.getColor("backStart"), style.getColor("backEnd")));
		aLayer.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);
		aLayer.setColor(style.getColor("border"));
		aLayer.setStroke(Enum.valueOf(StrokeStyle.class, style.getString("borderStyle").toUpperCase()).createBasicStroke(1));
		aLayer.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
		aLayer.setStroke(oldStroke);

		Rectangle textBounds = aLayer.getTextBounds(mActivity.getLabel(), bounds.x+textIndent, bounds.y+1, bounds.width-textIndent-2, labelHeight-style.getInt("iconHeight"), Anchor.CENTER, false, false);

		aLayer.setBackground(null);
		aLayer.setColor(style.getColor("text"));
		aLayer.drawString(mActivity.getLabel(), textBounds, Anchor.CENTER, false);

		aLayer.add(new Region("state", textBounds, new RegionData(RegionAction.LABEL, mActivity, null)));

		if (drawCollapse)
		{
			int ix = textBounds.x-button.getWidth()-5;
			int iy = textBounds.y+(textBounds.height-button.getHeight())/2;
			aLayer.drawImage(button, ix, iy, null);

			aLayer.add(new Region("menu", ix-4, textBounds.y, textBounds.x-ix+4, textBounds.height, new RegionData(RegionAction.STATE, mActivity, null)));
		}

		BufferedImage icon = style.getImage("icon");
		aLayer.drawImage(icon, bounds.x+(bounds.width-icon.getWidth())/2, bounds.y+labelHeight-icon.getHeight()-2, null);

		if (!isCollapsed())
		{
			for (AbstractActivity activity : mGroup)
			{
				mWorkflowPane.getActivityLayout(activity).render(aStyleSheet, aLayer.createLayer());
			}
		}
	}
}