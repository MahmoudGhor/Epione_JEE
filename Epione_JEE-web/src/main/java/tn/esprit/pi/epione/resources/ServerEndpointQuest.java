package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import tn.esprit.pi.epione.persistence.ChatMessage;
import tn.esprit.pi.epione.persistence.Key;
import tn.esprit.pi.epione.util.MessageDecoder;
import tn.esprit.pi.epione.util.MessageEncoder;

@Singleton
@javax.websocket.server.ServerEndpoint(value = "/chat/quest/{key}", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class ServerEndpointQuest {

    private final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(Session session, ChatMessage message, @PathParam("key") String key) {
    	String isPatient = (String) session.getUserProperties().get("typeUser");
    	
    	if(isPatient == null)
    		session.getUserProperties().put("typeUser", message.getIsPatient());
    	
        sessions.get(key).parallelStream().forEach(session2 -> {
            if (session == session2) {
                return;
            }
            try {
				session2.getBasicRemote().sendObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("key") String key) {
        if (!sessions.containsKey(key)) {
            sessions.put(key, new ArrayList<>());
        }
        sessions.get(key).add(session);
        
       /* if(Connected_User.getUser() instanceof Patient)
        	session.getUserProperties().put("typeUser", "patient");*/
    }

    @OnClose
    public void onClose(Session session, @PathParam("key") String key) {
    	
    	String isPatient = (String) session.getUserProperties().get("typeUser");
    	if(isPatient.equals("true")){
    		sessions.remove(key);
    	}
    	else{
    		sessions.get(key).remove(session);
    	}
    }

    @OnError
    public void onError(Session session, @PathParam("key") String key, Throwable throwable) {
    	System.out.println("error");
        sessions.get(key).remove(session);
        try {
            session.close();
        } catch (IOException ex) {
        }
    }

    public void send(ChatMessage message, String key) {
        if (!sessions.containsKey(key)) {
            return;
        }
        sessions.get(key).parallelStream().forEach(session2 -> {
            try {
				session2.getBasicRemote().sendObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    public List<Key> getPatientId() {
    	
    	ArrayList<Key> keys = new ArrayList<Key>();
    	Iterator<String> iterator = sessions.keySet().iterator(); 
        while (iterator.hasNext()) { 
        	Key k = new Key();
        	k.setKey(iterator.next());
        	keys.add(k);
		} 
        
        return keys;
    }
    
}