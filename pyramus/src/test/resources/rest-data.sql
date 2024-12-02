insert into 
  SettingKey (id, name)
values 
  (1, 'system.environment');
  
insert into 
  Setting (id, settingKey, value)
values 
  (1, 1, 'it');

insert into
  Organization (id, name, archived)
values
  (1, 'Common Test Organization', false);
  
insert into 
  ContactType (id, name, version, nonUnique, archived)
values 
  (1, 'Home', 1, false, false);

insert into 
  GradingScale (id, archived, name, description, version)
values  
  (1, false, 'test scale #1', 'grading scale for testing #1', 1),
  (2, false, 'test scale #2', 'grading scale for testing #2', 1),
  (3, false, 'test scale #3 (4-10)', 'grading scale for testing #3', 1);

insert into 
  Grade (id, GPA, archived, name, description, passingGrade, qualification, gradingScale, indexColumn, version)
values  
  (1, 10, false, 'test grade #1', 'grade for testing #1', false, 'qualification #1', 1, 1, 1),
  (2, 20, false, 'test grade #2', 'grade for testing #2', true, 'qualification #2', 1, 2, 1),
  (3, 4, false, '4', 'grade for testing #3', false, 'qualification #1', 3, 1, 1),
  (4, 5, false, '5', 'grade for testing #4', true, 'qualification #1', 3, 1, 1),
  (5, 6, false, '6', 'grade for testing #5', true, 'qualification #1', 3, 1, 1),
  (6, 7, false, '7', 'grade for testing #6', true, 'qualification #1', 3, 1, 1),
  (7, 8, false, '8', 'grade for testing #7', true, 'qualification #1', 3, 1, 1),
  (8, 9, false, '9', 'grade for testing #8', true, 'qualification #1', 3, 1, 1),
  (9, 10, false, '10', 'grade for testing #9', true, 'qualification #1', 3, 1, 1);
  
insert into 
  ContactInfo (id, additionalInfo, version)
values   
  (1, 'For test school #1', 1),
  (2, 'For test school #2', 1),
  (3, 'For test student #3', 1),
  (4, 'For test student #4', 1),
  (5, 'Test Guest #1', 1),
  (6, 'Test Guest #2', 1),
  (7, 'Test User  #1', 1),
  (8, 'Test Manager #1', 1),
  (9, 'Test Administrator #1', 1),
  (10, 'Test Student #1', 1),
  (11, 'Trusted System', 1),
  (12, 'Study Guider', 1),
  (13, 'Teacher', 1),
  (14, 'Study Programme Leader', 1),
  (15, 'Test Student #2', 1),
  (16, 'Closed', 1),
  (17, 'Student Parent', 1);
  
insert into 
  Email (id, address, defaultAddress, contactInfo, contactType, indexColumn, version)
values 
  (1, 'school1@bogusmail.com', true, 1, 1, 0, 1),
  (2, 'school2@bogusmail.com', true, 2, 1, 0, 1),
  (3, 'student1@bogusmail.com', true, 3, 1, 0, 1),
  (4, 'student2@bogusmail.com', true, 4, 1, 0, 1),
  (5, 'guest1@bogusmail.com', true, 5, 1, 0, 1),
  (6, 'guest2@bogusmail.com', true, 6, 1, 0, 1),
  (7, 'user1@bogusmail.com', true, 7, 1, 0, 1),
  (8, 'manager1@bogusmail.com', true, 8, 1, 0, 1),
  (9, 'administrator1@bogusmail.com', true, 9, 1, 0, 1),
  (10, 'student1@bogusmail.com', true, 10, 1, 0, 1),
  (11, 'trusted@bogusmail.com', true, 11, 1, 0, 1),
  (12, 'guider@bogusmail.com', true, 12, 1, 0, 1),
  (13, 'teacher@bogusmail.com', true, 13, 1, 0, 1),
  (14, 'stuproleader@bogusmail.com', true, 14, 1, 0, 1);

insert into 
  Person (id, version, birthday, sex, socialSecurityNumber, basicInfo, secureInfo)
