package fi.otavanopisto.pyramus.domainmodel.base.search;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

/**
 * FieldBridge that adds all Person's Students' Koski
 * Study Permit OIDs to the indexed document.
 */
public class PersonKoskiStudentOIDFieldIndexer implements FieldBridge {

  @Inject
  private UserVariableDAO userVariableDAO;
  
  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value instanceof Person) {
      Person person = (Person) value;
      
      for (Student student : person.getStudents()) {
        // The value is straight up OID
        String koskiStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.studypermission-id");
        if (StringUtils.isNotBlank(koskiStudyPermissionOID)) {
          document.add(new StringField(name, koskiStudyPermissionOID, luceneOptions.getStore()));
        }

        // Complex JSON type with potentially multiple OIDs
        String koskiInternetixStudyPermissionOID = userVariableDAO.findByUserAndKey(student, "koski.internetix-studypermission-id");
        if (StringUtils.isNotBlank(koskiInternetixStudyPermissionOID)) {
          ObjectMapper mapper = new ObjectMapper();
          TypeReference<List<KoskiStudentId>> typeRef = new TypeReference<List<KoskiStudentId>>() {};
          try {
            List<KoskiStudentId> ids = mapper.readValue(koskiInternetixStudyPermissionOID, typeRef);
            if (CollectionUtils.isNotEmpty(ids)) {
              for (KoskiStudentId id : ids) {
                document.add(new StringField(name, id.getOid(), luceneOptions.getStore()));
              }
            }
          } catch (Exception ex) {
          }
        }
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
