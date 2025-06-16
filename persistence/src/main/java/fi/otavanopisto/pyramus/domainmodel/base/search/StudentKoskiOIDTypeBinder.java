package fi.otavanopisto.pyramus.domainmodel.base.search;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Binds the Koski Study Permit OIDs to Student so they can be searched on.
 */
public class StudentKoskiOIDTypeBinder implements TypeBinder {

  @Override
  public void bind(TypeBindingContext context) {
    // No dependencies to Person fields
    context.dependencies().useRootOnly();

    // Declare field for Student OIDs
    IndexFieldReference<String> studentOIDsFieldRef = context.indexSchemaElement()
        .field("koskiStudentOIDs", f -> f.asString())
        .multiValued()
        .toReference();
    
    // Declare the bridge that populates the field data on indexing
    context.bridge(Student.class, new StudentStudyPermitOIDTypeBridge(studentOIDsFieldRef));
  }

  private static class StudentStudyPermitOIDTypeBridge implements TypeBridge<Student> {

    private IndexFieldReference<String> studyPermitOIDFieldRef;

    public StudentStudyPermitOIDTypeBridge(IndexFieldReference<String> studyPermitOIDFieldRef) {
      this.studyPermitOIDFieldRef = studyPermitOIDFieldRef;
    }
    
    @Override
    public void write(DocumentElement target, Student student, TypeBridgeWriteContext context) {
      try {
        // Objects here are not initialized as CDI beans so we have to do this
        UserVariableDAO userVariableDAO = CDI.current().select(UserVariableDAO.class).get();

        // Study Permit OIDs
        
        // The value is straight up OID
        String koskiStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.studypermission-id");
        if (StringUtils.isNotBlank(koskiStudyPermissionOID)) {
          target.addValue(this.studyPermitOIDFieldRef, koskiStudyPermissionOID);
        }

        // Complex JSON type with potentially multiple OIDs
        String koskiInternetixStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.internetix-studypermission-id");
        if (StringUtils.isNotBlank(koskiInternetixStudyPermissionOID)) {
          ObjectMapper mapper = new ObjectMapper();
          TypeReference<List<KoskiStudentId>> typeRef = new TypeReference<List<KoskiStudentId>>() {};

          List<KoskiStudentId> ids = mapper.readValue(koskiInternetixStudyPermissionOID, typeRef);
          if (CollectionUtils.isNotEmpty(ids)) {
            for (KoskiStudentId id : ids) {
              target.addValue(this.studyPermitOIDFieldRef, id.getOid());
            }
          }
        }
      } catch (Exception ex) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.SEVERE, String.format("Failed to include OIDs for student %d", student != null ? student.getId() : null), ex);
      }
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  static class KoskiStudentId {
    
    public KoskiStudentId() {
    }
    
    public String getOid() {
      return oid;
    }

    public void setOid(String oid) {
      this.oid = oid;
    }
    
    public String getStudentIdentifier() {
      return studentIdentifier;
    }

    public void setStudentIdentifier(String studentIdentifier) {
      this.studentIdentifier = studentIdentifier;
    }

    private String oid;
    private String studentIdentifier;
  }

}
