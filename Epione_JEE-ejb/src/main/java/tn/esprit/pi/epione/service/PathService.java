package tn.esprit.pi.epione.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.pi.epione.iservices.PathServiceLocal;
import tn.esprit.pi.epione.iservices.PathServiceRemote;
import tn.esprit.pi.epione.persistence.Appointment;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.Notification;
import tn.esprit.pi.epione.persistence.Patient;
import tn.esprit.pi.epione.persistence.Recommandation;
import tn.esprit.pi.epione.persistence.Speciality;



@Stateless
public class PathService implements PathServiceLocal{

    @PersistenceContext(unitName = "Epione_JEE-ejb")
	private EntityManager em;
	
	
	@Override
	public JsonObject addRecommandation(int a, int d,String type) {
		
		JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
	
		Recommandation r = new Recommandation();
		r.setCreated_at(new Date());
		r.setValidation(false);
		r.setAppointment(em.find(Appointment.class,a));
		r.setType(type);
		Appointment appointment = new Appointment();
		appointment = em.find(Appointment.class,a);
		
		Doctor doc = em.find(Doctor.class,d);
		
		
		
		if (doc==null)
		{
			errorBuilder.add("Error","This is not a doctor");
			return errorBuilder.build();
		}
		else if (appointment.getDoctor().equals(doc))
		{
			
			errorBuilder.add("Error","Can't recommand yourself");
			return errorBuilder.build();
		}
		else {
	    r.getDoctors().add(doc);
	    appointment.getRecommandations().add(r);
	   this.addNotification(a,d);
	    em.persist(r);
	    em.flush();
		JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
		succesBuilder.add("succes", "Recommandation created");
		return succesBuilder.build();
		}
		
	}
	
	
	@Override
	public JsonObject addPath(int a,String type) {
		
		JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
	
		Recommandation r = new Recommandation();
		r.setCreated_at(new Date());
		r.setValidation(false);
		r.setAppointment(em.find(Appointment.class,a));
		Appointment appointment = new Appointment();
		appointment = em.find(Appointment.class,a);
		r.setValidation(true);
	  r.setRecommandPath(true);
	  
	   r.setType(type);
	   if(r.getType()==null || appointment.equals(null))
	   {
		   
		   errorBuilder.add("error","You have to insert a type or insert a valid appointment");
		   return errorBuilder.build();
		   
	   }
	    appointment.getRecommandations().add(r);
	    em.persist(r);
	    em.flush();
		JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
		succesBuilder.add("succes", "Path created");
		return succesBuilder.build();
		
		
	}
	
	
	public List<Recommandation>findAllPathsByPatient(int patient)
	{
		JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
		
		Patient p = em.find(Patient.class,patient);

		List<Recommandation> allPaths = new ArrayList<>();
		List<Recommandation> recommandations = new ArrayList<>();
		List<Appointment> appointments = new ArrayList<>();
		
		TypedQuery<Appointment> query =em.createQuery("select a from Appointment a where a.patient=?1",Appointment.class);
		query.setParameter(1,em.find(Patient.class,patient));
		
		for (int i=0;i<query.getResultList().size();i++)
		{
			
			recommandations = query.getResultList().get(i).getRecommandations();
			for (int j=0;j<recommandations.size();j++)
				
			{
				if(recommandations.get(j).getType()!=null)
				{
				allPaths.add(recommandations.get(j));
				
				}
			}
			
			
			
		}
		
		return allPaths;
		
	}
	
	

	
	 public List<Recommandation> findAllRecommandation() {
	      
		 
			TypedQuery<Recommandation> query= em.createQuery("select r from Recommandation r",Recommandation.class);
			return query.getResultList();
		 
		 
	    }
	
	
	 public List<Recommandation> findAllRecommandationPatient(int p) {
	      
		 
			TypedQuery<Recommandation> query= em.createQuery("select r from Recommandation r",Recommandation.class);
			return query.getResultList();
		 
		 
	    }
	 
	 
	 @Override
	 public List<Recommandation> findAllRecommandationByDoctor(int d) {
	      
		 
		Doctor doc=em.find(Doctor.class, d);
		List<Recommandation> parcours = new ArrayList();
		List<Recommandation> ListRecommandations = new ArrayList();
		
		TypedQuery<Recommandation> query = em.createQuery("select r from Recommandation r",Recommandation.class);
	
		parcours = query.getResultList();
		
		for(int i=0;i<parcours.size();i++)
		{
			
			Doctor docteur = parcours.get(i).getAppointment().getDoctor();
		
				
				if (docteur.equals(doc))
				{
					ListRecommandations.add(query.getResultList().get(i));
					
					
				}
				
				
			
		} 
		
		return ListRecommandations;
		 
	    }
	 
