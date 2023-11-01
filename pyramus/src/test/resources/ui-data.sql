SET foreign_key_checks = 0;

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
  GradingScale (id, archived, name, description, version)
values  
  (1, false, 'test scale #1', 'grading scale for testing #1', 1),
  (2, false, 'test scale #2', 'grading scale for testing #2', 1);

insert into 
  Grade (id, GPA, archived, name, description, passingGrade, qualification, gradingScale, indexColumn, version)
values  
  (1, 10, false, 'test grade #1', 'grade for testing #1', false, 'qualification #1', 1, 1, 1),
  (2, 20, false, 'test grade #2', 'grade for testing #2', true, 'qualification #2', 1, 2, 1);

insert into 
  InternalAuth (password, username, id, version) 
values 
('b9a58c1892016f48e330ce62010609c4', 'devadmin', 1, 0),
('b9a58c1892016f48e330ce62010609c4', 'tonyt', 2, 0);

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
  (10, 'Test Student #1', 1);

insert into 
  Person (id, version, birthday, sex, socialSecurityNumber, basicInfo, secureInfo, defaultUser_id)
values 
  (1, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '123411-7890', 'Test staff #1', false, 1),
  (2, 1, STR_TO_DATE('1 1 1970', '%d %m %Y'), 'MALE', '012345535-8901', 'Test staff #2', false, 2),
  (3, 1, STR_TO_DATE('1 1 1990', '%d %m %Y'), 'FEMALE', '123456-7890', 'Test student #1', false, 3),
  (4, 1, STR_TO_DATE('1 1 1990', '%d %m %Y'), 'MALE', '01234567-8901', 'Test student #2', false, 4),
  (5, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121213-7890', 'Test User #1', false, 5),
  (6, 1, STR_TO_DATE('1 1 1970', '%d %m %Y'), 'MALE', '131214-8901', 'Test Manager #1', false, 6),
  (7, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121216-7891', 'Test administrator #1', false, 7),
  (8, 1, STR_TO_DATE('1 1 1980', '%d %m %Y'), 'FEMALE', '121217-7892', 'Test Student #1', false, 8);
  
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
  (8, 8, 'Tony', 'Tester', 10, 1, false);

update Person p
set p.defaultUser_id = p.id;

SET foreign_key_checks = 1;

insert into
  UserIdentification (id, externalId, authSource, person_id)
values
  (1, '1', 'internal', 7),
  (2, '2', 'internal', 8);

insert into
  StaffMember (id, title, enabled)
values 
  (1, null, true),
  (2, null, true),
  (5, null, true),
  (6, null, true),
  (7, null, true);
  
insert into
  StaffMember (staffMember_id, role)
values 
  (1, 'GUEST'),
  (2, 'GUEST'),
  (5, 'USER'),
  (6, 'MANAGER'),
  (7, 'ADMINISTRATOR');

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
  (3, false, 'TST3', 'Test Subject #3', 1, 2);
  
insert into
  CourseState (id, archived, name, version)
values 
  (1, false, 'Planning', 1),
  (2, false, 'In Progress', 1),
  (3, false, 'Ended', 1);
  
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
  (2, false, 40, 'Week', 'p', 1);

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
  ContactType (id, name, version, nonUnique, archived)
values 
  (1, 'Home', 1, false, false);

insert into 
  Address (id, city, country, postalCode, streetAddress, name, contactInfo, contactType, indexColumn, defaultAddress, version)
values 
  (1, 'Eastbury', 'Senegal', '76763-3962', '2636 Johnston Harbors', null, 1, 1, 0, true, 1),
  (2, 'Zambia', 'Portborough', '02531-1064', '430 Vesta Inlet', null, 2, 1, 0, true, 1);
  
insert into 
  Email (id, address, defaultAddress, contactInfo, contactType, indexColumn, version)
values 
  (1, 'school1@bogusmail.com', true, 1, 1, 0, 1),
  (2, 'school2@bogusmail.com', true, 2, 1, 0, 1);
  
insert into 
  PhoneNumber (id, number, defaultNumber, contactInfo, contactType, indexColumn, version)
values 
  (1, '+123 45 678 9012', true, 1, 1, 0, 1),
  (2, '+234 56 789 0123', true, 2, 1, 0, 1);
  
insert into
  ContactURLType (id, name, version, archived)
values 
  (1, 'WWW', 1, false);
    
insert into 
  ContactURL (id, url, contactInfo, contactURLType, indexColumn, version)
values 
  (1, 'http://www.school1webpage.com', 1, 1, 0, 1),
  (2, 'http://www.school2webpage.com', 2, 1, 0, 1);
  
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
  (1, '1', 'Municipality #1', 1, false),
  (2, '2', 'Municipality #2', 1, false);  

insert into 
  Nationality (id, code, name, version, archived)
values 
  (1, '1', 'Nationality #1', 1, false),
  (2, '2', 'Nationality #2', 1, false);  

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
  StudentGroup (id, name, description, creator, lastModifier, beginDate, created, lastModified, version, archived, guidanceGroup)
values 
  (1, 'StudentGroup #1', 'Group of students #1', 1, 1, STR_TO_DATE('1 1 2010', '%d %m %Y'), STR_TO_DATE('2 2 2010', '%d %m %Y'), STR_TO_DATE('3 3 2010', '%d %m %Y'), 1, false, false),
  (2, 'StudentGroup #2', 'Group of students #2', 1, 1, STR_TO_DATE('4 4 2010', '%d %m %Y'), STR_TO_DATE('5 5 2010', '%d %m %Y'), STR_TO_DATE('6 6 2010', '%d %m %Y'), 1, false, false);  
  
insert into
  StudentActivityType (id, name, version, archived)
values  
  (1, 'StudentActivityType #1', 1, false),
  (2, 'StudentActivityType #2', 1, false);

insert into StudentStudyEndReason 
  (id, name, parentReason, archived, version)
values 
  (1, 'StudentStudyEndReason #1', null, false, 1),
  (2, 'StudentStudyEndReason #2', 1, false, 1);
  
insert into ClientApplication 
  (id, clientName, clientId, clientSecret, skipPrompt)
values 
  (1, 'dev.muikku.fi', '854885cf-2284-4b17-b63c-a8b189535f8d' ,'cqJ4J1if8ca5RMUqaYyFPYToxfFxt2YT8PXL3pNygPClnjJdt55lrFs6k1SZ6colJN24YEtZ7bhFW29S', 0),
  (2, 'dev.suikku.fi', '567765cf-1114-4b17-b63c-awbe89535f8d' ,'cqJ4J1if8ca5RMUqaYyFPYToxfFxt2YT8PXL3pNygPClnjJdt55lrFs6k1SZ6colJN24YEtZ7bhasAS5', 1);

insert into 
  Student (id, studyProgramme, nickname, previousStudies, studyStartDate, 
    additionalInfo, activityType, educationalLevel, language, municipality, nationality, school, 
    examinationType, education, curriculum_id)
values 
  (8, 1, 'TEST-User', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test test', 1, 1, 1, 1, 1, 1, 1, 'Education smthg', null),
  (3, 1, 'Tankky', 0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test test test', 1, 1, 1, 1, 1, 1, 1, 'Education', null);

insert into StudentGroupStudent
  (id, studentGroup, student, version)
values 
  (1, 1, 8, 1);
 
insert into
  CourseUser (id, version, course)
values
  (1, 0, 1000),
  (2, 0, 1001); 

insert into 
  CourseStudent (archived, enrolmentTime, lodging, optionality, id, billingDetails, enrolmentType, 
  participationType, student)
values 
  (0, STR_TO_DATE('1 1 2010', '%d %m %Y'), 0, 'OPTIONAL', 1, NULL, NULL, 1, 8);

insert into 
  StudentContactLogEntry (id, creatorName, entryDate, text, type, student, version, archived)
values
  (1, 'Tester #1', STR_TO_DATE('1 1 2010', '%d %m %Y'), 'Test text #1', 'LETTER', 8, 1, false),
  (2, 'Tester #2', STR_TO_DATE('1 1 2011', '%d %m %Y'), 'Test text #2', 'PHONE', 8, 1, false);

insert into Defaults 
  (id, educationalTimeUnit, courseState, version, courseParticipationType, courseEnrolmentType, organization, studentDefaultContactType, userDefaultContactType)
values
  (1, 1, 1, 1, 1, 1, 1, 1, 1);

INSERT INTO 
  MagicKey (created, name, id, version, scope) 
VALUES ('2014-09-08 12:33:50', '0793c5ee-3283-4628-b3b9-746da4e96d5f', 4, 0, 'APPLICATION');

INSERT INTO
  PluginRepository (id, url, repositoryId)
VALUES 
  (1, 'https://nexus.muikkuverkko.fi/repository/otavanopisto-snapshots/', '');

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
DELETE FROM hibernate_sequences WHERE sequence_name = 'MagicKey';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'MagicKey', COALESCE(MAX(id) + 1, 1) FROM MagicKey;
DELETE FROM hibernate_sequences WHERE sequence_name = 'PluginRepository';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'PluginRepository', COALESCE(MAX(id) + 1, 1) FROM PluginRepository;
DELETE FROM hibernate_sequences WHERE sequence_name = 'InternalAuth';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value)  
  SELECT 'InternalAuth', COALESCE(MAX(id) + 1, 1) FROM InternalAuth;
DELETE FROM hibernate_sequences WHERE sequence_name = 'UserIdentification';
INSERT INTO hibernate_sequences (sequence_name, sequence_next_hi_value) 
  SELECT 'UserIdentification', COALESCE(MAX(id) + 1, 1) FROM UserIdentification;

