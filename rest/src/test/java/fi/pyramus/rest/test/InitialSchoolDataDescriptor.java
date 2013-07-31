package fi.pyramus.rest.test;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.rest.controller.SchoolController;

@Stateful
@ApplicationScoped
public class InitialSchoolDataDescriptor implements Serializable {

	private static final long serialVersionUID = 4946586734273110933L;
	
	@Inject
	private SchoolController schoolController;

	@PostConstruct
	public void init() {
		SchoolField universitySchoolField = schoolController.createSchoolField(getSchoolFieldName());
		School mitSchool = schoolController.createSchool(getSchoolCode(), getSchoolName(), universitySchoolField);
		
		setSchoolFieldId(universitySchoolField.getId());
		setSchoolId(mitSchool.getId());
	}
	
	public Long getSchoolFieldId() {
		return schoolFieldId;
	}
	
	public void setSchoolFieldId(Long schoolFieldId) {
		this.schoolFieldId = schoolFieldId;
	}
	
	public Long getSchoolId() {
		return schoolId;
	}
	
	public String getSchoolFieldName() {
		return "University";
	}

	public String getSchoolName() {
		return "Massachusetts Institute of Technology";
	}
	
	public String getSchoolCode() {
		return "MIT";
	}
	
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	
	private Long schoolFieldId;
	private Long schoolId;
}