		@Override
		public String getRecommandationById(int recommandationId) {
			return (em.find(Recommandation.class, recommandationId).toString());
		}
	
	  
	
		@Override
		public List<Recommandation> getRecommandationsByAppointment(int app){
		
			TypedQuery<Recommandation> query =em.createQuery("select r from Recommandation r where r.appointment=?1",Recommandation.class);
			query.setParameter(1,em.find(Appointment.class,app));
			return query.getResultList();
			
		}
		
		@Override
		public List<Recommandation> getRecommandationsByPatient(int patient){
		
			List<Recommandation> FinalList = new ArrayList<>();
			List<Recommandation> ListRecommandations = new ArrayList<>();
			List<Appointment> ListAppointments= new ArrayList<>();
			
			Patient p = em.find(Patient.class,patient);
			TypedQuery<Appointment> query =em.createQuery("select a from Appointment a where a.patient=?1",Appointment.class);
			query.setParameter(1,em.find(Patient.class,patient));
			
			ListAppointments = query.getResultList();
			
			for (int i=0;i<ListAppointments.size();i++)
			{
				TypedQuery<Recommandation> query2 =em.createQuery("select r from Recommandation r where r.appointment=?1 order by r.created_at",Recommandation.class);
				query2.setParameter(1,em.find(Appointment.class,ListAppointments.get(i).getId()));
				ListRecommandations = query2.getResultList();
				for (int j=0;j<ListRecommandations.size();j++)
				{
					FinalList.add(ListRecommandations.get(j));
				
				}
			}
		
			return FinalList;
			
		}
		
		
		
		@Override
		 public JsonObject ValidateRecommandation(int recommandation,int doctor)
		
		 {
			
			int position=0;
			
			JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
			
	         
	
			Recommandation r= em.find(Recommandation.class,recommandation);
			
			Appointment a = r.getAppointment();
			//TypedQuery<Recommandation> query =em.createQuery("select r from Recommandation r where r.appointment=?1",Recommandation.class);
			//query.setParameter(1,em.find(Appointment.class,r.getAppointment()));
			
			
		
			
			List<Recommandation> Listrecommandation = new ArrayList<>();
			
			Listrecommandation = this.getRecommandationsByPatient(a.getPatient().getId());
			//Listrecommandation = query.getResultList();
			
		
			
		for(int x=0;x<Listrecommandation.size();x++)
		{
			if (Listrecommandation.size()==1)
			{
				for (int i=0 ; i<r.getDoctors().size();i++)
				{
				
					
					if (r.getDoctors().get(i).equals(em.find(Doctor.class,doctor)))
					{
						if (r.isValidation()==true)
						{   JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
							succesBuilder.add("NO", "You have already validated this path");
							return succesBuilder.build();
							
						}
						
					 r.setValidation(true);
					 
						JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
						this.addNotificationToClient(recommandation,doctor);
						succesBuilder.add("succes", "Recommandation validated and a notification will be sent");
						return succesBuilder.build();
					}
					if(i==r.getDoctors().size()-1) {

						
						errorBuilder.add("Error","You don't have the permission to update the recommandation");
					   return errorBuilder.build();
					}
				}
				
			}
			
			else if ( (Listrecommandation.get(0).equals(r) ) ) 
			{
		          
				for (int i=0 ; i<r.getDoctors().size();i++)
				{
				
				
					
					if (r.getDoctors().get(i).equals(em.find(Doctor.class,doctor)))
					{
						if (r.isValidation()==true)
						{   JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
							succesBuilder.add("NO", "You have already validated this path");
							return succesBuilder.build();
							
						}
						
					 r.setValidation(true);
						JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
						this.addNotificationToClient(recommandation,doctor);
						succesBuilder.add("succes", "Recommandation validated and a notification will be sent");
						return succesBuilder.build();
					}
					if(i==r.getDoctors().size()-1) {

						
						errorBuilder.add("Error","You don't have the permission to update the recommandation");
					   return errorBuilder.build();
					}
				}
				
			}
			
			else if ( (Listrecommandation.get(x).equals(r) ) && (Listrecommandation.get(x-1).isValidation()==true ) ) 
			{
		          
				for (int i=0 ; i<r.getDoctors().size();i++)
				{
				
				
					
					if (r.getDoctors().get(i).equals(em.find(Doctor.class,doctor)))
					{
						if (r.isValidation()==true)
						{   JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
							succesBuilder.add("NO", "You have already validated this path");
							return succesBuilder.build();
							
						}
						
					 r.setValidation(true);
						JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
						this.addNotificationToClient(recommandation,doctor);
						succesBuilder.add("succes", "Recommandation validated and a notification will be sent");
						return succesBuilder.build();
					}
					if(i==r.getDoctors().size()-1) {

						
						errorBuilder.add("Error","You don't have the permission to update the recommandation");
					   return errorBuilder.build();
					}
				}
				
			}
			

			
			
		}
			
			
			

			
			errorBuilder.add("Error","The patient have to valid the previous recommandation");
		   return errorBuilder.build();
			
			
		 }
		
		
		@Override
		public List<Recommandation> getRecommandationsByRecommanderDoctor(int d){
		
			
			List<Recommandation> ListRecommandations = new ArrayList();
			List<Recommandation> parcours = new ArrayList();
			
			
			TypedQuery<Recommandation> query = em.createQuery("select r from Recommandation r",Recommandation.class);
		
			parcours = query.getResultList();
			
			
			for(int i=0;i<parcours.size();i++)
			{
				
				List Doctors = new ArrayList<>();
				Doctors = parcours.get(i).getDoctors();
				for (int j=0;j<parcours.get(i).getDoctors().size();j++)
				
				{
					
					if (Doctors.get(j).equals(em.find(Doctor.class,d)))
					{
						ListRecommandations.add(query.getResultList().get(i));
						
						
					}
					
					
				}
			} 
			  
			
			/*
			
			for (int i=0 ; i<em.find(Doctor.class,d).getRecommandations().size();i++)
			{
				
				ListRecommandations.add(em.find(Doctor.class,d).getRecommandations().get(i));
				
				
				
			}*/
			
			return ListRecommandations;
		}
		
		
		
