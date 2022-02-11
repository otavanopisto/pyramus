package fi.otavanopisto.pyramus.envers;

import javax.servlet.http.HttpSession;

import org.hibernate.envers.RevisionListener;

import fi.otavanopisto.pyramus.util.ThreadSessionContainer;

public class PyramusRevisionEntityListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    PyramusRevisionEntity customRevisionEntity = (PyramusRevisionEntity) revisionEntity;
    customRevisionEntity.setUserId(getLoggedUserId());
  }

  private Long getLoggedUserId() {
    HttpSession httpSession = ThreadSessionContainer.getSession();
    if (httpSession != null) {
      return (Long) httpSession.getAttribute("loggedUserId");
    }
    return null;
  }

}