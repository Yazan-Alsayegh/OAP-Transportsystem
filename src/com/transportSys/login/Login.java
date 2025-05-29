package com.transportSys.login;

import com.formdev.flatlaf.FlatDarkLaf;
import com.transportSys.dashboard.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class represents the login window of the transportation system
 * application.
 * 
 * @author 7074, 7107, 7078
 */
public class Login extends JFrame {

	/**
	 * The serial version UID for serialization/de-serialization. This helps in
	 * uniquely identifying the class when serializing instances of this class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The main content pane for the login interface.
	 */
	private JPanel contentPane;

	/**
	 * Text field for entering the username.
	 */
	private JTextField usernameField;

	/**
	 * Text field for entering the password (hidden for security).
	 */
	private JPasswordField passwordField;

	/**
	 * Label indicating the "Username" text field.
	 */
	private JLabel lblUsername;

	/**
	 * Label indicating the "Password" text field.
	 */
	private JLabel lblPassword;

	/**
	 * The panel encapsulating the login form components.
	 */
	private JPanel Login_Form;

	/**
	 * Main method to start the application.
	 * 
	 * @param args The command-line arguments
	 */
	public static void main(String[] args) {
		try {
			// Set the FlatLaf dark theme
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> new Login());
	}

	/**
	 * Constructs the login window.
	 */
	public Login() {
		// Set the FlatLaf dark theme
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set the size of the login window
		int windowWidth = 1092;
		int windowHeight = 523;
		setSize(windowWidth, windowHeight);

		// Set the location of the login window to the center of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - windowWidth) / 2;
		int y = (screenSize.height - windowHeight) / 2;
		setLocation(x, y);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setTitle("tranzit Login");
		JPanel panel = new JPanel();
		panel.setBackground(new Color(10, 21, 38));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		// Load the image icon for the window icon
		ImageIcon windowIcon = new ImageIcon("images/windowIcon.png");
		setIconImage(windowIcon.getImage());

		// Add JLabel to display logo
		JLabel logoLabel = new JLabel(new ImageIcon("images/logoM.png"));
		logoLabel.setBounds(10, 105, 793, 246);
		panel.add(logoLabel);

		// Login form panel
		Login_Form = new JPanel();
		Login_Form.setBackground(new Color(10, 21, 38));
		Login_Form.setBounds(813, 142, 210, 209);
		Login_Form.setLayout(null);

		// Username field
		usernameField = new JTextField();
		usernameField.setBounds(32, 37, 130, 26);
		Login_Form.add(usernameField);
		usernameField.setColumns(10);

		// Password field
		passwordField = new JPasswordField();
		passwordField.setBounds(32, 82, 130, 26);
		Login_Form.add(passwordField);

		// Login button
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(59, 118, 79, 29);
		Login_Form.add(btnLogin);

		// Labels for username and password
		lblUsername = new JLabel("Username");
		lblUsername.setBounds(32, 21, 62, 16);
		Login_Form.add(lblUsername);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(32, 64, 59, 16);
		Login_Form.add(lblPassword);
		panel.add(Login_Form);

		// Instructions for default login credentials
		JLabel lblLoginInfo = new JLabel("<html>(Use \"student\" as <br>Username and Password)</html>");
		lblLoginInfo.setBounds(32, 152, 178, 57);
		Login_Form.add(lblLoginInfo);
		lblLoginInfo.setHorizontalAlignment(SwingConstants.LEFT);
		lblLoginInfo.setFont(new Font("Tahoma", Font.BOLD, 12));

		// ActionListener for the "Login" button
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performLogin();
			}
		});

		// KeyListener for the password field
		passwordField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					performLogin();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		setVisible(true);
	}

	/**
	 * Checks if the login credentials are valid.
	 * 
	 * @param username The entered username
	 * @param password The entered password
	 * @return true if the credentials are valid, false otherwise
	 */
	private boolean isLoginValid(String username, String password) {
		return username.equals("student") && password.equals("student");
	}

	/**
	 * Performs the login process.
	 */
	private void performLogin() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		// Check if the entered username and password are correct
		if (isLoginValid(username, password)) {
			// If correct, display a success message
			JOptionPane.showMessageDialog(Login.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

			// Open the Dashboard window after the user presses "OK"
			openDashboard();
		} else {
			// If incorrect, show an error message
			JOptionPane.showMessageDialog(Login.this, "Invalid username or password", "Login Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Opens the Dashboard window upon successful login.
	 */
	@SuppressWarnings("deprecation")
	private void openDashboard() {
		EventQueue.invokeLater(() -> {
			try {
				// Set the FlatLaf light theme
				FlatDarkLaf.install();

				// Create and display the Dashboard window
				Dashboard dashboard = new Dashboard();
				dashboard.setVisible(true);

				// Close the login window
				dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
