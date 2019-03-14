package fi.otavanopisto.pyramus.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.accesslog.AccessLogEntryDAO;
import fi.otavanopisto.pyramus.dao.accesslog.AccessLogEntryPathDAO;
import fi.otavanopisto.pyramus.dao.accommodation.RoomDAO;
import fi.otavanopisto.pyramus.dao.accommodation.RoomTypeDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.AcademicTermDAO;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.base.ComponentBaseDAO;
import fi.otavanopisto.pyramus.dao.base.ContactInfoDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CourseBaseDAO;
import fi.otavanopisto.pyramus.dao.base.CourseBaseVariableDAO;
import fi.otavanopisto.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalLengthDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.LoginLogDAO;
import fi.otavanopisto.pyramus.dao.base.MagicKeyDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.changelog.ChangeLogEntryDAO;
import fi.otavanopisto.pyramus.dao.changelog.ChangeLogEntryEntityDAO;
import fi.otavanopisto.pyramus.dao.changelog.ChangeLogEntryEntityPropertyDAO;
import fi.otavanopisto.pyramus.dao.changelog.ChangeLogEntryPropertyDAO;
import fi.otavanopisto.pyramus.dao.changelog.TrackedEntityPropertyDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.dao.courses.BasicCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseUserDAO;
import fi.otavanopisto.pyramus.dao.courses.GradeCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.courses.OtherCostDAO;
import fi.otavanopisto.pyramus.dao.courses.StudentCourseResourceDAO;
import fi.otavanopisto.pyramus.dao.drafts.DraftDAO;
import fi.otavanopisto.pyramus.dao.file.FileDAO;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateCourseDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.otavanopisto.pyramus.dao.help.HelpFolderDAO;
import fi.otavanopisto.pyramus.dao.help.HelpItemDAO;
import fi.otavanopisto.pyramus.dao.help.HelpItemTitleDAO;
import fi.otavanopisto.pyramus.dao.help.HelpPageContentDAO;
import fi.otavanopisto.pyramus.dao.help.HelpPageDAO;
import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleComponentDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.plugins.PluginDAO;
import fi.otavanopisto.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportContextDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.dao.resources.MaterialResourceDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceCategoryDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceDAO;
import fi.otavanopisto.pyramus.dao.resources.WorkResourceDAO;
import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupUserDAO;
import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.EmailSignatureDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.dao.webhooks.WebhookDAO;

public class DAOFactory {
  
  public static DAOFactory getInstance() {
    return instance;
  }
  
  private final static DAOFactory instance = new DAOFactory();
  
  /* SystemDAO */
  
  public SystemDAO getSystemDAO() {
    return (SystemDAO) findByClass(SystemDAO.class);
  }

  /* Draft */
  
  public DraftDAO getDraftDAO() {
    return (DraftDAO) findByClass(DraftDAO.class);
  }
  
  /* Application */

  public fi.otavanopisto.pyramus.dao.application.ApplicationDAO getApplicationDAO() {
    return (ApplicationDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationDAO.class);
  }

  public fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO getApplicationSignaturesDAO() {
    return (ApplicationSignaturesDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO.class);
  }

  public fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO getApplicationLogDAO() {
    return (ApplicationLogDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO.class);
  }

  public fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO getApplicationMailTemplateDAO() {
    return (ApplicationMailTemplateDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO.class);
  }

  public fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO getApplicationNotificationDAO() {
    return (ApplicationNotificationDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO.class);
  }

  public fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO getApplicationAttachmentDAO() {
    return (ApplicationAttachmentDAO) findByClass(fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO.class);
  }
  
  /* Matriculation examination enrollments */
  
  public MatriculationExamEnrollmentDAO getMatriculationExamEnrollmentDAO() {
    return (MatriculationExamEnrollmentDAO) findByClass(MatriculationExamEnrollmentDAO.class);
  }

