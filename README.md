# OAuthSampleJavaWebApp
This is a Sample Java web application uses to demonstrate OAuth Ping Fed authenication process.
This application will do the following steps

1. When the user tries to access the home page of the application. 
2. It will redirect the user to Ping Fed login page if user doesn't authenicated before.
3. Once user authenicated successfully in the Ping Fed. Ping Fed application will redirect the user to this web application as configured. [Please configure the Ping fed application to redirect to this web application home page, by default it is configured as "http://localhost:8080/oidc"]
4. Once the page is redirected, the web application will make another POST request to get access token.
5. Once the Ping fed application provide the acces token. it will decode the token and extract the high level user information and displays thw same in the web page.
6. The application also persist the access token in the cookie. so, it will not ask the user to authenicate next time.
7. if the cookie is expired or access token is expired, the application will refresh the token automatically.

On a high level, it looks little complicated process. but, it doesn't. 
This sample web application will come handy when you have setup a ping fed and wanted to tested the same.
Please fill all the neccessary information in the application.properties file present in this project before running the web application.

Please feel to share your comments.

Thanks


