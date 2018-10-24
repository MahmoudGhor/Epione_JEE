package tn.esprit.pi.epione.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;

@Path("/analytics")
public class AnalyticsClient {
	@Inject
	AnalyticsServiceLocal AnalyticsManager;
	
	
	/*      Treated Patients         */
	@Path("/patients/counttreated")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int countTreatedPatients()
	{
	
		return AnalyticsManager.countTreatedPatients();
	}

}
