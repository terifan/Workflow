package org.terifan.ui.deprecated_propertygrid;

import java.io.Serializable;
import javax.swing.JCheckBox;
import org.terifan.bundle.Bundle;


public class CheckBoxProperty extends Property<JCheckBox, Boolean> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private boolean mSelected;


	public CheckBoxProperty(String aLabel, boolean aSelected)
	{
		super(aLabel);

		mSelected = aSelected;
	}


	@Override
	protected JCheckBox createValueComponent()
	{
		JCheckBox c = new JCheckBox();
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setSelected(mSelected);

		return c;
	}


	@Override
	public Boolean getValue()
	{
		return mSelected;
	}


	@Override
	public CheckBoxProperty setValue(Boolean aValue)
	{
		mSelected = aValue;
		if (mValueComponent != null)
		{
			mValueComponent.setSelected(aValue);
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		mSelected = mValueComponent.isSelected();
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putBoolean(mLabel, mSelected);
	}


	@Override
	public String toString()
	{
		return Boolean.toString(((JCheckBox)mValueComponent).isSelected());
	}
}
