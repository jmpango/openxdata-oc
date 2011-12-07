package org.openxdata.server.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.openxdata.server.admin.model.User;
import org.openxdata.server.service.AuthenticationService;
import org.openxdata.server.util.HibernateProxyUtil;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Authentication REST web service.
 * 
 * @author Jmpango
 * 
 */
@Singleton
@Transactional
@Path("/authenticationService")
public class AuthenticationEndpoint {

	@Inject
	private AuthenticationService authenticationService;

	@GET
	@Path("/authenticate")
	@Produces("application/xml")
	public User authenticate(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		User user = authenticationService.authenticate(username, password);
		try {
			HibernateProxyUtil.cleanObject(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return user;
	}

}
