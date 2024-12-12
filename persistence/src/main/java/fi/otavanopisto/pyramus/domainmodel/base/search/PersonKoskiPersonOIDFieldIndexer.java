package fi.otavanopisto.pyramus.domainmodel.base.search;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;

/**
 * FieldBridge that adds Person's Koski OID to the indexed document.
 */
public class PersonKoskiPersonOIDFieldIndexer implements FieldBridge {

  @Inject
  private PersonVariableDAO personVariableDAO;
  
  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value instanceof Person) {
      Person person = (Person) value;
      String koskiPersonOID = personVariableDAO.findByPersonAndKey(person, "koski.henkilo-oid");

      if (StringUtils.isNotBlank(koskiPersonOID)) {
        luceneOptions.addFieldToDocument(name, koskiPersonOID, document);
      }
    }
  }

}
