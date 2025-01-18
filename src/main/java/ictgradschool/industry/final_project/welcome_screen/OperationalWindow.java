package ictgradschool.industry.final_project.welcome_screen;

import ictgradschool.industry.final_project.inventory_management.InventoryManager;

import ictgradschool.industry.final_project.pos_management.POSManager;
import ictgradschool.industry.final_project.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class OperationalWindow extends JFrame {

    private String fileStorePath;

    public OperationalWindow(String filePath) {
        fileStorePath = filePath;

        // Set the title of the window
        setTitle("Operations");

        // Set the size of the window
        setSize(600, 600);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Ensure the program exits when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding

        // Create the "Open Inventory Manager" button
        JButton openInventoryButton = new JButton("Open Inventory Manager");
        Utils.styleButton(openInventoryButton,
                "#f5cb42",
                new Font("Arial", Font.BOLD, 20),
                new Dimension(200, 80));
        openInventoryButton.addActionListener(e -> {
            dispose(); // Close the current OperationalWindow
            new InventoryManager(fileStorePath).setVisible(true);
        });

        // Create the "Open Point of Sale" button
        JButton openPOSButton = new JButton("Open Point of Sale");
        Utils.styleButton(openPOSButton,
                "#f5a742",
                new Font("Arial", Font.BOLD, 20),
                new Dimension(200, 80));
        openPOSButton.addActionListener(e -> {
            // Open the Point of Sale window
            dispose(); // Close the current OperationalWindow
            new POSManager(fileStorePath).setVisible(true); // Open the POSManager with the file store path
        });

        // Create the "Close Filestore" button
        JButton closeFilestoreButton = new JButton("Close Filestore");
        Utils.styleButton(closeFilestoreButton,
                "#f55742",
                new Font("Arial", Font.BOLD, 20),
                new Dimension(200, 80));
        closeFilestoreButton.addActionListener(e -> {
            // Add logic to close the filestore and return to the WelcomeScreen
            dispose(); // Close the current window
            new WelcomeScreen().setVisible(true); // Open the WelcomeScreen
        });

        // Add buttons to the panel
        buttonPanel.add(openInventoryButton);
        buttonPanel.add(openPOSButton);
        buttonPanel.add(closeFilestoreButton);

        // Add the button panel to the center of the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Create a label to display the file path at the bottom
        JLabel filePathLabel = new JLabel("Filestore: " + fileStorePath, SwingConstants.CENTER);
        filePathLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font
        filePathLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Add the file path label to the bottom of the main panel
        mainPanel.add(filePathLabel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Make the window visible
        setVisible(true);
    }
}