  public MatriculationExamAttendanceDAO getMatriculationExamAttendanceDAO() {
    return (MatriculationExamAttendanceDAO) findByClass(MatriculationExamAttendanceDAO.class);
  }

  public MatriculationExamDAO getMatriculationExamDAO() {
    return (MatriculationExamDAO) findByClass(MatriculationExamDAO.class);
  }
  
  public MatriculationExamSubjectSettingsDAO getMatriculationExamSubjectSettingsDAO() {
    return (MatriculationExamSubjectSettingsDAO) findByClass(MatriculationExamSubjectSettingsDAO.class);
  }

  /* Student */
  
  public fi.otavanopisto.pyramus.dao.students.StudentDAO getStudentDAO() {
    return (StudentDAO) findByClass(fi.otavanopisto.pyramus.dao.students.StudentDAO.class);
  }

  public StudentActivityTypeDAO getStudentActivityTypeDAO() {
    return (StudentActivityTypeDAO) findByClass(StudentActivityTypeDAO.class);
  }
  
  public StudentContactLogEntryDAO getStudentContactLogEntryDAO() {
    return (StudentContactLogEntryDAO) findByClass(StudentContactLogEntryDAO.class);
  }
  
  public StudentContactLogEntryCommentDAO getStudentContactLogEntryCommentDAO() {
    return (StudentContactLogEntryCommentDAO) findByClass(StudentContactLogEntryCommentDAO.class);
  }
  
  public StudentGroupContactLogEntryDAO getStudentGroupContactLogEntryDAO() {
    return (StudentGroupContactLogEntryDAO) findByClass(StudentGroupContactLogEntryDAO.class);
  }
  
  public StudentGroupContactLogEntryCommentDAO getStudentGroupContactLogEntryCommentDAO() {
    return (StudentGroupContactLogEntryCommentDAO) findByClass(StudentGroupContactLogEntryCommentDAO.class);
  }
  
  public StudentEducationalLevelDAO getStudentEducationalLevelDAO() {
    return (StudentEducationalLevelDAO) findByClass(StudentEducationalLevelDAO.class);
  }
  
  public StudentExaminationTypeDAO getStudentExaminationTypeDAO() {
    return (StudentExaminationTypeDAO) findByClass(StudentExaminationTypeDAO.class);
  }
  
  public StudentGroupDAO getStudentGroupDAO() {
    return (StudentGroupDAO) findByClass(StudentGroupDAO.class);
  }
  
  public StudentGroupStudentDAO getStudentGroupStudentDAO() {
    return (StudentGroupStudentDAO) findByClass(StudentGroupStudentDAO.class);
  }
  
  public StudentGroupUserDAO getStudentGroupUserDAO() {
    return (StudentGroupUserDAO) findByClass(StudentGroupUserDAO.class);
  }
  
  public StudentImageDAO getStudentImageDAO() {
    return (StudentImageDAO) findByClass(StudentImageDAO.class);
  }

  public StudentStudyEndReasonDAO getStudentStudyEndReasonDAO() {
    return (StudentStudyEndReasonDAO) findByClass(StudentStudyEndReasonDAO.class);
  }

  public CourseStudentVariableDAO getCourseStudentVariableDAO() {
    return (CourseStudentVariableDAO) findByClass(CourseStudentVariableDAO.class);
  }
  
  public CourseStudentVariableKeyDAO getCourseStudentVariableKeyDAO() {
    return (CourseStudentVariableKeyDAO) findByClass(CourseStudentVariableKeyDAO.class);
  }

  public StudentLodgingPeriodDAO getStudentLodgingPeriodDAO() {
    return (StudentLodgingPeriodDAO) findByClass(StudentLodgingPeriodDAO.class);
  }
  
  public StudentSubjectGradeDAO getStudentSubjectGradeDAO() {
    return (StudentSubjectGradeDAO) findByClass(StudentSubjectGradeDAO.class);
  }
  
