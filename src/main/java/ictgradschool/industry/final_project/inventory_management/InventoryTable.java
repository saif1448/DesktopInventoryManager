package ictgradschool.industry.final_project.inventory_management;

import ictgradschool.industry.final_project.utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryTable extends JPanel {
    private Inventory inventory;
    private JTable inventoryTable;
    private JButton removeItemButton;
    private JButton searchButton;
    private JButton filterButton;
    private JButton showAllButton;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public InventoryTable(Inventory inventory) {
        this.inventory = inventory;
        setLayout(new BorderLayout());
        initializeTable();    // Initialize table
        initializeButtons();  // Initialize buttons
        setupLayout();       // Setup the layout
    }

    private void initializeTable() {
        String[] columnNames = {"ID", "Name", "Description", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: return String.class;
                    case 2: return String.class;
                    case 3: return Double.class;
                    case 4: return Integer.class;
                    default: return Object.class;
                }
            }
        };

        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(30);

        // Initialize sorter
        sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);

        // Add selection listener for Remove button
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            removeItemButton.setEnabled(inventoryTable.getSelectedRow() != -1);
        });

        // Add table model listener for editing
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Ensure the row and column are valid
            if (row >= 0 && column >= 0 && !isRefreshing) {
                try {
                    String id = (String) tableModel.getValueAt(row, 0);
                    InventoryItem item = inventory.getItemById(id);
                    if (item != null) {
                        String name = (String) tableModel.getValueAt(row, 1);
                        String description = (String) tableModel.getValueAt(row, 2);
                        double price = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
                        int stockQuantity = Integer.parseInt(tableModel.getValueAt(row, 4).toString());

                        InventoryItem updatedItem = new InventoryItem(
                                id, name, description, price, stockQuantity
                        );
                        inventory.updateItem(id, updatedItem);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid data: Price and Stock Quantity must be numeric.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    refresh();
                }
            }
        });

        // Load initial data
        refreshTableData();
    }

    private void initializeButtons() {
        // Initialize Remove Item button
        removeItemButton = new JButton("Remove Item");
        removeItemButton.setEnabled(false);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Utils.styleButton(removeItemButton, "#eb4034", buttonFont, new Dimension(400, 40));
        removeItemButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                inventory.removeItem(id);
                refresh();
            }
        });

        // Initialize Search, Filter, and Show All buttons
        searchButton = new JButton();
        filterButton = new JButton();
        showAllButton = new JButton("Show All");

        // Load and set icons
        try {
            ImageIcon searchIcon = new ImageIcon(getClass().getResource("/ictgradschool/industry/final_project/resources/icons/search.png"));
            ImageIcon filterIcon = new ImageIcon(getClass().getResource("/ictgradschool/industry/final_project/resources/icons/filter.png"));

            Image searchImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            Image filterImage = filterIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

            searchButton.setIcon(new ImageIcon(searchImage));
            filterButton.setIcon(new ImageIcon(filterImage));
        } catch (Exception e) {
            // Fallback to text if icons fail to load
            searchButton.setText("Search");
            filterButton.setText("Filter");
        }

        // Style buttons
        Utils.styleButton(searchButton, "#34d3eb", buttonFont, new Dimension(120, 40));
        Utils.styleButton(filterButton, "#34d3eb", buttonFont, new Dimension(120, 40));
        Utils.styleButton(showAllButton, "#34d3eb", buttonFont, new Dimension(120, 40));

        // Add action listeners
        searchButton.addActionListener(e -> showSearchDialog());
        filterButton.addActionListener(e -> showFilterDialog());
        showAllButton.addActionListener(e -> showAllData());
    }

    private void setupLayout() {
        removeAll();

        if (inventory.getItems().isEmpty()) {
            JLabel emptyLabel = new JLabel("Inventory is empty", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Serif", Font.BOLD, 18));
            add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Create top button panel
            JPanel topButtonPanel = new JPanel();
            topButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            topButtonPanel.add(searchButton);
            topButtonPanel.add(filterButton);
            topButtonPanel.add(showAllButton);
            add(topButtonPanel, BorderLayout.NORTH);

            // Add table with scroll pane
            JScrollPane scrollPane = new JScrollPane(inventoryTable);
            add(scrollPane, BorderLayout.CENTER);

            // Create bottom button panel
            JPanel bottomButtonPanel = new JPanel();
            bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            bottomButtonPanel.add(removeItemButton);
            add(bottomButtonPanel, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }

    // Flag to prevent TableModelListener from triggering during refresh
    private boolean isRefreshing = false;

    protected void refreshTableData() {
        isRefreshing = true; // Set flag to true
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear existing rows

            for (InventoryItem item : inventory.getItems()) {
                Object[] rowData = {
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getStockQuantity()
                };
                tableModel.addRow(rowData);
            }
            isRefreshing = false; // Reset flag after refresh
        });
    }

    public void refresh() {
        refreshTableData(); // Refresh the table data
        setupLayout();      // Rebuild the layout
        revalidate();       // Revalidate the panel
        repaint();          // Repaint the panel
    }

    private void showSearchDialog() {
        JDialog searchDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search Item", true);
        searchDialog.setLayout(new GridLayout(4, 2, 10, 10));
        searchDialog.setSize(300, 200);

        JLabel searchLabel = new JLabel("Search Term:");
        JTextField searchField = new JTextField();
        JLabel criteriaLabel = new JLabel("Search By:");
        JComboBox<String> criteriaComboBox = new JComboBox<>(new String[]{"ID", "Name", "Description"});
        JButton searchButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            String searchCriteria = (String) criteriaComboBox.getSelectedItem();

            if (!searchTerm.isEmpty()) {
                filterTable(searchTerm, searchCriteria);
                searchDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(searchDialog, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> searchDialog.dispose());

        searchDialog.add(searchLabel);
        searchDialog.add(searchField);
        searchDialog.add(criteriaLabel);
        searchDialog.add(criteriaComboBox);
        searchDialog.add(searchButton);
        searchDialog.add(cancelButton);

        searchDialog.setLocationRelativeTo(this);
        searchDialog.setVisible(true);
    }

    private void showFilterDialog() {
        JDialog filterDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Filter Items", true);
        filterDialog.setLayout(new GridLayout(4, 1, 10, 10));
        filterDialog.setSize(300, 200);

        JLabel filterLabel = new JLabel("Filter By Stock Quantity:");
        JRadioButton inStockButton = new JRadioButton("In-Stock Items (Quantity > 0)");
        JRadioButton outOfStockButton = new JRadioButton("Out-of-Stock Items (Quantity = 0)");
        JRadioButton allItemsButton = new JRadioButton("All Items");

        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(inStockButton);
        filterGroup.add(outOfStockButton);
        filterGroup.add(allItemsButton);
        allItemsButton.setSelected(true);

        inStockButton.addActionListener(e -> {
            filterTableByStockQuantity("in-stock");
            filterDialog.dispose();
        });
        outOfStockButton.addActionListener(e -> {
            filterTableByStockQuantity("out-of-stock");
            filterDialog.dispose();
        });
        allItemsButton.addActionListener(e -> {
            filterTableByStockQuantity("all");
            filterDialog.dispose();
        });

        filterDialog.add(filterLabel);
        filterDialog.add(inStockButton);
        filterDialog.add(outOfStockButton);
        filterDialog.add(allItemsButton);

        filterDialog.setLocationRelativeTo(this);
        filterDialog.setVisible(true);
    }

    private void filterTable(String searchTerm, String searchCriteria) {
        try {
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchTerm, getColumnIndex(searchCriteria));
            sorter.setRowFilter(rowFilter);
            inventoryTable.revalidate();
            inventoryTable.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying filter: " + e.getMessage());
        }
    }

    private void filterTableByStockQuantity(String filterType) {
        try {
            RowFilter<DefaultTableModel, Object> rowFilter = null;
            switch (filterType) {
                case "in-stock":
                    rowFilter = new RowFilter<DefaultTableModel, Object>() {
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                            int stockQuantity = (Integer) entry.getValue(4);
                            return stockQuantity > 0;
                        }
                    };
                    break;
                case "out-of-stock":
                    rowFilter = new RowFilter<DefaultTableModel, Object>() {
                        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                            int stockQuantity = (Integer) entry.getValue(4);
                            return stockQuantity == 0;
                        }
                    };
                    break;
                case "all":
                    rowFilter = null;
                    break;
            }
            sorter.setRowFilter(rowFilter);
            inventoryTable.revalidate();
            inventoryTable.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying filter: " + e.getMessage());
        }
    }

    private void showAllData() {
        // Clear the filter to show all data
        sorter.setRowFilter(null);

        // Ensure the table is updated on the EDT
        SwingUtilities.invokeLater(() -> {
            inventoryTable.revalidate();
            inventoryTable.repaint();
        });
    }

    private int getColumnIndex(String searchCriteria) {
        switch (searchCriteria) {
            case "ID": return 0;
            case "Name": return 1;
            case "Description": return 2;
            default: return -1;
        }
    }
}