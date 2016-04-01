package fi.otavanopisto.pyramus;

import fi.otavanopisto.pyramus.domainmodel.users.Role;

public final class Common {
  public static String CLIENT_ID = "854885cf-2284-4b17-b63c-a8b189535f8d";
  public static String CLIENT_SECRET = "cqJ4J1if8ca5RMUqaYyFPYToxfFxt2YT8PXL3pNygPClnjJdt55lrFs6k1SZ6colJN24YEtZ7bhFW29S";
  public static String REDIRECT_URL = "https://localhost:8443/oauth2ClientTest/success";
  public static String AUTH_URL = "https://dev.pyramus.fi:8443/users/authorize.page";
  public static String AUTH_CODE = "ff81d5b8500c773e7a1776a7963801e7";
  
  public static Role strToRole(String roleStr) {
    return Role.valueOf(roleStr);
  }
  
  public static String getRoleAuth(Role role) {
    switch(role) {
      case ADMINISTRATOR:
        return "ff81d5b8500c773e7a1776a7963801e7";
      case GUEST:
        return "ff81d5b8500c773e7a1776a7963801e4";
      case MANAGER:
        return "ff81d5b8500c773e7a1776a7963801e6";
      case STUDENT:
        return "ff81d5b8500c773e7a1776a7963801e8";
      case STUDY_GUIDER:
        return "ff81d5b8500c773e7a1776a7963801e0";
      case TEACHER:
        return "ff81d5b8500c773e7a1776a7963801e1";
      case TRUSTED_SYSTEM:
        return "ff81d5b8500c773e7a1776a7963801e9";
      case USER:
        return "ff81d5b8500c773e7a1776a7963801e5";
      default:
        throw new RuntimeException("Missing or EVERYONE role auth requested.");
    }
  }
  
  public static Long getUserId(Role role) {
    switch(role) {
      case ADMINISTRATOR:
        return 7l;
      case GUEST:
        return 4l;
      case MANAGER:
        return 6l;
      case STUDENT:
        return 8l;
      case STUDY_GUIDER:
        return 10l;
      case TEACHER:
        return 11l;
      case TRUSTED_SYSTEM:
        return 9l;
      case USER:
        return 5l;
      default:
        throw new RuntimeException("Missing or EVERYONE role auth requested.");
    }
  }
  
}
