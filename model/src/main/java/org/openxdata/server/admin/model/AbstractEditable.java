package org.openxdata.server.admin.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.openxdata.server.admin.model.state.EditableState;

/**
 * This serves as the base class for domain objects which can be edited.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "abstractEditable")
public abstract class AbstractEditable extends LightEntity implements Editable{
	
	/**unique identifier for the model object. Correlates to the database PK. */
	protected int id = 0;
	
	private static final long serialVersionUID = 7939951597182605859L;

	/** A flag to determine if the object has been changed and hence needs saving. */
	@XmlElement(name = "dirty")
	protected boolean dirty;
	
	/** A flag which tells that the objects has validation errors which need to be
	 * Corrected before it can be saved.
	 */
	@XmlElement(name = "hasErrors", nillable = true)
	protected boolean hasErrors;
	
	/** The user who last changed or edited the object. */
	@XmlElement(name = "changedBy", type = User.class)
	protected User changedBy;
	
	/** The date when the object was last edited or changed. */
	@XmlElement
	protected Date dateChanged;
	
	/** The user who first submitted this data. */
	@XmlElement(name = "creator", type = User.class)
	protected User creator;
	
	/** The date when this data was first submitted. */ 
	@XmlElement
	protected Date dateCreated;

	@XmlElement(name = "retired", nillable = true)
	protected Boolean retired = false;
	@XmlElement(name = "retiredBy", type = User.class)
	protected User retiredBy;
	@XmlElement
	protected Date dateRetired;
	@XmlElement
	protected String retiredReason;
	
	@XmlElement(name = "state", type = EditableState.class)
	private EditableState state = EditableState.LOADED;	
	
	public void setId(int id){
		this.id = id;
	}
	
	@Override
	public int getId(){
		return id;
	}
	
	@Override
	public boolean isNew(){
		return id == 0;
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	@Override
	public boolean hasErrors(){
		return hasErrors;
	}
	
	@Override
	public void setHasErrors(boolean hasErrors){
		this.hasErrors = hasErrors;
	}
	
	public User getChangedBy(){
		return changedBy;
	}
	
	@Override
	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}
	
	public Date getDateChanged(){
		return dateChanged;
	}
	
	@Override
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public EditableState getState(){
		return this.state ;
	}
	
	public void setState(EditableState state){
		this.state = state;
	}
	
	protected Date getDateRetired() {
		return dateRetired;
	}

	protected void setDateRetired(Date dateRetired) {
		this.dateRetired = dateRetired;
	}

	protected Boolean getRetired() {
		return retired;
	}

	protected void setRetired(Boolean retired) {
		this.retired = retired;
	}

	protected User getRetiredBy() {
		return retiredBy;
	}

	public void setRetiredBy(User retiredBy) {
		this.retiredBy = retiredBy;
	}

	public String getRetiredReason() {
		return retiredReason;
	}

	public void setRetiredReason(String voidReason) {
		this.retiredReason = voidReason;
	}
}