		  @Override
		  public JsonObject addNotification(int appointment,int specialist)
		  {
			  
			  
				JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
				JsonObjectBuilder successBuilder=Json.createObjectBuilder();
				
			  Appointment a = em.find(Appointment.class,appointment);
			  Doctor spec = em.find(Doctor.class,specialist);
			 if (a==null)
			 {
				 errorBuilder.add("Error","This is not a valid appointment");
				 return errorBuilder.build();
				 
			 }
			 
			 if (spec==null)
			 {
				 errorBuilder.add("Error","This is not a valid Specialist");
				 return errorBuilder.build();
				 
			 }
			  
			  Doctor d = a.getDoctor();
			  Patient p = a.getPatient();
			  Notification n = new Notification();
			  n.setSending_date(new Date());
			  n.setSentToDoctor(false);
			  n.setSentToPatient(false);
			  n.setComm(d.getFirstname()+" "+d.getLastname()+" Just recommanded the patient : "+p.getFirstname()+" "+p.getLastname()+" To the specialist :"+spec.getFirstname()+" "+spec.getLastname());
			  
			  n.setDoctor(spec);
			  n.setPatient(p);
			  
			  em.persist(n);
			
			  
			  
			  successBuilder.add("Success","A notification will be send to the patient and the specialist");
			  return successBuilder.build();
			  
			  
		  }
		  
		  
		  
