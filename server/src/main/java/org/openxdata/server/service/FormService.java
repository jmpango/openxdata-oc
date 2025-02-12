package org.openxdata.server.service;

import java.util.List;
import java.util.Map;

import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.ExportedFormData;
import org.openxdata.server.admin.model.FormData;
import org.openxdata.server.admin.model.FormDataHeader;
import org.openxdata.server.admin.model.FormDataVersion;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefHeader;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.exception.ExportedDataNotFoundException;
import org.openxdata.server.admin.model.exception.OpenXDataSecurityException;
import org.openxdata.server.admin.model.paging.PagingLoadConfig;
import org.openxdata.server.admin.model.paging.PagingLoadResult;


public interface FormService {

	/**
	 * Returns a given Form given the ID.
	 * 
	 * @param formId Id of the Form to retrieve.
	 * @return FormDef
	 */
	FormDef getForm(int formId);
	
	/**
	 * Returns a FormDefVersion given the ID
	 * @param formVersionId
	 * @return
	 */
	FormDefVersion getFormVersion(int formVersionId);
	
	/**
	 * Gets form data as identified by the id.
	 * 
	 * @param formDataId the form data identifier.
	 * @return the form data.
	 */
	FormData getFormData(Integer formDataId);

	/**
	 * Retrieves the history of the specified FormData object
	 * Deprecated - use FormService instead
	 * @param formDataId Integer FormData identifier
	 * @return List of FormDataVersion
	 */
	List<FormDataVersion> getFormDataVersion(Integer formDataId);

	/**
	 * Deletes a form definition from the database.
	 * 
	 * @param formDef the form definition to delete.
	 */
	void deleteForm(FormDef formDef);
	
	/**
	 * Deletes a row of data from the database.
	 * 
	 * @param formData FormData to delete
	 */
	void deleteFormData(FormData formData);

	/**
	 * Deletes all the submitted data specified by the ids
	 * NOTE: this method assumes the form data has not been exported (hence
	 * does not try to delete from exported tables)
	 * @param formDataIds
	 */
	void deleteFormData(List<Integer> formDataIds);

	/**
	 * Reprocesses (runs RDMS Export) for all the specified form data
	 * @param formDataIds
	 */
	void exportFormData(List<Integer> formDataIds);
	
	/**
	 * Saves a given Form.
	 * 
	 * @param formDef Form to save.
	 */
	void saveForm(FormDef formDef);
	
    /**
     * Saves the data captured by the user for a particular form
     * @param formData FormData
     * @return FormData that was saved (contains id reference)
     */
    FormData saveFormData(FormData formData);
    
    /**
     * Retrieves a page of form definitions in the system
     * @param loadConfig PagingLoadConfiguration specifying page number and size etc
     * @return PagingLoadResult of Form Definitions
     */
    PagingLoadResult<FormDef> getForms(PagingLoadConfig loadConfig);
    
    /**
     * Retrieves a page of form definitions in the system
     * @param user User
     * @param loadConfig PagingLoadConfiguration specifying page number and size etc
     * @return PagingLoadResult of Form Definitions
     */
    PagingLoadResult<FormDef> getForms(User user, PagingLoadConfig loadConfig);
    
    /**
     * Retrieves a page of form version definitions in the system
     * @param user User
     * @param loadConfig PagingLoadConfiguration specifying page number and size etc
     * @return PagingLoadResult of Form Version Definitions
     */
	PagingLoadResult<FormDefVersion> getFormVersions(User user, PagingLoadConfig loadConfig);
    
    /**
	 * Retrieves the Forms for the specified Study for which the user has permission
	 * @param user User
	 * @param studyDefId Integer identifier
	 * @return List of Forms
	 */
	List<FormDef> getStudyForms(User user, Integer studyDefId);
    
    /**
     * Retrieves all the names of the forms that are available for the currently logged in user
     * and are under the specified study
     * @param studyId identifier of the study
     * @return
     */
    Map<Integer, String> getFormNamesForCurrentUser(Integer studyId);

    /**
     * Calculates the number of responses captured for a specified formDefVersion
     * @param formId int identifier for a form definition version
     * @return Integer (positive number, 0 for no responses)
     */
    Integer getFormResponseCount(int formDefVersionId);

    /**
     * Checks if a study, form or form version has data collected for it.
     *
     * @param item the study, form, or form version.
     * @return true if it has, else false.
     */
    Boolean hasEditableData(Editable item);

    /**
     * Retrieves a page of the form data (directly from exported tables) for a specified form definition
     * @param formBinding String xform binding (table name)
     * @param formFields String question binding (column names)
     * @param pagingLoadConfig settings used to control paged result
     * @return PagingLoadResult containing partial exported form data
     * @throws ExportedDataNotFoundException when the exported table does not exist
     */
    PagingLoadResult<ExportedFormData> getFormDataList(String formBinding, String[] questionBindings, 
    		PagingLoadConfig pagingLoadConfig) throws ExportedDataNotFoundException;
    
	/**
	 * Get a page of Users mapped to a specific form 
	 * @param formId
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<User> getMappedUsers(Integer formId, PagingLoadConfig loadConfig);
	
	/**
	 * Get a page of Users NOT mapped to the specified form
	 * @param studyId
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<User> getUnmappedUsers(Integer formId, PagingLoadConfig loadConfig);
	
	/**
	 * Updates the users currently mapped to the specified form.
	 * @param formId Integer id of specified form
	 * @param usersToAdd List of users to add to the study mapping
	 * @param usersToDelete List of users to delete from the study mapping
	 */
	void saveMappedFormUsers(Integer formId, List<User> usersToAdd, List<User> usersToDelete);
	
	/**
	 * Get a page of Forms mapped to a specific user 
	 * @param userId
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<FormDef> getMappedForms(Integer userId, PagingLoadConfig loadConfig);

	/**
	 * Get a page of Forms mapped to a specific user 
	 * @param userId
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<FormDefHeader> getMappedFormNames(Integer userId, PagingLoadConfig loadConfig) throws OpenXDataSecurityException;
	
	/**
	 * Get a page of Forms NOT mapped to the specified user
	 * @param userId
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<FormDefHeader> getUnmappedFormNames(Integer userId, PagingLoadConfig loadConfig) throws OpenXDataSecurityException;
	
	List<FormData> getFormData(FormDef form);
	
	void saveMappedUserFormNames(Integer userId, List<FormDefHeader> formsToAdd, List<FormDefHeader> formsToDelete) throws OpenXDataSecurityException;
	
	/**
	 * Retrieves a page of unexported Form Data
	 * @param loadConfig
	 * @return
	 */
	PagingLoadResult<FormDataHeader> getUnexportedFormData(PagingLoadConfig loadConfig);
}
