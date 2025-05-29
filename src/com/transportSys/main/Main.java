package com.transportSys.main;

import com.transportSys.login.Login;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

/**
 * Main class that serves as the entry point for the Tranzit system application.
 * Launches the login window upon execution.
 * 
 * @author 7090
 */
public class Main {
	
    /**
     * Default constructor for the Main class.
     */
    public Main() {
        // Default constructor
    }
	
    /**
     * The main method to initialize and start the Tranzit system application.
     * 
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Set FlatLaf Dark Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create an instance of the Login class
        Login login = new Login();

        // Make the login window visible
        login.setVisible(true);
    }
}

