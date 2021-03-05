package fi.otavanopisto.pyramus.domainmodel.worklist;

public enum WorklistItemTemplateType {

  EDITABLE,               // Default template; items based on this template are user-editable
  UNEDITABLE,             // Default template;  items based on this template are not user-editable
  COURSE_ASSESSMENT,      // Template for course assessment; items based on this template are not user-editable
  GRADE_RAISE             // Template for raised grade; items based on this template are not user-editable
  
}
