package org.openxdata.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.StudyDefHeader;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.mapping.UserStudyMap;
import org.openxdata.server.admin.model.paging.PagingLoadConfig;
import org.openxdata.server.admin.model.paging.PagingLoadResult;
import org.openxdata.server.dao.EditableDAO;
import org.openxdata.server.dao.StudyDAO;
import org.openxdata.server.dao.UserFormMapDAO;
import org.openxdata.server.dao.UserStudyMapDAO;
import org.openxdata.server.security.util.OpenXDataSecurityUtil;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.util.XformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation for study manager service.
 * 
 * @author daniel
 *
 */
@Service("studyManagerService")
@Transactional
public class StudyManagerServiceImpl implements StudyManagerService {

	
	@Autowired
	private StudyDAO studyDao;
	
	@Autowired
	private UserFormMapDAO userFormMapDAO;
	
	@Autowired
	private UserStudyMapDAO userStudyMapDAO;

	@Autowired
	private EditableDAO editableDAO;

	@Override
	@Secured("Perm_Delete_Studies")
	public void deleteStudy(StudyDef studyDef) {
		userStudyMapDAO.deleteUserMappedStudies(studyDef.getId());
		List<FormDef> forms = studyDef.getForms();
		if (forms != null) {
			for (FormDef formDef : forms) {
				userFormMapDAO.deleteUserMappedForms(formDef.getId());
			}
		}
		studyDao.deleteStudy(studyDef);
	}

	@Override
	@Transactional(readOnly=true)
	@Secured("Perm_View_Studies")
	public List<StudyDef> getStudies() {		
		return studyDao.getStudies();
	}
	
	@Override
	@Transactional(readOnly=true)
	@Secured("Perm_View_Studies")
	public Map<Integer, String> getStudyNamesForCurrentUser() {		
		return userStudyMapDAO.getStudyNamesForUser(OpenXDataSecurityUtil.getLoggedInUser());
	}
	
	@Override
	@Transactional(readOnly=true)
	@Secured("Perm_View_Studies")
	public StudyDef getStudy(Integer id) {		
		return studyDao.getStudy(id);
	}

	@Override
    @Secured(value = "Perm_Add_Studies")
    public void saveStudy(StudyDef studyDef) {
        List<FormDefVersion> newVersions = new ArrayList<FormDefVersion>();

        //Get a list of new form versions
        if (studyDef.getForms() != null) {
            for (FormDef formDef : studyDef.getForms()) {
                if (formDef.getVersions() == null) {
                    continue;
                }

                for (FormDefVersion formDefVersion : formDef.getVersions()) {
                    if (formDefVersion.isNew()) {
                        newVersions.add(formDefVersion);
                    }
                }
            }
        }

        studyDao.saveStudy(studyDef);

        //Now set the xforms id attribute to the value of the saved form version id.
        for (FormDefVersion formDefVersion : newVersions) {
            if (formDefVersion.getXform() != null) {
                formDefVersion.setXform(XformUtil.addFormId2Xform(formDefVersion.getId(),
                        formDefVersion.getXform()));
            }
        }

        //Save the modified new form versions, if any
        //TODO This should only save the formDefVersion instead of the whole study.
        if (newVersions.size() > 0) {
            studyDao.saveStudy(studyDef);
        }
        //map this new study to the current user
        //am assuming a study that has no user has just been created
        //this avoid having to map a study every time it is saved
        if (!OpenXDataSecurityUtil.getLoggedInUser().hasAdministrativePrivileges()&& (studyDef.getUsers()== null)) {
            UserStudyMap map = new UserStudyMap();
            map.setStudy(studyDef);
            map.setUser(OpenXDataSecurityUtil.getLoggedInUser());
            userStudyMapDAO.saveUserMappedStudy(map);
        }
    }

	@Override
	@Transactional(readOnly=true)
	@Secured("Perm_View_Form_Data")
	public Boolean hasEditableData(Editable item) {
		return editableDAO.hasEditableData(item);
	}

