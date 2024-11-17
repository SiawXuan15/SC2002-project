package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class orderRequestManager {

    private final String orderRequestFilePath;

    // Constructor
    public orderRequestManager(String orderRequestFilePath) {
        this.orderRequestFilePath = orderRequestFilePath;
    }

    // Method to add a replenishment request
    public void addReplenishmentRequest(String medID, String medicationName, int quantity) throws IOException {
        // Retrieve all existing requests
        List<String[]> existingRequests = CSVReader.readCSV(orderRequestFilePath);

        // Determine the next Request ID
        int maxID = 0;
        for (String[] request : existingRequests) {
            try {
                maxID = Math.max(maxID, Integer.parseInt(request[0])); // Parse numeric Request IDs
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }

        // Generate the new Request ID
        String newRequestID = String.valueOf(maxID + 1);
        String status = "Pending";

        // Create the new request record
        String[] newRequest = {newRequestID, medID, medicationName, String.valueOf(quantity), status};

        // Append the new request to the CSV file
        CSVWriter.appendToCSV(orderRequestFilePath, newRequest);
        System.out.println("Replenishment request added successfully: " + String.join(", ", newRequest));
    }

    // Method to get a replenishment request by ID
    public String[] getReplenishmentRequestByID(String requestID) throws IOException {
        List<String[]> requests = CSVReader.readCSV(orderRequestFilePath);
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                return request;
            }
        }
        return null; // Return null if no matching request is found
    }

    // Method to update the status of a replenishment request
    public void updateReplenishmentRequestStatus(String requestID, String status) throws IOException {
        List<String[]> requests = CSVReader.readCSV(orderRequestFilePath);

        // Update status for the matching request
        boolean updated = false;
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                request[4] = status; // Update the status
                updated = true;
            }
        }

        if (updated) {
            // Write the updated list back to the file
            CSVWriter.writeCSV(orderRequestFilePath, requests);
            System.out.println("Replenishment request status updated successfully.");
        } else {
            System.out.println("Request ID not found: " + requestID);
        }
    }

    // Method to retrieve all order requests from the CSV
    public List<String[]> getOrderRequests() throws IOException {
        return CSVReader.readCSV(orderRequestFilePath);
    }

    // Method to retrieve all pending replenishment requests
    public List<String[]> getPendingReplenishmentRequests() throws IOException {
        List<String[]> pendingRequests = new ArrayList<>();
        List<String[]> allRequests = CSVReader.readCSV(orderRequestFilePath);

        for (String[] fields : allRequests) {
            if (fields[4].trim().equalsIgnoreCase("Pending")) {
                pendingRequests.add(fields);
            }
        }
        return pendingRequests;
    }
}
