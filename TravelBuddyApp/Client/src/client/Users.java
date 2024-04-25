/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author joann
 */
public class Users {
    private String firstname, lastname, Username, Password, Id;
    private static Map<String, String> userSessions = new HashMap<>();
    Scanner sc = new Scanner(System.in);

// signup function
    public void signUp(){
        try {
            
            System.out.print("First Name: ");
            firstname = sc.nextLine();
            System.out.print("Last Name: ");
            lastname = sc.nextLine();
            System.out.print("Username: ");
            Username = sc.nextLine();
            System.out.print("Password: ");
            Password = sc.nextLine();
            
            Map<String, String> parameters = new HashMap<>();
            parameters.put("firstname", firstname);
            parameters.put("Username", Username);
            parameters.put("lastname", lastname);
            parameters.put("Password", Password);
            
            String ParamsToString = "firstname="+ firstname 
                    + "&lastname=" + lastname 
                    + "&Username=" + Username 
                    + "&Password=" + Password;
            
            
            URL url1 = new URL("http://localhost:8080/Orchestrator/webresources/user/signup?"+ParamsToString);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("Content-Type","application/json");
            con.setDoOutput(true);
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                //System.out.println("Response from server: " + response.toString());
            }

            int responseCode = con.getResponseCode();
            //System.out.println("HTTP Response Code: " + responseCode);
        } catch (IOException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error occurred: " + ex.getMessage());
        }
    }
    
    // login function
    public void login(){
            
        try {
            System.out.print("Username: ");
            Username = sc.nextLine();
            System.out.print("Password: ");
            Password = sc.nextLine();

            String ParamsToString = "Username=" + Username
                    + "&Password=" + Password;


            URL url1 = new URL("http://localhost:8080/Orchestrator/webresources/user/login?"+ParamsToString);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("Content-Type","application/json");
            con.setDoOutput(true);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String responseString = response.toString();
                //System.out.println("Response from server: " + responseString);
                if(responseString == "true") {
                    String sessionToken = generateSessionToken();
                    saveUserDetails(Username, Password);
                }
            }

            int responseCode = con.getResponseCode();
            //System.out.println("HTTP Response Code: " + responseCode);
        }
        catch (IOException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error occurred: " + ex.getMessage());
        }
    }
    
    // function that saves the logged in users details in an instance
    public void saveUserDetails(String Username, String Password){
        UserManager um = new UserManager();
        String ParamsToString = "Username=" + Username
                    + "&Password=" + Password;
        try {
            URL url5 = new URL("http://localhost:8080/Orchestrator/webresources/user/login/details?"+ParamsToString);
            HttpURLConnection con = (HttpURLConnection) url5.openConnection();
            con.setRequestMethod("GET");
            String response = "";
            BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
            String line = in.readLine();

            while( line != null )
            {
                response += line + "\r\n";
                line = in.readLine();
            }
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            JsonArray results = gson.fromJson(response, JsonObject.class).getAsJsonArray("results");
            JsonObject user = results.get(0).getAsJsonObject();
            um.setID(user.get("id").getAsString());
            um.setfirstname(user.get("firstname").getAsString());
            um.setlastname(user.get("lastname").getAsString());
        } catch (IOException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // generates a session token to allow the users to stay logged in for the duration that they use the application
    private static String generateSessionToken() {
        return java.util.UUID.randomUUID().toString();
    }
    
}
        

