package tn.esprit.pi.epione.service;


import java.util.List;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.print.Doc;

import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;

@Stateless

public class AnalyticsService implements AnalyticsServiceLocal {
    @PersistenceContext(unitName = "Epione_JEE-ejb")
	EntityManager em;
	
	
	
	/*   Get (count) treated patients */
	@Override
	public long countTreatedPatients() {
		long result= (long) em.createQuery(
				  "SELECT DISTINCT count(idPatient) from Appointment where date < CURDATE()").getSingleResult();
	return result;
	}



	/* Get (count) canceled Appointments */
	@Override
	public int countCanceledAppointments() {
		int result=(int) em.createQuery(
				  "SELECT  count(id) from Appointment where status=false").getSingleResult();
		
		return result;
	}



	@Override
	public int countAppointmentsbyDoctor(Doctor d) {
		int result=(int) em.createQuery(
				  "SELECT count(a) from Appointment a WHERE a.idDoctor = :id").
				  setParameter("id", d.getId()).getSingleResult();
		
		return result;
	}



	@Override
	public int countAppointmentsbyPatient(Patient p) {
		int result=(int) em.createQuery(
				  "SELECT count(a) from Appointment a WHERE a.id = :id").
				  setParameter("id", p.getId()).getSingleResult();
		
		return result;
	}



	@Override
	public List<Doctor> getDoctorsByRegion(String region) {
		TypedQuery<Doctor> query =em.createQuery("select d from Doctor d where d.ville=?1",Doctor.class);
		query.setParameter(1, region);
		return query.getResultList();
	}



	@Override
	public List<Doctor> getDoctorsBySpecialities(Speciality speciality) {
		TypedQuery<Doctor> query =em.createQuery("select d from Doctor d where d.speciality=?1",Doctor.class);
		query.setParameter(1, speciality);
		return query.getResultList();
	}

	
	
}
