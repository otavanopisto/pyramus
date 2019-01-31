package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StaffMemberProperties {

  public static final EntityProperty STUDY_APPROVER = new EntityProperty("studyApprover", 
      VariableType.BOOLEAN, "staffMemberProperties.studyApprover");

  public static List<EntityProperty> listProperties() {
    return Arrays.asList(STUDY_APPROVER);
  }
}