  public StudentStudyPeriodDAO getStudentStudyPeriodDAO() {
    return (StudentStudyPeriodDAO) findByClass(StudentStudyPeriodDAO.class);
  }
  
  /* Course */
  
  public BasicCourseResourceDAO getBasicCourseResourceDAO() {
    return (BasicCourseResourceDAO) findByClass(BasicCourseResourceDAO.class);
  }
  
  public CourseComponentDAO getCourseComponentDAO() {
    return (CourseComponentDAO) findByClass(CourseComponentDAO.class);
  }
  
  public CourseComponentResourceDAO getCourseComponentResourceDAO() {
    return (CourseComponentResourceDAO) findByClass(CourseComponentResourceDAO.class);
  }
  
  public fi.otavanopisto.pyramus.dao.courses.CourseDAO getCourseDAO() {
    return (CourseDAO) findByClass(fi.otavanopisto.pyramus.dao.courses.CourseDAO.class);
  }
  
  public CourseDescriptionCategoryDAO getCourseDescriptionCategoryDAO() {
    return (CourseDescriptionCategoryDAO) findByClass(CourseDescriptionCategoryDAO.class);
  }
  
  public CourseDescriptionDAO getCourseDescriptionDAO() {
    return (CourseDescriptionDAO) findByClass(CourseDescriptionDAO.class);
  }
  
  public CourseEnrolmentTypeDAO getCourseEnrolmentTypeDAO() {
    return (CourseEnrolmentTypeDAO) findByClass(CourseEnrolmentTypeDAO.class);
  }
  
  public CourseParticipationTypeDAO getCourseParticipationTypeDAO() {
    return (CourseParticipationTypeDAO) findByClass(CourseParticipationTypeDAO.class);
  }
  
  public CourseStateDAO getCourseStateDAO() {
    return (CourseStateDAO) findByClass(CourseStateDAO.class);
  }
  
  public CourseTypeDAO getCourseTypeDAO() {
    return (CourseTypeDAO) findByClass(CourseTypeDAO.class);
  }
  
  public CourseStudentDAO getCourseStudentDAO() {
    return (CourseStudentDAO) findByClass(CourseStudentDAO.class);
  }

  public CourseUserDAO getCourseUserDAO() {
    return (CourseUserDAO) findByClass(CourseUserDAO.class);
  }
  
  public GradeCourseResourceDAO getGradeCourseResourceDAO() {
    return (GradeCourseResourceDAO) findByClass(GradeCourseResourceDAO.class);
  }

  public OtherCostDAO getOtherCostDAO() {
    return (OtherCostDAO) findByClass(OtherCostDAO.class);
  }
  
  public StudentCourseResourceDAO getStudentCourseResourceDAO() {
    return (StudentCourseResourceDAO) findByClass(StudentCourseResourceDAO.class);
  }

  public CourseStaffMemberDAO getCourseStaffMemberDAO() {
    return (CourseStaffMemberDAO) findByClass(CourseStaffMemberDAO.class);
  }

  public CourseStaffMemberRoleDAO getCourseStaffMemberRoleDAO() {
    return (CourseStaffMemberRoleDAO) findByClass(CourseStaffMemberRoleDAO.class);
  }

  /* System */
  
  public SettingDAO getSettingDAO() {
    return (SettingDAO) findByClass(SettingDAO.class);
  }

  public SettingKeyDAO getSettingKeyDAO() {
    return (SettingKeyDAO) findByClass(SettingKeyDAO.class);
  }

  /* Report */
  
  public ReportDAO getReportDAO() {
    return (ReportDAO) findByClass(ReportDAO.class);
  }
  
  public ReportCategoryDAO getReportCategoryDAO() {
    return (ReportCategoryDAO) findByClass(ReportCategoryDAO.class);
  }

  public ReportContextDAO getReportContextDAO() {
    return (ReportContextDAO) findByClass(ReportContextDAO.class);
  }
  
