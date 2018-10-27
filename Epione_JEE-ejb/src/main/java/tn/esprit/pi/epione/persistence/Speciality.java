package tn.esprit.pi.epione.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Speciality {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String speciality;
	
	@OneToOne(mappedBy="speciality")
	private Doctor doctor;
	
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public Speciality() {
		
	};
	public Speciality(String speciality) {
		super();
		this.speciality = speciality;
	}
	
	
	

}
