package tn.esprit.pi.epione.iservices;

import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface UserServiceLocal {
	public JsonObject SignUp(User user);
	public String addPattern(Doctor medecin , String pattern , int price , int periode);
	public User getUserByid (int idUser);
	public void modifyPeriodePattern(int  idMedecin , int perionde , String pattern);
	public void deletePattern (int idMedecin , String pattern);
	public Pattern findPatternByDescriptionAndDoctor (int idDoctor , String pattern);
	public List<Pattern> getListPatternByMedecin (int idDoctor);
	public void modifyPatternDescription(int  idMedecin , int perionde , String pattern);
	

}