  /* Users */

  public UserDAO getUserDAO() {
    return (UserDAO) findByClass(UserDAO.class);
  }
  
  public InternalAuthDAO getInternalAuthDAO() {
    return (InternalAuthDAO) findByClass(InternalAuthDAO.class);
  }
  
  public StaffMemberDAO getStaffMemberDAO() {
    return (StaffMemberDAO) findByClass(StaffMemberDAO.class);
  }
  
  public UserVariableDAO getUserVariableDAO() {
    return (UserVariableDAO) findByClass(UserVariableDAO.class);
  }
  
  public UserVariableKeyDAO getUserVariableKeyDAO() {
    return (UserVariableKeyDAO) findByClass(UserVariableKeyDAO.class);
  }
  
  public PersonDAO getPersonDAO() {
    return (PersonDAO) findByClass(PersonDAO.class);
  }
  
  public UserIdentificationDAO getUserIdentificationDAO(){
    return (UserIdentificationDAO) findByClass(UserIdentificationDAO.class);
  }
  
  public PersonVariableDAO getPersonVariableDAO() {
    return (PersonVariableDAO) findByClass(PersonVariableDAO.class);
  }
  
  public PersonVariableKeyDAO getPersonVariableKeyDAO() {
    return (PersonVariableKeyDAO) findByClass(PersonVariableKeyDAO.class);
  }
  
  /* Webhooks */

  public WebhookDAO getWebhookDAO() {
    return (WebhookDAO) findByClass(WebhookDAO.class);
  }
  
  /* Change Log */
  
  public ChangeLogEntryDAO getChangeLogEntryDAO() {
    return (ChangeLogEntryDAO) findByClass(ChangeLogEntryDAO.class);
  }
  
  public ChangeLogEntryEntityDAO getChangeLogEntryEntityDAO() {
    return (ChangeLogEntryEntityDAO) findByClass(ChangeLogEntryEntityDAO.class);
  }
  
  public ChangeLogEntryEntityPropertyDAO getChangeLogEntryEntityPropertyDAO() {
    return (ChangeLogEntryEntityPropertyDAO) findByClass(ChangeLogEntryEntityPropertyDAO.class);
  }
  
  public ChangeLogEntryPropertyDAO getChangeLogEntryPropertyDAO() {
    return (ChangeLogEntryPropertyDAO) findByClass(ChangeLogEntryPropertyDAO.class);
  }
  
  public TrackedEntityPropertyDAO getTrackedEntityPropertyDAO() {
    return (TrackedEntityPropertyDAO) findByClass(TrackedEntityPropertyDAO.class);
  }

  /* Resource */
  
  public MaterialResourceDAO getMaterialResourceDAO() {
    return (MaterialResourceDAO) findByClass(MaterialResourceDAO.class);
  }
  
  public ResourceCategoryDAO getResourceCategoryDAO() {
    return (ResourceCategoryDAO) findByClass(ResourceCategoryDAO.class);
  }
  
  public ResourceDAO getResourceDAO() {
    return (ResourceDAO) findByClass(ResourceDAO.class);
  }
  
  public WorkResourceDAO getWorkResourceDAO() {
    return (WorkResourceDAO) findByClass(WorkResourceDAO.class);
  }

  /* Module */
  
  public ModuleDAO getModuleDAO() {
    return (ModuleDAO) findByClass(ModuleDAO.class);
  }
  
  public ModuleComponentDAO getModuleComponentDAO() {
    return (ModuleComponentDAO) findByClass(ModuleComponentDAO.class);
  }

  /* Project */
  
  public ProjectDAO getProjectDAO() {
    return (ProjectDAO) findByClass(ProjectDAO.class);
  }
  
  public ProjectModuleDAO getProjectModuleDAO() {
    return (ProjectModuleDAO) findByClass(ProjectModuleDAO.class);
  }
  
