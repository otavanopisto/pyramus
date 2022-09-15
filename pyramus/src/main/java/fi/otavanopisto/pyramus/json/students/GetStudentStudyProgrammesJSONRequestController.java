package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller to view student info.
 * 
 * @author antti.viljakainen
 */
public class GetStudentStudyProgrammesJSONRequestController extends JSONRequestController {
  
  /**
   * Processes JSON request
   * 
   * In parameters
   * - studentId - student id to retrieve information for
   * 
   * Page parameters
   * - student - Map including
   * * id - Student id
   * * firstname - First name
   * * lastname - Last name
   * 
   * @param requestContext JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    Long personId = NumberUtils.createLong(requestContext.getRequest().getParameter("personId"));
    Person person = personDAO.findById(personId);
    List<Student> students = studentDAO.listByPerson(person);
    Collections.sort(students, new Comparator<Student>() {
      @Override
      public int compare(Student o1, Student o2) {
        /**
         * Ordering study programmes as follows
         *  1. studies that have start date but no end date (ongoing)
         *  2. studies that have no start nor end date
         *  3. studies that have ended
         *  4. studies that are archived
         *  5. other
         */
        
        int o1class =
          (o1.getArchived()) ? 4:
            (o1.getStudyStartDate() != null && o1.getStudyEndDate() == null) ? 1:
              (o1.getStudyStartDate() == null && o1.getStudyEndDate() == null) ? 2:
                (o1.getStudyEndDate() != null) ? 3:
                  5;
        int o2class =
          (o2.getArchived()) ? 4:
            (o2.getStudyStartDate() != null && o2.getStudyEndDate() == null) ? 1:
              (o2.getStudyStartDate() == null && o2.getStudyEndDate() == null) ? 2:
                (o2.getStudyEndDate() != null) ? 3:
                  5;

        if (o1class == o2class) {
          // classes are the same, we try to do last comparison from the start dates
          return ((o1.getStudyStartDate() != null) && (o2.getStudyStartDate() != null)) ? 
              o2.getStudyStartDate().compareTo(o1.getStudyStartDate()) : 0; 
        } else
          return o1class < o2class ? -1 : o1class == o2class ? 0 : 1;
      }
    });
    
    List<Map<String, Object>> result = new ArrayList<>();
    
    for (int i = 0; i < students.size(); i++) {
    	Student student = students.get(i);
    	
      if (student != null) {
    		if (student.getStudyProgramme() != null) {
          Map<String, Object> studentInfo = new HashMap<>();
          studentInfo.put("studyProgrammeId", student.getStudyProgramme().getId());

          String studyProgrammeName = student.getStudyProgramme().getName();
          if (student.getStudyEndDate() != null) {
            studyProgrammeName += " *";
          }

          studentInfo.put("studyProgrammeName", studyProgrammeName);
          studentInfo.put("studentId", student.getId());

          result.add(studentInfo);
    		} else {
          Map<String, Object> studentInfo = new HashMap<>();
          studentInfo.put("studyProgrammeId", Long.valueOf(-1));
          if (!student.getArchived())
            studentInfo.put("studyProgrammeName", Messages.getInstance().getText(requestContext.getRequest().getLocale(), "students.editStudent.noStudyProgrammeDropDownItemLabel"));
          else
          	studentInfo.put("studyProgrammeName", Messages.getInstance().getText(requestContext.getRequest().getLocale(),"students.editStudent.noStudyProgrammeDropDownItemLabel") + " *");
          studentInfo.put("studentId", student.getId());

          result.add(studentInfo);
    		}
    	}
    }
    
    requestContext.addResponseParameter("studentStudyProgrammes", result);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
