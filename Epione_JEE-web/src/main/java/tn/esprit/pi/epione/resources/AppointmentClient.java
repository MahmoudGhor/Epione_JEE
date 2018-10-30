package tn.esprit.pi.epione.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public Response addAppointment(JsonObject body) {


		
		//int x = body.get("doctor");
		
		//System.out.println(x);

//		int idDoctor = (int) body.get("doctor");
//
//		System.out.println(idDoctor);
		return null;
//			return Response.ok(appointmentManager.addAppointment(description,idDoctor,idPatient,pattern)).build();
		
	}
}
