package tn.esprit.pi.epione.persistence;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pattern {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int price;
	private String label;
	private int periode;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private Doctor doctor;
	private boolean isActif ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public boolean isActif() {
		return isActif;
	}
	public void setActif(boolean isActif) {
		this.isActif = isActif;
	}
	public Pattern(int price, String label, int periode, Doctor doctor) {
		super();
		this.price = price;
		this.label = label;
		this.periode = periode;
		this.doctor = doctor;
		this.isActif = true;
	}
	public Pattern(String label, Doctor doctor) {
		super();
		this.label = label;
		this.doctor = doctor;
		this.isActif = true;
	}
	public Pattern() {
		super();
	}
	
	
	
	

}
