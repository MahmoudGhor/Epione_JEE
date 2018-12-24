package tn.esprit.pi.epione.resources;



import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Patient;


@Path("/patient")
@RequestScoped
public class PatientClient {
	@EJB
	UserServiceLocal userManager;
	
	
	
	
	
	/******************************  sign up patient ******************************************/
	@Path("/addPatient")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUpPatient(Patient patient)
	{
		return Response.ok(userManager.signUpPatient(patient)).build();
		
	}
	
	
	
	/****************************** enable account patient ********************************/
	@Path("/enable/{username}/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response enableUser(@PathParam("username") String username , @PathParam("token") String token)
	{
		return Response.ok(userManager.enableUser(username , token)).build();
	}
	
	/******************* get info patient *****************************************/
	@Path("/getPatientById/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatientById(@PathParam("id") int id)
	{
		System.out.println("wsel lehné");
		return Response.ok(userManager.findPatientById(id)).build();
	}
	
	
	

	
}
