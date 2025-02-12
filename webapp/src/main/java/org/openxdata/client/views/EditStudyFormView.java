package org.openxdata.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openxdata.client.Emit;
import org.openxdata.client.controllers.EditStudyFormController;
import org.openxdata.client.controllers.FormDesignerController;
import org.openxdata.client.model.UserSummary;
import org.openxdata.client.util.ProgressIndicator;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.state.EditableState;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import org.openxdata.client.util.FormDefVersionUtil;

/**
 * Encapsulates UI functionality for Editing a given Study/Form/Form version..
 * 
 */
public class EditStudyFormView extends WizardView {

	private FormDefVersion formDefVersion;

	private final TextField<String> studyName = new TextField<String>();
	private final TextField<String> studyDescription = new TextField<String>();
	private ItemAccessListField<UserSummary> userAccessToStudy;
	private Button designFormButton1;

	private final TextField<String> formName = new TextField<String>();
	private final TextField<String> formDescription = new TextField<String>();
	private ItemAccessListField<UserSummary> userAccessToForm;
	private Button designFormButton2;

	private final TextField<String> formVersion = new TextField<String>();
	private final TextField<String> formVersionDescription = new TextField<String>();
	private CheckBox published;
	private Button designFormButton3;

	private final EditStudyFormController controller = (EditStudyFormController) this.getController();

	public EditStudyFormView(EditStudyFormController controller) {
		super(controller);
	}

	@Override
	protected void createButtons() {
		super.createButtons();
	}

	@Override
	protected List<LayoutContainer> createPages() {
		List<LayoutContainer> wizardPages = new ArrayList<LayoutContainer>();
		wizardPages.add(createEditStudyPage());
		wizardPages.add(createEditFormPage());
		wizardPages.add(createEditVersionPage());
		return wizardPages;
	}

	@Override
	protected void display(int activePage, List<LayoutContainer> pages) {
		// nothing special to do here
	}

	@Override
	protected void finish() {
		saveAndExit();
	}

	private LayoutContainer createEditStudyPage() {
		final LayoutContainer editStudyPanel = new LayoutContainer();

		editStudyPanel.setLayout(new FitLayout());
		editStudyPanel.setStyleAttribute("padding", "10px");
		FormPanel formPanel = getWizardFormPanel();
		((FormLayout)formPanel.getLayout()).setDefaultWidth(300);
		studyName.setFieldLabel(appMessages.studyName());
		studyName.setAllowBlank(false);
		formPanel.add(studyName);

		studyDescription.setFieldLabel(appMessages.studyDescription());
		formPanel.add(studyDescription);
		ItemAccessListFieldMessages messages = new ItemAccessListFieldMessages("leftHeading="+appMessages.availableUsers()+"\n" +
        		"rightHeading="+appMessages.usersWithAccessToStudy()+"\n" +
        		"addOne="+appMessages.addUser()+"\n" +
        		"addAll="+appMessages.addAllUsers()+"\n" +
        		"removeOne="+appMessages.removeUser()+"\n" +
        		"removeAll="+appMessages.removeAllUsers()+"\n" +
        		"search="+appMessages.searchForAUser()+"\n" +
        		"loading="+appMessages.loading());
		userAccessToStudy = new ItemAccessListField<UserSummary>(messages, controller.getUserStudyAccessController());
		FieldSet fs = new FieldSet();
		fs.setHeading(appMessages.setUserAccessToStudy());
		fs.setCollapsible(true);
		fs.setExpanded(false);
		fs.add(userAccessToStudy);
		userAccessToStudy.mask();
		formPanel.add(fs);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		designFormButton1 = getDesignFormButton(appMessages.designForm());
		formPanel.add(designFormButton1);
		editStudyPanel.add(formPanel);

		return editStudyPanel;
	}

	private LayoutContainer createEditFormPage() {
		final LayoutContainer editFormPanel = new LayoutContainer();
		editFormPanel.setLayout(new FitLayout());
		editFormPanel.setStyleAttribute("padding", "10px");
		FormPanel formPanel = getWizardFormPanel();
		((FormLayout)formPanel.getLayout()).setDefaultWidth(300);
		formName.setFieldLabel(appMessages.formName());
		formName.setAllowBlank(false);
		formPanel.add(formName);

		formDescription.setFieldLabel(appMessages.formDescription());
		formPanel.add(formDescription);
		ItemAccessListFieldMessages messages = new ItemAccessListFieldMessages("leftHeading="+appMessages.availableUsers()+"\n" +
        		"rightHeading="+appMessages.usersWithAccessToForm()+"\n" +
        		"addOne="+appMessages.addUser()+"\n" +
        		"addAll="+appMessages.addAllUsers()+"\n" +
        		"removeOne="+appMessages.removeUser()+"\n" +
        		"removeAll="+appMessages.removeAllUsers()+"\n" +
        		"search="+appMessages.searchForAUser()+"\n" +
        		"loading="+appMessages.loading());
		userAccessToForm = new ItemAccessListField<UserSummary>(messages, controller.getUserFormAccessController());
		FieldSet fs = new FieldSet();
		fs.setHeading(appMessages.setUserAccessToForm());
		fs.setCollapsible(true);
		fs.setExpanded(false);
		fs.add(userAccessToForm);
		userAccessToForm.mask();
		formPanel.add(fs);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		designFormButton2 = getDesignFormButton(appMessages.designForm());
		formPanel.add(designFormButton2);
		editFormPanel.add(formPanel);

		return editFormPanel;
	}

