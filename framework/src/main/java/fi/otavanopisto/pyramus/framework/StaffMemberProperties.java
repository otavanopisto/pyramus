package fi.otavanopisto.pyramus.framework;

import java.util.Arrays;
import java.util.List;

import fi.otavanopisto.pyramus.domainmodel.base.VariableType;

public class StaffMemberProperties {

  public static final StaffMemberProperty STUDY_APPROVER = new StaffMemberProperty("studyApprover", 
      VariableType.BOOLEAN, "staffMemberProperties.studyApprover");

  public static List<StaffMemberProperty> listProperties() {
    return Arrays.asList(STUDY_APPROVER);
  }
}
