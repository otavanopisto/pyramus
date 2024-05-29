package fi.otavanopisto.pyramus.binary.students;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.rest.controller.SliceController;
import fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel;

public class SliceBinaryRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(SliceBinaryRequestController.class.getName());
  
  public void process(BinaryRequestContext requestContext) {
    try {
      
      SliceController sliceController = CDI.current().select(SliceController.class).get();

      List<SliceStudentCardRestModel> studentCardList = sliceController.listStudentCards();
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      String requestStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(studentCardList);
      
      requestContext.setResponseContent(requestStr.getBytes("UTF-8"), "application/json;charset=UTF-8");
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading person variables", e);
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER, UserRole.STUDY_GUIDER, UserRole.TEACHER };
  }

}
