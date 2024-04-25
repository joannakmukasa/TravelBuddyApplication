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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joann
 */

// generates unique ID for the user ID and trip ID

public class IdGenerator {
    public static void main(String[] args){
        IdGenerator id = new IdGenerator();

       try {
           id.parseID(id.getRandomUUID());
       } catch (IOException ex) {
           Logger.getLogger(IdGenerator.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
       }

   }
   public void ID() throws IOException{
       parseID(getRandomUUID());
   }
   public String getRandomUUID() throws IOException{
       String request = "{\n" +
       "    \"jsonrpc\": \"2.0\",\n" +
       "    \"method\": \"generateUUIDs\",\n" +
       "    \"params\": {\n" +
       "        \"apiKey\": \"00000000-0000-0000-0000-000000000000\",\n" +
       "        \"n\": 1\n" +
       "    },\n" +
       "    \"id\": 15998\n" +
       "}";
       URL url = new URL("https://api.random.org/json-rpc/4/invoke");
       HttpURLConnection con = (HttpURLConnection) url.openConnection();
       con.setRequestMethod("POST");
       con.setRequestProperty("Accept","application/json");
       con.setRequestProperty("Content-Type","application/json");
       con.setDoOutput(true);


       OutputStream os = con.getOutputStream();
       OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
       osw.write(request);
       osw.flush();
       osw.close();
       os.close();

       con.connect();

       BufferedReader in = new BufferedReader (new InputStreamReader(con.getInputStream()));
       String response = " ";    
       String line = in.readLine();

       while( line != null )
           {
               response += line + "\r\n";
               line = in.readLine();
           }

   return response;

   }

   public String parseID(String line){
       GsonBuilder builder = new GsonBuilder();
       builder.setPrettyPrinting();
       Gson gson = builder.create();
       JsonObject obj = gson.fromJson(line, JsonObject.class)
               .getAsJsonObject("result").getAsJsonObject("random");
       JsonArray arr = obj.getAsJsonArray("data");
       String ID = arr.getAsString();
//        System.out.println("ID: " + ID);

       return ID;
   }
}
