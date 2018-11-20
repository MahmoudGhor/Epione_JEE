package tn.esprit.pi.epione.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSONObjectConvertor;

import tn.esprit.pi.epione.iservices.UserServiceLocal;
import tn.esprit.pi.epione.persistence.Admin;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Planning;
import tn.esprit.pi.epione.persistence.Speciality;
import tn.esprit.pi.epione.persistence.Status;
import tn.esprit.pi.epione.persistence.User;
import tn.esprit.pi.epione.util.Mail;
import tn.esprit.pi.epione.util.Utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;


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
		patient.setType("patient");
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
		doctor.setType("doctor");
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
		System.out.println(pattern.getId());
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

	
	
	
	/*************************  get list pattern of a doctor  *************************************************/
	@Override
	public List<Pattern> getListPatternByMedecin(int idDoctor) {
		if (findDoctorById(idDoctor) != null) {
			TypedQuery<Pattern> query = em.createQuery(
					"select c from Pattern c where c.doctor = ( select t from User t where t.id = ?1 ) and c.isActif = ?2  ",
					Pattern.class);
			System.out.println("ena lennnnnnnnnnnnnnnnna");
			query.setParameter(1, idDoctor);
			query.setParameter(2, true);
			return query.getResultList();
		}
		return Collections.emptyList();
	}
	
	/*************************  get list disabled pattern of a doctor  *************************************************/
	@Override
	public List<Pattern> getListPatternDisabledByMedecin(int idDoctor) {
		if (findDoctorById(idDoctor) != null) {
			TypedQuery<Pattern> query = em.createQuery(
					"select c from Pattern c where c.doctor = ( select t from User t where t.id = ?1 ) and c.isActif = ?2  ",
					Pattern.class);
			System.out.println("ena lennnnnnnnnnnnnnnnna");
			query.setParameter(1, idDoctor);
			query.setParameter(2, false);
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
		System.out.println("wsell éééé");
		System.out.println(username);
		try {
			System.out.println("wsel welyeeeeeeeey");
			TypedQuery<User> query = em.createQuery("select c from User c where c.username = ?1 ", User.class);
			query.setParameter(1, username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("wsel lel catch yéééé");
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


	
	/*******************  get calendar   ************************************************/
	
    private static final String APPLICATION_NAME = "Epione_JEE";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
   
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = UserService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receier = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receier).authorize("user");
    }

		@Override
		public String getCaldendar() throws GeneralSecurityException, IOException {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	                .setApplicationName(APPLICATION_NAME)
	                .build();

	        // List the next 10 events from the primary calendar.
	        DateTime now = new DateTime(System.currentTimeMillis());
	        Events events = service.events().list("primary")
	                .setMaxResults(10)
	                .setTimeMin(now)
	                .setOrderBy("startTime")
	                .setSingleEvents(true)
	                .execute();
	        List<Event> items = events.getItems();
	        if (items.isEmpty()) {
	            System.out.println("No upcoming events found.");
	        } else {
	            System.out.println("Upcoming events");
	            for (Event event : items) {
	                DateTime start = event.getStart().getDateTime();
	                if (start == null) {
	                    start = event.getStart().getDate();
	                }
	                System.out.printf("%s (%s)\n", event.getSummary(), start);
	            }
	        }
			return "ali";
		}



		@Override
		public String setCalendar() throws GeneralSecurityException {
			/*Details details = new Details();
		    details.setClientId("59629572033-0o5jeipeu242b1kpvo39sbrg4c1qbggg.apps.googleusercontent.com");
		    details.setClientSecret("fm74KXQw4G_VvIB0cq959wWX");
		    
		    String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
		    String scope = "https://www.googleapis.com/auth/content";*/
		    
		    
			Event event = new Event()
				    .setSummary("Google I/O 2015")
				    .setLocation("800 Howard St., San Francisco, CA 94103")
				    .setDescription("A chance to hear more about Google's developer products.");

				DateTime startDateTime = new DateTime("2018-10-31T09:00:00-00:00");
				EventDateTime start = new EventDateTime()
				    .setDateTime(startDateTime)
				    .setTimeZone("America/Los_Angeles");
				event.setStart(start);

				DateTime endDateTime = new DateTime("2018-10-31T10:00:00-00:00");
				EventDateTime end = new EventDateTime()
				    .setDateTime(endDateTime)
				    .setTimeZone("America/Los_Angeles");
				event.setEnd(end);

				String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
				event.setRecurrence(Arrays.asList(recurrence));

				EventAttendee[] attendees = new EventAttendee[] {
				    new EventAttendee().setEmail("mahmoud.ghorbel@esprit.tn"),
				};
				event.setAttendees(Arrays.asList(attendees));
			
				EventReminder[] reminderOverrides = new EventReminder[] {
				    new EventReminder().setMethod("email").setMinutes(24 * 60),
				    new EventReminder().setMethod("popup").setMinutes(10),
				};
				Event.Reminders reminders = new Event.Reminders()
				    .setUseDefault(false)
				    .setOverrides(Arrays.asList(reminderOverrides));
				event.setReminders(reminders);

				String calendarId = "mahmoud.ghorbel@esprit.tn";
				try {
					// com.google.api.services.calendar.Calendar service;
					final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
					 com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				                .setApplicationName(APPLICATION_NAME)
				                .build();

					event = service.events().insert(calendarId , event).execute();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.printf("Event created: %s\n", event.getHtmlLink());
				return event.getHtmlLink();
		}



		@Override
	public JsonObject updateDoctor(Doctor doctor) {
		if (em.find(Doctor.class, doctor.getId()) != null) {
			if (em.find(Doctor.class, doctor.getId()).isActive() == true) {
				if (em.find(Doctor.class, doctor.getId()).getConnected() == true) {

					Doctor d = em.find(Doctor.class, doctor.getId());
					if (doctor.getFirstname() != null)
						d.setFirstname(doctor.getFirstname());
					if (doctor.getLastname() != null)
						d.setLastname(doctor.getLastname());
					if (doctor.getBiography() != null)
						d.setBiography(doctor.getBiography());
					if (doctor.getOffice_Number() != null)
						d.setOffice_Number(doctor.getOffice_Number());
					if (doctor.getWebsite() != null)
						d.setWebsite(doctor.getWebsite());
					if (doctor.getPaymentMethod() != null)
						d.setPaymentMethod(doctor.getPaymentMethod());
					if (doctor.getOfficeAdress() != null)
						d.setOfficeAdress(doctor.getOfficeAdress());
					if (doctor.getRemboursement() != null)
						d.setRemboursement(doctor.getRemboursement());
					if (doctor.getUsername() != null)
						d.setUsername(doctor.getUsername());
					if (doctor.getDoctolib() != null)
						d.setDoctolib(doctor.getDoctolib());
					if (doctor.getPicture() != null)
						d.setPicture(doctor.getPicture());
					if (doctor.getDoctolib() != null)
						d.setDoctolib(doctor.getDoctolib());
					if (doctor.getDoctolib() != null)
						d.setDoctolib(doctor.getDoctolib());
					if ((doctor.getEmail() != null) && (Utils.emailValidator(doctor.getEmail())))
						d.setEmail(doctor.getEmail());
					if (doctor.getPassword() != null) {
						try {
							d.setPassword(Utils.toMD5(doctor.getPassword()));
						} catch (NoSuchAlgorithmException e) {
							return Json.createObjectBuilder().add("error", "the password is too weak").build();

						}
					}

					em.persist(d);
					em.flush();

					return Json.createObjectBuilder().add("succes", "Doctor updated successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "Doctor not exist").build();
		}
	}


		/*********************** add a day time  ****************************/
	@Override
	public JsonObject addPlanningForOneDay(int idDoctor, Date day, Timestamp startTime, Timestamp endTime,
			int timeMeeting) {

		if (idDoctor != 0) {
			if (findDoctorById(idDoctor) != null) {
				Doctor doc = em.find(Doctor.class, idDoctor);
				if (doc.isActive() == true) {
					if (day != null) {
						System.out.println("bbbbbbbbbbbbbbbbbb");
						System.out.println(startTime);
						System.out.println(endTime);
						System.out.println("bbbbbbbbbbbbbbbbbb");
						Planning plan = new Planning();
						plan.setDay(day);
						plan.setStart_at(startTime);
						plan.setEnd_at(endTime);
						plan.setDisponibility(false);
						plan.setDoctor(doc);

						em.persist(plan);
						return Json.createObjectBuilder().add("succes", "your account has been activated successfully")
								.build();
					} else {
						return Json.createObjectBuilder().add("error", "specify the day please!").build();
					}

				}
				{
					return Json.createObjectBuilder().add("error", "the doctor is disabled").build();
				}
			}

			{
				return Json.createObjectBuilder().add("error", "you are not a doctor").build();
			}
		}
		{
			return Json.createObjectBuilder().add("error", "no doctor inserted").build();
		}
	}


		/************** select liste pattern by periode **********************/
	@Override
	public List<Pattern> selectListPatternByPeriode(int idDoctor, int periode) {
		if (findDoctorById(idDoctor) != null) {
			TypedQuery<Pattern> query = em.createQuery(
					"select c from Pattern c where c.doctor = ( select t from User t where t.id = ?1 ) and c.periode = ?2 and c.isActif =1 ",
					Pattern.class);
			query.setParameter(1, idDoctor);
			query.setParameter(2, periode);
			return query.getResultList();
		}
		return Collections.emptyList();
	}
		
		

	@Override
	public JsonObject updatePatient(Patient patient) {
		if (em.find(Patient.class, patient.getId()) != null) {
			if (em.find(Patient.class, patient.getId()).isActive() == true) {
				if (em.find(Patient.class, patient.getId()).getConnected() == true) {

					Patient p = em.find(Patient.class, patient.getId());
					if (patient.getFirstname() != null)
						p.setFirstname(patient.getFirstname());
					if (patient.getLastname() != null)
						p.setLastname(patient.getLastname());

					if ((patient.getEmail() != null) && (Utils.emailValidator(patient.getEmail())))
						p.setEmail(patient.getEmail());
					if (patient.getCivil_status() != null)
						p.setCivil_status(patient.getCivil_status());
					if (patient.getBirthday() != null)
						p.setBirthday(patient.getBirthday());
					if (patient.getPhone() != null)
						p.setPhone(patient.getPhone());
					if (patient.getUsername() != null)
						p.setUsername(patient.getUsername());
					if (patient.getPicture() != null)
						p.setPicture(patient.getPicture());

					if (patient.getPassword() != null) {
						try {
							p.setPassword(Utils.toMD5(patient.getPassword()));
						} catch (NoSuchAlgorithmException e) {
							return Json.createObjectBuilder().add("error", "the password is too weak").build();

						}
					}

					em.persist(p);
					em.flush();

					return Json.createObjectBuilder().add("succes", "Patient updated successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "Patient not exist").build();
		}
	}
		
		
		public static final Date END_OF_TIME;
		public static final TimeZone UTC;

	public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	static {
		UTC = TimeZone.getTimeZone("UTC");
		TimeZone.setDefault(UTC);
		final Calendar c = new GregorianCalendar(UTC);
		c.set(1, 0, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);

		c.setTime(new Date(Long.MAX_VALUE));
		END_OF_TIME = c.getTime();
	}


		@Override
	public JsonObject updateAdmin(Admin admin) {
		if (em.find(Patient.class, admin.getId()) != null) {
			if (em.find(Patient.class, admin.getId()).isActive() == true) {
				if (em.find(Patient.class, admin.getId()).getConnected() == true) {

					Patient a = em.find(Patient.class, admin.getId());
					if (admin.getFirstname() != null)
						a.setFirstname(admin.getFirstname());
					if (admin.getLastname() != null)
						a.setLastname(admin.getLastname());

					if ((admin.getEmail() != null) && (Utils.emailValidator(admin.getEmail())))
						a.setEmail(admin.getEmail());
					if (admin.getCivil_status() != null)
						a.setCivil_status(admin.getCivil_status());
					if (admin.getBirthday() != null)
						a.setBirthday(admin.getBirthday());
					if (admin.getPhone() != null)
						a.setPhone(admin.getPhone());
					if (admin.getUsername() != null)
						a.setUsername(admin.getUsername());
					if (admin.getPicture() != null)
						a.setPicture(admin.getPicture());

					if (admin.getPassword() != null) {
						try {
							a.setPassword(Utils.toMD5(admin.getPassword()));
						} catch (NoSuchAlgorithmException e) {
							return Json.createObjectBuilder().add("error", "the password is too weak").build();

						}
					}

					em.persist(a);
					em.flush();

					return Json.createObjectBuilder().add("succes", "Admin updated successfully").build();
				} else {
					return Json.createObjectBuilder().add("error", "you must connect before").build();
				}
			} else {
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "User not exist").build();
		}
	}
	/******************** makePlanningforAnAppointment ********************************/
	@Override
	public JsonObject makePlanningForAnAppointment(int idAppointment, int idPlanning) {
		if (idAppointment != 0 && idPlanning != 0) {
			Appointment appointment = em.find(Appointment.class, idAppointment);
			Planning planning = em.find(Planning.class, idPlanning);
			if (appointment != null && planning != null) {
				if (planning.isDisponibility() == false) {
					int x = (planning.getEnd_at().getHours() * 60 + planning.getEnd_at().getMinutes())
							- planning.getStart_at().getHours() * 60 + planning.getStart_at().getMinutes();
					if (appointment.getPattern().getPeriode() == x) {
						// Query query = em.createQuery("update a from
						// Appointment a set a.")
						final SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);
						sdf.setTimeZone(UTC);
						appointment.setPlanning(planning);
						planning.setDisponibility(true);
						em.persist(appointment);
						em.persist(planning);
						String description = "Appointment : " + appointment.getPattern().getLabel() + " for "
								+ appointment.getPatient().getFirstname() + " "
								+ appointment.getPatient().getLastname();
						Date endDate = planning.getEnd_at();
						Date startDate = planning.getStart_at();
						String dat1;
						String dat2;
						String summary = appointment.getPatient().getUsername();
						// DateTimeFormatter formatter;
						// formatter = new DateTimeFormatter();
						// dat1=
						// startDate.getYear()+"-"+startDate.getMonth()+"-"+startDate.getDate()+"T"+startDate.getHours()+":"+startDate.getMinutes()+":"+startDate.getSeconds()+"000Z";
						// Date parseDate = formatter.parse(startDate);
						// dat2=
						// endDate.getYear()+"-"+endDate.getMonth()+"-"+endDate.getDate()+"T"+endDate.getHours()+":"+endDate.getMinutes()+":"+endDate.getSeconds()+"000Z";

						JSONObject json = new JSONObject();
						JSONArray array = new JSONArray();
						JSONArray array1 = new JSONArray();
						JSONObject item = new JSONObject();
						JSONObject item1 = new JSONObject();
						json.put("summary", summary);

						item.put("dateTime", sdf.format(endDate));
						item1.put("dateTime", sdf.format(startDate));
						array.put(item);
						array1.put(item1);
						json.put("start", array1);
						json.put("end", array);

						json.put("description", description);
						System.out.println("add this json to postman");
						System.out.println(json.toString());

						return Json.createObjectBuilder().add("succes", "appointment is set for the planning").build();

					} else {
						return Json.createObjectBuilder()
								.add("error", "periode appointment not the same of the planning").build();
					}
				} else {
					return Json.createObjectBuilder().add("error", "Planning already reserved").build();
				}

			} else {
				return Json.createObjectBuilder().add("error", "fail to find appointment or planning").build();
			}
		} else {
			return Json.createObjectBuilder().add("error", "issue wih the Appointment or the planning").build();
		}
	}


	/********************** show appointments of the day ***************************/
	@SuppressWarnings("unchecked")
	@Override
	public List<Appointment> selectAppointmentOfToday(int idDoctor) {
		if (idDoctor != 0) {
			if (findDoctorById(idDoctor) != null) {
				Doctor doc = em.find(Doctor.class, idDoctor);
				if (doc.isActive() == true) {
					Date d = new Date();
					TypedQuery<Appointment> query = em.createQuery(
							"select c from Appointment c where c.doctor = ( select t from User t where t.id = ?1 ) and c.date = ?2 ",
							Appointment.class);
					query.setParameter(1, idDoctor);
					query.setParameter(2, d);
					return query.getResultList();

				} else {
					return (List<Appointment>) Json.createObjectBuilder().add("error", "Doctor is disabled").build();
				}

			} else {
				return (List<Appointment>) Json.createObjectBuilder().add("error", "Doctor not fouds").build();
			}

		} else {
			return (List<Appointment>) Json.createObjectBuilder().add("error", "issue wih the doctor").build();
		}
	}


/*********************************** get liste doctors *****************************/
	@Override
	public List<Doctor> getListDoctors() {
		System.out.println("wsel lahné 2");
		TypedQuery<Doctor> query = em.createQuery("select c from Doctor c where c.active = ?1 ", Doctor.class);
		query.setParameter(1, true);
		return query.getResultList();

	}


/*********************** find user by username ************************************/
	@Override
	public User findUserByUserName(String username) {
		TypedQuery<User> query = em.createQuery("select c from User c where c.username = ?1 ", User.class);
		query.setParameter(1, true);
		return query.getSingleResult();
	}

/*************  update pattern  ************************/

	@Override
	public JsonObject updatePattern(Pattern pattern) {
		if (em.find(Pattern.class, pattern.getId()) != null) {
			Pattern pat = em.find(Pattern.class, pattern.getId());
			if (pattern.getLabel() != null) {
				pat.setLabel(pattern.getLabel());
			}
			if (pattern.getPeriode() != 0) {
				pat.setPeriode(pattern.getPeriode());
			}
			if (pattern.getPrice() != 0) {
				pat.setPrice(pattern.getPrice());
			}
			em.persist(pat);
			em.flush();

			return Json.createObjectBuilder().add("succes", "Pattern updated successfully").build();
		} else {
			return Json.createObjectBuilder().add("error", "Pattern not exist").build();
		}
	}


	
	
	
	
	
	
		
}
		
		
		
		
		
		











		



		
		
		
		
		
		
		
		
		
		
	
	





