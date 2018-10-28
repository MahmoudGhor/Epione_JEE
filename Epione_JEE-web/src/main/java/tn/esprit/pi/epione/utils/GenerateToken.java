package tn.esprit.pi.epione.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class GenerateToken {
	@Context
	private UriInfo uriInfo;

	public String issueToken(String username) {
		return Jwts.builder().claim("id", username).signWith(SignatureAlgorithm.HS512, "test").compact();

	}
}
