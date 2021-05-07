package fi.otavanopisto.pyramus.rest.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemState;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;

@Dependent
@Stateless
public class WorklistController {

  @Inject
  private Logger logger;

  @Inject
  private WorklistItemDAO worklistItemDAO;

  @Inject
  private WorklistItemTemplateDAO worklistItemTemplateDAO;

  public WorklistItem create(User user, WorklistItemTemplate template, Date entryDate, String description, Double price, Double factor,
      String billingNumber, CourseAssessment courseAssessment, User currentUser) {
    return worklistItemDAO.create(template, user, entryDate, description, price, factor, billingNumber, courseAssessment, currentUser);
  }

  public WorklistItem update(WorklistItem worklistItem, Date entryDate, String description, Double price, Double factor, String billingNumber, WorklistItemState state, User currentUser) {
    return worklistItemDAO.update(worklistItem, entryDate, description, price, factor, billingNumber, state, currentUser);
  }
  
  public void updateState(List<WorklistItem> worklistItems, WorklistItemState state, boolean enforceChangeLogic) {
    for (WorklistItem worklistItem : worklistItems) {
      if (!enforceChangeLogic || isValidStateChange(worklistItem.getState(), state)) {
        worklistItemDAO.updateState(worklistItem, state);
      }
    }
  }

  public void removeByCourseAssessment(CourseAssessment courseAssessment, boolean permanent) {
    if (permanent) {
      List<WorklistItem> worklistItems = worklistItemDAO.listByCourseAssessment(courseAssessment);
      for (WorklistItem worklistItem : worklistItems) {
        if (worklistItem.getState() == WorklistItemState.ENTERED || worklistItem.getState() == WorklistItemState.PROPOSED) {
          worklistItemDAO.delete(worklistItem);
        }
      }
    }
    else {
      List<WorklistItem> worklistItems = worklistItemDAO.listByCourseAssessmentAndArchived(courseAssessment, Boolean.FALSE);
      for (WorklistItem worklistItem : worklistItems) {
        if (worklistItem.getState() == WorklistItemState.ENTERED || worklistItem.getState() == WorklistItemState.PROPOSED) {
          worklistItemDAO.archive(worklistItem);
        }
      }
    }
  }

  public void remove(WorklistItem worklistItem, boolean permanent) {
    if (permanent) {
      worklistItemDAO.delete(worklistItem);
    }
    else {
      worklistItemDAO.archive(worklistItem);
    }
  }

  public WorklistItemTemplate findTemplateById(Long id) {
    return worklistItemTemplateDAO.findById(id);
  }

  public WorklistItemTemplate getTemplateForCourseAssessment(boolean raisedGrade) {
    Set<WorklistItemTemplateType> templateTypes = new HashSet<>();
    if (!raisedGrade) {
      templateTypes.add(WorklistItemTemplateType.COURSE_ASSESSMENT);
    }
    else {
      templateTypes.add(WorklistItemTemplateType.GRADE_RAISE);
    }
    List<WorklistItemTemplate> templates = worklistItemTemplateDAO.listByTemplateTypesAndArchived(templateTypes, false);
    if (templates.size() > 1) {
      logger.log(Level.SEVERE, "Multiple course assessment templates defined");
    }
    return templates.size() == 1 ? templates.get(0) : null;
  }

  public WorklistItem findItemById(Long id) {
    return worklistItemDAO.findById(id);
  }

  public List<WorklistItemTemplate> listWorklistItemTemplates(boolean includeNonCreatable) {
    List<WorklistItemTemplate> templates;
    if (includeNonCreatable) {
      templates = worklistItemTemplateDAO.listUnarchived();
    }
    else {
      templates = worklistItemTemplateDAO.listByTemplateTypesAndArchived(EnumSet.of(WorklistItemTemplateType.DEFAULT), false);
    }
    templates.sort(Comparator.comparing(WorklistItemTemplate::getDescription));
    return templates;
  }

  public List<WorklistItem> listWorklistItemsByOwner(User owner) {
    List<WorklistItem> worklistItems = worklistItemDAO.listByOwnerAndArchived(owner, false);
    worklistItems.sort(Comparator.comparing(WorklistItem::getEntryDate));
    return worklistItems;
  }

  public List<WorklistItem> listWorklistItemsByOwnerAndTimeframe(User owner, Date beginDate, Date endDate) {
    List<WorklistItem> worklistItems = worklistItemDAO.listByOwnerAndTimeframeAndArchived(owner, beginDate, endDate, false);
    worklistItems.sort(Comparator.comparing(WorklistItem::getEntryDate));
    return worklistItems;
  }
  
  private boolean isValidStateChange(WorklistItemState oldState, WorklistItemState newState) {
    return newState.ordinal() - 1 == oldState.ordinal();
  }

}