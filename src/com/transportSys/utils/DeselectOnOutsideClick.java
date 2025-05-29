package com.transportSys.utils;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Provides functionality to deselect table selection when clicking outside the
 * table or its related components.
 * 
 * Tracks the main frame, table, and scroll pane to handle deselection on an
 * outside click event.
 * 
 * @author 7107, 7090
 */

public class DeselectOnOutsideClick {

	private final JFrame frame;
	private final JTable table;
	private final JScrollPane scrollPane;

	/**
	 * Constructs an instance of DeselectOnOutsideClick with the specified
	 * components.
	 *
	 * @param frame      The main frame associated with the action.
	 * @param table      The JTable where deselection occurs.
	 * @param scrollPane The scroll pane containing the table.
	 */
	public DeselectOnOutsideClick(JFrame frame, JTable table, JScrollPane scrollPane) {
		this.frame = frame;
		this.table = table;
		this.scrollPane = scrollPane;
	}

	/**
	 * Method to set up deselecting selection on outside click
	 */
	public void setupDeselectOnOutsideClick() {
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Retrieve the deepest component at the clicked position
				Component clickedComponent = SwingUtilities.getDeepestComponentAt(frame, e.getX(), e.getY());

				// Check if the clicked component is not related to the table or buttons
				if (!isTableOrRelatedComponent(clickedComponent) && !isButtonOrRelatedComponent(clickedComponent)) {
					table.clearSelection(); // Clear selection from the table
					frame.requestFocusInWindow(); // Request focus for the main frame
				}
			}
		});
	}

	// Checks if the component is the table or any related component
	private boolean isTableOrRelatedComponent(Component component) {
		return component == table || isDescendant(component, JTable.class);
	}

	// Checks if the component is a button or any related component
	private boolean isButtonOrRelatedComponent(Component component) {
		return component instanceof JButton || isDescendant(component, JButton.class);
	}

	// Checks if the component is a descendant of a specific class
	private boolean isDescendant(Component component, Class<?> clazz) {
		if (clazz.isInstance(component)) {
			return true;
		}
		if (component == scrollPane) {
			return false;
		}
		if (component.getParent() == null) {
			return false;
		}
		return isDescendant(component.getParent(), clazz);
	}
}
