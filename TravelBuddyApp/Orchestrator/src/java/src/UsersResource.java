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
import java.util.HashMap;
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
@Path("/user")
public class UsersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    /**
     * Retrieves representation of an instance of src.UsersResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * POST method for creating an instance of UserResource
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
    public UserResource getUserResource(@PathParam("id") String id) {
        return UserResource.getInstance(id);
    }
    // create new user
    @Path("/signup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(@QueryParam("firstname")String fname,@QueryParam("lastname")String lname, @QueryParam("Username")String Username, @QueryParam("Password")String Password) throws ClassNotFoundException, SQLException, IOException {
        String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";
           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection con = DriverManager.getConnection(DBurl);
           Statement st = con.createStatement();
           IdGenerator randomN = new IdGenerator();
           String id = randomN.parseID(randomN.getRandomUUID());
           String query ="INSERT INTO users (userID,firstname,lastname,username,password) VALUES ('"+id+"','"+fname+"','"+lname+"', '"+Username+"','"+Password+"')";// Add a new document with a generated ID
           
            st.execute(query);
            con.close();
        return Response.created(context.getAbsolutePath()).build();
    }
    
    // checks users information against the database and responds accordingly
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)    
    @Produces(MediaType.APPLICATION_JSON)
    public String checkLogin(@QueryParam("Username") String username, @QueryParam("Password") String password) {
    String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";

    try (Connection con = DriverManager.getConnection(DBurl);
         PreparedStatement st = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

        st.setString(1, username);
        st.setString(2, password);
        
        Gson gson = new Gson();
        

        try (ResultSet result = st.executeQuery()) {
            return gson.toJson(result.next());  // If there's a matching user, return true
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return "failed";  // Return false for any exceptions or if no matching user is found
    }
    
    // retrieves the details of the user who has successfully logged in
    @Path("/login/details")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserDetails(@QueryParam("Username") String username, @QueryParam("Password") String password) throws ClassNotFoundException {
        String DBurl = "jdbc:mysql://u5ffca4jtg6kzmxm:QfbiksMFzk4YxEgwWBwU@bcnlwr2ckx6oe8yj7bb1-mysql.services.clever-cloud.com:3306/bcnlwr2ckx6oe8yj7bb1";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Map<String, String> userDetails = new HashMap<>();
        try (Connection con = DriverManager.getConnection(DBurl);
         PreparedStatement st = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

        st.setString(1, username);
        st.setString(2, password);
        
        Gson gson = new Gson();
        
        try{
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                String id = (String) rs.getObject("UserID");
                String firstname = (String) rs.getObject("firstname");
                String lastname = (String) rs.getObject("lastname");
                
                userDetails.put("id", id);
                userDetails.put("firstname", firstname);
                userDetails.put("lastname", lastname);
            }
        }
        catch(SQLException e){
        e.printStackTrace();
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        
        return json;
    }
    
}
