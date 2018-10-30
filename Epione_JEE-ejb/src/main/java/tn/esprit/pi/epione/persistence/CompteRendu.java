package tn.esprit.pi.epione.persistence;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Entity implementation class for Entity: CompteRendu
 *
 */
@Entity

public class CompteRendu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JsonInclude
	@ManyToOne(fetch = FetchType.EAGER)
	private Patient patient;
	@JsonIgnore
	@ManyToOne
	private Doctor doctor;
	private String contenu;
	private String img;
	

	private String document;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	private static final long serialVersionUID = 1L;

	public CompteRendu() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public CompteRendu(String contenu, String img, String document) {
		super();
		this.contenu = contenu;
		this.img = img;
		this.document = document;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	public CompteRendu(Patient patient, Doctor doctor, String contenu, String img, String document) {
		super();
		this.patient = patient;
		this.doctor = doctor;
		this.contenu = contenu;
		this.img = img;
		this.document = document;
	}

}
