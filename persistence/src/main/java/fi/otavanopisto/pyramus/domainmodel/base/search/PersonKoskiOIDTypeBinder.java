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

import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Binds the Koski OIDs to Person so they can be searched on.
 */
public class PersonKoskiOIDTypeBinder implements TypeBinder {

  @Override
  public void bind(TypeBindingContext context) {
    // No dependencies to Person fields
    context.dependencies().useRootOnly();

    // Declare field for Person OID
    IndexFieldReference<String> personOIDFieldRef = context.indexSchemaElement()
        .field("koskiPersonOID", f -> f.asString())
        .toReference();
    
    // Declare field for Student OIDs
    IndexFieldReference<String> studentOIDsFieldRef = context.indexSchemaElement()
        .field("koskiStudentOIDs", f -> f.asString())
        .multiValued()
        .toReference();
    
    // Declare the bridge that populates the field data on indexing
    context.bridge(Person.class, new PersonOIDTypeBridge(personOIDFieldRef, studentOIDsFieldRef));
  }

  private static class PersonOIDTypeBridge implements TypeBridge<Person> {

    private IndexFieldReference<String> personOIDFieldRef;
    private IndexFieldReference<String> studyPermissionOIDFieldRef;

    public PersonOIDTypeBridge(IndexFieldReference<String> personOIDFieldRef, IndexFieldReference<String> studyPermissionOIDFieldRef) {
      this.personOIDFieldRef = personOIDFieldRef;
      this.studyPermissionOIDFieldRef = studyPermissionOIDFieldRef;
    }
    
    @Override
    public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
      try {
        // Objects here are not initialized as CDI beans so we have to do this
        PersonVariableDAO personVariableDAO = CDI.current().select(PersonVariableDAO.class).get();
        UserVariableDAO userVariableDAO = CDI.current().select(UserVariableDAO.class).get();

        // Person's OID
        
        String koskiPersonOID = personVariableDAO.findByPersonAndKey(person, "koski.henkilo-oid");
        if (StringUtils.isNotBlank(koskiPersonOID)) {
          target.addValue(this.personOIDFieldRef, koskiPersonOID);
        }
        
        // Study Permit OIDs
        
        for (Student student : person.getStudents()) {
          // The value is straight up OID
          String koskiStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.studypermission-id");
          if (StringUtils.isNotBlank(koskiStudyPermissionOID)) {
            target.addValue(this.studyPermissionOIDFieldRef, koskiStudyPermissionOID);
          }
  
          // Complex JSON type with potentially multiple OIDs
          String koskiInternetixStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.internetix-studypermission-id");
          if (StringUtils.isNotBlank(koskiInternetixStudyPermissionOID)) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<KoskiStudentId>> typeRef = new TypeReference<List<KoskiStudentId>>() {};
  
            List<KoskiStudentId> ids = mapper.readValue(koskiInternetixStudyPermissionOID, typeRef);
            if (CollectionUtils.isNotEmpty(ids)) {
              for (KoskiStudentId id : ids) {
                target.addValue(this.studyPermissionOIDFieldRef, id.getOid());
              }
            }
          }
        }
      } catch (Exception ex) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.SEVERE, String.format("Failed to include OIDs for person %d", person != null ? person.getId() : null), ex);
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
