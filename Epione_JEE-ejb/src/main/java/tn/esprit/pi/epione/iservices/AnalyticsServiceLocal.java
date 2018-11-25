package tn.esprit.pi.epione.iservices;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Medical_Prescription;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface AnalyticsServiceLocal {

	public long countTreatedPatients(); // done

	public long countCanceledAppointments(); // done

	public List<Object> appointmentsbyYear(int doc_id); // done without title

	public long countAppointmentsbyDoctor(int doc_id); // done

	public long countAppointmentsbyPatient(int p); // done

	public List<Doctor> getDoctorsByRegion(String region); // done

	public List<Appointment> getAppointmentsByDoctor(int doc_id); // done A TESTER

	public List<Doctor> getDoctorsBySpecialities(Speciality speciality); // done

	public JsonObject VacationsByDoctor(int doc_id); // Open-Used Vacations

	public List<Appointment> AppointmentsBySpeciality(Speciality speciality); // Stats Count Appointments by Speciality
																				// A TESTER

	public List<Appointment> getAppointmentsByPattern(int pattern_id); // done

	public List<Medical_Prescription> getPrescribedMedication(String med); // done

	public List<Object> getPatientsbyAgeRange(int age1, int age2); // done
	
	public List<Patient> getPatientsByDoctor(int doc_id);
	
	public List<Patient> getPatientbyAgeRangeandDoctor(int doc_id,int age1, int age2);
	

}
