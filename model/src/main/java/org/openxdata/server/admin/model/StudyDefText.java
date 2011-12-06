package org.openxdata.server.admin.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains text for a study in a given locale.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "studyDefText")
public class StudyDefText extends AbstractEditable{

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = -4438293413015913822L;

	/** The database identifier for the study text. */
	@XmlElement
	private int studyTextId = 0;
	
	/** The database identifier for the study whose text we contain. */
	@XmlElement
	private int studyId;
	
	/** The locale key for this study text. */
	@XmlElement
	private String localeKey;
	
	/** The name of the study in the locale specified by the localKey field. */
	@XmlElement
	private String name;
	
	/** The description of the study in the locale specified in the localeKey field. */
	@XmlElement
	private String description;
	
	/**
	 * Constructs a new study text object.
	 */
	public StudyDefText(){
		
	}

	public int getStudyTextId() {
		return studyTextId;
	}

	@Override
	public int getId() {
		return studyTextId;
	}

	public void setStudyTextId(int studyTextId) {
		this.studyTextId = studyTextId;
	}

	public int getStudyId() {
		return studyId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}

	public String getLocaleKey() {
		return localeKey;
	}

	public void setLocaleKey(String localeKey) {
		this.localeKey = localeKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean isNew(){
		return studyTextId == 0;
	}
}
