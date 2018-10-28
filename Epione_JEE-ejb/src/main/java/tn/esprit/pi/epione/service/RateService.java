package tn.esprit.pi.epione.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.pi.epione.iservices.RateServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Rating;

@Stateless
public class RateService implements RateServiceLocal {

	@PersistenceContext(unitName="Epione_JEE-ejb")
	EntityManager em;

	@Override
	public int AddRate(Rating r) {
		em.persist(r);
		r.setCreated_at(new Date());
		//Appointment appointment = new Appointment();
		r.setAppointment(null);
		return r.getId();
	}

	@Override
	public int EditRate(Rating r,int rate, String comment) {
		Rating rating = em.find(Rating.class, r.getId());
		rating.setComment(comment);
		rating.setRate(rate);
		return rating.getId();
	}
	
	

}
