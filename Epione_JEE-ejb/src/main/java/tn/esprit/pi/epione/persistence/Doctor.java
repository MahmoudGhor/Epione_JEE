package tn.esprit.pi.epione.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@Entity
public class Doctor extends User {
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@ManyToOne
	private Speciality speciality;
	
	private String PaymentMethod;
	private String biography;
	private String Office_Number;
	private String Website;
	private String Doctolib;
	private String OfficeAdress;
	private String Remboursement;
	
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="doctor")
	private List<DoctorFormation> formations = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="doctor", fetch=FetchType.LAZY)
	private List<Appointment> appointments;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(mappedBy="doctor", fetch=FetchType.EAGER)
	private List<Pattern> patterns;
	
	@OneToMany(mappedBy="doctor")
	private List<CompteRendu> cr;
	
	@OneToMany(mappedBy="doctor")
	private List<Acts> acts;
	@JsonIgnore
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
	public List<DoctorFormation> getFormations() {
		return formations;
	}
	public void setFormations(List<DoctorFormation> formations) {
		this.formations = formations;
	}
	public String getOfficeAdress() {
		return OfficeAdress;
	}
	public void setOfficeAdress(String officeAdress) {
		OfficeAdress = officeAdress;
	}
	public String getRemboursement() {
		return Remboursement;
	}
	public void setRemboursement(String remboursement) {
		Remboursement = remboursement;
	}
	public String getDoctolib() {
		return Doctolib;
	}
	public void setDoctolib(String doctolib) {
		Doctolib = doctolib;
	}
	public List<Acts> getActs() {
		return acts;
	}
	public void setActs(List<Acts> acts) {
		this.acts = acts;
	}
 
}