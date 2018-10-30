package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.CompteRendu;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;

@Local
public interface AnalyticsServiceRemote {

	
	JsonObject addCompteRendu(String d, String p,String contenu, String document, String img);
	public Doctor findDoctorById(int idDoctor);
	public Patient findPatientById(int idPatient);
	
}
