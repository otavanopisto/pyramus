package fi.otavanopisto.pyramus.rest.util;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRoleEnum;

public class PyramusRestUtils {

  /**
   * Converts CourseStaffMemberRoleEnum to the equivalent rest model one.
   * 
   * Returns null if the role parameter is null or if the role is not mapped (they all should be though).
   * 
   * @param role
   * @return
   */
  public static CourseStaffMemberRoleEnum convert(fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum role) {
    if (role == null) {
      return null;
    }
    
    switch (role) {
      case COURSE_TEACHER:
        return CourseStaffMemberRoleEnum.TEACHER;
      case COURSE_TUTOR:
        return CourseStaffMemberRoleEnum.TUTOR;
      case COURSE_ORGANIZER:
        return CourseStaffMemberRoleEnum.ORGANIZER;
    }
    
    return null;
  }

  /**
   * Converts rest model CourseStaffMemberRoleEnum to equivalent domain model one.
   * 
   * Returns null if the role parameter is null or if the role is not mapped (they all should be though).
   * 
   * @param role
   * @return
   */
  public static fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum convert (CourseStaffMemberRoleEnum role) {
    if (role == null) {
      return null;
    }

    switch (role) {
      case TEACHER:
        return fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum.COURSE_TEACHER;
      case TUTOR:
        return fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum.COURSE_TUTOR;
      case ORGANIZER:
        return fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum.COURSE_ORGANIZER;
    }
    
    return null;
  }
}
