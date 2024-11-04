package org.terifan.workflow.client.activities_layout;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.vectorgraphics.Layer;
import org.terifan.ui.StyleSheet;


public class WorkflowLayout extends SequentialGroupLayout
{
	@Override
	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		super.render(aStyleSheet, aLayer);

		StyleSheet style = aStyleSheet.getStyleSheet(getStyleName());
		Rectangle bounds = getBounds();

		BufferedImage image = style.getImage("start");
		aLayer.drawImage(image, bounds.x+(bounds.width-image.getWidth())/2, bounds.y);

		image = style.getImage("end");
		aLayer.drawImage(image, bounds.x+(bounds.width-image.getWidth())/2, bounds.y+bounds.height-image.getHeight());

		for (AbstractActivity activity : mGroup)
		{
			mWorkflowPane.getActivityLayout(activity).render(aStyleSheet, aLayer.createLayer());
		}
	}
}