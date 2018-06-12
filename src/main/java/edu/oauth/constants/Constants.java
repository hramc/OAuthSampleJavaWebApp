package edu.oauth.constants;

public interface Constants {
	
	String LOCAL_OAUTH_URL = "local.oauth.url";
	String REFRESH_TOKEN="refresh_token";
	String OAUTH_ACCESS_TOKEN="oAuthAccessToken";
	String OAUTH_REFRESH_TOKEN="oAuthRefreshToken";
	String OAUTH_TOKEN_DECODE="oAuthTokenDecode";
	String REDIRECT = "redirect:";
	String BASE_URL = "/";
	String EMPTY_STRING = "";
	String HOME_PAGE="index";
	String LOCAL_OAUTH_URL_REDIRECT="/oidc";
	String OAUTH_CODE_PARAMETER="code";
	String OAUTH_AUTHORIZATION_CODE_PARAMETER="authorization_code";
	String OAUTH_URL="ouath.url";
	String CLIENT_ID = "client.id";
	String CLIENT_SECRET="client.secret";
	String AUTHORIZATION_CODE_OAUTH_URL = "grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s";
	String CODE_OAUTH_URL = "%s?client_id=%s&response_mode=form_post&response_type=code&scope=openid+profile&state=%s&redirect_uri=%s&pfidpadapterid=%s";
	String REFRESH_TOKEN_OAUTH_URL = "grant_type=refresh_token&refresh_token=%s&redirect_uri=%s&client_id=%s&client_secret=%s";
	String AUTHORIZATION_OAUTH_URL="oauth.authorization.url";
	String OAUTH_ADAPTER = "outh.adapter";
	String STATE_URL = "state.url";
	String REDIRECT_URL = "redirect.url";
	String POST_METHOD="POST";
}

