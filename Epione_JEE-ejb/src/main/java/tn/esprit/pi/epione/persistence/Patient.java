package tn.esprit.pi.epione.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Patient extends User {
	
	@OneToMany(mappedBy="patient")
	private List<Appointment> appointments;
	
	@OneToMany(mappedBy="patient")
	private List<CompteRendu> cr;

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	


	

}