  public StudentProjectDAO getStudentProjectDAO() {
    return (StudentProjectDAO) findByClass(StudentProjectDAO.class);
  }
  
  public StudentProjectModuleDAO getStudentProjectModuleDAO() {
    return (StudentProjectModuleDAO) findByClass(StudentProjectModuleDAO.class);
  }

  /* Help */
  
  public HelpFolderDAO getHelpFolderDAO() {
    return (HelpFolderDAO) findByClass(HelpFolderDAO.class);
  }
  
  public HelpItemDAO getHelpItemDAO() {
    return (HelpItemDAO) findByClass(HelpItemDAO.class);
  }
  
  public HelpItemTitleDAO getHelpItemTitleDAO() {
    return (HelpItemTitleDAO) findByClass(HelpItemTitleDAO.class);
  }
  
  public HelpPageContentDAO getHelpPageContentDAO() {
    return (HelpPageContentDAO) findByClass(HelpPageContentDAO.class);
  }
  
  public HelpPageDAO getHelpPageDAO() {
    return (HelpPageDAO) findByClass(HelpPageDAO.class);
  }
  
  /* Grading */
  
  public CourseAssessmentDAO getCourseAssessmentDAO() {
    return (CourseAssessmentDAO) findByClass(CourseAssessmentDAO.class);
  }
  
  public CourseAssessmentRequestDAO getCourseAssessmentRequestDAO() {
    return (CourseAssessmentRequestDAO) findByClass(CourseAssessmentRequestDAO.class);
  }

  public CreditDAO getCreditDAO() {
    return (CreditDAO) findByClass(CreditDAO.class);
  }

  public CreditLinkDAO getCreditLinkDAO() {
    return (CreditLinkDAO) findByClass(CreditLinkDAO.class);
  }
  
  public GradeDAO getGradeDAO() {
    return (GradeDAO) findByClass(GradeDAO.class);
  }
  
  public GradingScaleDAO getGradingScaleDAO() {
    return (GradingScaleDAO) findByClass(GradingScaleDAO.class);
  }
  
  public ProjectAssessmentDAO getProjectAssessmentDAO() {
    return (ProjectAssessmentDAO) findByClass(ProjectAssessmentDAO.class);
  }
  
  public TransferCreditDAO getTransferCreditDAO() {
    return (TransferCreditDAO) findByClass(TransferCreditDAO.class);
  }
  
  public TransferCreditTemplateCourseDAO getTransferCreditTemplateCourseDAO() {
    return (TransferCreditTemplateCourseDAO) findByClass(TransferCreditTemplateCourseDAO.class);
  }
  
  public TransferCreditTemplateDAO getTransferCreditTemplateDAO() {
    return (TransferCreditTemplateDAO) findByClass(TransferCreditTemplateDAO.class);
  }
  
  public CreditVariableDAO getCreditVariableDAO() {
    return (CreditVariableDAO) findByClass(CreditVariableDAO.class);
  }

  public CreditVariableKeyDAO getCreditVariableKeyDAO() {
    return (CreditVariableKeyDAO) findByClass(CreditVariableKeyDAO.class);
  }

  /* Base */
  
  public AcademicTermDAO getAcademicTermDAO() {
    return (AcademicTermDAO) findByClass(AcademicTermDAO.class);
  }

  public AddressDAO getAddressDAO() {
    return (AddressDAO) findByClass(AddressDAO.class);
  }

  public BillingDetailsDAO getBillingDetailsDAO() {
    return (BillingDetailsDAO) findByClass(BillingDetailsDAO.class);
  }

  public ComponentBaseDAO getComponentBaseDAO() {
    return (ComponentBaseDAO) findByClass(ComponentBaseDAO.class);
  }

  public ContactInfoDAO getContactInfoDAO() {
    return (ContactInfoDAO) findByClass(ContactInfoDAO.class);
  }

  public ContactTypeDAO getContactTypeDAO() {
    return (ContactTypeDAO) findByClass(ContactTypeDAO.class);
  }

