package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariable;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudentVariableKey;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ListCourseStudentVariablesJSONRequestController extends JSONRequestController {
  
  private static final String STUDYPROGRAMMEIDS_VARIABLE = "ooplugin.studyProgrammeIds"; 
  private static final String STUDYPROGRAMMEIDS_DEFAULT = "12,13,41,44";

  private static final String EXCLUDEDGRADEIDS_VARIABLE = "ooplugin.excludedGradeIds"; 
  private static final String EXCLUDEDGRADEIDS_DEFAULT = "26,28,18,36";

  private static final String AUTOCHECK_BILLING_VARIABLE = "ooplugin.autoCheckBillingBySubtype"; 
  private static final String AUTOCHECK_BILLING_DEFAULT = "koulukohtainensyventava";
  
  @Override
  public void process(JSONRequestContext requestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    CourseStudentVariableDAO courseStudentVariableDAO = DAOFactory.getInstance().getCourseStudentVariableDAO();
    CourseStudentVariableKeyDAO courseStudentVariableKeyDAO = DAOFactory.getInstance().getCourseStudentVariableKeyDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    
    Date startDate = requestContext.getDate("startDate");
    Date endDate = requestContext.getDate("endDate");
    boolean showChecked = requestContext.getBoolean("showChecked");

    startDate = getDateStart(startDate != null ? startDate : new Date());
    endDate = getDateEnd(endDate != null ? endDate : new Date());
    
    CourseStudentVariableKey todistusKey = courseStudentVariableKeyDAO.findByKey("todistusTehty");
    if (todistusKey == null) {
      todistusKey = courseStudentVariableKeyDAO.create(true, "todistusTehty", "Todistus tehty", VariableType.BOOLEAN);
    }
    
    CourseStudentVariableKey laskutusKey = courseStudentVariableKeyDAO.findByKey("laskutus");
    if (laskutusKey == null) {
      laskutusKey = courseStudentVariableKeyDAO.create(true, "laskutus", "Laskutuksen kohde", VariableType.TEXT);
    }
    
    String autoCheckBilling = getAutoCheckBillingVariable();
    
    List<CourseAssessment> assessments = new ArrayList<CourseAssessment>();

    for (Long studyProgrammeId : getStudyProgrammeIds()) {
      StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
      assessments.addAll(listCourseAssessmentsByStudyProgramme(studyProgramme, startDate, endDate));
    }
    
    JSONArray courseAssessmentsJSON = new JSONArray();

    List<Long> excludedGradeIds = getExcludedGradeIds();
    
    for (CourseAssessment courseAssessment : assessments) {
      if (courseAssessment.getStudent() != null && !isExcludedCredit(courseAssessment, excludedGradeIds)) {
        Student student = courseAssessment.getStudent();
        boolean hasSchool = student.getSchool() != null;
        boolean contractSchool = hasSchool ? 
            "1".equals(schoolVariableDAO.findValueBySchoolAndKey(student.getSchool(), "contractSchool")) : false;
        boolean excludedSchool = hasSchool ? 
            "1".equals(schoolVariableDAO.findValueBySchoolAndKey(student.getSchool(), "internetixBillingExcludedSchool")) : false;
            
        // Skipataan arviointi, jos käytetyn koulun kanssa on erillinen sopimus
        if (excludedSchool) {
          continue;
        }

        CourseStudentVariable todistus = courseStudentVariableDAO.findByCourseStudentAndKey(courseAssessment.getCourseStudent(), todistusKey);
        CourseStudentVariable laskutus = courseStudentVariableDAO.findByCourseStudentAndKey(courseAssessment.getCourseStudent(), laskutusKey);
        boolean todistusValue = todistus != null ? Boolean.valueOf(todistus.getValue()) : false;
        String laskutusValue = laskutus != null ? laskutus.getValue() : null;
        
        if (!todistusValue && laskutusValue == null) {
          // Jos opiskelijan koulu maksaa kaiken, oletusmaksaja on koulu
          if (student.getSchool() != null && schoolPaysEverything(schoolVariableDAO, student.getSchool())) {
            laskutusValue = "SCHOOL";
          }
          
          // Jos on koulukohtainen syventävä ja ei ole merkittyä koulua, lasku menee opiskelijalle
          if (laskutusValue == null && courseAssessment.getCourseStudent() != null) {
            Course course = courseAssessment.getCourseStudent().getCourse();
            
            if (getHasEduSubType(course, autoCheckBilling) && student.getSchool() == null)
              laskutusValue = "STUDENT";
          }

          // Jos opiskelijalla on koulu, joka ei ole sopimusoppilaitos niin oletusmaksaja on opiskelija
          if (laskutusValue == null && student.getSchool() != null && !contractSchool)
            laskutusValue = "STUDENT";
        }

        // Jos showChecked=true (näytä kuitatut), näytetään ne, joilla todistusvalue=true ja vastaavasti jos false
        if (todistusValue == showChecked) {
          JSONObject obj = new JSONObject();
  
          String schoolName = "";
          
          if (student.getSchool() != null) {
            School school = student.getSchool();
            
            if (contractSchool) {
              schoolName = "(sop) ";
            }
            
            schoolName += courseAssessment.getStudent().getSchool().getName();

            // Oppilaitos maksaa vain osan 
            boolean schoolPaysAllCourses = schoolPaysEverything(schoolVariableDAO, school);
            obj.put("schoolPaysAllCourses", schoolPaysAllCourses);
            obj.put("contractSchool", contractSchool);
          }
          
          obj.put("personId", courseAssessment.getCourseStudent().getStudent().getPerson().getId());
          obj.put("courseStudentId", courseAssessment.getCourseStudent().getId());
          obj.put("studentName", courseAssessment.getStudent().getFullName());
          obj.put("courseName", courseAssessment.getCourseStudent().getCourse().getName());
          obj.put("assessorName", courseAssessment.getAssessor().getFullName());
          obj.put("assessmentDate", courseAssessment.getDate() != null ? courseAssessment.getDate().getTime() : "");
          obj.put("schoolName", schoolName);
          obj.put("hasSchool", hasSchool);

          if (courseAssessment.getGrade() == null) {
            obj.put("grade", null);
          } else {
            obj.put("grade", courseAssessment.getGrade().getName());
          }
          
          obj.put("todistus", todistusValue);
          obj.put("laskutus", laskutusValue);
  
          courseAssessmentsJSON.add(obj);
        }
      }
    }
    
    requestContext.addResponseParameter("assessments", courseAssessmentsJSON.toString());
  }
  
  private boolean schoolPaysEverything(SchoolVariableDAO schoolVariableDAO, School school) {
    String sopimusoppilaitosValue = schoolVariableDAO.findValueBySchoolAndKey(school, "contractSchool");
    if ("1".equals(sopimusoppilaitosValue))
      return "1".equals(schoolVariableDAO.findValueBySchoolAndKey(school, "sopimusMaksKaikki"));
    
    return false;
  }

  private boolean getHasEduSubType(Course course, String subtypecode) {
    for (CourseEducationType courseEducationType : course.getCourseEducationTypes()) {
      for (CourseEducationSubtype courseEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
        if (courseEducationSubtype.getEducationSubtype() != null) {
          if (subtypecode.equals(courseEducationSubtype.getEducationSubtype().getCode()))
            return true;
        }
      }
    }
    return false;
  }

  private boolean isExcludedCredit(CourseAssessment c, List<Long> excludedGradeIds) {
    return c == null || c.getGrade() == null || excludedGradeIds.contains(c.getGrade().getId());
  }

  private Date getDateEnd(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTime();
  }

  private Date getDateStart(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  private List<CourseAssessment> listCourseAssessmentsByStudyProgramme(StudyProgramme sp, Date startDate, Date endDate) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    EntityManager entityManager = systemDAO.getEntityManager();
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> courseAssessment = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudent = courseAssessment.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Student> student = courseStudent.join(CourseStudent_.student);
    
    criteria.select(courseAssessment);

    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.greaterThanOrEqualTo(courseAssessment.get(CourseAssessment_.date), startDate),
            criteriaBuilder.lessThanOrEqualTo(courseAssessment.get(CourseAssessment_.date), endDate),
            criteriaBuilder.equal(courseAssessment.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.isNotNull(courseAssessment.get(CourseAssessment_.grade)),
            criteriaBuilder.equal(student.get(Student_.studyProgramme), sp),
            criteriaBuilder.equal(student.get(Student_.archived), Boolean.FALSE)
        )
    );
    criteria.orderBy(criteriaBuilder.desc(courseAssessment.get(CourseAssessment_.date)));
    TypedQuery<CourseAssessment> query = entityManager.createQuery(criteria);
    
//    query.setMaxResults(100);
    
    return query.getResultList();
  }
  
  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
  private String getAutoCheckBillingVariable() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(AUTOCHECK_BILLING_VARIABLE);
    if (settingKey == null) {
      settingKey = settingKeyDAO.create(AUTOCHECK_BILLING_VARIABLE);
    }
    
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting == null) {
      setting = settingDAO.create(settingKey, AUTOCHECK_BILLING_DEFAULT);
    }
    
    return setting.getValue();
  }
  
  private List<Long> getExcludedGradeIds() {
    List<Long> result = new ArrayList<Long>();
    
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(EXCLUDEDGRADEIDS_VARIABLE);
    if (settingKey == null) {
      settingKey = settingKeyDAO.create(EXCLUDEDGRADEIDS_VARIABLE);
    }
    
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting == null) {
      setting = settingDAO.create(settingKey, EXCLUDEDGRADEIDS_DEFAULT);
    }
    
    String[] values = StringUtils.split(setting.getValue(), ",");
    for (String value : values) {
      Long excludedGradeId = NumberUtils.createLong(value);
      if (excludedGradeId != null) {
        result.add(excludedGradeId);
      }
    }
    
    return result;
  }
  
  private List<Long> getStudyProgrammeIds() {
    List<Long> result = new ArrayList<Long>();
    
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    
    SettingKey settingKey = settingKeyDAO.findByName(STUDYPROGRAMMEIDS_VARIABLE);
    if (settingKey == null) {
      settingKey = settingKeyDAO.create(STUDYPROGRAMMEIDS_VARIABLE);
    }
    
    Setting setting = settingDAO.findByKey(settingKey);
    if (setting == null) {
      setting = settingDAO.create(settingKey, STUDYPROGRAMMEIDS_DEFAULT);
    }
    
    String[] values = StringUtils.split(setting.getValue(), ",");
    for (String value : values) {
      Long studyProgrammeId = NumberUtils.createLong(value);
      if (studyProgrammeId != null) {
        result.add(studyProgrammeId);
      }
    }
    
    return result;
  }

}
