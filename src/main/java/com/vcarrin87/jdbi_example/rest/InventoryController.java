package com.vcarrin87.jdbi_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbi_example.models.Inventory;
import com.vcarrin87.jdbi_example.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * This method retrieves the inventory.
     * Example of a GET request: /inventory/all
     */
    @GetMapping("/all-inventory")
    public List<Inventory> getInventory() {
        return inventoryService.getInventory();
    }
}
