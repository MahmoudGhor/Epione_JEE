package tn.esprit.pi.epione.iservices;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Rating;

@Local
public interface RateServiceLocal {
	
	public int AddRate(Rating r,int AppId);
	public Rating getRateByAppoitement(int AppId);
	public List<Rating> getRatesByPatient(String paientUserName);
	public List<Rating> getRatesByDoctor(String doctorUserName);
	public List<Rating> getAllRates();
	public int EditRate(Rating r);
	public int deleteRate(int id);

}
