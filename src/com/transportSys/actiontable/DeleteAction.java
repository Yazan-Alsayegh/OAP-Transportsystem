package com.transportSys.actiontable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import com.transportSys.connection.dbConnection;

/**
 * Represents a JButton action specifically for deleting items from a JTable.
 * Provides functionality to delete records associated with a specific table and
 * primary key.
 * 
 * @author 7107, 7072, 7074
 * 
 */
public class DeleteAction extends JButton {
	/**
	 * The serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The JTable associated with this DeleteAction.
	 */
	private JTable table;

	/**
	 * The name of the database table for deletion.
	 */
	private String tableName;

	/**
	 * The name of the primary key column in the database table.
	 */
	private String primaryKeyColumnName;

	/**
	 * Instance of ForeignKeyChecker
	 */
	private ForeignKeyChecker foreignKeyChecker;

	/**
	 * Constructs a DeleteAction instance.
	 *
	 * @param text                 The text displayed on the button.
	 * @param table                The JTable associated with this action.
	 * @param primaryKeyColumnName The name of the primary key column.
	 * @param tableName            The name of the table for deletion.
	 */
	public DeleteAction(String text, JTable table, String primaryKeyColumnName, String tableName) {
		super(text);
		this.table = table;
		this.primaryKeyColumnName = primaryKeyColumnName;
		this.tableName = tableName;
		this.foreignKeyChecker = new ForeignKeyChecker(); // Instantiate ForeignKeyChecker
		addActionListener(new DeleteTableActionListener());
	}

	private class DeleteTableActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				int confirmDialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

				if (confirmDialogResult == JOptionPane.YES_OPTION) {
					int modelRowIndex = table.convertRowIndexToModel(selectedRow);
					Object primaryKeyValue = table.getValueAt(selectedRow,
							table.getColumn(primaryKeyColumnName).getModelIndex());
					// Check if there are foreign key references before deletion
					boolean hasReferences = foreignKeyChecker.hasForeignKeyReferences(tableName, primaryKeyColumnName,
							primaryKeyValue);

					if (hasReferences) {
						String referencingTable = foreignKeyChecker.getReferencingTable(tableName, primaryKeyColumnName,
								primaryKeyValue);
						if (referencingTable != null) {
							JOptionPane.showMessageDialog(null,
									"Cannot delete. Data is referenced in table: " + referencingTable);
						} else {
							JOptionPane.showMessageDialog(null,
									"Cannot delete. Data is referenced in another table, but the referencing table is unknown.");
						}
					} else {
						deleteData(tableName, primaryKeyColumnName, primaryKeyValue, modelRowIndex);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please select a row to delete.");
			}
		}
	}

	private void deleteData(String tableName, String primaryKeyColumnName, Object primaryKeyValue, int modelRowIndex) {
		Connection myConn = null;
		PreparedStatement pst = null;

		try {
			myConn = dbConnection.getConnection();

			Statement disableForeignKeyChecks = myConn.createStatement();
			disableForeignKeyChecks.execute("SET FOREIGN_KEY_CHECKS=0");
			disableForeignKeyChecks.close();

			String deleteQuery = "DELETE FROM " + tableName + " WHERE " + primaryKeyColumnName + " = ?";
			pst = myConn.prepareStatement(deleteQuery);
			pst.setObject(1, primaryKeyValue);

			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				myConn.setAutoCommit(false);
				myConn.commit();
				myConn.setAutoCommit(true);
				((javax.swing.table.DefaultTableModel) table.getModel()).removeRow(modelRowIndex);
				JOptionPane.showMessageDialog(null, "Record deleted successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "No records deleted. Please check your inputs.");
			}

			Statement enableForeignKeyChecks = myConn.createStatement();
			enableForeignKeyChecks.execute("SET FOREIGN_KEY_CHECKS=1");
			enableForeignKeyChecks.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error deleting data: " + ex.getMessage());
		} finally {
			dbConnection.closeConnection(myConn);
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
