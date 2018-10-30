package tn.esprit.pi.epione.iservices;

import javax.ejb.Remote;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.CompteRendu;

@Remote
public interface DoctolibServiceRemote {
	
	String get();
	
	

}
