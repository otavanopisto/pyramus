package fi.otavanopisto.pyramus.domainmodel.base.search;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Binds the Person's Koski OID to Person so they can be searched on.
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
    
    // Declare the bridge that populates the field data on indexing
    context.bridge(Person.class, new PersonOIDTypeBridge(personOIDFieldRef));
  }

  private static class PersonOIDTypeBridge implements TypeBridge<Person> {

    private IndexFieldReference<String> personOIDFieldRef;

    public PersonOIDTypeBridge(IndexFieldReference<String> personOIDFieldRef) {
      this.personOIDFieldRef = personOIDFieldRef;
    }
    
    @Override
    public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
      try {
        // Objects here are not initialized as CDI beans so we have to do this
        PersonVariableDAO personVariableDAO = CDI.current().select(PersonVariableDAO.class).get();

        // Person's OID
        String koskiPersonOID = personVariableDAO.findByPersonAndKey(person, "koski.henkilo-oid");
        if (StringUtils.isNotBlank(koskiPersonOID)) {
          target.addValue(this.personOIDFieldRef, koskiPersonOID);
        }
      } catch (Exception ex) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.SEVERE, String.format("Failed to include OIDs for person %d", person != null ? person.getId() : null), ex);
      }
    }
  }

}
