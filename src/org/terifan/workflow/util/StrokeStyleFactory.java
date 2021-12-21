package org.terifan.workflow.util;

import java.awt.BasicStroke;


public enum StrokeStyleFactory
{
	SOLID,
	DASH,
	DASH_DOT,
	DASH_DOT_DOT,
	LONG_DASH,
	LONG_DASH_DOT,
	LONG_DASH_DOT_DOT,
	LONG_DOT,
	DOT;


	public BasicStroke createBasicStroke(double aWidth)
	{
		switch (this)
		{
			case SOLID:
				return new BasicStroke((float)aWidth);
			case DASH:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{4f,1f}, 0f);
			case DASH_DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{4f,1f,1f,1f}, 0f);
			case DASH_DOT_DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{4f,1f,1f,1f,1f,1f}, 0f);
			case DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{1f,1f}, 0f);
			case LONG_DASH:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{8f,2f}, 0f);
			case LONG_DASH_DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{8f,2f,2f,2f}, 0f);
			case LONG_DASH_DOT_DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{8f,2f,2f,2f,2f,2f}, 0f);
			case LONG_DOT:
				return new BasicStroke((float)aWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{2f,2f}, 0f);
		}
		throw new IllegalStateException();
	}
}