package com.transportSys.utils;

import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.ToolTipManager;

/**
 * Manages tooltips for columns and cells within a JTable's header.
 * Associates tooltips with specific columns or cells based on their name or index when the mouse is over the header.
 * Handles flex-wrap of text within tooltips that exceed a certain width.
 * 
 * @author 7090, 7078 
 */
public class TableHeaderTooltipManager {

    /**
     * Default constructor
     */
    public TableHeaderTooltipManager() {
        // Default constructor - no additional functionality
    }

    /**
     * Sets up tooltips for table header columns and cells.
     * Associates tooltips with specific columns or cells based on their name or index when the mouse is over the header.
     * Handles flex-wrap of text within tooltips that exceed a certain width.
     * 
     * @param table       The JTable for which tooltips need to be set up in the header.
     * @param maxWidth    The maximum width for the tooltip before text starts to wrap.
     */
    public static void setupTableHeaderTooltips(JTable table, int maxWidth) {
        JTableHeader header = table.getTableHeader(); // Gets the table header
        
        // Set tooltip initial delay and dismiss delay
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                TableColumnModel columnModel = table.getColumnModel();
                int col = columnModel.getColumnIndexAtX(e.getX()); // Retrieves the column index under the mouse pointer

                if (col != -1) {
                    String columnName = table.getColumnName(col);
                    String wrappedText = wrapTooltipText(columnName, maxWidth);
                    header.setToolTipText(wrappedText); // Sets the tooltip for the column header
                    table.setToolTipText(null); // Clears the cell tooltip when hovering over the header
                } else {
                    header.setToolTipText(null); // Clears the tooltip if not over a column
                }
            }
        });

        // Tooltips for individual cells in a column
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint()); // Retrieves the column index under the mouse pointer
                int row = table.rowAtPoint(e.getPoint()); // Retrieves the row index under the mouse pointer

                if (col != -1 && row != -1) {
                    Object cellValue = table.getValueAt(row, col);
                    String cellText = cellValue != null ? cellValue.toString() : "";
                    String wrappedText = wrapTooltipText(cellText, maxWidth);
                    table.setToolTipText(wrappedText); // Sets the tooltip for the cell
                    header.setToolTipText(null); // Clears the header tooltip when hovering over cells
                } else {
                    table.setToolTipText(null); // Clears the tooltip if not over a cell
                }
            }
        });
    }

    /**
     * Wrap tooltip text if it exceeds a specified maximum width.
     * 
     * @param text      The text to be wrapped.
     * @param maxWidth  The maximum width for the tooltip before text starts to wrap.
     * @return HTML-wrapped tooltip text.
     */
    private static String wrapTooltipText(String text, int maxWidth) {
        FontMetrics metrics = new JTable().getFontMetrics(new JTable().getFont());
        StringBuilder wrappedText = new StringBuilder("<html>");
        StringBuilder line = new StringBuilder();

        for (String word : text.split(" ")) {
            if (metrics.stringWidth(line.toString() + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                wrappedText.append(line).append("<br>");
                line = new StringBuilder(word).append(" ");
            }
        }
        wrappedText.append(line);
        wrappedText.append("</html>");
        return wrappedText.toString();
    }
}
