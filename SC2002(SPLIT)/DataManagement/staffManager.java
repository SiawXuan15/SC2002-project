package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;  

public class staffManager {
    private final String staffFilePath;

    public staffManager(String staffFilePath) {
        this.staffFilePath = staffFilePath;
    }

    public String[] getStaffByUserID(String userID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        for (String[] staff : staffList) {
            if (staff[1].equals(userID)) { 
                return staff;
            }
        }
        return null;
    }

    private String generateNextStaffID(List<String[]> staffList) {
        int maxID = 0;
        for (String[] staff : staffList) {
            String staffID = staff[0]; 
            if (staffID.startsWith("S")) {
                try {
                    int idNumber = Integer.parseInt(staffID.substring(1)); 
                    maxID = Math.max(maxID, idNumber); 
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        return "S" + String.format("%03d", maxID + 1); 
    }

    private String generateUserID(String rolePrefix, String filePath) throws IOException {
        List<String[]> records = CSVReader.readCSV(filePath);
        int maxNumber = 0;
        for (String[] record : records) {
            if (record.length > 0 && record[1].startsWith(rolePrefix)) {
                try {
                    int num = Integer.parseInt(record[1].substring(rolePrefix.length()));
                    maxNumber = Math.max(maxNumber, num);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return rolePrefix + String.format("%03d", maxNumber + 1);
    }

    private String getRolePrefix(String role) {
        switch (role.toLowerCase()) {
            case "doctor":
                return "D";
            case "administrator":
                return "A";
            case "pharmacist":
                return "P";
            default:
                return "U"; 
        }
    }

    public List<String[]> getStaffList() throws IOException {
        return CSVReader.readCSV(staffFilePath);
    }

    public void addStaff() throws IOException {
        Scanner sc = new Scanner(System.in);
        List<String[]> staffList = getStaffList();

        String nextStaffID = generateNextStaffID(staffList);

        System.out.println("Enter details for new staff.");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Role: ");
        String role = sc.nextLine().trim().toLowerCase(); 
        while (!role.equals("doctor") && !role.equals("patient") 
                && !role.equals("administrator") && !role.equals("pharmacist")) {
            System.out.print("Invalid role. Please enter one of the following: doctor, patient, administrator, pharmacists: ");
            role = sc.nextLine().trim().toLowerCase(); 
        }
        String rolePrefix = getRolePrefix(role);
        String userID = generateUserID(rolePrefix, staffFilePath);
        System.out.println("Generated User ID: " + userID);

        String gender;
        while (true) {
            System.out.print("Gender: ");
            gender = sc.nextLine();
            if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase(); 
                break;
            } else {
                System.out.println("Invalid Gender. Please enter Male or Female.");
            }
        }

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

        System.out.print("Specialization: ");
        String specialization = sc.nextLine();

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

        String email;
        while (true) {
            System.out.print("Email: ");
            email = sc.nextLine();
            if (email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) { 
                break;
            } else {
                System.out.println("Invalid Email. Please enter a valid email address.");
            }
        }

        String[] newStaff = {nextStaffID, userID, name, role, gender, age, specialization, contact, email};

        try {
            CSVWriter.appendToCSV(staffFilePath, newStaff);
            System.out.println("New staff added successfully with Staff ID: " + nextStaffID);
        } catch (IOException e) {
            System.err.println("Error adding new staff: " + e.getMessage());
        }
    }

    public boolean removeStaff(String userID) throws IOException {
        List<String[]> staffList = getStaffList(); 

        boolean removed = staffList.removeIf(staff -> staff[1].equals(userID)); 

        if (removed) {
            updateStaffList(staffList); 
        }

        return removed; 
    }

    public void updateStaffList(List<String[]> updatedStaff) throws IOException {
        CSVWriter.writeCSV(staffFilePath, updatedStaff);
    }

    public void updateStaffInfo(String userID, String[] updatedStaff) throws IOException {
        List<String[]> staffList = getStaffList();
        boolean found = false;

        for (int i = 0; i < staffList.size(); i++) {
            String[] staff = staffList.get(i);
            if (staff[1].equals(userID)) { 
                staffList.set(i, updatedStaff); 
                found = true;
                break;
            }
        }

        if (found) {
            updateStaffList(staffList);
        } else {
            System.out.println("Staff with UserID " + userID + " not found.");
        }
    }

    public List<String[]> filterStaff(String criterion, String value) throws IOException {
        List<String[]> staffList = getStaffList();
        List<String[]> filteredList = new ArrayList<>();


        for (String[] staff : staffList) {
            switch (criterion.toLowerCase()) {
                case "role":
                    if (staff[3].equalsIgnoreCase(value)) { 
                        filteredList.add(staff);
                    }
                    break;
                case "gender":
                    if (staff[4].equalsIgnoreCase(value)) { 
                        filteredList.add(staff);
                    }
                    break;
                case "age":
                    if (staff[5].equals(value)) { 
                        filteredList.add(staff);
                    }
                    break;
            }
        }
        return filteredList;
    }
    public List<String[]> filterStaffByMultipleCriteria(String role, String gender, String age) throws IOException {
        List<String[]> staffList = getStaffList();
        List<String[]> filteredList = new ArrayList<>();
    
        for (String[] staff : staffList) {
            if (staff.length >= 6) { // Ensure there are at least 6 elements
                boolean matchesRole = (role == null || role.isEmpty() || staff[3].equalsIgnoreCase(role)); 
                boolean matchesGender = (gender == null || gender.isEmpty() || staff[4].equalsIgnoreCase(gender)); 
                boolean matchesAge = (age == null || age.isEmpty() || staff[5].equals(age)); 
    
                if (matchesRole && matchesGender && matchesAge) {
                    filteredList.add(staff);
                }
            } else {
                System.err.println("Staff record does not have enough data: " + Arrays.toString(staff));
            }
        }
    
        return filteredList; // Add this line to return the filtered list
    }
    
}