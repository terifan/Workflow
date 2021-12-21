package org.terifan.ui.deprecated_propertygrid;

import java.awt.BorderLayout;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.function.Function;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class PropertyGrid extends JPanel
{
	protected ArrayList<PropertyChangeListener> mListeners;
	protected PropertyGridModel mModel;
	protected int mDividerPosition;
	protected JScrollPane mScrollPane;
	protected PropertyGridListPane mPanel;
	protected Property mSelectedProperty;
	protected StyleSheet mStyleSheet;
	protected Function<Property,Function<Property,Object>> mPopupFactory;


	public PropertyGrid(PropertyGridModel aPropertyGridModel)
	{
		this(aPropertyGridModel, null, null);
	}


	public PropertyGrid(PropertyGridModel aPropertyGridModel, Function<Property, Function<Property, Object>> aPopupFactory)
	{
		this(aPropertyGridModel, aPopupFactory, null);
	}


	public PropertyGrid(PropertyGridModel aModel, StyleSheet aStyleSheet)
	{
		this(aModel, null, aStyleSheet);
	}


	public PropertyGrid(PropertyGridModel aModel, Function<Property, Function<Property, Object>> aPopupFactory, StyleSheet aStyleSheet)
	{
		super(new BorderLayout());

		mListeners = new ArrayList<>();
		mPopupFactory = aPopupFactory;

		if (aStyleSheet == null)
		{
			aStyleSheet = new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet.json"));
		}

		setStyleSheet(aStyleSheet);

		mPanel = new PropertyGridListPane(this);
		mScrollPane = new JScrollPane(mPanel);
		mScrollPane.setBorder(null);

		add(mScrollPane, BorderLayout.CENTER);

		setOpaque(true);
		setModel(aModel);
	}


	public StyleSheet getStyleSheet()
	{
		return mStyleSheet;
	}


	public void setStyleSheet(StyleSheet aStyleSheet)
	{
		mStyleSheet = aStyleSheet;

		mDividerPosition = mStyleSheet.getInt("divider_position");
	}


	public Function<Property, Function<Property, Object>> getPopupFactory()
	{
		return mPopupFactory;
	}


	public void setDividerPosition(int aDividerPosition)
	{
		mDividerPosition = aDividerPosition;
	}


	public int getDividerPosition()
	{
		return mDividerPosition;
	}


	public void addChangeListener(PropertyChangeListener aChangeListener)
	{
		mListeners.add(aChangeListener);
	}


	public void removeChangeListener(PropertyChangeListener aChangeListener)
	{
		mListeners.remove(aChangeListener);
	}


	public PropertyGridModel getModel()
	{
		return mModel;
	}


	/**
	 * Sets the model without changing the popup factory property of this PropertyGrid.
	 */
	public void setModel(PropertyGridModel aPropertyGridModel)
	{
		mModel = aPropertyGridModel;

		mPanel.removeAll();

		buildComponentTree(mModel.getChildren(), 0);

		validate();
	}


	public void setModel(PropertyGridModel aPropertyGridModel, Function<Property, Function<Property, Object>> aPopupFactory)
	{
		mPopupFactory = aPopupFactory;
		setModel(aPropertyGridModel);
	}


	protected void buildComponentTree(ArrayList<Property> aList, int aIndent)
	{
		for (Property item : aList)
		{
			item.buildItem(this, mPanel, aIndent);

			if (item instanceof PropertyList)
			{
				buildComponentTree(((PropertyList)item).getChildren(), aIndent + 1);
			}
		}
	}


	protected Property getSelectedProperty()
	{
		return mSelectedProperty;
	}


	protected void setSelectedProperty(Property aProperty)
	{
//		if (mSelectedProperty != null && mSelectedProperty != aProperty)
//		{
//			if (mListeners.size() > 0)
//			{
//				PropertyChangeEvent event = new PropertyChangeEvent(this, mSelectedProperty);
//				for (PropertyChangeListener o : mListeners)
//				{
//					o.stateChanged(event);
//				}
//			}
//		}

		mSelectedProperty = aProperty;

		if (mSelectedProperty != null)
		{
			mSelectedProperty.getValueComponent().requestFocus();
		}
	}


	protected void redisplay()
	{
		mPanel.invalidate();
		mPanel.validate();
		mScrollPane.invalidate();
		mScrollPane.validate();
		invalidate();
		validate();
		repaint();
	}


	public int getRowHeight()
	{
		return mStyleSheet.getInt("row_padding") + (int)mStyleSheet.getFont("item_font").getLineMetrics("Aj]", new FontRenderContext(null, true, false)).getHeight();
	}


	void updateValue(Property aProperty)
	{
		aProperty.updateValue();

		if (mListeners.size() > 0)
		{
			PropertyChangeEvent event = new PropertyChangeEvent(this, aProperty);

			for (PropertyChangeListener o : mListeners)
			{
				o.stateChanged(event);
			}
		}
	}
}