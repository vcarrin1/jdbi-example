package com.vcarrin87.jdbi_example.services;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.Inventory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private Jdbi jdbi;

    /**
     * Get inventory
     */
    public List<Inventory> getInventory() {
        log.info("Retrieving inventory");
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_INVENTORY)
                  .mapTo(Inventory.class)
                  .list()
        );
    }

    /**
     * Update the stock level of a product.
     */
    public void updateInventory(int productId, int delta) {
        log.info("Updating inventory for product ID: {}, new stock level: {}", productId, delta);
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.UPDATE_INVENTORY)
                  .bind("product_id", productId)
                  .bind("delta", delta)
                  .execute()
        );
    }
}
