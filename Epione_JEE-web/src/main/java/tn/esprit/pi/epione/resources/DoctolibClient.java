package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import tn.esprit.pi.epione.iservices.DoctolibServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;

@Path("/doctolib")
@RequestScoped
public class DoctolibClient {
	
	@EJB
	private DoctolibServiceLocal D;
	
	
	
	@Path("/doc")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Doctor getDoctors() {

		return D.get();
	}
}
