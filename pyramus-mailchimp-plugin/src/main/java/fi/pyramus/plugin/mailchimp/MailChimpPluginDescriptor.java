package fi.otavanopisto.pyramus.plugin.mailchimp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.otavanopisto.pyramus.plugin.PluginDescriptor;
import fi.otavanopisto.pyramus.plugin.scheduler.ScheduledPluginDescriptor;
import fi.otavanopisto.pyramus.plugin.scheduler.ScheduledPluginTask;

public class MailChimpPluginDescriptor implements PluginDescriptor, ScheduledPluginDescriptor {

  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }

  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }

  public String getName() {
    return "MailChimp";
  }

  public Map<String, Class<?>> getPageHookControllers() {
    return null;
  }

  public Map<String, Class<?>> getPageRequestControllers() {
    return null;
  }

  public Map<String, Class<?>> getAuthenticationProviders() {
    return null;
  }

  public String getMessagesBundlePath() {
    return null;
  }

  @Override
  public List<ScheduledPluginTask> getScheduledTasks() {
    List<ScheduledPluginTask> result = new ArrayList<ScheduledPluginTask>();

    result.add(new MailChimpSynchronizationTask());

    return result;
  }

}
