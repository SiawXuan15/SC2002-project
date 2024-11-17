package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class staffManager {
    private final String staffFilePath;

    // Constructor
    public staffManager(String staffFilePath) {
        this.staffFilePath = staffFilePath;
    }

    public String[] getStaffByUserID(String userID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        for (String[] staff : staffList) {
            if (staff[1].equals(userID)) { // Assuming UserID is at index 1
                return staff;
            }
        }
        return null; // Staff not found
    }

    // Method to generate the next Staff ID
    private String generateNextStaffID(List<String[]> staffList) {
        int maxID = 0;
        for (String[] staff : staffList) {
            String staffID = staff[0]; // Assuming Staff ID is at index 0
            if (staffID.startsWith("S")) {
                try {
                    int idNumber = Integer.parseInt(staffID.substring(1)); // Extract numeric part
                    if (idNumber > maxID) {
                        maxID = idNumber; // Keep track of the largest ID
                    }
                } catch (NumberFormatException e) {
                    // Ignore any invalid ID formats
                }
            }
        }
        return "S" + String.format("%03d", maxID + 1); // Generate next ID (e.g., S005)
    }

    // Helper function to generate UserID
    private String generateUserID(String rolePrefix, String filePath) throws IOException {
        List<String[]> records = CSVReader.readCSV(filePath);
        int maxNumber = 0;
        for (String[] record : records) {
            if (record.length > 0 && record[1].startsWith(rolePrefix)) {
                try {
                    int num = Integer.parseInt(record[1].substring(rolePrefix.length()));
                    maxNumber = Math.max(maxNumber, num);
                } catch (NumberFormatException ignored) {
                    // Ignore invalid IDs
                }
            }
        }
        return rolePrefix + String.format("%03d", maxNumber + 1);
    }

    private String getRolePrefix(String role) {
        switch (role.toLowerCase()) {
            case "doctor": return "D";
            case "administrator": return "A";
            case "pharmacist": return "P";
            default: return "U"; // Default prefix for unrecognized roles
        }
    }

    public List<String[]> getStaffList() throws IOException {
        return CSVReader.readCSV(staffFilePath);
    }

    public void addStaff() throws IOException {
        Scanner sc = new Scanner(System.in);
        List<String[]> staffList = getStaffList();

        // Generate the next Staff ID
        String nextStaffID = generateNextStaffID(staffList);

        System.out.println("Enter details for new staff.");

        // Input Name
        System.out.print("Name: ");
        String name = sc.nextLine();

        // Input Role
        System.out.print("Role: ");
        String role = sc.nextLine();
        String rolePrefix = getRolePrefix(role);
        String userID = generateUserID(rolePrefix, staffFilePath);
        System.out.println("Generated User ID: " + userID);

        // Input and validate Gender
        String gender;
        while (true) {
            System.out.print("Gender: ");
            gender = sc.nextLine();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase(); // Capitalize
                break;
            } else {
                System.out.println("Invalid Gender. Please enter Male or Female.");
            }
        }

        // Input and validate Age
        String age;
        while (true) {
            System.out.print("Age: ");
            age = sc.nextLine();
            try {
                int ageInt = Integer.parseInt(age);
                if (ageInt >= 0 && ageInt <= 100) {
                    break;
                } else {
                    System.out.println("Invalid Age. It must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Age. Please enter a valid number.");
            }
        }

        // Input Specialization
        System.out.print("Specialization: ");
        String specialization = sc.nextLine();

        // Input and validate Contact
        String contact;
        while (true) {
            System.out.print("Contact (8 digits, starts with 8 or 9): ");
            contact = sc.nextLine();
            if (contact.matches("^[89]\\d{7}$")) {
                break;
            } else {
                System.out.println("Invalid Contact. It must start with 8 or 9 and have 8 digits.");
            }
        }

        // Input and validate Email
        String email;
        while (true) {
            System.out.print("Email: ");
            email = sc.nextLine();
            if (email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) { // Basic email validation regex
                break;
            } else {
                System.out.println("Invalid Email. Please enter a valid email address.");
            }
        }

        // Define newStaff array with email at the last index
        String[] newStaff = {nextStaffID, userID, name, role, gender, age, specialization, contact, email};

        try {
            CSVWriter.appendToCSV(staffFilePath, newStaff);
            System.out.println("New staff added successfully with Staff ID: " + nextStaffID);
        } catch (IOException e) {
            System.err.println("Error adding new staff: " + e.getMessage());
        }
    }

    public boolean removeStaff(String userID) throws IOException {
        List<String[]> staffList = getStaffList(); // Get the current list of staff

        // Try to remove the staff with the given userID
        boolean removed = staffList.removeIf(staff -> staff[1].equals(userID)); // Assuming UserID is at index 1

        if (removed) {
            // Update the CSV file to reflect the removal
            updateStaffList(staffList); // This method should write the updated list back to the CSV
        }

        return removed; // Return true if removal was successful, otherwise false
    }

    public void updateStaffList(List<String[]> updatedStaff) throws IOException {
        CSVWriter.writeCSV(staffFilePath, updatedStaff);
    }

    public void updateStaffInfo(String userID, String[] updatedStaff) throws IOException {
        List<String[]> staffList = getStaffList();
        boolean found = false;

        for (int i = 0; i < staffList.size(); i++) {
            String[] staff = staffList.get(i);
            if (staff[1].equals(userID)) { // Assuming UserID is at index 1
                staffList.set(i, updatedStaff); // Replace the old data with updated data
                found = true;
                break;
            }
        }

        if (found) {
            updateStaffList(staffList); // Write the updated list back to the CSV
        } else {
            System.out.println("Staff with UserID " + userID + " not found.");
        }
    }

    public List<String[]> filterStaff(String criterion, String value) throws IOException {
        List<String[]> staffList = getStaffList();
        List<String[]> filteredList = new ArrayList<>();

        // Assuming the indexes are:
        // Index 2: Role, Index 3: Gender, Index 4: Age
        for (String[] staff : staffList) {
            switch (criterion.toLowerCase()) {
                case "role":
                    if (staff[3].equalsIgnoreCase(value)) { // Assuming Role is at index 3
                        filteredList.add(staff);
                    }
                    break;
                case "gender":
                    if (staff[4].equalsIgnoreCase(value)) { // Assuming Gender is at index 4
                        filteredList.add(staff);
                    }
                    break;
                case "age":
                    if (staff[5].equals(value)) { // Assuming Age is at index 5
                        filteredList.add(staff);
                    }
                    break;
            }
        }
        return filteredList;
    }
}
