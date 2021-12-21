package org.terifan.ui.deprecated_propertygrid;

import java.io.Serializable;
import javax.swing.JTextField;
import org.terifan.bundle.Bundle;


public class TextProperty extends Property<JTextField,String> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected String mValue;


	public TextProperty(String aLabel, String aValue)
	{
		super(aLabel);

		mValue = aValue;
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(mValue);
		c.setBorder(null);
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));

		return c;
	}


	@Override
	public String getValue()
	{
		return mValue;
	}


	@Override
	public TextProperty setValue(String aValue)
	{
		mValue = aValue;
		if (mValueComponent != null)
		{
			mValueComponent.setText(aValue);
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		mValue = mValueComponent.getText();
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putString(mLabel, mValue);
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
