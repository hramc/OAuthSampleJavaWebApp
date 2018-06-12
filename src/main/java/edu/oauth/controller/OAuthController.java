package edu.oauth.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import edu.oauth.constants.Constants;
import edu.oauth.helper.OAuthTokenDecode;
import edu.oauth.model.OAuthAccessTokenResponse;
/**
 * 
 * @author hramc
 * 
 * Main controller class which will redirect the default page to OAuth login page. 
 * if the user is not authenticated before. 
 * Once user authenticated successful in OAuth login page, it will get the access token and store the same in cookie for future use.
 *
 */
@Controller
public class OAuthController {
	
	@Autowired
	Environment application;

    /**
     * This is a method will be called by default.
     * It will check the Cookie and do the following action
     * 	1. if the Access token is not present in the cookie - it will redirect the request to OAuth login page.
     *     once user successfully authenticated, it will post another request to OAuth and get an access token.
     *     Save the access token in the cookie for future use.
     *     After that, it will provide all the user information to the calling page.
     *  2. if the Access token is present in the cookie - it will read the value and provide all the user information to the calling page.
     *   
     * @param model
     * @param oAuthToken
     * @param oAuthRefreshToken
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @RequestMapping(value=Constants.BASE_URL, method=RequestMethod.GET)
    public String oauth(Model model,
    		@CookieValue(value=Constants.OAUTH_ACCESS_TOKEN, defaultValue=Constants.EMPTY_STRING) String oAuthToken,
    		@CookieValue(value=Constants.OAUTH_REFRESH_TOKEN, defaultValue=Constants.EMPTY_STRING) String oAuthRefreshToken,
    		HttpServletRequest request,HttpServletResponse response
    		) throws ParseException {
    	// Model object to store the user information
    	OAuthTokenDecode oAuthTokenDecode = new OAuthTokenDecode();
    	
    	
    	// if the Acces token is not present in the cookie
    	if(oAuthToken==null || oAuthToken.equals(Constants.EMPTY_STRING)) {
        	return Constants.REDIRECT + BuildAuthRedirect();
    	}else {
    		// Decode the access token
    		oAuthTokenDecode = OAuthTokenDecoder(oAuthToken);
    		
    		// If the token is expired, then redirect the page to OAuth loing page
    		if((Calendar.getInstance().getTime().getTime()>=oAuthTokenDecode.getExpiresAt().getTime())){
    			
    			// if the refresh token is available
    			if(oAuthRefreshToken!=null && !oAuthRefreshToken.equals(Constants.EMPTY_STRING)) {
    	    		String responsenew = null;
    	    		String redirectUri =
    	        			URLEncoder.encode(application.getProperty(Constants.LOCAL_OAUTH_URL));
    	        	try {
    	        		responsenew = sendPost(null,redirectUri,Constants.REFRESH_TOKEN,oAuthRefreshToken);
    	    		} catch (Exception e) {
    	    			// TODO Auto-generated catch block
    	    			e.printStackTrace();
    	    		}
    	        	if(responsenew!=null) {
    	        		Gson gson = new Gson();
    	        		OAuthAccessTokenResponse oAuthTokennew = gson.fromJson(responsenew, OAuthAccessTokenResponse.class);
    	        		oAuthTokenDecode = OAuthTokenDecoder(oAuthTokennew.getAccess_token());
    	        		response.addCookie(new Cookie(Constants.OAUTH_ACCESS_TOKEN,oAuthTokennew.getAccess_token()));
    	        		response.addCookie(new Cookie(Constants.OAUTH_REFRESH_TOKEN,oAuthTokennew.getRefresh_token()));
    	        	}else {
    	        		return Constants.REDIRECT + BuildAuthRedirect();
    	        	}
    	    	}else {
    	    		return Constants.REDIRECT + BuildAuthRedirect();
    	    	}
    		}
    	}
    	
    	
    	model.addAttribute(Constants.OAUTH_TOKEN_DECODE,oAuthTokenDecode);
        return Constants.HOME_PAGE;
    }
    
    /**
     * 
     * @param model
     * @param request
     * @param response
     * @param requestBody
     * @return
     * @throws IOException
     */
    

