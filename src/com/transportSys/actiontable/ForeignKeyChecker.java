package com.transportSys.actiontable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.transportSys.connection.dbConnection;

/**
 * Utility class to check for foreign key references in the database tables.
 * Helps in verifying if a specific primary key value is referenced in other
 * tables.
 * 
 * @author 7090, 7072, 7078 
 * 
 */
public class ForeignKeyChecker {

	/**
	 * Default constructor for ForeignKeyChecker class. Constructs a
	 * ForeignKeyChecker instance.
	 */
	public ForeignKeyChecker() {
		// Default constructor
	}

	/**
	 * Checks if the provided primary key value is referenced in other tables.
	 *
	 * @param tableName            The name of the table containing the foreign key
	 *                             references.
	 * @param primaryKeyColumnName The column name of the primary key in the
	 *                             referenced table.
	 * @param primaryKeyValue      The value of the primary key to check for
	 *                             references.
	 * @return True if the primary key value has references in other tables, false
	 *         otherwise.
	 */
	public boolean hasForeignKeyReferences(String tableName, String primaryKeyColumnName, Object primaryKeyValue) {
		Connection myConn = null;
		ResultSet resultSet = null;
		PreparedStatement pst = null;

		try {
			myConn = dbConnection.getConnection();

			// Query to retrieve tables referencing the provided foreign key
			String query = "SELECT TABLE_NAME " + "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
					+ "WHERE REFERENCED_TABLE_NAME = ? " + "AND REFERENCED_COLUMN_NAME = ?";

			pst = myConn.prepareStatement(query);
			pst.setString(1, tableName);
			pst.setString(2, primaryKeyColumnName);

			resultSet = pst.executeQuery();

			while (resultSet.next()) {
				String referencingTable = resultSet.getString("TABLE_NAME");
				// Skip self-references to prevent blocking deletions of self-referencing rows
				if (!referencingTable.equalsIgnoreCase(tableName)) {
					// Check if the referencing table contains the foreign key value
					if (checkForeignKeyValueExists(referencingTable, primaryKeyColumnName, primaryKeyValue)) {
						return true; // Reference found, can't delete
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// Handle exceptions
		} finally {
			// Close resources and connections
			dbConnection.closeConnection(myConn);
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		// No references found, or only self-references found, can proceed with deletion
		return false;
	}

	/**
	 * Checks if the foreign key value exists in the referencing table.
	 *
	 * @param referencingTable     The table name referencing the primary key.
	 * @param primaryKeyColumnName The column name of the primary key.
	 * @param primaryKeyValue      The value of the primary key to check in the
	 *                             referencing table.
	 * @return True if the primary key value exists in the referencing table, false
	 *         otherwise.
	 */
	private boolean checkForeignKeyValueExists(String referencingTable, String primaryKeyColumnName,
	        Object primaryKeyValue) {
	    Connection myConn = null;
	    ResultSet resultSet = null;
	    PreparedStatement pst = null;

	    try {
	        myConn = dbConnection.getConnection();

	        DatabaseMetaData metaData = myConn.getMetaData();

	        // Fetch the columns of the referencing table
	        ResultSet columns = metaData.getColumns(null, null, referencingTable, null);

	        // Check if the primary key column exists in the referencing table
	        boolean primaryKeyExists = false;
	        while (columns.next()) {
	            String columnName = columns.getString("COLUMN_NAME");
	            if (columnName.equalsIgnoreCase(primaryKeyColumnName)) {
	                primaryKeyExists = true;
	                break;
	            }
	        }

	        if (!primaryKeyExists) {
	            // Handle if the primary key column doesn't exist
	            System.out.println("Primary key column doesn't exist in the referencing table.");
	            return false;
	        }

	        // Query to check if the primary key value exists in the referencing table
	        String query = "SELECT * FROM " + referencingTable + " WHERE " + primaryKeyColumnName + " = ?";
	        pst = myConn.prepareStatement(query);
	        pst.setObject(1, primaryKeyValue);

	        resultSet = pst.executeQuery();

	        return resultSet.next(); // If result set has any rows, the value exists
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        // Handle exceptions
	    } finally {
	        // Close resources
	        dbConnection.closeConnection(myConn);
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (pst != null) {
	                pst.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return false;
	}


	/**
	 * Retrieves the referencing table when a foreign key reference is found.
	 *
	 * @param tableName            The name of the table containing the foreign key
	 *                             references.
	 * @param primaryKeyColumnName The column name of the primary key in the
	 *                             referenced table.
	 * @param primaryKeyValue      The value of the primary key to check for
	 *                             references.
	 * @return The referencing table name if the primary key value has references in
	 *         other tables, null otherwise.
	 */
	public String getReferencingTable(String tableName, String primaryKeyColumnName, Object primaryKeyValue) {
		Connection myConn = null;
		ResultSet resultSet = null;
		PreparedStatement pst = null;

		try {
			myConn = dbConnection.getConnection();

			String query = "SELECT TABLE_NAME " + "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
					+ "WHERE REFERENCED_TABLE_NAME = ? AND REFERENCED_COLUMN_NAME = ?";

			pst = myConn.prepareStatement(query);
			pst.setString(1, tableName);
			pst.setString(2, primaryKeyColumnName);

			resultSet = pst.executeQuery();

			while (resultSet.next()) {
				String referencingTable = resultSet.getString("TABLE_NAME");
				boolean hasReferences = checkForeignKeyValueExists(referencingTable, primaryKeyColumnName,
						primaryKeyValue);
				if (hasReferences) {
					return referencingTable; // Return the referencing table name
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// Handle exceptions
		} finally {
			// Close resources and connections
			dbConnection.closeConnection(myConn);
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return null; // No references found
	}
}
