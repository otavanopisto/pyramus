package fi.pyramus.plugin.mailchimp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpException;
import com.ecwid.mailchimp.method.v2_0.lists.BatchError;
import com.ecwid.mailchimp.method.v2_0.lists.BatchSubscribeInfo;
import com.ecwid.mailchimp.method.v2_0.lists.BatchSubscribeMethod;
import com.ecwid.mailchimp.method.v2_0.lists.BatchSubscribeResult;
import com.ecwid.mailchimp.method.v2_0.lists.BatchUnsubscribeMethod;
import com.ecwid.mailchimp.method.v2_0.lists.BatchUnsubscribeResult;
import com.ecwid.mailchimp.method.v2_0.lists.Email;
import com.ecwid.mailchimp.method.v2_0.lists.ListMethod;
import com.ecwid.mailchimp.method.v2_0.lists.ListMethodResult;
import com.ecwid.mailchimp.method.v2_0.lists.MemberInfoData;
import com.ecwid.mailchimp.method.v2_0.lists.MembersMethod;
import com.ecwid.mailchimp.method.v2_0.lists.MembersResult;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.Student;

public class MailChimpSyncUtils {

  public static MailChimpSyncResult synchronizeStudyProgramme(MailChimpClient mailChimpClient, String apiKey, StudyProgramme studyProgramme) throws IOException, MailChimpException {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    String listId = findStudyProgrammeListIdByName(mailChimpClient, apiKey, studyProgramme.getName());
    if (StringUtils.isNotBlank(listId)) {
      MailChimpSyncResult result = new MailChimpSyncResult();
      
      List<String> subscribed = getListMemberEmails(mailChimpClient, apiKey, listId, "subscribed");
      List<String> unsubscribed = getListMemberEmails(mailChimpClient, apiKey, listId, "unsubscribed");
      List<String> subscribeEmails = new ArrayList<String>();
      List<String> removeEmails = new ArrayList<String>();
      List<String> removedEmails = new ArrayList<String>(subscribed);
  
      List<Student> students = studentDAO.listByStudyProgramme(studyProgramme);
      for (Student student : students) {
        // Students with secure info flag are skipped 
        if (!student.getAbstractStudent().getSecureInfo()) {
          fi.pyramus.domainmodel.base.Email defaultEmail = student.getDefaultEmail();
          if (defaultEmail != null) {
            String email = defaultEmail.getAddress();
            if (StringUtils.isNotBlank(email)) {
              removedEmails.remove(email);
      
              // If user is already unsubscribed, we well leave him/her alone...
              if (!unsubscribed.contains(email)) {
                boolean emailSubscribed = subscribed.contains(email);
      
                if ((student.getActive()) && (!emailSubscribed)) {
                  // If student is active but is not subscribed, we need to subscribe him/her
                  subscribeEmails.add(email);
                } else if ((!student.getActive()) && (emailSubscribed)) {
                  // If student is not active but is subscribed, we need to unsubscribe him/her
                  removeEmails.add(email);
                }
              } 
            }
          }
        }
      }
      
      if (!removedEmails.isEmpty()) {
        removeEmails.addAll(removedEmails);
      }
      
      if (!subscribeEmails.isEmpty()) {
        BatchSubscribeMethod subscribeMethod = new BatchSubscribeMethod();
        subscribeMethod.id = listId;
        subscribeMethod.apikey = apiKey;
        subscribeMethod.update_existing = true;
        subscribeMethod.double_optin = false;
        
        subscribeMethod.batch = new ArrayList<BatchSubscribeInfo>();
  
        for (String subscribeEmail : subscribeEmails) {
          BatchSubscribeInfo batchSubscribeInfo = new BatchSubscribeInfo();
          batchSubscribeInfo.email = new Email();
          batchSubscribeInfo.email.email = subscribeEmail;
          // TODO: html?
          batchSubscribeInfo.email_type = "html";
          subscribeMethod.batch.add(batchSubscribeInfo);
        }
  
        BatchSubscribeResult batchSubscribeResult = mailChimpClient.execute(subscribeMethod);
        result.addAdded(batchSubscribeResult.add_count);
        result.addUpdated(batchSubscribeResult.update_count);
        
        for (BatchError error : batchSubscribeResult.errors) {
          result.addError(error.email.email, error.error);
        }
      }
      
      if (!removeEmails.isEmpty()) {
        BatchUnsubscribeMethod unsubscribeMethod = new BatchUnsubscribeMethod();
        unsubscribeMethod.id = listId;
        unsubscribeMethod.apikey = apiKey;
        unsubscribeMethod.send_goodbye = false;
        unsubscribeMethod.send_notify = false;
        unsubscribeMethod.delete_member = true;
        
        unsubscribeMethod.batch = new ArrayList<Email>();
  
        for (String removeEmail : removeEmails) {
          Email email = new Email();
          email.email = removeEmail;
          unsubscribeMethod.batch.add(email);
        }
  
        BatchUnsubscribeResult batchUnsubscribeResult = mailChimpClient.execute(unsubscribeMethod);
        result.addRemoved(batchUnsubscribeResult.success_count);

        for (BatchError error : batchUnsubscribeResult.errors) {
          result.addError(error.email.email, error.error);
        }
      }
      
      return result;
    }
    
    return null;
  }

  public static List<String> getListMemberEmails(MailChimpClient client, String apiKey, String listId, String status) throws IOException, MailChimpException {
    List<String> result = new ArrayList<String>();

    MembersMethod membersMethod = new MembersMethod();
    membersMethod.apikey = apiKey;
    membersMethod.id = listId;
    membersMethod.status = status;

    MembersResult membersResult = client.execute(membersMethod);
    for (MemberInfoData data : membersResult.data) {
      if (data != null && StringUtils.isNotBlank(data.email)) {
        result.add(data.email);
      }
    }

    return result;
  }
  
  public static String findStudyProgrammeListIdByName(MailChimpClient mailChimpClient, String apiKey, String listName) throws IOException, MailChimpException {
    ListMethod listMethod = new ListMethod();
    listMethod.apikey = apiKey;
    listMethod.filters = new ListMethod.Filters();
    listMethod.filters.exact = true;
    listMethod.filters.list_name = listName;
    ListMethodResult listMethodResult = mailChimpClient.execute(listMethod);

    if (!listMethodResult.isEmpty() && listMethodResult.data.size() == 1) {
      return listMethodResult.data.get(0).id;
    }
    
    return null;
  }
}
