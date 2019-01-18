package tn.esprit.pi.epione.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Prescription {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private Appointment appointment;
	@OneToOne
	private Medical_Prescription MedicalPrescription;
	private int quantite;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public Medical_Prescription getMedicalPrescription() {
		return MedicalPrescription;
	}
	public void setMedicalPrescription(Medical_Prescription medicalPrescription) {
		MedicalPrescription = medicalPrescription;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	
	

}
