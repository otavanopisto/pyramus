package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BrowseMatriculationExamsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO examEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO examAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    
    List<MatriculationExam> exams = examDAO.listSorted();
    
    JSONArray jsonMatriculationExams = new JSONArray();
    for (MatriculationExam exam : exams) {
      Long totalEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.allOf(MatriculationExamEnrollmentState.class));
      Long confirmedEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.CONFIRMED));
      Long rejectedEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.REJECTED));
      Long fobEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.FILLED_ON_BEHALF));
      Long enrolledConfirmedAttendances = examAttendanceDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.CONFIRMED, MatriculationExamEnrollmentState.FILLED_ON_BEHALF), EnumSet.of(MatriculationExamAttendanceStatus.ENROLLED));
      
      JSONObject obj = new JSONObject();
      obj.put("id", exam.getId());
      obj.put("examYear", exam.getExamYear());
      obj.put("examTerm", exam.getExamTerm());
      obj.put("starts", exam.getStarts() != null ? exam.getStarts().getTime() : null);
      obj.put("ends", exam.getEnds() != null ? exam.getEnds().getTime() : null);
      obj.put("confirmationDate", exam.getConfirmationDate() != null ? exam.getConfirmationDate().getTime() : null);
      obj.put("enrollmentActive", exam.isEnrollmentActive());
      obj.put("totalEnrollments", totalEnrollments);
      obj.put("confirmedEnrollments", confirmedEnrollments);
      obj.put("rejectedEnrollments", rejectedEnrollments);
      obj.put("fobEnrollments", fobEnrollments);
      obj.put("enrolledConfirmedAttendances", enrolledConfirmedAttendances);
      jsonMatriculationExams.add(obj);
    }
    setJsDataVariable(pageRequestContext, "matriculationExams", jsonMatriculationExams.toString());

    // Collect eligible groups
    
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    
    List<StudyProgramme> eligibleStudyProgrammes = new ArrayList<>();
    List<StudentGroup> eligibleStudentGroups = new ArrayList<>();
    
    String eligibleGroupsStr = SettingUtils.getSettingValue(PyramusConsts.Matriculation.SETTING_ELIGIBLE_GROUPS);

    if (StringUtils.isNotBlank(eligibleGroupsStr)) {
      String[] split = StringUtils.split(eligibleGroupsStr, ",");

      for (String groupIdentifier : split) {
        if (groupIdentifier.startsWith("STUDYPROGRAMME:")) {
          Long studyProgrammeId = Long.parseLong(groupIdentifier.substring(15));
          StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
          if (studyProgramme != null) {
            eligibleStudyProgrammes.add(studyProgramme);
          }
        } else if (groupIdentifier.startsWith("STUDENTGROUP:")) {
          Long studentGroupId = Long.parseLong(groupIdentifier.substring(13));
          StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);
          if (studentGroup != null) {
            eligibleStudentGroups.add(studentGroup);
          }
        }
      }
    }

    eligibleStudyProgrammes.sort(Comparator.comparing(StudyProgramme::getName));
    eligibleStudentGroups.sort(Comparator.comparing(StudentGroup::getName));

    pageRequestContext.getRequest().setAttribute("eligibleStudyProgrammes", eligibleStudyProgrammes);
    pageRequestContext.getRequest().setAttribute("eligibleStudentGroups", eligibleStudentGroups);
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-browse-exams.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
