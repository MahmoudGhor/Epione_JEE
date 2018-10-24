package tn.esprit.pi.epione.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Speciality;

@Path("/analytics")
public class AnalyticsClient {
	@EJB
	private AnalyticsServiceLocal AnalyticsService;
	
	@Path("/patients/counttreated")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public long countTreatedPatients()
	{
	
		return AnalyticsService.countTreatedPatients();
	}
	
	@Path("/doctors/{speciality}")
	@GET
	public List<Doctor> doctorsBySpeciality(@PathParam("speciality") Speciality spec)
	{
		return AnalyticsService.getDoctorsBySpecialities(spec);
	}

}
