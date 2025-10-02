package fi.otavanopisto.pyramus.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.TypedContactInfo;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentAdditionalContactInfo;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ContactInfoUtils {

  /**
   * Companion of front end contact info editor. 
   * Reads and updates the list of contactInfos.
   * 
   * @param requestContext RequestContext used to read the parameters from front end contact info editor
   * @param contactInfoEditorName the parameter name used for the front end contact info editor
   * @param contactInfos the list of current contact infos to update
   */
  public static void readAndUpdateStudentAdditionalContactInfos(RequestContext requestContext, String contactInfoEditorName, List<StudentAdditionalContactInfo> contactInfos) {
    readAndUpdateTypedContactInfos(requestContext, contactInfoEditorName, contactInfos, new ContactInfoConstructor<StudentAdditionalContactInfo>() {
      @Override
      public StudentAdditionalContactInfo create(ContactType contactType) {
        return DAOFactory.getInstance().getStudentAdditionalContactInfoDAO().create(contactType, false);
      }

      @Override
      public StudentAdditionalContactInfo update(StudentAdditionalContactInfo contactInfo, ContactType contactType) {
        return DAOFactory.getInstance().getStudentAdditionalContactInfoDAO().update(contactInfo, contactType, contactInfo.isAllowStudyDiscussions());
      }
    });
  }
  
  /**
   * Companion of front end contact info editor. 
   * Reads and updates the list of contactInfos.
   * 
   * @param requestContext RequestContext used to read the parameters from front end contact info editor
   * @param contactInfoEditorName the parameter name used for the front end contact info editor
   * @param contactInfos the list of current contact infos to update
   */
  public static void readAndUpdateTypedContactInfos(RequestContext requestContext, String contactInfoEditorName, List<TypedContactInfo> contactInfos) {
    readAndUpdateTypedContactInfos(requestContext, contactInfoEditorName, contactInfos, new ContactInfoConstructor<TypedContactInfo>() {
      @Override
      public TypedContactInfo create(ContactType contactType) {
        return DAOFactory.getInstance().getTypedContactInfoDAO().create(contactType);
      }

      @Override
      public TypedContactInfo update(TypedContactInfo contactInfo, ContactType contactType) {
        return DAOFactory.getInstance().getTypedContactInfoDAO().update(contactInfo, contactType);
      }
    });
  }
  
  /**
   * Companion of front end contact info editor. 
   * Reads and updates the list of contactInfos.
   * 
   * @param requestContext RequestContext used to read the parameters from front end contact info editor
   * @param contactInfoEditorName the parameter name used for the front end contact info editor
   * @param contactInfos the list of current contact infos to update
   * @return 
   */
  private static <E extends TypedContactInfo> void readAndUpdateTypedContactInfos(RequestContext requestContext, String contactInfoEditorName, List<E> contactInfos, ContactInfoConstructor<E> dao) {
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    
    int contactInfoCount = requestContext.getInteger(String.format("%s.rowCount", contactInfoEditorName));
    
    for (int contactInfoIndex = 0; contactInfoIndex < contactInfoCount; contactInfoIndex++) {
      Long contactInfoContactTypeId = requestContext.getLong(String.format("%s.%d.contactTypeId", contactInfoEditorName, contactInfoIndex));
      ContactType contactInfoContactType = contactTypeDAO.findById(contactInfoContactTypeId);

      E contactInfo;
      Long contactInfoId = requestContext.getLong(String.format("%s.%d.id", contactInfoEditorName, contactInfoIndex));
      if (Long.valueOf(-1L).equals(contactInfoId)) {
        // Create new
        contactInfo = dao.create(contactInfoContactType);
        contactInfos.add(contactInfo);
      }
      else {
        // Update existing with a new type
        
        contactInfo = contactInfos.stream().filter(ci -> ci.getId().equals(contactInfoId)).findFirst().orElse(null);

        if (contactInfo != null) {
          dao.update(contactInfo, contactInfoContactType);
        }
        else {
          throw new RuntimeException(String.format("No contact info found with id %d", contactInfoId));
        }
      }
      
      // Addresses
      
      Set<Long> existingAddresses = new HashSet<>();
      PyramusIxTableFacade addressTable = PyramusIxTableFacade.from(requestContext, String.format("%s.%d.addressTable", contactInfoEditorName, contactInfoIndex));
      for (PyramusIxTableRowFacade addressRow : addressTable.rows()) {
        Long addressId = addressRow.getLong("addressId");
        Boolean defaultAddress = addressRow.getBoolean("defaultAddress");
        String name = addressRow.getString("name");
        String street = addressRow.getString("street");
        String postal = addressRow.getString("postal");
        String city = addressRow.getString("city");
        String country = addressRow.getString("country");
        boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;

        if (hasAddress) {
          if (addressId == -1) {
            Address address = addressDAO.create(contactInfo, name, street, postal, city, country, defaultAddress);
            existingAddresses.add(address.getId());
          }
          else if (addressId > 0) {
            Address address = contactInfo.getAddresses().stream().filter(addr -> addr.getId().equals(addressId)).findFirst().orElse(null);
            // TODO not found?
            existingAddresses.add(addressId);
            addressDAO.update(address, defaultAddress, name, street, postal, city, country);
          }
        }
      }
      List<Address> addresses = contactInfo.getAddresses();
      for (int i = addresses.size() - 1; i >= 0; i--) {
        Address address = addresses.get(i);
        if (!existingAddresses.contains(address.getId())) {
          addressDAO.delete(address);
        }
      }

      // Emails
      
      Set<Long> existingEmails = new HashSet<>();
      PyramusIxTableFacade emailTable = PyramusIxTableFacade.from(requestContext, String.format("%s.%d.emailTable", contactInfoEditorName, contactInfoIndex));
      for (PyramusIxTableRowFacade emailRow : emailTable.rows()) {
        Long emailId = emailRow.getLong("emailId");
        Boolean defaultAddress = emailRow.getBoolean("defaultAddress");
        String emailAddress = StringUtils.trim(emailRow.getString("email"));
        
        if (StringUtils.isNotBlank(emailAddress)) {
          if (emailId == -1) {
            Email email = emailDAO.create(contactInfo, defaultAddress, emailAddress);
            existingEmails.add(email.getId());
          }
          else if (emailId > 0) {
            Email email = contactInfo.getEmails().stream().filter(ema -> ema.getId().equals(emailId)).findFirst().orElse(null);
            // TODO not found?
            existingEmails.add(emailId);
            emailDAO.update(email, defaultAddress, emailAddress);
          }
        }
      }
      
      List<Email> emails = contactInfo.getEmails();
      for (int i = emails.size() - 1; i >= 0; i--) {
        Email email = emails.get(i);
        if (!existingEmails.contains(email.getId())) {
          emailDAO.delete(email);
        }
      }

      // PhoneNumbers
      
      Set<Long> existingPhoneNumbers = new HashSet<>();
      PyramusIxTableFacade phoneTable = PyramusIxTableFacade.from(requestContext, String.format("%s.%d.phoneTable", contactInfoEditorName, contactInfoIndex));
      for (PyramusIxTableRowFacade phoneRow : phoneTable.rows()) {
        Long phoneId = phoneRow.getLong("phoneId");
        Boolean defaultNumber = phoneRow.getBoolean("defaultNumber");
        String number = StringUtils.trim(phoneRow.getString("phone"));
        
        if (StringUtils.isNotBlank(number)) {
          if (phoneId == -1) {
            PhoneNumber phoneNumber = phoneNumberDAO.create(contactInfo, defaultNumber, number);
            existingPhoneNumbers.add(phoneNumber.getId());
          }
          else if (phoneId > 0) {
            PhoneNumber phoneNumber = contactInfo.getPhoneNumbers().stream().filter(phonenum -> phonenum.getId().equals(phoneId)).findFirst().orElse(null);
            // TODO not found?
            phoneNumberDAO.update(phoneNumber, defaultNumber, number);
            existingPhoneNumbers.add(phoneId);
          }
        }
      }
      
      List<PhoneNumber> phoneNumbers = contactInfo.getPhoneNumbers();
      for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
        PhoneNumber phoneNumber = phoneNumbers.get(i);
        if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
          phoneNumberDAO.delete(phoneNumber);
        }
      }

    }
  }

  /**
   * Returns JSONObject that contains the TypedContactInfos of the students.
   * 
   * @param students
   * @return
   */
  public static JSONObject getPersonAdditionalContactInfos(Collection<Student> students) {
    JSONObject studentAdditionalContactInfosJSON = new JSONObject();
    
    for (Student student : students) {
      // Student's additional contact infos
      
      JSONArray studentAdditionalContactInfoJSON = ContactInfoUtils.toJSON(student.getAdditionalContactInfos());
      studentAdditionalContactInfosJSON.put(student.getId(), studentAdditionalContactInfoJSON);
    }
    
    return studentAdditionalContactInfosJSON;
  }
  
  public static JSONArray toJSON(List<? extends TypedContactInfo> contactInfos) {
    JSONArray ret = new JSONArray();
    
    if (CollectionUtils.isNotEmpty(contactInfos)) {
      for (TypedContactInfo contactInfo : contactInfos) {
        JSONObject contactInfoJSON = new JSONObject();
        contactInfoJSON.put("id", contactInfo.getId());
        contactInfoJSON.put("typeId", contactInfo.getContactType() != null ? contactInfo.getContactType().getId() : null);
        contactInfoJSON.put("typeName", contactInfo.getContactType() != null ? contactInfo.getContactType().getName() : null);
        
        if (contactInfo instanceof StudentAdditionalContactInfo) {
          contactInfoJSON.put("allowStudyDiscussions", ((StudentAdditionalContactInfo) contactInfo).isAllowStudyDiscussions());
        }
        
        JSONArray addressesJSON = new JSONArray();
        JSONArray emailsJSON = new JSONArray();
        JSONArray phoneNumbersJSON = new JSONArray();
        JSONArray contactURLsJSON = new JSONArray();
        
        for (Address address : contactInfo.getAddresses()) {
          JSONObject addressJSON = new JSONObject();
          addressJSON.put("id", address.getId());
          addressJSON.put("defaultAddress", address.getDefaultAddress());
          addressJSON.put("name", address.getName());
          addressJSON.put("streetAddress", address.getStreetAddress());
          addressJSON.put("city", address.getCity());
          addressJSON.put("postalCode", address.getPostalCode());
          addressJSON.put("country", address.getCountry());
          addressesJSON.add(addressJSON);
        }
        
        for (Email email : contactInfo.getEmails()) {
          JSONObject emailJSON = new JSONObject();
          emailJSON.put("id", email.getId());
          emailJSON.put("defaultAddress", email.getDefaultAddress());
          emailJSON.put("address", email.getAddress());
          emailsJSON.add(emailJSON);
        }
        
        for (PhoneNumber phoneNumber : contactInfo.getPhoneNumbers()) {
          JSONObject phoneNumberJSON = new JSONObject();
          phoneNumberJSON.put("id", phoneNumber.getId());
          phoneNumberJSON.put("defaultNumber", phoneNumber.getDefaultNumber());
          phoneNumberJSON.put("number", phoneNumber.getNumber());
          phoneNumbersJSON.add(phoneNumberJSON);
        }
        
        for (ContactURL contactURL : contactInfo.getContactURLs()) {
          JSONObject contactURLJSON = new JSONObject();
          contactURLJSON.put("id", contactURL.getId());
          contactURLJSON.put("url", contactURL.getURL());
          contactURLsJSON.add(contactURLJSON);
        }
        
        contactInfoJSON.put("addresses", addressesJSON);
        contactInfoJSON.put("emails", emailsJSON);
        contactInfoJSON.put("phoneNumbers", phoneNumbersJSON);
        contactInfoJSON.put("contactURLs", contactURLsJSON);
        
        ret.add(contactInfoJSON);
      }
    }
    
    return ret;
  }

  private interface ContactInfoConstructor<T> {
    T create(ContactType contactType);
    T update(T contactInfo, ContactType contactType);
  }
}
