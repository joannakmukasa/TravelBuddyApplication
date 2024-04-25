/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joann
 */
public class UserManager {
    private String firstname, lastname, Username, Password, Id;
    private static Map<String, String> userSessions = new HashMap<>();
    
//    UserManager(String Id, String Username, String Password, String firstname, String lastname){
//        this.Id = Id;
//        this.Username = Username;
//        this.Password = Password;
//        this.firstname = firstname;
//        this.lastname = lastname;
//    }
    
    public String getId(){
        return Id;
    }
    
    public void setID(String Id) {
        this.Id = Id;
    }
    
    public String getUsername(){
        return Username;
    }
    
    public void setUsername(String Username){
        this.Username = Username;
    }
    
    public String getPassword(){
        return Password;
    }
    
    public void setPassword(String Password){
        this.Password = Password;
    }
    
    public String getfirstname(){
        return firstname;
    }
    
    public void setfirstname(String firstname){
        this.firstname = firstname;
    }
    
    public String getlastname(){
        return lastname;
    }
    
    public void setlastname(String lastname){
        this.lastname = lastname;
    }

    public static boolean login(String username, String password) {
        // Perform authentication (e.g., check username and password against a database)
        if (authenticate(username, password)) {
            String sessionToken = generateSessionToken();
            userSessions.put(username, sessionToken);
            System.out.println("Login successful. Session token: " + sessionToken);
            return true;
        } else {
            System.out.println("Login failed. Invalid credentials.");
            return false;
        }
    }

    public static void logout(String username) {
        if (userSessions.containsKey(username)) {
            userSessions.remove(username);
            System.out.println("Logout successful.");
        } else {
            System.out.println("User not found or not logged in.");
        }
    }

    private static boolean authenticate(String username, String password) {
        // Add your authentication logic here (e.g., check against a database)
        // For simplicity, this example always returns true.
        return true;
    }

    private static String generateSessionToken() {
        // You can use a more sophisticated method to generate session tokens
        return java.util.UUID.randomUUID().toString();
    }

    public static boolean isUserLoggedIn(String username) {
        return userSessions.containsKey(username);
    }

    public static String getSessionToken(String username) {
        return userSessions.get(username);
    }
}
