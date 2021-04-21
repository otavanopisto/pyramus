package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudyProgrammeData;

public class WebhookStudyProgrammeCreatePayload extends WebhookPayload<WebhookStudyProgrammeData> {

  public WebhookStudyProgrammeCreatePayload(Long studyProgrammeId) {
    super(WebhookType.STUDYPROGRAMME_CREATE, new WebhookStudyProgrammeData(studyProgrammeId));
  }
  
}
