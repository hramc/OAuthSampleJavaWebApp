package edu.oauth.model;


import java.util.Date;

public class OAuthAccessTokenResponse {
	
	String access_token;
	
	String refresh_token;
	
	String token_type;

	String expires_in;
	
	Date expiresIn;
	
	//static SimpleDateFormat sdf = new SimpleDateFormat("mmddyyyy HHmmss");
	
	/*public static Date getExpiresInDate(String expiresIn) throws ParseException {
		return sdf.parse(expiresIn);
	}*/
	
	public String toString() {
		
		return "access_token>>"+access_token+
				"refresh_token>>"+refresh_token+
				"token_type>>"+token_type+
				"expiresIn>>"+expiresIn+
				"expires_in>>"+expires_in;		
	}

	/*public String getExpiresIn() {
		return sdf.format(expiresIn);
	}
	
	public void setExpiresIn() {
		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.add(Calendar.SECOND, Integer.parseInt(expires_in));
		expiresIn = Calendar.getInstance().getTime();
	}*/

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
	
}
