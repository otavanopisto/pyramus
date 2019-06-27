package fi.otavanopisto.pyramus.rest;

import java.time.OffsetDateTime;
import java.util.Map;

public interface AbstractRestServicePermissionsTestI {

  Map<String, String> getAdminAuthHeaders();

  OffsetDateTime getDate(int year, int monthOfYear, int dayOfMonth);

}
