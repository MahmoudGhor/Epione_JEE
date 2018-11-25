package tn.esprit.pi.epione.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.iservices.RateServiceLocal;
import tn.esprit.pi.epione.persistence.Rating;
	
@Path("/rate")
@RequestScoped
public class RatingClient {
	
	@EJB
	RateServiceLocal rateManager;
	
	@Path("/add/{appId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRate(Rating r,@PathParam("appId") int appId){
		return Response.ok(rateManager.AddRate(r,appId)).build();
	}
	
	@Path("get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		return Response.ok(rateManager.getAllRates()).build();
	}
	
	@Path("get/{appId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRateByAppointement(@PathParam("appId") int appId){
		return Response.ok(rateManager.getRateByAppoitement(appId)).build();
	}
	
	@Path("edit")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response EditRate(Rating r) {
		return Response.ok(rateManager.EditRate(r)).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteRate (@PathParam("id") int id)
	{
		return Response.ok(rateManager.deleteRate(id)).build();
	}
	
	@Path("patient/{paientUserName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRatesByPatientUserName(@PathParam("paientUserName") String paientUserName){
		return Response.ok(rateManager.getRatesByPatient(paientUserName)).build();
	}
	
	@Path("doctor/{doctorUserName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRatesByDoctorUserName(@PathParam("doctorUserName") String doctorUserName){
		return Response.ok(rateManager.getRatesByDoctor(doctorUserName)).build();
	}
	
	@Path("doctorRate/{doctorId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response DoctorRate(@PathParam("doctorId") int doctorId){
		return Response.ok(rateManager.DoctorRate(doctorId)).build();
	}

}
