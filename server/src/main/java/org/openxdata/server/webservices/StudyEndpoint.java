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

import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.security.RestEndpointSecurity;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.util.HibernateProxyUtil;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Study Rest web service.
 * 
 * @author Jmpango
 * 
 */
@Singleton
@Transactional
@Path("/studyService")
public class StudyEndpoint {

	@Inject
	private StudyManagerService studyService;

	@Inject
	private RestEndpointSecurity endpointSecurity;

	@Context
	private HttpHeaders headers;

	public StudyEndpoint() {
	}

	@GET
	@Path("/getStudies")
	@Produces("application/xml")
	public List<StudyDef> getStudies() {
		if (endpointSecurity.isAuthenticated(headers)) {
			try {
				List<StudyDef> studies = studyService.getStudies();
				HibernateProxyUtil.cleanObject(studies);
				return studies;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@GET
	@Path("/getStudy/{id}")
	@Produces("application/xml")
	public StudyDef getStudy(@QueryParam("id") Integer id) {
		if (endpointSecurity.isAuthenticated(headers)) {
			try {
				StudyDef study = studyService.getStudy(id);
				HibernateProxyUtil.cleanObject(study);
				return study;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@GET
	@Produces("text/plain")
	@Path("/studyKey/{studyId}")
	public String getStudyKey(@QueryParam("studyId") int studyId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			return studyService.getStudyKey(studyId);
		}
		return null;
	}

	@GET
	@Path("/studyName")
	@Produces("text/plain")
	public String getStudyName(@QueryParam("studyId") int studyId) {
		if (endpointSecurity.isAuthenticated(headers)) {
			return studyService.getStudyName(studyId);
		}
		return null;
	}

	@GET
	@Path("/studyByName")
	@Produces("application/xml")
	public List<StudyDef> getStudyByName(
			@QueryParam("studyName") String studyName) {
		if (endpointSecurity.isAuthenticated(headers)) {
			try {
				List<StudyDef> studies = studyService.getStudyByName(studyName);
				HibernateProxyUtil.cleanObject(studies);
				return studies;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@POST
	@Path("/saveStudy")
	public void saveStudy(StudyDef studyDef) {
		if (endpointSecurity.isAuthenticated(headers)) {
			studyService.saveStudy(studyDef);
		}
	}

	@DELETE
	@Path("/deleteStudy")
	public void deleteStudy(StudyDef studyDef) {
		if (endpointSecurity.isAuthenticated(headers)) {
			studyService.deleteStudy(studyDef);
		}
	}

}
