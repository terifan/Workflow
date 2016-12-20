package org.terifan.workflow.client.workflow_pane;

import org.terifan.vectorgraphics.Region;
import org.terifan.vectorgraphics.RegionSelector;


public class RegionFilter implements RegionSelector
{
	public final static RegionFilter DROP = new RegionFilter(RegionAction.DROP);
	public final static RegionFilter LABEL = new RegionFilter(RegionAction.LABEL);
	public final static RegionFilter SELECT = new RegionFilter(RegionAction.SELECT);
	public final static RegionFilter STATE = new RegionFilter(RegionAction.STATE);

	private RegionAction mAction;


	private RegionFilter(RegionAction aAction)
	{
		mAction = aAction;
	}


	@Override
	public boolean match(Region aRegion)
	{
		return ((RegionData)aRegion.getUserObject()).getAction() == mAction;
	}
}
