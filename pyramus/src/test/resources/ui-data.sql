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

SET foreign_key_checks = 0;

insert into 
  Person (id, version, birthday, sex, socialSecurityNumber, basicInfo, secureInfo, defaultUser_id)
values 
  (1, 1, PARSEDATETIME('1 1 1980', 'd M yyyy'), 'FEMALE', '123411-7890', 'Test staff #1', false, 1),
  (2, 1, PARSEDATETIME('1 1 1970', 'd M yyyy'), 'MALE', '012345535-8901', 'Test staff #2', false, 2),
  (3, 1, PARSEDATETIME('1 1 1990', 'd M yyyy'), 'FEMALE', '123456-7890', 'Test student #1', false, 3),
  (4, 1, PARSEDATETIME('1 1 1990', 'd M yyyy'), 'MALE', '01234567-8901', 'Test student #2', false, 4),
  (5, 1, PARSEDATETIME('1 1 1980', 'd M yyyy'), 'FEMALE', '121213-7890', 'Test User #1', false, 5),
  (6, 1, PARSEDATETIME('1 1 1970', 'd M yyyy'), 'MALE', '131214-8901', 'Test Manager #1', false, 6),
  (7, 1, PARSEDATETIME('1 1 1980', 'd M yyyy'), 'FEMALE', '121216-7891', 'Test administrator #1', false, 7),
  (8, 1, PARSEDATETIME('1 1 1980', 'd M yyyy'), 'FEMALE', '121217-7892', 'Test Student #1', false, 8);
  
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
  StaffMember (id, role, title)
values 
  (1, 'GUEST', null),
  (2, 'GUEST', null),
  (5, 'USER', null),
  (6, 'MANAGER', null),
  (7, 'ADMINISTRATOR', null);
  
insert into 
  AcademicTerm (id, name, startDate, endDate, archived, version)
values 
  (1, 'fall', PARSEDATETIME('1 8 2014', 'd M yyyy'), PARSEDATETIME('23 12 2014', 'd M yyyy'), false, 0),
  (2, 'spring', PARSEDATETIME('4 1 2015', 'd M yyyy'), PARSEDATETIME('30 5 2015', 'd M yyyy'), false, 0);
  
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
  CourseBase (id, name, archived, courseNumber, created, lastModified, description, courseLength, creator, lastModifier, subject, version, maxParticipantCount)
