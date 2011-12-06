package org.openxdata.server.admin.model.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openxdata.server.admin.model.AbstractEditable;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.User;

/**
 * Maps a <code>Study</code> to a <code>User</code>.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userStudyMap")
public class UserStudyMap extends AbstractEditable {

	@XmlElement
	private int	userId;
	
	@XmlElement
	private int	studyId;
	private static final long serialVersionUID = 2870582564160870766L;

	public UserStudyMap() {
	}
	
	public UserStudyMap(int userId, int studyId) {
		this.userId = userId;
		this.studyId = studyId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStudyId() {
		return this.studyId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}

	/**
	 * Adds the specified <code>User</code> to the Map.
	 * @param user <code>User</code> to remove.
	 */
	public void setUser(User user) {
		setUserId(user.getId());
	}

	/**
	 * Adds the specified <code>StudyDef</code> to the Map.
	 * @param study <code>StudyDef</code> to remove.
	 */
	public void setStudy(StudyDef study) {
		setStudyId(study.getId());
	}
}
