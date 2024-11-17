package InventoryManagement;

public class OrderRequest {
    private final String medID; // Medication ID from the CSV
    private final int quantity; // Quantity for replenishment
    private String status; // Status of the order request

    // Constructor to initialize medID and quantity, with default status as "Pending"
    public OrderRequest(String medID, int quantity) {
        this.medID = medID;
        this.quantity = quantity;
        this.status = "Pending";
    }

    // Getter for Medication ID
    public String getMedID() {
        return medID;
    }

    // Getter for Quantity
    public int getQuantity() {
        return quantity;
    }

    // Getter for Status
    public String getStatus() {
        return status;
    }

    // Setter for Status
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // Display relevant information
        return "OrderRequest for Medication ID: " + medID +
               ", Quantity: " + quantity +
               ", Status: " + status;
    }
}
