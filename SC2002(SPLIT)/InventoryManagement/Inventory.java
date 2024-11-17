package InventoryManagement;


public interface Inventory {
    void getMedicationList(); // view inventory for pharmacy and admin
    //List<String[]> checkLowStock();
}

// package PharmacyManagement;
// import InventoryManagement.OrderRequest;
// import java.util.ArrayList;
// import java.util.List;

// public class Inventory {

//     private final List<Medication> medicationList;
//     private List<OrderRequest> orderRequests = new ArrayList<>();
//     private final int reorderThreshold;

//     public Inventory(int reorderThreshold) {
//     this.medicationList = new ArrayList<>(); 
//     this.reorderThreshold = reorderThreshold; 
//     }

//     public Inventory() {
//         this.medicationList = new ArrayList<>(); 
//         this.reorderThreshold = 10; 
//     }
// }

//     @Override
//     public void addMedication(Medication newMedication) {
//         medicationList.add(newMedication);
//     }

//     @Override
//     public void removeMedication(String medicationID) {
//         medicationList.removeIf(medication -> medication.getMedicationID().equals(medicationID));
//     }

//     @Override
//     public void updateStockLevel(String medicationID, int newQuantity) {
//         for (Medication medication : medicationList) {
//         if (medication.getMedicationID().equals(medicationID)) {
//             medication.updateQuantity(newQuantity);
//             return;
//         }
//     }
//     System.out.println("Medication with ID " + medicationID + " not found.");
//     }

//     @Override
//     public List<Medication> checkLowStock() {
//            List<Medication> lowStockMedications = new ArrayList<>();
//         for (Medication medication : medicationList) {
//             if (medication.getQuantity() <= reorderThreshold) {
//                 lowStockMedications.add(medication);
//             }
//         }
//         return lowStockMedications;
//     }

//     @Override
//     public OrderRequest createOrderRequest(Medication medication, int quantity) {
//         if (medication.getQuantity() <= reorderThreshold) {
//         return new OrderRequest(medication, quantity); // set status to pending
//         } else {
//             System.out.println("Stock level is sufficient, no need for reorder.");
//             return null;
//         }
//     }

//     @Override
//     public List<OrderRequest> getOrderRequests() {
//         return orderRequests;
//     }

//     @Override
//     public void removeOrderRequest(OrderRequest request) {
//         orderRequests.remove(request);
//     }



//     @Override
//         public List<Medication> getMedicationList() {
//         return medicationList;
//     }

//     @Override
//     public Medication getMedicationDetails(String medicationID) {
//     for (Medication medication : medicationList) {
//         if (medication.getMedicationID().equals(medicationID)) {
//             return medication;
//         }
//     }
//     System.out.println("Medication with ID " + medicationID + " not found.");
//     return null; // or alternatively, throw an exception if preferred
// }

// }

