package fi.otavanopisto.pyramus.plugin.scheduler;

import java.util.List;

public interface ScheduledPluginDescriptor {

  public List<ScheduledPluginTask> getScheduledTasks();
  
}