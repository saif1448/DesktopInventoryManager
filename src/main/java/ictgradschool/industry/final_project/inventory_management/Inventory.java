package ictgradschool.industry.final_project.inventory_management;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Inventory {

    private String fileStorePath; // Path to the file store
    private List<InventoryItem> items;

    public Inventory(String fileStorePath) {
        this.fileStorePath = fileStorePath;
        this.items = new ArrayList<>();
        loadInventory(); // Load inventory data from the file store
    }



    private void loadInventory() {
        File file = new File(fileStorePath, "inventory_items.json");

        if (file.exists()) {
            // If the file exists, load the inventory data
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Type itemListType = new TypeToken<ArrayList<InventoryItem>>() {}.getType();
                items = gson.fromJson(reader, itemListType);

                // If the file is empty or invalid, initialize an empty list
                if (items == null) {
                    items = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("Error loading inventory: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error loading inventory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // If the file doesn't exist, create a new one
            try {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("New inventory file created: " + file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "No existing inventory file found. A new one has been created.");
                } else {
                    System.err.println("Failed to create inventory file.");
                }
            } catch (IOException e) {
                System.err.println("Error creating inventory file: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error loading inventory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    public List<InventoryItem> getItems() {
        return items;
    }

    public void addItem(InventoryItem newItem) {
        items.add(newItem);
        saveInventory();
    }

    public InventoryItem getItemById(String id) {
        for (InventoryItem item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null; // Return null if the item is not found
    }


    public void saveInventory() {
        File file = new File(fileStorePath, "inventory_items.json");

        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(items, writer);
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error saving inventory", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeItem(String id) {
        // Remove the item with the specified ID from the list
        items.removeIf(item -> item.getId().equals(id));
        saveInventory(); // Save the updated inventory to the file
    }

    public void updateItem(String id, InventoryItem updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                items.set(i, updatedItem);
                saveInventory(); // Save the updated inventory to the file
                break;
            }
        }
    }
}
