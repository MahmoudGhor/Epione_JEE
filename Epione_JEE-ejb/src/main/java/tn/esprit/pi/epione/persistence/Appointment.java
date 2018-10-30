package tn.esprit.pi.epione.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Appointment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String description;
	@JsonIgnore
	@OneToOne(fetch= FetchType.EAGER)
	private Medical_Prescription medical_Prescription;
	@OneToOne(fetch= FetchType.EAGER)
	private Rating rating;
	
	@JsonIgnore
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="idDoctor",referencedColumnName="id",insertable=false, updatable=false)
	private Doctor doctor;
	@JsonIgnore
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="idPatient",referencedColumnName="id",insertable=false, updatable=false)
	private Patient patient;
	
	@JsonIgnore
	@OneToMany(mappedBy="appointment")
	private List<Recommandation> recommandations;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private Pattern pattern;
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	private Planning planning;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Medical_Prescription getMedical_Prescription() {
		return medical_Prescription;
	}
	public void setMedical_Prescription(Medical_Prescription medical_Prescription) {
		this.medical_Prescription = medical_Prescription;
	}
	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
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
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public List<Recommandation> getRecommandations() {
		return recommandations;
	}
	public void setRecommandations(List<Recommandation> recommandations) {
		this.recommandations = recommandations;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public Planning getPlanning() {
		return planning;
	}
	public void setPlanning(Planning planning) {
		this.planning = planning;
	}
	
	
	

}
