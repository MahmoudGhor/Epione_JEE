package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import tn.esprit.pi.epione.util.MessageDecoder;
import tn.esprit.pi.epione.util.MessageEncoder;

@Singleton
@javax.websocket.server.ServerEndpoint(value = "/chat/quest/{key}", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class ServerEndpointQuest {

    private final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("key") String key) {
        sessions.get(key).parallelStream().forEach(session2 -> {
            if (session == session2) {
                return;
            }
            session2.getAsyncRemote().sendText(message);
        });
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("key") String key) {
        if (!sessions.containsKey(key)) {
            sessions.put(key, new ArrayList<>());
        }
        sessions.get(key).add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("key") String key) {
        sessions.get(key).remove(session);
    }

    @OnError
    public void onError(Session session, @PathParam("key") String key, Throwable throwable) {
        sessions.get(key).remove(session);
        try {
            session.close();
        } catch (IOException ex) {
        }
    }

    public void send(String message, String key) {
        if (!sessions.containsKey(key)) {
            return;
        }
        sessions.get(key).parallelStream().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }
    
    public List<String> getPatientId() {
        return new ArrayList<>(sessions.keySet());
    }


}