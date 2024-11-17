package InventoryManagement;

import java.util.Scanner; 

public interface InventoryEditor extends Inventory { 
    void updateStockLevel(Scanner sc);
    void updateLowStockLevel(Scanner sc);
    void viewPendingRequests();
    void approveRequest(String requestID);   
}
