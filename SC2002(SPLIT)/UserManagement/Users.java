package UserManagement;

import DataManagement.userManager;
import java.io.IOException;

public abstract class Users {

    private final String userID;
    private String name;
    private String password;
    private String role;
    private String email;
    private int contactNumber;
    private final userManager userManager; 


        public Users(String userID, userManager userManager) throws IOException{
            this.userID = userID;
            this.userManager = userManager;
            this.name = userManager.getUserName(userID);
        }


    public boolean login(String userID, String password) {
        return this.userID.equals(userID) && this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    } 


    public void updateContactDetails(String email, int contactNumber) {
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public void logout() {
        System.out.println("User " + userID + " has logged out.");
    }

    public String getRole() {
        return this.role;
    }

    public boolean hasAccess(String requiredRole) {
        return this.role.equals(requiredRole);
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String newName){
        this.name = newName; 
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getUserID() {
        return this.userID;
    }

    public abstract void displayMenu();

    public int getContactNumber() {
        return this.contactNumber;
}

    public String getEmail() {
        return this.email;
    }

}
