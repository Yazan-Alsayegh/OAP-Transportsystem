package com.transportSys.dashboard;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.transportSys.connection.dbConnection;
import com.transportSys.customers.CustomersWindow;
import com.transportSys.employees.EmployeesWindow;
import com.transportSys.offices.OfficesWindow;
import com.transportSys.orderdetails.OrderDetailsWindow;
import com.transportSys.orders.OrdersWindow;
import com.transportSys.payments.PaymentsWindow;
import com.transportSys.productlines.ProductLinesWindow;
import com.transportSys.products.ProductsWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

/**
 * The main Dashboard frame for the application.
 * 
 * @author 7074, 7107, 7090
 * 
 */
public class Dashboard extends JFrame {
	/**
	 * The serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the Dashboard frame. Initializes and sets up the main user
	 * interface components, including buttons, panels, and window properties like
	 * title and icons.
	 */
	public Dashboard() {
		setForeground(new Color(10, 21, 38));
		setBackground(new Color(10, 21, 38));
		getContentPane().setBackground(new Color(10, 21, 38));
		
		// Create menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create the "Help" menu
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutMenuItem = new JMenuItem("About");
		JMenuItem testConnectionMenuItem = new JMenuItem("Test Connection"); // Create Test Connection menu item
		JMenuItem runSQLMenuItem = new JMenuItem("Run SQL Queries"); // Create Run SQL Queries menu item

		// Add action listener to "About" menu item
		aboutMenuItem.addActionListener(e -> showAboutDialog());

		// Add action listener to "Test Connection" menu item
		testConnectionMenuItem.addActionListener(e -> testDatabaseConnection());

		// Add action listener to "Run SQL Queries" menu item
		runSQLMenuItem.addActionListener(e -> showSQLQueryDialog());

		// Add "About", "Test Connection", and "Run SQL Queries" items to the "Help" menu
		helpMenu.add(aboutMenuItem);
		helpMenu.add(testConnectionMenuItem); // Add Test Connection menu item to Help menu
		helpMenu.add(runSQLMenuItem); // Add Run SQL Queries menu item to Help menu

		// Add the "Help" menu to the menu bar
		menuBar.add(helpMenu);

		// Set the menu bar for the frame
		setJMenuBar(menuBar);




		// Set the title
		setTitle("tranzit Dashboard");

		
	 // Load the image icon for the window icon
	    ImageIcon windowIcon = new ImageIcon("images/windowIcon.png");
	    setIconImage(windowIcon.getImage());

	    // Load the image for the center logo
	    ImageIcon centerLogo = new ImageIcon("images/logoM.png");
	    JLabel centerLogoLabel = new JLabel(centerLogo); // Use a different name for the center logo label

	    // Create a panel for the logo
	    JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    logoPanel.setBackground(new Color(10, 21, 38));
	    centerLogoLabel.setBackground(new Color(10, 21, 38));
	    centerLogoLabel.setPreferredSize(new Dimension(600, 600)); // Adjust the size as needed
	    logoPanel.add(centerLogoLabel); // Use the center logo label in the logo panel
	    
		// Create buttons
	    
		// Create btnOffice button
		JButton btnOffice = new JButton("Offices");
		btnOffice.setBackground(new Color(10, 21, 38));
		btnOffice.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnOffice.setForeground(Color.WHITE);
		btnOffice.setFocusPainted(false);
		// Add action listener
		btnOffice.addActionListener(e -> openOfficesWindow());

		JButton btnEmployees = new JButton("Employees");
		btnEmployees.setBackground(new Color(10, 21, 38));
		btnEmployees.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnEmployees.setForeground(Color.WHITE);
		btnEmployees.setFocusPainted(false);
		// Add action listener
		btnEmployees.addActionListener(e -> openEmployeesWindow());

		JButton btnProductLines = new JButton("Product Lines");
		btnProductLines.setBackground(new Color(10, 21, 38));
		btnProductLines.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnProductLines.setForeground(Color.WHITE);
		btnProductLines.setFocusPainted(false);
		// Add action listener
		btnProductLines.addActionListener(e -> openProductLinesWindow());

		JButton btnOrderdetails = new JButton("Order Details");
		btnOrderdetails.setBackground(new Color(10, 21, 38));
		btnOrderdetails.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnOrderdetails.setForeground(Color.WHITE);
		btnOrderdetails.setFocusPainted(false);
		// Add action listener
		btnOrderdetails.addActionListener(e -> openOrderDetailsWindow());

		JButton btnPayments = new JButton("Payments");
		btnPayments.setBackground(new Color(10, 21, 38));
		btnPayments.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnPayments.setForeground(Color.WHITE);
		btnPayments.setFocusPainted(false);
		// Add action listener
		btnPayments.addActionListener(e -> openPaymentsWindow());

		JButton btnOrders = new JButton("Orders");
		btnOrders.setBackground(new Color(10, 21, 38));
		btnOrders.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnOrders.setForeground(Color.WHITE);
		btnOrders.setFocusPainted(false);
		// Add action listener
		btnOrders.addActionListener(e -> openOrdersWindow());

		JButton btnCustomers = new JButton("Customers");
		btnCustomers.setBackground(new Color(10, 21, 38));
		btnCustomers.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCustomers.setForeground(Color.WHITE);
		btnCustomers.setFocusPainted(false);
		// Add action listener
		btnCustomers.addActionListener(e -> openCustomersWindow());

		JButton btnProducts = new JButton("Products");
		btnProducts.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnProducts.setForeground(Color.WHITE);
		btnProducts.setBackground(new Color(10, 21, 38));
		btnProducts.setFocusPainted(false);
		// Add action listener
		btnProducts.addActionListener(e -> openProductsWindow());

		// Call the method for each button
		setButtonPreferredSize(btnOffice);
		setButtonPreferredSize(btnEmployees);
		setButtonPreferredSize(btnProductLines);
		setButtonPreferredSize(btnOrderdetails);
		setButtonPreferredSize(btnPayments);
		setButtonPreferredSize(btnOrders);
		setButtonPreferredSize(btnCustomers);
		setButtonPreferredSize(btnProducts);

		// Create buttonPanel as before
		JPanel buttonPanel = new JPanel(); // Use FlowLayout
		buttonPanel.setBackground(new Color(10, 21, 38));

		// Add padding to the bottom of the buttonPanel
		int bottomPadding = 50; // Set the desired bottom padding
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, bottomPadding, 0));

		// Create a layout for the frame
		getContentPane().setLayout(new BorderLayout());

		// Add the logo and button panels to the frame
		getContentPane().add(logoPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JLabel lblOrdersIcon = new JLabel();
		lblOrdersIcon.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblCustPayIcon = new JLabel();
		lblCustPayIcon.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblcompanyIcon = new JLabel();
		lblcompanyIcon.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblProductsIcon = new JLabel();
		lblProductsIcon.setHorizontalAlignment(SwingConstants.CENTER);

		// Load the icons for the labels
		ImageIcon iconOrders = new ImageIcon("images/orders.png");
		ImageIcon iconCustomers = new ImageIcon("images/customers.png");
		ImageIcon iconCompany = new ImageIcon("images/office.png");
		ImageIcon iconProducts = new ImageIcon("images/product lines.png");

		// Icon for Order and Orderdetails
		lblOrdersIcon.setIcon(iconOrders);

		// Icon for Customers and Payments
		lblCustPayIcon.setIcon(iconCustomers);

		// Icon for Offices and Employees
		lblcompanyIcon.setIcon(iconCompany);

		// Icon for Products and Productlines
		lblProductsIcon.setIcon(iconProducts);

		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addContainerGap(293, Short.MAX_VALUE)
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addComponent(lblOrdersIcon, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addComponent(btnOrders, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addComponent(btnOrderdetails, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addComponent(lblCustPayIcon, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnPayments, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCustomers, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnEmployees, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOffice, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblcompanyIcon, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnProducts, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProductsIcon, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnProductLines, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
					.addGap(15))
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProductsIcon, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblcompanyIcon, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(lblCustPayIcon, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(lblOrdersIcon, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnProducts, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnOffice, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnCustomers, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnOrders, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnProductLines, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnEmployees, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnPayments, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
						.addComponent(btnOrderdetails, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
					.addGap(150))
		);
		buttonPanel.setLayout(gl_buttonPanel);

		// Set frame properties
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set a minimum size for the frame
		setMinimumSize(new Dimension(1420, 750));

		// Add a WindowListener to handle window events
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				// Check if the window is iconified
				if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
					// When the window is minimized, restore it to the center
					setExtendedState(JFrame.NORMAL);
					setLocationRelativeTo(null);
				}
			}
		});
		
		
		// Create a panel for the logo and buttons
				JPanel contentPanel = new JPanel(new GridBagLayout());
				contentPanel.setBackground(new Color(10, 21, 38));

				// Create a GridBagConstraints instance
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.weightx = 1.0; // Horizontal weight to occupy available space
				gbc.weighty = 1.0; // Vertical weight to occupy available space
				gbc.anchor = GridBagConstraints.CENTER; // Align content to the center

				// Add the logo panel
				contentPanel.add(logoPanel, gbc);

				// Increment the gridy for the buttons
				gbc.gridy++;

				// Add the button panel
				contentPanel.add(buttonPanel, gbc);

				// Add the content panel to the center of the frame
				getContentPane().add(contentPanel, BorderLayout.CENTER);


				setVisible(true);
			}

	
	
	// Method to show the "About" dialog and its content
	private void showAboutDialog() {
	    String message = "<html><body style='width: 300px;'>" +
	                     "<h1>Welcome back!</h1>" +
	                     "<h2>General rules:</h2>" +
	                     "<p>Fields that strictly take only numbers as input can't be left without a value. <br>For instance, every column containing 'Number', 'Price', 'credLimit', 'quantityInStock', 'reportsTo'.</p>" +
	                     "<h2>'Reports to' & 'SalesRepEmployeeeNum':</h2>" +
	                     "<p>These fields must be filled with valid input. </br> Even if it was empty prior to the user editing the field, it must be added to keep track of who made the change.<p>" +
	                     "<h3>Near, Far, Wherever Our Customers Are..</h3>" +
	                     "</body></html>";

	    JOptionPane.showMessageDialog(this, message, "About the program", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Method to show the connection to the database
	private void testDatabaseConnection() {
	    Connection conn = null;
	    try {
	        conn = dbConnection.getConnection();
	        if (conn != null && !conn.isClosed()) {
	            JOptionPane.showMessageDialog(this, "Database connection successful!", "Connection Test", JOptionPane.INFORMATION_MESSAGE);
	            dbConnection.closeConnection(conn); // Close the connection after testing
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Connection Test", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Connection Test", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace(); // Log the exception for debugging purposes
	    } finally {
	        dbConnection.closeConnection(conn); // Close the connection in case of an exception
	    }
	}

	private void showSQLQueryDialog() {
	    JTextArea queryTextArea = new JTextArea(10, 40);
	    JScrollPane scrollPane = new JScrollPane(queryTextArea);

	    String[] suggestions = {
	        "SELECT * FROM customers;",
	        "SELECT * FROM employees;",
	        "SELECT * FROM orders;",
	        "SELECT * FROM customers JOIN orders ON customers.customerNumber = orders.customerNumber;"
	    };

	    JList<String> suggestionList = new JList<>(suggestions);
	    suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    suggestionList.setVisibleRowCount(5);
	    suggestionList.addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            String selectedQuery = suggestionList.getSelectedValue();
	            queryTextArea.setText(selectedQuery);
	        }
	    });

	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(scrollPane, BorderLayout.CENTER);
	    panel.add(new JScrollPane(suggestionList), BorderLayout.EAST);

	    int option = JOptionPane.showConfirmDialog(
	        this, panel, "Enter SQL Query", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
	    );

	    if (option == JOptionPane.OK_OPTION) {
	        String sqlQuery = queryTextArea.getText().trim();
	        executeAndDisplaySQLQuery(sqlQuery);
	    }
	}


	private void executeAndDisplaySQLQuery(String sqlQuery) {
	    if (sqlQuery.toLowerCase().contains("alter") ||
	        sqlQuery.toLowerCase().contains("drop") ||
	        sqlQuery.toLowerCase().contains("delete")) {
	        JOptionPane.showMessageDialog(this, "You are not allowed to perform this operation!", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try (Connection conn = dbConnection.getConnection()) {
	        Statement statement = conn.createStatement();
	        ResultSet resultSet = statement.executeQuery(sqlQuery);

	        JTable table = new JTable(buildTableModel(resultSet));
	        JScrollPane tableScrollPane = new JScrollPane(table);

	        JDialog resultDialog = new JDialog(this, "SQL Query Result", Dialog.ModalityType.MODELESS);
	        resultDialog.setMinimumSize(new Dimension(900, 800));
	        resultDialog.setResizable(true);

	        // Get the screen size to set the dialog size
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        resultDialog.setSize(screenSize.width, screenSize.height);

	        resultDialog.add(tableScrollPane);
	        resultDialog.setLocationRelativeTo(this);
	        resultDialog.setVisible(true);

	        resultSet.close();
	        statement.close();
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "Error executing SQL query: " + " Code not accepted. ", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	// Method to build a TableModel from a ResultSet
	private static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
	    java.sql.ResultSetMetaData metaData = resultSet.getMetaData();

	    // Create columns based on the ResultSet metadata
	    int columnCount = metaData.getColumnCount();
	    String[] columnNames = new String[columnCount];
	    for (int i = 1; i <= columnCount; i++) {
	        columnNames[i - 1] = metaData.getColumnName(i);
	    }

	    // Create data for the table
	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	    while (resultSet.next()) {
	        Object[] rowData = new Object[columnCount];
	        for (int i = 1; i <= columnCount; i++) {
	            rowData[i - 1] = resultSet.getObject(i);
	        }
	        tableModel.addRow(rowData);
	    }
	    return tableModel;
	}

	
	
	// Method to set preferred size for buttons
	private void setButtonPreferredSize(JButton button) {
		button.setPreferredSize(new Dimension(150, 60));
		button.setBackground(new Color(10, 21, 38));
		button.setFont(new Font("Tahoma", Font.BOLD, 15));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openOfficesWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				OfficesWindow officesWindow = new OfficesWindow();
	            officesWindow.frame.setLocationRelativeTo(null); // Center the window
				officesWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openEmployeesWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				EmployeesWindow employeesWindow = new EmployeesWindow();
				employeesWindow.frame.setLocationRelativeTo(null); // Center the window
				employeesWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openProductLinesWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				ProductLinesWindow productLines = new ProductLinesWindow();
				productLines.frame.setLocationRelativeTo(null); // Center the window
				productLines.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openProductsWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				ProductsWindow productsWindow = new ProductsWindow();
				productsWindow.frame.setLocationRelativeTo(null); // Center the window
				productsWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openOrdersWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				OrdersWindow ordersWindow = new OrdersWindow();
				ordersWindow.frame.setLocationRelativeTo(null); // Center the window
				ordersWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openOrderDetailsWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				OrderDetailsWindow ordersDetailsWindow = new OrderDetailsWindow();
				ordersDetailsWindow.frame.setLocationRelativeTo(null); // Center the window
				ordersDetailsWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openCustomersWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				CustomersWindow customersWindow = new CustomersWindow();
				customersWindow.frame.setLocationRelativeTo(null); // Center the window
				customersWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// Method to open respective class
	@SuppressWarnings("deprecation")
	private void openPaymentsWindow() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatLightLaf.install();

				// Create OfficesWindow instance after setting the theme
				PaymentsWindow paymentsWindow = new PaymentsWindow();
				paymentsWindow.frame.setLocationRelativeTo(null); // Center the window
				paymentsWindow.frame.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	
	}
	/**
	 * The main method to start the application. Sets the look and feel to
	 * FlatDarkLaf and invokes the Dashboard GUI.
	 *
	 * @param args Command line arguments (not used in this context).
	 */
	public static void main(String[] args) {
		try {
			// Preserve existing colors and set the look and feel to FlatDarkLaf
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Create and display the Dashboard GUI on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> new Dashboard());
	}

}
