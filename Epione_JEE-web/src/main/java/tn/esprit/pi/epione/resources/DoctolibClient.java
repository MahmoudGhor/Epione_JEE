package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tn.esprit.pi.epione.filters.Secured;
import tn.esprit.pi.epione.iservices.DoctolibServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
//@Secured
@Path("/doctolib")
@RequestScoped
public class DoctolibClient {
	
	@EJB
	private DoctolibServiceLocal D;
	
	
	
	@Path("/getDoctor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Doctor getDoctors(@QueryParam("path") String path) {

		return D.get(path);
	}
	
	
	
	@Path("/getDocBySpec")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Doctor> getDoctorsbySpeciality(@QueryParam("spec") String spec, @QueryParam("page") int page) {

		return D.getDoctorsbySpeciality(spec,page);
	}
	
	
	
	@Path("/getJson")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctorsbySpeciality(@QueryParam("path") String path,@QueryParam("page") String page) {
		String myString =D.getFromJson(path,page);
		byte ptext[];
		try {
			ptext = myString.getBytes("ISO-8859-1");
			String value = new String(ptext, "UTF-8"); 
			return Response.ok(value).header("Content-Type", "application/json;charset=UTF-8").build();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return Response.ok(D.getFromJson(path,page)).header("Access-Control-Allow-Origin", "*").header("Content-Type", "application/json;charset=UTF-8").build();
		} 
	}
}
