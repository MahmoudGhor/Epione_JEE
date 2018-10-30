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

import tn.esprit.pi.epione.filters.Secured;
import tn.esprit.pi.epione.iservices.UserServiceLocal;
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
		if (user.getEmail() != null) {
			JsonObject jsonObject = userManager.SignIn(user.getEmail(), user.getPassword());
			if (jsonObject.containsKey("error")) {
				return Response.ok(jsonObject).build();
			} else {
				jsonObject = jsonObjectToBuilder(jsonObject).add("token", generateToken.issueToken(user.getEmail()))
						.build();
				Connected_User.setUser(user); // get the connected user
				return Response.ok(jsonObject).build();
			}
		} else {
			JsonObject jsonObject = userManager.SignIn(user.getUsername(), user.getPassword());
			if (jsonObject.containsKey("error")) {
				return Response.ok(jsonObject).build();
			} else {
				jsonObject = jsonObjectToBuilder(jsonObject)
						.add("token", generateToken.issueToken(user.getUsername())).build();
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

}
