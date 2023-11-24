package fi.otavanopisto.pyramus.framework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public class PyramusFileUtils {
  
  public static boolean isFileSystemStorageEnabled() {
    return SettingUtils.getSettingValue("files.storageFolder") != null;    
  }

  public static Long getLastMovedEntityId() {
    String lastEntityId = SettingUtils.getSettingValue("files.lastMovedEntityId");
    return lastEntityId == null || !NumberUtils.isNumber(lastEntityId) ? 0L : Long.valueOf(lastEntityId);
  }
  
  public static void setLastMovedEntityId(Long id) {
    SettingUtils.setSettingValue("files.lastMovedEntityId", id.toString());
  }

  public static File getStorageFolder(User user) throws IOException {
    String storageFolder = SettingUtils.getSettingValue("files.storageFolder");
    if (StringUtils.isBlank(storageFolder)) {
      throw new IOException("files.storageFolder not set");
    }
    File userFolder = Paths.get(storageFolder, user.getId().toString()).toFile();
    if (!userFolder.exists()) {
      userFolder.mkdir();
      if (!userFolder.exists()) {
        throw new IOException(String.format("Cannot create user folder %s", userFolder.getPath()));
      }
    }
    return userFolder;
  }
  
  public static int relocateToFileSystem(StudentFile studentFile) throws IOException {
    int bytes = 0;
    if (studentFile.getData() != null) {
      bytes = studentFile.getData().length;
      String fileId = generateFileId();
      User user = studentFile.getStudent();
      storeFile(user, fileId, studentFile.getData());
      StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
      studentFileDAO.updateData(studentFile, studentFile.getContentType(), fileId, null, studentFile.getLastModifier());
    }
    return bytes;
  }
  
  public static byte[] getFileData(StudentFile studentFile) throws IOException {
    if (studentFile.getFileId() == null) {
      return studentFile.getData();
    }
    else {
      File storageFolder = getStorageFolder(studentFile.getStudent());
      java.nio.file.Path path = Paths.get(storageFolder.getPath(), studentFile.getFileId());
      File file = path.toFile();
      return file.exists() ? FileUtils.readFileToByteArray(file) : null; 
    }
  }
  
  public static boolean moveFileForNewStudent(StudentFile studentFile, Student newStudent, StaffMember modifier) throws IOException {
    if (studentFile.getStudent() == null || newStudent == null || !studentFile.getStudent().getPersonId().equals(newStudent.getPersonId())) {
      throw new IllegalArgumentException("Cannot move file under another person.");
    }

    if (studentFile.getFileId() == null) {
      return true;
    }
    else {
      File oldFolder = getStorageFolder(studentFile.getStudent());
      java.nio.file.Path oldFilePath = Paths.get(oldFolder.getPath(), studentFile.getFileId());
      File oldFile = oldFilePath.toFile();
      
      if (oldFilePath.toFile().exists()) {
        File newFolder = getStorageFolder(newStudent);
        
        FileUtils.moveFileToDirectory(oldFile, newFolder, true);
        
        StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
        studentFileDAO.updateStudent(studentFile, newStudent, modifier);
        return true;
      }
      else {
        // Moving the file failed, old file was not found
        return false;
      }
    }
  }
  
  /**
   * Deletes a StudentFile
   * 
   * @param studentFile the StudentFile to be deleted
   * @return true if all went smoothly
   * @throws IOException if something goes wrong
   */
  public static boolean deleteFile(StudentFile studentFile) throws IOException {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();

    if (studentFile.getFileId() == null) {
      // File data is in the entity (old)
      studentFileDAO.delete(studentFile);
      return true;
    }
    else {
      // File data is on disk
      File storageFolder = getStorageFolder(studentFile.getStudent());
      java.nio.file.Path path = Paths.get(storageFolder.getPath(), studentFile.getFileId());
      File file = path.toFile();
      if (file.exists()) {
        // Throws an exception if deletion fails
        FileUtils.delete(file);
        studentFileDAO.delete(studentFile);
        return true;
      }
      
      // ?? File didn't exist
      return false; 
    }
  }
  
  public static void storeFile(User user, String fileId, byte[] data) throws IOException {
    File storageFolder = getStorageFolder(user);
    File userFile = Paths.get(storageFolder.getPath(), fileId).toFile();
    FileUtils.writeByteArrayToFile(userFile, data);
  }

  public static String generateFileId() {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    String s = UUID.randomUUID().toString();
    while (studentFileDAO.findByFileId(s) != null) {
      s = UUID.randomUUID().toString();
    }
    return s;
  }

}
