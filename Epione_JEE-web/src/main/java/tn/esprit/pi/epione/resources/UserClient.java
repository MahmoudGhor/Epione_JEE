package tn.esprit.pi.epione.resources;

import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.pi.epione.filters.Secured;
import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Admin;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.User;
import tn.esprit.pi.epione.utils.GenerateToken;

@Path("/user")
@javax.faces.bean.RequestScoped
public class UserClient {

	@EJB
	UserServiceLocal userManager;

	/***************************** signIn ***********************************/
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(User user) {
		GenerateToken generateToken = new GenerateToken();
		User user1 = userManager.getUserByUserName(user.getUsername());
		if (user.getEmail() != null) {
			JsonObject jsonObject = userManager.SignIn(user.getEmail(), user.getPassword());
			if (jsonObject.containsKey("error")) {
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				jsonObject = jsonObjectToBuilder(jsonObject).add("token", generateToken.issueToken(user.getEmail()))
						.build();
				jsonObject = jsonObjectToBuilder(jsonObject).add("type", user.getType().toString()).build();
				Connected_User.setUser(user); // get the connected user
				return Response.ok(jsonObject).build();
			}
		} else {
			JsonObject jsonObject = userManager.SignIn(user.getUsername(), user.getPassword());
			if (jsonObject.containsKey("error")) {
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				jsonObject = jsonObjectToBuilder(jsonObject)
						.add("token", generateToken.issueToken(user.getUsername())).build();
				System.out.println("*************"+user1.getType()+"------------");
				
				jsonObject = jsonObjectToBuilder(jsonObject).add("type", user1.getType()).build();
				Connected_User.setUser(user); // get the connected user
				return Response.ok(jsonObject).build();
			}

		}
	}
	/************************** afficher le token  en json ****************************************/
	private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
		JsonObjectBuilder job = Json.createObjectBuilder();

		for (Entry<String, JsonValue> entry : jo.entrySet()) {
			if (entry != null && entry.getKey() != null && entry.getValue() != null) {
				job.add(entry.getKey(), entry.getValue());
			}
		}

		return job;
	}
	
	
	/*************************** LogOut   ********************************************************/
	@Path("/logout/{idUser}")
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Response logOut(@PathParam("idUser") int idUser)
	{
		return Response.ok(userManager.logOut(idUser)).build();
		
	}
	
	/*************************** Update Doctor    ********************************************************/
	@Path("/updateDoctor")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDoctor(Doctor doctor)
	{
		return Response.ok(userManager.updateDoctor(doctor)).build();
		
	}
	
	/*************************** Update Patient    ********************************************************/
	@Path("/updatePatient")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePatient(Patient patient)
	{
		return Response.ok(userManager.updatePatient(patient)).build();
		
	}
	
	/*************************** Update Admin    ********************************************************/
	@Path("/updateAdmin")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDoctor(Admin admin)
	{
		return Response.ok(userManager.updateAdmin(admin)).build();
		
	}
	/*************************** find user By id **************************************************/
	@Path("/getUser/{user}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUserName(@PathParam("user")String username)
	{
		return Response.ok(userManager.getUserByUserName(username)).build();
	}
	/*************************** find user By id **************************************************/
	@Path("/getUserID/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUserName(@PathParam("userId")int useriD)
	{
		return Response.ok(userManager.getUserByid(useriD)).build();
	}

	/***************************** get current date *****************************************/
	@Path("/getCurrentDate")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentDate()
	{
		return Response.ok().entity(userManager.getCurrentDate()).build();
	}
}
