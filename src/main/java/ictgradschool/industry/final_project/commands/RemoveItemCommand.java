package ictgradschool.industry.final_project.commands;

import ictgradschool.industry.final_project.interfaces.Command;
import ictgradschool.industry.final_project.inventory_management.Inventory;

public class RemoveItemCommand implements Command {
    private Inventory inventory;
    private String itemId;

    public RemoveItemCommand(Inventory inventory, String itemId) {
        this.inventory = inventory;
        this.itemId = itemId;
    }

    @Override
    public void execute() {
        inventory.removeItem(itemId); // This will trigger the Observer Pattern
    }
}