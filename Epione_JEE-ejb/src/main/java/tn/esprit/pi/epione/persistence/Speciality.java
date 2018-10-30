package tn.esprit.pi.epione.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Speciality {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String speciality;
	
	@JsonIgnore
	@OneToMany(mappedBy=("speciality"),fetch = FetchType.EAGER)
	private List<Doctor> doctor;
	
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
