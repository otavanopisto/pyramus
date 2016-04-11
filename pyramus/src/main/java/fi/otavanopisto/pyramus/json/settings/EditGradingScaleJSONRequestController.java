package fi.otavanopisto.pyramus.json.settings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditGradingScaleJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();

    Long gradingScaleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("gradingScaleId"));
    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    
    GradingScale gradingScale = gradingScaleDAO.findById(gradingScaleId);
    gradingScaleDAO.update(gradingScale, name, description);
    
    Set<Long> existingGrades = new HashSet<>();
    
    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("gradesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "gradesTable." + i;
      
      String gradeIdParam = jsonRequestContext.getRequest().getParameter(colPrefix + ".gradeId");
      Long gradeId = StringUtils.isBlank(gradeIdParam) ? null : NumberUtils.createLong(gradeIdParam);
      String gradeName = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String gradeQualification = jsonRequestContext.getRequest().getParameter(colPrefix + ".qualification");
      String gradeGPAParam = jsonRequestContext.getRequest().getParameter(colPrefix + ".GPA");
      String gradeDescription = jsonRequestContext.getRequest().getParameter(colPrefix + ".description");
      Boolean passingGrade = "1".equals(jsonRequestContext.getRequest().getParameter(colPrefix + ".passingGrade"));
      Double gradeGPA = StringUtils.isBlank(gradeGPAParam) ? null : NumberUtils.createDouble(gradeGPAParam);
      
      if (gradeId != null) {
        Grade grade = gradeDAO.findById(gradeId);
        gradeDAO.update(grade, gradeName, gradeDescription, passingGrade, gradeGPA, gradeQualification);
        existingGrades.add(grade.getId());
      } else {
        Grade grade = gradeDAO.create(gradingScale, gradeName, gradeDescription, passingGrade, gradeGPA, gradeQualification);
        existingGrades.add(grade.getId());
      }
    }
    
    List<Grade> grades = gradingScale.getGrades();
    for (int i = grades.size() - 1; i >= 0; i--) {
      Grade grade = grades.get(i);
      if (!existingGrades.contains(grade.getId()))
        gradingScale.removeGrade(grade);
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
