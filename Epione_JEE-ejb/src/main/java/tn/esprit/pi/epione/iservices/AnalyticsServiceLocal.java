package tn.esprit.pi.epione.iservices;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.google.gson.JsonObject;

import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Local
public interface AnalyticsServiceLocal {

	public long countTreatedPatients(); // done

	public long countCanceledAppointments(); // done

	public List<Object> appointmentsbyYear(int doc_id); // done without title

	public int countAppointmentsbyDoctor(Doctor d);

	public int countAppointmentsbyPatient(Patient p);

	public List<Doctor> getDoctorsByRegion(String region); // done
	
	public List<Appointment> getAppointmentsByDoctor(int doc_id); // done

	public List<Doctor> getDoctorsBySpecialities(Speciality speciality); // done
	

}
