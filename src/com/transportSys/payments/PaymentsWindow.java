package com.transportSys.payments;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.transportSys.connection.dbConnection;
import com.transportSys.utils.*;
import com.transportSys.actiontable.*;

/**
 * Class representing the respective table window in the application.
 * 
 * @author 7090
 * 
 */

public class PaymentsWindow {

	/**
	 * Represents the main frame of the window.
	 */

	public JFrame frame;

	// The table displaying table information
	private JTable table;

	// Connection object for database interaction
	private Connection conn = null;

	// Scroll pane to handle scrolling within the table
	private JScrollPane scrollPane;

	// Handler for deselecting selections on an outside click
	private DeselectOnOutsideClick deselectHandler;

	/**
	 * Main method to start the application.
	 * 
	 * @param args Command line arguments.
	 */

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			com.formdev.flatlaf.FlatLightLaf.install();
		} catch (Exception ex) {
			System.err.println("Failed to initialize FlatLaf");
		}

		EventQueue.invokeLater(() -> {
			try {
				PaymentsWindow window = new PaymentsWindow();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Constructor for the table window class. Initializes the window and sets up
	 * various components.
	 */

	public PaymentsWindow() {
		initialize();
		connectToDatabase();
		loadTable();
		TableHeaderTooltipManager.setupTableHeaderTooltips(table, 200); // Import Tooltip for header
		TableColumnResizer.resizeColumnWidth(table); // Import Column Resizer
		deselectHandler = new DeselectOnOutsideClick(frame, table, scrollPane); // Initializes deselect handler
		deselectHandler.setupDeselectOnOutsideClick(); // Sets up deselect behavior
		new TableBuilder();
	}

	/**
	 * Inner class acting as a custom cell renderer for word-wrapping in table
	 * cells.
	 */

	public class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for WordWrapCellRenderer class. Enables word wrap, line wrap,
		 * sets the renderer as opaque, and applies padding.
		 */
		public WordWrapCellRenderer() {
			setWrapStyleWord(true); // Enable word wrap at word boundaries
			setLineWrap(true); // Enable line wrap within the cell
			setOpaque(true); // Set the renderer to be opaque
			setBorder(new EmptyBorder(5, 5, 5, 5)); // Apply padding to the renderer
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(5, 5, 5, 5));
			}

			setText((value == null) ? "" : value.toString());
			adjustRowHeight(table, row);
			return this;
		}

		/**
		 * Calculate the preferred height of a row to accommodate all cell content.
		 */

		private void adjustRowHeight(JTable table, int row) {
			int cHeight = table.getRowHeight(row);
			int prefHeight = getPreferredSize().height;
			if (cHeight != prefHeight) {
				table.setRowHeight(row, prefHeight);
			}
		}
	}

	/**
	 * Initializes and sets up the components in the window.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Payments"); // Set the title of the GUI window
		frame.setBounds(100, 100, 1210, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);

		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable() {
			/**
			 * The serial version UID for serialization.
			 */
			private static final long serialVersionUID = 1L;
		};
		
		table.setForeground(Color.LIGHT_GRAY);
		table.setBackground(Color.LIGHT_GRAY);
		table.setFont(new Font("Verdana", Font.PLAIN, 11));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.BLACK);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 12));
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setAutoCreateRowSorter(true);

		// Enable table sorting
		table.setAutoCreateRowSorter(true);

		// Search Field
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		panel.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setBorder(new EmptyBorder(40, 100, 10, 0));

		// ThemeChanger toggle button
		ThemeChanger themeButton = new ThemeChanger(table, frame);
		themeButton.setPreferredSize(new Dimension(60, 30));
		themeButton.setMinimumSize(new Dimension(60, 30));
		themeButton.setMaximumSize(new Dimension(60, 30));
		searchPanel.add(themeButton);

		// Customer Number search field
		JPanel customerNumberPanel = new JPanel(new BorderLayout());
		JLabel lblCustomerNumber = new JLabel("Customer Number");
		lblCustomerNumber.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 14));
		customerNumberPanel.add(lblCustomerNumber, BorderLayout.NORTH);

		JTextArea textAreaSearchCustomerNumber = new JTextArea();
		textAreaSearchCustomerNumber.setBorder(new LineBorder(Color.BLACK, 2));
		customerNumberPanel.add(textAreaSearchCustomerNumber, BorderLayout.CENTER);
		searchPanel.add(customerNumberPanel);

		// Payment Date search field
		JPanel paymentDatePanel = new JPanel(new BorderLayout());
		JLabel lblPaymentDate = new JLabel("Payment Date");
		lblPaymentDate.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 14));
		paymentDatePanel.add(lblPaymentDate, BorderLayout.NORTH);

		JTextArea textAreaSearchPaymentDate = new JTextArea();
		textAreaSearchPaymentDate.setBorder(new LineBorder(Color.BLACK, 2));
		paymentDatePanel.add(textAreaSearchPaymentDate, BorderLayout.CENTER);
		searchPanel.add(paymentDatePanel);

		KeyAdapter searchAdapter = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchTable(textAreaSearchCustomerNumber.getText(), textAreaSearchPaymentDate.getText());
			}
		};

		textAreaSearchCustomerNumber.addKeyListener(searchAdapter);
		textAreaSearchPaymentDate.addKeyListener(searchAdapter);

		new JPopupMenu();
		table.setComponentPopupMenu(CopyAction.createCopyMenu(table));

		// Panel for buttons
		JPanel panelButtons = new JPanel();
		panel.add(panelButtons, BorderLayout.EAST);
		panelButtons.setBorder(new EmptyBorder(30, 30, 30, 30));
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));

		new Dimension(100, 30);

		// Define a uniform size for all buttons
		Dimension buttonSize = new Dimension(100, 30);

		// Export Button

		ExportAction exportButton = new ExportAction(table);
		exportButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		exportButton.setText("EXPORT");
		exportButton.setMaximumSize(buttonSize);
		panelButtons.add(exportButton);
	}

	/**
	 * Connects to the database.
	 */

	private void connectToDatabase() {
		try {
			conn = dbConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to the database");
		}
	}

	/**
	 * Loads data into the table from the database.
	 */

	private void loadTable() {
		try {
			String query = "SELECT * FROM payments ORDER BY customerNumber DESC, checkNumber DESC";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(TableBuilder.buildTableModel(rs)); // Use the tableBuilder instance to call the
																// buildTableModel method
			table.setShowGrid(true);
			table.setGridColor(Color.LIGHT_GRAY);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 0));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Searches the payments table using specified criteria for filtering.
	 * 
	 * @param customerNumberSearchText Text to search within the customer number
	 *                                 field.
	 * @param paymentDateSearchText    Text to search within the payment date field.
	 */

	private void searchTable(String customerNumberSearchText, String paymentDateSearchText) {
		try {
			String query = "SELECT * FROM payments " + "WHERE (customerNumber LIKE ?) "
					+ "AND (paymentDate LIKE ? OR ? IS NULL) ";
			PreparedStatement pst = conn.prepareStatement(query);

			pst.setString(1, "%" + customerNumberSearchText + "%");
			pst.setString(2, "%" + paymentDateSearchText + "%");
			pst.setString(3, paymentDateSearchText.isEmpty() ? null : paymentDateSearchText);
			ResultSet rs = pst.executeQuery();
			table.setModel(TableBuilder.buildTableModel(rs)); // Use the tableBuilder instance to call the
																// buildTableModel method
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
