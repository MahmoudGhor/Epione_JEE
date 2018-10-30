package tn.esprit.pi.epione.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.filters.Secured;
import tn.esprit.pi.epione.iservices.PathServiceLocal;
import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Notification;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Recommandation;


@Path("/recommandation")
@RequestScoped
public class PathClient {
	
	
	
	@EJB
	PathServiceLocal pathmanage;

	
	public static List<Recommandation> listRecommandation = new ArrayList<>();
	
	//Date created,int validation,int a,int d
	/******************************** add recommandation **********************************************/
	@Path("/addRecommandation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRecommandation (
			
			@QueryParam("a") int a,
			@QueryParam("d") int d,
			@QueryParam("type") String type
			)
	{
	
		
		
		return Response.ok(pathmanage.addRecommandation(a, d,type)).build();
		
		
	}
	
	@Path("/addPath")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPath (
			
			@QueryParam("a") int a,
			@QueryParam("type") String type
			)
	{
	
		
		
		return Response.ok(pathmanage.addPath(a, type)).build();
		
		
	}
	
	
	@Path("/addNotification")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNotification (
			
			@QueryParam("appointment") int appointment,
			@QueryParam("specialist") int specialist
			)
	{
	
		
		
		return Response.ok(pathmanage.addNotification(appointment,specialist)).build();
		
		
	}
	
	@Path("/openNotification")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response openNotification (
			
			@QueryParam("notification") int notification,
			@QueryParam("user") int user
			)
	{
	
		
		
		return Response.ok(pathmanage.OpenNotification(notification, user)).build();
		
		
	}
	
	
	
	@Path("/validateRecommandation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateRecommandation (
			@QueryParam("recommandation") int recommandation,
			@QueryParam("doctor") int doctor
		
		
			)
	{
	

		
		return Response.ok(pathmanage.ValidateRecommandation(recommandation, doctor)).build();
		
		
	}
	
	@Path("/getallrecommandations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Recommandation> getallrecommandations(
		
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
       r=pathmanage.findAllRecommandation();
       for(int i=0;i<r.size();i++)
       {
    	   r.get(i).setAppointment(null);
    	   r.get(i).setDoctors(null);    	   
       }
		return r;
	}
	
	
	
	@Path("/findPathByPatient")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Recommandation> findPathByPatient(
			@QueryParam("patient") int patient
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.findAllPathsByPatient(patient);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
			return r;
		

	}
	
	
	@Path("/listNotifications")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Notification> listNotifications(
			@QueryParam("patient") int patient
		)
	{
		
		List<Notification> n = new ArrayList<>();
	       n=pathmanage.listNotifications(patient);
	       for (int i=0;i<n.size();i++)
	       {
	    	   n.get(i).setPatient(null);
	    	   n.get(i).setDoctor(null);
	    	  
	    	   
	       }
			return n;
		

	}
	
	@Path("/listNotificationsSpecialist")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Notification> listNotificationsSpecialist(
			@QueryParam("specialist") int specialist
		)
	{
		
		List<Notification> n = new ArrayList<>();
	       n=pathmanage.listNotificationsForSpecialist(specialist);
	    
	       for (int i=0;i<n.size();i++)
	       {
	    	   n.get(i).setPatient(null);
	    	   n.get(i).setDoctor(null);
	    	   
	       }
			return n;
		

	}
	
	
	@Path("/listallrecommandations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Recommandation> RecommandationbyAppointment(
			@QueryParam("appointment") int app
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.getRecommandationsByAppointment(app);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
			return r;
		

	}
	
	
	@Path("/listPatientRecommandation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Recommandation> listPatientRecommandation(
			@QueryParam("patient") int patient
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.getRecommandationsByPatient(patient);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
			return r;
		

	}
	
	
	
	@Path("/listallrecommandationsByRecommanderDcotor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response listallrecommandationsByRecommanderDcotor(
			@QueryParam("doctor") int doctor
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.getRecommandationsByRecommanderDoctor(doctor);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
		
		return Response.ok(r).build();
	}
	
	//findAllRecommandationByDoctor
	@Path("/findAllRecommandationByDoctor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response findAllRecommandationByDoctor(
			@QueryParam("doctor") int doctor
		)
	{
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.findAllRecommandationByDoctor(doctor);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
		
		return Response.ok(r).build();
	}
	
	
	
	
	@GET
	@Path("/hello")
	public String hello(){
		
		return "hello";
	}
	
	
	@GET
	@Path("/listallrecommandations")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Recommandation> displayEmployees(){
		listRecommandation = pathmanage.findAllRecommandation();
		if(listRecommandation == null) {
			return null;
		}else {
			return listRecommandation;
		}
	}
	
	
	@Path("/updateRecommandation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRecommandation (
			@QueryParam("recommandation") int recommandation,
			@QueryParam("specialist") int specialist,
			@QueryParam("type") String type,
			@QueryParam("justification") String justification,
			@QueryParam("newspecialist") int newspecialist
		
			)
	{
	

		
		return Response.ok(pathmanage.UpdateRecommandation(recommandation, specialist, type, justification, newspecialist)).build();
		
		
	}
	
	@Path("/ListAllPatientsForSpecialist")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response ListAllPatientsForSpecialist(
			@QueryParam("specialist") int specialist
		)
	{
		
		List<Patient> p = new ArrayList<>();
	       p=pathmanage.ListAllPatientsForSpecialist(specialist);
	       
	   for(Patient pat : p)
	   {
		   pat.setAppointments(null);
		   pat.setListPatterns(null);
		   pat.setNotifications(null);
		
	   }
		
		return Response.ok(p).build();
	}
	
	//getRecommandationsByPatient
	@Path("/getRecommandationsByPatient")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response getRecommandationsByPatient(
			@QueryParam("patient") int patient
		)
	{
		
		
		
		List<Recommandation> r = new ArrayList<>();
	       r=pathmanage.getRecommandationsByPatient(patient);
	       for(int i=0;i<r.size();i++)
	       {
	    	   r.get(i).setAppointment(null);
	    	   r.get(i).setDoctors(null);    	   
	       }
	   
		
		return Response.ok(r).build();
	}
	
	

	
	
}

