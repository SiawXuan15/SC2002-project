package InventoryManagement;

public interface InventoryMonitor extends Inventory{     
     void submitReplenishmentRequest(String medID, int quantity);
}
