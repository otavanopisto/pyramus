insert into 
  SettingKey (id, name)
values 
  (1, 'system.environment');
  
insert into 
  Setting (id, settingKey, value)
values 
  (1, 1, 'it');

insert into
  User (id, authProvider, externalId, firstName, lastName, role, contactInfo, version, title)
values 
  (1, 'TEST', 'TEST-GUEST-1', 'Test Guest', 'User #1', 'GUEST', null, 1, null);
  
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
  Subject (id, archived, code, name, version, educationType)
values 
  (1, false, 'TEST', 'Test Subject', 1, 1);
  
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
  EducationalTimeUnit (id, archived, baseUnits, name, version)
values 
  (1, false, 1, 'Hour', 1);

insert into
  EducationalLength (id, units, unit, version)
values 
  (1, 1, 1, 1),
  (2, 123, 1, 1),
  (3, 234, 1, 1),
  (4, 345, 1, 1),
  (5, 456, 1, 1),
  (6, 567, 1, 1);
  
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
  Course (id, beginDate, endDate, localTeachingDays, nameExtension, module, state, teachingHours, distanceTeachingDays, planningHours, assessingHours, enrolmentTimeEnd)
values 
  (1000, PARSEDATETIME('2 2 2010', 'd M yyyy'), PARSEDATETIME('3 3 2010', 'd M yyyy'), 10, 'Ext', 1, 1, 40, 30, 20, 10, PARSEDATETIME('1 1 2010', 'd M yyyy')),
  (1001, PARSEDATETIME('2 2 2011', 'd M yyyy'), PARSEDATETIME('3 3 2011', 'd M yyyy'), 20, 'ABC', 1, 2, 15, 17, 20, 10, PARSEDATETIME('1 1 2011', 'd M yyyy'));

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







