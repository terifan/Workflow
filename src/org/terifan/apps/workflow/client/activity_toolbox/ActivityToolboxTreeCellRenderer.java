package org.terifan.apps.workflow.client.activity_toolbox;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.terifan.ui.StyleSheet;


class ActivityToolboxTreeCellRenderer extends DefaultTreeCellRenderer
{
	private StyleSheet mStyleSheet;


	public ActivityToolboxTreeCellRenderer(StyleSheet aStyleSheet)
	{
		mStyleSheet = aStyleSheet;

		setBackgroundNonSelectionColor(new Color(240,240,240));
		setFont(mStyleSheet.getFont("default"));
	}


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (c instanceof JLabel)
		{
			StyleSheet style = mStyleSheet.getRoot().getStyleSheet(((ActivityToolboxTreeNode)value).getStyleId());

			((JLabel)c).setIcon(style.getIcon("icon"));
		}

		return c;
	}
}
