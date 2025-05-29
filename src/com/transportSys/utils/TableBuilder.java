package com.transportSys.utils;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Builds a DefaultTableModel from a ResultSet by extracting metadata for column
 * names and iterating through the result set to create a vector for the table
 * data. Provides a method to infer the column class based on the first non-null
 * value in the column. Date columns are formatted as "yyyy-MM-dd".
 *
 * @author 7107, 7090, 7074
 */
public class TableBuilder {

    /**
     * Constructs a TableBuilder.
     */
    public TableBuilder() {
        // Default constructor
    }

    /**
     * Builds a DefaultTableModel from a ResultSet.
     *
     * @param rs The ResultSet to be converted into a DefaultTableModel.
     * @return DefaultTableModel representing the data in the ResultSet.
     * @throws SQLException If a database access error occurs or this method is
     *                      called on a closed result set.
     */
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                Object value = rs.getObject(columnIndex);
                if (value instanceof java.sql.Date) {
                    // Format the date as yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = sdf.format(value);
                    vector.add(formattedDate);
                } else {
                    vector.add(value);
                }
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames) {
            /**
             * Unique serial version ID for this DefaultTableModel to support serialization.
             * Ensure to update this value if significant changes are made to the class to maintain compatibility.
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                for (Vector<Object> row : data) {
                    Object value = row.get(columnIndex);
                    if (value != null) {
                        return value.getClass();
                    }
                }
                return Object.class;
            }
        };
    }
}
