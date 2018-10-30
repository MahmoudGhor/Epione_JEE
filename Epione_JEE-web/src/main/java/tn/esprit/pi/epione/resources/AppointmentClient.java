package tn.esprit.pi.epione.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


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


		
			System.out.println(patient);
			System.out.println(description);
			System.out.println(doctor);
			System.out.println(pattern);

			return Response.ok(appointmentManager.addAppointment(description,doctor,patient,pattern)).build();
		
	}
}
