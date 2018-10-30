package tn.esprit.pi.epione.iservices;

import java.io.IOException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Doctor;


public interface DoctolibServiceLocal {
	
	Doctor get(String path);
	List<Doctor> getDoctorsbySpeciality(String speciality,int page);
	
	String getFromJson(String path,String page);
	
}
