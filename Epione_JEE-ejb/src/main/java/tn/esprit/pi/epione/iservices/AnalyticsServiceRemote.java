package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.CompteRendu;

@Local
public interface AnalyticsServiceRemote {

	
	JsonObject addCompteRendu(String contenu, String document, String img);
}
