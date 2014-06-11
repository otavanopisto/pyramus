insert into
  User (id, authProvider, externalId, firstName, lastName, role, contactInfo, version, title)
values 
  (1, 'TEST', 'TEST-GUEST-1', 'Test Guest', 'User #1', 'GUEST', null, 1, null);
  
insert into
  EducationType (id, archived, code, name, version)
values
  (1, false, 'TEST', 'Test Education Type', 1);
  
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
  EducationalTimeUnit (id, archived, baseUnits, name, version)
values 
  (1, false, 1, 'Hour', 1);

insert into
  EducationalLength (id, units, unit, version)
values 
  (1, 1, 1, 1);
  
insert into 
  CourseBase (id, name, archived, courseNumber, created, lastModified, description, courseLength, creator, lastModifier, subject, version, maxParticipantCount)
values
  (1, 'Test Module #1', false, 1, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Module #1 for testing', 1, 1, 1, 1, 1, 100);

insert into
  Module (id)
values 
  (1);
  
insert into 
  CourseBase (id, name, archived, courseNumber, created, lastModified, description, courseLength, creator, lastModifier, subject, version, maxParticipantCount)
values
  (1000, 'Test Course #1', false, 1, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'Course #1 for testing', 1, 1, 1, 1, 1, 100),
  (1001, 'Test Course #2', false, 2, PARSEDATETIME('1 1 2011', 'd M yyyy'), PARSEDATETIME('1 1 2011', 'd M yyyy'), 'Course #2 for testing', 1, 1, 1, 1, 1, 200);
  
insert into 
  Course (id, beginDate, endDate, localTeachingDays, nameExtension, module, state, teachingHours, distanceTeachingDays, planningHours, assessingHours, enrolmentTimeEnd)
values 
  (1000, PARSEDATETIME('2 2 2010', 'd M yyyy'), PARSEDATETIME('3 3 2010', 'd M yyyy'), 10, 'Ext', 1, 1, 40, 30, 20, 10, PARSEDATETIME('1 1 2010', 'd M yyyy')),
  (1001, PARSEDATETIME('2 2 2011', 'd M yyyy'), PARSEDATETIME('3 3 2011', 'd M yyyy'), 20, 'ABC', 1, 2, 15, 17, 20, 10, PARSEDATETIME('1 1 2010', 'd M yyyy'));
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  