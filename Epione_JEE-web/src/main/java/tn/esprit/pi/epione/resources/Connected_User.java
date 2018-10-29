package tn.esprit.pi.epione.resources;

import tn.esprit.pi.epione.persistence.User;

public class Connected_User {
	
	private static User user;

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Connected_User.user = user;
	}

}
