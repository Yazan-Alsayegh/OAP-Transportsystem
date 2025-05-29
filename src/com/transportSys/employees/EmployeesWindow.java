package com.transportSys.employees;

import com.transportSys.connection.dbConnection;
import com.transportSys.utils.*;
import com.transportSys.actiontable.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Font;

/**
 * 
 * Class representing the respective table window in the application.
 * 
 * @author 7072
 * 
 */
public class EmployeesWindow {

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
		// FlatLaf initialization code
		try {
			// Set FlatIntelliJLaf as the default Look and Feel
			com.formdev.flatlaf.FlatIntelliJLaf.install();
		} catch (Exception ex) {
			System.err.println("Failed to initialize FlatLaf");
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeesWindow window = new EmployeesWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor for the table window class. Initializes the window and sets up
	 * various components.
	 */

	public EmployeesWindow() {
		initialize();
		connectToDatabase();
		loadEmployees();
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
			// The default height is a constant set to 16.
			int cHeight = table.getRowHeight(row);
			// The new height is based on the preferred height of the JTextArea we just used
			// to render the cell.
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
		frame.setTitle("Employees"); // Set the title of the GUI window
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

		table.setFont(new Font("Verdana", Font.PLAIN, 11));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.BLACK);
		header.setForeground(Color.WHITE); // Set the text color to white for better visibility against the black
											// background
		header.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 12));
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// Enable table sorting
		table.setAutoCreateRowSorter(true);

		// ------------- [ Search Field ] ------------- //

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0)); // Added 20px horizontal gap
		panel.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setBorder(new EmptyBorder(40, 100, 10, 0));

		// Themechanger toggle button
		ThemeChanger themeButton = new ThemeChanger(table, frame);
		themeButton.setPreferredSize(new Dimension(60, 30));
		themeButton.setMinimumSize(new Dimension(60, 30));
		themeButton.setMaximumSize(new Dimension(60, 30));
		searchPanel.add(themeButton);

		// Last Name search field
		JPanel lastNamePanel = new JPanel(new BorderLayout());
		JLabel lbllastName = new JLabel("Last Name");
		lbllastName.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 14));
		lastNamePanel.add(lbllastName, BorderLayout.NORTH);

		JTextArea textAreaSearchlastName = new JTextArea();
		textAreaSearchlastName.setBorder(new LineBorder(Color.BLACK, 2));
		lastNamePanel.add(textAreaSearchlastName, BorderLayout.CENTER);
		searchPanel.add(lastNamePanel);

		// First Name search field
		JPanel firstNamePanel = new JPanel(new BorderLayout());
		JLabel lblfirstName = new JLabel("First Name");
		lblfirstName.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 14));
		firstNamePanel.add(lblfirstName, BorderLayout.NORTH);

		JTextArea textAreaSearchfirstName = new JTextArea();
		textAreaSearchfirstName.setBorder(new LineBorder(Color.BLACK, 2));
		firstNamePanel.add(textAreaSearchfirstName, BorderLayout.CENTER);
		searchPanel.add(firstNamePanel);

		// Job Title search field
		JPanel jobTitlePanel = new JPanel(new BorderLayout());
		JLabel lbljobTitle = new JLabel("Job Title");
		lbljobTitle.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 14));
		jobTitlePanel.add(lbljobTitle, BorderLayout.NORTH);

		JTextArea textAreaSearchjobTitle = new JTextArea();
		textAreaSearchjobTitle.setBorder(new LineBorder(Color.BLACK, 2));
		jobTitlePanel.add(textAreaSearchjobTitle, BorderLayout.CENTER);
		searchPanel.add(jobTitlePanel);

		KeyAdapter searchAdapter = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				searchEmployees(textAreaSearchlastName.getText(), textAreaSearchfirstName.getText(),
						textAreaSearchjobTitle.getText());
			}
		};

		textAreaSearchlastName.addKeyListener(searchAdapter);
		textAreaSearchfirstName.addKeyListener(searchAdapter);
		textAreaSearchjobTitle.addKeyListener(searchAdapter);
		new JPopupMenu();

		// inherit the copy function
		table.setComponentPopupMenu(CopyAction.createCopyMenu(table));

		// Panel for buttons

		JPanel panelButtons = new JPanel();
		panel.add(panelButtons, BorderLayout.EAST);

		// Set padding for the entire panel
		panelButtons.setBorder(new EmptyBorder(30, 30, 30, 30));

		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));

		// Define a uniform size for all buttons
		Dimension buttonSize = new Dimension(100, 30);

		Component verticalStrut = Box.createVerticalStrut(10);
		panelButtons.add(verticalStrut);

		// Add Button

		AddAction addButton = new AddAction("Add", table, "employeeNumber", "employees");
		addButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		addButton.setText("ADD");
		addButton.setMaximumSize(buttonSize);
		panelButtons.add(addButton);

		// Space between buttons
		panelButtons.add(Box.createVerticalStrut(10)); // 10px vertical space

		// Edit Button

		EditAction editButton = new EditAction("Edit", table, "employeeNumber", "employees");
		editButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		editButton.setText("EDIT");
		editButton.setMaximumSize(buttonSize);
		panelButtons.add(editButton);

		// Space between buttons
		panelButtons.add(Box.createVerticalStrut(10)); // 10px vertical space

		// Delete Button

		DeleteAction deleteButton = new DeleteAction("Delete", table, "employeeNumber", "employees");
		deleteButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		deleteButton.setText("DELETE");
		deleteButton.setMaximumSize(buttonSize);
		panelButtons.add(deleteButton);

		// Space between buttons
		panelButtons.add(Box.createVerticalStrut(10)); // 10px vertical space

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

	private void loadEmployees() {
		try {
			String query = "SELECT * FROM employees ORDER BY employeeNumber DESC";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(TableBuilder.buildTableModel(rs)); // Use the tableBuilder instance to call the
																// buildTableModel method
			table.setShowGrid(true); // Make grid lines visible and set their color
			table.setGridColor(Color.LIGHT_GRAY);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 0)); // Add 50px padding to the left of the
																					// table
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Searches the employees table using specified criteria for filtering.
	 * 
	 * 
	 * @param lastNameSearchText  Text to search within the lastName field.
	 * 
	 * @param firstNameSearchText Text to search within the firstName field.
	 * 
	 * @param jobTitleSearchText  Text to search within the jobTitle field.
	 * 
	 */

	private void searchEmployees(String lastNameSearchText, String firstNameSearchText, String jobTitleSearchText) {
		try {
			String query = "SELECT * FROM employees " + "WHERE (lastName LIKE ? OR ? IS NULL) "
					+ "AND (firstName LIKE ? OR ? IS NULL) " + "AND (jobTitle LIKE ? OR ? IS NULL)";
			PreparedStatement pst = conn.prepareStatement(query);

			pst.setString(1, "%" + lastNameSearchText + "%");
			pst.setString(2, lastNameSearchText.isEmpty() ? null : lastNameSearchText);
			pst.setString(3, "%" + firstNameSearchText + "%");
			pst.setString(4, firstNameSearchText.isEmpty() ? null : firstNameSearchText);
			pst.setString(5, "%" + jobTitleSearchText + "%");
			pst.setString(6, jobTitleSearchText.isEmpty() ? null : jobTitleSearchText);

			ResultSet rs = pst.executeQuery();
			table.setModel(TableBuilder.buildTableModel(rs)); // Use the tableBuilder instance to call the
																// buildTableModel method
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}