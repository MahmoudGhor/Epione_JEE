package tn.esprit.pi.epione.service;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Status;
import tn.esprit.pi.epione.persistence.User;
import tn.esprit.pi.epione.util.Mail;
import tn.esprit.pi.epione.util.Utils;

@Stateless
public class UserService implements UserServiceLocal {

	@PersistenceContext(unitName="Epione_JEE-ejb")
	EntityManager em;
	
	@EJB
	Mail mail;
	
	
	/*********************** SignUp patient  *************************************/
	@Override
	public JsonObject signUpPatient(Patient patient) {
		// TODO Auto-generated method stub
		JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
		if (Utils.emailValidator(patient.getEmail())) {
			if (this.isExistEmail(patient.getEmail())) {
				errorBuilder.add("error", "the email address is allready exist");
				return errorBuilder.build();
			} else if (this.isExistUsername(patient.getUsername())) {
				errorBuilder.add("error", "the username is allready exist");
				return errorBuilder.build();
			} else if (!this.isValidPassword(patient.getPassword())) {
				errorBuilder.add("error", "the password is too weak");
				return errorBuilder.build();
			}
		} else {
			errorBuilder.add("error", "your email address is not valid");
			return errorBuilder.build();
		}

		try {
			patient.setPassword(Utils.toMD5(patient.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			errorBuilder.add("error", "the password is too weak");
			return errorBuilder.build();

		}
		patient.setToken(Utils.tokenGenerator());
		patient.setActive(false);
		em.persist(patient);
		em.flush();
		JsonObjectBuilder succesBuilder = Json.createObjectBuilder();
		succesBuilder.add("succes", "signup Patient successfully completed");
		succesBuilder.add("patient_id", patient.getId());
		String path = "http://localhost:18080/Epione_JEE-web/epione/patient/enable/" + patient.getUsername() + "/"
				+ patient.getToken();
		mail.send(patient.getEmail(), "singup", Utils.getValidationEmail(path),
				"your account has been successfully created please activate it now <br> http://localhost:18080/Epione_JEE-web/epione/patient/enable/"
						+ patient.getUsername() + "/" + patient.getToken(),
				path);
		return succesBuilder.build();
	}
	
	
	
	/*********************** SignUp Doctor  *************************************/
	@Override
	public JsonObject signUpDoctor(Doctor doctor) {
		// TODO Auto-generated method stub
		JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
		if (Utils.emailValidator(doctor.getEmail())) {
			if (this.isExistEmail(doctor.getEmail())) {
				errorBuilder.add("error", "the email address is allready exist");
				return errorBuilder.build();
			} else if (this.isExistUsername(doctor.getUsername())) {
				errorBuilder.add("error", "the username is allready exist");
				return errorBuilder.build();
			} else if (!this.isValidPassword(doctor.getPassword())) {
				errorBuilder.add("error", "the password is too weak");
				return errorBuilder.build();
			}
		} else {
			errorBuilder.add("error", "your email address is not valid");
			return errorBuilder.build();
		}

		try {
			doctor.setPassword(Utils.toMD5(doctor.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			errorBuilder.add("error", "the password is too weak");
			return errorBuilder.build();

		}
		doctor.setToken(Utils.tokenGenerator());
		doctor.setActive(false);
		em.persist(doctor);
		em.flush();
		JsonObjectBuilder succesBuilder = Json.createObjectBuilder();
		succesBuilder.add("succes", "signup doctor successfully completed");
		succesBuilder.add("user_id", doctor.getId());
		String path = "http://localhost:18080/Epione_JEE-web/epione/doctor/enable/" + doctor.getUsername() + "/"
				+ doctor.getToken();
		mail.send(doctor.getEmail(), "singup", Utils.getValidationEmail(path),
				"the account has been successfully created the doctor need to activate it now <br> http://localhost:18080/Epione_JEE-web/epione/doctor/enable/"
						+ doctor.getUsername() + "/" + doctor.getToken(),
				path);
		return succesBuilder.build();
	}
	
	
	
	/***************************************** Sign In ************************************************/
	public JsonObject SignIn(String usernameOrEmail, String password) {
		try {

			password = Utils.toMD5(password);
		} catch (NoSuchAlgorithmException e) {
			return Json.createObjectBuilder().add("error", "your password is not correct").build();
		}
		if (isExistEmail(usernameOrEmail)) {
			User user = loginEmail(usernameOrEmail, password);
			if (user != null) {
				if (user.isActive() == true) {
					user.setConnected(true);
					user.setLastConnect(new Date());
					em.persist(user);
					em.flush();
					return Json.createObjectBuilder().add("succes", "you have been logged in successfully")
							.add("user_id", user.getId()).build();
				} else {
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your email or your password is not correct").build();
			}
		} else {
			User user = loginUsername(usernameOrEmail, password);
			if (user != null) {
				if (user.isActive() == true) {
					user.setConnected(true);
					user.setLastConnect(new Date());
					em.persist(user);
					em.flush();
					return Json.createObjectBuilder().add("succes", "you have been logged in successfully")
							.add("user_id", user.getId()).build();
				} else {
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your email or your password is not correct").build();
			}
		}

	}
	
	
	
	
	/***************************  logOut  ***********************************/
	public JsonObject logOut(int idUser) {
		User user = getUserByid(idUser);
		if (user != null) {
			if (user.getConnected() == null) {
				return Json.createObjectBuilder().add("error", "you are not logged in yet").build();
			} else {
				if (user.getConnected() == true) {
					user.setConnected(false);
					em.persist(user);
					em.flush();
					return Json.createObjectBuilder().add("succes", "you have been successfully logged out")
							.add("user_id", user.getId()).build();
				} else {
					return Json.createObjectBuilder().add("error", "you are already logged out").build();
				}
			}

		} else
			return Json.createObjectBuilder().add("error", "there are no user with ID: " + idUser).build();
	}
	
	
	
	
	
	
	
	/**************** verifier l'existance d'un email  *************************/
	public boolean isExistEmail(String email) {
		long result = (long) em.createQuery("SELECT count(u) from User u WHERE u.email = :email")
				.setParameter("email", email).getSingleResult();

		if (result == 0)
			return false;
		else
			return true;
	}
	
	
	
	/**************** verifier l'existance d'un utilisateur  *************************/
	public boolean isExistUsername(String username) {
		long result = (long) em.createQuery("SELECT count(u) from User u WHERE u.username = :username")
				.setParameter("username", username).getSingleResult();
		if (result == 0)
			return false;
		else
			return true;
	}
	
	
	/************** verifier si un password est valide *******************************/
	private boolean isValidPassword(String password) {
		if (password.length() < 5)
			return false;
		else
			return true;
	}


	
	
	
	/******************* creation d'un pattern  ************************/
	@Override
	public JsonObject addPattern(Doctor medecin, String pattern, int price, int periode) {
		System.out.println("lvl1");
		if (pattern != "") {
			if (findDoctorById(medecin.getId()) != null) {
				if (em.find(Doctor.class, medecin.getId()).isActive() == true) {
					if (em.find(Doctor.class, medecin.getId()).getConnected() == true) {
						System.out.println("a");
						Pattern p = new Pattern(price, pattern, periode, medecin);
						em.persist(p);
						return Json.createObjectBuilder().add("succes", "Pattern added successfully").build();
					} else {
						return Json.createObjectBuilder().add("error", "you must connect before").build();
					}
				} else {
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "You are not a doctor").build();
			}

		}
		return Json.createObjectBuilder().add("error", "empty pattern").build();

	}

	
	
	
	
	/************** get user by id *********************************/
	
	@Override
	public User getUserByid(int idUser) {
		return em.find(User.class, idUser);

	}
	
	
	/*********** find doctor by id *********************************/
	@Override
	public Doctor findDoctorById(int idDoctor) {
		return em.find(Doctor.class, idDoctor);
	}
	
	
	/********* find patient by id ************************************/
	@Override
	public Patient findPatientById(int idPatient) {
		return em.find(Patient.class, idPatient);
	}
	
	
	/********* find pattern by id **********************************/
	@Override
	public Pattern findPatternById(int idPattern) {
		return em.find(Pattern.class, idPattern);
	}

	
	
	
	/***********  Modifier periode of a pattern **************************/

	@Override
	public JsonObject modifyPeriodePattern(int idPattern, int periode) {
		if (idPattern != 0 && periode != 0) {
			em.find(Pattern.class, idPattern).setPeriode(periode);
			return Json.createObjectBuilder().add("succes", "Pattern updated successfully").build();
		}
		return Json.createObjectBuilder().add("error", "issue wih the update").build();
	}
	
	
	
	
	/*********************** find pattern ***************************************/
	@Override
	public Pattern findPatternByDescriptionAndDoctor(String pattern, int idDoctor) {

		Query query = em.createQuery(
				"SELECT u  from Pattern u WHERE u.label = ?1  AND u.doctor = ( select t from User t where t.id = ?2 ) ");
		query.setParameter(1, pattern);
		query.setParameter(2, idDoctor);

		List<Pattern> patters = query.getResultList();
		return patters.get(1);
	}

	
	
	
	
	/*****************************  delete pattern  *************************************/
	@Override
	public JsonObject deletePattern(Pattern pattern) {
		System.out.println("wsel lehné");
		System.out.println(pattern.getDoctor().getId());
		System.out.println(pattern.getId());
		
		if (findDoctorById(pattern.getDoctor().getId()) != null) {
			System.out.println("wsel lahné 2");
			if (findPatternById(pattern.getId()) != null)
			{
				System.out.println("wsel lahné 3");
			em.find(Pattern.class, pattern.getId()).setActif(false);
			return Json.createObjectBuilder().add("succes", "Pattern deleted").build();
			}
			else {
				return Json.createObjectBuilder().add("error", "you are not allowed for this action").build();
			}
		}
		return Json.createObjectBuilder().add("error", "you are not allowed for this action").build();

	}

	
	
	
	/*************************  get list pattern of a doctor  *************************************************/
	@Override
	public List<Pattern> getListPatternByMedecin(int idDoctor) {
		if (findDoctorById(idDoctor) != null) {
			TypedQuery<Pattern> query = em.createQuery(
					"select c from Pattern c where c.doctor = ( select t from User t where t.id = ?1 ) ",
					Pattern.class);
			query.setParameter(1, idDoctor);
			return query.getResultList();
		}
		return Collections.emptyList();
	}

	
	
	
	
	/**************************** Modify pattern	*****************************************************/
	@Override
	public JsonObject modifyPatternDescription(int idPattern, String pattern) {
		if (idPattern != 0 && pattern != null) {
			em.find(Pattern.class, idPattern).setLabel(pattern);
			return Json.createObjectBuilder().add("succes", "Pattern updated successfully").build();
		}
		return Json.createObjectBuilder().add("error", "issue wih the update").build();
	}


	
	
	
	/************************ Enable Account ****************************************************/
	public JsonObject enableUser(String username, String token) {
		User user = this.verifToken(username, token);
		System.out.println("" + username);
		if (user != null) {
			user.setToken(null);
			user.setActive(true);

			em.persist(user);
			em.flush();
			return Json.createObjectBuilder().add("succes", "your account has been activated successfully").build();
		} else {
			return Json.createObjectBuilder().add("error", "your username or your token is not correct").build();
		}

	}
		
	
	
	
		/*********************** Get user by userName ***********************************************************/
	public User getUserByUserName(String username) {
		try {
			User result = (User) em.createQuery("SELECT u from User u WHERE u.username = :username ")
					.setParameter("username", username).getSingleResult();
			System.out.println("aaaaaaaa");
			System.out.println("" + result);
			return result;
		} catch (NoResultException e) {
			return null;
		}

	}
		
		
		
		/********************************** verify user with token ************************************************/
	public User verifToken(String username, String token) {
		try {
			User result = (User) em
					.createQuery("SELECT u from User u WHERE u.username = :username and u.token = :token")
					.setParameter("username", username).setParameter("token", token).getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}

	}
		
		/*************************************  find User By Email and Password *****************************/
	public User loginEmail(String email, String password) {
		try {
			User result = (User) em
					.createQuery("SELECT u from User u WHERE u.email = :email and u.password = :password")
					.setParameter("email", email).setParameter("password", password).getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
		
		
		
		
		/****************************** find User by username and Password  *******************************/
	public User loginUsername(String username, String password) {
		try {
			User result = (User) em
					.createQuery("SELECT u from User u WHERE u.username = :username and u.password = :password")
					.setParameter("username", username).setParameter("password", password).getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	/******************** trait appointment acceptation or refusing       **********************/
	@Override
	public JsonObject treatAppointment(int appointment, Status newstate) {

		if (appointment != 0) {
			if (em.find(Appointment.class, appointment).getStatus() != Status.achieved) {
				if (newstate.equals(Status.confirmed) || newstate.equals(Status.refused)) {
					if (getDoctorFromAppointment(appointment).isActive() == true) {
						Appointment appoint = em.find(Appointment.class, appointment);
						appoint.setStatus(newstate);
						em.persist(appoint);
						em.flush();
						return Json.createObjectBuilder().add("succes", "Status updated Successfully")
								.add("appointment id", appoint.getId()).build();
					} else {
						return Json.createObjectBuilder().add("error", "your account is disabled").build();
					}
				} else {
					return Json.createObjectBuilder().add("error", "Status missmatch").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "appontment already achieved").build();
			}

		} else {
			return Json.createObjectBuilder().add("error", "appointment does not exist").build();
		}

	}


		/********************* mark an appointment achieved status    *****************************/
		@Override
		public JsonObject markAchievedAppointment(int appointment) {
			if (appointment != 0) {
				if (getDoctorFromAppointment(appointment).isActive() == true) {
					Appointment appoint = em.find(Appointment.class, appointment);
					appoint.setStatus(Status.achieved);
					em.persist(appoint);
					em.flush();
					return Json.createObjectBuilder().add("succes", "Appointment maked achieved")
							.add("appointment id", appoint.getId()).build();
				} else {
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}

			} else {
				return Json.createObjectBuilder().add("error", "appointment does not exist").build();
			}
		}


		
		
		
		/******************** get list of all appointment no treated yet **************************/
	@Override
	public List<Appointment> getListAppointmentNotTreated(int idDoctor) {
		if (idDoctor != 0 && getUserByid(idDoctor).isActive() == true) {
			TypedQuery<Appointment> query = em.createQuery(
					"select c from Appointment c where c.doctor = ( select t from User t where t.id = ?1 ) and c.status = ?2 ",
					Appointment.class);
			query.setParameter(1, idDoctor);
			query.setParameter(2, Status.waiting);
			return query.getResultList();
		} else {
			System.out.println("incorrect doctor or inactivated");
			return null;
		}
	}


		/******************** get list of all appointment in specific date **************************/
	@Override
	public List<Appointment> getListAppointmentForSpecificDate(int idDoctor, Date startDate, Date endDate) {
		if (idDoctor != 0 && getUserByid(idDoctor).isActive() == true) {
			if (startDate != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				if (startDate.compareTo(endDate) < 0 || startDate.compareTo(endDate) == 0) {
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					TypedQuery<Appointment> query = em.createQuery(
							"select c from Appointment c where c.doctor = ( select t from User t where t.id = ?1 ) and c.date BETWEEN ?2  AND ?3  ",
							Appointment.class);
					query.setParameter(1, idDoctor);
					query.setParameter(2, startDate);
					query.setParameter(3, endDate);
					System.out.println(formatter.format(startDate));
					System.out.println(endDate);
					return query.getResultList();
				} else {
					return null;
				}

			} else
				return null;
		}
		return null;
	}


		/**************** get doctor from appointment   ***********************************/
	@Override
	public Doctor getDoctorFromAppointment(int appointment) {

		try {
			Doctor doc = (Doctor) em
					.createQuery(
							"SELECT u from User u WHERE u.id = ( select a.doctor from Appointment a where a.id = :idAppointment )")
					.setParameter("idAppointment", appointment).getSingleResult();

			return doc;

		} catch (NoResultException e) {
			return null;
		}
	}






		



		
		
		
		
		
		
		
		
		
		
	
	

}



