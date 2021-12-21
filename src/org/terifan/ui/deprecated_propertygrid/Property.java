package org.terifan.ui.deprecated_propertygrid;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.terifan.bundle.Bundle;


public abstract class Property<T extends JComponent,R> implements Comparable<Property>, Cloneable, Serializable
{
	private static final long serialVersionUID = 1L;

	protected String mLabel;
	protected boolean mEditable;
	protected boolean mGroup;
	protected boolean mHasPopup; // this exists in order for the model to recreate popup functions when being deserisalized
	protected String mKey;
	protected Object mUserObject;

	protected transient Function<Property,R> mPopup;
	protected transient JButton mActionButton;
	protected transient PropertyGrid mPropertyGrid;
	protected transient PropertyGridIndent mIndentComponent;
	protected transient PropertyGridLabel mLabelComponent;
	protected transient T mValueComponent;
	protected transient int mIndent;
	protected transient PropertyChangeListener mChangeListener;


	public Property(String aLabel)
	{
		mLabel = aLabel;
		mEditable = true;
	}


	public String getLabel()
	{
		return mLabel;
	}


	public Property setLabel(String aLabel)
	{
		mLabel = aLabel;
		return this;
	}


	public boolean isEditable()
	{
		return mEditable;
	}


	public Property<T, R> setEditable(boolean aEditable)
	{
		mEditable = aEditable;
		return this;
	}


	public Function<Property, R> getPopup()
	{
		return mPopup;
	}


	public Property<T, R> setPopup(Function<Property, R> aFunction)
	{
		mPopup = aFunction;
		mHasPopup = mPopup != null;
		return this;
	}


	public String getKey()
	{
		return mKey;
	}


	public Property<T, R> setKey(String aKey)
	{
		mKey = aKey;
		return this;
	}


	public PropertyChangeListener getChangeListener()
	{
		return mChangeListener;
	}


	public Property<T, R> setChangeListener(PropertyChangeListener aChangeListener)
	{
		mChangeListener = aChangeListener;
		return this;
	}


	public Object getUserObject()
	{
		return mUserObject;
	}


	public Property<T, R> setUserObject(Object aUserObject)
	{
		mUserObject = aUserObject;
		return this;
	}


	public JButton getActionButton()
	{
		if (mActionButton == null)
		{
			mActionButton = createActionButton();
		}
		return mActionButton;
	}


	public boolean isGroup()
	{
		return mGroup;
	}


	public Property setGroup(boolean aGroup)
	{
		mGroup = aGroup;
		return this;
	}


	protected Integer getIndent()
	{
		return mIndent;
	}


	public PropertyGrid getPropertyGrid()
	{
		return mPropertyGrid;
	}


	protected T getValueComponent()
	{
		return mValueComponent;
	}


	protected JComponent getLabelComponent()
	{
		return mLabelComponent;
	}


	protected JComponent getIndentComponent()
	{
		return mIndentComponent;
	}


	@Override
	public int hashCode()
	{
		return mLabel == null ? 0 : mLabel.hashCode();
	}


	@Override
	public boolean equals(Object aObject)
	{
		if (aObject instanceof Property)
		{
			Property p = (Property)aObject;
			return p.mLabel == null ? false : p.getLabel().equals(mLabel);
		}
		return false;
	}


	@Override
	public int compareTo(Property aProperty)
	{
		return mLabel.compareTo(aProperty.getLabel());
	}


	protected JButton createActionButton()
	{
		if (mPopup == null)
		{
			return null;
		}

		AbstractAction action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				R value = mPopup.apply(Property.this);

				if (value != null)
				{
					setValue(value);
					mPropertyGrid.updateValue(Property.this);
					mPropertyGrid.repaint();
				}
			}
		};

		JButton button = new JButton(action);
		button.setOpaque(false);
		button.setFocusable(false);
		button.setIcon(new ImageIcon(mPropertyGrid.getStyleSheet().getImage("popup_icon")));

		mActionButton = button;

		return button;
	}


	protected void buildItem(PropertyGrid aPropertyGrid, JPanel aPanel, int aIndent)
	{
		mPropertyGrid = aPropertyGrid;
		mIndent = aIndent;

		mIndentComponent = new PropertyGridIndent(this);
		mLabelComponent = new PropertyGridLabel(this);

		mValueComponent = createValueComponent();
		mValueComponent.setFont(mPropertyGrid.getStyleSheet().getFont("item_font"));
		if (!(mValueComponent instanceof JPanel))
		{
			mValueComponent.addFocusListener(new PropertyGridEditorListener(this));
		}

		if (mHasPopup && mPopup == null)
		{
			Function<Property, Function<Property, Object>> factory = mPropertyGrid.getPopupFactory();
			if (factory != null)
			{
				mPopup = (Function<Property,R>)factory.apply(this);
				mHasPopup = mPopup != null;
			}
		}

		aPanel.add(mIndentComponent);
		aPanel.add(mLabelComponent);
		aPanel.add(mValueComponent);

		JButton button = getActionButton();
		if (button != null)
		{
			aPanel.add(button);
		}
	}


	protected Property clone() throws CloneNotSupportedException
	{
		Property clone = (Property)super.clone();
		clone.mActionButton = null;

		return clone;
	}


	abstract void marshal(Bundle aBundle);


	protected abstract T createValueComponent();


	protected abstract void updateValue();


	public abstract R getValue();


	public abstract Property<T,R> setValue(R aValue);
}
