({
    'run': function(api) {
        function lipsum() {
            return api.faker.sentence(3);
        }
        function city() {
            return api.faker.cityPrefix() + api.faker.citySuffix();
        }
        var bundles = []
        for (var i=0; i < 4; i++) {
            var schoolField = api.student.createSchoolField(lipsum());
            var educationType = api.student.createEducationType(lipsum(), ""+((Math.random()*1000) | 0));
            var activityType = api.student.createActivityType(lipsum());
            var examinationType = api.student.createExaminationType(lipsum());
            var educationalLevel = api.student.createEducationalLevel(lipsum());
            var nationality = api.student.createNationality(api.faker.country(), ""+((Math.random()*1000) | 0));
            var language = api.student.createLanguage(api.faker.country(), ""+((Math.random()*1000) | 0));
            var municipality = api.student.createMunicipality(city(), ""+((Math.random()*1000) | 0));
            var school = api.student.createSchool(""+(Math.random()*1000 | 0), lipsum(), schoolField);
            var studyProgrammeCategory = api.student.createStudyProgrammeCategory(lipsum(), educationType);
            var studyProgramme = api.student.createStudyProgramme(lipsum(), studyProgrammeCategory, ""+((Math.random()*1000) | 0));
            var studyEndReason = null;
            api.log(lipsum());
            api.log(activityType);
            api.log(examinationType);
            api.log(educationalLevel);
            api.log(nationality);
            api.log(language);
            api.log(municipality);
            api.log(schoolField);
            api.log(school);
            api.log(educationType);
            api.log(studyProgramme);
            api.log(studyProgrammeCategory);
            api.log(studyEndReason);
            bundles.push({
            	schoolField: schoolField,
            	educationType: educationType,
            	activityType: activityType,
            	examinationType: examinationType,
            	educationalLevel: educationalLevel,
            	nationality: nationality,
            	language: language,
            	municipality: municipality,
            	schoolField: schoolField,
            	school: school,
            	educationType: educationType,
            	studyProgramme: studyProgramme,
            	studyProgrammeCategory: studyProgrammeCategory,
            	studyEndReason: studyEndReason
            });
        }
        
        for (var i=0; i < 10; i++) {
            var bundle = bundles[((Math.random() * 4) | 0)];
            var abstractStudent = api.student.createAbstractStudent(
                        new Date((Math.random() * 1396960893)),
                        ""+((Math.random()*100000)|0),
                        (Math.random() > 0.5 ? "m" : "f"),
                        lipsum(),
                        false);
            for (var j = 0; j < 2; j++) {
                api.student.createStudent(
                        abstractStudent,
                        api.faker.firstName(),
                        api.faker.lastName(),
                        "",
                        lipsum(),
                        new Date((Math.random() * 1396960893)),
                        bundle.activityType,
                        bundle.examinationType,
                        bundle.educationalLevel,
                        lipsum(),
                        bundle.nationality,
                        bundle.municipality,
                        bundle.language,
                        bundle.school,
                        bundle.studyProgramme,
                        0.0,
                        new Date((Math.random() * 1396960893)),
                        new Date((Math.random() * 1396960893)),
                        bundle.studyEndReason,
                        "",
                        false);
            }
        }
    }
})



































