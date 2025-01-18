package ictgradschool.industry.final_project.inventory_management;

import ictgradschool.industry.final_project.utils.Utils;
import ictgradschool.industry.final_project.welcome_screen.OperationalWindow;

import javax.swing.*;
import java.awt.*;

public class InventoryManager extends JFrame {

    private String fileStorePath;
    private Inventory inventory;
    private InventoryTable inventoryTable;

    public InventoryManager(String fileStorePath) {
        this.fileStorePath = fileStorePath;
        this.inventory = new Inventory(fileStorePath); // Load inventory data
        // Create the InventoryTable panel
        inventoryTable = new InventoryTable(inventory);
        inventory.addObserver(inventoryTable);

        // Set the title of the window
        setTitle("Inventory Manager");

        // Set the size of the window
        setSize(800, 600);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Ensure the program exits when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a label with the text "Inventory Manager"
        JLabel titleLabel = new JLabel("Inventory Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding

        // Add the label to the top of the main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        mainPanel.add(inventoryTable, BorderLayout.CENTER);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column, with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50)); // Add padding

        // Create the "Add Item" button
        JButton addItemButton = new JButton("Add Item");
        Utils.styleButton(addItemButton, "#34d3eb", new Font("Arial", Font.BOLD, 16), new Dimension(200, 50));
        addItemButton.addActionListener(e -> showAddItemDialog());
        buttonPanel.add(addItemButton);

        // Create the "Return to Operations" button
        JButton returnButton = new JButton("Return to Operations");
        Utils.styleButton(returnButton, "#f5cb42", new Font("Arial", Font.BOLD, 16), new Dimension(200, 50));
        returnButton.addActionListener(e -> {
            // Close the current window and open the OperationalWindow
            dispose(); // Close the InventoryManager window
            new OperationalWindow(fileStorePath).setVisible(true); // Open the OperationalWindow
        });
        buttonPanel.add(returnButton);

        // Add the button panel to the SOUTH region of the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Make the window visible
        setVisible(true);
    }

    // Method to show the "Add Item" dialog
    private void showAddItemDialog() {
        // Create a dialog for adding a new item
        JDialog addItemDialog = new JDialog(this, "Add New Item", true);
        addItemDialog.setSize(400, 350); // Slightly increased height to accommodate padding
        addItemDialog.setLocationRelativeTo(this);

        // Create a panel to hold the form fields
        JPanel formPanel = new JPanel(new BorderLayout(10, 10)); // Use BorderLayout for the form panel
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Create a panel for the input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns for input fields

        // Add fields for name, description, price, and stock quantity
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockQuantityField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stock Quantity:"));
        inputPanel.add(stockQuantityField);

        // Add the input panel to the form panel
        formPanel.add(inputPanel, BorderLayout.CENTER);

        // Create a button to submit the form
        JButton submitButton = new JButton("Submit");
        Utils.styleButton(submitButton, "#34ebb4", new Font("Arial", Font.BOLD, 16), new Dimension(0, 40)); // Height is 40, width will stretch
        submitButton.addActionListener(e -> {
            // Validate and add the new item
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText());

                // Create a new InventoryItem
                InventoryItem newItem = new InventoryItem(name, description, price, stockQuantity);

                // Add the new item to the inventory
                inventory.addItem(newItem);

                // Save the updated inventory to the file
                inventory.saveInventory();

                // Close the dialog
                addItemDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addItemDialog, "Invalid input. Please enter numeric values for price and stock quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create a panel for the submit button to add padding
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add padding above the button
        buttonPanel.add(submitButton, BorderLayout.CENTER);

        // Add the button panel to the form panel
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the form panel to the dialog
        addItemDialog.add(formPanel);

        // Make the dialog visible
        addItemDialog.setVisible(true);
    }
}