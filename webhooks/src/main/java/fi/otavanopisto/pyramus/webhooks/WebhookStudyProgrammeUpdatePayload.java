package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudyProgrammeData;

public class WebhookStudyProgrammeUpdatePayload extends WebhookPayload<WebhookStudyProgrammeData> {

  public WebhookStudyProgrammeUpdatePayload(Long studyProgrammeId) {
    super(WebhookType.STUDYPROGRAMME_UPDATE, new WebhookStudyProgrammeData(studyProgrammeId));
  }
  
}
