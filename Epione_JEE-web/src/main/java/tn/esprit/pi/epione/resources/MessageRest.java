package tn.esprit.pi.epione.resources;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.User;


@Path("/messages/ques")
@RequestScoped
public class MessageRest {
    @Inject
    ServerEndpointQuest webSocketEndpoint;
    
    @POST
    @Path("/send")
    public void sendMessage(@FormParam("key") String key, @FormParam("message") String message) {
        webSocketEndpoint.send(message, key);
    }
        /*User user = Connected_User.getUser();
    	if(user instanceof Patient){
    		String key = user.getUsername();
    		webSocketEndpoint.send(message, key);
    	}*/

    
    @GET
    @Path("/PatientsQuestions")
    public String Patients() {
        return webSocketEndpoint.getPatientId().toString();
    }
}
