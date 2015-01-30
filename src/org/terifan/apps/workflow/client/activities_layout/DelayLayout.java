package org.terifan.apps.workflow.client.activities_layout;

import javax.swing.JSlider;
import org.terifan.apps.workflow.activities.DelayActivity;
import org.terifan.util.log.Log;


public class DelayLayout extends LeafLayout
{
	public DelayLayout()
	{
		JSlider slider = new JSlider(0, 10000, 1000);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);

		mSwingComponent = slider;
	}


	@Override
	public void updateActivityState()
	{
		((DelayActivity)mActivity).setDelay(((JSlider)mSwingComponent).getValue());
	}
}