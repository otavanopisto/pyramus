package fi.pyramus.services;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;

import org.apache.commons.io.IOUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@WebService
@BindingType(value="http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")
@RolesAllowed("WebServices")
@MTOM (enabled = true)
public class FileService extends PyramusService {

  @WebMethod
  public void uploadStudentFile(
      @WebParam (name="studentId") Long studentId, 
      @WebParam (name="name") String name, 
      @WebParam (name="fileName") String fileName, 
      @WebParam (name="fileTypeId") Long fileTypeId, 
      @WebParam (name="contentType") String contentType, 
      @WebParam (name="creatorId") Long creatorId, 
      @WebParam (name="content") DataHandler content) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    
    User creator = creatorId != null ? userDAO.findById(creatorId) : null;
    
    FileType fileType = fileTypeId != null ? fileTypeDAO.findById(fileTypeId) : null;
    
    byte[] data = null;
    
    try {
      InputStream inputStream = content.getInputStream();
      
      data = IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    studentFileDAO.create(student, name, fileName, null, fileType, contentType, data, creator);
  }
  
}
