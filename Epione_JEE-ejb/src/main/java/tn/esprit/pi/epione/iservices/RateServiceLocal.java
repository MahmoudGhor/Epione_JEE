package tn.esprit.pi.epione.iservices;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Rating;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface RateServiceLocal {
	
	public Rating AddRate(Rating r,int AppId);
	public Rating getRateByAppoitement(int AppId);
	public List<Rating> getRatesByPatient(String paientUserName);
	public List<Rating> getRatesByDoctor(String doctorUserName);
	public List<Rating> getAllRates();
	public int EditRate(Rating r);
	public int deleteRate(int id);
	public double DoctorRate(int idDoctor);
	public Doctor GetDoctorApp(int app);
	public List<Appointment> GetListApp(int id);
	public List<Appointment> GetListAppr(int id);

}