	private LayoutContainer createEditVersionPage() {
		final LayoutContainer editVersionPage = new LayoutContainer();
		editVersionPage.setLayout(new FitLayout());
		editVersionPage.setStyleAttribute("padding", "10px");
		FormPanel formPanel = getWizardFormPanel();
		formVersion.setFieldLabel(appMessages.formVersion());
		formVersion.setAllowBlank(false);
		formVersion.setEnabled(false);
		formPanel.add(formVersion);

		formVersionDescription.setFieldLabel(appMessages.formVersionDescription());
		formPanel.add(formVersionDescription);

		published = new CheckBox();
		published.setBoxLabel("");
		published.setLabelSeparator("");
		published.setFieldLabel(appMessages.formVersionDefault());
		published.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				if (published.getValue()) {
					published.setBoxLabel("");
				} else {
					published.setBoxLabel(appMessages.publishedHelp());
				}
			}
		});
		formPanel.add(published);
		designFormButton3 = getDesignFormButton(appMessages.designForm());
		formPanel.add(designFormButton3);
		editVersionPage.add(formPanel);

		return editVersionPage;
	}

	private FormPanel getWizardFormPanel() {
		FormPanel formPanel = new FormPanel();
		formPanel.setFrame(false);
		formPanel.setBorders(false);
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(150);
		formPanel.setLayout(layout);
		return formPanel;
	}

	@Override
	protected void handleEvent(AppEvent event) {
		GWT.log("EditStudyFormView : handleEvent");
		if (event.getType() == EditStudyFormController.EDITSTUDYFORM) {

			formDefVersion = event.getData("formVersion");
			FormDef form = formDefVersion.getFormDef();
			StudyDef study = form.getStudy();

			// Set the values of the form to that of the selected Form
			// page 1
			studyName.setValue(study.getName());
			studyDescription.setValue(study.getDescription());
			controller.setStudyForAccessControl(formDefVersion.getFormDef().getStudy());
			userAccessToStudy.refresh();
			userAccessToStudy.unmask();
			
			// page 2
			formName.setValue(form.getName());
			formDescription.setValue(form.getDescription());
			controller.setFormForAccessControl(formDefVersion.getFormDef());
			userAccessToForm.refresh();
			userAccessToForm.unmask();
			
			// page 3
			formVersion.setValue(formDefVersion.getName());
			formVersionDescription.setValue(formDefVersion.getDescription());
			published.setValue(formDefVersion.getIsDefault());

			designFormButton1.setText(appMessages.designForm() + " (" + form.getName() + " " + formDefVersion.getName() + ")");
			designFormButton2.setText(appMessages.designForm() + " (" + formDefVersion.getName() + ")");			
		}
		showWindow(appMessages.editStudyOrForm(), 555, 400);
	}

	private void save(final boolean triggerRefreshEvent) {

		if (formDefVersion == null) {
			return;
		}
		
		if (formDefVersion.getState() == EditableState.HASDATA) {
			MessageBox.info(appMessages.existingDataTitle(), appMessages.cannotSave(), null);
			return;
		}

		// update study/form/version information
		final FormDef form = formDefVersion.getFormDef();
		form.getStudy().setName(studyName.getValue());
		form.getStudy().setDescription(studyDescription.getValue());
		form.setName(formName.getValue());
		form.setDescription(formDescription.getValue());
		formDefVersion.setName(formVersion.getValue());
		formDefVersion.setDescription(formVersionDescription.getValue());
		formDefVersion.setIsDefault(published.getValue());
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				controller.saveForm(form, triggerRefreshEvent);
			}
		});
	}

	@Override
	public void saveAndExit() {
		ProgressIndicator.showProgressBar();
		try {
			save(true);
		} finally {
			ProgressIndicator.hideProgressBar();
		}
	}

	public void onFormDataCheckComplete(Boolean hasData) {
		if (hasData) {
			closeWindow();
			new StudyFormHasDataChoiceView().show();
		} else {
			closeWindow();
			launchDesigner(false);
		}
	}
	
	public FormDefVersion createNewVersionForFormWithData() {
		FormDef form = formDefVersion.getFormDef();
		formVersion.setValue(form.getNextVersionName()); // this value is used in the save
		FormDefVersion version = new FormDefVersion();
		form.addVersion(version);
		version.setFormDef(form);
		version.setIsDefault(false);
		version.setCreator((User) Registry.get(Emit.LOGGED_IN_USER_NAME));
		version.setDateCreated(new Date());
		version.setFormDef(formDefVersion.getFormDef());
		version.setLayout(formDefVersion.getLayout());
		version.setXform(formDefVersion.getXform());
		version.setName(formVersion.getValue());
		version.setDescription(formDefVersion.getDescription());
		FormDefVersionUtil.renameFormBinding(version,
		FormDefVersionUtil.generateDefaultFormBinding(version));
		FormDefVersionUtil.renameXformName(version,formVersion.getValue());
		formDefVersion = version; // replace selected version with the new one so future saves will work
		return version;
	}
	
	public void launchDesigner(FormDefVersion formDefVersion, boolean readOnly) {
		this.formDefVersion = formDefVersion;
        launchDesigner(readOnly);
	}
	
	public void launchDesigner(boolean readOnly) {
		AppEvent event = new AppEvent((readOnly ? FormDesignerController.READONLY_FORM : FormDesignerController.EDIT_FORM));
        event.setData("formDefVersion", formDefVersion);
        Dispatcher.get().dispatch(event);
	}

	public Button getDesignFormButton(String label) {
		final Button designFormButton = new Button(label);
		designFormButton.addListener(Events.Select,
				new Listener<ButtonEvent>() {

					@Override
					public void handleEvent(ButtonEvent be) {
                                                designFormButton.setEnabled(false);
						controller.formHasData(formDefVersion);
					}
				});
		return designFormButton;
	}
}