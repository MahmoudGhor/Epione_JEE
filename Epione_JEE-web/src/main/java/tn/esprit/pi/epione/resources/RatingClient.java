package tn.esprit.pi.epione.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.iservices.RateServiceLocal;
import tn.esprit.pi.epione.persistence.Rating;
	
@Path("/rate")
@RequestScoped
public class RatingClient {
	
	@EJB
	RateServiceLocal rateManager;
	public static List<Rating> empL = new ArrayList<Rating>();
	
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRate(Rating r){
		/*Rating r = new Rating();
		r.setRate(rate);
		r.setComment(comment);*/
		System.out.println("add succ "+r.getId() );
		return Response.ok(rateManager.AddRate(r)).build();
	}

}
