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



@Entity
public class Appointment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String description;
	@OneToOne(fetch= FetchType.EAGER)
	private Medical_Prescription medical_Prescription;
	@OneToOne(fetch= FetchType.EAGER)
	private Rating rating;
	
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="idDoctor",referencedColumnName="id",insertable=false, updatable=false)
	private Doctor doctor;
	
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="idPatient",referencedColumnName="id",insertable=false, updatable=false)
	private Patient patient;

	@OneToMany(mappedBy="appointment", fetch=FetchType.EAGER)
	private List<Recommandation> recommandations;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Pattern pattern;

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
	@Override
	public String toString() {
		return "Appointment [id=" + id + ", date=" + date + ", description=" + description
				+ ", medical_Prescription=" + medical_Prescription + ", rating=" + rating + ", doctor=" + doctor
				+ ", patient=" + patient + ", recommandations=" + recommandations + ", pattern=" + pattern
				+ ", planning=" + planning + "]";
	}
	
	
	

}
