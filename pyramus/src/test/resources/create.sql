    create table AbstractStudent (
        id bigint not null,
        basicInfo clob,
        birthday date,
        secureInfo boolean not null,
        sex varchar(255),
        socialSecurityNumber varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table AcademicTerm (
        id bigint not null,
        archived boolean not null,
        endDate date not null,
        name varchar(255) not null,
        startDate date not null,
        version bigint not null,
        primary key (id)
    );

    create table Address (
        id bigint not null,
        city varchar(255),
        country varchar(255),
        defaultAddress boolean not null,
        name varchar(255),
        postalCode varchar(255),
        streetAddress varchar(255),
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    );

    create table BasicCourseResource (
        id bigint not null,
        hourlyCost_amount double,
        hourlyCost_currency varchar(255),
        hours double not null,
        unitCost_amount double,
        unitCost_currency varchar(255),
        units integer not null,
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    );

    create table BillingDetails (
        id bigint not null,
        bic varchar(255),
        city varchar(255),
        companyIdentifier varchar(255),
        companyName varchar(255),
        country varchar(255),
        emailAddress varchar(255),
        iban varchar(255),
        personName varchar(255),
        phoneNumber varchar(255),
        postalCode varchar(255),
        referenceNumber varchar(255),
        region varchar(255),
        streetAddress1 varchar(255),
        streetAddress2 varchar(255),
        primary key (id)
    );

    create table ChangeLogEntry (
        id bigint not null,
        entityId varchar(255),
        time timestamp,
        type varchar(255) not null,
        entity bigint,
        user bigint,
        primary key (id)
    );

    create table ChangeLogEntryEntity (
        id bigint not null,
        name varchar(255) unique,
        primary key (id)
    );

    create table ChangeLogEntryEntityProperty (
        id bigint not null,
        name varchar(255),
        entity bigint,
        primary key (id)
    );

    create table ChangeLogEntryProperty (
        id bigint not null,
        value clob,
        entry bigint,
        property bigint,
        primary key (id)
    );

    create table ComponentBase (
        id bigint not null,
        archived boolean not null,
        description clob,
        name varchar(255) not null,
        version bigint not null,
        length bigint,
        primary key (id)
    );

    create table ContactInfo (
        id bigint not null,
        additionalInfo clob,
        version bigint not null,
        primary key (id)
    );

    create table ContactType (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table ContactURL (
        id bigint not null,
        url varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        contactURLType bigint,
        indexColumn integer,
        primary key (id)
    );

    create table ContactURLType (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table Course (
        assessingHours double,
        beginDate date,
        distanceTeachingDays double,
        endDate date,
        enrolmentTimeEnd timestamp,
        localTeachingDays double,
        nameExtension varchar(255),
        planningHours double,
        teachingHours double,
        id bigint not null,
        module bigint,
        state bigint,
        primary key (id)
    );

    create table CourseAssessment (
        id bigint not null,
        courseStudent bigint unique,
        primary key (id)
    );

    create table CourseAssessmentRequest (
        id bigint not null,
        archived boolean not null,
        created timestamp not null,
        requestText clob,
        courseStudent bigint,
        primary key (id)
    );

    create table CourseBase (
        id bigint not null,
        archived boolean not null,
        courseNumber integer,
        created timestamp not null,
        description varchar(255),
        lastModified timestamp not null,
        maxParticipantCount bigint,
        name varchar(255) not null,
        version bigint not null,
        courseLength bigint,
        creator bigint,
        lastModifier bigint,
        subject bigint,
        primary key (id)
    );

    create table CourseBaseVariable (
        id bigint not null,
        value varchar(255),
        version bigint not null,
        courseBase bigint,
        variableKey bigint,
        primary key (id)
    );

    create table CourseBaseVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table CourseComponent (
        id bigint not null,
        course bigint,
        indexColumn integer,
        primary key (id)
    );

    create table CourseComponentResource (
        id bigint not null,
        usagePercent double not null,
        courseComponent bigint,
        resource bigint,
        primary key (id)
    );

    create table CourseDescription (
        id bigint not null,
        description varchar(2147483647),
        category bigint,
        courseBase bigint,
        primary key (id)
    );

    create table CourseDescriptionCategory (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table CourseEducationSubtype (
        id bigint not null,
        version bigint not null,
        courseEducationType bigint,
        educationSubtype bigint,
        primary key (id)
    );

    create table CourseEducationType (
        id bigint not null,
        version bigint not null,
        courseBase bigint,
        educationType bigint,
        primary key (id)
    );

    create table CourseEnrolmentType (
        id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table CourseParticipationType (
        id bigint not null,
        archived boolean not null,
        indexColumn integer not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table CourseState (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table CourseStudent (
        id bigint not null,
        archived boolean not null,
        enrolmentTime timestamp not null,
        lodging boolean not null,
        optionality varchar(255),
        version bigint not null,
        billingDetails bigint,
        course bigint,
        enrolmentType bigint,
        participationType bigint,
        student bigint not null,
        primary key (id)
    );

    create table CourseStudentVariable (
        id bigint not null,
        value clob not null,
        version bigint not null,
        courseStudent bigint,
        variableKey bigint,
        primary key (id)
    );

    create table CourseStudentVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table CourseUser (
        id bigint not null,
        version bigint not null,
        course bigint,
        userRole bigint,
        pyramusUser bigint,
        primary key (id)
    );

    create table CourseUserRole (
        id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table Credit (
        id bigint not null,
        archived boolean not null,
        creditType varchar(255) not null,
        date timestamp not null,
        verbalAssessment clob,
        version bigint not null,
        assessingUser bigint,
        grade bigint,
        primary key (id)
    );

    create table CreditLink (
        id bigint not null,
        archived boolean not null,
        created timestamp,
        creator bigint,
        credit bigint,
        student bigint,
        primary key (id)
    );

    create table CreditVariable (
        id bigint not null,
        value clob not null,
        version bigint not null,
        credit bigint,
        variableKey bigint,
        primary key (id)
    );

    create table CreditVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table Defaults (
        id bigint not null,
        version bigint not null,
        educationalTimeUnit bigint,
        courseEnrolmentType bigint,
        courseParticipationType bigint,
        courseState bigint,
        primary key (id)
    );

    create table EducationSubtype (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    );

    create table EducationType (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table EducationalLength (
        id bigint not null,
        units double not null,
        version bigint not null,
        unit bigint,
        primary key (id)
    );

    create table EducationalTimeUnit (
        id bigint not null,
        archived boolean not null,
        baseUnits double not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table Email (
        id bigint not null,
        address varchar(255) not null,
        defaultAddress boolean not null,
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    );

    create table File (
        id bigint not null,
        archived boolean not null,
        contentType varchar(255),
        created timestamp not null,
        data blob,
        fileName varchar(255),
        lastModified timestamp not null,
        name varchar(255),
        creator bigint,
        fileType bigint,
        lastModifier bigint,
        primary key (id)
    );

    create table FileType (
        id bigint not null,
        archived boolean not null,
        name varchar(255),
        primary key (id)
    );

    create table FormDraft (
        id bigint not null,
        created timestamp,
        data clob,
        modified timestamp,
        url varchar(255),
        version bigint not null,
        creator bigint,
        primary key (id)
    );

    create table Grade (
        id bigint not null,
        GPA double,
        archived boolean not null,
        description varchar(255),
        name varchar(255) not null,
        passingGrade boolean not null,
        qualification varchar(255),
        version bigint not null,
        gradingScale bigint,
        indexColumn integer,
        primary key (id)
    );

    create table GradeCourseResource (
        id bigint not null,
        hourlyCost_amount double,
        hourlyCost_currency varchar(255),
        hours double not null,
        unitCost_amount double,
        unitCost_currency varchar(255),
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    );

    create table GradingScale (
        id bigint not null,
        archived boolean,
        description clob,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table HelpFolder (
        id bigint not null,
        primary key (id)
    );

    create table HelpItem (
        id bigint not null,
        created timestamp not null,
        indexColumn integer not null,
        lastModified timestamp not null,
        creator bigint,
        lastModifier bigint,
        parent bigint,
        primary key (id)
    );

    create table HelpItemTitle (
        id bigint not null,
        created timestamp not null,
        lastModified timestamp not null,
        locale varchar(255) not null,
        title varchar(255) not null,
        creator bigint,
        item bigint,
        lastModifier bigint,
        primary key (id)
    );

    create table HelpPage (
        id bigint not null,
        primary key (id)
    );

    create table HelpPageContent (
        id bigint not null,
        content clob not null,
        created timestamp not null,
        lastModified timestamp not null,
        locale varchar(255) not null,
        creator bigint,
        lastModifier bigint,
        page bigint,
        primary key (id)
    );

    create table InternalAuth (
        id bigint not null,
        password varchar(255) not null,
        username varchar(255) not null unique,
        version bigint not null,
        primary key (id)
    );

    create table Language (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table MagicKey (
        id bigint not null,
        created timestamp not null,
        name varchar(255) not null unique,
        scope varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table MaterialResource (
        unitCost_amount double,
        unitCost_currency varchar(255),
        id bigint not null,
        primary key (id)
    );

    create table Module (
        id bigint not null,
        primary key (id)
    );

    create table ModuleComponent (
        id bigint not null,
        module bigint,
        indexColumn integer,
        primary key (id)
    );

    create table Municipality (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table Nationality (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table OtherCost (
        id bigint not null,
        cost_amount double,
        cost_currency varchar(255),
        name varchar(255) not null,
        version bigint not null,
        course bigint,
        primary key (id)
    );

    create table PhoneNumber (
        id bigint not null,
        defaultNumber boolean not null,
        number varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    );

    create table Plugin (
        id bigint not null,
        artifactId varchar(255) not null,
        enabled boolean not null,
        groupId varchar(255) not null,
        version varchar(255) not null,
        primary key (id)
    );

    create table PluginRepository (
        id bigint not null,
        repositoryId varchar(255) not null unique,
        url varchar(255) not null unique,
        primary key (id)
    );

    create table Project (
        id bigint not null,
        archived boolean not null,
        created timestamp not null,
        description clob,
        lastModified timestamp not null,
        name varchar(255) not null,
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        optionalStudies bigint,
        primary key (id)
    );

    create table ProjectAssessment (
        id bigint not null,
        studentProject bigint,
        primary key (id)
    );

    create table ProjectModule (
        id bigint not null,
        optionality varchar(255) not null,
        version bigint not null,
        module bigint,
        project bigint,
        indexColumn integer,
        primary key (id)
    );

    create table Report (
        id bigint not null,
        archived boolean not null,
        created timestamp not null,
        data clob not null,
        lastModified timestamp not null,
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        creator bigint,
        lastModifier bigint,
        primary key (id)
    );

    create table ReportCategory (
        id bigint not null,
        archived boolean not null,
        indexColumn integer,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table ReportContext (
        id bigint not null,
        context varchar(255) not null,
        report bigint,
        primary key (id)
    );

    create table Resource (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        primary key (id)
    );

    create table ResourceCategory (
        id bigint not null,
        archived boolean not null,
        name varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table School (
        id bigint not null,
        archived boolean not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        field bigint,
        primary key (id)
    );

    create table SchoolField (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table SchoolVariable (
        id bigint not null,
        archived boolean not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        school bigint,
        primary key (id)
    );

    create table SchoolVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table Setting (
        id bigint not null,
        value varchar(255),
        settingKey bigint,
        primary key (id)
    );

    create table SettingKey (
        id bigint not null,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table Student (
        id bigint not null,
        additionalInfo clob,
        archived boolean not null,
        education varchar(255),
        firstName varchar(255) not null,
        lastName varchar(255) not null,
        lodging boolean not null,
        nickname varchar(255),
        previousStudies double,
        studyEndDate date,
        studyEndText varchar(255),
        studyStartDate date,
        studyTimeEnd date,
        version bigint not null,
        abstractStudent bigint,
        activityType bigint,
        contactInfo bigint,
        educationalLevel bigint,
        examinationType bigint,
        language bigint,
        municipality bigint,
        nationality bigint,
        school bigint,
        studyEndReason bigint,
        studyProgramme bigint,
        primary key (id)
    );

    create table StudentActivityType (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table StudentContactLogEntry (
        id bigint not null,
        archived boolean not null,
        creatorName varchar(255),
        entryDate timestamp,
        text clob,
        type varchar(255),
        version bigint not null,
        student bigint not null,
        primary key (id)
    );

    create table StudentContactLogEntryComment (
        id bigint not null,
        archived boolean not null,
        commentDate timestamp,
        creatorName varchar(255),
        text clob,
        version bigint not null,
        entry bigint,
        primary key (id)
    );

    create table StudentCourseResource (
        id bigint not null,
        hourlyCost_amount double,
        hourlyCost_currency varchar(255),
        hours double not null,
        unitCost_amount double,
        unitCost_currency varchar(255),
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    );

    create table StudentEducationalLevel (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table StudentExaminationType (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table StudentFile (
        id bigint not null,
        student bigint,
        primary key (id)
    );

    create table StudentGroup (
        id bigint not null,
        archived boolean not null,
        beginDate timestamp,
        created timestamp not null,
        description clob,
        lastModified timestamp not null,
        name varchar(255) not null,
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        primary key (id)
    );

    create table StudentGroupStudent (
        id bigint not null,
        version bigint not null,
        student bigint,
        studentGroup bigint,
        primary key (id)
    );

    create table StudentGroupUser (
        id bigint not null,
        version bigint not null,
        studentGroup bigint,
        user bigint,
        primary key (id)
    );

    create table StudentImage (
        id bigint not null,
        contentType varchar(255),
        data blob,
        student bigint,
        primary key (id)
    );

    create table StudentProject (
        id bigint not null,
        archived boolean not null,
        created timestamp not null,
        description clob,
        lastModified timestamp not null,
        name varchar(255) not null,
        optionality varchar(255),
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        optionalStudies bigint,
        project bigint,
        student bigint,
        primary key (id)
    );

    create table StudentProjectModule (
        id bigint not null,
        optionality varchar(255) not null,
        version bigint not null,
        academicTerm bigint,
        module bigint,
        studentProject bigint,
        indexColumn integer,
        primary key (id)
    );

    create table StudentStudyEndReason (
        id bigint not null,
        name varchar(255),
        version bigint not null,
        parentReason bigint,
        primary key (id)
    );

    create table StudentVariable (
        id bigint not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        student bigint,
        primary key (id)
    );

    create table StudentVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table StudyProgramme (
        id bigint not null,
        archived boolean not null,
        code varchar(255),
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        primary key (id)
    );

    create table StudyProgrammeCategory (
        id bigint not null,
        archived boolean not null,
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    );

    create table Subject (
        id bigint not null,
        archived boolean not null,
        code varchar(255),
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    );

    create table Tag (
        id bigint not null,
        text varchar(255) not null unique,
        version bigint not null,
        primary key (id)
    );

    create table TrackedEntityProperty (
        id bigint not null,
        entity varchar(255),
        property varchar(255),
        primary key (id)
    );

    create table TransferCredit (
        courseName varchar(255) not null,
        courseNumber integer,
        optionality varchar(255),
        id bigint not null,
        courseLength bigint,
        school bigint,
        student bigint,
        subject bigint,
        primary key (id)
    );

    create table TransferCreditTemplate (
        id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    );

    create table TransferCreditTemplateCourse (
        id bigint not null,
        courseName varchar(255) not null,
        courseNumber integer,
        optionality varchar(255) not null,
        version bigint not null,
        courseLength bigint,
        subject bigint,
        transferCreditTemplate bigint,
        indexColumn integer,
        primary key (id)
    );

    create table User (
        id bigint not null,
        authProvider varchar(255) not null,
        externalId varchar(255) not null,
        firstName varchar(255) not null,
        lastName varchar(255) not null,
        role varchar(255) not null,
        title varchar(255),
        version bigint not null,
        contactInfo bigint,
        primary key (id)
    );

    create table UserVariable (
        id bigint not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        user bigint,
        primary key (id)
    );

    create table UserVariableKey (
        id bigint not null,
        userEditable boolean not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table WorkResource (
        costPerUse_amount double,
        costPerUse_currency varchar(255),
        hourlyCost_amount double,
        hourlyCost_currency varchar(255),
        id bigint not null,
        primary key (id)
    );

    create table __CourseTags (
        course bigint not null,
        tag bigint not null,
        primary key (course, tag)
    );

    create table __HelpItemTags (
        helpItem bigint not null,
        tag bigint not null,
        primary key (helpItem, tag)
    );

    create table __ModuleTags (
        module bigint not null,
        tag bigint not null,
        primary key (module, tag)
    );

    create table __ProjectTags (
        project bigint not null,
        tag bigint not null,
        primary key (project, tag)
    );

    create table __ResourceTags (
        resource bigint not null,
        tag bigint not null,
        primary key (resource, tag)
    );

    create table __SchoolTags (
        school bigint not null,
        tag bigint not null,
        primary key (school, tag)
    );

    create table __StudentBillingDetails (
        student bigint not null,
        billingDetails bigint not null
    );

    create table __StudentGroupTags (
        studentGroup bigint not null,
        tag bigint not null,
        primary key (studentGroup, tag)
    );

    create table __StudentProjectTags (
        studentProject bigint not null,
        tag bigint not null,
        primary key (studentProject, tag)
    );

    create table __StudentTags (
        student bigint not null,
        tag bigint not null,
        primary key (student, tag)
    );

    create table __UserBillingDetails (
        user bigint not null,
        billingDetails bigint not null
    );

    create table __UserTags (
        user bigint not null,
        tag bigint not null,
        primary key (user, tag)
    );

    alter table Address 
        add constraint FK1ED033D4C0329886 
        foreign key (contactType) 
        references ContactType;

    alter table Address 
        add constraint FK1ED033D4C028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table BasicCourseResource 
        add constraint FK87983CF75475C87B 
        foreign key (course) 
        references Course;

    alter table BasicCourseResource 
        add constraint FK87983CF7A7C0C394 
        foreign key (resource) 
        references Resource;

    alter table ChangeLogEntry 
        add constraint FK29D7737E2D667C2D 
        foreign key (entity) 
        references ChangeLogEntryEntity;

    alter table ChangeLogEntry 
        add constraint FK29D7737E62D248B 
        foreign key (user) 
        references User;

    alter table ChangeLogEntryEntityProperty 
        add constraint FKE9F8DDF62D667C2D 
        foreign key (entity) 
        references ChangeLogEntryEntity;

    alter table ChangeLogEntryProperty 
        add constraint FKE30B57733DEECE14 
        foreign key (property) 
        references ChangeLogEntryEntityProperty;

    alter table ChangeLogEntryProperty 
        add constraint FKE30B57735D7A51F9 
        foreign key (entry) 
        references ChangeLogEntry;

    alter table ComponentBase 
        add constraint FK954D014E6270F1B1 
        foreign key (length) 
        references EducationalLength;

    alter table ContactURL 
        add constraint FK7F280D4F58FFEF20 
        foreign key (contactURLType) 
        references ContactURLType;

    alter table ContactURL 
        add constraint FK7F280D4FC028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Course 
        add constraint FK78A7CC3BB33F86B5 
        foreign key (id) 
        references CourseBase;

    alter table Course 
        add constraint FK78A7CC3B5D81ECA2 
        foreign key (state) 
        references CourseState;

    alter table Course 
        add constraint FK78A7CC3BD7113A6E 
        foreign key (module) 
        references Module;

    alter table CourseAssessment 
        add constraint FK9833115D6EEA9BD 
        foreign key (id) 
        references Credit;

    alter table CourseAssessment 
        add constraint FK9833115D55F637B 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseAssessmentRequest 
        add constraint FKADBCA67255F637B 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseBase 
        add constraint FKB40B1AC977F310A 
        foreign key (subject) 
        references Subject;

    alter table CourseBase 
        add constraint FKB40B1AC76DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table CourseBase 
        add constraint FKB40B1AC4344B8EC 
        foreign key (creator) 
        references User;

    alter table CourseBase 
        add constraint FKB40B1AC2054118C 
        foreign key (courseLength) 
        references EducationalLength;

    alter table CourseBaseVariable 
        add constraint FK73597C4847BCDB06 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseBaseVariable 
        add constraint FK73597C48322966AC 
        foreign key (variableKey) 
        references CourseBaseVariableKey;

    alter table CourseComponent 
        add constraint FK4A4C03025F7117BB 
        foreign key (id) 
        references ComponentBase;

    alter table CourseComponent 
        add constraint FK4A4C03025475C87B 
        foreign key (course) 
        references Course;

    alter table CourseComponentResource 
        add constraint FK23A809306F64EBBF 
        foreign key (courseComponent) 
        references CourseComponent;

    alter table CourseComponentResource 
        add constraint FK23A80930A7C0C394 
        foreign key (resource) 
        references Resource;

    alter table CourseDescription 
        add constraint FKBB50FCC147BCDB06 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseDescription 
        add constraint FKBB50FCC18177E998 
        foreign key (category) 
        references CourseDescriptionCategory;

    alter table CourseEducationSubtype 
        add constraint FK102A2B0DC9842B12 
        foreign key (educationSubtype) 
        references EducationSubtype;

    alter table CourseEducationSubtype 
        add constraint FK102A2B0DED033D00 
        foreign key (courseEducationType) 
        references CourseEducationType;

    alter table CourseEducationType 
        add constraint FK5F27B287A5E07316 
        foreign key (educationType) 
        references EducationType;

    alter table CourseEducationType 
        add constraint FK5F27B28747BCDB06 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseStudent 
        add constraint FK21572580EE2B836F 
        foreign key (student) 
        references Student;

    alter table CourseStudent 
        add constraint FK215725805475C87B 
        foreign key (course) 
        references Course;

    alter table CourseStudent 
        add constraint FK2157258023DFBF96 
        foreign key (participationType) 
        references CourseParticipationType;

    alter table CourseStudent 
        add constraint FK21572580D2B417DC 
        foreign key (enrolmentType) 
        references CourseEnrolmentType;

    alter table CourseStudent 
        add constraint FK2157258075F2C3BC 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table CourseStudentVariable 
        add constraint FK99F8DC1C55F637B 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseStudentVariable 
        add constraint FK99F8DC1CEDFABEB 
        foreign key (variableKey) 
        references CourseStudentVariableKey;

    alter table CourseUser 
        add constraint FKB4996A65475C87B 
        foreign key (course) 
        references Course;

    alter table CourseUser 
        add constraint FKB4996A657C009DE 
        foreign key (pyramusUser) 
        references User;

    alter table CourseUser 
        add constraint FKB4996A6D5977E22 
        foreign key (userRole) 
        references CourseUserRole;

    alter table Credit 
        add constraint FK78CA9719D709AF89 
        foreign key (assessingUser) 
        references User;

    alter table Credit 
        add constraint FK78CA971979EF34E5 
        foreign key (grade) 
        references Grade;

    alter table CreditLink 
        add constraint FK552B86B3B654479B 
        foreign key (credit) 
        references Credit;

    alter table CreditLink 
        add constraint FK552B86B3EE2B836F 
        foreign key (student) 
        references Student;

    alter table CreditLink 
        add constraint FK552B86B34344B8EC 
        foreign key (creator) 
        references User;

    alter table CreditVariable 
        add constraint FKA051A4B5B654479B 
        foreign key (credit) 
        references Credit;

    alter table CreditVariable 
        add constraint FKA051A4B592438EA4 
        foreign key (variableKey) 
        references CreditVariableKey;

    alter table Defaults 
        add constraint FK2A415672531DE567 
        foreign key (courseState) 
        references CourseState;

    alter table Defaults 
        add constraint FK2A41567221BB8DB 
        foreign key (courseParticipationType) 
        references CourseParticipationType;

    alter table Defaults 
        add constraint FK2A41567261817BA1 
        foreign key (courseEnrolmentType) 
        references CourseEnrolmentType;

    alter table Defaults 
        add constraint FK2A415672572F2D3A 
        foreign key (educationalTimeUnit) 
        references EducationalTimeUnit;

    alter table EducationSubtype 
        add constraint FKD17434D2A5E07316 
        foreign key (educationType) 
        references EducationType;

    alter table EducationalLength 
        add constraint FK8E7A679278A621A 
        foreign key (unit) 
        references EducationalTimeUnit;

    alter table Email 
        add constraint FK3FF5B7CC0329886 
        foreign key (contactType) 
        references ContactType;

    alter table Email 
        add constraint FK3FF5B7CC028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table File 
        add constraint FK21699C76DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table File 
        add constraint FK21699C4344B8EC 
        foreign key (creator) 
        references User;

    alter table File 
        add constraint FK21699CDCDEF2CF 
        foreign key (fileType) 
        references FileType;

    alter table FormDraft 
        add constraint FKE1D2F71D4344B8EC 
        foreign key (creator) 
        references User;

    alter table Grade 
        add constraint FK41DCFB7146934B5 
        foreign key (gradingScale) 
        references GradingScale;

    alter table GradeCourseResource 
        add constraint FK39880CE05475C87B 
        foreign key (course) 
        references Course;

    alter table GradeCourseResource 
        add constraint FK39880CE0A7C0C394 
        foreign key (resource) 
        references Resource;

    alter table HelpFolder 
        add constraint FKA9227F2F42CE482D 
        foreign key (id) 
        references HelpItem;

    alter table HelpItem 
        add constraint FKD4C29A1476DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table HelpItem 
        add constraint FKD4C29A144344B8EC 
        foreign key (creator) 
        references User;

    alter table HelpItem 
        add constraint FKD4C29A148774E157 
        foreign key (parent) 
        references HelpFolder;

    alter table HelpItemTitle 
        add constraint FKC5BFC4A442FFB625 
        foreign key (item) 
        references HelpItem;

    alter table HelpItemTitle 
        add constraint FKC5BFC4A476DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table HelpItemTitle 
        add constraint FKC5BFC4A44344B8EC 
        foreign key (creator) 
        references User;

    alter table HelpPage 
        add constraint FKD4C5819042CE482D 
        foreign key (id) 
        references HelpItem;

    alter table HelpPageContent 
        add constraint FKB5D068C94305851D 
        foreign key (page) 
        references HelpPage;

    alter table HelpPageContent 
        add constraint FKB5D068C976DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table HelpPageContent 
        add constraint FKB5D068C94344B8EC 
        foreign key (creator) 
        references User;

    alter table MaterialResource 
        add constraint FKE255BA75BC150CA1 
        foreign key (id) 
        references Resource;

    alter table Module 
        add constraint FK89B0928CB33F86B5 
        foreign key (id) 
        references CourseBase;

    alter table ModuleComponent 
        add constraint FKFAFEC7D15F7117BB 
        foreign key (id) 
        references ComponentBase;

    alter table ModuleComponent 
        add constraint FKFAFEC7D1D7113A6E 
        foreign key (module) 
        references Module;

    alter table OtherCost 
        add constraint FK36EBADD5475C87B 
        foreign key (course) 
        references Course;

    alter table PhoneNumber 
        add constraint FK1C4E6237C0329886 
        foreign key (contactType) 
        references ContactType;

    alter table PhoneNumber 
        add constraint FK1C4E6237C028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Project 
        add constraint FK50C8E2F9BE6D6F92 
        foreign key (optionalStudies) 
        references EducationalLength;

    alter table Project 
        add constraint FK50C8E2F976DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table Project 
        add constraint FK50C8E2F94344B8EC 
        foreign key (creator) 
        references User;

    alter table ProjectAssessment 
        add constraint FKA42D529B6EEA9BD 
        foreign key (id) 
        references Credit;

    alter table ProjectAssessment 
        add constraint FKA42D529B22B70CA1 
        foreign key (studentProject) 
        references StudentProject;

    alter table ProjectModule 
        add constraint FKD8B796C536EE660D 
        foreign key (project) 
        references Project;

    alter table ProjectModule 
        add constraint FKD8B796C5D7113A6E 
        foreign key (module) 
        references Module;

    alter table Report 
        add constraint FK91B1415476DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table Report 
        add constraint FK91B141544344B8EC 
        foreign key (creator) 
        references User;

    alter table Report 
        add constraint FK91B1415426DDD5AE 
        foreign key (category) 
        references ReportCategory;

    alter table ReportContext 
        add constraint FK336625BB47FBFEC6 
        foreign key (report) 
        references Report;

    alter table Resource 
        add constraint FKEF86282E3857F7A2 
        foreign key (category) 
        references ResourceCategory;

    alter table School 
        add constraint FK93464794C028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table School 
        add constraint FK93464794ECFDC92 
        foreign key (field) 
        references SchoolField;

    alter table SchoolVariable 
        add constraint FK58FEAA301BB391F6 
        foreign key (school) 
        references School;

    alter table SchoolVariable 
        add constraint FK58FEAA3092C708C4 
        foreign key (variableKey) 
        references SchoolVariableKey;

    alter table Setting 
        add constraint FKD997A6301C9B8DAE 
        foreign key (settingKey) 
        references SettingKey;

    alter table Student 
        add constraint FKF3371A1BEF70A856 
        foreign key (educationalLevel) 
        references StudentEducationalLevel;

    alter table Student 
        add constraint FKF3371A1B30ED0CC0 
        foreign key (studyEndReason) 
        references StudentStudyEndReason;

    alter table Student 
        add constraint FKF3371A1BFDB61D8B 
        foreign key (abstractStudent) 
        references AbstractStudent;

    alter table Student 
        add constraint FKF3371A1BC655592A 
        foreign key (nationality) 
        references Nationality;

    alter table Student 
        add constraint FKF3371A1BC028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Student 
        add constraint FKF3371A1B9F140BD4 
        foreign key (studyProgramme) 
        references StudyProgramme;

    alter table Student 
        add constraint FKF3371A1B29206186 
        foreign key (municipality) 
        references Municipality;

    alter table Student 
        add constraint FKF3371A1B8ECB763E 
        foreign key (language) 
        references Language;

    alter table Student 
        add constraint FKF3371A1B44566106 
        foreign key (activityType) 
        references StudentActivityType;

    alter table Student 
        add constraint FKF3371A1B1BB391F6 
        foreign key (school) 
        references School;

    alter table Student 
        add constraint FKF3371A1B7CB521A2 
        foreign key (examinationType) 
        references StudentExaminationType;

    alter table StudentContactLogEntry 
        add constraint FKEBB80C33EE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentContactLogEntryComment 
        add constraint FKD11CEAAC4458F86C 
        foreign key (entry) 
        references StudentContactLogEntry;

    alter table StudentCourseResource 
        add constraint FKCF4D58445475C87B 
        foreign key (course) 
        references Course;

    alter table StudentCourseResource 
        add constraint FKCF4D5844A7C0C394 
        foreign key (resource) 
        references Resource;

    alter table StudentFile 
        add constraint FKEFB7FE373EE7CD5A 
        foreign key (id) 
        references File;

    alter table StudentFile 
        add constraint FKEFB7FE37EE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentGroup 
        add constraint FK75A052476DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table StudentGroup 
        add constraint FK75A05244344B8EC 
        foreign key (creator) 
        references User;

    alter table StudentGroupStudent 
        add constraint FKD427C477EE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentGroupStudent 
        add constraint FKD427C47786F746CF 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table StudentGroupUser 
        add constraint FK12F3330F86F746CF 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table StudentGroupUser 
        add constraint FK12F3330F62D248B 
        foreign key (user) 
        references User;

    alter table StudentImage 
        add constraint FK773B800EE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentProject 
        add constraint FK7500447EBE6D6F92 
        foreign key (optionalStudies) 
        references EducationalLength;

    alter table StudentProject 
        add constraint FK7500447EEE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentProject 
        add constraint FK7500447E36EE660D 
        foreign key (project) 
        references Project;

    alter table StudentProject 
        add constraint FK7500447E76DFD82D 
        foreign key (lastModifier) 
        references User;

    alter table StudentProject 
        add constraint FK7500447E4344B8EC 
        foreign key (creator) 
        references User;

    alter table StudentProjectModule 
        add constraint FK6FBF008A22B70CA1 
        foreign key (studentProject) 
        references StudentProject;

    alter table StudentProjectModule 
        add constraint FK6FBF008A15319894 
        foreign key (academicTerm) 
        references AcademicTerm;

    alter table StudentProjectModule 
        add constraint FK6FBF008AD7113A6E 
        foreign key (module) 
        references Module;

    alter table StudentStudyEndReason 
        add constraint FK9136D6316227D0F8 
        foreign key (parentReason) 
        references StudentStudyEndReason;

    alter table StudentVariable 
        add constraint FK1C1405B7EE2B836F 
        foreign key (student) 
        references Student;

    alter table StudentVariable 
        add constraint FK1C1405B7B02EFAD2 
        foreign key (variableKey) 
        references StudentVariableKey;

    alter table StudyProgramme 
        add constraint FKB037EB733296F97D 
        foreign key (category) 
        references StudyProgrammeCategory;

    alter table StudyProgrammeCategory 
        add constraint FKFCB7E991A5E07316 
        foreign key (educationType) 
        references EducationType;

    alter table Subject 
        add constraint FKF3E2ED0CA5E07316 
        foreign key (educationType) 
        references EducationType;

    alter table TransferCredit 
        add constraint FKB1D3EE4977F310A 
        foreign key (subject) 
        references Subject;

    alter table TransferCredit 
        add constraint FKB1D3EE46EEA9BD 
        foreign key (id) 
        references Credit;

    alter table TransferCredit 
        add constraint FKB1D3EE4EE2B836F 
        foreign key (student) 
        references Student;

    alter table TransferCredit 
        add constraint FKB1D3EE42054118C 
        foreign key (courseLength) 
        references EducationalLength;

    alter table TransferCredit 
        add constraint FKB1D3EE41BB391F6 
        foreign key (school) 
        references School;

    alter table TransferCreditTemplateCourse 
        add constraint FK471C0139977F310A 
        foreign key (subject) 
        references Subject;

    alter table TransferCreditTemplateCourse 
        add constraint FK471C013931068E25 
        foreign key (transferCreditTemplate) 
        references TransferCreditTemplate;

    alter table TransferCreditTemplateCourse 
        add constraint FK471C01392054118C 
        foreign key (courseLength) 
        references EducationalLength;

    alter table User 
        add constraint FK285FEBC028436E 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table UserVariable 
        add constraint FK918A7B87E073D3C6 
        foreign key (variableKey) 
        references UserVariableKey;

    alter table UserVariable 
        add constraint FK918A7B8762D248B 
        foreign key (user) 
        references User;

    alter table WorkResource 
        add constraint FKDD5DA25FBC150CA1 
        foreign key (id) 
        references Resource;

    alter table __CourseTags 
        add constraint FK271392D45475C87B 
        foreign key (course) 
        references Course;

    alter table __CourseTags 
        add constraint FK271392D4F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __HelpItemTags 
        add constraint FK7314542DF8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __HelpItemTags 
        add constraint FK7314542D13C4FCE6 
        foreign key (helpItem) 
        references HelpItem;

    alter table __ModuleTags 
        add constraint FK5EBEC8A5F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __ModuleTags 
        add constraint FK5EBEC8A5D7113A6E 
        foreign key (module) 
        references Module;

    alter table __ProjectTags 
        add constraint FK88F02ED236EE660D 
        foreign key (project) 
        references Project;

    alter table __ProjectTags 
        add constraint FK88F02ED2F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __ResourceTags 
        add constraint FK46CF4547F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __ResourceTags 
        add constraint FK46CF4547A7C0C394 
        foreign key (resource) 
        references Resource;

    alter table __SchoolTags 
        add constraint FK8CE2B9ADF8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __SchoolTags 
        add constraint FK8CE2B9AD1BB391F6 
        foreign key (school) 
        references School;

    alter table __StudentBillingDetails 
        add constraint FK4F787262EE2B836F 
        foreign key (student) 
        references Student;

    alter table __StudentBillingDetails 
        add constraint FK4F78726275F2C3BC 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table __StudentGroupTags 
        add constraint FKF41EC73DF8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __StudentGroupTags 
        add constraint FKF41EC73D86F746CF 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table __StudentProjectTags 
        add constraint FK19E519722B70CA1 
        foreign key (studentProject) 
        references StudentProject;

    alter table __StudentProjectTags 
        add constraint FK19E5197F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __StudentTags 
        add constraint FK4D4A04F4EE2B836F 
        foreign key (student) 
        references Student;

    alter table __StudentTags 
        add constraint FK4D4A04F4F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __UserBillingDetails 
        add constraint FK5BD3FFF275F2C3BC 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table __UserBillingDetails 
        add constraint FK5BD3FFF262D248B 
        foreign key (user) 
        references User;

    alter table __UserTags 
        add constraint FKA09E4684F8D12EE6 
        foreign key (tag) 
        references Tag;

    alter table __UserTags 
        add constraint FKA09E468462D248B 
        foreign key (user) 
        references User;

    create table hibernate_sequences (
         sequence_name varchar(255),
         sequence_next_hi_value integer 
    ) ;