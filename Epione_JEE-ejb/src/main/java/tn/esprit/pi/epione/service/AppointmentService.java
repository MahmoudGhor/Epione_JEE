package tn.esprit.pi.epione.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.pi.epione.iservices.AppointmentSeviceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Status;

@Stateless
public class AppointmentService implements AppointmentSeviceLocal {
	
	@PersistenceContext(unitName="Epione_JEE-ejb")
	EntityManager em;
	
	@Override
	public JsonObject addAppointment(String description , int idDoctor , int idPatient , int pattern) {
		if ((findPatientById(idPatient) != null)&&(findDoctorById(idDoctor) != null)&&(em.find(Pattern.class, pattern) != null) ) {
			if (em.find(Patient.class, idPatient).isActive() == true) {
				if (em.find(Patient.class, idPatient).getConnected() == true) {
					System.out.println("enter");
					Patient _patient = em.find(Patient.class, idPatient);
					Doctor _doctor = em.find(Doctor.class, idDoctor);
					Pattern _pattern = em.find(Pattern.class, pattern);
					Appointment a = new Appointment();
					a.setStatus(Status.waiting);
					a.setDate(new Date());
					a.setDescription(description);
					a.setPatient(_patient);
					a.setPattern(_pattern);
					a.setDoctor(_doctor);
					em.persist(a);
					return Json.createObjectBuilder().add("succes", "Appointment added successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "Patient or doctor or pattern not exist").build();
		}
	}

	@Override
	public JsonObject cancelAppointment(int idPatient , int idAppointment) {
		if ((findPatientById(idPatient) != null)&&(em.find(Appointment.class, idAppointment) != null) ) {
			if (em.find(Patient.class, idPatient).isActive() == true) {
				if (em.find(Patient.class, idPatient).getConnected() == true) {
					em.find(Appointment.class, idAppointment).setStatus(Status.canceled);
					return Json.createObjectBuilder().add("succes", "Appointment canceled successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "Patient or Appointment not exist").build();
		}
	}

	@Override
	public JsonObject updateAppointment(int idPatient , int idAppointment, String description, int idDoctor,int pattern) {
		if ((findPatientById(idPatient) != null)&&(em.find(Appointment.class, idAppointment) != null)&&(findDoctorById(idDoctor) != null)&&(em.find(Pattern.class, pattern) != null) ) {
			if (em.find(Patient.class, idPatient).isActive() == true) {
				if (em.find(Patient.class, idPatient).getConnected() == true) {
					Doctor _doctor = em.find(Doctor.class, idDoctor);
					Pattern _pattern = em.find(Pattern.class, pattern);
					Appointment a = em.find(Appointment.class, idAppointment);
					a.setStatus(Status.waiting);
					//a.setDate(new Date());
					a.setDescription(description);
					a.setPattern(_pattern);
					a.setDoctor(_doctor);
					em.persist(a);
					em.flush();
					return Json.createObjectBuilder().add("succes", "Appointment updated successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "Patient, Appointment, pattern or Doctor not exist").build();
		}
	}

	@Override
	public JsonObject removeAppointment(int idAppointment) {
		if (em.find(Appointment.class, idAppointment) != null) {
					Appointment a = em.find(Appointment.class, idAppointment);
					em.remove(a);
					em.flush();
					return Json.createObjectBuilder().add("succes", "Appointment removed successfully").build();
		} else {
			return Json.createObjectBuilder().add("error", "Appointment not exist").build();
		}
	}

	public Patient findPatientById(int idPatient) {
		return em.find(Patient.class, idPatient);
	}
	
	public Doctor findDoctorById(int idDoctor) {
		return em.find(Doctor.class, idDoctor);
	}
	

	
}
