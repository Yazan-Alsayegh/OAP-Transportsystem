package com.transportSys.utils;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 
 * The TableColumnResizer class provides functionality to resize columns in a
 * JTable.
 * 
 * @author 7090, 7072
 * 
 */
public class TableColumnResizer {

	/**
	 * Constructs a TableColumnResizer.
	 */
	public TableColumnResizer() {
		// Default constructor
	}

	/**
	 * Adjusts the column widths in a JTable based on the content within each
	 * column.
	 *
	 * @param table The JTable whose columns need to be resized.
	 */
	public static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();

		// Loop through each column
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 50; // Default width

			// Check each row's renderer for the widest element in the column
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}

			width = Math.min(width, 100); // Limit the maximum width
			TableColumn tableColumn = columnModel.getColumn(column);
			tableColumn.setPreferredWidth(width);
			tableColumn.setMinWidth(50); // Set a minimum width for flexibility
		}
	}
}
