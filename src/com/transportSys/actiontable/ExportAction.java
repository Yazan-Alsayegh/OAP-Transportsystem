package com.transportSys.actiontable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class represents an action to export data from a JTable to an Excel
 * file. Extends JButton to provide an export button functionality.
 * 
 * @author 7090, 7078 
 * 
 */
public class ExportAction extends JButton {

	/**
	 * The serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The JTable used for exporting data to Excel file
	 */
	private JTable table;

	/**
	 * Constructor to create an ExportAction associated with a JTable.
	 *
	 * @param table The JTable containing the data to be exported.
	 */
	public ExportAction(JTable table) {
		super("EXPORT");
		this.table = table;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table != null) {
					try {
						exportTableAsExcel();
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error exporting table!");
					}
				}
			}
		});
	}

	/**
	 * Exports the data from the associated JTable to an Excel file.
	 *
	 * @throws IOException If an I/O error occurs during the export process.
	 */
	private void exportTableAsExcel() throws IOException {
		TableModel model = table.getModel();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Table Data");

		// Create header row in the Excel sheet
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < model.getColumnCount(); i++) {
			headerRow.createCell(i).setCellValue(model.getColumnName(i));
		}

		// Write table data to the Excel sheet
		for (int rowNum = 0; rowNum < model.getRowCount(); rowNum++) {
			Row row = sheet.createRow(rowNum + 1);
			for (int colNum = 0; colNum < model.getColumnCount(); colNum++) {
				Object value = model.getValueAt(rowNum, colNum);
				if (value != null) {
					// Set cell value based on its type
					if (value instanceof String) {
						row.createCell(colNum).setCellValue((String) value);
					} else if (value instanceof Integer) {
						row.createCell(colNum).setCellValue((Integer) value);
					} else if (value instanceof Double) {
						row.createCell(colNum).setCellValue((Double) value);
					} else {
						row.createCell(colNum).setCellValue(value.toString());
					}
				}
			}
		}

		// Choose the file path for saving the Excel file
		JFileChooser fileChooser = new JFileChooser();
		int choice = fileChooser.showSaveDialog(null);

		// Save the Excel file
		if (choice == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx";
			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
				JOptionPane.showMessageDialog(null, "Table exported successfully!");
			}
		}
		workbook.close();
	}

	/**
	 * Get the associated JTable.
	 *
	 * @return The JTable associated with this ExportAction.
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * Set the JTable to be associated with this ExportAction.
	 *
	 * @param table The JTable to be associated with this ExportAction.
	 */
	public void setTable(JTable table) {
		this.table = table;
	}
}
