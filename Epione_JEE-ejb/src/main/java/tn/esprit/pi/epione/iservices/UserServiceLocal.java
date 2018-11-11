package tn.esprit.pi.epione.iservices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.enterprise.inject.Any;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Admin;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Status;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface UserServiceLocal {
	public JsonObject signUpPatient (Patient patient); //ok
	public JsonObject signUpDoctor(Doctor doctor) ;
	public JsonObject addPattern(Doctor medecin , String pattern , int price , int periode); //ok
	public User getUserByid (int idUser); //ok
	public Doctor findDoctorById (int idDoctor); //ok
	public Patient findPatientById (int idPatient);  //ok
	public Pattern findPatternById (int idPattern); //ok
	public JsonObject modifyPeriodePattern(int idPattern, int periode); //ok
	public JsonObject deletePattern(Pattern pattern);
	public Pattern findPatternByDescriptionAndDoctor (String pattern ,int idDoctor );
	public List<Pattern> getListPatternByMedecin (int idDoctor);
	public JsonObject modifyPatternDescription(int idPattern , String pattern);
	public JsonObject enableUser(String username , String token);
	public User getUserByUserName(String username);
	public User verifToken(String username,String token);
	public JsonObject SignIn (String usernameOrEmail , String password);
	public boolean isExistEmail(String email);
	public boolean isExistUsername(String username);
	public User loginEmail(String email, String password);
	public User loginUsername(String username, String password);
	public JsonObject logOut (int idUser);
	public Doctor getDoctorFromAppointment(int appointment);
	public JsonObject treatAppointment(int appointment, Status newstate);
	public JsonObject markAchievedAppointment(int appointment);
	public List<Appointment> getListAppointmentNotTreated(int idDoctor);
	public List<Appointment> getListAppointmentForSpecificDate( int idDoctor , Date startDate , Date endDate );
	public String getCaldendar() throws GeneralSecurityException, IOException;
	public String setCalendar() throws GeneralSecurityException;
	public JsonObject updateDoctor(Doctor doctor);
	public JsonObject updatePatient(Patient patient);
	public JsonObject updateAdmin(Admin admin);
	public JsonObject addPlanningForOneDay(int idDoctor , Date day , Timestamp startTime , Timestamp endTime , int TimeMeeting);
	public List<Pattern> selectListPatternByPeriode(int idDoctor , int periode);
	public JsonObject makePlanningForAnAppointment(int idAppointment, int idPlanning);
	public List<Doctor> getListDoctors();
	public List<Appointment> selectAppointmentOfToday(int idDoctor);
	

}
