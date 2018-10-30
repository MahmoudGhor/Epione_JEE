package tn.esprit.pi.epione.util;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import tn.esprit.pi.epione.persistence.ChatMessage;

public class MessageDecoder implements Decoder.Text<ChatMessage> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChatMessage decode(String textMessage) throws DecodeException {	
		ChatMessage message = new ChatMessage();
        JsonObject jsonObject = Json.createReader(new StringReader(textMessage)).readObject();
        message.setContent(jsonObject.getString("message"));
        message.setDoctorName(jsonObject.getString("doctorName"));
        message.setPatientName(jsonObject.getString("patientName"));
        message.setIsPatient(jsonObject.getString("isPatient"));
        return message;
	}

	@Override
	public boolean willDecode(String textMessage) {
		boolean flag = true;
		try {
			Json.createReader(new StringReader(textMessage)).readObject();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

    
}
