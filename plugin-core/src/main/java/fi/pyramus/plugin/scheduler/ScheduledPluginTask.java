package fi.pyramus.plugin.scheduler;

public interface ScheduledPluginTask {

  public ScheduledTaskInternal getInternal();
  public void execute() throws ScheduledTaskException;
  
}
