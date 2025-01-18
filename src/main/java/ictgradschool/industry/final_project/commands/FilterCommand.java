package ictgradschool.industry.final_project.commands;

import ictgradschool.industry.final_project.interfaces.Command;
import ictgradschool.industry.final_project.inventory_management.InventoryTable;

public class FilterCommand implements Command {
    private InventoryTable inventoryTable;
    private String filterType;

    public FilterCommand(InventoryTable inventoryTable, String filterType) {
        this.inventoryTable = inventoryTable;
        this.filterType = filterType;
    }

    @Override
    public void execute() {
        inventoryTable.filterTableByStockQuantity(filterType);
    }
}