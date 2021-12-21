package org.terifan.ui.deprecated_propertygrid;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;


public class ToggleListSelectionModel extends DefaultListSelectionModel
{
	private JList mList;

	public ToggleListSelectionModel(JList aList)
	{
		mList = aList;
	}

	@Override
	public void setSelectionInterval(int index0, int index1)
	{
		if (mList.isSelectedIndex(index0))
		{
			mList.removeSelectionInterval(index0, index1);
		}
		else
		{
			mList.addSelectionInterval(index0, index1);
		}
	}
}
