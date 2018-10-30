package tn.esprit.pi.epione.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import javax.persistence.ManyToMany;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import javax.persistence.OneToMany;



@Entity
public class Doctor extends User {
	
	@Enumerated(EnumType.STRING)
	private Speciality speciality;
	
	private String PaymentMethod;
	private String biography;
	private String Office_Number;
	private String Website;
	private boolean cnam;
	
	@OneToMany(mappedBy="doctor", fetch=FetchType.LAZY)
	private List<Appointment> appointments;
	@OneToMany(mappedBy="doctor", fetch=FetchType.LAZY)
	private List<Pattern> patterns;
	@OneToMany(mappedBy="doctor")
	private List<Planning> plannings;
	@ManyToMany(mappedBy="doctors")
	private List<Recommandation> recommandations = new ArrayList<>();
	@OneToMany(mappedBy="doctor")
	private List<Notification> notifications = new ArrayList<>();
	
	public List<Recommandation> getRecommandations() {
		return recommandations;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	public void setRecommandations(List<Recommandation> recommandations) {
		this.recommandations = recommandations;
	}
	public Speciality getSpeciality() {
		return speciality;
	}
	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public String getOffice_Number() {
		return Office_Number;
	}
	public void setOffice_Number(String office_Number) {
		Office_Number = office_Number;
	}
	public String getWebsite() {
		return Website;
	}
	public void setWebsite(String website) {
		Website = website;
	}
	public boolean isCnam() {
		return cnam;
	}
	public void setCnam(boolean cnam) {
		this.cnam = cnam;
	}
	public List<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	public List<Pattern> getPatterns() {
		return patterns;
	}
	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}
	public List<Planning> getPlannings() {
		return plannings;
	}
	public void setPlannings(List<Planning> plannings) {
		this.plannings = plannings;
	}
	public String getPaymentMethod() {
		return PaymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}
 
}
