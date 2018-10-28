package tn.esprit.pi.epione.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import tn.esprit.pi.epione.persistence.ChatMessage;
import tn.esprit.pi.epione.util.MessageDecoder;
import tn.esprit.pi.epione.util.MessageEncoder;

@javax.websocket.server.ServerEndpoint(value = "/chat", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class ServerEndpoint {

    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println( session.getId()+" joined the chat room. ");
        peers.add(session);
    }

    @OnMessage
    public void onMessage(ChatMessage message, Session session) throws IOException, EncodeException {
    	    	    	
        Iterator<Session> iterator = peers.iterator();
        while (iterator.hasNext()) {
        	iterator.next().getBasicRemote().sendObject(message);
		}
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
    	System.out.println( session.getId()+" left the chat room. ");
        peers.remove(session);
        //notify peers about leaving the chat room
        /*for (Session peer : peers) {
        	ChatMessage message = new ChatMessage();
            message.setSender("Server");
            message.setContent(format("%s left the chat room", (String) session.getUserProperties().get("user")));
            message.setReceived(new Date());
            peer.getBasicRemote().sendObject(message);
        }*/
    }

}