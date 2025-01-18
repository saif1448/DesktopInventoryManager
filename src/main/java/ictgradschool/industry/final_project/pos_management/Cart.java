package ictgradschool.industry.final_project.pos_management;



import ictgradschool.industry.final_project.inventory_management.InventoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the cart. If the item already exists, increments its quantity.
     *
     * @param item The item to add.
     */
    public void addItem(InventoryItem item) {
        // Check if the item already exists in the cart
        for (CartItem cartItem : items) {
            if (cartItem.getItem().getId().equals(item.getId())) {
                // Increment the quantity if the item is already in the cart
                cartItem.incrementQuantity();
                return;
            }
        }

        // If the item is not in the cart, add it with a quantity of 1
        items.add(new CartItem(item, 1));
    }

    /**
     * Removes one instance of an item from the cart. If the quantity drops to 0, the item is removed entirely.
     *
     * @param itemId The ID of the item to remove.
     */
    public void removeItem(String itemId) {
        for (CartItem cartItem : items) {
            if (cartItem.getItem().getId().equals(itemId)) {
                // Decrement the quantity
                cartItem.decrementQuantity();

                // If the quantity drops to 0, remove the item from the cart
                if (cartItem.getQuantity() == 0) {
                    items.remove(cartItem);
                }
                return;
            }
        }
    }

    /**
     * Gets the list of items in the cart.
     *
     * @return The list of items in the cart.
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Calculates the total cost of all items in the cart.
     *
     * @return The total cost.
     */
    public double getTotalCost() {
        double total = 0.0;
        for (CartItem cartItem : items) {
            total += cartItem.getItem().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Represents an item in the cart with its quantity.
     */
    public static class CartItem {
        private InventoryItem item;
        private int quantity;

        public CartItem(InventoryItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public InventoryItem getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void incrementQuantity() {
            quantity++;
        }

        public void decrementQuantity() {
            if (quantity > 0) {
                quantity--;
            }
        }
    }
}