package ictgradschool.industry.final_project.welcome_screen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import ictgradschool.industry.final_project.utils.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Welcome to Inventory Manager");

        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a label with the text "Welcome to Inventory Manager"
        JLabel welcomeLabel = new JLabel("Welcome to Inventory Manager", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        // Create the "Create New Filestore" button
        JButton createButton = new JButton("Create New Filestore");
        Utils.styleButton(createButton,
                "#f5cb42",
                new Font("Arial", Font.BOLD, 20),
                new Dimension(300, 80)); // Use Utils class to style the button
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser("Create New Filestore");
            }
        });

        // Create the "Open Existing Filestore" button
        JButton openButton = new JButton("Open Existing Filestore");
        Utils.styleButton(openButton,
                "#f5a742",
                new Font("Arial", Font.BOLD, 20),
                new Dimension(300, 80));
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser("Open Existing Filestore");
            }
        });

        // Create the "Exit" button
        JButton exitButton = new JButton("Exit");
        Utils.styleButton(exitButton,
                "#eb4034", // Red color for the exit button
                new Font("Arial", Font.BOLD, 20),
                new Dimension(300, 80));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });

        // Add buttons to the panel
        buttonPanel.add(createButton);
        buttonPanel.add(openButton);
        buttonPanel.add(exitButton);

        // Add the button panel to the center of the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    private void openFileChooser(String action) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Allow only directories to be selected
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String filePath = selectedDirectory.getAbsolutePath(); // Get the selected file path
            JOptionPane.showMessageDialog(this, action + ": " + filePath);

            // Open the OperationalWindow and pass the file path
            dispose(); // Close the current WelcomeScreen window
            new OperationalWindow(filePath).setVisible(true); // Open the OperationalWindow with the file path
        }
    }

    private void openOperationalWindow(String filePath) {
        dispose(); // Close the current WelcomeScreen window
        new OperationalWindow(filePath).setVisible(true); // Open the OperationWindow
    }
}