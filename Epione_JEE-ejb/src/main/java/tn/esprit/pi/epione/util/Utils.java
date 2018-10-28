package tn.esprit.pi.epione.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utils {
	
	public static boolean emailValidator(String email) {
		boolean isValid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
			}
		catch (AddressException e) 
		{
			 isValid = false;
		}
		return isValid;
	}
	
	public static String toMD5(String passwordToHash) throws NoSuchAlgorithmException {
		String generatedPassword = null;
     
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();

		return generatedPassword;
	}
	
	
	public static String getValidationEmail(String path)  
	{
		String email = "<!doctype html>"+
"<html lang='en'>"+
"<head>"+
    "<meta charset='UTF-8'>"+
    "<meta name='viewport'"+
          "content='width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0'>"+
    "<meta http-equiv='X-UA-Compatible' content='ie=edge'>"+
    "<title>Document</title>"+
    "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>"+
    "<style>"+
        ".qlt-confirmation {"+
           " width: 30%;"+
           " margin: 10px auto;"+
        "}"+

   " </style>"+
"</head>"+
"<body>"+
"<div class='qlt-confirmation'>"+
    "<div class='panel panel-default'>"+
        "<div class='panel-body'>"+
            "<center>"+
                "<img src='https://i.imgur.com/Iyd5rwO.png' style='margin-right: 500px;'>"+
                "<p class='desc'>Thank you for signing up!<br>This is a confirmation link.<br><a href='"+path+"' class='btn btn-info' role='button'>VERIFY YOUR ACCOUNT</a></p>"+
            "</center>"+

        "</div>"+
    "</div>"+
"</div>"+
"</body>"+
"</html>";
   		return email;
	}
	
	public static String tokenGenerator () 
	{
		SecureRandom random = new SecureRandom();
		long longToken = Math.abs( random.nextLong() );
        String result = Long.toString( longToken, 16 );
        return result;
	}

}
