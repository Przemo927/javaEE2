package pl.przemek.security;


import io.jsonwebtoken.ExpiredJwtException;
import pl.przemek.model.User;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.rest.utils.ResponseUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Provider
public class LoginFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @Inject
    private JpaUserRepository userrep;
    @Inject
    private TokenService tokenService;
    @Inject
    private TokenStore tokenStore;
    @Inject
    private AuthenticationDataStore userDataStore;
    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext){

        String username;
        Principal userPrincipal=requestContext.getSecurityContext().getUserPrincipal();
        HttpSession session=request.getSession(false);

    	if(session!=null && session.getAttribute("user")!=null){
    	    String encryptedPassword=userDataStore.getEncryptedPassword();
    	    username=userDataStore.getUsername();
        	String token=requestContext.getHeaderString(AUTHORIZATION);
            if(!ifTokenIsValid(token,encryptedPassword)){
                logout();
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }
			saveToken(username,encryptedPassword);
        }
        else if(userPrincipal != null && session!=null && session.getAttribute("user") == null) {
            username = userPrincipal.getName();
            List<User> listUserByUsername = userrep.getUserByUsername(username);
            if(!listUserByUsername.isEmpty()) {
                updateLastLogin(username,userrep);
                User userByUsername=listUserByUsername.get(0);
                try {
                    LogoutIfInActiveStatus(userByUsername, request);
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"[LoginFilter] filter()",e);
                }
                saveToken(userByUsername.getUsername(), userByUsername.getPassword());
                saveUserData(userByUsername);
                saveUserInSession(request, userByUsername);
            } else {
               logout();
               requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
               return;
            }

        }
    }
    void updateLastLogin(String username, JpaUserRepository userrep){
        boolean resultBoolean=userrep.updateLastLogin(username);
        if(!resultBoolean){
            logger.log(Level.SEVERE,"[LoginFilter] updateLastLogin() lastLogin wasn't updated");
        }
    }
    void LogoutIfInActiveStatus(User user, HttpServletRequest request) throws IOException {
        if(!user.isActive()){
            logout();
            response.sendRedirect(ResponseUtils.getLogoutPath(request));
        }
    }
    void logout(){
        try {
            request.getSession().invalidate();
            request.logout();
        } catch (ServletException e) {
            logger.log(Level.SEVERE,"[LoginFilter] logout()",e);
        }
    }
    void saveUserInSession(HttpServletRequest request,User user) {

        request.getSession(false).setAttribute("user", user);
    }
    void saveToken(String username,String password){
    	String token=tokenService.generateToken(username, password);
        tokenStore.setToken(token);
    }
    void saveUserData(User user){
    	userDataStore.setUsername(user.getUsername());
        userDataStore.setEncryptedPassword(user.getPassword());
    }
    boolean ifTokenIsValid(String token, String encryptedPassword) {
       try{
            Key key=tokenService.generateKey(encryptedPassword);
            JwtsRepository.checkToken(key,token);
        } catch (ExpiredJwtException e) {
           logger.log(Level.SEVERE,"[LoginFilter] Token is invalid");
           return false;
        }
        return true;
    }
}
