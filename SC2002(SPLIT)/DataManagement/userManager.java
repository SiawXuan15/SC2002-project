package DataManagement;

import java.io.IOException;
import java.util.List;

public class userManager {
    private final String userFilePath;
    private final CSVReader csvReader;
    private final CSVWriter csvWriter;

    // Constructor
    public userManager(String userFilePath, CSVReader csvReader, CSVWriter csvWriter) {
        this.userFilePath = userFilePath;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
    }

    public String[] getUserById(String userID) throws IOException {
        List<String[]> users = csvReader.readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(userID)) { // Assuming UserID is in the first column
                return user;
            }
        }
        return null; // User not found
    }

    public String getUserName(String userID) throws IOException {
        List<String[]> users = csvReader.readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equalsIgnoreCase(userID.trim())) {  // Trim and compare case-insensitively
                return user[1].trim();  // Trim the name to remove any leading/trailing spaces
            }
        }
        return null;  // Return null if no match is found
    }

    // Verify login credentials
    public boolean login(String userID, String password) throws IOException {
        String[] user = getUserById(userID);
        return user != null && user[2].equals(password); // Assuming password is at index 2
    }

    // Update user password
    public boolean updateUserPassword(String userID, String newPassword) throws IOException {
        List<String[]> users = csvReader.readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equals(userID.trim())) {
                user[2] = newPassword; // Assuming password is at index 2
                csvWriter.writeCSV(userFilePath, users);
                return true;
            }
        }
        return false; // User not found
    }

    // Check if user exists
    public boolean isUserExists(String userID) throws IOException {
        return getUserById(userID) != null;
    }

    public List<String[]> getUsersList() throws IOException {
        return csvReader.readCSV(userFilePath);
    }

    public void addUser(String[] newUser) throws IOException {
        csvWriter.appendToCSV(userFilePath, newUser);
    }

    public void updateUsersList(List<String[]> updatedUsers) throws IOException {
        csvWriter.writeCSV(userFilePath, updatedUsers);
    }

    public String getRoleByUserID(String userID) throws IOException {
        List<String[]> users = csvReader.readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equalsIgnoreCase(userID.trim())) { // Assuming UserID is at index 0
                return user[3].trim(); // Assuming Role is at index 3
            }
        }
        return null; // Return null if user not found
    }
}
