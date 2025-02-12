package org.openxdata.client;

import com.google.gwt.i18n.client.Messages;

public interface AppMessages extends Messages {

	String title();
	String logo();
	String logoUrl();
	String splashImageUrl();
	String branded();
	String provider();
	String providerLogo();
	String providerLink();
	String user();
	String login();
	String logout();
	String userProfile();	
	String myDetails();
	String username();
	String passWord();
	String oldPassWord();
	String changeMyPassword();
	String newPassWord();
	String confirmPassword();
	String resetPassword();
	String oldPasswordNotValid();
	String passwordNotSame();
	String profileNotSaved();
	String profileSaved();
	String userNotFound();
	String save();
	String cancel();
	String close();
	String firstName();
	String middleName();
	String lastName();
	String phoneNo();
	String eMail();
	String language();
	
	String auditFields();
	String responseDataFields();
	String id();
	String form();
	String version();
	String status();
	String organisation();
	String creator();
	String changed();
	String responses();
	String listOfForms();
	String captureData();
	String formMustBeSelected();
	String noResponses();
	String printForm();
	String viewResponses();
	
	String from();
	String to();
	String selectADataCapturer();
	String exportResponses();
	String exportToCSV();
	String dataCapture();
	String date();
	String capturer();
	String browseResponses();
	String editResponse();
	String showAllVersions();
	String showAllForms();
	String showPublishedVersions();
	
	String dataSavedSucessfully(String sessionReference);
	String pleaseTryAgainLater(String technicalMessage);
	String errorWhileRetrievingForms();
	String accessDeniedError();
	String sessionExpired();
	String providerBlurb();
	String brandedBlurb();
	String and();
	
	String itemsPerPage(String pageSize);
	String unsuccessfulLogin();
	
	String loading();
	String success();
	String error();
	
	String admin();
	String study();
	String edit();
	String delete();
	String newStudyOrForm();
	String editStudyOrForm();
	String deleteStudyOrForm();
	
	String next();
	String back();
	String finish();
	String saveAndExit();
	String areYouSure();
	String areYouSureWizard();
	String stepOf(int step, int total);
	
	String newStudyFormOrVersionHeading();
	String design();
	String addNewStudy();
	String existingStudy();
	String studyName();
	String studyDescription();
	String addNewForm();
	String existingForm();
	String formName();
	String formDescription();
	String formVersion();
	String formVersionName();
	String formVersionDescription();
	String formVersionDefault();
	String designForm();
	String publishedHelp();

	String setUserAccessToStudy();

	String allUsers();
	String availableUsers();
	String searchForAUser();

	String usersWithAccessToStudy();

	String addAllUsers();

	String addUser();

	String removeAllUsers();

	String removeUser();

	String setUserAccessToForm();

	String usersWithAccessToForm();

	String unableToDeleteFormWithData();

	String areYouSureDelete();
	
	String selectFormVersion();
	
	String active();
	
	String exportAs();
	
	String export();
	
	String exportError();
	
	String exportA();
	
	String exportErrorsLabel();
	
	String downloadImportTemplate();
	
	String existingDataTitle();
	
	String existingDataMessage();
	
	String cannotSave();
	
	String removeFormIdAttribute();
	
	String saveSuccess();
	
	String deleteSuccess();
	
	String sessionTimeout();
	
	String invalidUsernameOrPassword();
	
	String rightCredentials();
	
	String conflictingLogins();
	
	String anotherLoggedInUser();
		
	String securityAdminChangePassInfo();
	
	String adminDefaultPasswordChangeCancel();
	
	String sameAdminPassword();
	
	String emptyPasswords();
	
	String formNameUnique();
	String studyNameUnique();
	
	String studyAccess();
	
	String noFormVersion();
	
	String deleteStudy();
	
	String deleteForm();
	
	String deleteFormVersion();
	
	String exportForm();
	
	String exportFormVersion();
	
	String exportStudy();
	
	String securityWarning();
	
	String successfulExportAs();
	
	String exportEditable();
	
	String exportStudyTooltip();
	String exportFormTooltip();
	String exportFormVersionTooltip();
	String filenameContainsIllegalCharacters();
	
	String filename();
	String importX();
	
	String importEditable();
	
	String importFormVersion();
	
	String importForm();
	
	String importStudy();
	
	String importParseError();
	
	String importSuccess();
	
	String importError();
	
	String importStudyTooltip();
	
	String importFormTooltip();
	
	String importFormVersionTooltip();
	
	String intoStudy();
	
	String intoForm();
	
	String createNewVersion();
	
	String openReadOnly();
	
	String closeFormDesigner();
	
	String openClinica();
	
	String noData();
	
	String noDataToExport();
	
	String exportSuccess();
	
	String changeSettings();
	
	String changeOpenclinicaSettings();
	String search();
	
	String newX();
	String listOfUsers();
	String noUserSelected();
	
	String newUser();
	String editUser(String username);
	String saveAndNext();
	String setPassword();
	String usernameIsNotUnique();
	String passwordAtLeastXCharacters(String numberOfCharacters);
	String invalidEmailAddress();
	String cannotDisableRoleAdministrator();
	String cannotRemoveRoleAdministratorFromLoggedInUser();
	
	String setRolesForUser();
	String availableRoles();
	String rolesAssignedToUser();
	String addRole();
	String addAllRoles();
	String removeRole();
	String removeAllRoles();
	String searchForRole();
	
	String setStudiesForUser();
	String availableStudies();
	String studiesAssignedToUser();
	String addStudy();
	String addAllStudies();
	String removeStudy();
	String removeAllStudies();
	String searchForStudy();
	
	String setFormsForUser();
	String availableForms();
	String formsAssignedToUser();
	String addForm();
	String addAllForms();
	String removeForm();
	String removeAllForms();
	String searchForForm();
	
	String importUsers();
	String importUsersWait();
	String importing();
	String importUserSuccess(int numberOfUsers);
	String importUserError(int numberOfUsers);
	String name();
	String errorMessages();
	
	String newUserEmailSubject();
	String newUserEmail(String firstname, String username, String password, String loginUrl);
	
	String manageUnprocessedData();
	String description();
	String reprocess();
	String reprocessMessage();
	String selectDataToEdit();
	String selectDataToDelete();
	String selectDataToReprocess();
	String selectOnlyOneDataRow();
	
}
