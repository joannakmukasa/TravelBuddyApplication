/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author joann
 */
public class Weather {
    public static void main(String[] args){
        
//        Weather w = new Weather();
//        try {
//            w.weather("london");
//            
//        } catch (IOException ex) {
//            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public String weather(String city) throws IOException{
         // get geocode 
        URL url = new URL("https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        
        String GeoResponse = "";
        
        BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
            String line = in.readLine();

            while( line != null )
                {
                    GeoResponse += line + "\r\n";
                    line = in.readLine();
                }
        
        System.out.print(GeoResponse);
        
        // parsing the parameters to get the latitude and longitude
        
       GsonBuilder builder = new GsonBuilder();
       builder.setPrettyPrinting();
       Gson gson = builder.create();
       JsonArray results = gson.fromJson(GeoResponse, JsonObject.class).getAsJsonArray("results");
       JsonObject obj1 = results.get(0).getAsJsonObject();
       String lat = obj1.get("latitude").getAsString();
       String lon = obj1.get("longitude").getAsString();
       
       // saving params to 2 dp
       
       lat = saveto2dp(lat);
       lon = saveto2dp(lon);
       
       System.out.println("Latitude = " + lat);
       System.out.println("Longitude = " + lon);
       Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("latitude", lat);
        parameters1.put("longitude", lon);
        // Convert parameters to String
        
        
        String start = "2023-11-11";
        String end = "2023-11-21";
        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("start_date", start); // format (yyyy-mm-dd)
        parameters2.put("end_date", end); // format (yyyy-mm-dd)
        //remove from map and just leave out
        
//        String convertedParamsToString1 = "longitude=" + lon + "," + "latitude=" + lat;
//        String convertedParamsToString2 = "start_date=" + start + "," + "end_date=" + end;
        String convertedParamsToString1 = parameters1.entrySet().stream()
                .map(e->e.getKey()+"="+e.getValue())
                .collect(Collectors.joining("&"));
        
        String convertedParamsToString2 = parameters2.entrySet().stream()
                .map(e->e.getKey()+"="+e.getValue())
                .collect(Collectors.joining("&"));

       
        URL url2 = new URL("https://api.open-meteo.com/v1/forecast?"+convertedParamsToString1+"&daily=temperature_2m_max,temperature_2m_min,uv_index_max&forecast_days=1");
        
        HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
        con2.setRequestMethod("GET");
        con2.connect();
        
        // Get the response
        String response = "";
        
        BufferedReader in2 = new BufferedReader (new InputStreamReader(con2.getInputStream()));
            String line2 = in2.readLine();

            while( line2 != null )
                {
                    response += line2 + "\r\n";
                    line2 = in2.readLine();
                }
        
        System.out.print(response);
        
        Map<String, Object> data = gson.fromJson(response, Map.class);
        Map<String, Object> daily = (Map<String, Object>) data.get("daily");

        Object min = daily.get("temperature_2m_min");
        Object max = daily.get("temperature_2m_max");
        Object uvindex = daily.get("uv_index_max");
        
        
        String forecast = "Minimum Temperature: "+ min.toString() + "\n" + "Maximum Temperature: " + max.toString() + "\n" + "UV Index: " + uvindex.toString();
        
        System.out.println(forecast);
        return forecast;
    }
    public String saveto2dp(String s){
        double d = Double.parseDouble(s);
        
        s = String.format("%.2f", d);
        System.out.print(s);
        return s;
    }
}
