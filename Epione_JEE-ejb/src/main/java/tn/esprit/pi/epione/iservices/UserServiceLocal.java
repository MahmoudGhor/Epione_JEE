package tn.esprit.pi.epione.iservices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.enterprise.inject.Any;
import javax.json.JsonObject;

import org.json.JSONObject;

import tn.esprit.pi.epione.persistence.Admin;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.MedicalFile;
import tn.esprit.pi.epione.persistence.Medical_Prescription;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Planning;
import tn.esprit.pi.epione.persistence.Prescription;
import tn.esprit.pi.epione.persistence.Review;
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
	public JsonObject enablePattern(Pattern pattern);
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
	public JsonObject rejectAppointment(int appointment);
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
	public User findUserByUserName(String username);
	public JsonObject updatePattern(Pattern pattern);
	public List<Pattern> getListPatternDisabledByMedecin(int idDoctor);
	public List<Planning> getListePlanning(int idDoctor);
	public List<Patient> getListPatientByDoctor(int idDoctor);
	public List<String> getListDayNoWorking(int idDoctor);
	public String getCurrentDate();
	public List<Planning> getPlanningOfday(int idDoctor , String year , String month , String day);
	public Appointment getAppointmentByIdPlanning(int idPlanning);
	public Patient getPatientById(int idPatient);
	public Review getReviewOfPatient(int idDoctor , int idPatient);
	public JsonObject addReview(int idDoctor, int idPatient , String description);
	public JsonObject updateReview(int id , String description);
	public List<MedicalFile> getMedicalFilsOfPatient(int idDoctor , int idPatient);
	public JsonObject addMedicalFils(int idDoctor , int idPatient , String description , int idAppointment);
	public JsonObject addPrescription(String medicament,  int quantite , int idAppointment);
	public List<Medical_Prescription> getAllMedicalPrescription();
	public JsonObject addNotWorkingDays(int idDoctor , String year , String month , String day );
	public JsonObject addWorkingDays(int idDoctor , String year , String month , String day , String hours , String minutes , String seconds );
	public List<Prescription> getListPrescriptionByAppointment();
	public Date selectMaxDay (int idDoctor);
}
