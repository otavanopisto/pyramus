package fi.otavanopisto.pyramus.json.students;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.model.StudentCardActivity;
import fi.otavanopisto.pyramus.rest.model.StudentCardType;

@WebServlet("/studentCard")
public class StudentCardServlet extends HttpServlet {


  private static final long serialVersionUID = -7146705572564666750L;

  @Inject
  private Logger logger;
  
  @Inject
  private StudentCardDAO studentCardDAO;
  
  @Inject
  private StudentController studentController;

  public StudentCardServlet() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String sliceAuth = SettingUtils.getSettingValue("slice.auth");
    
    if (!StringUtils.equals(authHeader, sliceAuth)) {
      logger.log(Level.WARNING, "Could not authorize");
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    
    List<StudentCard> studentCards = studentCardDAO.listStudentCardsByExpiryDateInFuture();
    
    List<fi.otavanopisto.pyramus.rest.model.StudentCard> studentCardList = new ArrayList<fi.otavanopisto.pyramus.rest.model.StudentCard>();
    
    
    for (StudentCard studentCard : studentCards) {
      // Student cards with activity "cancelled" need still to be listed two weeks after cancellation
      if (studentCard.getActivity().equals(fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity.CANCELLED)) {
       // get Calendar instance
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
  
        // substract 14 days
        // If we give 14 there it will give 15 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-13);
  
        // convert to date
        Date twoWeeksBefore = cal.getTime();
        
        if (studentCard.getCancellationDate().before(twoWeeksBefore)) {
          continue;
        }
      }
      
      fi.otavanopisto.pyramus.rest.model.StudentCard restModel = new fi.otavanopisto.pyramus.rest.model.StudentCard();
      
      Student student = studentController.findStudentById(studentCard.getStudent().getId());
      
      restModel.setId(studentCard.getId());
      restModel.setUserEntityId(studentCard.getStudent().getId());
      restModel.setActivity(StudentCardActivity.valueOf(studentCard.getActivity().name()));
      restModel.setExpiryDate(studentCard.getExpiryDate());
      restModel.setFirstName(student.getFirstName());
      restModel.setLastName(student.getLastName());
      restModel.setType(StudentCardType.valueOf(studentCard.getType().name()));
      restModel.setStudyProgramme(student.getStudyProgramme().getName());
       
      studentCardList.add(restModel);
    }
    
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(writer, studentCardList);
    
    String requestStr = writer.toString();
    
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    out.print(requestStr);
    out.flush();
  }
}
