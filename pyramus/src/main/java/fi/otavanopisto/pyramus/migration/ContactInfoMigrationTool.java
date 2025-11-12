package fi.otavanopisto.pyramus.migration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.math.NumberUtils;

import fi.otavanopisto.pyramus.dao.base.UserAdditionalContactInfoDAO;
import fi.otavanopisto.pyramus.dao.migration.ContactInfoMigrationUserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.UserAdditionalContactInfo;
import fi.otavanopisto.pyramus.domainmodel.migration.ContactInfoMigrationUser;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN) 
public class ContactInfoMigrationTool {

  private final static int BATCHSIZE = 10;
  
  @Inject
  private Logger logger;

  @Resource
  private UserTransaction userTransaction;
  
  @Inject
  private ContactInfoMigrationUserDAO migrationDAO;
  
  @Inject
  private UserAdditionalContactInfoDAO additionalContactInfoDAO;
  
  @PostConstruct
  public void contactInfoMigration() {
    try {
      logger.info("Migrating ContactInfos");
      
      int migratedCount = 0;
      
      do {
        migratedCount = executeBatch();
        
        if (migratedCount > 0) {
          logger.info(String.format("Migrated %d contactinfos", migratedCount));
        }
      } while (migratedCount > 0);
      
      logger.info("ContactInfo Migration Done!");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "CONTACT INFO MIGRATION FAILED", e);
    }
  }

  /**
   * Runs a batch of ContactInfo migrations within it's own transaction.
   * 
   * @return
   * @throws Exception
   */
  private int executeBatch() throws Exception {
    userTransaction.begin();
    try {
      List<ContactInfoMigrationUser> batch = migrationDAO.listBatch(BATCHSIZE);
      int migrationCount = batch.size();
      
      for (ContactInfoMigrationUser contactInfoMigrationUser : batch) {
        User user = contactInfoMigrationUser.getUser();
        
        ContactInfo contactInfo = user.getContactInfo();
        
        // Lists of addresses, phonenumbers and emails which are non-unique by type - we do nothing to the unique type contacts, they stay put
        List<Address> addresses = contactInfo.getAddresses().stream()
            .filter(address -> address.getContactType() == null || Boolean.TRUE.equals(address.getContactType().getNonUnique()))
            .toList();
        List<PhoneNumber> phonenumbers = contactInfo.getPhoneNumbers().stream()
            .filter(phone -> phone.getContactType() == null || Boolean.TRUE.equals(phone.getContactType().getNonUnique()))
            .toList();
        List<Email> emails = contactInfo.getEmails().stream()
            .filter(email -> email.getContactType() == null || Boolean.TRUE.equals(email.getContactType().getNonUnique()))
            .toList();

        // Number of new ContactInfos
        int newContactInfoCount = NumberUtils.max(addresses.size(), phonenumbers.size(), emails.size());
        
        if (newContactInfoCount > 0) {
          for (int i = 0; i < newContactInfoCount; i++) {
            UserAdditionalContactInfo newContactInfo = null;
            
            // Email
            if (emails.size() - 1 >= i) {
              Email email = emails.get(i);

              if (email != null) {
                if (newContactInfo == null) {
                  newContactInfo = additionalContactInfoDAO.create(email.getContactType(), false);
                }
                
                newContactInfo.addEmail(migrationDAO.setAsDefault(email));
              }
            }
            
            // Address & name
            if (addresses.size() - 1 >= i) {
              Address address = addresses.get(i);

              if (address != null) {
                if (newContactInfo == null) {
                  newContactInfo = additionalContactInfoDAO.create(address.getContactType(), false);
                }
                
                newContactInfo.addAddress(migrationDAO.setAsDefault(address));
              }
            }
            
            // Phone number
            if (phonenumbers.size() - 1 >= i) {
              PhoneNumber phoneNumber = phonenumbers.get(i);

              if (phoneNumber != null) {
                if (newContactInfo == null) {
                  newContactInfo = additionalContactInfoDAO.create(phoneNumber.getContactType(), false);
                }

                newContactInfo.addPhoneNumber(migrationDAO.setAsDefault(phoneNumber));
              }
            }
            
            if (newContactInfo != null) {
              user.getAdditionalContactInfos().add(newContactInfo);
            }
          }
          
          migrationDAO.update(user);
        }
        else {
          logger.warning(String.format("Student (%d) didn't have any contacts to migrate.", user.getId()));
        }
        
        migrationDAO.delete(contactInfoMigrationUser);
      } // end of batch
      
      userTransaction.commit();
      return migrationCount;
    }
    catch (Exception e) {
      userTransaction.rollback();
      throw e;
    }
  }
  
}
