package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StudentStudyEndReasonProperties {

  public static final EntityProperty STUDY_APPROVER_REQUIRED = new EntityProperty("studyApprovalRequired", 
      VariableType.BOOLEAN, "studentStudyEndReasonProperties.studyApprovalRequired");
  public static final EntityProperty KOSKI_STATE = new EntityProperty("koskiState", 
      VariableType.BOOLEAN, "studentStudyEndReasonProperties.koskiState");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(
        STUDY_APPROVER_REQUIRED, 
        KOSKI_STATE
    );
  }
  
}
