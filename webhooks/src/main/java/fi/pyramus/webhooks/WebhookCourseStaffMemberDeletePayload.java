package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookCourseStaffMemberData;

public class WebhookCourseStaffMemberDeletePayload extends WebhookPayload<WebhookCourseStaffMemberData> {

  public WebhookCourseStaffMemberDeletePayload(Long courseStaffMemberId, Long courseId, Long staffMemberId) {
    super(WebhookType.COURSE_STAFF_MEMBER_DELETE, new WebhookCourseStaffMemberData(courseStaffMemberId, courseId, staffMemberId));
  }
  
}
