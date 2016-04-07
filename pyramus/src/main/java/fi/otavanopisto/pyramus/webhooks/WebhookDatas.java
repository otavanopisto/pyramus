package fi.otavanopisto.pyramus.webhooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;

@RequestScoped
@Stateful
public class WebhookDatas {
  
  @PostConstruct
  public void init() {
    updatedStaffMemberIds = new ArrayList<>();
    updatedStudentIds = new ArrayList<>();
    updatedCourseIds = new ArrayList<>();
    updatedCourseStudentIds = new ArrayList<>();
    courseStudentStudentIdMap = new HashMap<>();
    courseStudentCourseIdMap = new HashMap<>();
    updatedCourseStaffMemberIds = new ArrayList<>();
    courseStaffMemberStaffMemberIdMap = new HashMap<>();
    courseStaffMemberCourseIdMap = new HashMap<>();
  }
  
  /* StaffMemberIds */
  
  public void addUpdatedStaffMemberId(Long updatedStaffMemberId) {
    if (!updatedStaffMemberIds.contains(updatedStaffMemberId)) {
      updatedStaffMemberIds.add(updatedStaffMemberId);
    }
  }
  
  public List<Long> retrieveUpdatedStaffMemberIds() {
    List<Long> result = new ArrayList<>(updatedStaffMemberIds);
    updatedStaffMemberIds.clear();
    return result;
  }
  
  /* StudentIds */
  
  public void addUpdatedStudentId(Long updatedStudentId) {
    if (!updatedStudentIds.contains(updatedStudentId)) {
      updatedStudentIds.add(updatedStudentId);
    }
  }
  
  public List<Long> retrieveUpdatedStudentIds() {
    List<Long> result = new ArrayList<>(updatedStudentIds);
    updatedStudentIds.clear();
    return result;
  }
  
  /* CourseIds */
  
  public void addUpdatedCourseId(Long updatedCourseId) {
    if (!updatedCourseIds.contains(updatedCourseId)) {
      updatedCourseIds.add(updatedCourseId);
    }
  }
  
  public List<Long> retrieveUpdatedCourseIds() {
    List<Long> result = new ArrayList<>(updatedCourseIds);
    updatedCourseIds.clear();
    return result;
  }
  
  /* Course Students */
  
  public void addUpdatedCourseStudent(Long updatedCourseStudentId, Long courseId, Long studentId) {
    if (!updatedCourseStudentIds.contains(updatedCourseStudentId)) {
      updatedCourseStudentIds.add(updatedCourseStudentId);
    }
    
    courseStudentStudentIdMap.put(updatedCourseStudentId, studentId);
    courseStudentCourseIdMap.put(updatedCourseStudentId, courseId);
  }
  
  public List<Long> retrieveUpdatedCourseStudentIds() {
    List<Long> result = new ArrayList<>(updatedCourseStudentIds);
    updatedCourseStudentIds.clear();
    return result;
  }

  public void clearUpdatedCourseStudentIds() {
    updatedCourseStudentIds.clear();
    courseStudentStudentIdMap.clear();
    courseStudentCourseIdMap.clear();
  }

  public Long getCourseStudentCourseId(Long courseStudentId) {
    return courseStudentCourseIdMap.get(courseStudentId);
  }

  public Long getCourseStudentStudentId(Long courseStudentId) {
    return courseStudentStudentIdMap.get(courseStudentId);
  }
  
  /* Course Staff Members */
  
  public void addUpdatedCourseStaffMember(Long updatedCourseStaffMemberId, Long courseId, Long staffMemberId) {
    if (!updatedCourseStaffMemberIds.contains(updatedCourseStaffMemberId)) {
      updatedCourseStaffMemberIds.add(updatedCourseStaffMemberId);
    }
    
    courseStaffMemberStaffMemberIdMap.put(updatedCourseStaffMemberId, staffMemberId);
    courseStaffMemberCourseIdMap.put(updatedCourseStaffMemberId, courseId);
  }
  
  public List<Long> retrieveUpdatedCourseStaffMemberIds() {
    List<Long> result = new ArrayList<>(updatedCourseStaffMemberIds);
    updatedCourseStaffMemberIds.clear();
    return result;
  }

  public void clearUpdatedCourseStaffMemberIds() {
    updatedCourseStaffMemberIds.clear();
    courseStaffMemberStaffMemberIdMap.clear();
    courseStaffMemberCourseIdMap.clear();
  }

  public Long getCourseStaffMemberCourseId(Long courseStaffMemberId) {
    return courseStaffMemberCourseIdMap.get(courseStaffMemberId);
  }

  public Long getCourseStaffMemberStaffMemberId(Long courseStaffMemberId) {
    return courseStaffMemberStaffMemberIdMap.get(courseStaffMemberId);
  }
  
  private List<Long> updatedStaffMemberIds;
  private List<Long> updatedStudentIds;
  private List<Long> updatedCourseIds;
  private List<Long> updatedCourseStudentIds;
  private Map<Long, Long> courseStudentStudentIdMap;
  private Map<Long, Long> courseStudentCourseIdMap;
  private List<Long> updatedCourseStaffMemberIds;
  private Map<Long, Long> courseStaffMemberStaffMemberIdMap;
  private Map<Long, Long> courseStaffMemberCourseIdMap;
}
