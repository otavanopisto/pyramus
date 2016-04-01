package fi.otavanopisto.pyramus.rest.controller;


import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.AcademicTermDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.persistence.search.SearchTimeFilterMode;

@Dependent
@Stateless
public class CalendarController {
  @Inject
  private AcademicTermDAO academicTermDAO;
  @Inject
  private CourseDAO courseDAO;
  
  public AcademicTerm createAcademicTerm(String name, Date startDate, Date endDate) {
    AcademicTerm academicTerm = academicTermDAO.create(name, startDate, endDate);
    return academicTerm;
  }
  
  public List<AcademicTerm> listAcademicTerms() {
    List<AcademicTerm> academicTerms = academicTermDAO.listAll();
    return academicTerms;
  }
  
  public List<AcademicTerm> listUnarchivedAcademicTerms() {
    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();
    return academicTerms;
  }
  
  public AcademicTerm findAcademicTermById(Long id) {
    AcademicTerm academicTerm = academicTermDAO.findById(id);
    return academicTerm;
  }
  
  public SearchResult<Course> findCoursesByTerm(int resultsPerPage, int page, SearchTimeFilterMode timeFilterMode, Date timeframeStart, Date timeframeEnd, boolean filterArchived) {
    SearchResult<Course> courses = courseDAO.searchCourses(resultsPerPage, page, null, null, null, null, null, null, timeFilterMode, timeframeStart, timeframeEnd, filterArchived);
    return courses;
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
  
  public void deleteAcademicTerm(AcademicTerm academicTerm) {
    academicTermDAO.delete(academicTerm);
  }
}
