package org.terifan.ui.deprecated_propertygrid;

import java.io.Serializable;
import javax.swing.JTextField;
import org.terifan.bundle.Bundle;


public class NumberProperty extends Property<JTextField, Number> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Number mValue;


	public NumberProperty(String aLabel, Number aValue)
	{
		super(aLabel);

		mValue = aValue;
	}


	@Override
	protected JTextField createValueComponent()
	{
		JTextField c = new JTextField(mValue.toString());
		c.setCaretColor(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		c.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
		c.addActionListener(e -> mValueComponent.repaint());
		c.setBorder(null);

		return c;
	}


	@Override
	public Number getValue()
	{
		return mValue;
	}


	@Override
	public NumberProperty setValue(Number aValue)
	{
		mValue = aValue;
		if (mValueComponent != null)
		{
			mValueComponent.setText(mValue.toString());
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		if (mValue instanceof Integer)
		{
			mValue = Integer.parseInt(mValueComponent.getText());
		}
		else if (mValue instanceof Long)
		{
			mValue = Long.parseLong(mValueComponent.getText());
		}
		else
		{
			mValue = Double.parseDouble(mValueComponent.getText());
		}
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putNumber(mLabel, mValue);
	}


	@Override
	public String toString()
	{
		return ((JTextField)mValueComponent).getText();
	}
}
