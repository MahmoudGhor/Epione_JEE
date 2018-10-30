package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@Singleton
@ServerEndpoint(value = "/chatUsers", configurator = CookieServerConfigurator.class)
public class WebSocketEndpointChatUsers {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        String username = getUsername((List<String>)endpointConfig.getUserProperties().get("cookie"));
        if (username == null) {
            throw new RuntimeException("Username cookie not found");
        }
        sessions.put(username, session);
        updateUsersList();
    }
    
    public void updateUsersList() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        sessions.keySet().forEach(user -> arrayBuilder.add(user));
        builder.add("type", "users");
        builder.add("content", arrayBuilder);
        String message = builder.build().toString();
        sessions.values().parallelStream()
                .forEach(session -> session.getAsyncRemote().sendText(message));
    }
    
    private String getUsername(List<String> cookies) {
        return cookies.stream()
                .filter(cookieString -> cookieString.startsWith("username"))
                .map(cookieString -> cookieString.split("=")[1])
                .findAny().get();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try (JsonReader jr = Json.createReader(new StringReader(message))) {
            JsonObject jo = jr.readObject();
            String toUser = jo.getString("toUser");
            sessions.get(toUser).getAsyncRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String user = getUserForSession(session);
        sessions.remove(user);
        updateUsersList();
    }

    private String getUserForSession(Session session) {
        String key = sessions.keySet().stream()
                .filter(t -> sessions.get(t).equals(session))
                .findAny().get();
        return key;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException ex) {
        }
    }
}
