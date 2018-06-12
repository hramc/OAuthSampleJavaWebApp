package edu.oauth.helper;


import java.util.Date;

public class OAuthTokenDecode {
	
	String algorithm;
	
	String kid;
	
	String issuer;
		
	String audience;
	
	Date expiresAt;
	
	public String toString() {
		
		return "algorithm>>"+algorithm+"\n"+
				"kid>>"+kid+"\n"+
				"issuer>>"+issuer+"\n"+
				"audience>>"+audience+"\n"+
				"expiresAt>>"+expiresAt;		
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	

}
