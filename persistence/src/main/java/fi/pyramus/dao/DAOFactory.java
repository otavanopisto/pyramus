package fi.pyramus.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.dao.accesslog.AccessLogEntryDAO;
import fi.pyramus.dao.accesslog.AccessLogEntryPathDAO;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.BillingDetailsDAO;
import fi.pyramus.dao.base.ComponentBaseDAO;
import fi.pyramus.dao.base.ContactInfoDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.base.CourseBaseDAO;
import fi.pyramus.dao.base.CourseBaseVariableDAO;
import fi.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.pyramus.dao.base.CourseEducationTypeDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalLengthDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableDAO;
import fi.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryEntityDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryEntityPropertyDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryPropertyDAO;
import fi.pyramus.dao.changelog.TrackedEntityPropertyDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.pyramus.dao.courses.BasicCourseResourceDAO;
import fi.pyramus.dao.courses.CourseComponentDAO;
import fi.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseDescriptionDAO;
import fi.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.courses.CourseStudentVariableDAO;
import fi.pyramus.dao.courses.CourseStudentVariableKeyDAO;
import fi.pyramus.dao.courses.CourseUserDAO;
import fi.pyramus.dao.courses.GradeCourseResourceDAO;
import fi.pyramus.dao.courses.OtherCostDAO;
import fi.pyramus.dao.courses.StudentCourseResourceDAO;
import fi.pyramus.dao.drafts.DraftDAO;
import fi.pyramus.dao.file.FileDAO;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.pyramus.dao.grading.CreditDAO;
import fi.pyramus.dao.grading.CreditLinkDAO;
import fi.pyramus.dao.grading.CreditVariableDAO;
import fi.pyramus.dao.grading.CreditVariableKeyDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateCourseDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.dao.help.HelpFolderDAO;
import fi.pyramus.dao.help.HelpItemDAO;
import fi.pyramus.dao.help.HelpItemTitleDAO;
import fi.pyramus.dao.help.HelpPageContentDAO;
import fi.pyramus.dao.help.HelpPageDAO;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.plugins.PluginDAO;
import fi.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.projects.ProjectModuleDAO;
import fi.pyramus.dao.projects.StudentProjectDAO;
import fi.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.pyramus.dao.reports.ReportCategoryDAO;
import fi.pyramus.dao.reports.ReportContextDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.dao.resources.MaterialResourceDAO;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.dao.resources.WorkResourceDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentActivityTypeDAO;
import fi.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.pyramus.dao.students.StudentGroupDAO;
import fi.pyramus.dao.students.StudentGroupStudentDAO;
import fi.pyramus.dao.students.StudentGroupUserDAO;
import fi.pyramus.dao.students.StudentImageDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.dao.users.InternalAuthDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.dao.users.UserVariableKeyDAO;

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
  
  /* Student */
  
  public fi.pyramus.dao.students.StudentDAO getStudentDAO() {
    return (StudentDAO) findByClass(fi.pyramus.dao.students.StudentDAO.class);
  }

  public AbstractStudentDAO getAbstractStudentDAO() {
    return (AbstractStudentDAO) findByClass(AbstractStudentDAO.class);
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
  
  public fi.pyramus.dao.courses.CourseDAO getCourseDAO() {
    return (CourseDAO) findByClass(fi.pyramus.dao.courses.CourseDAO.class);
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
  
  public InternalAuthDAO getInternalAuthDAO() {
    return (InternalAuthDAO) findByClass(InternalAuthDAO.class);
  }
  
  public UserDAO getUserDAO() {
    return (UserDAO) findByClass(UserDAO.class);
  }
  
  public UserVariableDAO getUserVariableDAO() {
    return (UserVariableDAO) findByClass(UserVariableDAO.class);
  }
  
  public UserVariableKeyDAO getUserVariableKeyDAO() {
    return (UserVariableKeyDAO) findByClass(UserVariableKeyDAO.class);
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