  public ContactURLDAO getContactURLDAO() {
    return (ContactURLDAO) findByClass(ContactURLDAO.class);
  }

  public ContactURLTypeDAO getContactURLTypeDAO() {
    return (ContactURLTypeDAO) findByClass(ContactURLTypeDAO.class);
  }

  public CourseBaseDAO getCourseBaseDAO() {
    return (CourseBaseDAO) findByClass(CourseBaseDAO.class);
  }

  public CourseBaseVariableDAO getCourseBaseVariableDAO() {
    return (CourseBaseVariableDAO) findByClass(CourseBaseVariableDAO.class);
  }

  public CourseBaseVariableKeyDAO getCourseBaseVariableKeyDAO() {
    return (CourseBaseVariableKeyDAO) findByClass(CourseBaseVariableKeyDAO.class);
  }

  public CourseEducationSubtypeDAO getCourseEducationSubtypeDAO() {
    return (CourseEducationSubtypeDAO) findByClass(CourseEducationSubtypeDAO.class);
  }

  public CourseEducationTypeDAO getCourseEducationTypeDAO() {
    return (CourseEducationTypeDAO) findByClass(CourseEducationTypeDAO.class);
  }

  public DefaultsDAO getDefaultsDAO() {
    return (DefaultsDAO) findByClass(DefaultsDAO.class);
  }

  public EducationalLengthDAO getEducationalLengthDAO() {
    return (EducationalLengthDAO) findByClass(EducationalLengthDAO.class);
  }

  public EducationalTimeUnitDAO getEducationalTimeUnitDAO() {
    return (EducationalTimeUnitDAO) findByClass(EducationalTimeUnitDAO.class);
  }

  public EducationSubtypeDAO getEducationSubtypeDAO() {
    return (EducationSubtypeDAO) findByClass(EducationSubtypeDAO.class);
  }

  public EducationTypeDAO getEducationTypeDAO() {
    return (EducationTypeDAO) findByClass(EducationTypeDAO.class);
  }

  public EmailDAO getEmailDAO() {
    return (EmailDAO) findByClass(EmailDAO.class);
  }

  public EmailSignatureDAO getEmailSignatureDAO() {
    return (EmailSignatureDAO) findByClass(EmailSignatureDAO.class);
  }

  public LanguageDAO getLanguageDAO() {
    return (LanguageDAO) findByClass(LanguageDAO.class);
  }

  public MagicKeyDAO getMagicKeyDAO() {
    return (MagicKeyDAO) findByClass(MagicKeyDAO.class);
  }

  public MunicipalityDAO getMunicipalityDAO() {
    return (MunicipalityDAO) findByClass(MunicipalityDAO.class);
  }

  public NationalityDAO getNationalityDAO() {
    return (NationalityDAO) findByClass(NationalityDAO.class);
  }

  public PhoneNumberDAO getPhoneNumberDAO() {
    return (PhoneNumberDAO) findByClass(PhoneNumberDAO.class);
  }

  public SchoolDAO getSchoolDAO() {
    return (SchoolDAO) findByClass(SchoolDAO.class);
  }

  public SchoolFieldDAO getSchoolFieldDAO() {
    return (SchoolFieldDAO) findByClass(SchoolFieldDAO.class);
  }

  public SchoolVariableDAO getSchoolVariableDAO() {
    return (SchoolVariableDAO) findByClass(SchoolVariableDAO.class);
  }

  public SchoolVariableKeyDAO getSchoolVariableKeyDAO() {
    return (SchoolVariableKeyDAO) findByClass(SchoolVariableKeyDAO.class);
  }

  public StudyProgrammeDAO getStudyProgrammeDAO() {
    return (StudyProgrammeDAO) findByClass(StudyProgrammeDAO.class);
  }

