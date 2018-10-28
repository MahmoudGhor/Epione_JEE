package tn.esprit.pi.epione.util;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import tn.esprit.pi.epione.persistence.ChatMessage;

public class MessageEncoder implements Encoder.Text<ChatMessage> {

    @Override
    public String encode(ChatMessage message) throws EncodeException {
        return Json.createObjectBuilder()
                       .add("message", message.getContent())
                       .build().toString();
    }

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

}