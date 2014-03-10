package fi.pyramus.plugin.scheduler;

public interface ScheduledPluginTask {

  public ScheduledTaskInterval getInternal();
  public void execute() throws ScheduledTaskException;
  
}
