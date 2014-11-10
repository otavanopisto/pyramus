package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookCourseStaffMemberData;

public class WebhookCourseStaffMemberCreatePayload extends WebhookPayload<WebhookCourseStaffMemberData> {

  public WebhookCourseStaffMemberCreatePayload(Long courseStaffMemberId, Long courseId, Long staffMemberId) {
    super(WebhookType.COURSE_STAFF_MEMBER_CREATE, new WebhookCourseStaffMemberData(courseStaffMemberId, courseId, staffMemberId));
  }
  
}