	@Override
	@Secured("Perm_View_Studies")
	public String getStudyKey(int studyId) {
		String key = studyDao.getStudyKey(studyId);
		if (key == null)
			key = "";
		return key;
	}

	@Override
	@Secured("Perm_View_Studies")
	public String getStudyName(int studyId) {
		String name = studyDao.getStudyName(studyId);
		if(name == null)
                name = "UNKNOWN STUDY";
        return name;
	}
	
	@Override
	@Secured("Perm_View_Studies")
	public List<StudyDef> getStudyByName(String studyName) {
		return studyDao.searchByPropertyEqual("name", studyName);
	}

	@Override
	@Secured({"Perm_View_Studies", "Perm_View_Users"})
    public PagingLoadResult<User> getMappedUsers(Integer studyId, PagingLoadConfig loadConfig) {
	    return studyDao.getMappedUsers(studyId, loadConfig);
    }

	@Override
	@Secured({"Perm_View_Studies", "Perm_View_Users"})
    public PagingLoadResult<User> getUnmappedUsers(Integer studyId, PagingLoadConfig loadConfig) {
	    return studyDao.getUnmappedUsers(studyId, loadConfig);
    }

	@Override
	@Secured({"Perm_Add_Users", "Perm_Add_Studies"})
    public void saveMappedStudyUsers(Integer studyId, List<User> usersToAdd, List<User> usersToDelete) {
		if (usersToAdd != null) {
			for (User u : usersToAdd) {
				UserStudyMap map = new UserStudyMap(u.getId(), studyId);
				userStudyMapDAO.saveUserMappedStudy(map);
			}
		}
		if (usersToDelete != null) {
			for (User u : usersToDelete) {
				UserStudyMap map = userStudyMapDAO.getUserStudyMap(u.getId(), studyId);
				userStudyMapDAO.deleteUserMappedStudy(map);
			}
		}
    }

	@Override
	@Secured({"Perm_View_Studies", "Perm_View_Users"})
    public PagingLoadResult<StudyDef> getMappedStudies(Integer userId, PagingLoadConfig loadConfig) {
		return studyDao.getMappedStudies(userId, loadConfig);
    }

	@Override
	@Secured({"Perm_View_Studies", "Perm_View_Users"})
	public PagingLoadResult<StudyDefHeader> getMappedStudyNames(Integer userId, PagingLoadConfig loadConfig) {
		return studyDao.getMappedStudyNames(userId, loadConfig);
	}

	@Override
	@Secured({"Perm_View_Studies", "Perm_View_Users"})
    public PagingLoadResult<StudyDefHeader> getUnmappedStudyNames(Integer userId, PagingLoadConfig loadConfig) {
	    return studyDao.getUnmappedStudyNames(userId, loadConfig);
    }

	@Override
	@Secured({"Perm_Add_Users", "Perm_Add_Studies"})
    public void saveMappedUserStudyNames(Integer userId, List<StudyDefHeader> studiesToAdd, List<StudyDefHeader> studiesToDelete) {
	    if (studiesToAdd != null) {
		    for (StudyDefHeader sd : studiesToAdd) {
		    	UserStudyMap map = new UserStudyMap(userId, sd.getId());
				userStudyMapDAO.saveUserMappedStudy(map);
		    }
	    }
	    if (studiesToDelete != null) {
		    for (StudyDefHeader sd : studiesToDelete) {
		    	UserStudyMap map = userStudyMapDAO.getUserStudyMap(userId, sd.getId());
				userStudyMapDAO.deleteUserMappedStudy(map);
		    }
	    }
    }

	@Override
	public StudyDef getStudy(String studyKey) {
		return studyDao.getStudy(studyKey);
	}

	
	@Override
	public void setStudyDAO(StudyDAO studyDAO) {
		this.studyDao = studyDAO;
	}
}
