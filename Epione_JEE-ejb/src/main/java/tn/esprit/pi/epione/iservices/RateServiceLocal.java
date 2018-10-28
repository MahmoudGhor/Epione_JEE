package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Rating;

@Local
public interface RateServiceLocal {
	
	public int AddRate(Rating r);
	public int EditRate(Rating r,int rate, String comment);

}
