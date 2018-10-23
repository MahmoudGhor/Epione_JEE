package tn.esprit.pi.epione.persistence;

import javax.persistence.Embeddable;

@Embeddable
public class Adresse {
	
	private String street_name;
	private int postal_code;
	private String city;
	
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	public int getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(int postal_code) {
		this.postal_code = postal_code;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	

}
