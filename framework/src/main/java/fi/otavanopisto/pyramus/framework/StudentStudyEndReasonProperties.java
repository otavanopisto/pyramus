package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StudentStudyEndReasonProperties {

  public static final EntityProperty STUDY_APPROVER_REQUIRED = new EntityProperty("studyApprovalRequired", 
      VariableType.BOOLEAN, "studentStudyEndReasonProperties.studyApprovalRequired");
  public static final EntityProperty KOSKI_STATE = new EntityProperty("koskiState", 
      VariableType.TEXT, "studentStudyEndReasonProperties.koskiState");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(
        STUDY_APPROVER_REQUIRED, 
        KOSKI_STATE
    );
  }

  public static boolean isProperty(String key) {
    if (StringUtils.isBlank(key)) {
      return false;
    }
    
    return listProperties().stream()
        .map(property -> property.getKey())
        .anyMatch(key::equals);
  }
  
}
