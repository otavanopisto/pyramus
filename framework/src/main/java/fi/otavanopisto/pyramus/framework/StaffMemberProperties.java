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

  public static final EntityProperty REPORT_SIGNATORY = new EntityProperty("reportSignatory", 
      VariableType.BOOLEAN, "staffMemberProperties.reportSignatory");

  public static final EntityProperty APPLICATIONS_AINEOPISKELU = new EntityProperty("applicationsAineopiskelu",
      VariableType.BOOLEAN, "staffMemberProperties.applicationsAineopiskelu");

  public static final EntityProperty APPLICATIONS_NETTILUKIO = new EntityProperty("applicationsNettilukio",
      VariableType.BOOLEAN, "staffMemberProperties.applicationsNettilukio");

  public static final EntityProperty APPLICATIONS_NETTIPERUSKOULU = new EntityProperty("applicationsNettiperuskoulu",
      VariableType.BOOLEAN, "staffMemberProperties.applicationsNettiperuskoulu");

  public static final EntityProperty APPLICATIONS_AIKUISLUKIO = new EntityProperty("applicationsAikuislukio",
      VariableType.BOOLEAN, "staffMemberProperties.applicationsAikuislukio");

  public static final EntityProperty APPLICATIONS_AIKUISTENPERUSOPETUS = new EntityProperty("applicationsAikuistenperusopetus",
      VariableType.BOOLEAN, "staffMemberProperties.applicationsAikuistenperusopetus");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(STUDY_APPROVER, WORKLIST_APPROVER, REPORT_SIGNATORY, APPLICATIONS_AINEOPISKELU, APPLICATIONS_NETTILUKIO,
        APPLICATIONS_NETTIPERUSKOULU, APPLICATIONS_AIKUISLUKIO, APPLICATIONS_AIKUISTENPERUSOPETUS);
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
