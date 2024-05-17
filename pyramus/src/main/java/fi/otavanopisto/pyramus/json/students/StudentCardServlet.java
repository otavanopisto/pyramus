package fi.otavanopisto.pyramus.json.students;

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.model.StudentCardType;

@Transactional
@WebServlet("/api/slice")
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
    
    if (StringUtils.isBlank(sliceAuth) || !StringUtils.equals(authHeader, sliceAuth)) {
      logger.log(Level.WARNING, "Could not authorize");
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    
    Calendar cal = Calendar.getInstance();
    
    // substract 14 days
    cal.add(Calendar.DATE, -14);

    // convert to date
    Date twoWeeksBefore = cal.getTime();
    
    // List active & cancelled student cards where expiration date is within the past two weeks or in the future
    List<StudentCard> studentCards = studentCardDAO.listStudentCardsByExpiryDateAndActive(twoWeeksBefore);
    
    List<fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel> studentCardList = new ArrayList<fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel>();

    for (StudentCard studentCard : studentCards) {
      // Student cards with activity "cancelled" need still to be listed two weeks after cancellation
      if (studentCard.getActivity().equals(fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity.CANCELLED)) {
        if (studentCard.getCancellationDate().before(twoWeeksBefore)) {
          continue;
        }
      }
      
      fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel restModel = new fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel();
      
      Student student = studentController.findStudentById(studentCard.getStudent().getId());

      Email email = student.getContactInfo().getEmails().stream().filter(e -> Boolean.TRUE.equals(e.getDefaultAddress())).findFirst().orElse(null);
      
      restModel.setFirstName(student.getFirstName());
      restModel.setLastName(student.getLastName());
      restModel.setBirthday(student.getPerson().getBirthday());
      restModel.setEmail(email != null ? email.getAddress() : null);
      restModel.setType(StudentCardType.valueOf(studentCard.getType().name()));
      
      if (studentCard.getCancellationDate() != null && studentCard.getActivity().equals(StudentCardActivity.CANCELLED)) {
        if (studentCard.getCancellationDate().before(studentCard.getExpiryDate())) {
          restModel.setExpiryDate(studentCard.getCancellationDate());
        }
      } else {
        restModel.setExpiryDate(studentCard.getExpiryDate());
      }
       
      studentCardList.add(restModel);
    }
    
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(out, studentCardList);
    
    String requestStr = out.toString();

    out.print(requestStr);
    out.flush();
  }
}
