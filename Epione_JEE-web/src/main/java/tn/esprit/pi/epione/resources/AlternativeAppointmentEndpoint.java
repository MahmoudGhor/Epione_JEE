package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import tn.esprit.pi.epione.persistence.AppointementAlternatif;
import tn.esprit.pi.epione.util.AppDecoder;
import tn.esprit.pi.epione.util.AppEncoder;

@Singleton
@ServerEndpoint(value = "/alternativeAppointment", encoders = { AppEncoder.class }, decoders = { AppDecoder.class })
public class AlternativeAppointmentEndpoint {
	
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	private Session sessionLast;
	
	 @OnOpen
	 public void handelopen(Session session){
		 sessions.add(session);
	 }
	 
	 @OnMessage
	 public void onMessage(Session session, AppointementAlternatif message){
		 
		 Date date = (Date) session.getUserProperties().get("date");

		 if(date == null)
			 session.getUserProperties().put("date", message.getDate());
		 System.out.println(session.getUserProperties().put("date", message.getDate()));
		 
	 }
	 
	 @OnClose
	    public void onClose(Session session) {
	        sessions.remove(session);
	    }
	 
	 public void send(AppointementAlternatif message) throws IOException, EncodeException {
		 
		 
		 Iterator<Session> iterator1 = sessions.iterator(); 
		 Date date1 = null;
		 
		 /* set first date like canceled appointment day */
		 while (iterator1.hasNext()) { 
			 Date date2 = (Date) iterator1.next().getUserProperties().get("date");
			 if(date2.getYear() == message.getDate().getYear()){
				 sessionLast = iterator1.next();
				 date1 = (Date) sessionLast.getUserProperties().get("date");
			 } 
		 }
		 
		 /* search for the last appointment of the day */
		 while (iterator1.hasNext()) { 
			 Date date2 = (Date) iterator1.next().getUserProperties().get("date");
			 if(date2.getYear() == message.getDate().getYear())
				 if (date2.compareTo(date1)>0){
					 sessionLast = iterator1.next();
					 System.out.println("Last is "+sessionLast.getUserProperties().get("date"));
				 }
		 }
		 
		 message.setMsg("you want to change you appointment time to"+message.getDate());
		 if(sessionLast != null)
			 sessionLast.getBasicRemote().sendObject(message);
		 else/* remove all sessions */
			 while (iterator1.hasNext()) { 
				 sessions.remove(iterator1.next());
			 }
		 
	 }

}
