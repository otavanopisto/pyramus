package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudyProgrammeData;

public class WebhookStudyProgrammeArchivePayload extends WebhookPayload<WebhookStudyProgrammeData> {

  public WebhookStudyProgrammeArchivePayload(Long studyProgrammeId) {
    super(WebhookType.STUDYPROGRAMME_ARCHIVE, new WebhookStudyProgrammeData(studyProgrammeId));
  }
  
}
