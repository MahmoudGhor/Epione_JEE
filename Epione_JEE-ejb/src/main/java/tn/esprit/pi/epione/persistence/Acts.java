package tn.esprit.pi.epione.persistence;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity implementation class for Entity: Acts
 *
 */
@Entity

public class Acts implements Serializable {

	   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	@JsonIgnore
	@ManyToOne
	private Doctor doctor;
	private static final long serialVersionUID = 1L;

	public Acts() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Acts(String name, Doctor doctor) {
		super();
		this.name = name;
		this.doctor = doctor;
	}
   
}
