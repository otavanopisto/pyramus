package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StaffMemberProperties {

  public static final EntityProperty STUDY_APPROVER = new EntityProperty("studyApprover", 
      VariableType.BOOLEAN, "staffMemberProperties.studyApprover");

  public static final EntityProperty WORKLIST_APPROVER = new EntityProperty("worklistApprover", 
      VariableType.BOOLEAN, "staffMemberProperties.worklistApprover");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(STUDY_APPROVER, WORKLIST_APPROVER);
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
