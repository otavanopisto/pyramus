package fi.otavanopisto.pyramus.rest.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseModuleDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistBillingSettingsDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistBillingSettings;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemEditableFields;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemState;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.rest.model.worklist.CourseBillingRestModel;

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
  
  public WorklistItem updateBilledPrice(WorklistItem worklistItem, Double price) {
    return worklistItemDAO.updatePrice(worklistItem, price);
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
  
  public CourseBillingRestModel getCourseBillingRestModel() {
    WorklistBillingSettings settings = getBillingSettings();
    if (settings != null) {
      CourseBillingRestModel courseBillingRestModel = null;
      try {
        ObjectMapper mapper = new ObjectMapper();
        courseBillingRestModel = mapper.readValue(settings.getSettings(), CourseBillingRestModel.class);
        return courseBillingRestModel;
      }
      catch (Exception e) {
        logger.severe(String.format("Malformatted settings document: %s", e.getMessage()));
      }
    }
    return null;
  }
  
  public Double getCourseModuleBasePrice(CourseModule courseModule, User user) {
    
    // Determine base price based on education type and course length
    
    CourseBillingRestModel courseBillingRestModel = getCourseBillingRestModel();
    if (courseBillingRestModel != null) {
      String type = courseModule.getSubject() != null && courseModule.getSubject().getEducationType() != null
          ? courseModule.getSubject().getEducationType().getName() : null;
      if (StringUtils.equalsIgnoreCase(type, PyramusConsts.SUBJECT_PERUSOPETUS)) {
        return courseBillingRestModel.getElementaryPrice();
      }
      else if (StringUtils.equalsIgnoreCase(type, PyramusConsts.SUBJECT_LUKIO)) {
        Double price = isEarliestCourseModuleOfSubjectAssessedByUser(courseModule, user)
            ? courseBillingRestModel.getHighSchoolPrice()
            : courseBillingRestModel.getHighSchoolPointPrice(); 
        Double length = 0d;
        String symbol = courseModule.getCourseLength().getUnit().getSymbol();
        if (StringUtils.equals(symbol, PyramusConsts.TIMEUNIT_OP)) {
          // pituus opintopisteinä 
          length = courseModule.getCourseLength().getUnits();
        }
        else if (StringUtils.equals(symbol, PyramusConsts.TIMEUNIT_HOURS) && courseModule.getCourseLength().getUnits() == 38) {
          // pituus opintotunteina (lukiossa ainoa vaihtoehto on 38h -> 2 op) 
          length = 2d;
        }
        else {
          // jos pituus ei ole opintopisteitä tai 38 opintotuntia, ei laskuteta
          return null;
        }
        if (length > 1) {
          price += courseBillingRestModel.getHighSchoolPointPrice() * (length - 1); 
        }
        return price;
      }
      else {
        return null;
      }
    }
    return null;
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

  public WorklistItemTemplate getTemplateForCourseAssessment() {
    Set<WorklistItemTemplateType> templateTypes = new HashSet<>();
    templateTypes.add(WorklistItemTemplateType.COURSE_ASSESSMENT);
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
  
  public List<WorklistItem> listByCourseAssessment(CourseAssessment courseAssessment) {
    return worklistItemDAO.listByCourseAssessmentAndArchived(courseAssessment, Boolean.FALSE);
  }

  public List<WorklistItem> listWorklistItemsByOwnerAndTimeframe(User owner, Date beginDate, Date endDate) {
    List<WorklistItem> worklistItems = worklistItemDAO.listByOwnerAndTimeframeAndArchived(owner, beginDate, endDate, false);
    worklistItems.sort(Comparator.comparing(WorklistItem::getEntryDate));
    return worklistItems;
  }
  
  public static Set<WorklistItemEditableFields> editableFieldsFromString(String s) {
    Set<WorklistItemEditableFields> result = new HashSet<>();
    if (!StringUtils.isEmpty(s)) {
      String[] editableFields = s.split(",");
      for (String editableField : editableFields) {
        result.add(WorklistItemEditableFields.valueOf(editableField));
      }
    }
    return result;
  }

  public static String editableFieldsToString(Set<WorklistItemEditableFields> fields) {
    if (fields == null || fields.size() == 0) {
      return null;
    }
    return String.join(",", fields.stream().map(Object::toString).collect(Collectors.toList()));
  }
  
  private boolean isEarliestCourseModuleOfSubjectAssessedByUser(CourseModule courseModule, User user) {
    CourseModuleDAO courseModuleDAO = DAOFactory.getInstance().getCourseModuleDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    
    List<CourseModule> courseModules = courseModuleDAO.listByCourseAndSubject(courseModule.getCourse(), courseModule.getSubject());
    
    // Check if course has multiple modules with the same subject as the given module
    
    if (courseModules.size() <= 1) {
      return true;
    }
    
    // Check if modules sharing the same subject have multiple assessments by the given user
    
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByUserAndCourseModules(user, courseModules);
    if (courseAssessments.size() == 0) {
      return true;
    }
    else if (courseAssessments.size() == 1) {
      return courseAssessments.get(0).getCourseModule().getId().equals(courseModule.getId());
    }

    // Check if the given module is the first one assessed by the given user
    
    courseAssessments.sort(Comparator.comparing(CourseAssessment::getDate));
    return courseAssessments.get(0).getCourseModule().getId().equals(courseModule.getId());
  }

  private WorklistBillingSettings getBillingSettings() {
    WorklistBillingSettingsDAO worklistBillingSettingsDAO = DAOFactory.getInstance().getWorklistBillingSettingsDAO();
    List<WorklistBillingSettings> billingSettings = worklistBillingSettingsDAO.listAll();
    return billingSettings.isEmpty() ? null : billingSettings.get(0); 
  }
  
  private boolean isValidStateChange(WorklistItemState oldState, WorklistItemState newState) {
    return newState.ordinal() - 1 == oldState.ordinal();
  }

}
