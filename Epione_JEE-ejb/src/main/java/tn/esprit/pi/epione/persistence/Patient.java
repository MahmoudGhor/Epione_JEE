package tn.esprit.pi.epione.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Patient extends User {
	
	@OneToOne
	private Doctor referent;
	
	@OneToMany(mappedBy="patient")
	private List<Appointment> appointments;

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Doctor getReferent() {
		return referent;
	}

	public void setReferent(Doctor referent) {
		this.referent = referent;
	}

	


	

}