    @RequestMapping(value=Constants.LOCAL_OAUTH_URL_REDIRECT, method=RequestMethod.POST)
    public String oidc(Model model,HttpServletRequest request,HttpServletResponse response,@RequestBody String requestBody) throws IOException {
    	String responsenew = null;
    	if(request.getParameter(Constants.OAUTH_CODE_PARAMETER)!=null) {
	    	String redirectUri =
	    			URLEncoder.encode(application.getProperty(Constants.LOCAL_OAUTH_URL));
	    	try {
	    		responsenew = sendPost(request.getParameter(Constants.OAUTH_CODE_PARAMETER),redirectUri,Constants.OAUTH_AUTHORIZATION_CODE_PARAMETER,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	OAuthTokenDecode oAuthTokenDecode = new OAuthTokenDecode();
    	if(responsenew!=null) {
    		Gson gson = new Gson();
    		OAuthAccessTokenResponse oAuthToken = gson.fromJson(responsenew, OAuthAccessTokenResponse.class);
    		oAuthTokenDecode = OAuthTokenDecoder(oAuthToken.getAccess_token());
    		response.addCookie(new Cookie(Constants.OAUTH_ACCESS_TOKEN,oAuthToken.getAccess_token()));
    		response.addCookie(new Cookie(Constants.OAUTH_REFRESH_TOKEN,oAuthToken.getRefresh_token()));
    	}
    	
       // model.addAttribute("welcomeMessage", "You Logged in successfully with "+oAuthTokenDecode.toString());
        model.addAttribute(Constants.OAUTH_TOKEN_DECODE,oAuthTokenDecode);
		return Constants.HOME_PAGE;
    }
    
    // 
    private OAuthTokenDecode OAuthTokenDecoder(String accessToken) {
    	OAuthTokenDecode oAuthTokenDecode = new OAuthTokenDecode();;
    	DecodedJWT jwt = JWT.decode(accessToken);
		oAuthTokenDecode.setAlgorithm(jwt.getAlgorithm());
		oAuthTokenDecode.setAudience((jwt.getAudience()!=null && !jwt.getAudience().isEmpty())?jwt.getAudience().get(0):null);
		oAuthTokenDecode.setIssuer(jwt.getIssuer());
		oAuthTokenDecode.setExpiresAt(jwt.getExpiresAt());
		oAuthTokenDecode.setKid(jwt.getKeyId());
		return oAuthTokenDecode;
    }
    
    // HTTP POST request
 	private String sendPost(String code,String redirectUri,String grantType,String refrehToken) throws Exception {

 		URL obj = new URL(application.getProperty(Constants.OAUTH_URL));
 		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

 		//add reuqest header
 		con.setRequestMethod(Constants.POST_METHOD);
 		String urlParameters = "";

 		if(grantType.equalsIgnoreCase(Constants.OAUTH_AUTHORIZATION_CODE_PARAMETER)) {
	 		urlParameters = 
	 		String.format(
	 				Constants.AUTHORIZATION_CODE_OAUTH_URL,
	 				code, redirectUri,application.getProperty(Constants.CLIENT_ID),application.getProperty(Constants.CLIENT_SECRET));
 		}else {
 			urlParameters = 
 			 		String.format(
 			 				Constants.REFRESH_TOKEN_OAUTH_URL,
 			 				refrehToken, redirectUri,application.getProperty(Constants.CLIENT_ID),application.getProperty(Constants.CLIENT_SECRET));
 		}
 		// Send post request
 		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(urlParameters);
 		wr.flush();
 		wr.close();
 		int responseCode = con.getResponseCode();
 		System.out.println("\nSending 'POST' request to URL : " + application.getProperty(Constants.OAUTH_URL));
 		System.out.println("Post parameters : " + urlParameters);
 		System.out.println("Response Code : " + responseCode);
 		StringBuffer response = null;
 		if(responseCode==200) {
	 		BufferedReader in = new BufferedReader(
	 		        new InputStreamReader(con.getInputStream()));
	 		String inputLine;
	 		response = new StringBuffer();
	
	 		while ((inputLine = in.readLine()) != null) {
	 			response.append(inputLine);
	 		}
	 		in.close();
 		}

 		//print result
 		System.out.println(response!=null?response.toString():"");
 		return response!=null?response.toString():null;
 	}
    
   
    
    public String BuildAuthRedirect()
    {
        // Remember the URL that the user was trying to access so we can redirect them there after we get a token
        // Tell Ping to post access code to our Token action
    	String authUrl =
    			String.format(
                Constants.CODE_OAUTH_URL,
                application.getProperty(Constants.AUTHORIZATION_OAUTH_URL),application.getProperty(Constants.CLIENT_ID),
                application.getProperty(Constants.STATE_URL), application.getProperty(Constants.REDIRECT_URL), application.getProperty(Constants.OAUTH_ADAPTER));
        return authUrl;
    }

}
