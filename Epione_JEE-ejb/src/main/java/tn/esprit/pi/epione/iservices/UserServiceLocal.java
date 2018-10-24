package tn.esprit.pi.epione.iservices;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface UserServiceLocal {
	public JsonObject SignUp(User user);
	public String AddPattern(Doctor medecin , String pattern , int price , int periode);
	public User getUserByid (int idUser);
	

}
