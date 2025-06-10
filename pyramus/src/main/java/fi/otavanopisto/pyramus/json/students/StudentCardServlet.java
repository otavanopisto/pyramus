package fi.otavanopisto.pyramus.json.students;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

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
    String sliceAuth = SettingUtils.getSettingValue(SliceController.SLICEAUTH_SETTINGKEY);
    sliceAuth = StringUtils.isNotBlank(sliceAuth) ? "Basic " + sliceAuth : null;

    if (StringUtils.isBlank(sliceAuth) || !StringUtils.equals(authHeader, sliceAuth)) {
      logger.log(Level.WARNING, "Could not authorize");
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    
    logger.info("Fetching student card list.");

    List<SliceStudentCardRestModel> studentCardList = sliceController.listStudentCards();
    
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    mapper.writeValue(out, studentCardList);
  }
}
