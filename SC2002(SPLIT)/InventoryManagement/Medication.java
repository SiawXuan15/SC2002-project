package InventoryManagement;

import java.time.LocalDate;

public class Medication {

    private String medicationID;
    private String medicationName;
    private int initialStock;
    private int lowStockAlert;
    private String dosageInformation;
    private LocalDate expiryDate;
    private String status;

    // Constructor to fit the CSV format
    public Medication(String medicationID, String medicationName, int initialStock, int lowStockAlert, String dosageInformation, LocalDate expiryDate) {
        this.medicationID = medicationID;
        this.medicationName = medicationName;
        this.initialStock = initialStock;
        this.lowStockAlert = lowStockAlert;
        this.dosageInformation = dosageInformation;
        this.expiryDate = expiryDate;
        this.status = "Available"; // Default status
    }


    // public String getMedicationDetails() {
    //     return "Medication ID: " + medicationID +
    //             "\nName: " + medicationName +
    //             "\nExpiration Date: " + expirationDate +
    //             "\nQuantity: " + quantity +
    //             "\nCategory: " + category +
    //             "\nSupplier: " + supplierInfo +
    //             "\nLow Stock Alert Level: " + lowStockAlert +
    //             "\nStatus: " + status;
    // }

    // public void updateQuantity(int newQuantity) {
    //     this.quantity = newQuantity;
    // }

    // public void updateStatus(String status) {
    //     this.status = status;
    // }

    // public boolean isExpired() {
    //     Date currentDate = new Date();
    //     return expirationDate != null && expirationDate.before(currentDate);
    // }

    // public void adjustQuantity(int change) {
    //     this.quantity += change;
    // }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

    // public boolean isBelowStockAlert() {
    //     return quantity <= lowStockAlert;
    // }

    // public String getMedicationID() {
    //     return medicationID;
    // }

    // public String getMedicationName() {
    //     return medicationName;
    // }

    // public int getQuantity() {
    //     return quantity;
    // }

    // public int getLowStockAlert() {
    //     return lowStockAlert;
    // }

    // public String getStatus() {
    //     return status;
    // }

    // public void setLowStockAlert(int lowStockAlert) {
    //     this.lowStockAlert = lowStockAlert;
    // }

}