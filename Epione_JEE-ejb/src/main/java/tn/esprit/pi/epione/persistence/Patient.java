package tn.esprit.pi.epione.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
public class Patient extends User {
	
	@JsonIgnore
	@OneToMany(mappedBy="patient")
	private List<Appointment> appointments;
	@JsonIgnore
	@OneToMany(mappedBy="patient")
	private List<Notification> notifications = new ArrayList<>();
	
	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}


	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	

}