		  @Override
		  public JsonObject addNotificationToClient(int recommandation,int specialist)
		  {
			  
			  
				JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
				JsonObjectBuilder successBuilder=Json.createObjectBuilder();
				
			  Recommandation r = em.find(Recommandation.class,recommandation);
			  Doctor spec = em.find(Doctor.class,specialist);
			 if (r==null)
			 {
				 errorBuilder.add("Error","This is not a valid recommandation");
				 return errorBuilder.build();
				 
			 }
			 
			 if (spec==null)
			 {
				 errorBuilder.add("Error","This is not a valid Specialist");
				 return errorBuilder.build();
				 
			 }
			  
			  Doctor d = r.getAppointment().getDoctor();
			  Patient p = r.getAppointment().getPatient();
			  Notification n = new Notification();
			  n.setSending_date(new Date());
			  n.setSentToDoctor(false);
			  n.setSentToPatient(false);
			  n.setComm(spec.getFirstname()+" "+spec.getLastname()+"Just validated the recommandation : "+r.getType()+" of the Patient"+p.getFirstname()+" "+p.getLastname());
			  
			  n.setDoctor(d);
			  n.setPatient(p);
			  
			  em.persist(n);
			
			  
			  
			  successBuilder.add("Success","A notification will be send to the patient and the specialist");
			  return successBuilder.build();
			  
			  
		  }
		  
		  
		  @Override
			public List<Notification> listNotifications(int patient)
			{
				
				List<Notification> notifications = new ArrayList<>();
				
				Patient p = em.find(Patient.class,patient);
			
				
				TypedQuery<Notification> query =em.createQuery("select n from Notification n where n.patient=?1 and n.sentToPatient=FALSE",Notification.class);
				query.setParameter(1,em.find(Patient.class,patient));
				
				return query.getResultList();
			
				
				
				
				
			}
		
		
		  @Override
			public List<Notification> listNotificationsForSpecialist(int doctor)
			{
				
				List<Notification> notifications = new ArrayList<>();
				
				Doctor d = em.find(Doctor.class,doctor);
			
				
				TypedQuery<Notification> query =em.createQuery("select n from Notification n where n.doctor=?1 and n.sentToDoctor=FALSE",Notification.class);
				query.setParameter(1,em.find(Doctor.class,doctor));
				
				return query.getResultList();
			
				
				
				
				
			}
		  
		  
		  @Override
		     public JsonObject OpenNotification(int notification,int user){
			  
				 Notification n =  em.find(Notification.class, notification);
				 JsonObjectBuilder successBuilder=Json.createObjectBuilder();
				 
			  if (em.find(Doctor.class,user)!=null && n.getDoctor()==em.find(Doctor.class,user) )
			  {
				  
				 
				  n.setSentToDoctor(true);
				  successBuilder.add("Success","The doctor opened the notification");
				  return successBuilder.build();
				  
			  }
			  
			  if (em.find(Patient.class,user)!=null && n.getPatient()==em.find(Patient.class,user) )
			  {
				  
		
				  n.setSentToPatient(true);
				  successBuilder.add("Success","The patient opened the notification");
				  return successBuilder.build();
				  
			  }
			  
			  
			  successBuilder.add("Error","Invalid notification and user");
			  return successBuilder.build();
			  
			  
			  
		  }
		  
		  
		  @Override
		  public JsonObject UpdateRecommandation(int recommandation,int specialist,String type,String justification,int newspecialist)
		  {
			  
			  Recommandation r = em.find(Recommandation.class,recommandation);
			  JsonObjectBuilder successBuilder=Json.createObjectBuilder();
			  Patient p = r.getAppointment().getPatient();
			  List<Recommandation> ListRecommandations = this.findAllRecommandationPatient(p.getId());
			  List<Doctor> newDoctor = new ArrayList<>();
			 
			  
			  for (int i=0;i<ListRecommandations.size();i++)
			  {
				  List<Doctor> ListDoctors = new ArrayList<>();
				  ListDoctors=ListRecommandations.get(i).getDoctors();
				for (int j=0;j<ListDoctors.size();j++)
				{
					
					if (ListDoctors.get(j).equals(em.find(Doctor.class,specialist) ))
					{
						
						r.setJustification(justification);
						newDoctor.add(em.find(Doctor.class,newspecialist));
						r.setDoctors(newDoctor);
						r.setType(type);
						  successBuilder.add("Success","Recommandation updated !");
						  return successBuilder.build();
						
					}
				}
				  
			  }
			  
			  successBuilder.add("Error","You don't have the permission to update");
			  return successBuilder.build();
			  
		  }
		  
		  
		  
			 public List<Patient> ListAllPatientsForSpecialist(int specialist)
			 
			 {
				 List<Patient> ListPatients = new ArrayList<>();
				 
				 
					TypedQuery<Recommandation> query =em.createQuery("select r from Recommandation r",Recommandation.class);
					
					for (int i=0;i<query.getResultList().size();i++)
					{
						
						for (int j=0;j<query.getResultList().get(i).getDoctors().size();j++)
						{
							
							if (query.getResultList().get(i).getDoctors().get(j).equals(em.find(Doctor.class,specialist)))
							{
								
						
								
								ListPatients.add(query.getResultList().get(i).getAppointment().getPatient());
								
								
								
							}
						}
						
					}
				 
				 return ListPatients.stream().distinct().collect(Collectors.toList());
			 }
		  
		
		
}
