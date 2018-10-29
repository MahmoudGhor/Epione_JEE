package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.CompteRendu;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;

@Local
public interface AnalyticsServiceRemote {

	
	JsonObject addCompteRendu(Doctor d, Patient p,String contenu, String document, String img);
}