values 
  (1, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '123411-7890', 'Test staff #1', false),
  (2, 1, STR_TO_DATE('1 1 1970', '%d %m %Y'), 'MALE', '012345535-8901', 'Test staff #2', false),
  (3, 1, STR_TO_DATE('1 1 1990', '%d %m %Y'), 'FEMALE', '123456-7890', 'Test student #1', false),
  (4, 1, STR_TO_DATE('1 1 1990', '%d %m %Y'), 'MALE', '01234567-8901', 'Test student #2', false),
  (5, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121213-7890', 'Test User #1', false),
  (6, 1, STR_TO_DATE('1 1 1970', '%d %m %Y'), 'MALE', '131214-8901', 'Test Manager #1', false),
  (7, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121216-7891', 'Test administrator #1', false),
  (8, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121217-7892', 'Test Student #1', false),
  (9, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '000000-0000', 'Trusted System', false),
  (10, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '000100-0000', 'Study Guider', false),
  (11, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '000010-0000', 'Teacher', false),
  (12, 1, STR_TO_DATE('1 1 1981', '%d %m %Y'), 'FEMALE', '000100-0000', 'SPLeader', false),
  (13, 1, STR_TO_DATE('1 1 1981', '%d %m %Y'), 'FEMALE', '000100-0000', 'Test Student #2', false),
  (14, 1, STR_TO_DATE('1 1 1956', '%d %m %Y'), 'MALE', '010100-2000', 'Closed', false),
  (15, 1, STR_TO_DATE('1 1 1957', '%d %m %Y'), 'MALE', '010100-3000', 'Student Parent', false);
  
insert into
  User (id, person_id, firstName, lastName, contactInfo, version, archived)
values 
  (1, 1, 'Test Guest', 'User #1', 5, 1, false),
  (2, 2, 'Test Guest', 'User #2', 6, 1, false),
  (3, 3, 'Tanya', 'Test #1', 3, 1, false),
  (4, 4, 'David', 'Test #2', 4, 1, false),
  (5, 5, 'Test User', 'User #3', 7, 1, false),
  (6, 6, 'Test Manager', 'User #4', 8, 1, false),
  (7, 7, 'Test Administrator', 'User #5', 9, 1, false),
  (8, 8, 'Test Student', 'User #4', 10, 1, false),
  (9, 9, 'Trusted System', 'Trusted system user', 11, 1, false),
  (10, 10, 'Study Guider', 'Study Guider user', 12, 1, false),
  (11, 11, 'Teacher', 'Teacher user', 13, 1, false),
  (12, 12, 'SPLeader', 'SPLeader user', 14, 1, false),
  (13, 13, 'Test Student2', 'User #4', 15, 1, false),
  (14, 14, 'Test Closed', 'User #14', 16, 1, false),
  (15, 15, 'Test Student Parent', 'User #15', 17, 1, false);

update Person p
set p.defaultUser_id = p.id;
  
insert into
  StudentParent (id, organization)
values
  (15, 1);
  
insert into
  StaffMember (id, organization, title, enabled)
values 
  (1, 1, null, true),
  (2, 1, null, true),
  (5, 1, null, true),
  (6, 1, null, true),
  (7, 1, null, true),
  (9, 1, null, true),
  (10, 1, null, true),
  (11, 1, null, true),
  (12, 1, null, true),
  (14, 1, null, true);
  
insert into
  StaffMemberRoles (staffMember_id, role)
values
  (1, 'GUEST'),
  (2, 'GUEST'),
  (5, 'USER'),
  (6, 'MANAGER'),
  (7, 'ADMINISTRATOR'),
  (9, 'TRUSTED_SYSTEM'),
  (10, 'STUDY_GUIDER'),
  (11, 'TEACHER'),
  (12, 'STUDY_PROGRAMME_LEADER');
  
insert into 
  AcademicTerm (id, name, startDate, endDate, archived, version)
values 
  (1, 'fall', STR_TO_DATE('1 8 2014', '%d %m %Y'), STR_TO_DATE('23 12 2014', '%d %m %Y'), false, 0),
  (2, 'spring', STR_TO_DATE('4 1 2015', '%d %m %Y'), STR_TO_DATE('30 5 2015', '%d %m %Y'), false, 0);
  
insert into
  EducationType (id, archived, code, name, version)
values
  (1, false, 'TEST', 'Test Education Type', 1),
  (2, false, 'TST2', 'Test EduType 2', 1);
  
insert into
  EducationSubtype (id, archived, code, name, educationType, version)
values
  (1, false, 'TST1', 'Test Subtype #1', 1, 1),
  (2, false, 'TST2', 'Test Subtype #2', 1, 1),
  (3, false, 'TST3', 'Test Subtype #3', 2, 1),
  (4, false, 'TST4', 'Test Subtype #4', 2, 1);
  
insert into 
  Subject (id, archived, code, name, version, educationType)
values 
  (1, false, 'TEST', 'Test Subject', 1, 1),
  (2, false, 'TST2', 'Test Subject #2', 1, 2),
  (3, false, 'TST3', 'Test Subject #3', 1, 2),
  (4, false, 'BI', 'Biology', 1, 1);
  
insert into
  CourseState (id, archived, name, version)
values 
  (1, false, 'Planning', 1),
  (2, false, 'In Progress', 1),
  (3, false, 'Ended', 1);
  
insert into
  CourseType (id, archived, name, version)
values 
  (1, false, 'Non-stop', 1),
  (2, false, 'Group Work', 1);
  
insert into
  CourseEnrolmentType (id, name, version)
values 
  (1, 'Manual', 1),
  (2, 'LE', 1);
   
insert into
  CourseParticipationType (id, name, version, indexColumn, archived)
values 
  (1, 'Canceled', 1, 0, false),
  (2, 'Passed', 1, 1, false);
 
insert into 
  EducationalTimeUnit (id, archived, baseUnits, name, symbol, version)
values 
  (1, false, 1, 'Hour', 'h', 1),
  (2, false, 1, 'Points', 'p', 1),
  (3, false, 168, 'Week', 'w', 1);

insert into
  EducationalLength (id, units, unit, version)
values 
  (1, 1, 1, 1),
  (2, 123, 1, 1),
  (3, 234, 1, 1),
  (4, 345, 1, 1),
  (5, 456, 1, 1),
  (6, 567, 1, 1),
  (7, 678, 1, 1),
  (8, 910, 1, 1);
  
insert into
  CourseDescriptionCategory (id, name, archived)
values
  (1, 'Basic', false),
  (2, 'Special', false);
  
insert into 
  CourseBase (id, name, archived, created, lastModified, description, maxParticipantCount, version, creator, lastModifier)
values
  (1, 'Test Module #1', false, STR_TO_DATE('1 1 2010', '%d %m %Y'), STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Module #1 for testing', 100, 1, 1, 1),
  (1000, 'Test Course #1', false, STR_TO_DATE('1 1 2010', '%d %m %Y'), STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Course #1 for testing', 100, 1, 1, 1),
  (1001, 'Test Course #2', false, STR_TO_DATE('1 1 2011', '%d %m %Y'), STR_TO_DATE('1 1 2011', '%d %m %Y'), 'Course #2 for testing', 200, 1, 1, 1);

insert into
  CourseModule (course, subject, courseNumber, courseLength)
values
  (1, 1, 1, 1);

insert into
  Module (id)
values 
  (1);
  
insert into 
  ComponentBase (id, name, description, length, archived, version) 
values 
  (1, 'Test Module #1 component #1', 'Module component for testing', 1, false, 1),
  (2, 'Test Module #1 component #2', 'Module component for testing', 2, false, 1);
  
insert into 
  ModuleComponent (id, module, indexColumn)
values 
  (1, 1, 0),
  (2, 1, 1);
  
insert into
  CourseModule (id, course, subject, courseNumber, courseLength)
values
  (1000, 1000, 1, 1, 1),
  (1001, 1001, 1, 2, 1);

insert into 
  Course (id, beginDate, endDate, localTeachingDays, nameExtension, module, state, teachingHours, distanceTeachingDays, planningHours, assessingHours, enrolmentTimeEnd, courseTemplate)
values 
  (1000, STR_TO_DATE('2 2 2010', '%d %m %Y'), STR_TO_DATE('3 3 2010', '%d %m %Y'), 10, 'Ext', 1, 1, 40, 30, 20, 10, STR_TO_DATE('1 1 2010', '%d %m %Y'), false),
  (1001, STR_TO_DATE('2 2 2011', '%d %m %Y'), STR_TO_DATE('3 3 2011', '%d %m %Y'), 20, 'ABC', 1, 2, 15, 17, 20, 10, STR_TO_DATE('1 1 2011', '%d %m %Y'), false);
  
insert into
  CourseEducationType (id, courseBase, educationType, version)
values
  (1000, 1001, 1, 1);

insert into
  CourseEducationSubtype (id, courseEducationType, educationSubtype, version)
values
  (1000, 1000, 1, 1);

insert into 
  ComponentBase (id, name, description, length, archived, version) 
values 
  (1000, 'Test Course #1 component #1', 'Course component for testing', 3, false, 1),
  (1001, 'Test Course #1 component #2', 'Course component for testing', 4, false, 1),
  (1002, 'Test Course #2 component #1', 'Course component for testing', 5, false, 1),
  (1003, 'Test Course #2 component #2', 'Course component for testing', 6, false, 1);
  
insert into 
  CourseComponent (id, course, indexColumn)
values 
  (1000, 1000, 0),
  (1001, 1000, 1),
  (1002, 1001, 0),
  (1003, 1001, 1);
  
insert into 
  Project (id, name, description, optionalStudies, creator, created, lastModified, lastModifier, archived, version)
values 
  (1, 'Test Project #1', 'Project for testing', 7, 1, STR_TO_DATE('6 6 2010', '%d %m %Y'), STR_TO_DATE('6 6 2010', '%d %m %Y'), 1, false, 1),
  (2, 'Test Project #2', 'Project for testing', 8, 1, STR_TO_DATE('6 6 2011', '%d %m %Y'), STR_TO_DATE('6 6 2011', '%d %m %Y'), 1, false, 1);
  
insert into 
  ProjectModule (id, optionality, module, project, indexColumn, version)
values 
  (1, 'OPTIONAL', 1, 1, 0, 1),
  (2, 'OPTIONAL', 1, 2, 0, 1);
 
insert into 
  SchoolField (id, name, archived)
values 
  (1, 'Field #1', false),
  (2, 'Field #2', false);
  
insert into 
  Address (id, city, country, postalCode, streetAddress, name, contactInfo, contactType, indexColumn, defaultAddress, version)
values 
  (1, 'Eastbury', 'Senegal', '76763-3962', '2636 Johnston Harbors', null, 1, 1, 0, true, 1),
  (2, 'Zambia', 'Portborough', '02531-1064', '430 Vesta Inlet', null, 2, 1, 0, true, 1),
  (3, 'Southshire', 'Yemen', '17298', '6967 Bailee Mission', null, 3, 1, 0, true, 1),
  (4, 'Northchester', 'Cuba', '97733', '556 Lupe Mountains', null, 4, 1, 0, true, 1),
  (5, 'Shire', 'New-Zealand', '17298', '123 Mission', null, 7, 1, 0, true, 1),
  (6, 'Chester', 'Belgium', '1111', '456 Mountains', null, 8, 1, 0, true, 1),
  (7, 'Ushire', 'China', '17298', '123 Missions', null, 9, 1, 0, true, 1),
  (8, 'Kishter', 'Brazil', '1111', '456 Tains', null, 10, 1, 0, true, 1);
  
insert into 
  PhoneNumber (id, number, defaultNumber, contactInfo, contactType, indexColumn, version)
values 
  (1, '+123 45 678 9012', true, 1, 1, 0, 1),
  (2, '+234 56 789 0123', true, 2, 1, 0, 1),
  (3, '+456 78 901 2345', true, 3, 1, 0, 1),
  (4, '+567 89 012 3456', true, 4, 1, 0, 1),
  (5, '+456 78 901 2347', true, 7, 1, 0, 1),
  (6, '+567 89 012 3458', true, 8, 1, 0, 1),
  (7, '+56 78 901 2347', true, 9, 1, 0, 1),
  (8, '+67 89 012 3458', true, 10, 1, 0, 1);
  
insert into
  ContactURLType (id, name, version, archived)
values 
  (1, 'WWW', 1, false);
    
insert into 
  ContactURL (id, url, contactInfo, contactURLType, indexColumn, version)
values 
  (1, 'http://www.school1webpage.com', 1, 1, 0, 1),
  (2, 'http://www.school2webpage.com', 2, 1, 0, 1),
  (3, 'http://www.student1webpage.com', 3, 1, 0, 1),
  (4, 'http://www.student2webpage.com', 4, 1, 0, 1);
  
insert into 
  School (id, name, code, contactInfo, version, field, archived)
values 
  (1, 'School #1', 'TST1', 1, 1, 1, false),
  (2, 'School #2', 'TST2', 2, 1, 1, false);
  
insert into 
  SchoolVariableKey (id, userEditable, variableKey, variableName, variableType, version)
values 
  (1, false, 'TV1', 'Test Variable #1 - text', 'TEXT', 1),
  (2, false, 'TV2', 'Test Variable #2 - number', 'NUMBER', 1),
  (3, false, 'TV3', 'Test Variable #3 - boolean', 'BOOLEAN', 1);
  
insert into 
  SchoolVariable (id, value, school, variableKey, version, archived)
values 
  (1, 'test', 1, 1, 1, false);
  
insert into 
  UserVariableKey (id, userEditable, variableKey, variableName, variableType, version)
values 
  (1, false, 'TV1', 'Test Variable #1 - text', 'TEXT', 1),
  (2, false, 'TV2', 'Test Variable #2 - number', 'NUMBER', 1),
  (3, false, 'TV3', 'Test Variable #3 - boolean', 'BOOLEAN', 1);
  
insert into 
  CourseBaseVariableKey (id, userEditable, variableKey, variableName, variableType, version)
values 
  (1, false, 'TV1', 'Test Variable #1 - text', 'TEXT', 1),
  (2, false, 'TV2', 'Test Variable #2 - number', 'NUMBER', 1),
  (3, false, 'TV3', 'Test Variable #3 - boolean', 'BOOLEAN', 1);

insert into 
  Language (id, code, name, version, archived)
values 
  (1, 'TST1', 'Language #1', 1, false),
  (2, 'TST2', 'Language #2', 1, false);  

insert into 
  Curriculum (id, name, archived)
values 
  (1, 'Curriculum #1', false),
  (2, 'Curriculum #2', false),
  (3, 'Curriculum #3', false),
  (4, 'Curriculum #4', false);  

insert into 
  Municipality (id, code, name, version, archived)
values 
  (1, 'TST1', 'Municipality #1', 1, false),
  (2, 'TST2', 'Municipality #2', 1, false);  

insert into 
  Nationality (id, code, name, version, archived)
values 
  (1, 'TST1', 'Nationality #1', 1, false),
  (2, 'TST2', 'Nationality #2', 1, false);  

insert into 
  StudentEducationalLevel (id, name, version, archived)
values 
  (1, 'StudentEducationalLevel #1', 1, false),
  (2, 'StudentEducationalLevel #2', 1, false);   

insert into 
  StudentExaminationType (id, name, version, archived)
values 
  (1, 'StudentExaminationType #1', 1, false),
  (2, 'StudentExaminationType #2', 1, false);   
   
insert into 
  StudyProgrammeCategory (id, name, educationType, version, archived)
values 
  (1, 'StudyProgrammeCategory #1', 1, 1, false),
  (2, 'StudyProgrammeCategory #2', 2, 1, false);   
   
insert into 
  StudyProgramme (id, organization, code, name, category, version, hasEvaluationFees, archived)
values 
  (1, 1, 'TST1', 'StudyProgramme #1', 1, 1, false, false),
  (2, 1, 'TST2', 'StudyProgramme #2', 2, 1, false, false);     
   
insert into 
  StudentGroup (id, name, description, creator, lastModifier, beginDate, created, lastModified, version, archived, guidanceGroup, organization)
values 
  (1, 'StudentGroup #1', 'Group of students #1', 1, 1, STR_TO_DATE('1 1 2010', '%d %m %Y'), STR_TO_DATE('2 2 2010', '%d %m %Y'), STR_TO_DATE('3 3 2010', '%d %m %Y'), 1, false, false, 1),
  (2, 'StudentGroup #2', 'Group of students #2', 1, 1, STR_TO_DATE('4 4 2010', '%d %m %Y'), STR_TO_DATE('5 5 2010', '%d %m %Y'), STR_TO_DATE('6 6 2010', '%d %m %Y'), 1, false, false, 1),
  (3, 'StudentGroup #3', 'Group of students #3', 1, 1, STR_TO_DATE('4 4 2010', '%d %m %Y'), STR_TO_DATE('5 5 2012', '%d %m %Y'), STR_TO_DATE('6 6 2012', '%d %m %Y'), 1, false, false, 1);  
  
insert into
  StudentActivityType (id, name, version, archived)
values  
  (1, 'StudentActivityType #1', 1, false),
  (2, 'StudentActivityType #2', 1, false);
  
insert into 
  Student (id, studyProgramme, nickname, previousStudies, studyStartDate, 
    additionalInfo, activityType, educationalLevel, language, municipality, nationality, school, 
    examinationType, education, curriculum_id)
values 
  (3, 1, 'Tanya-T', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Testing #1', 1, 1, 1, 1, 1, 1, 1, 'Education #1', 3),
  (4, 1, 'David-T', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Testing #2', 1, 1, 1, 1, 1, 1, 1, 'Education #2', null),
  (8, 1, 'TEST-User', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test test', 1, 1, 1, 1, 1, 1, 1, 'Education smthg', null),
  (13, 1, 'TEST-Student2', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test test', 1, 1, 1, 1, 1, 1, 1, 'Education smthg2', null);

insert into StudentGroupStudent
  (id, studentGroup, student, version)
values 
  (1, 1, 3, 1),
  (2, 1, 4, 1),
  (3, 3, 13, 1);

insert into StudentGroupUser
  (id, studentGroup, staffMember_id, groupAdvisor, studyAdvisor, messageReceiver, version)
values 
  (1, 3, 10, false, false, false, 1);
  
insert into StudentStudyEndReason 
  (id, name, parentReason, archived, version)
values 
  (1, 'StudentStudyEndReason #1', null, false, 1),
  (2, 'StudentStudyEndReason #2', 1, false, 1);
  
insert into 
  StudentContactLogEntry (id, creatorName, entryDate, text, type, student, version, archived)
values
  (1, 'Tester #1', STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test text #1', 'LETTER', 3, 1, false),
  (2, 'Tester #2', STR_TO_DATE('1 1 2011', '%d %m %Y'), 'Test text #2', 'PHONE', 3, 1, false);

insert into
   ClientApplication (id, clientName, clientId, clientSecret, skipPrompt)
values
    (1, 'Clientapp without prompt skipping', '854885cf-2284-4b17-b63c-a8b189535f8d', 'cqJ4J1if8ca5RMUqaYyFPYToxfFxt2YT8PXL3pNygPClnjJdt55lrFs6k1SZ6colJN24YEtZ7bhFW29S', false),
    (2, 'Clientapp with prompt skipping', 'a46d97f8-c37b-4bef-afe4-369a2481f632', 'n22FKq4WBSJxX91ekTnx4F7KdmcGsVsXL2XEplCfm9fYrNbGRyp7CpXiGiu4TaL25lceLchFatjCpSCU', true);
   
insert into
   ClientApplicationAuthorizationCode (id, authorizationCode, redirectUrl, user_id, app_id)
values
/**   
Old. Needed anymore?
**/
    (1, 'ff81d5b8500c773e7a1776a7963801e3', 'https://localhost:8443/oauth2ClientTest/success', 1, 1),
/** GUEST ROLE**/
    (2, 'ff81d5b8500c773e7a1776a7963801e4', 'https://localhost:8443/oauth2ClientTest/success', 2, 1),
/** USER ROLE**/    
    (3, 'ff81d5b8500c773e7a1776a7963801e5', 'https://localhost:8443/oauth2ClientTest/success', 5, 1),
/** MANAGER ROLE**/
    (4, 'ff81d5b8500c773e7a1776a7963801e6', 'https://localhost:8443/oauth2ClientTest/success', 6, 1),
/** ADMINISTRATOER ROLE**/
    (5, 'ff81d5b8500c773e7a1776a7963801e7', 'https://localhost:8443/oauth2ClientTest/success', 7, 1),
/** STUDENT ROLE**/
    (6, 'ff81d5b8500c773e7a1776a7963801e8', 'https://localhost:8443/oauth2ClientTest/success', 8, 1),
/** TRUSTED_SYSTEM ROLE**/
    (7, 'ff81d5b8500c773e7a1776a7963801e9', 'https://localhost:8443/oauth2ClientTest/success', 9, 1),
/** STUDY_GUIDER ROLE**/
    (8, 'ff81d5b8500c773e7a1776a7963801e0', 'https://localhost:8443/oauth2ClientTest/success', 10, 1),
/** TEACHER ROLE**/
    (9, 'ff81d5b8500c773e7a1776a7963801e1', 'https://localhost:8443/oauth2ClientTest/success', 11, 1),
/** STUDY_PROGRAMME_LEADER ROLE**/
    (10, 'ff81d5b8500c773e7a1776a7963801e2', 'https://localhost:8443/oauth2ClientTest/success', 12, 1),
/** CLOSED ROLE**/
    (11, 'ff81d5b8500c773e7a1776a796380166', 'https://localhost:8443/oauth2ClientTest/success', 14, 1),
/** STUDENT_PARENT ROLE**/
    (12, 'ff81d5b8500c773e7a1776a796380115', 'https://localhost:8443/oauth2ClientTest/success', 15, 1);    
        
insert into 
  CourseUser (id, course, version)
values 
  (1, 1000, 1),
  (2, 1000, 1),
  (3, 1000, 1),
  (4, 1000, 1),
  (5, 1000, 1),
  (6, 1000, 1),
  (7, 1001, 1);
  
insert into 
  CourseStaffMember (id, staffMember_id, role)
values 
  (1, 1, 'TEACHER'),
  (2, 2, 'TUTOR'),
  (3, 5, 'TEACHER'),
  (4, 6, 'TEACHER');

insert into 
  CourseStudent (id, archived, enrolmentTime, lodging, optionality, billingDetails, enrolmentType, participationType, student)
values
  (5, false, STR_TO_DATE('1 1 2010', '%d %m %Y'), false, 'OPTIONAL', null, 1, 1, 3),
  (6, false, STR_TO_DATE('1 1 2011', '%d %m %Y'), true, 'MANDATORY', null, 2, 2, 4),
  (7, false, STR_TO_DATE('1 1 2012', '%d %m %Y'), true, 'MANDATORY', null, 2, 2, 13);

insert into
  Credit (id, archived, verbalAssessment, date, creditType, version, assessor_id, grade)
values
  (1, false, 'TEST ASSESSMENT', STR_TO_DATE('1 1 2011', '%d %m %Y'), 'CourseAssessment', 1, 6, 2);
  
insert into
  CourseAssessment (id, courseStudent, courseModule)
values
  (1, 5, 1);

insert into Defaults 
  (id, educationalTimeUnit, courseState, version, courseParticipationType, courseEnrolmentType, organization, studentDefaultContactType, userDefaultContactType)
values
  (1, 1, 1, 1, 1, 1, 1, 1, 1);

DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseModule';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseModule', COALESCE(MAX(id) + 1, 1) FROM CourseModule;
DELETE FROM hibernate_sequences WHERE sequence_name = 'SettingKey';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'SettingKey', COALESCE(MAX(id) + 1, 1) FROM SettingKey;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Setting';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Setting', COALESCE(MAX(id) + 1, 1) FROM Setting;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Organization';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Organization', COALESCE(MAX(id) + 1, 1) FROM Organization;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ContactType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ContactType', COALESCE(MAX(id) + 1, 1) FROM ContactType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'GradingScale';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'GradingScale', COALESCE(MAX(id) + 1, 1) FROM GradingScale;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Grade';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Grade', COALESCE(MAX(id) + 1, 1) FROM Grade;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ContactInfo';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ContactInfo', COALESCE(MAX(id) + 1, 1) FROM ContactInfo;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Email';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Email', COALESCE(MAX(id) + 1, 1) FROM Email;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Person';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Person', COALESCE(MAX(id) + 1, 1) FROM Person;
DELETE FROM hibernate_sequences WHERE sequence_name = 'User';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'User', COALESCE(MAX(id) + 1, 1) FROM User;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StaffMember';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StaffMember', COALESCE(MAX(id) + 1, 1) FROM StaffMember;
DELETE FROM hibernate_sequences WHERE sequence_name = 'AcademicTerm';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'AcademicTerm', COALESCE(MAX(id) + 1, 1) FROM AcademicTerm;
DELETE FROM hibernate_sequences WHERE sequence_name = 'EducationType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'EducationType', COALESCE(MAX(id) + 1, 1) FROM EducationType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'EducationSubtype';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'EducationSubtype', COALESCE(MAX(id) + 1, 1) FROM EducationSubtype;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Subject';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Subject', COALESCE(MAX(id) + 1, 1) FROM Subject;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseState';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseState', COALESCE(MAX(id) + 1, 1) FROM CourseState;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseType', COALESCE(MAX(id) + 1, 1) FROM CourseType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseEnrolmentType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseEnrolmentType', COALESCE(MAX(id) + 1, 1) FROM CourseEnrolmentType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseParticipationType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseParticipationType', COALESCE(MAX(id) + 1, 1) FROM CourseParticipationType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'EducationalTimeUnit';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'EducationalTimeUnit', COALESCE(MAX(id) + 1, 1) FROM EducationalTimeUnit;
DELETE FROM hibernate_sequences WHERE sequence_name = 'EducationalLength';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'EducationalLength', COALESCE(MAX(id) + 1, 1) FROM EducationalLength;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseDescriptionCategory';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseDescriptionCategory', COALESCE(MAX(id) + 1, 1) FROM CourseDescriptionCategory;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseBase';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseBase', COALESCE(MAX(id) + 1, 1) FROM CourseBase;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Module';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Module', COALESCE(MAX(id) + 1, 1) FROM Module;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ComponentBase';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ComponentBase', COALESCE(MAX(id) + 1, 1) FROM ComponentBase;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ModuleComponent';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ModuleComponent', COALESCE(MAX(id) + 1, 1) FROM ModuleComponent;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseBase';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseBase', COALESCE(MAX(id) + 1, 1) FROM CourseBase;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Course';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Course', COALESCE(MAX(id) + 1, 1) FROM Course;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseEducationType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseEducationType', COALESCE(MAX(id) + 1, 1) FROM CourseEducationType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseEducationSubtype';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseEducationSubtype', COALESCE(MAX(id) + 1, 1) FROM CourseEducationSubtype;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ComponentBase';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ComponentBase', COALESCE(MAX(id) + 1, 1) FROM ComponentBase;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseComponent';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseComponent', COALESCE(MAX(id) + 1, 1) FROM CourseComponent;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Project';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Project', COALESCE(MAX(id) + 1, 1) FROM Project;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ProjectModule';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ProjectModule', COALESCE(MAX(id) + 1, 1) FROM ProjectModule;
DELETE FROM hibernate_sequences WHERE sequence_name = 'SchoolField';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'SchoolField', COALESCE(MAX(id) + 1, 1) FROM SchoolField;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Address';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Address', COALESCE(MAX(id) + 1, 1) FROM Address;
DELETE FROM hibernate_sequences WHERE sequence_name = 'PhoneNumber';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'PhoneNumber', COALESCE(MAX(id) + 1, 1) FROM PhoneNumber;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ContactURLType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ContactURLType', COALESCE(MAX(id) + 1, 1) FROM ContactURLType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ContactURL';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ContactURL', COALESCE(MAX(id) + 1, 1) FROM ContactURL;
DELETE FROM hibernate_sequences WHERE sequence_name = 'School';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'School', COALESCE(MAX(id) + 1, 1) FROM School;
DELETE FROM hibernate_sequences WHERE sequence_name = 'SchoolVariableKey';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'SchoolVariableKey', COALESCE(MAX(id) + 1, 1) FROM SchoolVariableKey;
DELETE FROM hibernate_sequences WHERE sequence_name = 'SchoolVariable';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'SchoolVariable', COALESCE(MAX(id) + 1, 1) FROM SchoolVariable;
DELETE FROM hibernate_sequences WHERE sequence_name = 'UserVariableKey';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'UserVariableKey', COALESCE(MAX(id) + 1, 1) FROM UserVariableKey;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseBaseVariableKey';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseBaseVariableKey', COALESCE(MAX(id) + 1, 1) FROM CourseBaseVariableKey;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Language';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Language', COALESCE(MAX(id) + 1, 1) FROM Language;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Curriculum';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Curriculum', COALESCE(MAX(id) + 1, 1) FROM Curriculum;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Municipality';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Municipality', COALESCE(MAX(id) + 1, 1) FROM Municipality;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Nationality';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Nationality', COALESCE(MAX(id) + 1, 1) FROM Nationality;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentEducationalLevel';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentEducationalLevel', COALESCE(MAX(id) + 1, 1) FROM StudentEducationalLevel;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentExaminationType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentExaminationType', COALESCE(MAX(id) + 1, 1) FROM StudentExaminationType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudyProgrammeCategory';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudyProgrammeCategory', COALESCE(MAX(id) + 1, 1) FROM StudyProgrammeCategory;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudyProgramme';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudyProgramme', COALESCE(MAX(id) + 1, 1) FROM StudyProgramme;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentGroup';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentGroup', COALESCE(MAX(id) + 1, 1) FROM StudentGroup;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentActivityType';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentActivityType', COALESCE(MAX(id) + 1, 1) FROM StudentActivityType;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Student';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Student', COALESCE(MAX(id) + 1, 1) FROM Student;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentGroupStudent';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentGroupStudent', COALESCE(MAX(id) + 1, 1) FROM StudentGroupStudent;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentGroupUser';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentGroupUser', COALESCE(MAX(id) + 1, 1) FROM StudentGroupUser;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentStudyEndReason';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentStudyEndReason', COALESCE(MAX(id) + 1, 1) FROM StudentStudyEndReason;
DELETE FROM hibernate_sequences WHERE sequence_name = 'StudentContactLogEntry';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'StudentContactLogEntry', COALESCE(MAX(id) + 1, 1) FROM StudentContactLogEntry;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ClientApplication';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ClientApplication', COALESCE(MAX(id) + 1, 1) FROM ClientApplication;
DELETE FROM hibernate_sequences WHERE sequence_name = 'ClientApplicationAuthorizationCode';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'ClientApplicationAuthorizationCode', COALESCE(MAX(id) + 1, 1) FROM ClientApplicationAuthorizationCode;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseUser';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseUser', COALESCE(MAX(id) + 1, 1) FROM CourseUser;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseStudent';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseStudent', COALESCE(MAX(id) + 1, 1) FROM CourseStudent;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Credit';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Credit', COALESCE(MAX(id) + 1, 1) FROM Credit;
DELETE FROM hibernate_sequences WHERE sequence_name = 'CourseAssessment';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'CourseAssessment', COALESCE(MAX(id) + 1, 1) FROM CourseAssessment;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Defaults';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Defaults', COALESCE(MAX(id) + 1, 1) FROM Defaults;
DELETE FROM hibernate_sequences WHERE sequence_name = 'Defaults';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'Defaults', COALESCE(MAX(id) + 1, 1) FROM Defaults;
