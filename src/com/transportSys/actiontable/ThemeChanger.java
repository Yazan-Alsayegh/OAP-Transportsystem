package com.transportSys.actiontable;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Represents a toggle button for changing themes in a table. The button
 * switches between dark and light themes and updates the table's appearance
 * accordingly.
 * 
 * This button class extends JToggleButton.
 * 
 * @author 7107, 7090
 * 
 */
public class ThemeChanger extends JToggleButton {
    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;
    
	/**
	 * Indicates whether the current theme is dark or light.
	 */
	private boolean isDarkTheme = false; // The current theme mode

	/**
	 * Represents the table where theme changes will be applied.
	 */
	private JTable table; // The table for theme changes

	/**
	 * Represents the main frame where the table exists.
	 */
	private JFrame frame; // The main frame containing the table

	/**
	 * Icon to represent the light theme.
	 */
	private ImageIcon sunIcon; // Icon for the light theme

	/**
	 * Icon to represent the dark theme.
	 */
	private ImageIcon moonIcon; // Icon for the dark theme

	/**
	 * Represents the current X position of the icon for button animation.
	 */
	private int iconPositionX = 0; // X-position of the theme switch icon

	/**
	 * Timer used for controlling the animation of the icon position.
	 */
	private Timer animationTimer; // Timer for icon animation

	/**
	 * Constructor for ThemeChanger. Initializes the ThemeChanger with the specified
	 * table and frame.
	 * 
	 * @param table The JTable where the theme changes will be applied.
	 * @param frame The main JFrame where the table exists.
	 * 
	 */
	public ThemeChanger(JTable table, JFrame frame) {
		this.table = table;
		this.frame = frame;

		// Loading icon images
		try {
			sunIcon = new ImageIcon(new File("images/sunImg1.png").toURI().toURL());
			moonIcon = new ImageIcon(new File("images/moonImg1.png").toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		setFocusPainted(false); // Remove the focus border
		setBorder(null); // Remove the border
		setIcon(moonIcon); // Default to moon icon (light mode)
		setPreferredSize(new Dimension(60, 30)); // Button size
		setBackground(Color.BLACK); // Default background for light mode
		addActionListener(new themeChangerActionListener());
		updateTableAppearance(); // Set initial appearance
	}

	@Override
	public Border getBorder() {
		return BorderFactory.createEmptyBorder();
	}

	// Custom border class for rounded corners
	class RoundedBorder extends LineBorder {
	    /**
	     * The serial version UID for serialization.
	     */
	    private static final long serialVersionUID = 1L;
	    
		private int radius;

		public RoundedBorder(Color color, int thickness, int radius) {
			super(color, thickness);
			this.radius = radius;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(getLineColor());
			g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
			g2d.dispose();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		// Custom painting of the component
		Graphics2D g2d = (Graphics2D) g.create();

		// Enable anti-aliasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw a rounded rectangle for the button's background
		g2d.setColor(getBackground());
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35); // Adds arc for rounded corners

		// Draws the icon based on the selected theme
		int iconPositionY = (getHeight() - 30) / 2; // Center the icon vertically
		if (isDarkTheme) {
			g2d.drawImage(sunIcon.getImage(), iconPositionX, iconPositionY, 30, 30, this);
		} else {
			g2d.drawImage(moonIcon.getImage(), iconPositionX, iconPositionY, 30, 30, this);
		}

		g2d.dispose(); // Dispose the graphics object
	}

	// Updates the appearance of the associated table based on the current theme
	private void updateTableAppearance() {
		// Applies table appearance settings for both light and dark themes
		table.setShowGrid(true); // Make grid lines visible
		JTableHeader header = table.getTableHeader(); // Get the table header
		header.setBackground(Color.BLACK); // Black header for light theme
		header.setForeground(Color.WHITE); // White text for light theme

		if (isDarkTheme) {
			table.setBackground(new Color(30, 30, 30)); // Set the background color for the dark theme
			table.setForeground(Color.WHITE); // Set the foreground color for the dark theme
			table.setGridColor(new Color(50, 50, 50)); // Set a slightly lighter grid color for the dark theme

			header.setBackground(Color.WHITE); // White header for dark theme
			header.setForeground(Color.BLACK); // Black text for dark theme
		} else {
			table.setBackground(Color.WHITE); // Set the background color for the light theme
			table.setForeground(Color.BLACK); // Set the foreground color for the light theme
			table.setGridColor(Color.WHITE); // Set grid color for the light theme

			header.setBackground(Color.BLACK); // Black header for light theme
			header.setForeground(Color.WHITE); // White text for light theme
		}
	}

	// ActionListener for the theme change button
	private class themeChangerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Handles the animation of theme change
			if (animationTimer != null && animationTimer.isRunning()) {
				animationTimer.stop();
			}

			int targetPosition = isDarkTheme ? 0 : getWidth() - 30;
			animationTimer = new Timer(5, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isDarkTheme) {
						iconPositionX -= 2;
						if (iconPositionX <= targetPosition) {
							animationTimer.stop();
							toggleTheme();
						}
					} else {
						iconPositionX += 2;
						if (iconPositionX >= targetPosition) {
							animationTimer.stop();
							toggleTheme();
						}
					}
					repaint();
				}
			});
			animationTimer.start();
		}

		// Toggles between light and dark themes
		private void toggleTheme() {
			// Switches between light and dark themes
			isDarkTheme = !isDarkTheme;
			if (isDarkTheme) {
				setBackground(Color.WHITE); // Set the background to white for dark theme
				try {
					UIManager.setLookAndFeel(new FlatDarkLaf());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				setBackground(Color.BLACK); // Set the background to black for light theme
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			SwingUtilities.updateComponentTreeUI(frame);
			updateTableAppearance();
			repaint(); // Trigger a repaint
		}

	}
}