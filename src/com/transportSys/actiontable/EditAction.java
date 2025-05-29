package com.transportSys.actiontable;

import com.transportSys.connection.dbConnection;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a button used for editing actions within a JTable.
 * 
 * @author 7107, 7090
 * 
 */
public class EditAction extends JButton {
    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;
    
	/**
	 * The table where actions are performed
	 */
	private JTable table;

	/**
	 * The name of the table associated with the actions
	 */
	private String tableName;

	/**
	 * The name of the primary key column in the table
	 */
	private String primaryKeyColumnName;

	/**
	 * Constructor for initializing the EditAction with essential parameters.
	 *
	 * @param text                 The text label for the button.
	 * @param table                The JTable where editing occurs.
	 * @param primaryKeyColumnName The name of the primary key column in the table.
	 * @param tableName            The name of the table being edited.
	 * 
	 */
	public EditAction(String text, JTable table, String primaryKeyColumnName, String tableName) {
		super(text);
		this.table = table;
		this.primaryKeyColumnName = primaryKeyColumnName;
		this.tableName = tableName;
		addActionListener(new EditActionListener());
	}

	// Method to restrict input to numeric characters in a text field
	private void restrictInputToNumbers(JTextField textField) {
		// Creates a DocumentFilter to limit input to numeric characters only
		((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				// Validates if the input string contains only numeric characters
				if (string.matches("[0-9]*")) {
					super.insertString(fb, offset, string, attr);
				} else {
					JOptionPane.showMessageDialog(null, "This field accepts only numbers");
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				// Validates if the replacement text contains only numeric characters
				if (text.matches("[0-9]*")) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					JOptionPane.showMessageDialog(null, "This field accepts only numbers");
				}
			}
		});
	}

	// Method to restrict input to decimal characters in a text field
	private void restrictInputToDecimal(JTextField textField) {
		// Creates a DocumentFilter to limit input to numeric and decimal characters
		((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				// Validates if the input string contains only numeric and decimal characters
				if (string.matches("[0-9.]*")) {
					super.insertString(fb, offset, string, attr);
				} else {
					JOptionPane.showMessageDialog(null, "This field accepts only numbers and a decimal point");
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				// Validates if the replacement text contains only numeric and decimal
				// characters
				if (text.matches("[0-9.]*")) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					JOptionPane.showMessageDialog(null, "This field accepts only numbers and a decimal point");
				}
			}
		});
	}

	// ActionListener for editing a record
	private class EditActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Gets the selected row for editing
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				// Creates a map to hold the row data
				Map<String, Object> rowData = new HashMap<>();
				// Iterates through columns to retrieve column names and values of the selected
				// row
				for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
					String columnName = table.getColumnName(columnIndex);
					Object columnValue = table.getValueAt(selectedRow, columnIndex);
					rowData.put(columnName, columnValue);
				}

				// Creates components for user input in a dialog
				Map<String, JComponent> columnTextFields = new HashMap<>();
				JPanel panel = new JPanel(new GridBagLayout());
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.weightx = 1;

				// Iterates through columns to create labels and input fields in the dialog
				for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
					// Retrieves column name and value from the row data
					String columnName = table.getColumnName(columnIndex);
					String columnValue = rowData.get(columnName) != null ? rowData.get(columnName).toString() : "";
					JLabel label = new JLabel(columnName);
					JComponent textField;

					// Sets appropriate input fields based on column content
					if (columnName.contains("Description")) {
						// For description columns, creates a text area for multiline input
						JTextArea textArea = new JTextArea(columnValue, 5, 20);
						textArea.setWrapStyleWord(true);
						textArea.setLineWrap(true);
						textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						JScrollPane scrollPane = new JScrollPane(textArea);
						scrollPane.setPreferredSize(new Dimension(200, 100));
						textField = scrollPane;
					} else {
						// For other columns, creates text fields for single line input
						textField = new JTextField(columnValue);
						textField.setPreferredSize(new Dimension(150, 25));
						// Sets input restrictions for numeric and decimal columns
						if (table.getValueAt(selectedRow, columnIndex) instanceof Integer
								|| table.getValueAt(selectedRow, columnIndex) instanceof Short) {
							restrictInputToNumbers((JTextField) textField);
						} else if (table.getValueAt(selectedRow, columnIndex) instanceof Float
								|| table.getValueAt(selectedRow, columnIndex) instanceof Double
								|| table.getValueAt(selectedRow, columnIndex) instanceof java.math.BigDecimal) {
							restrictInputToDecimal((JTextField) textField);
						}
					}
					// Disables editing of primary key column
					if (columnName.equals(primaryKeyColumnName)) {
						textField.setEnabled(false);
					}

					// Adds components to the panel with appropriate constraints
					columnTextFields.put(columnName, textField);
					constraints.gridy = columnIndex;
					constraints.gridx = 0;
					panel.add(label, constraints);
					constraints.gridx = 1;
					panel.add(textField, constraints);
				}

				// Creates a scrollable pane for the dialog
				JScrollPane mainScrollPane = new JScrollPane(panel);
				mainScrollPane.setPreferredSize(new Dimension(500, 400));

				// Shows the dialog for editing
				while (true) {
					@SuppressWarnings("unused")
					boolean validInput = false;
					int result = JOptionPane.showConfirmDialog(null, mainScrollPane, "Edit",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {
						boolean isEmptyFieldDetected = false;
						// Validates user input for empty fields
						for (Entry<String, JComponent> entry : columnTextFields.entrySet()) {
							// Retrieves column name and associated input component
							String columnName = entry.getKey();
							JComponent component = entry.getValue();
							String value;
							if (component instanceof JScrollPane) {
								JTextArea textArea = (JTextArea) ((JScrollPane) component).getViewport().getView();
								value = textArea.getText();
							} else {
								value = ((JTextField) component).getText();
							}

							// Checks for empty values in primary key, foreign key, and numerical columns
							if (value.isEmpty() && (
		                            columnName.equals(primaryKeyColumnName) || 
		                            table.getValueAt(selectedRow, table.getColumnModel().getColumnIndex(columnName)) instanceof Number ||
		                            (columnName.equals("reportsTo") || columnName.equals("salesRepEmployeeNumber"))
		                        )
		                    ) {
		                        isEmptyFieldDetected = true;
		                        JOptionPane.showMessageDialog(null, columnName + " cannot be left empty.");
		                        break;
		                    }
		                    // Updates the row data with user input
		                    rowData.put(columnName, value);
		                }

						if (!isEmptyFieldDetected) {
							validInput = true;
							Object primaryKeyValue = rowData.get(primaryKeyColumnName);
							boolean updateSuccessful = updateData(tableName, primaryKeyColumnName, primaryKeyValue,
									rowData);
							// Updates the table if the data update is successful
							if (updateSuccessful) {
								for (int i = 0; i < table.getColumnCount(); i++) {
									String columnName = table.getColumnName(i);
									Object value = rowData.get(columnName);
									table.setValueAt(value, selectedRow, i);
								}
								break;
							}
						}
					} else {
						break;
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please select a row to edit.");
			}
		}
	}

	// Method to update data in the database table
	private boolean updateData(String tableName, String primaryKeyColumnName, Object primaryKeyValue,
			Map<String, Object> columnData) {
		// Implementation updates data in the database
		Connection myConn = null;
		PreparedStatement pst = null;

		try {
			myConn = dbConnection.getConnection();
			StringBuilder queryBuilder = new StringBuilder("UPDATE ");
			queryBuilder.append(tableName);
			queryBuilder.append(" SET ");

			for (String columnName : columnData.keySet()) {
				queryBuilder.append(columnName).append("=?, ");
			}
			queryBuilder.setLength(queryBuilder.length() - 2);
			queryBuilder.append(" WHERE ").append(primaryKeyColumnName).append("=?");

			pst = myConn.prepareStatement(queryBuilder.toString());

			int parameterIndex = 1;
			for (Object columnValue : columnData.values()) {
				if (columnValue == null) {
					pst.setNull(parameterIndex++, java.sql.Types.NULL);
				} else {
					pst.setObject(parameterIndex++, columnValue);
				}
			}

			pst.setObject(parameterIndex, primaryKeyValue);

			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				myConn.setAutoCommit(false);
				myConn.commit();
				myConn.setAutoCommit(true);
				JOptionPane.showMessageDialog(null, "Data edited successfully!");
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "No data updated. Please check your inputs.");
				return false;
			}
		} catch (SQLException e) {
			Pattern pattern = Pattern.compile(
					"foreign key constraint fails \\(`[^`]+`.`[^`]+`, CONSTRAINT `[^`]+` FOREIGN KEY \\(`([^`]+)`\\)");
			Matcher matcher = pattern.matcher(e.getMessage());
			if (matcher.find()) {
				String foreignKeyColumn = matcher.group(1);
				JOptionPane.showMessageDialog(null, foreignKeyColumn + " doesn't exist. Check for typos.");
			} else {
				JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage());
			}
			return false;
		} finally {
			dbConnection.closeConnection(myConn);
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}