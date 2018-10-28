package tn.esprit.pi.epione.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.print.Doc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Stateless
public class AnalyticsService implements AnalyticsServiceLocal {
	@PersistenceContext(unitName = "Epione_JEE-ejb")
	EntityManager em;

	/* Get (count) treated patients */
	@Override
	public long countTreatedPatients() {
		long result = (long) em.createQuery("SELECT DISTINCT count(idPatient) from Appointment where date < CURDATE()")
				.getSingleResult();
		return result;
	}

	/* Get (count) canceled Appointments */
	@Override
	public long countCanceledAppointments() {
		long result = (long) em.createQuery("SELECT  count(id) from Appointment where status=0").getSingleResult();

		return result;
	}

	/* count appointments by doctor */
	@Override
	public long countAppointmentsbyDoctor(int doc_id) {
		int result = (int) em.createQuery("SELECT count(a) from Appointment a WHERE a.idDoctor = :id")
				.setParameter("id", doc_id).getSingleResult();

		return result;
	}

	/* count appointments by patient */
	@Override
	public int countAppointmentsbyPatient(Patient p) {
		int result = (int) em.createQuery("SELECT count(a) from Appointment a WHERE a.id = :id")
				.setParameter("id", p.getId()).getSingleResult();

		return result;
	}

	/* get doctors by region */
	@Override
	public List<Doctor> getDoctorsByRegion(String region) {
		TypedQuery<Doctor> query = em.createQuery("select d from Doctor d where d.ville=?1", Doctor.class);
		query.setParameter(1, region);
		return query.getResultList();
	}

	/* get all doctors by speciality */
	@Override
	public List<Doctor> getDoctorsBySpecialities(Speciality speciality) {
		TypedQuery<Doctor> query = em.createQuery("select u from Doctor u where u.speciality=?1 ", Doctor.class);
		query.setParameter(1, speciality);
		return query.getResultList();
	}

	/* Count appointments in all year (mouth) by doctor */
	@Override
	public List<Object> appointmentsbyYear(int doc_id) {

		/*
		 * for (int i = 0; i < 12; i++) { String query =
		 * "SELECT count(a) from Appointment a WHERE a.doctor.id=:id and month(a.date) =:m"
		 * ; Query q = em.createQuery(query); q.setParameter("id", doc_id);
		 * q.setParameter("m", i); //al.add(i,q.getResultList());
		 * System.out.println(q.getResultList().;);
		 * 
		 * }
		 */

		String query = "SELECT count(a),month(date) from Appointment a WHERE a.doctor.id = :id GROUP BY month(a.date)";
		Query q = em.createQuery(query);
		q.setParameter("id", doc_id);

		return q.getResultList();
	}

	/* Get all appointments by doctor */
	@Override
	public List<Appointment> getAppointmentsByDoctor(int doc_id) {
		TypedQuery<Appointment> query = em.createQuery("select d from Appointment d where d.doctor.id=?1",
				Appointment.class);
		query.setParameter(1, doc_id);
		return query.getResultList();
	}

}
