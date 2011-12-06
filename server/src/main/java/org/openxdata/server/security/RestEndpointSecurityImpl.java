package org.openxdata.server.security;

import javax.ws.rs.core.HttpHeaders;

import org.openxdata.server.admin.model.User;
import org.openxdata.server.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.core.util.Base64;

/**
 * Default implementation <code>RestEndpointSecurity Service</code>
 * 
 * @author Jmpango
 *
 */
@Service("restEndpointSecurity")
public class RestEndpointSecurityImpl implements RestEndpointSecurity{
	
	@Autowired
	private AuthenticationService authenticationService;
	
	public Boolean isAuthenticated(HttpHeaders headers){	  
        String auth = headers.getRequestHeader("authorization").get(0);		
		auth = auth.substring("Basic ".length());
		String[] creds = new String(Base64.base64Decode(auth)).split(":");
		
		String username = creds[0];
		String password = creds[1];		
		
		User user = authenticationService.authenticate(username, password);
		if (user != null) {
			return true;
		}
		return false;
	}
}
