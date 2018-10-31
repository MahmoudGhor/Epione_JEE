package tn.esprit.pi.epione.util;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import tn.esprit.pi.epione.persistence.AppointementAlternatif;

public class AppDecoder implements Decoder.Text<AppointementAlternatif> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AppointementAlternatif decode(String textMessage) throws DecodeException {	
		AppointementAlternatif message = new AppointementAlternatif();
        JsonObject jsonObject = Json.createReader(new StringReader(textMessage)).readObject();
        DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date;
		try {
			date = (Date) d.parse(jsonObject.getString("date"));
			message.setDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.setMsg(jsonObject.getString("msg"));
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
