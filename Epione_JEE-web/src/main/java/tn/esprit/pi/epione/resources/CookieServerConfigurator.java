package tn.esprit.pi.epione.resources;

import java.util.List;
import java.util.Map;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class CookieServerConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String,List<String>> headers = request.getHeaders();
        sec.getUserProperties().put("cookie", headers.get("cookie"));
    }
    
}
