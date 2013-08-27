package fi.pyramus.rest.controller;


import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CalendarController {
  @Inject
  private AcademicTermDAO academicTermDAO;
  
  public AcademicTerm createAcademicTerm(String name, Date startDate, Date endDate) {
    AcademicTerm academicTerm = academicTermDAO.create(name, startDate, endDate);
    return academicTerm;
  }
  
  public List<AcademicTerm> findAcademicTerms() {
    List<AcademicTerm> academicTerms = academicTermDAO.listAll();
    return academicTerms;
  }
  
  public List<AcademicTerm> findUnarchivedAcademicTerms() {
    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();
    return academicTerms;
  }
  
  public AcademicTerm findAcademicTermById(Long id) {
    AcademicTerm academicTerm = academicTermDAO.findById(id);
    return academicTerm;
  }
  
  public AcademicTerm updateAcademicTerm(AcademicTerm term, String name, Date startDate, Date endDate) {
    academicTermDAO.update(term, name, startDate, endDate);
    return term;
  }
  
  public AcademicTerm archiveAcademicTerm(AcademicTerm academicTerm, User user) {
    academicTermDAO.archive(academicTerm, user);
    return academicTerm;
  }
  
  public AcademicTerm unarchiveAcademicTerm(AcademicTerm academicTerm, User user) {
    academicTermDAO.unarchive(academicTerm, user);
    return academicTerm;
  }
}