values
  (1, 'Test Module #1', false, 1, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Module #1 for testing', 1, 1, 1, 1, 1, 100);

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
  CourseBase (id, name, archived, courseNumber, created, lastModified, description, courseLength, creator, lastModifier, subject, version, maxParticipantCount)
values
  (1000, 'Test Course #1', false, 1, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Course #1 for testing', 1, 1, 1, 1, 1, 100),
  (1001, 'Test Course #2', false, 2, PARSEDATETIME('1 1 2011', 'd M yyyy'), PARSEDATETIME('1 1 2011', 'd M yyyy'), 'Course #2 for testing', 1, 1, 1, 1, 1, 200);
  
insert into 
  Course (id, beginDate, endDate, localTeachingDays, nameExtension, module, state, teachingHours, distanceTeachingDays, planningHours, assessingHours, enrolmentTimeEnd, courseTemplate)
values 
  (1000, PARSEDATETIME('2 2 2010', 'd M yyyy'), PARSEDATETIME('3 3 2010', 'd M yyyy'), 10, 'Ext', 1, 1, 40, 30, 20, 10, PARSEDATETIME('1 1 2010', 'd M yyyy'), false),
  (1001, PARSEDATETIME('2 2 2011', 'd M yyyy'), PARSEDATETIME('3 3 2011', 'd M yyyy'), 20, 'ABC', 1, 2, 15, 17, 20, 10, PARSEDATETIME('1 1 2011', 'd M yyyy'), false);

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
  (1, 'Test Project #1', 'Project for testing', 7, 1, PARSEDATETIME('6 6 2010', 'd M yyyy'), PARSEDATETIME('6 6 2010', 'd M yyyy'), 1, false, 1),
  (2, 'Test Project #2', 'Project for testing', 8, 1, PARSEDATETIME('6 6 2011', 'd M yyyy'), PARSEDATETIME('6 6 2011', 'd M yyyy'), 1, false, 1);
  
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
  (1, 'StudentGroup #1', 'Group of students #1', 1, 1, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('2 2 2010', 'd M yyyy'), PARSEDATETIME('3 3 2010', 'd M yyyy'), 1, false, false),
  (2, 'StudentGroup #2', 'Group of students #2', 1, 1, PARSEDATETIME('4 4 2010', 'd M yyyy'), PARSEDATETIME('5 5 2010', 'd M yyyy'), PARSEDATETIME('6 6 2010', 'd M yyyy'), 1, false, false);  
  
insert into
  StudentActivityType (id, name, version, archived)
values  
  (1, 'StudentActivityType #1', 1, false),
  (2, 'StudentActivityType #2', 1, false);

insert into StudentStudyEndReason 
  (id, name, parentReason, version)
values 
  (1, 'StudentStudyEndReason #1', null ,1),
  (2, 'StudentStudyEndReason #2', 1 ,1);
  
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
  (8, 1, 'TEST-User', 0, PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Test test', 1, 1, 1, 1, 1, 1, 1, 'Education smthg', null);

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
  (0, PARSEDATETIME('1 1 2010', 'd M yyyy'), 0, 'OPTIONAL', 1, NULL, NULL, 1, 8);

insert into 
  StudentContactLogEntry (id, creatorName, entryDate, text, type, student, version, archived)
values
  (1, 'Tester #1', PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Test text #1', 'LETTER', 8, 1, false),
  (2, 'Tester #2', PARSEDATETIME('1 1 2011', 'd M yyyy'), 'Test text #2', 'PHONE', 8, 1, false);

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

insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'InternalAuth', max(id) + 1 from InternalAuth;  
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'User', max(id) + 1 from User;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'EducationType', max(id) + 1 from EducationType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Subject', max(id) + 1 from Subject;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseState', max(id) + 1 from CourseState;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseEnrolmentType', max(id) + 1 from CourseEnrolmentType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseParticipationType', max(id) + 1 from CourseParticipationType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'EducationalTimeUnit', max(id) + 1 from EducationalTimeUnit;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseBase', max(id) + 1 from CourseBase;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'EducationalLength', max(id) + 1 from EducationalLength;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ComponentBase', max(id) + 1 from ComponentBase;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseDescriptionCategory', max(id) + 1 from CourseDescriptionCategory;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'AcademicTerm', max(id) + 1 from AcademicTerm;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'SettingKey', max(id) + 1 from SettingKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Setting', max(id) + 1 from Setting;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'EducationSubtype', max(id) + 1 from EducationSubtype;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'GradingScale', max(id) + 1 from GradingScale;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Grade', max(id) + 1 from Grade;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Project', max(id) + 1 from Project;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ProjectModule', max(id) + 1 from ProjectModule;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'SchoolField', max(id) + 1 from SchoolField;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ContactInfo', max(id) + 1 from ContactInfo;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ContactType', max(id) + 1 from ContactType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Address', max(id) + 1 from Address;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Email', max(id) + 1 from Email;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'PhoneNumber', max(id) + 1 from PhoneNumber;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ContactURLType', max(id) + 1 from ContactURLType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ContactURL', max(id) + 1 from ContactURL;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'School', max(id) + 1 from School;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'SchoolVariableKey', max(id) + 1 from SchoolVariableKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'SchoolVariable', max(id) + 1 from SchoolVariable;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'UserVariableKey', max(id) + 1 from UserVariableKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseBaseVariableKey', max(id) + 1 from CourseBaseVariableKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Language', max(id) + 1 from Language;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Municipality', max(id) + 1 from Municipality;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Nationality', max(id) + 1 from Nationality;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentActivityType', max(id) + 1 from StudentActivityType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentEducationalLevel', max(id) + 1 from StudentEducationalLevel;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentExaminationType', max(id) + 1 from StudentExaminationType;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudyProgrammeCategory', max(id) + 1 from StudyProgrammeCategory;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudyProgramme', max(id) + 1 from StudyProgramme;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentGroup', max(id) + 1 from StudentGroup;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentStudyEndReason', max(id) + 1 from StudentStudyEndReason;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentContactLogEntry', max(id) + 1 from StudentContactLogEntry;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Person', max(id) + 1 from Person;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StaffMember', max(id) + 1 from StaffMember;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ClientApplication', max(id) + 1 from ClientApplication;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentGroupStudent', max(id) + 1 from StudentGroupStudent;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Student', max(id) + 1 from Student;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseUser', max(id) + 1 from CourseUser;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseStudent', max(id) + 1 from CourseStudent;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'PluginRepository', max(id) + 1 from PluginRepository;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'MagicKey', max(id) + 1 from MagicKey;
