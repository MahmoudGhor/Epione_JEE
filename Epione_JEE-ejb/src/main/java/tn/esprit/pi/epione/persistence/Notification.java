package tn.esprit.pi.epione.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String comm;
	private boolean sentToPatient;
	private boolean sentToDoctor;
	private Date sending_date;
	
	@ManyToOne
	private Doctor doctor;
	@ManyToOne
	private Patient patient;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComm() {
		return comm;
	}
	public void setComm(String comm) {
		this.comm = comm;
	}

	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Patient getPatient() {
		return patient;
	}
	public Date getSending_date() {
		return sending_date;
	}
	public void setSending_date(Date sending_date) {
		this.sending_date = sending_date;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public boolean isSentToPatient() {
		return sentToPatient;
	}
	public void setSentToPatient(boolean sentToPatient) {
		this.sentToPatient = sentToPatient;
	}
	public boolean isSentToDoctor() {
		return sentToDoctor;
	}
	public void setSentToDoctor(boolean sentToDoctor) {
		this.sentToDoctor = sentToDoctor;
	}
	
	
	
	
}