  public StudyProgrammeCategoryDAO getStudyProgrammeCategoryDAO() {
    return (StudyProgrammeCategoryDAO) findByClass(StudyProgrammeCategoryDAO.class);
  }

  public SubjectDAO getSubjectDAO() {
    return (SubjectDAO) findByClass(SubjectDAO.class);
  }

  public TagDAO getTagDAO() {
    return (TagDAO) findByClass(TagDAO.class);
  }

  public CurriculumDAO getCurriculumDAO() {
    return (CurriculumDAO) findByClass(CurriculumDAO.class);
  }
  
  /* File */
  
  public FileDAO getFileDAO() {
    return (FileDAO) findByClass(FileDAO.class);
  }
  
  public FileTypeDAO getFileTypeDAO() {
    return (FileTypeDAO) findByClass(FileTypeDAO.class);
  }

  public StudentFileDAO getStudentFileDAO() {
    return (StudentFileDAO) findByClass(StudentFileDAO.class);
  }
  
  
  /* Plugins */

  public PluginRepositoryDAO getPluginRepositoryDAO() {
    return (PluginRepositoryDAO) findByClass(PluginRepositoryDAO.class);
  }

  public PluginDAO getPluginDAO() {
    return (PluginDAO) findByClass(PluginDAO.class);
  }
  
  /* ClientApplications */
  
  public ClientApplicationDAO getClientApplicationDAO(){
    return (ClientApplicationDAO) findByClass(ClientApplicationDAO.class);
  }
  
  public ClientApplicationAuthorizationCodeDAO getClientApplicationAuthorizationCodeDAO(){
    return (ClientApplicationAuthorizationCodeDAO) findByClass(ClientApplicationAuthorizationCodeDAO.class);
  }
  
  public ClientApplicationAccessTokenDAO getClientApplicationAccessTokenDAO(){
    return (ClientApplicationAccessTokenDAO) findByClass(ClientApplicationAccessTokenDAO.class);
  }
  
  /* AccessLog */
  
  public AccessLogEntryDAO getAccessLogEntryDAO() {
    return (AccessLogEntryDAO) findByClass(AccessLogEntryDAO.class);
  }
  
  public AccessLogEntryPathDAO getAccessLogEntryPathDAO() {
    return (AccessLogEntryPathDAO) findByClass(AccessLogEntryPathDAO.class);
  }

  public LoginLogDAO getLoginLogDAO() {
    return (LoginLogDAO) findByClass(LoginLogDAO.class);
  }
  
  /* Security */
  
  public PermissionDAO getPermissionDAO() {
    return (PermissionDAO) findByClass(PermissionDAO.class);
  }
  
  public EnvironmentRolePermissionDAO getEnvironmentRolePermissionDAO() {
    return (EnvironmentRolePermissionDAO) findByClass(EnvironmentRolePermissionDAO.class);
  }
  
  /* Accommodation */

  public RoomTypeDAO getRoomTypeDAO() {
    return (RoomTypeDAO) findByClass(RoomTypeDAO.class);
  }

  public RoomDAO getRoomDAO() {
    return (RoomDAO) findByClass(RoomDAO.class);
  }

  /* Koski */
  
  public KoskiPersonLogDAO getKoskiPersonLogDAO() {
    return (KoskiPersonLogDAO) findByClass(KoskiPersonLogDAO.class);
  }

  private String getAppName() throws NamingException {
    String appName = "";
    try {
      String jndiName = "java:app/AppName";
      appName = (String) new InitialContext().lookup(jndiName);
    } catch (Throwable t) {
    }
    
    if (StringUtils.isBlank(appName))
      appName = "Pyramus";
    
    return appName;
  }
  
  private Object findByClass(Class<?> cls) {
    try {
      String jndiName = "java:app/" + getAppName() + "/" + cls.getSimpleName();
      return new InitialContext().lookup(jndiName);
    } catch (NamingException e) {
      throw new PersistenceException(e);
    }
  }

}
