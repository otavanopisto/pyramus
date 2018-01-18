package fi.otavanopisto.pyramus.koski;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

//@Stateless
@Singleton
@Startup
public class KoskiScheduler {

  @Resource
  private ManagedScheduledExecutorService mses;
  
  @PostConstruct
  public void pc() {
    mses.scheduleWithFixedDelay(this::b, 60, 10, TimeUnit.SECONDS);
  }
  
//  @Schedule(second="*", minute="*", hour="*", persistent=false)
//  public void a() {
//    System.out.println("FUU");
//  }
  
  public void b() {
//    System.out.println("BUHUU");
//    try {
//      Thread.sleep(new Random().nextInt(1000));
//    } catch (InterruptedException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
  }
  
}
