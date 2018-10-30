package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Appointment;


@Local
public interface AppointmentSeviceLocal {

	public JsonObject addAppointment (String description , int idDoctor , int idPatient , int pattern);
	public JsonObject cancelAppointment (Appointment appointment);
	public JsonObject updateAppointment (Appointment appointment);
	public JsonObject removeAppointment (Appointment appointment);

	
}
