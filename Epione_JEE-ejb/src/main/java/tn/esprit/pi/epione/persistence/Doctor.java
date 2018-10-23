package tn.esprit.pi.epione.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Doctor extends User {
	
	private Speciality speciality;
	private String biography;
	private String Office_Number;
	private String Website;
	private boolean cnam;
	@OneToMany(mappedBy="doctor")
	private List<Appointment> appointments;
	@OneToMany(mappedBy="doctor")
	private List<Pattern> patterns;
	@OneToMany(mappedBy="doctor")
	private List<Planning> plannings;
	
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
 
}
