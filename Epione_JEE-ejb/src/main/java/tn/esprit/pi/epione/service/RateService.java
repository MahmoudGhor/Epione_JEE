package tn.esprit.pi.epione.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.pi.epione.iservices.RateServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Rating;

@Stateless
public class RateService implements RateServiceLocal {

	@PersistenceContext(unitName="Epione_JEE-ejb")
	EntityManager em;

	@Override
	public int AddRate(Rating r,int AppId) {
		/* verify the rate */
		if(r.getRate()<0 || r.getRate()>5)
			return -1;
		em.persist(r);
		r.setCreated_at(new Date());
		Appointment app = em.find(Appointment.class, AppId);
		System.out.println(app.getId());
		r.setAppointment(app);
		return r.getId();
	}

	@Override
	public Rating getRateByAppoitement(int AppId) {
		TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r WHERE r.appointment.id ="+AppId, Rating.class);
		return query.getSingleResult();
	}

	@Override
	public List<Rating> getAllRates() {
		TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r", Rating.class);
		return query.getResultList();
	}

	@Override
	public int EditRate(Rating r) {
		return em.createQuery("update Rating r SET r.rate = "+r.getRate()+" WHERE r.id = "+r.getId()).executeUpdate();
	}

	@Override
	public int deleteRate(int id) {
		return em.createQuery("delete from Rating r WHERE r.id = "+id).executeUpdate();
	}

	@Override
	public List<Rating> getRatesByPatient(String paientUserName) {
		TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r where r.appointment.patient.username =:paientUserName", Rating.class);
		return query.setParameter("paientUserName", paientUserName).getResultList();
	}

	@Override
	public List<Rating> getRatesByDoctor(String doctorUserName) {
		TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r where r.appointment.patient.username =:doctorUserName", Rating.class);
		return query.setParameter("doctorUserName", doctorUserName).getResultList();
	}

	
	

}
