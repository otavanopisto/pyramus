package fi.pyramus.plugin.scheduler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import fi.pyramus.plugin.PluginManager;

@Singleton
public class PluginScheduler {
  
  private static Logger logger = Logger.getLogger(PluginScheduler.class.getName());

  @Schedule(minute = "0", hour = "0", dayOfMonth="1", persistent = false)
  public void fireMonthly() {
    executeTasks(ScheduledTaskInterval.MONTH);
  }

  @Schedule(minute = "0", hour = "0", dayOfWeek="0", persistent = false)
  public void fireWeekly() {
    executeTasks(ScheduledTaskInterval.WEEK);
  }

  @Schedule(minute = "0", hour = "0", persistent = false)
  public void executeDaily() {
    executeTasks(ScheduledTaskInterval.DAY);
  }

  @Schedule(minute = "0", hour = "*", persistent = false)
  public void executeHourly() {
    executeTasks(ScheduledTaskInterval.HOUR);
  }

  @Schedule(minute = "*/30", hour = "*", persistent = false)
  public void executeHalfHourly() {
    executeTasks(ScheduledTaskInterval.HALF_HOUR);
  }

  @Schedule(minute = "*/15", hour = "*", persistent = false)
  public void executeQuaterHourly() {
    executeTasks(ScheduledTaskInterval.QUATER_HOUR);
  }

  @Schedule(minute = "*/1", hour = "*", persistent = false)
  public void executeMinutely() {
    executeTasks(ScheduledTaskInterval.MINUTE);
  }
  
  private void executeTasks(ScheduledTaskInterval internal) {
    List<ScheduledPluginTask> scheduledTasks = PluginManager.getInstance().getScheduledTasks(internal);
    if (scheduledTasks != null) {
      executeTasks(scheduledTasks);
    }
  }
 
  private void executeTasks(List<ScheduledPluginTask> scheduledTasks) {
    for (ScheduledPluginTask scheduledTask : scheduledTasks) {
      try {
        scheduledTask.execute();
      } catch (ScheduledTaskException e) {
        logger.log(Level.SEVERE, "Error occurred while exucting scheduled task.", e);
      }
    }
    
  }
}
