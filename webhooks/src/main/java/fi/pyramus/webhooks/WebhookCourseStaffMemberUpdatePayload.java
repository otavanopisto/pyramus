package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookCourseStaffMemberData;

public class WebhookCourseStaffMemberUpdatePayload extends WebhookPayload<WebhookCourseStaffMemberData> {

  public WebhookCourseStaffMemberUpdatePayload(Long courseStaffMemberId, Long courseId, Long staffMemberId) {
    super(WebhookType.COURSE_STAFF_MEMBER_UPDATE, new WebhookCourseStaffMemberData(courseStaffMemberId, courseId, staffMemberId));
  }
  
}
