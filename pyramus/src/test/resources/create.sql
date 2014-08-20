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

    create table AccessLogEntry (
        id bigint not null,
        date timestamp not null,
        ip varchar(255) not null,
        parameters clob,
        path bigint,
        user bigint,
        primary key (id)
    );

    create table AccessLogEntryPath (
        id bigint not null,
        active boolean not null,
        path clob not null,
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
        name varchar(255),
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
        courseStudent bigint,
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
        username varchar(255) not null,
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
        name varchar(255) not null,
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
        repositoryId varchar(255) not null,
        url varchar(255) not null,
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
        name varchar(255) not null,
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
        text varchar(255) not null,
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

    alter table ChangeLogEntryEntity 
        add constraint UK_3ivxxlmh3gjyajo41lt19r30j  unique (name);

    alter table CourseAssessment 
        add constraint UK_h2uf2bqevk8mcg4c209iimiii  unique (courseStudent);

    alter table InternalAuth 
        add constraint UK_9i2dkw43taedavb18gke1ddmt  unique (username);

    alter table MagicKey 
        add constraint UK_3c73v1x7o1a8b2bqcaojh4bq7  unique (name);

    alter table PluginRepository 
        add constraint UK_oa6tjb0q8x7s16o1i59s15981  unique (repositoryId);

    alter table PluginRepository 
        add constraint UK_g5y4nx9re8jyupfge8su2s23w  unique (url);

    alter table SettingKey 
        add constraint UK_rdcw2wxypyku3249noelqeoj7  unique (name);

    alter table Tag 
        add constraint UK_am6x1sovg4dm5l9l581e0ckot  unique (text);

    alter table AccessLogEntry 
        add constraint FK_r031kiolhx2sg7hpdngdn026l 
        foreign key (path) 
        references AccessLogEntryPath;

    alter table AccessLogEntry 
        add constraint FK_pqnvv7lafgjgne4uxm2vqvqiu 
        foreign key (user) 
        references User;

    alter table Address 
        add constraint FK_p90qwaeto30hrsskrw3vddgup 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Address 
        add constraint FK_o4ui1nisxpnjicn3ift35lawt 
        foreign key (contactType) 
        references ContactType;

    alter table BasicCourseResource 
        add constraint FK_6vyqcfts1vlur0nv0d05e4td2 
        foreign key (course) 
        references Course;

    alter table BasicCourseResource 
        add constraint FK_gk3t0um3tiddm2goieocsqe3 
        foreign key (resource) 
        references Resource;

    alter table ChangeLogEntry 
        add constraint FK_trl3pu52yuhr6jlus9epa7ild 
        foreign key (entity) 
        references ChangeLogEntryEntity;

    alter table ChangeLogEntry 
        add constraint FK_kdh12wtrq0966fpwyjylcbwh1 
        foreign key (user) 
        references User;

    alter table ChangeLogEntryEntityProperty 
        add constraint FK_cf3qvc4k2fuqw17mo37kijm28 
        foreign key (entity) 
        references ChangeLogEntryEntity;

    alter table ChangeLogEntryProperty 
        add constraint FK_bd33yidoqd2jxs8n99p6tkpge 
        foreign key (entry) 
        references ChangeLogEntry;

    alter table ChangeLogEntryProperty 
        add constraint FK_qgkacalwos2pvununsghx60j 
        foreign key (property) 
        references ChangeLogEntryEntityProperty;

    alter table ComponentBase 
        add constraint FK_n3636lg4sxh0jvounesk7wec8 
        foreign key (length) 
        references EducationalLength;

    alter table ContactURL 
        add constraint FK_2s7xt7s92jscrxgn1x6lxp3t4 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table ContactURL 
        add constraint FK_b6cur2odxtvui687f979t3xoh 
        foreign key (contactURLType) 
        references ContactURLType;

    alter table Course 
        add constraint FK_hvlfik6l3xul7nju5i6mvr5pt 
        foreign key (module) 
        references Module;

    alter table Course 
        add constraint FK_4gv69tpplv1tn5l3caog1joy4 
        foreign key (state) 
        references CourseState;

    alter table Course 
        add constraint FK_1djmp3hn9iwd9ad4m58qtv3mw 
        foreign key (id) 
        references CourseBase;

    alter table CourseAssessment 
        add constraint FK_h2uf2bqevk8mcg4c209iimiii 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseAssessment 
        add constraint FK_fh8tfbpjnxx2vv360mjycnkl4 
        foreign key (id) 
        references Credit;

    alter table CourseAssessmentRequest 
        add constraint FK_8q7qimj1fjafgipdlghx21ihe 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseBase 
        add constraint FK_8n3d8qk4nbhisror3fa56mysa 
        foreign key (courseLength) 
        references EducationalLength;

    alter table CourseBase 
        add constraint FK_qdbp18pq48wyynl5ckj9t3bg2 
        foreign key (creator) 
        references User;

    alter table CourseBase 
        add constraint FK_5mf6i7k6y5kag8g3aovixs38y 
        foreign key (lastModifier) 
        references User;

    alter table CourseBase 
        add constraint FK_f7792dwjjlr8yt0r7mgkpirrd 
        foreign key (subject) 
        references Subject;

    alter table CourseBaseVariable 
        add constraint FK_20wssfclcmxxxobjutc6o4mfw 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseBaseVariable 
        add constraint FK_g1q49gtou9k3jso650k5r37yc 
        foreign key (variableKey) 
        references CourseBaseVariableKey;

    alter table CourseComponent 
        add constraint FK_lt90xbqly93wnxsjdqolp83vn 
        foreign key (course) 
        references Course;

    alter table CourseComponent 
        add constraint FK_87f9ovvi3hy4roxtebo6hy5al 
        foreign key (id) 
        references ComponentBase;

    alter table CourseComponentResource 
        add constraint FK_iirdnay0h9fc63st5atv9i1ur 
        foreign key (courseComponent) 
        references CourseComponent;

    alter table CourseComponentResource 
        add constraint FK_jyfucsejwcj8hbxbl3v8b2rno 
        foreign key (resource) 
        references Resource;

    alter table CourseDescription 
        add constraint FK_d3gaxsqdh05g53olhgjl99eij 
        foreign key (category) 
        references CourseDescriptionCategory;

    alter table CourseDescription 
        add constraint FK_77le1nirxo5yighwd96fyjxqf 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseEducationSubtype 
        add constraint FK_kt290qaxkhotm9jo7wb6hv0aj 
        foreign key (courseEducationType) 
        references CourseEducationType;

    alter table CourseEducationSubtype 
        add constraint FK_409elphhj1xbcuqo57tmypn96 
        foreign key (educationSubtype) 
        references EducationSubtype;

    alter table CourseEducationType 
        add constraint FK_hq2hbvq3f81dmyymqa2j07bcb 
        foreign key (courseBase) 
        references CourseBase;

    alter table CourseEducationType 
        add constraint FK_70hkwva11c9btj1r8xdflt6y4 
        foreign key (educationType) 
        references EducationType;

    alter table CourseStudent 
        add constraint FK_990n0w0581m3h4n0pvxyo09gs 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table CourseStudent 
        add constraint FK_tdsd2ykmt3gl8al74vhvetp5p 
        foreign key (course) 
        references Course;

    alter table CourseStudent 
        add constraint FK_joq4rus28ls505so74el2sne2 
        foreign key (enrolmentType) 
        references CourseEnrolmentType;

    alter table CourseStudent 
        add constraint FK_6b304h022160fuidrq2n1l7g9 
        foreign key (participationType) 
        references CourseParticipationType;

    alter table CourseStudent 
        add constraint FK_6syti63mb3rf5ohusl6hhxm1l 
        foreign key (student) 
        references Student;

    alter table CourseStudentVariable 
        add constraint FK_fdew52if7kx4x8t433jtwjbw8 
        foreign key (courseStudent) 
        references CourseStudent;

    alter table CourseStudentVariable 
        add constraint FK_g1rwycwb6g3lm68lrpg0k3rbs 
        foreign key (variableKey) 
        references CourseStudentVariableKey;

    alter table CourseUser 
        add constraint FK_8m1wdnkvu7f4w38jxpb0pf7n6 
        foreign key (course) 
        references Course;

    alter table CourseUser 
        add constraint FK_d71yo025tyvtm7sm6c8b33c7k 
        foreign key (userRole) 
        references CourseUserRole;

    alter table CourseUser 
        add constraint FK_gesju89o5caq9d5ics49nkmda 
        foreign key (pyramusUser) 
        references User;

    alter table Credit 
        add constraint FK_qeujhkfa3cpfwvb48646agdch 
        foreign key (assessingUser) 
        references User;

    alter table Credit 
        add constraint FK_fpl517x4owb3qt2nhit9oy8e7 
        foreign key (grade) 
        references Grade;

    alter table CreditLink 
        add constraint FK_6idi337xbuq6w5wk75w8kwad9 
        foreign key (creator) 
        references User;

    alter table CreditLink 
        add constraint FK_gmdwo9i5q09mbxs4idwgqfvxt 
        foreign key (credit) 
        references Credit;

    alter table CreditLink 
        add constraint FK_ju2x5va5e7votkdlghnmdc2gq 
        foreign key (student) 
        references Student;

    alter table CreditVariable 
        add constraint FK_iqmfjcic2boxjciqgamihkppj 
        foreign key (credit) 
        references Credit;

    alter table CreditVariable 
        add constraint FK_inaneefcy0mnigltdmt4fobdr 
        foreign key (variableKey) 
        references CreditVariableKey;

    alter table Defaults 
        add constraint FK_dnkpit0q6wlkfl0b66akw84pt 
        foreign key (educationalTimeUnit) 
        references EducationalTimeUnit;

    alter table Defaults 
        add constraint FK_g86nby8edvpbde88tdeyb851 
        foreign key (courseEnrolmentType) 
        references CourseEnrolmentType;

    alter table Defaults 
        add constraint FK_23wxeog8g1fyee0982ubptj3f 
        foreign key (courseParticipationType) 
        references CourseParticipationType;

    alter table Defaults 
        add constraint FK_s9uv5jk1xxljge95mksp4tfx0 
        foreign key (courseState) 
        references CourseState;

    alter table EducationSubtype 
        add constraint FK_ljkp7gmuy3wgdl2nkxs316ow1 
        foreign key (educationType) 
        references EducationType;

    alter table EducationalLength 
        add constraint FK_9i3s1om3re3kf4sciv2ay719f 
        foreign key (unit) 
        references EducationalTimeUnit;

    alter table Email 
        add constraint FK_9beti0nhdq60m9llr30r7xrbk 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Email 
        add constraint FK_98itl0gnxl42v33d8wtim00ny 
        foreign key (contactType) 
        references ContactType;

    alter table File 
        add constraint FK_p39k3e3t8kff359g2rkhjjovt 
        foreign key (creator) 
        references User;

    alter table File 
        add constraint FK_6nyh3x9yrffgiau2lbkbxbl3k 
        foreign key (fileType) 
        references FileType;

    alter table File 
        add constraint FK_i4xo8fnijunrxpmrslpsggjld 
        foreign key (lastModifier) 
        references User;

    alter table FormDraft 
        add constraint FK_ubrbpijc1j5magqtp2davuyw 
        foreign key (creator) 
        references User;

    alter table Grade 
        add constraint FK_amgg5818eisglvsplo3vt68yj 
        foreign key (gradingScale) 
        references GradingScale;

    alter table GradeCourseResource 
        add constraint FK_pr4wqos0fajw1wuqboieu8fmm 
        foreign key (course) 
        references Course;

    alter table GradeCourseResource 
        add constraint FK_3vhbap3vceorw22x4mh994n8g 
        foreign key (resource) 
        references Resource;

    alter table HelpFolder 
        add constraint FK_nuaikofqxx36tbnqva5w30dkr 
        foreign key (id) 
        references HelpItem;

    alter table HelpItem 
        add constraint FK_4rajwxh9isb1hj1i0r51i77jk 
        foreign key (creator) 
        references User;

    alter table HelpItem 
        add constraint FK_gxcqfqdc3twretfmsbo5xg4we 
        foreign key (lastModifier) 
        references User;

    alter table HelpItem 
        add constraint FK_kqa3kt0f5n4c6d50docm62tho 
        foreign key (parent) 
        references HelpFolder;

    alter table HelpItemTitle 
        add constraint FK_a6mow7m5ul7cer67yf3i4xc8c 
        foreign key (creator) 
        references User;

    alter table HelpItemTitle 
        add constraint FK_2xxynq0ppm8ou0o3dcwkhxawq 
        foreign key (item) 
        references HelpItem;

    alter table HelpItemTitle 
        add constraint FK_i1a0pr6j0cbeghcnv5qdlf6nc 
        foreign key (lastModifier) 
        references User;

    alter table HelpPage 
        add constraint FK_9awkdrimomfve3wo73o7x9lrq 
        foreign key (id) 
        references HelpItem;

    alter table HelpPageContent 
        add constraint FK_7aq0keo8uon28osm3biuxrxhd 
        foreign key (creator) 
        references User;

    alter table HelpPageContent 
        add constraint FK_qg5cfmrofk6kve7ffgpgm2nn 
        foreign key (lastModifier) 
        references User;

    alter table HelpPageContent 
        add constraint FK_skw5bpma7gi8pm1i2yih5hsch 
        foreign key (page) 
        references HelpPage;

    alter table MaterialResource 
        add constraint FK_pi7v6ltxwjfung4vhoricynqx 
        foreign key (id) 
        references Resource;

    alter table Module 
        add constraint FK_8qdxvhwj9bps8ktupfem3m53j 
        foreign key (id) 
        references CourseBase;

    alter table ModuleComponent 
        add constraint FK_tmd3hat9f93c34cxhvagf7b0f 
        foreign key (module) 
        references Module;

    alter table ModuleComponent 
        add constraint FK_r6r3bnkfpar0lqq1diqetqbac 
        foreign key (id) 
        references ComponentBase;

    alter table OtherCost 
        add constraint FK_6f2p8ujs7cleagmwfy226acqe 
        foreign key (course) 
        references Course;

    alter table PhoneNumber 
        add constraint FK_awul8v1e7o1lwrxpofjfyh4a0 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table PhoneNumber 
        add constraint FK_cxyslapwd99ifu2b4nv79pxtf 
        foreign key (contactType) 
        references ContactType;

    alter table Project 
        add constraint FK_1aik7d4w4m941okf0qa6i9uw8 
        foreign key (creator) 
        references User;

    alter table Project 
        add constraint FK_kmrhsrdppvcr99du224dr82qp 
        foreign key (lastModifier) 
        references User;

    alter table Project 
        add constraint FK_gifnakb4oeswbkdfj2j73yxym 
        foreign key (optionalStudies) 
        references EducationalLength;

    alter table ProjectAssessment 
        add constraint FK_4o00eth6ajsmkruwvjx2qwwnl 
        foreign key (studentProject) 
        references StudentProject;

    alter table ProjectAssessment 
        add constraint FK_2edqobatxcdbfqmy418e9m781 
        foreign key (id) 
        references Credit;

    alter table ProjectModule 
        add constraint FK_lrt13xbhvibxnp1i60handpmr 
        foreign key (module) 
        references Module;

    alter table ProjectModule 
        add constraint FK_fklaynsnhmwilbwdpul0tykfu 
        foreign key (project) 
        references Project;

    alter table Report 
        add constraint FK_37x7inqemxi63kf9knphcemt7 
        foreign key (category) 
        references ReportCategory;

    alter table Report 
        add constraint FK_nd95h1smw61bqoq7y1rknjvhr 
        foreign key (creator) 
        references User;

    alter table Report 
        add constraint FK_8hw8ykuibh000vu6qtllv67ps 
        foreign key (lastModifier) 
        references User;

    alter table ReportContext 
        add constraint FK_crg8up29hxw2njcbgureswxch 
        foreign key (report) 
        references Report;

    alter table Resource 
        add constraint FK_t58ubqwynff69fm5tpco4ye47 
        foreign key (category) 
        references ResourceCategory;

    alter table School 
        add constraint FK_5wwaivsnowvhspfqdmqjrtrxj 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table School 
        add constraint FK_jy0pdno5q9b79t2yg3ng3sn4q 
        foreign key (field) 
        references SchoolField;

    alter table SchoolVariable 
        add constraint FK_g478tf5sv2iie0uf25ahblw89 
        foreign key (variableKey) 
        references SchoolVariableKey;

    alter table SchoolVariable 
        add constraint FK_niktl5fwc9i5hmk0rwvqu8dny 
        foreign key (school) 
        references School;

    alter table Setting 
        add constraint FK_i4lfreu085sgpg9qjjhesr2vi 
        foreign key (settingKey) 
        references SettingKey;

    alter table Student 
        add constraint FK_jkly5h0o7bfv241wftfpj5mh6 
        foreign key (abstractStudent) 
        references AbstractStudent;

    alter table Student 
        add constraint FK_moxdtidtiop0frxnxal9unpt 
        foreign key (activityType) 
        references StudentActivityType;

    alter table Student 
        add constraint FK_6xxj4ek0nlfmtckd6u8hct201 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table Student 
        add constraint FK_h8ypmoc31ra5j40x74esdy6x2 
        foreign key (educationalLevel) 
        references StudentEducationalLevel;

    alter table Student 
        add constraint FK_3yckb2qkc56r4by902wwci0n8 
        foreign key (examinationType) 
        references StudentExaminationType;

    alter table Student 
        add constraint FK_d97pguoxqr7ame3qnip13ekp1 
        foreign key (language) 
        references Language;

    alter table Student 
        add constraint FK_ahq5fxxqkhcolav50hq9nh0q 
        foreign key (municipality) 
        references Municipality;

    alter table Student 
        add constraint FK_jdov17i07kqm1v2dyvey648xq 
        foreign key (nationality) 
        references Nationality;

    alter table Student 
        add constraint FK_bj6b1k4s9p44hqsrobb36tr86 
        foreign key (school) 
        references School;

    alter table Student 
        add constraint FK_6civiuvxkr0mt0xdewlk1p209 
        foreign key (studyEndReason) 
        references StudentStudyEndReason;

    alter table Student 
        add constraint FK_jos3vl724h8ln4toi52mn5b6f 
        foreign key (studyProgramme) 
        references StudyProgramme;

    alter table StudentContactLogEntry 
        add constraint FK_gnx1l8oymwaqxbl4hlownd4s2 
        foreign key (student) 
        references Student;

    alter table StudentContactLogEntryComment 
        add constraint FK_5cbk5okc1c4j3rbhgu8u5u0vm 
        foreign key (entry) 
        references StudentContactLogEntry;

    alter table StudentCourseResource 
        add constraint FK_1wfh045c1ajeqiyuedcs1w8jo 
        foreign key (course) 
        references Course;

    alter table StudentCourseResource 
        add constraint FK_8swnhosc1ckobku19m5vvqohr 
        foreign key (resource) 
        references Resource;

    alter table StudentFile 
        add constraint FK_rf3myt039nl5b85a5vftd0ry8 
        foreign key (student) 
        references Student;

    alter table StudentFile 
        add constraint FK_4p3ib0d8up3g526m3etu4x5jh 
        foreign key (id) 
        references File;

    alter table StudentGroup 
        add constraint FK_4d8ydne169e6ueqyj0jtwlpmm 
        foreign key (creator) 
        references User;

    alter table StudentGroup 
        add constraint FK_7idqmgs5v42cv13kosncuo0fe 
        foreign key (lastModifier) 
        references User;

    alter table StudentGroupStudent 
        add constraint FK_5l4cr2cte8wyiug3x0mkmpr6t 
        foreign key (student) 
        references Student;

    alter table StudentGroupStudent 
        add constraint FK_9l0e8ewb1nfi1vxsr4anqnjbj 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table StudentGroupUser 
        add constraint FK_7qmfe5ac665syeij5k3x3vb7o 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table StudentGroupUser 
        add constraint FK_a0t3yelwll2bbm4m123bs7int 
        foreign key (user) 
        references User;

    alter table StudentImage 
        add constraint FK_a4fvsbpg1wd2fq74f8xqcwb6s 
        foreign key (student) 
        references Student;

    alter table StudentProject 
        add constraint FK_3piais5t058ytt7hjlvm2yixh 
        foreign key (creator) 
        references User;

    alter table StudentProject 
        add constraint FK_87nxxdtnf8dyn2x1e3h86b5rh 
        foreign key (lastModifier) 
        references User;

    alter table StudentProject 
        add constraint FK_ke85ke770aj7jcbmjcoku5la2 
        foreign key (optionalStudies) 
        references EducationalLength;

    alter table StudentProject 
        add constraint FK_m0xgkc8vsti9bpb8j7hpg62kt 
        foreign key (project) 
        references Project;

    alter table StudentProject 
        add constraint FK_cac2lntddk7blosrxr9xofjsj 
        foreign key (student) 
        references Student;

    alter table StudentProjectModule 
        add constraint FK_dd1wmb18pl0s2d07y0oyoynbq 
        foreign key (academicTerm) 
        references AcademicTerm;

    alter table StudentProjectModule 
        add constraint FK_aqcxytl2yuj8xnjrvlm6xu3i4 
        foreign key (module) 
        references Module;

    alter table StudentProjectModule 
        add constraint FK_p4slb9gh4okk9uttkw09jwn7u 
        foreign key (studentProject) 
        references StudentProject;

    alter table StudentStudyEndReason 
        add constraint FK_2f8xkyc4p4muk488tcnc6gpqr 
        foreign key (parentReason) 
        references StudentStudyEndReason;

    alter table StudentVariable 
        add constraint FK_napjy06rvnne36aluoi0ayjfr 
        foreign key (variableKey) 
        references StudentVariableKey;

    alter table StudentVariable 
        add constraint FK_fvw9n1unc74nwjp64w9aml70w 
        foreign key (student) 
        references Student;

    alter table StudyProgramme 
        add constraint FK_9ro57ucpum3wqatybrefu71it 
        foreign key (category) 
        references StudyProgrammeCategory;

    alter table StudyProgrammeCategory 
        add constraint FK_lkbg43l77aimbt60q5tk1kdfm 
        foreign key (educationType) 
        references EducationType;

    alter table Subject 
        add constraint FK_2jvjpbje8i7rifw2cq5uhsplo 
        foreign key (educationType) 
        references EducationType;

    alter table TransferCredit 
        add constraint FK_9n6pwvfm0okxfwsiyf6fpx1p8 
        foreign key (courseLength) 
        references EducationalLength;

    alter table TransferCredit 
        add constraint FK_rk9k9w0b5oqlxohfnn5pgm146 
        foreign key (school) 
        references School;

    alter table TransferCredit 
        add constraint FK_hyn393ws6p0x42tnd9kw9d0ar 
        foreign key (student) 
        references Student;

    alter table TransferCredit 
        add constraint FK_5hw59qfrdyj1pg8xl6ipjed87 
        foreign key (subject) 
        references Subject;

    alter table TransferCredit 
        add constraint FK_3kvav953yf312os4409cgmpqi 
        foreign key (id) 
        references Credit;

    alter table TransferCreditTemplateCourse 
        add constraint FK_4g5726qoakm5036sjyj0xm58 
        foreign key (courseLength) 
        references EducationalLength;

    alter table TransferCreditTemplateCourse 
        add constraint FK_sqrxc1ecwi5ukroe3aesq4shw 
        foreign key (subject) 
        references Subject;

    alter table TransferCreditTemplateCourse 
        add constraint FK_ifciewbdasfs7ko6falijugdi 
        foreign key (transferCreditTemplate) 
        references TransferCreditTemplate;

    alter table User 
        add constraint FK_riou196egia4li7tw37luek46 
        foreign key (contactInfo) 
        references ContactInfo;

    alter table UserVariable 
        add constraint FK_qaiiydgi6aj0x9exdsxf0st2d 
        foreign key (variableKey) 
        references UserVariableKey;

    alter table UserVariable 
        add constraint FK_spn3atobymho400p1sn47vira 
        foreign key (user) 
        references User;

    alter table WorkResource 
        add constraint FK_2twof1hru8qnoib84e8h4ubmc 
        foreign key (id) 
        references Resource;

    alter table __CourseTags 
        add constraint FK_tioxjjt07nmxu8d3wj7myco0k 
        foreign key (tag) 
        references Tag;

    alter table __CourseTags 
        add constraint FK_9jy0h9af4687ay9t3syd8n1tm 
        foreign key (course) 
        references Course;

    alter table __HelpItemTags 
        add constraint FK_mo6ixudlv35ltqe396cfdgxb5 
        foreign key (tag) 
        references Tag;

    alter table __HelpItemTags 
        add constraint FK_r4j6p1twifvgvbq7fd3er8m1o 
        foreign key (helpItem) 
        references HelpItem;

    alter table __ModuleTags 
        add constraint FK_inga9109o64j42q32s2cainpt 
        foreign key (tag) 
        references Tag;

    alter table __ModuleTags 
        add constraint FK_3si3vhxd8bam4tqkxmlga9r45 
        foreign key (module) 
        references Module;

    alter table __ProjectTags 
        add constraint FK_p4oey2c9m9iujqgjc3cskvi86 
        foreign key (tag) 
        references Tag;

    alter table __ProjectTags 
        add constraint FK_p4bwrib9jhu1mx7ww2b9hop3o 
        foreign key (project) 
        references Project;

    alter table __ResourceTags 
        add constraint FK_nxqiot7237rkexinkncrvufe1 
        foreign key (tag) 
        references Tag;

    alter table __ResourceTags 
        add constraint FK_dft9for4owmie0uc1npd17gs6 
        foreign key (resource) 
        references Resource;

    alter table __SchoolTags 
        add constraint FK_7v02ry618vqfi225egm7ag2r8 
        foreign key (tag) 
        references Tag;

    alter table __SchoolTags 
        add constraint FK_lbkqqkavgltyrrcjatg5eqh6w 
        foreign key (school) 
        references School;

    alter table __StudentBillingDetails 
        add constraint FK_g03t04lpvdnt3j81fo53cbjqq 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table __StudentBillingDetails 
        add constraint FK_8s693eljni5o9vjboyg8rdbpa 
        foreign key (student) 
        references Student;

    alter table __StudentGroupTags 
        add constraint FK_44ub49nfa8oleq5fg66ur4fc0 
        foreign key (tag) 
        references Tag;

    alter table __StudentGroupTags 
        add constraint FK_iobxoo1vl9qll999oui4744xv 
        foreign key (studentGroup) 
        references StudentGroup;

    alter table __StudentProjectTags 
        add constraint FK_dtx3jka5ns3h5ntixwifloqc2 
        foreign key (tag) 
        references Tag;

    alter table __StudentProjectTags 
        add constraint FK_16emgsigygw6th8y8indckbjv 
        foreign key (studentProject) 
        references StudentProject;

    alter table __StudentTags 
        add constraint FK_7pip1b30oq902tv535xbor8n4 
        foreign key (tag) 
        references Tag;

    alter table __StudentTags 
        add constraint FK_4bbesi5caq54wpx5ivjx23mco 
        foreign key (student) 
        references Student;

    alter table __UserBillingDetails 
        add constraint FK_bj58cvcye8yvguabaacs0fw71 
        foreign key (billingDetails) 
        references BillingDetails;

    alter table __UserBillingDetails 
        add constraint FK_51scxf6eoh95igh7qsvi083dp 
        foreign key (user) 
        references User;

    alter table __UserTags 
        add constraint FK_r2nt0vyhb81p3a677ls3rskn5 
        foreign key (tag) 
        references Tag;

    alter table __UserTags 
        add constraint FK_cd49dr1pn4dybqibkwedyfl77 
        foreign key (user) 
        references User;

    create table hibernate_sequences (
         sequence_name varchar(255),
         sequence_next_hi_value integer 
    );