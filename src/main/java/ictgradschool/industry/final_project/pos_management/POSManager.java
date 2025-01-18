package ictgradschool.industry.final_project.pos_management;

import ictgradschool.industry.final_project.inventory_management.Inventory;
import ictgradschool.industry.final_project.inventory_management.InventoryItem;
import ictgradschool.industry.final_project.utils.Utils;
import ictgradschool.industry.final_project.welcome_screen.OperationalWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class POSManager extends JFrame {

    private Inventory inventory;
    private Cart cart;

    private DefaultTableModel cartTableModel;
    private JTable cartTable; // Declare cartTable as a field
    private JLabel totalCostLabel; // Label to display the total cost
    private JTable inventoryTable; // Declare inventoryTable as a field
    private JButton checkoutButton; // Declare checkoutButton as a field

    public POSManager(String fileStorePath) {
        // Load the inventory from the file store path
        this.inventory = new Inventory(fileStorePath);
        this.cart = new Cart(); // Initialize the cart

        // Set the title of the window
        setTitle("Point of Sale");

        // Set the size of the window
        setSize(800, 800); // Increased height to accommodate the cart table

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Ensure the program exits when the window is closed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a main panel with GridBagLayout for better control
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Create a label with the text "Point of Sale"
        JLabel posLabel = new JLabel("Point of Sale", SwingConstants.CENTER);
        posLabel.setFont(new Font("Serif", Font.BOLD, 24));
        posLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add the label to the top of the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(posLabel, gbc);

        // Create a panel to hold the "Available Items" label and the table
        JPanel availableItemsPanel = new JPanel(new BorderLayout());

        // Create a label for "Available Items"
        JLabel availableItemsLabel = new JLabel("Available Items", SwingConstants.CENTER);
        availableItemsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        availableItemsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add the "Available Items" label to the top of the available items panel
        availableItemsPanel.add(availableItemsLabel, BorderLayout.NORTH);

        // Create a table to display inventory items
        String[] columnNames = {"ID", "Name", "Description", "Price", "Stock Quantity"};
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Populate the table with inventory items that have a quantity > 0
        for (InventoryItem item : inventory.getItems()) {
            if (item.getStockQuantity() > 0) {
                Object[] rowData = {
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getStockQuantity()
                };
                inventoryTableModel.addRow(rowData);
            }
        }

        // Create the JTable with the populated table model
        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setRowHeight(30); // Set row height

        // Add the table to a scroll pane
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);
        inventoryScrollPane.setPreferredSize(new Dimension(700, 200)); // Set preferred size

        // Add the scroll pane to the center of the available items panel
        availableItemsPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        // Create a panel for the "Add to Cart" button
        JPanel addToCartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create a button to add selected items to the cart
        JButton addToCartButton = new JButton("Add to Cart");
        Utils.styleButton(addToCartButton, "#34d3eb", new Font("Arial", Font.BOLD, 16), new Dimension(200, 40));
        addToCartButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String itemId = (String) inventoryTableModel.getValueAt(selectedRow, 0);
                InventoryItem selectedItem = inventory.getItemById(itemId);
                if (selectedItem != null) {
                    cart.addItem(selectedItem);
                    updateCartTable(); // Refresh the cart table
                    updateTotalCost(); // Update the total cost
                    updateCheckoutButtonState(); // Update the "Checkout" button state
                    JOptionPane.showMessageDialog(this, "Added to cart: " + selectedItem.getName());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to add to the cart.");
            }
        });

        // Add the "Add to Cart" button to the button panel
        addToCartButtonPanel.add(addToCartButton);

        // Add the button panel below the available items table
        availableItemsPanel.add(addToCartButtonPanel, BorderLayout.SOUTH);

        // Add the available items panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6; // Allocate 60% of the space to the available items panel
        mainPanel.add(availableItemsPanel, gbc);

        // Create a panel to hold the "Cart" label and the cart table
        JPanel cartPanel = new JPanel(new BorderLayout());

        // Create a label for "Cart"
        JLabel cartLabel = new JLabel("Cart", SwingConstants.CENTER);
        cartLabel.setFont(new Font("Serif", Font.BOLD, 18));
        cartLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add the "Cart" label to the top of the cart panel
        cartPanel.add(cartLabel, BorderLayout.NORTH);

        // Create a table to display cart items
        String[] cartColumnNames = {"ID", "Name", "Price", "Quantity", "Total"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Create the JTable with the cart table model
        cartTable = new JTable(cartTableModel); // Initialize cartTable
        cartTable.setRowHeight(30); // Set row height

        // Add the table to a scroll pane
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setPreferredSize(new Dimension(700, 200)); // Set preferred size

        // Add the scroll pane to the center of the cart panel
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Create a panel for the "Remove Item", "Checkout" buttons, and total cost label
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create a panel for the buttons (Checkout and Remove Item)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create a button to checkout
        checkoutButton = new JButton("Checkout");
        Utils.styleButton(checkoutButton, "#34d3eb", new Font("Arial", Font.BOLD, 16), new Dimension(150, 40));
        checkoutButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty. Add items to proceed with checkout.");
                return;
            }

            // Open a file picker dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Receipt");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Write the receipt to the selected file
                if (writeReceiptToFile(fileToSave)) {
                    // Update the inventory by reducing the stock quantity of the items in the cart
                    for (Cart.CartItem cartItem : cart.getItems()) {
                        InventoryItem item = cartItem.getItem();
                        int newQuantity = item.getStockQuantity() - cartItem.getQuantity();
                        item.setStockQuantity(newQuantity);
                    }

                    // Save the updated inventory to the file
                    inventory.saveInventory();

                    // Clear the cart
                    cart.clear();
                    updateCartTable(); // Refresh the cart table
                    updateTotalCost(); // Update the total cost

                    // Update the available items table to reflect the new stock quantities
                    updateAvailableItemsTable();

                    // Disable the "Checkout" button if the cart is empty
                    updateCheckoutButtonState();

                    JOptionPane.showMessageDialog(this, "Checkout successful! Receipt saved to: " + fileToSave.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save receipt. Checkout canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Checkout canceled. No receipt file was saved.");
            }
        });

        // Create a button to remove selected items from the cart
        JButton removeItemButton = new JButton("Remove Item");
        Utils.styleButton(removeItemButton, "#eb4034", new Font("Arial", Font.BOLD, 16), new Dimension(150, 40));
        removeItemButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                String itemId = (String) cartTableModel.getValueAt(selectedRow, 0);
                cart.removeItem(itemId);
                updateCartTable(); // Refresh the cart table
                updateTotalCost(); // Update the total cost
                updateCheckoutButtonState(); // Update the "Checkout" button state
                JOptionPane.showMessageDialog(this, "Removed from cart: " + itemId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove from the cart.");
            }
        });

        // Add the "Checkout" and "Remove Item" buttons to the buttons panel
        buttonsPanel.add(checkoutButton);
        buttonsPanel.add(removeItemButton);

        // Add the buttons panel to the left side of the bottom panel
        bottomPanel.add(buttonsPanel, BorderLayout.WEST);

        // Create a label to display the total cost
        totalCostLabel = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        totalCostLabel.setFont(new Font("Serif", Font.BOLD, 16));
        totalCostLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20)); // Add padding

        // Add the total cost label to the right side of the bottom panel
        bottomPanel.add(totalCostLabel, BorderLayout.EAST);

        // Add the bottom panel to the cart panel
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the cart panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4; // Allocate 40% of the space to the cart panel
        mainPanel.add(cartPanel, gbc);

        // Create a "Back to Options" button
        JButton backToOptionsButton = new JButton("Back to Options");
        Utils.styleButton(backToOptionsButton, "#f5cb42", new Font("Arial", Font.BOLD, 16), new Dimension(700, 40));
        backToOptionsButton.addActionListener(e -> {
            dispose(); // Close the POSManager window
            new OperationalWindow(fileStorePath).setVisible(true); // Open the OperationalWindow
        });

        // Add the "Back to Options" button to the bottom of the main panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0; // Do not allocate extra space to the button
        mainPanel.add(backToOptionsButton, gbc);

        // Add the main panel to the frame
        add(mainPanel);

        // Initialize the "Checkout" button state
        updateCheckoutButtonState();

        // Make the window visible
        setVisible(true);
    }

    /**
     * Updates the cart table with the current items in the cart.
     */
    private void updateCartTable() {
        cartTableModel.setRowCount(0); // Clear the table

        for (Cart.CartItem cartItem : cart.getItems()) {
            InventoryItem item = cartItem.getItem();
            Object[] rowData = {
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    cartItem.getQuantity(),
                    item.getPrice() * cartItem.getQuantity()
            };
            cartTableModel.addRow(rowData);
        }
    }

    /**
     * Updates the total cost label with the current total cost of the cart.
     */
    private void updateTotalCost() {
        double totalCost = cart.getTotalCost();
        totalCostLabel.setText(String.format("Total: $%.2f", totalCost)); // Format the total cost
    }

    /**
     * Updates the available items table to reflect the current inventory.
     */
    private void updateAvailableItemsTable() {
        DefaultTableModel inventoryTableModel = (DefaultTableModel) inventoryTable.getModel();
        inventoryTableModel.setRowCount(0); // Clear the table

        // Populate the table with inventory items that have a quantity > 0
        for (InventoryItem item : inventory.getItems()) {
            if (item.getStockQuantity() > 0) {
                Object[] rowData = {
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getStockQuantity()
                };
                inventoryTableModel.addRow(rowData);
            }
        }
    }

    /**
     * Updates the state of the "Checkout" button based on whether the cart is empty.
     */
    private void updateCheckoutButtonState() {
        checkoutButton.setEnabled(!cart.isEmpty());
    }

    /**
     * Writes the receipt to the specified file.
     *
     * @param file The file to write the receipt to.
     * @return True if the receipt was successfully written, false otherwise.
     */
    private boolean writeReceiptToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            // Group items by name and calculate total quantity and cost
            Map<String, Integer> itemQuantities = new HashMap<>();
            Map<String, Double> itemTotalCosts = new HashMap<>();

            for (Cart.CartItem cartItem : cart.getItems()) {
                String itemName = cartItem.getItem().getName();
                int quantity = cartItem.getQuantity();
                double totalCost = cartItem.getItem().getPrice() * quantity;

                itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + quantity);
                itemTotalCosts.put(itemName, itemTotalCosts.getOrDefault(itemName, 0.0) + totalCost);
            }

            // Write the receipt header
            writer.write("-------------------------------\n");

            // Write each item in the receipt
            DecimalFormat df = new DecimalFormat("#.00");
            for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double totalCost = itemTotalCosts.get(itemName);
                double unitPrice = totalCost / quantity;

                if (quantity > 1) {
                    // Display unit price and total price
                    writer.write(String.format("%-3d %-20s ($%7s) $%8s\n",
                            quantity, itemName, df.format(unitPrice), df.format(totalCost)));
                } else {
                    // Display only total price, aligned with other total prices
                    writer.write(String.format("%-3d %-20s %11s $%8s\n",
                            quantity, itemName, "", df.format(totalCost)));
                }
            }

            // Write the total cost
            writer.write("================================\n");
            writer.write(String.format("%-24s %11s $%8s\n", "TOTAL", "", df.format(cart.getTotalCost())));
            writer.write("-------------------------------\n");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}