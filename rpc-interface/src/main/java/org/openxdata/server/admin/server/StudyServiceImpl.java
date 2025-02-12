package org.openxdata.server.admin.server;

import java.util.List;
import java.util.Map;

import org.openxdata.server.admin.client.service.StudyService;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.StudyDefHeader;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.exception.OpenXDataSecurityException;
import org.openxdata.server.admin.model.paging.PagingLoadConfig;
import org.openxdata.server.admin.model.paging.PagingLoadResult;
import org.openxdata.server.rpc.OxdPersistentRemoteService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StudyServiceImpl extends OxdPersistentRemoteService implements StudyService {
	
	private static final long serialVersionUID = 4681223938185113228L;
	
	private org.openxdata.server.service.StudyManagerService studyService;

    @Override
    public List<StudyDef> getStudies() {
        return getStudyManagerService().getStudies();
    }
    
    @Override
    public Map<Integer, String> getStudyNamesForCurrentUser() {
    	return getStudyManagerService().getStudyNamesForCurrentUser();
    }

    @Override
    public StudyDef saveStudy(StudyDef studyDef) {
        getStudyManagerService().saveStudy(studyDef);
        return studyDef;
    }

    @Override
    public void deleteStudy(StudyDef studyDef) {
        getStudyManagerService().deleteStudy(studyDef);
    }

	@Override
    public StudyDef getStudy(Integer studyId) throws OpenXDataSecurityException {
	    return getStudyManagerService().getStudy(studyId);
    }

	@Override
    public PagingLoadResult<User> getMappedUsers(Integer studyId, PagingLoadConfig loadConfig) {
	    return getStudyManagerService().getMappedUsers(studyId, loadConfig);
    }

	@Override
    public PagingLoadResult<User> getUnmappedUsers(Integer studyId, PagingLoadConfig loadConfig) {
	    return getStudyManagerService().getUnmappedUsers(studyId, loadConfig);
    }
	
	@Override
    public void saveMappedStudyUsers(Integer studyId, List<User> usersToAdd, List<User> usersToDelete) throws OpenXDataSecurityException {
		getStudyManagerService().saveMappedStudyUsers(studyId, usersToAdd, usersToDelete);
    }
	
    private org.openxdata.server.service.StudyManagerService getStudyManagerService() {
        if (studyService == null) {
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
            studyService = (org.openxdata.server.service.StudyManagerService) ctx.getBean("studyManagerService");
        }
        return studyService;
    }

	@Override
    public PagingLoadResult<StudyDef> getMappedStudies(Integer userId, PagingLoadConfig loadConfig) throws OpenXDataSecurityException {
	    return getStudyManagerService().getMappedStudies(userId, loadConfig);
    }

	@Override
	public PagingLoadResult<StudyDefHeader> getMappedStudyNames(Integer userId, PagingLoadConfig loadConfig) throws OpenXDataSecurityException {
		return getStudyManagerService().getMappedStudyNames(userId, loadConfig);
	}

	@Override
    public PagingLoadResult<StudyDefHeader> getUnmappedStudyNames(Integer userId, PagingLoadConfig loadConfig) throws OpenXDataSecurityException {
	    return getStudyManagerService().getUnmappedStudyNames(userId, loadConfig);
    }

	@Override
    public void saveMappedUserStudyNames(Integer userId, List<StudyDefHeader> studiesToAdd, List<StudyDefHeader> studiesToDelete)
            throws OpenXDataSecurityException {
		getStudyManagerService().saveMappedUserStudyNames(userId, studiesToAdd, studiesToDelete);
    }
}
