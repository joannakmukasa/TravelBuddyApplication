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
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author joann
 */
public class Trips {
    String city, country, startdate, enddate, weather, tripID;
    
    public String getCountry() {
            return country;
        }

        public String getEnddate() {
            return enddate;
        }

        public String getCity() {
            return city;
        }

        public String getTripID() {
            return tripID;
        }

        public String getStartdate() {
            return startdate;
        }

        public String getWeather() {
            return weather;
        }
    
    // function to propose a trip
    public void addTrip(){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("City: ");
            city = sc.nextLine();
            System.out.print("Country: ");
            country = sc.nextLine();
            System.out.print("Start Date: ");
            startdate = sc.nextLine();
            System.out.print("End Date: ");
            enddate = sc.nextLine();
            
            Map<String, String> params = new HashMap<>();
            params.put("city", city);
            params.put("country", country);
            params.put("startdate", startdate);
            params.put("enddate", enddate);
            
            String ParamsToString = params.entrySet().stream()
                    .map(e->e.getKey()+"="+e.getValue())
                    .collect(Collectors.joining("&"));
            
            URL url3 = new URL("http://localhost:8080/Orchestrator/webresources/trip/newTrip?"+ParamsToString);
            HttpURLConnection con = (HttpURLConnection) url3.openConnection();
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
        } catch (IOException ex) {
            Logger.getLogger(Trips.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // function to view available trips and express interest
    public void viewTrips(){
        Scanner sc = new Scanner(System.in);
        try {
            URL url4 = new URL("http://localhost:8080/Orchestrator/webresources/trip/viewTrips");
            HttpURLConnection con = (HttpURLConnection) url4.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            String response = "";
            BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
            String line = in.readLine();

            while( line != null )
                {
                    response += line + "\r\n";
                    line = in.readLine();
                }
            
            //System.out.println(response);
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            int i=1;
            Type tripListType = new TypeToken<List<Trips>>(){}.getType();
            List<Trips> trips = gson.fromJson(response, tripListType);
            for (Trips trip : trips) {
                
                city = trip.getCity();
                country = trip.getCountry();
                startdate = trip.getStartdate();
                enddate = trip.getEnddate();
                weather = trip.getWeather();
                tripID = trip.getTripID();
                
                System.out.println("Trip "+ i);
                
                System.out.println("City: " + trip.getCity());
                System.out.println("Country: " + trip.getCountry());
                System.out.println("Start Date: " + trip.getStartdate());
                System.out.println("End Date: " + trip.getEnddate());
                System.out.println("Weather: " + trip.getWeather());
                System.out.println("Trip ID: " + trip.getTripID());
                System.out.println();
                i++;
            }
            System.out.println("Are you interested in any of the above trips?");
            System.out.println("1. yes");
            System.out.println("2. No");
            System.out.println("Choice: ");
            int choice;
            int Tchoice;
            choice = sc.nextInt();
            System.out.println("");
            if (choice == 1) {
                System.out.println("which trip are you interested in?");
                System.out.print("Trip: ");
                Tchoice = sc.nextInt();
                Trips chosenTrip = trips.get(Tchoice);
                System.out.println("Please confirm that you would like to show you interest in attending the following trip");
                System.out.println("City: " + chosenTrip.getCity());
                System.out.println("Country: " + chosenTrip.getCountry());
                System.out.println("Start Date: " + chosenTrip.getStartdate());
                System.out.println("End Date: " + chosenTrip.getEnddate());
                System.out.println("Weather: " + chosenTrip.getWeather());
                System.out.println("");
            }
            else {
                Client c = new Client();
                c.menu();
            }
//            int ch = 2;
//            Trips chosenTrip = trips.get(ch);
//            System.out.println("Trip "+ ch + ": City: "+ chosenTrip.getCity());
                      
            
        } catch (IOException ex) {
            Logger.getLogger(Trips.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // function to search for available trips based on the city and country
    public void queryTrips(){
        Scanner sc = new Scanner(System.in);
        System.out.println("To search for trips in a specific location please input which city and country you would like to view.");
        System.out.print("City: ");
        city = sc.nextLine();
        System.out.print("Country: ");
        country = sc.nextLine();
        
        String ParamsToString = "city=" + city
                    + "&country=" + country;
        try {
            URL url4 = new URL("http://localhost:8080/Orchestrator/webresources/trip/searchTrips?" + ParamsToString);
            HttpURLConnection con = (HttpURLConnection) url4.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            String response = "";
            BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
            String line = in.readLine();

            while( line != null )
                {
                    response += line + "\r\n";
                    line = in.readLine();
                }
            
            //System.out.println(response);
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            int i=1;
            Type tripListType = new TypeToken<List<Trips>>(){}.getType();
            List<Trips> trips = gson.fromJson(response, tripListType);
            for (Trips trip : trips) {
                
                city = trip.getCity();
                country = trip.getCountry();
                startdate = trip.getStartdate();
                enddate = trip.getEnddate();
                weather = trip.getWeather();
                tripID = trip.getTripID();
                
                System.out.println("Trip "+ i);
                
                System.out.println("City: " + trip.getCity());
                System.out.println("Country: " + trip.getCountry());
                System.out.println("Start Date: " + trip.getStartdate());
                System.out.println("End Date: " + trip.getEnddate());
                System.out.println("Weather: " + trip.getWeather());
                System.out.println("Trip ID: " + trip.getTripID());
                System.out.println();
                i++;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Trips.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to Search");
            Client c = new Client();
                c.menu();
        }
    }
}
