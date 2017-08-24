package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class CoursePermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSE = "CREATE_COURSE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, USER, GUEST, STUDENT, TRUSTED_SYSTEM })
  public static final String LIST_COURSES = "LIST_COURSES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, USER, GUEST, STUDENT, TRUSTED_SYSTEM })
  public static final String LIST_COURSESBYSUBJECT = "LIST_COURSESBYSUBJECT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, USER, GUEST, STUDENT, TRUSTED_SYSTEM })
  public static final String FIND_COURSE = "FIND_COURSE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String UPDATE_COURSE = "UPDATE_COURSE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSE = "DELETE_COURSE";
  
  /**
   * COURSEComponent
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSECOMPONENT = "CREATE_COURSECOMPONENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String LIST_COURSECOMPONENTS = "LIST_COURSECOMPONENTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM })
  public static final String FIND_COURSECOMPONENT = "FIND_COURSECOMPONENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSECOMPONENT = "UPDATE_COURSECOMPONENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSECOMPONENT = "DELETE_COURSECOMPONENT";
  
  /**
   * COURSEDescription
   */

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSEDESCRIPTION = "CREATE_COURSEDESCRIPTION";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_COURSEDESCRIPTIONS = "LIST_COURSEDESCRIPTIONS";
  
  /**
   * COURSEStaffMember
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSESTUDENT = "CREATE_COURSESTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_COURSESTUDENTS = "LIST_COURSESTUDENTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_COURSESTUDENT = "FIND_COURSESTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String UPDATE_COURSESTUDENT = "UPDATE_COURSESTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSESTUDENT = "DELETE_COURSESTUDENT";

  /**
   * COURSEState
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSESTATE = "CREATE_COURSESTATE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSESTATES = "LIST_COURSESTATES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_COURSESTATE = "FIND_COURSESTATE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSESTATE = "UPDATE_COURSESTATE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String ARCHIVE_COURSESTATE = "ARCHIVE_COURSESTATE";

  /**
   * course types
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSETYPE = "CREATE_COURSETYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSETYPES = "LIST_COURSETYPES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_COURSETYPE = "FIND_COURSETYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSETYPE = "UPDATE_COURSETYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String ARCHIVE_COURSETYPE = "ARCHIVE_COURSETYPE";
  
  /**
   * COURSEStudent
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSESTAFFMEMBER = "CREATE_COURSESTAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_COURSESTAFFMEMBERS = "LIST_COURSESTAFFMEMBERS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_COURSESTAFFMEMBER = "FIND_COURSESTAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSESTAFFMEMBER = "UPDATE_COURSESTAFFMEMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSESTAFFMEMBER = "DELETE_COURSESTAFFMEMBER";
  
  /**
   * CourseEnrolmentType
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSEENROLMENTTYPE = "CREATE_COURSEENROLMENTTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_COURSEENROLMENTTYPES = "LIST_COURSEENROLMENTTYPES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_COURSEENROLMENTTYPE = "FIND_COURSEENROLMENTTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSEENROLMENTTYPE = "UPDATE_COURSEENROLMENTTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSEENROLMENTTYPE = "DELETE_COURSEENROLMENTTYPE";

  /**
   * CourseParticipationType
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSEPARTICIPATIONTYPE = "CREATE_COURSEPARTICIPATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_COURSEPARTICIPATIONTYPES = "LIST_COURSEPARTICIPATIONTYPES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_COURSEPARTICIPATIONTYPE = "FIND_COURSEPARTICIPATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSEPARTICIPATIONTYPE = "UPDATE_COURSEPARTICIPATIONTYPE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String ARCHIVE_COURSEPARTICIPATIONTYPE = "ARCHIVE_COURSEPARTICIPATIONTYPE";

  /**
   * CourseDescriptionCategory
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_COURSEDESCRIPTIONCATEGORY = "CREATE_COURSEDESCRIPTIONCATEGORY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSEDESCRIPTIONCATEGORIES = "LIST_COURSEDESCRIPTIONCATEGORIES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_COURSEDESCRIPTIONCATEGORY = "FIND_COURSEDESCRIPTIONCATEGORY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_COURSEDESCRIPTIONCATEGORY = "UPDATE_COURSEDESCRIPTIONCATEGORY";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_COURSEDESCRIPTIONCATEGORY = "DELETE_COURSEDESCRIPTIONCATEGORY";

  /**
   * StaffMemberRole
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STAFFMEMBERROLE = "CREATE_STAFFMEMBERROLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STAFFMEMBERROLES = "LIST_STAFFMEMBERROLES";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String FIND_STAFFMEMBERROLE = "FIND_STAFFMEMBERROLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STAFFMEMBERROLE = "UPDATE_STAFFMEMBERROLE";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STAFFMEMBERROLE = "DELETE_STAFFMEMBERROLE";
  
  /**
   * CourseEducationType & CourseEducationSubtype
   */

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_COURSEEDUCATIONTYPES = "LIST_COURSEEDUCATIONTYPES";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ EVERYONE })
  public static final String LIST_COURSEEDUCATIONSUBTYPES = "LIST_COURSEEDUCATIONSUBTYPES";
  
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(CoursePermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(CoursePermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(CoursePermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(CoursePermissions.class, permission);
  }
}
