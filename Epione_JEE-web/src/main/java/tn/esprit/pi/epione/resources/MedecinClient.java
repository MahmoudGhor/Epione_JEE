package tn.esprit.pi.epione.resources;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.User;

@Path("/doctor")
@RequestScoped
public class MedecinClient {
	@EJB
	UserServiceLocal userManager;
	
	
	
	@Path("/ii")
	@POST
	public String ao()
	{
		return "aa";
	}
	
	/******************************  sign up Doctor ******************************************/
	@Path("/addDoctor")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUpPatient(Doctor doctor)
	{
		return Response.ok(userManager.signUpDoctor(doctor)).build();
		
	}
	
	
	
	
	/****************************** enable account doctor ********************************/
	@Path("/enable/{username}/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response enableUser(@PathParam("username") String username , @PathParam("token") String token)
	{
		return Response.ok(userManager.enableUser(username , token)).build();
	}
	
	
	/******************************** add pattern **********************************************/
	// @Path("/addPattern/{medecin}/{pattern}/{price}/{periode}")
	@Path("/addPattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPattern(Pattern p) {

		User x = (p.getDoctor());
		if (x instanceof Doctor) {
			return Response.ok(userManager.addPattern((Doctor) x, p.getLabel(), (int) p.getPrice(), p.getPeriode()))
					.build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}

	}
	
	
	/*********************getUserById*********************************/
	@Path("/getUserById/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id") int id)
	{
		User user=userManager.getUserByid(id);
		if(user!=null)
		{
			return Response.ok(user).build();
		}
			
		return Response.status(Status.NO_CONTENT).build();
	}
	
	
	
	/*************************  update periode of pattern  ***************************************/
	@Path("/updatePeriodePattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePeriodeOfPattern(Pattern p)
	{
		return Response.ok(userManager.modifyPeriodePattern(p.getId(), p.getPeriode())).build();
	}
	
	
	
	
	/*************************  update label of pattern  ***************************************/
	@Path("/updateDescriptionPattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyPatternDescription(Pattern p)
	{
		return Response.ok(userManager.modifyPatternDescription(p.getId(), p.getLabel())).build();
		
	}
	

	
	
	/*********************** afficher list patterns by doctor *****************************/
	@Path("/getListPatternByDoctor/{idDoctor}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListPatternByMedecin(@PathParam("idDoctor") int idDoctor)
	{
		return Response.ok(userManager.getListPatternByMedecin(idDoctor)).build();
	}
	
	
	
	/********************* delete pattern ***********************************/
	@Path("/deletePattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePattern(Pattern p)
	{
		return Response.ok(userManager.deletePattern(p)).build();
		
	}
	
	
	
	
	
	
	
	//-------------------------------- Appointmens -------------------------------------------------------//
	
	
	
	
	
	
	
	/*********************** treat an appointment  *****************************************/
	@Path("/treatAppointment")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response treatAppointment(Map<String, String> map) {
		tn.esprit.pi.epione.persistence.Status x= null;

		if (map.get("newstate").equals("confirmed"))
			x = tn.esprit.pi.epione.persistence.Status.confirmed;

		if (map.get("newstate").equals("refused"))
			x = tn.esprit.pi.epione.persistence.Status.refused;
		return Response.ok(userManager.treatAppointment(Integer.parseInt(map.get("appointment")), x)).build();
	}

	
	
	/*********************** make an appointment to achieve status  *****************************************/
	@Path("/makeAcheived")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markAchievedAppointment(Map<String , String> map) {
		
		return Response.ok(userManager.markAchievedAppointment(Integer.parseInt(map.get("appointment")))).build();
		
		
	}
	
	
	
	/******************** get list of all appointment no treated yet **************************/
	@Path("/NotAchievedAppointments")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllNotAchievedAppointments(Map<String, String> map) {
		return Response.ok(userManager.getListAppointmentNotTreated(Integer.parseInt(map.get("idDoctor")))).build();
	}
	
	
	
	/******************** get list of all appointment in specific date **************************/
	@Path("/AppointmentsFromDate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getListAppointmentForSpecificDate(Map<String, String> map) throws NumberFormatException, ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (map.get("endDate") == null && map.get("startDate") != null)
		{
			Date end = new Date();
			return Response.ok(userManager.getListAppointmentForSpecificDate(Integer.parseInt(map.get("idDoctor")), formatter.parse(map.get("startDate")), end)).build();
		}
		else 
		{
			if (map.get("startDate") == null && map.get("endDate") != null)
			{
				Date start = new Date();
				return Response.ok(userManager.getListAppointmentForSpecificDate(Integer.parseInt(map.get("idDoctor")), start, formatter.parse(map.get("endDate")))).build();
			}
			else
			{
				if (map.get("startDate") == null && map.get("endDate") == null)
				{
				Date end = new Date();
				Date start = new Date();
				return Response.ok(userManager.getListAppointmentForSpecificDate(Integer.parseInt(map.get("idDoctor")), start, end)).build();
				}
				
			}
			
		}
		
		return Response.ok(userManager.getListAppointmentForSpecificDate(Integer.parseInt(map.get("idDoctor")), formatter.parse(map.get("startDate")), formatter.parse(map.get("endDate")))).build();
	}

	
}
