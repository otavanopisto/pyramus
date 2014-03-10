package fi.pyramus.plugin.scheduler;

import java.util.List;

public interface ScheduledPluginDescriptor {

  public List<ScheduledPluginTask> getScheduledTasks();
  
}