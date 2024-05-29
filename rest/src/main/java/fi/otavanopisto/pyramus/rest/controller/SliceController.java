package fi.otavanopisto.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity;
import fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel;
import fi.otavanopisto.pyramus.rest.model.StudentCardType;

@Dependent
@Stateless
public class SliceController {
  
  @Inject
  private StudentController studentController;
  
  @Inject
  private StudentCardDAO studentCardDAO;
  
  public List<SliceStudentCardRestModel> listStudentCards() {
    Calendar cal = Calendar.getInstance();
    
    // substract 14 days
    cal.add(Calendar.DATE, -14);

    // convert to date
    Date twoWeeksBefore = cal.getTime();
    
    // List active & cancelled student cards where expiration date is within the past two weeks or in the future
    List<StudentCard> studentCards = studentCardDAO.listStudentCardsByExpiryDateAndActive(twoWeeksBefore);
    
    List<fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel> studentCardList = new ArrayList<fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel>();

    for (StudentCard studentCard : studentCards) {
      // Student cards with activity "cancelled" need still to be listed two weeks after cancellation
      if (studentCard.getActivity().equals(fi.otavanopisto.pyramus.domainmodel.students.StudentCardActivity.CANCELLED)) {
        if (studentCard.getCancellationDate().before(twoWeeksBefore)) {
          continue;
        }
      }
      
      SliceStudentCardRestModel restModel = toRestModel(studentCard);
       
      studentCardList.add(restModel);
    }
    
    return studentCardList;
  }
  
  public SliceStudentCardRestModel toRestModel(StudentCard studentCard) {

    fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel restModel = new fi.otavanopisto.pyramus.rest.model.SliceStudentCardRestModel();
    
    Student student = studentController.findStudentById(studentCard.getStudent().getId());

    Email email = student.getContactInfo().getEmails().stream().filter(e -> Boolean.TRUE.equals(e.getDefaultAddress())).findFirst().orElse(null);
    
    restModel.setFirstName(student.getFirstName());
    restModel.setLastName(student.getLastName());
    restModel.setBirthday(student.getPerson().getBirthday());
    restModel.setEmail(email != null ? email.getAddress() : null);
    restModel.setType(StudentCardType.valueOf(studentCard.getType().name()));
    restModel.setExpiryDate(studentCard.getExpiryDate());
    
    if (studentCard.getCancellationDate() != null && studentCard.getActivity().equals(StudentCardActivity.CANCELLED)) {
      // Slice does not consider the cancellation date, so we need to compare the cancellation date and the expiration date, choose earlier one, and set it as the expiryDate for the slice.
      if (studentCard.getCancellationDate().before(studentCard.getExpiryDate())) {     
        restModel.setExpiryDate(studentCard.getCancellationDate());
      }
    }
    
    return restModel;
  }
}

