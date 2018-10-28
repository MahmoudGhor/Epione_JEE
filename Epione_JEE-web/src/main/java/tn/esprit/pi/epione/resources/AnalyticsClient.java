package tn.esprit.pi.epione.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.primefaces.json.JSONArray;

import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Path("/analytics")
@RequestScoped
public class AnalyticsClient {
	@EJB
	private AnalyticsServiceLocal AnalyticsService;

	/* Get (count) treated patients */

	@Path("/patients/counttreated")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public long countTreatedPatients() {

		return AnalyticsService.countTreatedPatients();
	}

	/* Get (count) canceled Appointments */

	@Path("/appointment/countcanceled")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public long CanceledAppointments() {

		return AnalyticsService.countCanceledAppointments();
	}
	
	
	@Path("/appointment/bydoctor/{doctorid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public long CountAppointmentByDoctor(@PathParam("doctorid") int doc_id) {

		return AnalyticsService.countAppointmentsbyDoctor(doc_id);
	}

	@Path("/doctors/yearappointments/{doctorid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> doctorYearAppointments(@PathParam("doctorid") int doc_id) {

		return AnalyticsService.appointmentsbyYear(doc_id);
	}

	@Path("/doctors/{speciality}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Doctor> doctorsBySpeciality(@PathParam("speciality") Speciality spec) {
		return AnalyticsService.getDoctorsBySpecialities(spec);
	}

	@Path("/doctors/region/{region}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Doctor> doctorsbyRegion(@PathParam("region") String region) {
		return AnalyticsService.getDoctorsByRegion(region);
	}
	
	
	@Path("/appointments/doctor/{doc_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Appointment> getAppointmentsbyDoctor(@PathParam("doc_id") int doc_id) {
		return AnalyticsService.getAppointmentsByDoctor(doc_id);
	}
	
	
	
	

}
