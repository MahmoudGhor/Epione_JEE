package tn.esprit.pi.epione.service;

import java.security.NoSuchAlgorithmException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;

import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.User;
import tn.esprit.pi.epione.util.Utils;

public class UserService implements UserServiceLocal {

	EntityManager em;
	
	
	/*********************** SignUp patient  *************************************/
	@Override
	public JsonObject SignUp(User user) {
		// TODO Auto-generated method stub
		JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
		if(Utils.emailValidator(user.getEmail()))
		{
			if(this.isExistEmail(user.getEmail()))
			{
				errorBuilder.add("error", "the email address is allready exist");
				return errorBuilder.build();
			}
			else if(this.isExistUsername(user.getUsername()))
			{
				errorBuilder.add("error", "the username is allready exist");
				return errorBuilder.build();
			}
			else if(!this.isValidPassword(user.getPassword()))
			{
				errorBuilder.add("error", "the password is too weak");
				return errorBuilder.build();
			}
		}
		else
		{
			errorBuilder.add("error", "your email address is not valid");
			return errorBuilder.build();
		}
		
		try {
			user.setPassword(Utils.toMD5(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			errorBuilder.add("error", "the password is too weak");
			return errorBuilder.build();
			
		}
		
		em.persist(user);
		em.flush();
		JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
		succesBuilder.add("succes", "quick signup successfully completed");
		succesBuilder.add("user_id",user.getId());
		return succesBuilder.build();
	}
	
	/**************** verifier l'existance d'un email  *************************/
	private boolean isExistEmail(String email)
	{
		long result=(long) em.createQuery(
				  "SELECT count(u) from User u WHERE u.email = :email").
				  setParameter("email", email).getSingleResult();
		
		if(result==0)
			return false;			
		else
			return true;
	}
	
	
	
	/**************** verifier l'existance d'un utilisateur  *************************/
	private boolean isExistUsername(String username)
	{
		long result=(long) em.createQuery(
				  "SELECT count(u) from User u WHERE u.username = :username").
				  setParameter("username", username).getSingleResult();
		if(result==0)
			return false;			
		else
			return true;
	}
	
	
	/************** verifier si un password est valide *******************************/
	private boolean isValidPassword(String password)
	{
		if(password.length()<5)
		return false;
		else
		return true;
	}


	
	
	
	/******************* creation d'un pattern  ************************/
	@Override
	public String AddPattern(Doctor medecin, String pattern , int price , int periode) {
		if (pattern != "")
		{
			return "you have to enter a valid pattern";
		}
		return "patter insert successfully" ;
	}

	
	
	
	
	/************** get user by id *********************************/
	
	@Override
	public User getUserByid(int idUser) {
		return em.find(User.class, idUser);
	}	
	

}
