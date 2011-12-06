/**
 * 
 */
package org.openxdata.server.security;

import javax.ws.rs.core.HttpHeaders;

/**
 * Authenticate a web service request.
 * 
 * @author Jmpango
 *
 */
public interface RestEndpointSecurity {

	/**
	 * Used to extract user credentials from 
	 * incoming requests from external entities.
	 * 
	 * @param HttpHeader containing the user credentials
	 * 
	 * @return true if user is authenticated and
	 *          false if not. 
	 */
	public Boolean isAuthenticated(HttpHeaders headers);
}
