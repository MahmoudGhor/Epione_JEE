package tn.esprit.pi.epione.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Planning {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_at;
	@Temporal(TemporalType.TIMESTAMP)
	private Date end_at;
	@Temporal(TemporalType.DATE)
	private Date day;
	private boolean disponibility;
	private boolean workings;
	@ManyToOne
	private Doctor doctor;
	@OneToOne(mappedBy="planning")
	private Appointment appointment;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getStart_at() {
		return start_at;
	}
	public void setStart_at(Date start_at) {
		this.start_at = start_at;
	}
	public Date getEnd_at() {
		return end_at;
	}
	public void setEnd_at(Date end_at) {
		this.end_at = end_at;
	}
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	public boolean isDisponibility() {
		return disponibility;
	}
	public void setDisponibility(boolean disponibility) {
		this.disponibility = disponibility;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public boolean isWorkings() {
		return workings;
	}
	public void setWorkings(boolean workings) {
		this.workings = workings;
	}
	
	
	
	
}
