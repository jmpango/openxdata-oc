package org.openxdata.server.webservices;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.openxdata.server.admin.model.FormData;
import org.openxdata.server.admin.model.FormDataVersion;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.dao.FormDataDAO;
import org.openxdata.server.security.RestEndpointSecurity;
import org.openxdata.server.util.HibernateProxyUtil;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

/**
 * FormDataDao Rest web service.
 * 
 * @author Jmpango
 * 
 */
@Singleton
@Transactional
@Path("/formDataDaoService")
public class FormDataDAOEndpoint {

	@Inject
	private RestEndpointSecurity endpointSecurity;

	@Inject
	private FormDataDAO formDataDAO;

	@Context
	private HttpHeaders headers;

	@GET
	@Path("/formData")
	@Produces("application/xml")
	public FormData getFormData(@QueryParam("formDataId")Integer formDataId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			try {
				FormData formData = formDataDAO.getFormData(formDataId);
				HibernateProxyUtil.cleanObject(formData);
				return formData;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@GET
	@Path("/formDataVersion/{formDataId}")
	@Produces("application/xml")
	public List<FormDataVersion> getFormDataVersion(@QueryParam("formDataId")Integer formDataId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			try {
				List<FormDataVersion> formDataVersion = formDataDAO
						.getFormDataVersion(formDataId);
				HibernateProxyUtil.cleanObject(formDataVersion);
				return formDataVersion;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@GET
	@Path("/formDataCount/{formDefId}")
	@Produces("text/plain")
	public Integer getFormDataCount(@QueryParam("formDefId")Integer formDefId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			formDataDAO.getFormDataCount(formDefId);
		}
		return null;
	}

	@POST
	@Path("/saveFormData")
	public void saveFormData(FormData formData) {
		if (endpointSecurity.isAuthenticated(headers)) {
			formDataDAO.saveFormData(formData);
		}
	}

	@POST
	@Path("/saveFormDataVersion")
	public void saveFormDataVersion(FormDataVersion formDataVersion) {
		if (endpointSecurity.isAuthenticated(headers)) {
			formDataDAO.saveFormDataVersion(formDataVersion);
		}
	}

	@POST
	@Path("/saveFormDataVersionByFormData")
	public void saveFormDataVersion(FormData formData) {
		if (endpointSecurity.isAuthenticated(headers)) {
			formDataDAO.saveFormDataVersion(formData);
		}
	}
	
	@DELETE
	@Path("/deleteFormData")
	public void deleteFormData(@QueryParam("formDataId")Integer formDataId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			formDataDAO.deleteFormData(formDataId);
		}
	}
}
