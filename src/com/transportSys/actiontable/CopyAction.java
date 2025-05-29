package com.transportSys.actiontable;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * Presents actions related to copying data in a JTable.
 * It includes methods to create a context menu for copying row/cell data.
 * 
 * @author 7072, 7090
 * 
 */
public class CopyAction {

    // Method to create a context menu for copying row/cell data

    /**
     * Default constructor with a comment
     */
    public CopyAction() {
        // This is a default constructor
    }
    /**
     * Creates a context menu for copying row/cell data in a JTable.
     *
     * @param table The JTable containing the data.
     * @return A JPopupMenu containing options to copy row/cell data.
     */
    public static JPopupMenu createCopyMenu(JTable table) {
        JPopupMenu popupMenu = new JPopupMenu(); // Creates a new popup menu
        
        // Menu item to copy an entire row
        JMenuItem copyRowItem = new JMenuItem("Copy Row"); 
        copyRowItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Gets the selected row index
            int columnCount = table.getColumnCount(); // Gets the total number of columns in the table

            if (selectedRow == -1) {
                // Show error message if no row is selected
                JOptionPane.showMessageDialog(null, "Please select a row to copy.", "No Row Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            StringBuilder rowData = new StringBuilder();

            // Iterates through each column of the selected row
            for (int i = 0; i < columnCount; i++) {
                Object value = table.getValueAt(selectedRow, i); // Retrieves cell value
                rowData.append("'").append(value != null ? value.toString() : "").append("'"); // Appends cell value to the row data
                if (i < columnCount - 1) {
                    rowData.append(", "); // Uses a comma as a separator between columns
                }
            }
            
            StringSelection stringSelection = new StringSelection(rowData.toString()); // Creates string selection
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // Gets system clipboard
            clipboard.setContents(stringSelection, null); // Sets the row data to clipboard
        });

        // Create menu item for copying a single cell
        JMenuItem copyCellItem = new JMenuItem("Copy Cell"); 
        copyCellItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Gets the selected row index
            int selectedColumn = table.getSelectedColumn(); // Gets the selected column index

            if (selectedRow == -1) {
                // Show error message if no row is selected
                JOptionPane.showMessageDialog(null, "Please select a cell to copy.", "No Cell Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Object value = table.getValueAt(selectedRow, selectedColumn); // Retrieves cell value
            String cellData = value != null ? value.toString() : ""; // Converts cell value to string

            StringSelection stringSelection = new StringSelection(cellData); // Creates string selection
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // Gets system clipboard
            clipboard.setContents(stringSelection, null); // Sets the cell data to clipboard
        });

        popupMenu.add(copyRowItem); // Adds copy row menu item to the popup menu
        popupMenu.add(copyCellItem); // Adds copy cell menu item to the popup menu

        return popupMenu; // Returns the popup menu
    }
}
