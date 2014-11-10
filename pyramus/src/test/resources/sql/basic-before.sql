INSERT INTO 
  Defaults (courseState, id, educationalTimeUnit, version, courseParticipationType, courseEnrolmentType) 
VALUES 
  (2, 1, 1, 3, 1, null);

INSERT INTO 
  MagicKey (created, name, id, version, scope) 
VALUES ('2014-09-08 12:33:50', '0793c5ee-3283-4628-b3b9-746da4e96d5f', 4, 0, 'APPLICATION');

INSERT INTO
  PluginRepository (id, url, repositoryId)
VALUES 
  (1, 'http://repo1.maven.org/maven2', '');
  
insert into 
  ContactInfo (id, additionalInfo, version)
values   
  (3, 'For test student #3', 1),
  (4, 'For test student #4', 1);
  
insert into 
  Address (id, city, country, postalCode, streetAddress, name, contactInfo, contactType, indexColumn, defaultAddress, version)
values 
  (3, 'Southshire', 'Yemen', '17298', '6967 Bailee Mission', null, 3, 1, 0, true, 1),
  (4, 'Northchester', 'Cuba', '97733', '556 Lupe Mountains', null, 4, 1, 0, true, 1);

insert into 
  Email (id, address, defaultAddress, contactInfo, contactType, indexColumn, version)
values 
  (3, 'student1@bogusmail.com', true, 3, 1, 0, 1),
  (4, 'student2@bogusmail.com', true, 4, 1, 0, 1);

insert into 
  PhoneNumber (id, number, defaultNumber, contactInfo, contactType, indexColumn, version)
values 
  (3, '+456 78 901 2345', true, 3, 1, 0, 1),
  (4, '+567 89 012 3456', true, 4, 1, 0, 1);
    
insert into 
  ContactURL (id, url, contactInfo, contactURLType, indexColumn, version)
values 
  (3, 'http://www.student1webpage.com', 3, 1, 0, 1),
  (4, 'http://www.student2webpage.com', 4, 1, 0, 1);
  
insert into 
  AbstractStudent (id, birthday, sex, socialSecurityNumber, basicInfo, secureInfo, version)
values 
  (1, PARSEDATETIME('1 1 1990', 'd M yyyy'), 'FEMALE', '123456-7890', 'Test student #1', false, 1),
  (2, PARSEDATETIME('1 1 1990', 'd M yyyy'), 'MALE', '01234567-8901', 'Test student #2', false, 1);
  
insert into 
  User (id, authProvider, externalId, role, firstName, lastName, contactInfo, version)
values 
  (3, 'internal', '-1', 'STUDENT', 'Tanya', 'Test #1', 3, 1),
  (4, 'internal', '-1', 'STUDENT', 'David', 'Test #2', 4, 1);
  
insert into 
  Student (id, abstractStudent, studyProgramme, nickname, previousStudies, studyStartDate, 
    additionalInfo, activityType, educationalLevel, language, municipality, nationality, school, 
    examinationType, education, lodging, archived)
values 
  (3, 1, 1, 'Tanya-T', 0, PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Testing #1', 1, 1, 1, 1, 1, 1, 1, 'Education #1', false, false),
  (4, 2, 1, 'David-T', 0, PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Testing #2', 1, 1, 1, 1, 1, 1, 1, 'Education #2', false, false);

insert into
  CourseUser (id, version, course)
values
  (1, 0, 1000),
  (2, 0, 1001);  
  
insert into 
  CourseStudent (archived, enrolmentTime, lodging, optionality, id, billingDetails, enrolmentType, 
  participationType, student)
values 
  (0, PARSEDATETIME('1 1 2010', 'd M yyyy'), 0, 'OPTIONAL', 1, NULL, NULL, 1, 3),
  (0, PARSEDATETIME('1 1 2011', 'd M yyyy'), 0, 'OPTIONAL', 2, NULL, NULL, 1, 4);

insert into StudentGroupStudent
  (id, studentGroup, student, version)
values 
  (1, 1, 3, 1),
  (2, 1, 4, 1);
  
insert into 
  StudentContactLogEntry (id, creatorName, entryDate, text, type, student, version, archived)
values
  (1, 'Tester #1', PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Test text #1', 'LETTER', 3, 1, false),
  (2, 'Tester #2', PARSEDATETIME('1 1 2011', 'd M yyyy'), 'Test text #2', 'PHONE', 3, 1, false);
  
delete from hibernate_sequences where sequence_name in ('User', 'ContactInfo', 'Address', 'Email', 'PhoneNumber', 'StudyProgramme', 
  'StudentGroup', 'StudentGroupStudent', 'AbstractStudent', 'Student', 'CourseUser', 'CourseStudent');
  
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'PluginRepository', max(id) + 1 from PluginRepository;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'MagicKey', max(id) + 1 from MagicKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Defaults', max(id) + 1 from Defaults;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'User', max(id) + 1 from User;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'ContactInfo', max(id) + 1 from ContactInfo;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Address', max(id) + 1 from Address;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Email', max(id) + 1 from Email;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'PhoneNumber', max(id) + 1 from PhoneNumber;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudyProgramme', max(id) + 1 from StudyProgramme;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentGroup', max(id) + 1 from StudentGroup;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'StudentGroupStudent', max(id) + 1 from StudentGroupStudent;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'AbstractStudent', max(id) + 1 from AbstractStudent;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Student', max(id) + 1 from Student;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseUser', max(id) + 1 from CourseUser;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'CourseStudent', max(id) + 1 from CourseStudent;
