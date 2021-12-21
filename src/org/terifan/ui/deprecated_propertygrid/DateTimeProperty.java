package org.terifan.ui.deprecated_propertygrid;

import java.io.Serializable;
import javax.swing.JTextField;
import org.terifan.bundle.Bundle;
import org.terifan.util.Calendar;


public class DateTimeProperty extends Property<JTextField,String> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Calendar mValue;


	public DateTimeProperty(String aLabel, Calendar aValue)
	{
		super(aLabel);

		mValue = aValue;
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(mValue == null ? "" : mValue.toString());
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
		return mValue == null ? "" : mValue.toString();
	}


	@Override
	public DateTimeProperty setValue(String aValue)
	{
		mValue = Calendar.parse(aValue);
		if (mValueComponent != null)
		{
			mValueComponent.setText(mValue.toString());
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		mValue = Calendar.parse(mValueComponent.getText());
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putString(mLabel, mValue == null ? null : mValue.toString());
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
