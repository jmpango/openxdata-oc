package org.openxdata.server.admin.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents locale text of a form version.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "formDefVersionText")
public class FormDefVersionText extends AbstractEditable{

	private static final long serialVersionUID = -8844252387415837817L;
	
	/** The key of the locale. */
	@XmlElement
	private String localeKey;
	
	/** The locale text for the xform as per the localeKey field. */
	@XmlElement
	private String xformText;
	
	/** The locale text for the layout xml as per the localeKey field. */
	@XmlElement
	private String layoutText;
	public FormDefVersionText() {
	}
	
	public FormDefVersionText(String locale, String xformText, String layoutText){
		this.localeKey = locale;
		this.xformText = xformText;
		this.layoutText = layoutText;
	}
		
	public String getLocaleKey() {
		return localeKey;
	}
	
	public void setLocaleKey(String localeKey) {
		this.localeKey = localeKey;
	}
	
	public String getXformText() {
		return xformText;
	}
	
	public void setXformText(String xformText) {
		this.xformText = xformText;
	}
	
	public String getLayoutText() {
		return layoutText;
	}
	
	public void setLayoutText(String layoutText) {
		this.layoutText = layoutText;
	}
}