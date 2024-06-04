package fi.otavanopisto.pyramus.rest.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

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
  
  /**
   * Converts Date to OffsetDateTime
   * 
   * @param date
   * @return
   */
  public static OffsetDateTime toOffsetDateTime(Date date) {
    if (date == null) {
      return null;
    }
    // If (as) date is java.sql.Date then toInstant() would cause UnsupportedOperationException
    Date tmpDate = new Date(date.getTime()); 
    Instant instant = tmpDate.toInstant();
    ZoneId systemId = ZoneId.systemDefault();
    ZoneOffset offset = systemId.getRules().getOffset(instant);
    return tmpDate.toInstant().atOffset(offset);
  }

}
