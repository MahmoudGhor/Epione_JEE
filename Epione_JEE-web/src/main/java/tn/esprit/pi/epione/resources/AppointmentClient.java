package tn.esprit.pi.epione.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import tn.esprit.pi.epione.iservices.AppointmentSeviceLocal;


@Path("/appointment")
@RequestScoped
public class AppointmentClient {
	@EJB
	AppointmentSeviceLocal appointmentManager;

	/******************************** Add Appointment **********************************************/
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAppointment(@QueryParam("patient") int patient,@QueryParam("description") String description,@QueryParam("doctor") int doctor,@QueryParam("pattern") int pattern) {
			return Response.ok(appointmentManager.addAppointment(description,doctor,patient,pattern)).build();
		
	}
	
	/******************************** Cancel Appointment **********************************************/
	@Path("/cancel")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelAppointment(@QueryParam("patient") int patient,@QueryParam("idAppointment") int idAppointment) {
			return Response.ok(appointmentManager.cancelAppointment(patient,idAppointment)).build();
		
	}
	
	/******************************** Update Appointment **********************************************/
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAppointment(@QueryParam("patient") int patient,@QueryParam("idAppointment") int idAppointment,@QueryParam("description") String description,@QueryParam("doctor") int doctor,@QueryParam("pattern") int pattern) {
			return Response.ok(appointmentManager.updateAppointment(patient,idAppointment,description,doctor,pattern)).build();
		
	}
	
	/******************************** Remove Appointment **********************************************/
	@Path("/remove")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAppointment(@QueryParam("idAppointment") int idAppointment) {
			return Response.ok(appointmentManager.removeAppointment(idAppointment)).build();
		
	}
}
