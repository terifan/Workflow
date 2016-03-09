package org.terifan.workflow.client.workflow_pane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.terifan.ui.vectorgraphics.Region;


public class DropRegion extends Region
{
	private static boolean mDragActive;
	private boolean mDragHover;
	
	public DropRegion(String aIdentity, int x, int y, int w, int h)
	{
		super(aIdentity, x, y, w, h);
	}


	public DropRegion(String aIdentity, int x, int y, int w, int h, Object aUserObject)
	{
		super(aIdentity, x, y, w, h, aUserObject);
	}


	public DropRegion(String aIdentity, Rectangle aRectangle)
	{
		super(aIdentity, aRectangle);
	}


	public DropRegion(String aIdentity, Rectangle aRectangle, Object aUserObject)
	{
		super(aIdentity, aRectangle, aUserObject);
	}


	public void setDragHover(boolean aDragHover)
	{
		mDragHover = aDragHover;
	}


	public static void setDragActive(boolean aDragActive)
	{
		mDragActive = aDragActive;
	}

	
	@Override
	public void render(Graphics2D aGraphics)
	{
		super.render(aGraphics);

		if (mDragHover || mDragActive)
		{
			Color old = aGraphics.getColor();

			aGraphics.setColor(mDragHover ? new Color(0,255,0,32) : new Color(255,255,0,32));
			aGraphics.fill(getBounds());

			aGraphics.setColor(old);
		}
	}
}
