package org.terifan.workflow.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Rectangle;


public enum GradientStyleFactory
{
	VERTICAL,
	HORIZONTAL,
	FORWARD_DIAGONAL,
	BACKWARD_DIAGONAL;


	public GradientPaint createGradientPaint(Rectangle aBounds, Color aStartColor, Color aEndColor)
	{
		switch (this)
		{
			case HORIZONTAL:
				return new GradientPaint(aBounds.x, aBounds.y, aStartColor, aBounds.x + aBounds.width, aBounds.y, aEndColor);
			case VERTICAL:
				return new GradientPaint(aBounds.x, aBounds.y, aStartColor, aBounds.x, aBounds.y + aBounds.height, aEndColor);
			case FORWARD_DIAGONAL:
				return new GradientPaint(aBounds.x, aBounds.y, aStartColor, aBounds.x + aBounds.width, aBounds.y + aBounds.height, aEndColor);
			case BACKWARD_DIAGONAL:
				return new GradientPaint(aBounds.x + aBounds.width, aBounds.y, aStartColor, aBounds.x, aBounds.y + aBounds.height, aEndColor);
		}
		throw new IllegalStateException();
	}
}