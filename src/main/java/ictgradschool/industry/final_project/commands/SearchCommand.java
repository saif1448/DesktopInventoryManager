package ictgradschool.industry.final_project.commands;

import ictgradschool.industry.final_project.interfaces.Command;
import ictgradschool.industry.final_project.inventory_management.InventoryTable;

public class SearchCommand implements Command {
    private InventoryTable inventoryTable;
    private String searchTerm;
    private String searchCriteria;

    public SearchCommand(InventoryTable inventoryTable, String searchTerm, String searchCriteria) {
        this.inventoryTable = inventoryTable;
        this.searchTerm = searchTerm;
        this.searchCriteria = searchCriteria;
    }

    @Override
    public void execute() {
        inventoryTable.filterTable(searchTerm, searchCriteria);
    }
}