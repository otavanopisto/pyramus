package fi.otavanopisto.pyramus.domainmodel.worklist;

public enum WorklistItemTemplateType {

  DEFAULT,                // Default template; items based on this template can be created by the user
  COURSE_ASSESSMENT,      // Template for course assessment; items based on this template are not user-editable
  GRADE_RAISE             // Template for raised grade; items based on this template are not user-editable
  
}
