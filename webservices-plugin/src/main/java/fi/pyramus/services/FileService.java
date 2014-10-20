package fi.pyramus.services;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;

import org.apache.commons.io.IOUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.file.FileType;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;

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
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
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
    
    studentFileDAO.create(student, name, fileName, fileType, contentType, data, creator);
  }
  
}
