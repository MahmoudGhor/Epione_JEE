package tn.esprit.pi.epione.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.print.Doc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import tn.esprit.pi.epione.iservices.AnalyticsServiceLocal;
import tn.esprit.pi.epione.iservices.AnalyticsServiceRemote;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.CompteRendu;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Medical_Prescription;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.User;

@Stateless
public class AnalyticsService implements AnalyticsServiceLocal, AnalyticsServiceRemote {
	@PersistenceContext(unitName = "Epione_JEE-ejb")
	EntityManager em;

	UserService us;

	/* Get (count) treated patients */
	@Override
	public long countTreatedPatients() {
		long result = (long) em.createQuery("SELECT DISTINCT count(idPatient) from Appointment where date < CURDATE() and status='confirmed'")
				.getSingleResult();
		return result;
	}

	/* Get (count) canceled Appointments */
	@Override
	public long countCanceledAppointments() {
		long result = (long) em.createQuery("SELECT  count(id) from Appointment where status='canceled'").getSingleResult();

		return result;
	}

	/* count appointments by doctor */
	@Override
	public long countAppointmentsbyDoctor(int doc_id) {
		long result = (long) em.createQuery("SELECT count(a) from Appointment a WHERE a.doctor.id = :id")
				.setParameter("id", doc_id).getSingleResult();

		return result;
	}

	/* count appointments by patient */
	@Override
	public long countAppointmentsbyPatient(int p) {
		long result = (long) em.createQuery("SELECT count(a) from Appointment a WHERE a.patient.id = :id")
				.setParameter("id", p).getSingleResult();

		return result;
	}

	/* get doctors by region */
	@Override
	public List<Doctor> getDoctorsByRegion(String region) {
		TypedQuery<Doctor> query = em.createQuery("select d from Doctor d where d.OfficeAdress LIKE '%" + region + "%'",
				Doctor.class);
		return query.getResultList();
	}

	/* get all doctors by speciality */
	@Override
	public List<Doctor> getDoctorsBySpecialities(Speciality speciality) {
		TypedQuery<Doctor> query = em.createQuery("select u from Doctor u where u.speciality.speciality=?1 ",
				Doctor.class);
		query.setParameter(1, speciality.getSpeciality());
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

	/* Get opened used vacations by doctor */
	@Override
	public javax.json.JsonObject VacationsByDoctor(int doc_id) {

		long query1 = (long) em
				.createQuery("SELECT count(p) from Planning p where p.disponibility=1 and p.doctor.id=" + doc_id)
				.getSingleResult();
		long query2 = (long) em
				.createQuery("SELECT count(p) from Planning p where p.disponibility=0 and p.doctor.id=" + doc_id)
				.getSingleResult();
		JsonObjectBuilder succesBuilder = Json.createObjectBuilder();
		if (query2 != 0) {
			Float taux = (float) ((query1 * 100) / query2);
			succesBuilder.add("Taux", taux);

		}

		succesBuilder.add("UsedVacations", query1);
		succesBuilder.add("OpenedVacations", query2);

		return succesBuilder.build();
	}

	/* Get appointments by speciality */

	@Override
	public List<Appointment> AppointmentsBySpeciality(Speciality speciality) {

		TypedQuery<Appointment> query = em.createQuery(
				"select c from Appointment c where c.doctor = ( select t from User t where t.speciality.speciality = ?1 ) ",
				Appointment.class);
		query.setParameter(1, speciality.getSpeciality());

		return query.getResultList();

	}

	/* Add compte rendu */
	@Override
	public javax.json.JsonObject addCompteRendu(String d, String p, String contenu, String document, String img) {
		// System.out.println(d+"aaaaaaaaaa");
		// Doctor d1= findDoctorById(Integer.parseInt(d));
		// Patient p1= findPatientById(Integer.parseInt(p));
		Doctor d1 = em.find(Doctor.class, Integer.parseInt(d));
		Patient p1 = em.find(Patient.class, Integer.parseInt(p));

		CompteRendu cr = new CompteRendu(p1, d1, contenu, img, document);
		System.out.println(p1.getEmail());

		em.persist(cr);
		return Json.createObjectBuilder().add("succes", "Compte rendu added successfully").build();

	}

	/********* find patient by id ************************************/
	@Override
	public Patient findPatientById(int idPatient) {
		return em.find(Patient.class, idPatient);
	}

	/*********** find doctor by id *********************************/
	@Override
	public Doctor findDoctorById(int idDoctor) {
		return em.find(Doctor.class, idDoctor);
	}

	/* Get appointments by Pattern */

	@Override
	public List<Appointment> getAppointmentsByPattern(int pattern_id) {

		TypedQuery<Appointment> query = em.createQuery("select c from Appointment c where c.pattern.id = ?1 ",
				Appointment.class);
		query.setParameter(1, pattern_id);

		return query.getResultList();

	}

	/* Get prescripted medication */
	@Override
	public List<Medical_Prescription> getPrescribedMedication(String med) {
		TypedQuery<Medical_Prescription> query = em.createQuery(
				"select d from Medical_Prescription d where d.description LIKE '%" + med + "%'",
				Medical_Prescription.class);
		return query.getResultList();
	}

	/* get patients by age range */
	@Override
	public List<Patient> getPatientsbyAgeRange(int age1, int age2) {
		TypedQuery<Patient> query = em.createQuery(
				"select p from Patient p where YEAR(CURRENT_DATE)-YEAR(p.birthday) BETWEEN ?1 and ?2 ", Patient.class);
		query.setParameter(1, age1);
		query.setParameter(2, age2);

		return query.getResultList();
	}

	@Override
	public List<Patient> getPatientsByDoctor(int doc_id) {
		TypedQuery<Patient> query = em.createQuery(
				"SELECT DISTINCT u.patient from Appointment u WHERE u.doctor.id=?1 ", Patient.class);
		query.setParameter(1, doc_id);
		

		return query.getResultList();
	}

	@Override
	public List<Patient> getPatientbyAgeRangeandDoctor(int doc_id,int age1, int age2) {
		List<Patient> listep=getPatientsByDoctor(doc_id);
		List<Patient> liste_patient = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();;
		for (Patient p : listep) {
			calendar.setTime(p.getBirthday());
			if (LocalDate.now().getYear()-calendar.get(Calendar.YEAR)>=age1 && LocalDate.now().getYear()-calendar.get(Calendar.YEAR)<=age2) {
				liste_patient.add(p);
			}
		}
		return liste_patient;
	}

	

}
