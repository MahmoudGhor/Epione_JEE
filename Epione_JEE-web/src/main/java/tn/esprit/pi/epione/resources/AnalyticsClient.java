package tn.esprit.pi.epione.resources;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.json.JSONArray;

import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.iservices.AnalyticsServiceRemote;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.CompteRendu;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Medical_Prescription;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Path("/analytics")
@RequestScoped
public class AnalyticsClient {
	@EJB
	private AnalyticsServiceLocal AnalyticsService;
	@EJB
	private AnalyticsServiceRemote AnalyticsServiceR;

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

	/* Count appointment by patients */

	@Path("/appointment/bypatient/{patient_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public long CountAppointmentByPatient(@PathParam("patient_id") int pat_id) {

		return AnalyticsService.countAppointmentsbyPatient(pat_id);
	}

	/* Count appointments by doctor */

	@Path("/appointment/bydoctor/{doctorid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public long CountAppointmentByDoctor(@PathParam("doctorid") int doc_id) {

		return AnalyticsService.countAppointmentsbyDoctor(doc_id);
	}

	/* Count Appointments by Year Months (Doctor) */

	@Path("/doctors/yearappointments/{doctorid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> doctorYearAppointments(@PathParam("doctorid") int doc_id) {

		return AnalyticsService.appointmentsbyYear(doc_id);
	}

	/* Get doctors by speciality */

	@Path("/doctors/{speciality}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Doctor> doctorsBySpeciality(@PathParam("speciality") Speciality spec) {
		return AnalyticsService.getDoctorsBySpecialities(spec);
	}

	/* Get doctors by region (OfficeAdress) */

	@Path("/doctors/region/{region}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Doctor> doctorsbyRegion(@PathParam("region") String region) {
		return AnalyticsService.getDoctorsByRegion(region);
	}

	/* get appointment by doctor id */

	@Path("/appointments/doctor/{doc_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Appointment> getAppointmentsbyDoctor(@PathParam("doc_id") int doc_id) {
		return AnalyticsService.getAppointmentsByDoctor(doc_id);
	}

	/* Get used / opened vacations */

	@Path("/doctors/vacations/{doc_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getUsedVacations(@PathParam("doc_id") int doc_id) {
		return AnalyticsService.VacationsByDoctor(doc_id);
	}

	/* Get appointments by speciality */
	@Path("/appointment/speciality/{speciality}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Appointment> getAppointmentsbySpeciality(@PathParam("speciality") Speciality spec) {
		return AnalyticsService.AppointmentsBySpeciality(spec);
	}

	/* Add Compte Rendu */
	@Path("/doctors/addCR")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUpPatient(Map<String, String> cr) {

		return Response.ok(AnalyticsServiceR.addCompteRendu(cr.get("iddoctor"), cr.get("idpatient"), cr.get("contenu"),
				cr.get("document"), cr.get("img"))).build();

	}

	/* Get appointments by Pattern */
	@Path("/appointment/pattern/{pattern_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppointmentsbyPattern(@PathParam("pattern_id") int pat_id) {
		if (!AnalyticsService.getAppointmentsByPattern(pat_id).isEmpty()) {
			return Response.ok(AnalyticsService.getAppointmentsByPattern(pat_id)).build();
		} else {
			return Response.ok("No Appointment with this pattern").build();
		}
	}

	/* Get medical prescriptions by medication name */
	@Path("/prescription/med/{med}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMedicalPrescriptionByMedication(@PathParam("med") String med) {
		if (!AnalyticsService.getPrescribedMedication(med).isEmpty()) {
			return Response.ok(AnalyticsService.getPrescribedMedication(med)).build();
		} else {
			return Response.ok("No prescription with this medication").build();
		}
	}
	
	
	/* Count medical prescription by medication name and date */
	@Path("/prescription/med/{med}/{date1}/{date2}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject countMedicalPrescriptionByMedication(@PathParam("med") String med, @PathParam("date1") Date Date1,@PathParam("date2") Date Date2) {
		int number=0;
		if (!AnalyticsService.getPrescribedMedication(med).isEmpty()) {
			for (Medical_Prescription e : AnalyticsService.getPrescribedMedication(med)) {
				   if (e.getAppointment().getDate().compareTo(Date1)>0 && e.getAppointment().getDate().compareTo(Date2)<0) {
					number++;
				}
			}
		}
		JsonObjectBuilder succesBuilder = Json.createObjectBuilder();
		succesBuilder.add(med, number);
		succesBuilder.add("Date debut", Date1.toString());
		succesBuilder.add("Date fin", Date2.toString());

		
		return succesBuilder.build();

	}
	
	/* Get patients by Age range  */
	@Path("/patients/age/{age1}/{age2}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatientbyAgeRange(@PathParam("age1") int age1, @PathParam("age2") int age2) {
		if (!AnalyticsService.getPatientsbyAgeRange(age1, age2).isEmpty()) {
			return Response.ok(AnalyticsService.getPatientsbyAgeRange(age1, age2)).build();
		} else {
			return Response.ok("Il n'existe pas de patient dans cette tranche d'age").build();
		}

	}
	
	
	/* Get patients by Age range  and doctor */

	@Path("/patients/age/{age1}/{age2}/{doc_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatientbyAgeRangeandDoctor(@PathParam("age1") int age1, @PathParam("age2") int age2,@PathParam("doc_id") int doc_id) {
		if (!AnalyticsService.getPatientbyAgeRangeandDoctor(doc_id, age1, age2).isEmpty()) {
			return Response.ok(AnalyticsService.getPatientbyAgeRangeandDoctor(doc_id, age1, age2)).build();
		} else {
			return Response.ok("Il n'existe pas de patient dans cette tranche d'age").build();
		}

	}
	
	
	
}
