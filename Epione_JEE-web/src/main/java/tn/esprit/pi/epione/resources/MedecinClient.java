package tn.esprit.pi.epione.resources;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.User;

@Path("/medecin")
public class MedecinClient {
	@Inject
	UserServiceLocal userManager;

	
	/******************************** add pattern **********************************************/
	@Path("/addPattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPattern (
			@QueryParam("medecin") int medecin,
			@QueryParam("pattern") String pattern,
			@QueryParam("price") int price,
			@QueryParam("periode") int periode
			)
	{
	
		
		Pattern patterns = new Pattern();
		Doctor x = (Doctor) userManager.getUserByid(medecin);
		patterns.setDoctor(x);
		patterns.setLabel(pattern);
		patterns.setPrice(price);
		return Response.ok(userManager.AddPattern(x,pattern,price,periode)).build();
		
		
	}
}
