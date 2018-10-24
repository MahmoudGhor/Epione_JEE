package tn.esprit.pi.epione.resources;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	@EJB
	UserServiceLocal userManager;

	
	/******************************** add pattern **********************************************/
	@Path("/addPattern/{medecin}/{pattern}/{price}/{periode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPattern (
			@PathParam("medecin") int medecin,
			@PathParam("pattern") String pattern,
			@PathParam("price") int price,
			@PathParam("periode") int periode
			)
	{
	
		
		Pattern patterns = new Pattern();
		Doctor x = (Doctor) userManager.getUserByid(medecin);
		patterns.setDoctor(x);
		patterns.setLabel(pattern);
		patterns.setPrice(price);
		return Response.ok(userManager.addPattern(x,pattern,price,periode)).build();
		
		
	}
}
