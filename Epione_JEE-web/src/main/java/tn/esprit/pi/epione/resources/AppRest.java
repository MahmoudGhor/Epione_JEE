package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import tn.esprit.pi.epione.persistence.AppointementAlternatif;


@Path("appointment")
@RequestScoped
public class AppRest {
	
    @Inject
    AlternativeAppointmentEndpoint webSocketEndpoint;
    
    @POST
    @Path("/cancel/{datecancel}")
    public void sendMessage(@PathParam("datecancel") String datecancel) {
    	
    	AppointementAlternatif aa = new AppointementAlternatif();
    	aa.setMsg(" ");
    	DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date dateApp;
		try {
			dateApp = (Date) d.parse(datecancel);
			aa.setDate(dateApp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			webSocketEndpoint.send(aa);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
