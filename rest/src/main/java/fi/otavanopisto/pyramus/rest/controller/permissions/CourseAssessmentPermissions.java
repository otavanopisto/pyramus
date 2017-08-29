package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class CourseAssessmentPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {


  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String CREATE_COURSEASSESSMENT = "CREATE_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String UPDATE_COURSEASSESSMENT = "UPDATE_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSEASSESSMENT = "LIST_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_COURSEASSESSMENT = "FIND_COURSEASSESSMENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String DELETE_COURSEASSESSMENT = "DELETE_COURSEASSESSMENT";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String CREATE_COURSEASSESSMENTREQUEST = "CREATE_COURSEASSESSMENTREQUEST";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String UPDATE_COURSEASSESSMENTREQUEST = "UPDATE_COURSEASSESSMENTREQUEST";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSEASSESSMENTREQUESTS = "LIST_COURSEASSESSMENTREQUESTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_COURSEASSESSMENTREQUEST = "FIND_COURSEASSESSMENTREQUEST";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String DELETE_COURSEASSESSMENTREQUEST = "DELETE_COURSEASSESSMENTREQUEST";

  @Override
  public List<String> listPermissions() {
    return super.listPermissions(CourseAssessmentPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(CourseAssessmentPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(CourseAssessmentPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(CourseAssessmentPermissions.class, permission);
  }
}
