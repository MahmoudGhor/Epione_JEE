package tn.esprit.pi.epione.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class Patient extends User {
	
	@JsonIgnore
	@OneToMany(mappedBy="patient")
	private List<Appointment> appointments;
	@JsonIgnore
	@OneToMany(mappedBy="patient")
	private List<CompteRendu> cr;

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	


	

}
