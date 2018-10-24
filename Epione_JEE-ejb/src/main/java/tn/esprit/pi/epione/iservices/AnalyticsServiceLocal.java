package tn.esprit.pi.epione.iservices;

import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;

@Local
public interface AnalyticsServiceLocal {

	public int countTreatedPatients();
	public int countCanceledAppointments();

	public int countAppointmentsbyDoctor(Doctor d);
	public int countAppointmentsbyPatient(Patient p);
	public List<Doctor> getDoctorsByRegion(String region);
	public List<Doctor> getDoctorsBySpecialities(Speciality speciality);
	
}
