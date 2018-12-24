package tn.esprit.pi.epione.resources;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
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
import tn.esprit.pi.epione.persistence.Admin;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.sql.Timestamp;

@Path("/doctor")
@RequestScoped
public class MedecinClient {
	@EJB
	UserServiceLocal userManager;
	
	
	/******************************  sign up Doctor ******************************************/
	@Path("/addDoctor")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUpPatient(Doctor doctor)
	{
		System.out.println(doctor.getSpeciality());
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
	public Response addPattern(Map<String, String>map) {

		User x = userManager.getUserByid(Integer.parseInt(map.get("idDoctor")));
			return Response.ok(userManager.addPattern((Doctor) x, map.get("label"),Integer.parseInt(map.get("price")), Integer.parseInt(map.get("periode")))).build();
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
	
	/*********************** afficher list disabled patterns by doctor *****************************/
	@Path("/getListDisabledPatternByDoctor/{idDoctor}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListDisabledPatternByMedecin(@PathParam("idDoctor") int idDoctor)
	{
		return Response.ok(userManager.getListPatternDisabledByMedecin(idDoctor)).build();
	}
	
	
	
	/********************* delete pattern ***********************************/
	@Path("/deletePattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePattern(Pattern p)
	{
		return Response.ok(userManager.deletePattern(p)).build();
		
	}
	
	/********************* enable pattern ***********************************/
	@Path("/enablePattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response enablePattern(Pattern p)
	{
		return Response.ok(userManager.enablePattern(p)).build();
		
	}
	
	/*********************** afficher list pattern by id *****************************/
	@Path("/getPatternById/{idPattern}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatternById(@PathParam("idPattern") int idPattern) {
		return Response.ok(userManager.findPatternById(idPattern)).build();
	}
	
	/*************************** Update pattern    ********************************************************/
	@Path("/updatePattern")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDoctor(Pattern pattern)
	{
		System.out.println("99999"+pattern.getLabel()+"--------"+pattern.getId());
		return Response.ok(userManager.updatePattern(pattern)).build();
		
	}
	
	
	
	
	//-------------------------------- Appointmens -------------------------------------------------------//
	
	
	
	
	
	
	
	/*********************** treat an appointment  *****************************************/
	@Path("/treatAppointment")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response treatAppointment(Map<String, String> map) {
		tn.esprit.pi.epione.persistence.Status stat= null;

		if (map.get("newstate").equals("confirmed"))
			stat = tn.esprit.pi.epione.persistence.Status.confirmed;

		if (map.get("newstate").equals("refused"))
			stat = tn.esprit.pi.epione.persistence.Status.refused;
		return Response.ok(userManager.treatAppointment(Integer.parseInt(map.get("appointment")), stat)).build();
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
	@Path("/NotAchievedAppointments/{idDoctor}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllNotAchievedAppointments(@PathParam("idDoctor") int idDoctor) {
		return Response.ok(userManager.getListAppointmentNotTreated(idDoctor)).build();
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
	
	/*************** test calendrier 
	 * @throws IOException 
	 * @throws GeneralSecurityException *******************/
	@Path("/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response testCalendar() throws GeneralSecurityException, IOException
	{
		return Response.ok(userManager.getCaldendar()).build();
	}
	
	
	/*************** add calendrier 
	 * @throws IOException 
	 * @throws GeneralSecurityException *******************/
	@Path("/set")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response setCalendar() throws GeneralSecurityException, IOException
	{
		return Response.ok(userManager.setCalendar()).build();
	}
	
	
	
	
	/********************************* add time work in day 
	 * @throws ParseException ***********************************/
	@Path("/addPlanning")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPlanningForOneDay(Map<String, String> map) throws ParseException {
		System.out.println("*********************");
		System.out.println(map.get("day"));
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd, MM,
		// yyyy", Locale.ENGLISH);
		// ZonedDateTime day = ZonedDateTime.parse(map.get("day"));
		// LocalDate day = LocalDate.parse(map.get("day"), formatter);
		// Date date =
		// Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
		// LocalDate start = LocalDate.parse(map.get("startDate"), formatter);
		// ZonedDateTime startTime = ZonedDateTime.parse(map.get("startDate"));
		// LocalDate end = LocalDate.parse(map.get("endTime"), formatter);
		// ZonedDateTime endTime = ZonedDateTime.parse(map.get("endTime"));
		/*
		 * SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
		 * java.util.Date d1
		 * =(java.util.Date)format.parse(map.get("startTime")); java.sql.Time
		 * startTime = new java.sql.Time(d1.getTime()); java.util.Date d2
		 * =(java.util.Date)format.parse(map.get("endTime")); java.sql.Time
		 * endTime = new java.sql.Time(d2.getTime());
		 */
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date day = simpleDateFormat.parse(map.get("day"));
		// Date startTime = simpleDateFormat.parse(map.get("startDate"));
		// Date endTime = simpleDateFormat.parse(map.get("endTime"));
		/*
		 * DateFormat formatter = new SimpleDateFormat("hh:mm:ss "); Date
		 * startTime = (Date)formatter.parse(map.get("startDate")); Date endTime
		 * = (Date)formatter.parse(map.get("endTime"));
		 */
		/*
		 * String sdf = new
		 * SimpleDateFormat("HH:mm:ss").format(map.get("startTime")); String
		 * sdf2 = new SimpleDateFormat("HH:mm:ss").format(map.get("endTime"));
		 * Time startTime = null; Time endTime = null; startTime =
		 * Time.valueOf(sdf); endTime = Time.valueOf(sdf2);
		 */
		DateFormat formatter;
		System.out.println("ladib");
		System.out.println(map.get("day") + " " + map.get("startDate"));
		formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date parseDate = formatter.parse(map.get("day") + " " + map.get("startDate"));
		Date parseDate2 = formatter.parse(map.get("day") + " " + map.get("endDate"));
		Timestamp startDate = new Timestamp(parseDate.getTime());
		Timestamp endDate = new Timestamp(parseDate2.getTime());
		// System.out.println(start);
		// System.out.println("-----------------------");
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println("azuihdazi");

		int x = (endDate.getHours() * 60 + endDate.getMinutes()) - startDate.getHours() * 60 + startDate.getMinutes();

		int y = x / Integer.parseInt(map.get("timeMeeting"));
		System.out.println("---------------");
		System.out.println("startdate" + startDate + "enddate:" + endDate);
		System.out.println("---------------");
		Timestamp time = new Timestamp(parseDate.getTime());
		Timestamp time1 = new Timestamp(time.getTime());
		System.out.println("laaaaaaaaabidi" + y);
		System.out.println(time);
		for (int i = 0; i <= y - 1; i++) {
			System.out.println("exuté" + i + "fois");
			time1.setTime(time.getTime());
			time.setMinutes(time1.getMinutes() + Integer.parseInt(map.get("timeMeeting")));
			userManager.addPlanningForOneDay(Integer.parseInt(map.get("idDoctor")), day, time1, time,
					Integer.parseInt(map.get("timeMeeting")));
			System.out.println("////////////////////////////////");
			System.out.println("tps:  " + time1 + "-->" + time);
			System.out.println("////////////////////////////////");

		}
		// System.out.println((map.get("startTime")).toString());
		return Response.ok("Planning added succ").build();

	}
	
	
	/********************* get list pattern using periode *****************************/
	@Path("/PatternByPeriode")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response selectListPatternByPeriode(Map<String, String> map) throws NumberFormatException, ParseException {
			
		return Response.ok(userManager.selectListPatternByPeriode(Integer.parseInt(map.get("idDoctor")),(Integer.parseInt(map.get("periode"))) )).build();
	}
	
	
	
	/*******************  makePlanningForAnAppointment  ***********************************/
	@Path("/makePlanningForAppointment")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makePlanningForAnAppointment (Map<String, String>map)
	{
		if ((map.get("idAppointment"))==null || map.get("idPlanning")==null)
		{
			return Response.status(Status.NO_CONTENT).build();
		}
		else {
		return Response.ok(userManager.makePlanningForAnAppointment(Integer.parseInt(map.get("idAppointment")),(Integer.parseInt(map.get("idPlanning"))) )).build();
		}
	}
	
	
	
	/********************* show program of the day************************************/
	@Path("/programOfTheDay")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response selectAppointmentOfToday (Map<String, String>map)
	{
		if ((map.get("idDoctor"))==null)
		{
			return Response.status(Status.NO_CONTENT).build();
		}
		else {
		return Response.ok(userManager.selectAppointmentOfToday(Integer.parseInt(map.get("idDoctor")))).build();
		}
	}
	
	
	/********************* reject appointment************************************/
	@Path("/reject")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rejectAppointment (Map<String, String>map)
	{
		
		return Response.ok(userManager.rejectAppointment(Integer.parseInt(map.get("id")))).build();
		
	}
	
	
	/******************* get liste medecin *****************************************/
	@Path("/getListDoctors")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListeDoctos()
	{
		System.out.println("wsel lehné");
		return Response.ok(userManager.getListDoctors()).build();
	}
	
	
	/******************* get info doctor *****************************************/
	@Path("/getInfoDoctor/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDetailConnectedDoctor(@PathParam("id") int id)
	{
		System.out.println("wsel lehné");
		return Response.ok(userManager.findDoctorById(id)).build();
	}
	
	
	/******************* get info doctor *****************************************/
	@Path("/getPlanningDoctor/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlanningDoctor(@PathParam("id") int id)
	{
		System.out.println("wsel lehné");
		return Response.ok(userManager.getListePlanning(id)).build();
	}
	
	/******************* get patients of doctor *****************************************/
	@Path("/getPatientsByDoctorId/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatientByDoctorId(@PathParam("id") int id)
	{
		System.out.println("wsel lehné");
		return Response.ok(userManager.getListPatientByDoctor(id)).build();
	}
	
	
	

	
}
