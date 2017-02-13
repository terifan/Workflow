package org.terifan.workflow.client.activities_layout;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.terifan.workflow.core.AbstractActivity;
import org.terifan.workflow.core.ActivityState;
import org.terifan.workflow.client.workflow_pane.WorkflowPane;
import org.terifan.workflow.client.workflow_pane.RegionAction;
import org.terifan.workflow.client.workflow_pane.RegionData;
import static org.terifan.workflow.client.activities_layout.DefaultLayout.mObjectDataFlavor;
import org.terifan.vectorgraphics.Layer;
import org.terifan.vectorgraphics.Region;
import org.terifan.vectorgraphics.Anchor;
import org.terifan.ui.StyleSheet;
import org.terifan.ui.sourceeditor.Document;
import org.terifan.ui.sourceeditor.JavaSyntaxParser;
import org.terifan.ui.sourceeditor.SourceEditor;


public abstract class AbstractActivityLayout
{
	public final static DataFlavor mObjectDataFlavor;

	protected boolean mCollapsed;
	protected String mTextBubble;
	protected JComponent mSwingComponent;
	protected Rectangle mBounds;
	protected AbstractActivity mActivity;
	protected WorkflowPane mWorkflowPane;
	protected Document mSource;


	static
	{
		try
		{
			mObjectDataFlavor = new DataFlavor("application/x-java-jvm-local-objectref; class=java.io.InputStream");
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}


	public AbstractActivityLayout()
	{
		mBounds = new Rectangle();
		mSource = new Document();

		SourceEditor sourceEditor = new SourceEditor(new JavaSyntaxParser(), mSource);
		sourceEditor.dontRequestFocus();

		mSwingComponent = new JScrollPane(sourceEditor);
	}


	public void bind(WorkflowPane aWorkflowPane, AbstractActivity aActivity)
	{
		mWorkflowPane = aWorkflowPane;
		mActivity = aActivity;

		mSource.setText(aActivity.getSource());
	}


	public void setCollapsed(boolean aCollapsed)
	{
		mCollapsed = aCollapsed;
	}


	public boolean isCollapsed()
	{
		return mCollapsed;
	}


	protected void setBounds(Rectangle aBounds)
	{
		mBounds = aBounds;
	}


	public Rectangle getBounds()
	{
		return mBounds;
	}


	public boolean contains(Point aPoint)
	{
		return mBounds.contains(aPoint);
	}


	protected String getStyleName()
	{
		return mActivity.getParent() == null ? "WorkflowRootActivity" : mActivity.getClass().getSimpleName();
	}


	public JComponent getSwingComponent()
	{
		return mSwingComponent;
	}


	public void render(StyleSheet aStyleSheet, Layer aLayer)
	{
		Rectangle bounds = getBounds();

		aLayer.add(new Region("select", bounds, new RegionData(RegionAction.SELECT, mActivity, null)));

		if (mActivity.getState() == ActivityState.COMPILER_ERROR)
		{
			BufferedImage image = aStyleSheet.getRoot().getImage("activity_error");
			aLayer.getCanvas().getLayer("overlay").drawImage(image, mBounds.x+mBounds.width-image.getWidth()-5, mBounds.y-image.getHeight()+3, null);
		}

		if (getTextBubble() != null && getTextBubble().length() > 0)
		{
			renderTextBubble(aLayer, bounds);
		}
	}


	public void layoutSize(StyleSheet aStyleSheet)
	{
		getBounds().setSize(130, 40);
	}


	public void layoutPosition(StyleSheet aStyleSheet, int x, int y)
	{
		getBounds().setLocation(x, y);
	}


	public String getTextBubble()
	{
		return mTextBubble;
	}


	public void setTextBubble(String aTextBubble)
	{
		mTextBubble = aTextBubble;
	}


	public AbstractActivity getActivity()
	{
		return mActivity;
	}


	public void setSource(Document aSource)
	{
		mSource = aSource;

		if (mSwingComponent instanceof JScrollPane)
		{
			((SourceEditor)((JScrollPane)mSwingComponent).getViewport().getView()).setDocument(aSource);
		}
	}


	public Document getSource()
	{
		return mSource;
	}


	public void updateActivityState()
	{
	}


	private void renderTextBubble(Layer aLayer, Rectangle bounds)
	{
		Layer bl = aLayer.getCanvas().getLayer("bubbles");

		Rectangle tb = bl.getTextBounds(getTextBubble(), bounds.x+bounds.width-30, 0, 350, 0, Anchor.SOUTH_WEST, true, false);

		tb.y = bounds.y-tb.height-3;

		if (tb.y < 10)
		{
			tb.y = 10;
		}
		if (tb.width < 40)
		{
			tb.width = 40;
		}

		int x = tb.x-5;
		int y = tb.y-5;
		int w = tb.width+10;
		int h = tb.height+10;

		bl.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		bl.setColor(new Color(255,245,116));
		bl.fillRoundRect(x, y, w, h, 10, 10);
		bl.setColor(Color.BLACK);
		bl.drawRoundRect(x, y, w, h, 10, 10);
		bl.setColor(new Color(255,245,116));
		bl.fillPolygon(x+20, y+h, x+20, y+h+10, x+20+10, y+h);
		bl.setColor(Color.BLACK);
		bl.drawLine(x+20, y+h, x+20, y+h+10, x+20+10, y+h);
		bl.setBackground(null);
		bl.drawString(getTextBubble(), tb.x, tb.y, tb.width, tb.height, Anchor.WEST, true);
		bl.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
