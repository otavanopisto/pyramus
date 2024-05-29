package fi.otavanopisto.pyramus.json.students;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.rest.controller.SliceController;
import fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel;

@Transactional
@WebServlet("/api/slice")
public class StudentCardServlet extends HttpServlet {


  private static final long serialVersionUID = -7146705572564666750L;

  @Inject
  private Logger logger;
  
  @Inject
  private SliceController sliceController;
  
  
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
    
    List<SliceStudentCardRestModel> studentCardList = sliceController.listStudentCards();
    
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(out, studentCardList);
  }
}
