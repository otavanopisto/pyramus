({
  'run' : function(api){
    var studentCount = 2000;
    var studentGroupCount = 100;
    var faker = api.getFaker('fi');
    
    function zeroPad(v, l) {
      var r = new String(v)
      while (r.length < l) {
        r = '0' + r;
      }      
      return r;
    };
    
    function createEmail(firstName, lastName) {
      var domain = new String(faker.internet().emailAddress());
      return new String((firstName.toLowerCase() + '.' + lastName.toLowerCase())).replace(/[^a-z\.\@]/g, '') + '@' + domain.substring(domain.indexOf('@') + 1);
    }
    
    api.log('Importing students...');
    
    var studyProgrammes = api.studyProgrammes.listIds();
    var studentGroups = [];
    var courses = api.courses.listIds();
    
    var emailContactTypes = api.contactTypes.listIdsByName('Koti');
    var emailContactType = emailContactTypes.length > 0 ? emailContactTypes[0] : null; 

    if (!emailContactType) {
      emailContactType = api.contactTypes.create('Koti');
    }
    
    for (var i = 0, l = studentGroupCount; i < l; i++) {
      studentGroups.push(
        api.studentGroups.create("Ohjaajan '" + faker.name().firstName() + "' ryhmä", 'Generated test group', null)
      );
    }
    
    while (studentCount > 0) {
      var birthday = new Date(Math.round(Math.random() * 86400000 * 365 * 40));
      var year = birthday.getFullYear();
      var month = birthday.getMonth() + 1;
      var day = birthday.getDate();
      var centuryIndicator = year < 1900 ? '+' : year < 2000 ? '-' : 'A';
      var twoDigitYear = year < 1900 ? year - 1800 : year < 2000 ? year - 1900 : year - 2000;
      var id = Math.round(Math.random() * 898) + 1;
      var checksumMod = (id % 31);
      var checksum = checksumMod < 10 ? new String(checksumMod) : String.fromCharCode(checksumMod + 55);
      var sex = (id % 2 === 0) ? 'f' : 'm';
        
      var ssn = 
        zeroPad(day, 2) + 
        zeroPad(month, 2) + 
        zeroPad(twoDigitYear, 2) +
        centuryIndicator + 
        zeroPad(id, 3) +
        checksum;
      
      var personId = api.persons.create(birthday, ssn, sex, 'Generated test student', false);
      var studyProgrammeCount = Math.round(Math.random() * 2) + 1;
      
      var firstName = faker.name().firstName();
      var lastName = faker.name().lastName();
      var email = createEmail(firstName, lastName);
      var nickname = null;
      var additionalInfo = null;
      var studyTimeEnd = null;
      var activityType = null;
      var examinationType = null;
      var educationalLevel = null;
      var education = null;
      var nationality = null;
      var municipality = null;
      var language = null;
      var school = null;
      var previousStudies = null;
      var lodging = false;
      
      while (studyProgrammeCount > 0) {
        var studyProgramme = studyProgrammes[Math.round(Math.random() * (studyProgrammes.length - 1))];
        var studentGroup = studentGroups[Math.round(Math.random() * (studentGroups.length - 1))];
        
        var studyStartDate = new Date(new Date(2010, 1, 1).getTime() + Math.round(Math.random() * 86400000 * 365 * 4));
        var studyEndDate = null;
        var studyEndReason = null;
        var studyEndText = null;
        
        var studentId = api.students.create(personId, firstName, lastName, email, emailContactType, nickname, additionalInfo, studyTimeEnd, activityType,
          examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme,
          previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText, lodging);
        
        if (!api.studentGroupStudents.findIdByStudentAndStudentGroup(studentId, studentGroup)) {
          api.studentGroupStudents.create(studentId, studentGroup);
        }
        
        var courseCount = (Math.random() * 10);
        while (courseCount > 0) {
          var courseId = courses[Math.round(Math.random() * (courses.length - 1))];
          
          if (!api.courseStudents.findIdByCourseAndStudent(courseId, studentId)) {
            api.courseStudents.create(courseId, studentId);
          }
          
          courseCount--;
        }
        
        studyProgrammeCount--;
      }
      
      studentCount--;
      
      if ((studentCount % 10) == 0) {
        api.log('Students remaining: ' + studentCount);
      }
    }
  }
})