package fi.otavanopisto.pyramus.rest.controller.permissions;

import java.util.List;

import fi.otavanopisto.pyramus.security.impl.AbstractPyramusPermissionCollection;
import fi.otavanopisto.pyramus.security.impl.DefaultPermissionRoles;
import fi.otavanopisto.pyramus.security.impl.PermissionScope;
import fi.otavanopisto.pyramus.security.impl.PyramusPermissionCollection;
import fi.otavanopisto.security.PermissionFeature;
import fi.otavanopisto.security.Scope;

public class StudentPermissions extends AbstractPyramusPermissionCollection implements PyramusPermissionCollection {

  /* Features */

  // TODO: RoleFeatures could be separate entity as this is bit awkward to ask with environmentpermission...
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ STUDY_GUIDER })
  public static final String FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION = "FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION";

  /* Student */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENT = "CREATE_STUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  public static final String LIST_STUDENTS = "LIST_STUDENTS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENTSBYPERSON = "LIST_STUDENTSBYPERSON";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STAFFMEMBERSBYPERSON = "LIST_STAFFMEMBERSBYPERSON";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_COURSESTUDENTSBYSTUDENT = "LIST_COURSESTUDENTSBYSTUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER, TRUSTED_SYSTEM })
  @PermissionFeature(PyramusPermissionFeatures.ONLY_GROUP_STUDENTS)
  public static final String FIND_STUDENT = "FIND_STUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  @PermissionFeature(PyramusPermissionFeatures.ONLY_GROUP_STUDENTS)
  public static final String UPDATE_STUDENT = "UPDATE_STUDENT";
  
  @Scope (PermissionScope.STUDENT_OWNER)
  public static final String STUDENT_OWNER = "STUDENT_OWNER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STUDENTPERSON = "UPDATE_STUDENTPERSON";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STUDENTADDITIONALCONTACTINFO = "UPDATE_STUDENTADDITIONALCONTACTINFO";

  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENT = "DELETE_STUDENT";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String ADD_STUDENTEMAIL = "ADD_STUDENTEMAIL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String ADD_STUDENTADDRESS = "ADD_STUDENTADDRESS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String ADD_STUDENTPHONENUMBER = "ADD_STUDENTPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String ADD_STUDENTCONTACTURL = "ADD_STUDENTCONTACTURL";
  
  /**
   * STUDENT emails
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTEMAIL = "CREATE_STUDENTEMAIL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM, STUDY_GUIDER })
  public static final String LIST_STUDENTEMAILS = "LIST_STUDENTEMAILS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, TRUSTED_SYSTEM, STUDY_GUIDER })
  public static final String FIND_STUDENTEMAIL = "FIND_STUDENTEMAIL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTEMAIL = "DELETE_STUDENTEMAIL";
  
  /**
   * STUDENT addresses
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTADDRESS = "CREATE_STUDENTADDRESS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENTADDRESSS = "LIST_STUDENTADDRESSS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STUDENTADDRESS = "FIND_STUDENTADDRESS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String UPDATE_STUDENTADDRESS = "UPDATE_STUDENTADDRESS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTADDRESS = "DELETE_STUDENTADDRESS";
  
  /**
   * STUDENT phones
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTPHONENUMBER = "CREATE_STUDENTPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENTPHONENUMBERS = "LIST_STUDENTPHONENUMBERS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STUDENTPHONENUMBER = "FIND_STUDENTPHONENUMBER";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR })
  public static final String DELETE_STUDENTPHONENUMBER = "DELETE_STUDENTPHONENUMBER";
  
  /**
   * STUDENT contact urls
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER })
  public static final String CREATE_STUDENTCONTACTURL = "CREATE_STUDENTCONTACTURL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENTCONTACTURLS = "LIST_STUDENTCONTACTURLS";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String FIND_STUDENTCONTACTURL = "FIND_STUDENTCONTACTURL";
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, STUDY_PROGRAMME_LEADER })
  public static final String DELETE_STUDENTCONTACTURL = "DELETE_STUDENTCONTACTURL";

  /**
   * STUDENT transfer credits
   */
  
  @Scope (PermissionScope.ENVIRONMENT)
  @DefaultPermissionRoles ({ ADMINISTRATOR, MANAGER, STUDY_PROGRAMME_LEADER, STUDY_GUIDER })
  public static final String LIST_STUDENT_TRANSFER_CREDITS = "LIST_STUDENT_TRANSFER_CREDITS";
   
  @Override
  public List<String> listPermissions() {
    return super.listPermissions(StudentPermissions.class);
  }

  @Override
  public boolean containsPermission(String permission) {
    return listPermissions().contains(permission);
  }

  @Override
  public String getPermissionScope(String permission) throws NoSuchFieldException {
    return super.getPermissionScope(StudentPermissions.class, permission);
  }

  @Override
  public String[] getDefaultRoles(String permission) throws NoSuchFieldException {
    return super.getDefaultRoles(StudentPermissions.class, permission);
  }

  @Override
  public PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException {
    return super.listPermissionFeatures(StudentPermissions.class, permission);
  }
}
