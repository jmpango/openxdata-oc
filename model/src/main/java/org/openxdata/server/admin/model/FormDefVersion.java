package org.openxdata.server.admin.model;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.bind.CycleRecoverable;

/**
 * The form definition version. For each form defined, we can have many versions to support
 * changing of form definition without breaking already collected data.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "formDefVersion")
public class FormDefVersion extends AbstractEditable implements Exportable, CycleRecoverable {

	private static final long serialVersionUID = 3882276404608627490L;

	/** The display name of the form version. */
	@XmlElement
	private String name;
	
	/** Description of the form version. */
	@XmlElement
	private String description;
		
	/** The form definition whose version we represent. */
	@XmlElement(name = "formDef", type = FormDef.class)
	private FormDef formDef;
	
	@XmlElement
	private String xform;
	
	@XmlElement
	private String layout;
	
	@XmlElement
	private Boolean isDefault = true;
	
	/** A list of the form text for different locales. */
	@XmlElementWrapper(name = "formDefVersionTexts", required = false)
	@XmlElement(name = "formDefVersionText", type = FormDefVersionText.class)
	private List<FormDefVersionText> versionText;
	
	public FormDefVersion() {
	}
	
	public FormDefVersion(int versionId, String name,FormDef formDef){
		this.id = versionId;
		this.name = name;
		this.formDef = formDef;
	}
	
	public FormDefVersion(int versionId, String name, String description,FormDef formDef) {
		this(versionId,name,formDef);
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getXform() {
		return xform;
	}

	public void setXform(String xform) {
		this.xform = xform;
	}

	public FormDef getFormDef() {
		return formDef;
	}

	public void setFormDef(FormDef formDef) {
		this.formDef = formDef;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Override
	public boolean isNew(){
		
		if(id == 0)
			return true;
		
		if(versionText == null)
			return false;
		
		for(FormDefVersionText text : versionText){
			if(text.isNew())
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isDirty() {
		if(super.isDirty())
			return true;
		
		if(versionText == null)
			return false;
		
		for(FormDefVersionText text : versionText){
			if(text.isDirty())
				return true;
		}
		
		return false;
	}
	
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		
		if(versionText == null)
			return;
		
		for(FormDefVersionText text : versionText)
			text.setDirty(dirty);
	}
	
	public List<FormDefVersionText> getVersionText() {
		return versionText;
	}

	public void setVersionText(List<FormDefVersionText> versionText) {
		this.versionText = versionText;
	}
	
	public void addVersionText(FormDefVersionText formDefVersionText){
		if(versionText == null)
			versionText = new Vector<FormDefVersionText>();
		versionText.add(formDefVersionText);
	}
	
	public void removeVersionText(FormDefVersionText formDefVersionText){
		versionText.remove(formDefVersionText);
	}
	
	public FormDefVersionText getFormDefVersionText(String locale){
		if(versionText == null)
			return null;
		
		for(FormDefVersionText text : versionText){
			if(locale.equalsIgnoreCase(text.getLocaleKey()))
				return text;
		}
		
		return null;
	}

	@Override
	public String getType() {
		return "version";
	}
	
	/** 
	 * this method is called by JAXB when a cycle is detected
	 */
	public FormDefVersion onCycleDetected(Context arg0) {
		FormDefVersion replacement = new FormDefVersion();
		replacement.setId(this.getId());		
		return replacement;
	}
}
