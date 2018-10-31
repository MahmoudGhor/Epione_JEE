package tn.esprit.pi.epione.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import tn.esprit.pi.epione.persistence.AppointementAlternatif;

public class AppEncoder implements Encoder.Text<AppointementAlternatif> {

    @Override
    public String encode(AppointementAlternatif message) throws EncodeException {
    	DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
    	String date = d.format(message.getDate());

        return Json.createObjectBuilder()
                       .add("date", date)
                       .add("msg", message.getMsg())
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