package org.terifan.ui.deprecated_propertygrid;

import java.awt.CardLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.Serializable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.terifan.bundle.Bundle;


public class ComboBoxProperty extends Property<JPanel, Object> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int mSelectedIndex;
	private Object[] mOptions;
	private Object mValue;


	public ComboBoxProperty(String aLabel, Object[] aOptions, int aSelectedIndex)
	{
		super(aLabel);

		mOptions = aOptions;
		mSelectedIndex = aSelectedIndex;
		mValue = mOptions[mSelectedIndex];
	}


	public <T extends Enum> ComboBoxProperty(String aLabel, Class<T> aEnumType, T aSelectedEnum)
	{
		super(aLabel);

		mOptions = aEnumType.getEnumConstants();
		mSelectedIndex = -1;

		int i = 0;
		for (Object o : mOptions)
		{
			if (o.equals(aSelectedEnum))
			{
				mSelectedIndex = i;
			}
			i++;
		}

		mValue = mSelectedIndex == -1 ? null : mOptions[mSelectedIndex];
	}


	@Override
	protected JPanel createValueComponent()
	{
		JPanel panel = new JPanel(new CardLayout());

		final JTextField label = new JTextField(mSelectedIndex == -1 ? "" : "" + mOptions[mSelectedIndex]);
		final JComboBox combobox = new JComboBox(mOptions);

		label.setForeground(mPropertyGrid.mStyleSheet.getColor("text_foreground"));
		label.setBackground(mPropertyGrid.mStyleSheet.getColor("text_background"));
		label.setOpaque(true);
		label.setBorder(null);
		label.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent aEvent)
			{
				((CardLayout)panel.getLayout()).show(panel, "combobox");
				combobox.grabFocus();
			}
		});

		combobox.setSelectedIndex(mSelectedIndex);
		combobox.addActionListener(e -> mValueComponent.repaint());
		combobox.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent aEvent)
			{
				if (combobox == aEvent.getComponent())
				{
					synchronized (this)
					{
						getPropertyGrid().setSelectedProperty(ComboBoxProperty.this);
					}

					getPropertyGrid().repaint();
				}
			}


			@Override
			public void focusLost(FocusEvent aEvent)
			{
				if (combobox == aEvent.getComponent() && aEvent.getOppositeComponent() != panel)
				{
					PropertyGrid propertyGrid = getPropertyGrid();

					synchronized (this)
					{
						if (propertyGrid.getSelectedProperty() == ComboBoxProperty.this)
						{
							propertyGrid.updateValue(ComboBoxProperty.this);
							propertyGrid.setSelectedProperty(null);
						}
					}

					label.setText(combobox.getSelectedItem() == null ? "" : combobox.getSelectedItem().toString());
					((CardLayout)panel.getLayout()).show(panel, "label");

					propertyGrid.repaint();

					panel.setFocusable(true);
				}
			}
		});

		panel.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent aEvent)
			{
				panel.setFocusable(false);

				((CardLayout)panel.getLayout()).show(panel, "combobox");
				combobox.grabFocus();
			}
		});

		panel.add("label", label);
		panel.add("combobox", combobox);

		return panel;
	}


	@Override
	public Object getValue()
	{
		return mValue;
	}


	@Override
	public ComboBoxProperty setValue(Object aValue)
	{
		mValue = aValue;
		if (mValueComponent != null)
		{
			getComboBox().setSelectedItem(aValue);
		}
		return this;
	}


	@Override
	protected void updateValue()
	{
		JComboBox comboBox = getComboBox();
		mValue = comboBox.getSelectedItem();
		mSelectedIndex = comboBox.getSelectedIndex();
	}


	public int getSelectedIndex()
	{
		return mSelectedIndex;
	}


	@Override
	void marshal(Bundle aBundle)
	{
		aBundle.putString(mLabel, mValue == null ? null : mValue.toString());
	}


	@Override
	public String toString()
	{
		Object selectedItem = getComboBox().getSelectedItem();
		return selectedItem == null ? null : selectedItem.toString();
	}


	private JComboBox getComboBox()
	{
		return (JComboBox)mValueComponent.getComponent(1);
	}
}
