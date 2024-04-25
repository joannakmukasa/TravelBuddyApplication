/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author joann
 */
@Path("/trip")
public class TripsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TripsResource
     */
    public TripsResource() {
    }

    /**
     * Retrieves representation of an instance of src.TripsResource
     * @return an instance of java.lang.String
     */
    // retrieves trips from the database
    @Path("/viewTrips")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTrip() throws SQLException, ClassNotFoundException{
        String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(DBurl);
        Statement st = con.createStatement();
        List<Map<String, String>> tripList = new ArrayList<>();

        try{
            String query ="SELECT city, country, startDate, endDate, weather, tripID FROM trips";
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                Map<String, String> tripinfo = new HashMap<>();
                String city = (String) rs.getObject("city");
                String country = (String) rs.getObject("country");
                String startDate = (String) rs.getObject("startDate");
                String endDate = (String) rs.getObject("endDate");
                String weather = (String) rs.getObject("weather");
                String tripId = (String) rs.getObject("tripID");

//                Date date = rs.getDate("startDate");
//                date.toString();

                tripinfo.put("city", city);
                tripinfo.put("country", country);
                tripinfo.put("startdate", startDate);
                tripinfo.put("enddate", endDate);
                tripinfo.put("weather", weather);
                tripinfo.put("tripID", tripId);

                tripList.add(tripinfo);

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        con.close();

        Gson gson = new Gson();
        String json = gson.toJson(tripList);

        return json;
    }

    /**
     * POST method for creating an instance of TripResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public TripResource getTripResource(@PathParam("id") String id) {
        return TripResource.getInstance(id);
    }
    
    // adds new trip to the database witha unique ID and weather information for the location
    @POST 
    @Path("/newTrip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTrip(
            @QueryParam("city") String city, 
            @QueryParam("country") String country, 
            @QueryParam("startdate") String startDate,
            @QueryParam("enddate") String endDate) throws SQLException, ClassNotFoundException, IOException{
        String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(DBurl);
        Statement st = con.createStatement();
        IdGenerator randomN = new IdGenerator();
        String id = randomN.parseID(randomN.getRandomUUID());
        Weather w = new Weather();
        String weather = w.weather(city);
        String query =
                "INSERT INTO trips (tripID, city, country, startDate, endDate, weather) VALUES ('"+id+"','"+city+"', '"+country+"','"+startDate+"','"+endDate+"', '"+weather+"')";// Add a new document with a generated ID
           
        st.execute(query);
        con.close();
    return Response.created(context.getAbsolutePath()).build();
    }
    
    // retreives trips according to the provided city and country
    @Path("/searchTrips")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String queryTrip(@QueryParam("city")String city, @QueryParam("country")String country) throws SQLException, ClassNotFoundException{
        String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<Map<String, String>> tripList = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(DBurl)) {
        
            try{
                
                String query ="SELECT city, country, startDate, endDate, weather, tripID FROM trips WHERE city = ? AND country = ?";
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, city);
                st.setString(2, country);
                
                ResultSet rs = st.executeQuery(query);
                
                while(rs.next()){
                    Map<String, String> tripinfo = new HashMap<>();
                    String City = (String) rs.getObject("city");
                    String Country = (String) rs.getObject("country");
                    String startDate = (String) rs.getObject("startDate");
                    String endDate = (String) rs.getObject("endDate");
                    String weather = (String) rs.getObject("weather");
                    String tripId = (String) rs.getObject("tripID");
                    
                    
                    
                    tripinfo.put("city", City);
                    tripinfo.put("country", Country);
                    tripinfo.put("startdate", startDate);
                    tripinfo.put("enddate", endDate);
                    tripinfo.put("weather", weather);
                    tripinfo.put("tripID", tripId);
                    
                    tripList.add(tripinfo);
                    
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(tripList);

        return json;
    }
}
