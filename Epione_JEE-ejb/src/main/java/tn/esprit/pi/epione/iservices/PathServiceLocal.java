package tn.esprit.pi.epione.iservices;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Notification;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Recommandation;

@Local
public interface PathServiceLocal {

	
	public JsonObject addRecommandation(int a,int d,String type);
    public List<Recommandation> findAllRecommandation();
	public String getRecommandationById(int recommandationId);
	public List<Recommandation> getRecommandationsByAppointment(int app);
    public JsonObject ValidateRecommandation(int recommandation,int doctor);
    public List<Recommandation> getRecommandationsByRecommanderDoctor(int d);
	 public List<Recommandation> findAllRecommandationByDoctor(int d);
	 public JsonObject addPath(int a,String type);
	 public List<Recommandation>findAllPathsByPatient(int patient);
	  public JsonObject addNotification(int appointment,int speciality);
		public List<Notification> listNotifications(int patient);
		public List<Notification> listNotificationsForSpecialist(int doctor);
		  public JsonObject OpenNotification(int notification,int user);
			public List<Recommandation> getRecommandationsByPatient(int patient);
			 public JsonObject UpdateRecommandation(int recommandation,int specialist,String type,String justification,int newspecialist);
			 public List<Patient> ListAllPatientsForSpecialist(int specialist);
			  public JsonObject addNotificationToClient(int recommandation,int specialist);
			  
			  
}
