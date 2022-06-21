
    create table __ApplicationNotificationUsers (
       notification bigint not null,
        user bigint not null,
        primary key (notification, user)
    ) engine=InnoDB;

    create table __CourseBaseCurriculums (
       courseBase bigint not null,
        curriculum bigint not null,
        primary key (courseBase, curriculum)
    ) engine=InnoDB;

    create table __CourseTags (
       course bigint not null,
        tag bigint not null,
        primary key (course, tag)
    ) engine=InnoDB;

    create table __HelpItemTags (
       helpItem bigint not null,
        tag bigint not null,
        primary key (helpItem, tag)
    ) engine=InnoDB;

    create table __ModuleTags (
       module bigint not null,
        tag bigint not null,
        primary key (module, tag)
    ) engine=InnoDB;

    create table __ProjectTags (
       project bigint not null,
        tag bigint not null,
        primary key (project, tag)
    ) engine=InnoDB;

    create table __ResourceTags (
       resource bigint not null,
        tag bigint not null,
        primary key (resource, tag)
    ) engine=InnoDB;

    create table __SchoolTags (
       school bigint not null,
        tag bigint not null,
        primary key (school, tag)
    ) engine=InnoDB;

    create table __StudentGroupTags (
       studentGroup bigint not null,
        tag bigint not null,
        primary key (studentGroup, tag)
    ) engine=InnoDB;

    create table __StudentProjectTags (
       studentProject bigint not null,
        tag bigint not null,
        primary key (studentProject, tag)
    ) engine=InnoDB;

    create table __UserTags (
       user bigint not null,
        tag bigint not null,
        primary key (user, tag)
    ) engine=InnoDB;

    create table AcademicTerm (
       id bigint not null,
        archived bit not null,
        endDate date not null,
        name varchar(255) not null,
        startDate date not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table AccessLogEntry (
       id bigint not null,
        date datetime not null,
        ip varchar(255) not null,
        parameters longtext,
        path bigint,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table AccessLogEntryPath (
       id bigint not null,
        active bit not null,
        path longtext not null,
        primary key (id)
    ) engine=InnoDB;

    create table Address (
       id bigint not null,
        city varchar(255),
        country varchar(255),
        defaultAddress bit not null,
        name varchar(255),
        postalCode varchar(255),
        streetAddress varchar(255),
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table Application (
       id bigint not null,
        applicantEditable bit not null,
        applicantLastModified datetime not null,
        applicationId varchar(255) not null,
        archived bit not null,
        created datetime not null,
        credentialToken varchar(255),
        email varchar(255) not null,
        firstName varchar(255) not null,
        formData longtext not null,
        lastModified datetime not null,
        lastName varchar(255) not null,
        line varchar(255) not null,
        referenceCode varchar(255) not null,
        state varchar(255),
        handler bigint,
        lastModifier bigint,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ApplicationAttachment (
       id bigint not null,
        applicationId varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        size integer not null,
        primary key (id)
    ) engine=InnoDB;

    create table ApplicationLog (
       id bigint not null,
        archived bit not null,
        date datetime not null,
        text longtext not null,
        type varchar(255) not null,
        application bigint,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ApplicationMailTemplate (
       id bigint not null,
        archived bit not null,
        content longtext not null,
        line varchar(255),
        name varchar(255) not null,
        subject varchar(255),
        staffMember bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ApplicationNotification (
       id bigint not null,
        line varchar(255),
        state varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table ApplicationSignatures (
       id bigint not null,
        applicantDocumentId varchar(255),
        applicantDocumentModified datetime,
        applicantDocumentState varchar(255),
        applicantInvitationId varchar(255),
        applicantInvitationToken varchar(255),
        staffDocumentId varchar(255),
        staffDocumentModified datetime,
        staffDocumentState varchar(255),
        staffInvitationId varchar(255),
        staffInvitationToken varchar(255),
        application bigint,
        primary key (id)
    ) engine=InnoDB;

    create table AuditLog (
       id bigint not null auto_increment,
        authorId bigint,
        data varchar(255),
        date datetime not null,
        entityId bigint,
        field varchar(255),
        personId bigint,
        target varchar(255) not null,
        type varchar(255) not null,
        userId bigint,
        primary key (id)
    ) engine=InnoDB;

    create table BasicCourseResource (
       id bigint not null,
        hourlyCost_amount double precision,
        hourlyCost_currency varchar(255),
        hours double precision not null,
        unitCost_amount double precision,
        unitCost_currency varchar(255),
        units integer not null,
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    ) engine=InnoDB;

    create table BillingDetails (
       id bigint not null,
        city varchar(255),
        companyIdentifier varchar(255),
        companyName varchar(255),
        country varchar(255),
        electronicBillingAddress longtext,
        electronicBillingOperator varchar(255),
        emailAddress varchar(255),
        notes longtext,
        personName varchar(255),
        phoneNumber varchar(255),
        postalCode varchar(255),
        referenceNumber varchar(255),
        region varchar(255),
        streetAddress1 varchar(255),
        streetAddress2 varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table ChangeLogEntry (
       id bigint not null,
        entityId varchar(255),
        time datetime,
        type varchar(255) not null,
        entity bigint,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ChangeLogEntryEntity (
       id bigint not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table ChangeLogEntryEntityProperty (
       id bigint not null,
        name varchar(255),
        entity bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ChangeLogEntryProperty (
       id bigint not null,
        value longtext,
        entry bigint,
        property bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ClientApplication (
       id bigint not null,
        clientId varchar(255) not null,
        clientName varchar(255) not null,
        clientSecret varchar(255) not null,
        skipPrompt bit not null,
        primary key (id)
    ) engine=InnoDB;

    create table ClientApplicationAccessToken (
       id bigint not null,
        accessToken varchar(255) not null,
        expires bigint not null,
        refreshToken varchar(255) not null,
        app_id bigint not null,
        clientApplicationAuthorizationCode bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ClientApplicationAuthorizationCode (
       id bigint not null,
        authorizationCode varchar(255) not null,
        redirectUrl varchar(255) not null,
        app_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ComponentBase (
       id bigint not null,
        archived bit not null,
        description longtext,
        name varchar(255) not null,
        version bigint not null,
        length bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Configuration (
       id bigint not null auto_increment,
        name varchar(255) not null,
        value longtext,
        primary key (id)
    ) engine=InnoDB;

    create table ContactInfo (
       id bigint not null,
        additionalInfo longtext,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ContactType (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        nonUnique bit not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ContactURL (
       id bigint not null,
        url varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        contactURLType bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table ContactURLType (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Course (
       assessingHours double precision,
        beginDate date,
        courseFee decimal(19,2),
        courseFeeCurrency varchar(255),
        courseTemplate bit not null,
        distanceTeachingDays double precision,
        distanceTeachingHours double precision,
        endDate date,
        enrolmentTimeEnd datetime,
        localTeachingDays double precision,
        nameExtension varchar(255),
        planningHours double precision,
        teachingHours double precision,
        id bigint not null,
        module bigint,
        organization bigint,
        state bigint,
        type_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseAssessment (
       id bigint not null,
        courseStudent bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseAssessmentRequest (
       id bigint not null,
        archived bit not null,
        created datetime not null,
        handled bit not null,
        requestText longtext,
        courseStudent bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseBase (
       id bigint not null,
        archived bit not null,
        courseNumber integer,
        created datetime not null,
        description longtext,
        lastModified datetime not null,
        maxParticipantCount bigint,
        name varchar(255) not null,
        version bigint not null,
        courseLength bigint,
        creator bigint,
        lastModifier bigint,
        subject bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseBaseVariable (
       id bigint not null,
        value varchar(255),
        version bigint not null,
        courseBase bigint,
        variableKey bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseBaseVariableKey (
       id bigint not null,
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseComponent (
       id bigint not null,
        course bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table CourseComponentResource (
       id bigint not null,
        usagePercent double precision not null,
        courseComponent bigint,
        resource bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseDescription (
       id bigint not null,
        description longtext,
        category bigint,
        courseBase bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseDescriptionCategory (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseEducationSubtype (
       id bigint not null,
        version bigint not null,
        courseEducationType bigint,
        educationSubtype bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseEducationType (
       id bigint not null,
        version bigint not null,
        courseBase bigint,
        educationType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseEnrolmentType (
       id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseParticipationType (
       id bigint not null,
        archived bit not null,
        indexColumn integer not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseSignupStudentGroup (
       id bigint not null auto_increment,
        course bigint,
        studentGroup bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseSignupStudyProgramme (
       id bigint not null auto_increment,
        course bigint,
        studyProgramme bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseStaffMember (
       id bigint not null,
        role_id bigint,
        staffMember_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseStaffMemberRole (
       id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseState (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseStudent (
       additionalInfo varchar(255),
        archived bit not null,
        enrolmentTime datetime not null,
        lodging bit not null,
        lodgingFee decimal(19,2),
        lodgingFeeCurrency varchar(255),
        optionality varchar(255),
        organization varchar(255),
        reservationFee decimal(19,2),
        reservationFeeCurrency varchar(255),
        roomAdditionalInfo varchar(255),
        id bigint not null,
        billingDetails bigint,
        enrolmentType bigint,
        participationType bigint,
        room_id bigint,
        student bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseStudentVariable (
       id bigint not null,
        value longtext not null,
        version bigint not null,
        courseStudent bigint,
        variableKey bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CourseStudentVariableKey (
       id bigint not null,
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseType (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table CourseUser (
       id bigint not null,
        version bigint not null,
        course bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Credit (
       id bigint not null,
        archived bit not null,
        creditType varchar(255) not null,
        date datetime not null,
        verbalAssessment longtext,
        version bigint not null,
        assessor_id bigint,
        grade bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CreditLink (
       id bigint not null,
        archived bit not null,
        created datetime,
        creator bigint,
        credit bigint,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CreditVariable (
       id bigint not null,
        value longtext not null,
        version bigint not null,
        credit bigint,
        variableKey bigint,
        primary key (id)
    ) engine=InnoDB;

    create table CreditVariableKey (
       id bigint not null,
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Curriculum (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table Defaults (
       id bigint not null,
        version bigint not null,
        educationalTimeUnit bigint,
        courseEnrolmentType bigint,
        courseParticipationType bigint,
        courseState bigint,
        organization bigint,
        studentDefaultContactType bigint,
        userDefaultContactType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table EducationalLength (
       id bigint not null,
        units double precision not null,
        version bigint not null,
        unit bigint,
        primary key (id)
    ) engine=InnoDB;

    create table EducationalTimeUnit (
       id bigint not null,
        archived bit not null,
        baseUnits double precision not null,
        name varchar(255) not null,
        symbol varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table EducationSubtype (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table EducationType (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Email (
       id bigint not null,
        address varchar(255) not null,
        defaultAddress bit not null,
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table EmailSignature (
       id bigint not null,
        signature longtext,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table EnvironmentRolePermission (
       id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table File (
       id bigint not null,
        archived bit not null,
        contentType varchar(255),
        created datetime not null,
        data longblob,
        fileId varchar(255),
        fileName varchar(255),
        lastModified datetime not null,
        name varchar(255),
        creator bigint,
        fileType bigint,
        lastModifier bigint,
        primary key (id)
    ) engine=InnoDB;

    create table FileType (
       id bigint not null,
        archived bit not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table FormDraft (
       id bigint not null,
        created datetime,
        data longtext,
        modified datetime,
        url varchar(255),
        version bigint not null,
        creator bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Grade (
       id bigint not null,
        GPA double precision,
        archived bit not null,
        description varchar(255),
        name varchar(255) not null,
        passingGrade bit not null,
        qualification varchar(255),
        version bigint not null,
        gradingScale bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table GradeCourseResource (
       id bigint not null,
        hourlyCost_amount double precision,
        hourlyCost_currency varchar(255),
        hours double precision not null,
        unitCost_amount double precision,
        unitCost_currency varchar(255),
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    ) engine=InnoDB;

    create table GradingScale (
       id bigint not null,
        archived bit,
        description longtext,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table HelpFolder (
       id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table HelpItem (
       id bigint not null,
        created datetime not null,
        indexColumn integer not null,
        lastModified datetime not null,
        creator bigint,
        lastModifier bigint,
        parent bigint,
        primary key (id)
    ) engine=InnoDB;

    create table HelpItemTitle (
       id bigint not null,
        created datetime not null,
        lastModified datetime not null,
        locale varchar(255) not null,
        title varchar(255) not null,
        creator bigint,
        item bigint,
        lastModifier bigint,
        primary key (id)
    ) engine=InnoDB;

    create table HelpPage (
       id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table HelpPageContent (
       id bigint not null,
        content longtext not null,
        created datetime not null,
        lastModified datetime not null,
        locale varchar(255) not null,
        creator bigint,
        lastModifier bigint,
        page bigint,
        primary key (id)
    ) engine=InnoDB;

    create table hibernate_sequences (
       sequence_name varchar(255) not null,
        sequence_next_hi_value bigint,
        primary key (sequence_name)
    ) engine=InnoDB;

    create table InternalAuth (
       id bigint not null,
        password varchar(255) not null,
        username varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table KoskiPersonLog (
       id bigint not null auto_increment,
        date datetime not null,
        message longtext,
        state varchar(255),
        person bigint not null,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Language (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table LoginLog (
       id bigint not null,
        date datetime not null,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table MagicKey (
       id bigint not null,
        created datetime not null,
        name varchar(255) not null,
        scope varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table MaterialResource (
       unitCost_amount double precision,
        unitCost_currency varchar(255),
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table MatriculationExam (
       id bigint not null,
        ends datetime not null,
        enrollmentActive bit not null,
        examTerm varchar(255),
        examYear integer,
        starts datetime not null,
        version bigint not null,
        signupGrade_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table MatriculationExamAttendance (
       id bigint not null,
        funding varchar(255),
        grade varchar(255),
        mandatory bit,
        retry bit,
        status varchar(255),
        subject varchar(255),
        term integer,
        year integer,
        enrollment_id bigint,
        projectAssessment_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table MatriculationExamEnrollment (
       id bigint not null,
        address varchar(255),
        approvedByGuider bit not null,
        canPublishName bit not null,
        candidateNumber integer,
        city varchar(255),
        degreeStructure varchar(255) not null,
        degreeType varchar(255),
        email varchar(255),
        enrollAs varchar(255),
        enrollmentDate datetime,
        guider varchar(255),
        location varchar(255),
        message longtext,
        name varchar(255),
        nationalStudentNumber bigint,
        numMandatoryCourses integer,
        phone varchar(255),
        postalCode varchar(255),
        restartExam bit not null,
        ssn varchar(255),
        state varchar(255) not null,
        exam_id bigint,
        student_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table MatriculationExamSubjectSettings (
       id bigint not null auto_increment,
        examDate datetime,
        subject varchar(255) not null,
        exam_id bigint,
        project_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Module (
       id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ModuleComponent (
       id bigint not null,
        module bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table Municipality (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Nationality (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Organization (
       id bigint not null auto_increment,
        archived bit not null,
        name varchar(255) not null,
        billingDetails bigint,
        educationType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table OrganizationContactPerson (
       id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        title varchar(255),
        type varchar(255),
        organization bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table OrganizationContractPeriod (
       id bigint not null auto_increment,
        begin date not null,
        end date,
        organization bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table OtherCost (
       id bigint not null,
        cost_amount double precision,
        cost_currency varchar(255),
        name varchar(255) not null,
        version bigint not null,
        course bigint,
        primary key (id)
    ) engine=InnoDB;

    create table PasswordResetRequest (
       id bigint not null,
        date datetime not null,
        secret varchar(255) not null,
        person_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Permission (
       id bigint not null,
        name varchar(255) not null,
        scope varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table Person (
       id bigint not null,
        basicInfo longtext,
        birthday date,
        secureInfo bit not null,
        sex varchar(255),
        socialSecurityNumber varchar(255),
        version bigint not null,
        defaultUser_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table PersonVariable (
       id bigint not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        person bigint,
        primary key (id)
    ) engine=InnoDB;

    create table PersonVariableKey (
       id bigint not null,
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table PhoneNumber (
       id bigint not null,
        defaultNumber bit not null,
        number varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        contactType bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table Plugin (
       id bigint not null,
        artifactId varchar(255) not null,
        enabled bit not null,
        groupId varchar(255) not null,
        version varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table PluginRepository (
       id bigint not null,
        repositoryId varchar(255) not null,
        url varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table Project (
       id bigint not null,
        archived bit not null,
        created datetime not null,
        description longtext,
        lastModified datetime not null,
        name varchar(255) not null,
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        optionalStudies bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ProjectAssessment (
       id bigint not null,
        studentProject bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ProjectModule (
       id bigint not null,
        optionality varchar(255) not null,
        version bigint not null,
        module bigint,
        project bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table Report (
       id bigint not null,
        archived bit not null,
        created datetime not null,
        data longtext not null,
        lastModified datetime not null,
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        creator bigint,
        lastModifier bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ReportCategory (
       id bigint not null,
        archived bit not null,
        indexColumn integer,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table ReportContext (
       id bigint not null,
        context varchar(255) not null,
        report bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Resource (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        primary key (id)
    ) engine=InnoDB;

    create table ResourceCategory (
       id bigint not null,
        archived bit not null,
        name varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table RolePermission (
       id bigint not null,
        role varchar(255) not null,
        permission_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Room (
       id bigint not null,
        name varchar(255) not null,
        type_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table RoomType (
       id bigint not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table School (
       id bigint not null,
        archived bit not null,
        code varchar(255) not null,
        name varchar(255) not null,
        version bigint not null,
        billingDetails bigint,
        contactInfo bigint,
        field bigint,
        primary key (id)
    ) engine=InnoDB;

    create table SchoolField (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table SchoolVariable (
       id bigint not null,
        archived bit not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        school bigint,
        primary key (id)
    ) engine=InnoDB;

    create table SchoolVariableKey (
       id bigint not null,
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Setting (
       id bigint not null,
        value varchar(255),
        settingKey bigint,
        primary key (id)
    ) engine=InnoDB;

    create table SettingKey (
       id bigint not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table StaffMember (
       role varchar(255) not null,
        title varchar(255),
        id bigint not null,
        organization bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StaffMemberProperties (
       staffMember_id bigint not null,
        value varchar(255),
        name varchar(100) not null,
        primary key (staffMember_id, name)
    ) engine=InnoDB;

    create table Student (
       additionalInfo longtext,
        education varchar(255),
        funding varchar(255),
        nickname varchar(255),
        previousStudies double precision,
        studyEndDate date,
        studyEndText varchar(255),
        studyStartDate date,
        studyTimeEnd date,
        id bigint not null,
        activityType bigint,
        curriculum_id bigint,
        educationalLevel bigint,
        examinationType bigint,
        language bigint,
        municipality bigint,
        nationality bigint,
        school bigint,
        studyApprover bigint,
        studyEndReason bigint,
        studyProgramme bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentActivityType (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentContactLogEntry (
       id bigint not null,
        archived bit not null,
        creatorName varchar(255),
        entryDate datetime,
        text longtext,
        type varchar(255),
        version bigint not null,
        student bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentContactLogEntryComment (
       id bigint not null,
        archived bit not null,
        commentDate datetime,
        creatorName varchar(255),
        text longtext,
        version bigint not null,
        entry bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentCourseResource (
       id bigint not null,
        hourlyCost_amount double precision,
        hourlyCost_currency varchar(255),
        hours double precision not null,
        unitCost_amount double precision,
        unitCost_currency varchar(255),
        version bigint not null,
        course bigint,
        resource bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentEducationalLevel (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentExaminationType (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentFile (
       id bigint not null,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentGroup (
       id bigint not null,
        archived bit not null,
        beginDate datetime,
        created datetime not null,
        description longtext,
        guidanceGroup bit not null,
        lastModified datetime not null,
        name varchar(255) not null,
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        organization bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentGroupContactLogEntry (
       id bigint not null,
        archived bit not null,
        creatorName varchar(255),
        entryDate datetime,
        text longtext,
        type varchar(255),
        version bigint not null,
        studentGroup bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentGroupContactLogEntryComment (
       id bigint not null,
        archived bit not null,
        commentDate datetime,
        creatorName varchar(255),
        text longtext,
        version bigint not null,
        entry bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentGroupStudent (
       id bigint not null,
        version bigint not null,
        student bigint,
        studentGroup bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentGroupUser (
       id bigint not null,
        version bigint not null,
        staffMember_id bigint,
        studentGroup bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentImage (
       id bigint not null,
        contentType varchar(255),
        data longblob,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentLodgingPeriod (
       id bigint not null auto_increment,
        begin date not null,
        end date,
        student bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentProject (
       id bigint not null,
        archived bit not null,
        created datetime not null,
        description longtext,
        lastModified datetime not null,
        name varchar(255) not null,
        optionality varchar(255),
        version bigint not null,
        creator bigint,
        lastModifier bigint,
        optionalStudies bigint,
        project bigint,
        student bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentProjectModule (
       id bigint not null,
        optionality varchar(255) not null,
        version bigint not null,
        academicTerm bigint,
        module bigint,
        studentProject bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table StudentStudyEndReason (
       id bigint not null,
        name varchar(255),
        version bigint not null,
        parentReason bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudentStudyEndReasonProperties (
       studentStudyEndReason_id bigint not null,
        value varchar(255),
        name varchar(100) not null,
        primary key (studentStudyEndReason_id, name)
    ) engine=InnoDB;

    create table StudentStudyPeriod (
       id bigint not null auto_increment,
        begin date not null,
        end date,
        periodType varchar(255) not null,
        student bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudentSubjectGrade (
       id bigint not null auto_increment,
        explanation longtext,
        gradeDate date,
        grade bigint not null,
        gradeApprover bigint,
        issuer bigint not null,
        student bigint not null,
        subject bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table StudyProgramme (
       id bigint not null,
        archived bit not null,
        code varchar(255),
        hasEvaluationFees bit not null,
        name varchar(255) not null,
        version bigint not null,
        category bigint,
        organization bigint,
        primary key (id)
    ) engine=InnoDB;

    create table StudyProgrammeCategory (
       id bigint not null,
        archived bit not null,
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Subject (
       id bigint not null,
        archived bit not null,
        code varchar(255),
        name varchar(255) not null,
        version bigint not null,
        educationType bigint,
        primary key (id)
    ) engine=InnoDB;

    create table Tag (
       id bigint not null,
        text varchar(255) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table TrackedEntityProperty (
       id bigint not null,
        entity varchar(255),
        property varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table TransferCredit (
       courseName varchar(255) not null,
        courseNumber integer,
        funding varchar(255),
        offCurriculum bit not null,
        optionality varchar(255),
        id bigint not null,
        courseLength bigint,
        curriculum_id bigint,
        school bigint,
        student bigint,
        subject bigint,
        primary key (id)
    ) engine=InnoDB;

    create table TransferCreditTemplate (
       id bigint not null,
        name varchar(255) not null,
        version bigint not null,
        curriculum_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table TransferCreditTemplateCourse (
       id bigint not null,
        courseName varchar(255) not null,
        courseNumber integer,
        optionality varchar(255) not null,
        version bigint not null,
        courseLength bigint,
        curriculum_id bigint,
        subject bigint,
        transferCreditTemplate bigint,
        indexColumn integer,
        primary key (id)
    ) engine=InnoDB;

    create table User (
       id bigint not null,
        archived bit not null,
        firstName varchar(255) not null,
        lastName varchar(255) not null,
        version bigint not null,
        contactInfo bigint,
        person_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table UserIdentification (
       id bigint not null,
        authSource varchar(255) not null,
        externalId varchar(255) not null,
        person_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table UserVariable (
       id bigint not null,
        value varchar(255),
        version bigint not null,
        variableKey bigint,
        user bigint,
        primary key (id)
    ) engine=InnoDB;

    create table UserVariableKey (
       id bigint not null,
        defaultValueOnCreation varchar(255),
        userEditable bit not null,
        variableKey varchar(255) not null,
        variableName varchar(255) not null,
        variableType varchar(255),
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table Webhook (
       id bigint not null,
        secret varchar(255) not null,
        url varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table WorklistBillingSettings (
       id bigint not null auto_increment,
        settings longtext,
        primary key (id)
    ) engine=InnoDB;

    create table WorklistItem (
       id bigint not null auto_increment,
        archived bit not null,
        billingNumber varchar(255),
        created datetime not null,
        description varchar(255) not null,
        editableFields varchar(255),
        entryDate date not null,
        factor double precision not null,
        modified datetime not null,
        price double precision not null,
        state varchar(255),
        courseAssessment_id bigint,
        creator_id bigint,
        modifier_id bigint,
        owner_id bigint,
        template_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table WorklistItemTemplate (
       id bigint not null auto_increment,
        archived bit not null,
        billingNumber varchar(255),
        description varchar(255) not null,
        editableFields varchar(255),
        factor double precision,
        price double precision,
        removable bit not null,
        templateType varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table WorkResource (
       costPerUse_amount double precision,
        costPerUse_currency varchar(255),
        hourlyCost_amount double precision,
        hourlyCost_currency varchar(255),
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    alter table Application 
       add constraint UK_tageinnby3smv061oabxl7qgt unique (applicationId);

    alter table ChangeLogEntryEntity 
       add constraint UK_3ivxxlmh3gjyajo41lt19r30j unique (name);

    alter table ClientApplication 
       add constraint UK_gon59vs1pdxyqysd5etdnn1n9 unique (clientId);

    alter table ClientApplicationAccessToken 
       add constraint UK_2eepl5h88wfb22nxhcd2rsxgh unique (accessToken);

    alter table ClientApplicationAccessToken 
       add constraint UK_rwdu61j1a1rn11pqv0ii1qdgu unique (clientApplicationAuthorizationCode);

    alter table Configuration 
       add constraint UK_sham1yps58yd0i428x5sk3uns unique (name);

    alter table CourseSignupStudentGroup 
       add constraint UK6s5qhjuyvshivw8wrlesqbse8 unique (course, studentGroup);

    alter table CourseSignupStudyProgramme 
       add constraint UK4x08yp3uxmjhf7rxnnj5y5jev unique (course, studyProgramme);

    alter table File 
       add constraint UKkth22wlka5tgycx6gxxid24v2 unique (fileId);

    alter table InternalAuth 
       add constraint UK_9i2dkw43taedavb18gke1ddmt unique (username);

    alter table MagicKey 
       add constraint UK_3c73v1x7o1a8b2bqcaojh4bq7 unique (name);

    alter table MatriculationExamSubjectSettings 
       add constraint UK1rkucrwue5jyrn8ja2h42u3r3 unique (exam_id, subject);

    alter table Permission 
       add constraint UK_m3j6m9ksltume23qomatoes1r unique (name);

    alter table PersonVariableKey 
       add constraint UK_jwbaj3vwdhakjj5kpc25d9bkh unique (variableKey);

    alter table PluginRepository 
       add constraint UK_oa6tjb0q8x7s16o1i59s15981 unique (repositoryId);

    alter table PluginRepository 
       add constraint UK_g5y4nx9re8jyupfge8su2s23w unique (url);

    alter table SchoolVariableKey 
       add constraint UK_4s9uh8gogc0fypg6yk03fqlh0 unique (variableKey);

    alter table SettingKey 
       add constraint UK_rdcw2wxypyku3249noelqeoj7 unique (name);

    alter table Tag 
       add constraint UK_am6x1sovg4dm5l9l581e0ckot unique (text);

    alter table UserVariableKey 
       add constraint UK_mb9er7y1ali34igjlk4ccxlfs unique (variableKey);

    alter table Webhook 
       add constraint UK_dk6f5jw076amfqpx329q009yq unique (url);

    alter table __ApplicationNotificationUsers 
       add constraint FKg85k5saoglogf2c5i3qxnhmqt 
       foreign key (user) 
       references User (id);

    alter table __ApplicationNotificationUsers 
       add constraint FKhu8x5do7vdnujss0xajy9wl9g 
       foreign key (notification) 
       references ApplicationNotification (id);

    alter table __CourseBaseCurriculums 
       add constraint FK7p25ednd26krp6dtgn6qbj29q 
       foreign key (curriculum) 
       references Curriculum (id);

    alter table __CourseBaseCurriculums 
       add constraint FK5slvhnjhhwbap5u9fxhm87hp6 
       foreign key (courseBase) 
       references CourseBase (id);

    alter table __CourseTags 
       add constraint FKocry36u4m3j6ohris3udjpr8a 
       foreign key (tag) 
       references Tag (id);

    alter table __CourseTags 
       add constraint FKpx3stlf1cpok871yxi69ndud1 
       foreign key (course) 
       references Course (id);

    alter table __HelpItemTags 
       add constraint FKqafq2eb63k6maeocfslbbk5t0 
       foreign key (tag) 
       references Tag (id);

    alter table __HelpItemTags 
       add constraint FKov0d3ar0prxgg7t4xr9yl5gt1 
       foreign key (helpItem) 
       references HelpItem (id);

    alter table __ModuleTags 
       add constraint FKt7p1qt5ng6yb34pik5gfylhm5 
       foreign key (tag) 
       references Tag (id);

    alter table __ModuleTags 
       add constraint FKrmepo6qrqw0qdu6wnstsu30j4 
       foreign key (module) 
       references Module (id);

    alter table __ProjectTags 
       add constraint FKlvnqnu285ajmo1e36gerkyiya 
       foreign key (tag) 
       references Tag (id);

    alter table __ProjectTags 
       add constraint FK9sr0hl9k4b760xndtis6unr4c 
       foreign key (project) 
       references Project (id);

    alter table __ResourceTags 
       add constraint FKpf6908xeunav7fi8090i35xqy 
       foreign key (tag) 
       references Tag (id);

    alter table __ResourceTags 
       add constraint FK6pb0j818eubw9smrmue1wn2s2 
       foreign key (resource) 
       references Resource (id);

    alter table __SchoolTags 
       add constraint FKj992ty7rvk32og7wq7ou3i7jg 
       foreign key (tag) 
       references Tag (id);

    alter table __SchoolTags 
       add constraint FKfawpw7nm36lxklt26hrpv2dvu 
       foreign key (school) 
       references School (id);

    alter table __StudentGroupTags 
       add constraint FKoieawhho5d1pmgwlmxribqnc0 
       foreign key (tag) 
       references Tag (id);

    alter table __StudentGroupTags 
       add constraint FK4tvcdd0t00dymgwxwk1eouv4b 
       foreign key (studentGroup) 
       references StudentGroup (id);

    alter table __StudentProjectTags 
       add constraint FKpk6qhtxhju0ntv9o0hdxeb5yi 
       foreign key (tag) 
       references Tag (id);

    alter table __StudentProjectTags 
       add constraint FK7r39a93o6iihuaqfyuy2cd9ok 
       foreign key (studentProject) 
       references StudentProject (id);

    alter table __UserTags 
       add constraint FKo0ogv6n5pfsmaksh0v54t0960 
       foreign key (tag) 
       references Tag (id);

    alter table __UserTags 
       add constraint FKn34sw0j1d44qn4h5hb4cg4a2f 
       foreign key (user) 
       references User (id);

    alter table AccessLogEntry 
       add constraint FKa6jjcasuh1sr2ugfocnnnxgbh 
       foreign key (path) 
       references AccessLogEntryPath (id);

    alter table AccessLogEntry 
       add constraint FK4k1w14h82rgy6812868ea3p3f 
       foreign key (user) 
       references User (id);

    alter table Address 
       add constraint FKkiddtym4763wa0sh35g35xqph 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table Address 
       add constraint FKjvs9xpb8xs5ic2cddk4xbyvni 
       foreign key (contactType) 
       references ContactType (id);

    alter table Application 
       add constraint FKpyvfpph539631fxg1w1fol3mx 
       foreign key (handler) 
       references StaffMember (id);

    alter table Application 
       add constraint FKc3glhha6int04qu1e1f0wy8l1 
       foreign key (lastModifier) 
       references User (id);

    alter table Application 
       add constraint FKg51n4jp76of7xi3y3gq97a4be 
       foreign key (student) 
       references Student (id);

    alter table ApplicationLog 
       add constraint FKqpws7go21y3bqq2f924nwwh75 
       foreign key (application) 
       references Application (id);

    alter table ApplicationLog 
       add constraint FKo8fwu0cbku555dssnpal5jla2 
       foreign key (user) 
       references User (id);

    alter table ApplicationMailTemplate 
       add constraint FKljheomvfy9b9oa1l88f6l3xuk 
       foreign key (staffMember) 
       references StaffMember (id);

    alter table ApplicationSignatures 
       add constraint FKhaly5cm30610fke6fb8vkpar7 
       foreign key (application) 
       references Application (id);

    alter table BasicCourseResource 
       add constraint FKs95mn1y3mj8thkgcvipbd1xq3 
       foreign key (course) 
       references Course (id);

    alter table BasicCourseResource 
       add constraint FKp6wuji95bmf632g9iymg55kut 
       foreign key (resource) 
       references Resource (id);

    alter table ChangeLogEntry 
       add constraint FK687use4nddiojxi9vjh3butyj 
       foreign key (entity) 
       references ChangeLogEntryEntity (id);

    alter table ChangeLogEntry 
       add constraint FKblr0jmaalpmta9lr3mnkaaooq 
       foreign key (user) 
       references User (id);

    alter table ChangeLogEntryEntityProperty 
       add constraint FKhvt25yu4xplnp8kehuvejmltn 
       foreign key (entity) 
       references ChangeLogEntryEntity (id);

    alter table ChangeLogEntryProperty 
       add constraint FKqa8jwjoetdccrecj7rwtsevh5 
       foreign key (entry) 
       references ChangeLogEntry (id);

    alter table ChangeLogEntryProperty 
       add constraint FKalqkuxgt2xxuq5urcmb4m1jht 
       foreign key (property) 
       references ChangeLogEntryEntityProperty (id);

    alter table ClientApplicationAccessToken 
       add constraint FKj25sd446aqsnnr0vf0rghvjv4 
       foreign key (app_id) 
       references ClientApplication (id);

    alter table ClientApplicationAccessToken 
       add constraint FKpxrakqfm2bw8b5t9ptf788qqu 
       foreign key (clientApplicationAuthorizationCode) 
       references ClientApplicationAuthorizationCode (id);

    alter table ClientApplicationAuthorizationCode 
       add constraint FK8ong6metbfvnm8ptdhbx1t58h 
       foreign key (app_id) 
       references ClientApplication (id);

    alter table ClientApplicationAuthorizationCode 
       add constraint FKa2ukjw2djbnf0xfi65sd6dxc9 
       foreign key (user_id) 
       references User (id);

    alter table ComponentBase 
       add constraint FK8irnev0q8i4kkxerda7fe69je 
       foreign key (length) 
       references EducationalLength (id);

    alter table ContactURL 
       add constraint FK9rln0my3lfncgd6k93m9dqphe 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table ContactURL 
       add constraint FKbp5h4poqfapxakkyaopdnk32v 
       foreign key (contactURLType) 
       references ContactURLType (id);

    alter table Course 
       add constraint FKiyjgk52cio1qip3t543a26t8x 
       foreign key (module) 
       references Module (id);

    alter table Course 
       add constraint FKmdy02c6ndkcx0ewxi9dxjewv2 
       foreign key (organization) 
       references Organization (id);

    alter table Course 
       add constraint FKiyiuwdktjua1hn7bqf970etl9 
       foreign key (state) 
       references CourseState (id);

    alter table Course 
       add constraint FKg72ycx741dbhpj3xl2cwee9qq 
       foreign key (type_id) 
       references CourseType (id);

    alter table Course 
       add constraint FK80sthkv3vgf00udu349qd93lg 
       foreign key (id) 
       references CourseBase (id);

    alter table CourseAssessment 
       add constraint FK36trbdwac7x0lh4qxiaqo4sri 
       foreign key (courseStudent) 
       references CourseStudent (id);

    alter table CourseAssessment 
       add constraint FKkhmlmo7p2jfsvyko40s79f62t 
       foreign key (id) 
       references Credit (id);

    alter table CourseAssessmentRequest 
       add constraint FKgd1ytjg0e9gwgesj48uqadoho 
       foreign key (courseStudent) 
       references CourseStudent (id);

    alter table CourseBase 
       add constraint FKp44u27sminfuier5bcw86jep6 
       foreign key (courseLength) 
       references EducationalLength (id);

    alter table CourseBase 
       add constraint FKmkt8t06ingoy3c5rf6p3lfk9w 
       foreign key (creator) 
       references User (id);

    alter table CourseBase 
       add constraint FKgiabnk7ggbvtjfir6d3quhoee 
       foreign key (lastModifier) 
       references User (id);

    alter table CourseBase 
       add constraint FKo9d38wne4pdv3ija957h47j56 
       foreign key (subject) 
       references Subject (id);

    alter table CourseBaseVariable 
       add constraint FK8td9hjoro2ti745qjk4b1vy8r 
       foreign key (courseBase) 
       references CourseBase (id);

    alter table CourseBaseVariable 
       add constraint FKcpvlqqx203bodywa8xrjjkw7b 
       foreign key (variableKey) 
       references CourseBaseVariableKey (id);

    alter table CourseComponent 
       add constraint FKluq35h14deqhjnpwg3w837gr2 
       foreign key (course) 
       references Course (id);

    alter table CourseComponent 
       add constraint FKh8o4no5ooyhyxbyngn76ywis6 
       foreign key (id) 
       references ComponentBase (id);

    alter table CourseComponentResource 
       add constraint FKge00t7t19wm3s28sc5djkorpv 
       foreign key (courseComponent) 
       references CourseComponent (id);

    alter table CourseComponentResource 
       add constraint FKja26cq093ederklwrd6gnfyfn 
       foreign key (resource) 
       references Resource (id);

    alter table CourseDescription 
       add constraint FKljotokbv4ydi3spq8fgsokjwr 
       foreign key (category) 
       references CourseDescriptionCategory (id);

    alter table CourseDescription 
       add constraint FKf00bbxan4go6c2vklm789qbrw 
       foreign key (courseBase) 
       references CourseBase (id);

    alter table CourseEducationSubtype 
       add constraint FK5wfpk0qsmd6sgckim17xvpwkr 
       foreign key (courseEducationType) 
       references CourseEducationType (id);

    alter table CourseEducationSubtype 
       add constraint FK59ncivov5j84a2n5d4fp6c40q 
       foreign key (educationSubtype) 
       references EducationSubtype (id);

    alter table CourseEducationType 
       add constraint FKrm1y1ojxvtdw42u8v0alydyjw 
       foreign key (courseBase) 
       references CourseBase (id);

    alter table CourseEducationType 
       add constraint FK1q89k87rh4ee5ecmhyd0h1hqe 
       foreign key (educationType) 
       references EducationType (id);

    alter table CourseSignupStudentGroup 
       add constraint FKkx75wvsff8sn3diro4bxxrgmk 
       foreign key (course) 
       references Course (id);

    alter table CourseSignupStudentGroup 
       add constraint FKgf40eud5lkhn8121smruv1woc 
       foreign key (studentGroup) 
       references StudentGroup (id);

    alter table CourseSignupStudyProgramme 
       add constraint FKar6ibjp1fnm5s423ctio16g7g 
       foreign key (course) 
       references Course (id);

    alter table CourseSignupStudyProgramme 
       add constraint FKqnnl7ik69gedm8mk4dody7ojt 
       foreign key (studyProgramme) 
       references StudyProgramme (id);

    alter table CourseStaffMember 
       add constraint FKbnsxixgwkate9vnevfc0fj0mf 
       foreign key (role_id) 
       references CourseStaffMemberRole (id);

    alter table CourseStaffMember 
       add constraint FKpxienvmiobgqwjsxhacvl3t43 
       foreign key (staffMember_id) 
       references StaffMember (id);

    alter table CourseStaffMember 
       add constraint FK9qktsgr1j42houihhw1m89v6s 
       foreign key (id) 
       references CourseUser (id);

    alter table CourseStudent 
       add constraint FK9wl6eq4qu0ebonsnuw6v7uhs2 
       foreign key (billingDetails) 
       references BillingDetails (id);

    alter table CourseStudent 
       add constraint FKlgnhkv7lq94q203u96kow96l2 
       foreign key (enrolmentType) 
       references CourseEnrolmentType (id);

    alter table CourseStudent 
       add constraint FK9wu4pb4d2oh6le4y5f9x042v4 
       foreign key (participationType) 
       references CourseParticipationType (id);

    alter table CourseStudent 
       add constraint FK8neu2q44y9w89gustp1b6d5c5 
       foreign key (room_id) 
       references Room (id);

    alter table CourseStudent 
       add constraint FKj8jj8gipvnb275y3nvsp1b5kv 
       foreign key (student) 
       references Student (id);

    alter table CourseStudent 
       add constraint FKoh86r0opt2l2q7uc0o8xl5wmq 
       foreign key (id) 
       references CourseUser (id);

    alter table CourseStudentVariable 
       add constraint FKjbmbd70jvxvsbvqcf3wrb8wv4 
       foreign key (courseStudent) 
       references CourseStudent (id);

    alter table CourseStudentVariable 
       add constraint FKsnfruvfba5h7jarek42hai6o9 
       foreign key (variableKey) 
       references CourseStudentVariableKey (id);

    alter table CourseUser 
       add constraint FK1owas0rhcmrykaumwhosxglj5 
       foreign key (course) 
       references Course (id);

    alter table Credit 
       add constraint FKcsdi9t3mc67oyibh60uem1v85 
       foreign key (assessor_id) 
       references StaffMember (id);

    alter table Credit 
       add constraint FK5h4bkm6yjeg1wnu9l9ejai8f3 
       foreign key (grade) 
       references Grade (id);

    alter table CreditLink 
       add constraint FKnlgmwp52nob1kmyajv6b4yyk9 
       foreign key (creator) 
       references User (id);

    alter table CreditLink 
       add constraint FK3lhj2fwd8w7lot14rb0xqaj97 
       foreign key (credit) 
       references Credit (id);

    alter table CreditLink 
       add constraint FK6iytcx8ipb3t7bpqqgp1eli9t 
       foreign key (student) 
       references Student (id);

    alter table CreditVariable 
       add constraint FKfbyu5dkr6irt6gqf4mcwqrp3 
       foreign key (credit) 
       references Credit (id);

    alter table CreditVariable 
       add constraint FKh6b4s9nxhxu89pa1ow8wvlui7 
       foreign key (variableKey) 
       references CreditVariableKey (id);

    alter table Defaults 
       add constraint FK7f9ahyahtxeprkja42dnbs5qf 
       foreign key (educationalTimeUnit) 
       references EducationalTimeUnit (id);

    alter table Defaults 
       add constraint FK9v6f7nxpo49k40fc62jf1guob 
       foreign key (courseEnrolmentType) 
       references CourseEnrolmentType (id);

    alter table Defaults 
       add constraint FK4uir0e9n0wln8lnxh40nm9ypk 
       foreign key (courseParticipationType) 
       references CourseParticipationType (id);

    alter table Defaults 
       add constraint FKslgufgfmc66hh8q7kg4r63p4b 
       foreign key (courseState) 
       references CourseState (id);

    alter table Defaults 
       add constraint FK4lxvrit66p8rteuqyi18bqtoc 
       foreign key (organization) 
       references Organization (id);

    alter table Defaults 
       add constraint FKo2u8jfh1xnae7vab3xwoamrnd 
       foreign key (studentDefaultContactType) 
       references ContactType (id);

    alter table Defaults 
       add constraint FKqfam2uqi4wjws4m9blauq2q54 
       foreign key (userDefaultContactType) 
       references ContactType (id);

    alter table EducationalLength 
       add constraint FK8u78b5awgm5wg3lgag6qefb7v 
       foreign key (unit) 
       references EducationalTimeUnit (id);

    alter table EducationSubtype 
       add constraint FK1owrc9yeqfy1jem9fmq1ynoe1 
       foreign key (educationType) 
       references EducationType (id);

    alter table Email 
       add constraint FKmaosn4bks1dbo7313edy9p30w 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table Email 
       add constraint FKq3rpsc2mpa8fonqt1nnva9n7a 
       foreign key (contactType) 
       references ContactType (id);

    alter table EmailSignature 
       add constraint FK9u4andvts020w5sgyefbnw375 
       foreign key (user) 
       references User (id);

    alter table EnvironmentRolePermission 
       add constraint FKiru4bc3jnx6mk52mk8ogpe8q 
       foreign key (id) 
       references RolePermission (id);

    alter table File 
       add constraint FKqw8lqcnorglw0l89go1vrqhs 
       foreign key (creator) 
       references User (id);

    alter table File 
       add constraint FKob7xa41cq8i8fkh690ir5bxtf 
       foreign key (fileType) 
       references FileType (id);

    alter table File 
       add constraint FKpq6tigl927lpy701979kc5iib 
       foreign key (lastModifier) 
       references User (id);

    alter table FormDraft 
       add constraint FK4vq7ja54uw3b2hmoq4qc6ymux 
       foreign key (creator) 
       references User (id);

    alter table Grade 
       add constraint FK8dg3iw67b26ae1eekr7ki2wrk 
       foreign key (gradingScale) 
       references GradingScale (id);

    alter table GradeCourseResource 
       add constraint FKdndlsu7t82icf1guv74jpg08 
       foreign key (course) 
       references Course (id);

    alter table GradeCourseResource 
       add constraint FK83g6olw36che61d8giaiesqig 
       foreign key (resource) 
       references Resource (id);

    alter table HelpFolder 
       add constraint FKoo3pjfeirwma9a6kt1aa0r0rs 
       foreign key (id) 
       references HelpItem (id);

    alter table HelpItem 
       add constraint FK52xqajvf8g3ychax8crdvuyoh 
       foreign key (creator) 
       references User (id);

    alter table HelpItem 
       add constraint FK6bk5j1ip7798lsiudq5hww3x5 
       foreign key (lastModifier) 
       references User (id);

    alter table HelpItem 
       add constraint FKhorfs4mt54o4gmgpcuwr6rwc1 
       foreign key (parent) 
       references HelpFolder (id);

    alter table HelpItemTitle 
       add constraint FKm9jowusbdum85to2hhii0v00n 
       foreign key (creator) 
       references User (id);

    alter table HelpItemTitle 
       add constraint FKdh98i9g9fmd8gfr6r3qpvj2ne 
       foreign key (item) 
       references HelpItem (id);

    alter table HelpItemTitle 
       add constraint FK46sgm347wa1n144pn51mymwb7 
       foreign key (lastModifier) 
       references User (id);

    alter table HelpPage 
       add constraint FKqxga5uav2m5o6g50umc7dhcjh 
       foreign key (id) 
       references HelpItem (id);

    alter table HelpPageContent 
       add constraint FK2mc875786r0wbn8aihou0gkkd 
       foreign key (creator) 
       references User (id);

    alter table HelpPageContent 
       add constraint FK1y943kclmq0ax5ukbm7hfifx9 
       foreign key (lastModifier) 
       references User (id);

    alter table HelpPageContent 
       add constraint FK5w358eh2yjj5qjh0umtnkesxn 
       foreign key (page) 
       references HelpPage (id);

    alter table KoskiPersonLog 
       add constraint FKmdm2gsv5s7305yhetyfphovsa 
       foreign key (person) 
       references Person (id);

    alter table KoskiPersonLog 
       add constraint FKkd2g0nn8pfy22m5kfvcjmmlyh 
       foreign key (student) 
       references Student (id);

    alter table LoginLog 
       add constraint FKt8771wo8pvrmr0wo7xjd6pmg4 
       foreign key (user) 
       references User (id);

    alter table MaterialResource 
       add constraint FKbooe0ton0qdsxe4so83w6nk61 
       foreign key (id) 
       references Resource (id);

    alter table MatriculationExam 
       add constraint FKsvv2o3706wmr3xm936yacvlb9 
       foreign key (signupGrade_id) 
       references Grade (id);

    alter table MatriculationExamAttendance 
       add constraint FKo2r1ips57nb3bgl52d52apmu7 
       foreign key (enrollment_id) 
       references MatriculationExamEnrollment (id);

    alter table MatriculationExamAttendance 
       add constraint FKani6hdqbrl508k6e9dggv7cs5 
       foreign key (projectAssessment_id) 
       references ProjectAssessment (id);

    alter table MatriculationExamEnrollment 
       add constraint FK42x7g8a30rpjmjcir4yt0up1r 
       foreign key (exam_id) 
       references MatriculationExam (id);

    alter table MatriculationExamEnrollment 
       add constraint FK3oit4ow46jkd4ojhm8m89i6nt 
       foreign key (student_id) 
       references Student (id);

    alter table MatriculationExamSubjectSettings 
       add constraint FK1pq8kcj53uww6wdyv68ybf85s 
       foreign key (exam_id) 
       references MatriculationExam (id);

    alter table MatriculationExamSubjectSettings 
       add constraint FKornlanpb0c6n1weyt279b72p8 
       foreign key (project_id) 
       references Project (id);

    alter table Module 
       add constraint FKte4a4jf75opjtljf7cf6d9tbg 
       foreign key (id) 
       references CourseBase (id);

    alter table ModuleComponent 
       add constraint FKagjq8pjh65xb20djj5pw56jlt 
       foreign key (module) 
       references Module (id);

    alter table ModuleComponent 
       add constraint FK4mg5cw0o545whbvbnm5m92oq5 
       foreign key (id) 
       references ComponentBase (id);

    alter table Organization 
       add constraint FKrjnl20rt1kistycsqqfqsdk4s 
       foreign key (billingDetails) 
       references BillingDetails (id);

    alter table Organization 
       add constraint FK3e66u99djs2hwvnyuyqar3bw4 
       foreign key (educationType) 
       references EducationType (id);

    alter table OrganizationContactPerson 
       add constraint FKa5r5ok06avp63ut96qk2ugjya 
       foreign key (organization) 
       references Organization (id);

    alter table OrganizationContractPeriod 
       add constraint FK7ygyjdddcah88wr4txg6n8sbg 
       foreign key (organization) 
       references Organization (id);

    alter table OtherCost 
       add constraint FKmjg5ufkotw0l9da52ab1u7tfe 
       foreign key (course) 
       references Course (id);

    alter table PasswordResetRequest 
       add constraint FKpbry36c20ofiw12jrcd2roe91 
       foreign key (person_id) 
       references Person (id);

    alter table Person 
       add constraint FKfl5bycex7s9poh1m7hydiudsb 
       foreign key (defaultUser_id) 
       references User (id);

    alter table PersonVariable 
       add constraint FKq5shr64eleqvle7m9kd4bp4t1 
       foreign key (variableKey) 
       references PersonVariableKey (id);

    alter table PersonVariable 
       add constraint FKj2fjk0oypbi3bx9oi16x5bxcu 
       foreign key (person) 
       references Person (id);

    alter table PhoneNumber 
       add constraint FK5hhyj63seelk9cq0tle29y9tt 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table PhoneNumber 
       add constraint FK4w9wfr2pdv0xxj2qccqhsxkei 
       foreign key (contactType) 
       references ContactType (id);

    alter table Project 
       add constraint FKdf68aiu75i0wwtthi9mg9e24l 
       foreign key (creator) 
       references User (id);

    alter table Project 
       add constraint FK199s6413n2nbqysnfj2lufvhc 
       foreign key (lastModifier) 
       references User (id);

    alter table Project 
       add constraint FKr8xji6qmqvmrlj9it6fmpppgr 
       foreign key (optionalStudies) 
       references EducationalLength (id);

    alter table ProjectAssessment 
       add constraint FKmrwcto4gdb1jdvic83nq90s08 
       foreign key (studentProject) 
       references StudentProject (id);

    alter table ProjectAssessment 
       add constraint FKnp3yp531l64gr90sd6u8ecesg 
       foreign key (id) 
       references Credit (id);

    alter table ProjectModule 
       add constraint FKo99bgw3hhmp694mh6y76ns3cn 
       foreign key (module) 
       references Module (id);

    alter table ProjectModule 
       add constraint FKgvaw6pgu5053s837k7cynlh8i 
       foreign key (project) 
       references Project (id);

    alter table Report 
       add constraint FK5n8rp8h1bnhbxfavy2itxm96i 
       foreign key (category) 
       references ReportCategory (id);

    alter table Report 
       add constraint FK8bxbf0sy1fsrnx2veyvs5i3en 
       foreign key (creator) 
       references User (id);

    alter table Report 
       add constraint FK1hefjp2vw82bfx2knob1s7uwc 
       foreign key (lastModifier) 
       references User (id);

    alter table ReportContext 
       add constraint FK44l06knvc35ttrdeh8268hpgm 
       foreign key (report) 
       references Report (id);

    alter table Resource 
       add constraint FK7ypa9142vs54nh8dqfvc2809j 
       foreign key (category) 
       references ResourceCategory (id);

    alter table RolePermission 
       add constraint FK3hcess71f1xxxljetox836oa3 
       foreign key (permission_id) 
       references Permission (id);

    alter table Room 
       add constraint FKn5tljppajs813n1cb1naxkj7d 
       foreign key (type_id) 
       references RoomType (id);

    alter table School 
       add constraint FK1dege0n0auw40veljc4ujhpgw 
       foreign key (billingDetails) 
       references BillingDetails (id);

    alter table School 
       add constraint FKkx6ewsrs7iausnsnj27hlkrot 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table School 
       add constraint FK3611rswp7lgvtem1nyhwkp767 
       foreign key (field) 
       references SchoolField (id);

    alter table SchoolVariable 
       add constraint FKra7iru4bncegvpt6x16qxeet2 
       foreign key (variableKey) 
       references SchoolVariableKey (id);

    alter table SchoolVariable 
       add constraint FK1ejhpomqr9ralbkk2p3ttrh3m 
       foreign key (school) 
       references School (id);

    alter table Setting 
       add constraint FKmxdgmxhvan93skssihvh3yid1 
       foreign key (settingKey) 
       references SettingKey (id);

    alter table StaffMember 
       add constraint FK2rilq2fct1j64or28pvtxs2pt 
       foreign key (organization) 
       references Organization (id);

    alter table StaffMember 
       add constraint FKslfsr6q5g7hyl6ikx70jg45l2 
       foreign key (id) 
       references User (id);

    alter table StaffMemberProperties 
       add constraint FKmpc7tap99sctobv17lswsm2lx 
       foreign key (staffMember_id) 
       references StaffMember (id);

    alter table Student 
       add constraint FK9odpkwh07wwqkdpyu1a5uxppu 
       foreign key (activityType) 
       references StudentActivityType (id);

    alter table Student 
       add constraint FKg147x5cf9el5h11fx8yjvyd0g 
       foreign key (curriculum_id) 
       references Curriculum (id);

    alter table Student 
       add constraint FKpgb2c390mo0le76adek9yanj1 
       foreign key (educationalLevel) 
       references StudentEducationalLevel (id);

    alter table Student 
       add constraint FKb60d6fmbt9j2u0oidn9otr6xb 
       foreign key (examinationType) 
       references StudentExaminationType (id);

    alter table Student 
       add constraint FK7y26ea8ib9i02qv10pt4yeu3e 
       foreign key (language) 
       references Language (id);

    alter table Student 
       add constraint FKdtdbbnt9j6o28gg539kxfd2lb 
       foreign key (municipality) 
       references Municipality (id);

    alter table Student 
       add constraint FK7y8ou7iljq07fd8h7soitm7ug 
       foreign key (nationality) 
       references Nationality (id);

    alter table Student 
       add constraint FK2w2el40k7ek9xefouk9sqac51 
       foreign key (school) 
       references School (id);

    alter table Student 
       add constraint FK48yuusihb1wlke6ydmc3rmyqy 
       foreign key (studyApprover) 
       references StaffMember (id);

    alter table Student 
       add constraint FK99dh113727vlxjix2kerbccsq 
       foreign key (studyEndReason) 
       references StudentStudyEndReason (id);

    alter table Student 
       add constraint FK4ppcchdbb8j84eckxfappx1i8 
       foreign key (studyProgramme) 
       references StudyProgramme (id);

    alter table Student 
       add constraint FKkfqq4nickg8wu56axc7jktv1 
       foreign key (id) 
       references User (id);

    alter table StudentContactLogEntry 
       add constraint FK6sl7koe2eub7r7ayjp3qcog5k 
       foreign key (student) 
       references Student (id);

    alter table StudentContactLogEntryComment 
       add constraint FK3f3gjfv92t75apb78kwe2ba08 
       foreign key (entry) 
       references StudentContactLogEntry (id);

    alter table StudentCourseResource 
       add constraint FKq7xn9wjy0w3u04eydcrh5seyq 
       foreign key (course) 
       references Course (id);

    alter table StudentCourseResource 
       add constraint FKb8rjf8abjju70bo5goh1hovtl 
       foreign key (resource) 
       references Resource (id);

    alter table StudentFile 
       add constraint FKb89ilu3k3tqcnb06ujjnq6smk 
       foreign key (student) 
       references Student (id);

    alter table StudentFile 
       add constraint FKneljfuq7q22u7b69yqod906cf 
       foreign key (id) 
       references File (id);

    alter table StudentGroup 
       add constraint FK4mw7p1yvlibr388pvg9o8r6ax 
       foreign key (creator) 
       references User (id);

    alter table StudentGroup 
       add constraint FKn7jygyi8f3f7x64n93wwgn0bv 
       foreign key (lastModifier) 
       references User (id);

    alter table StudentGroup 
       add constraint FKeei7sat9kolo6w5y747sfbv2w 
       foreign key (organization) 
       references Organization (id);

    alter table StudentGroupContactLogEntry 
       add constraint FK2rnv1av8lniwrqexjsvgdi82u 
       foreign key (studentGroup) 
       references StudentGroup (id);

    alter table StudentGroupContactLogEntryComment 
       add constraint FKk4s7pektw4qtmb8itobr9v4r3 
       foreign key (entry) 
       references StudentGroupContactLogEntry (id);

    alter table StudentGroupStudent 
       add constraint FKcn9ye3mi8srad64far7fd1i4p 
       foreign key (student) 
       references Student (id);

    alter table StudentGroupStudent 
       add constraint FKta85qamvkdrv6f4d6yiayqg9 
       foreign key (studentGroup) 
       references StudentGroup (id);

    alter table StudentGroupUser 
       add constraint FK9jbwqruni7rxkjh7d2cbxhvwd 
       foreign key (staffMember_id) 
       references StaffMember (id);

    alter table StudentGroupUser 
       add constraint FKq15qd6u4lefj5mrm1ckf9tb4o 
       foreign key (studentGroup) 
       references StudentGroup (id);

    alter table StudentImage 
       add constraint FKgx44mg9hh6rl9ch9rephotyic 
       foreign key (student) 
       references Student (id);

    alter table StudentLodgingPeriod 
       add constraint FKp5lb4yxvw29ha6abeook1bx32 
       foreign key (student) 
       references Student (id);

    alter table StudentProject 
       add constraint FK21uu4fnyptwvy8e1lo1bjhn72 
       foreign key (creator) 
       references User (id);

    alter table StudentProject 
       add constraint FKpotb1qkwf90j9fjkhgfr9d0s9 
       foreign key (lastModifier) 
       references User (id);

    alter table StudentProject 
       add constraint FK4a9wdgl6su1fobv03sr8l9d9p 
       foreign key (optionalStudies) 
       references EducationalLength (id);

    alter table StudentProject 
       add constraint FKh84d985peeqdc5oogvqmqiwao 
       foreign key (project) 
       references Project (id);

    alter table StudentProject 
       add constraint FKh2iatc4s20h8p7xg5052peyia 
       foreign key (student) 
       references Student (id);

    alter table StudentProjectModule 
       add constraint FK38unjxhldhye8r67fasn9hflm 
       foreign key (academicTerm) 
       references AcademicTerm (id);

    alter table StudentProjectModule 
       add constraint FKp5g1s9l1icrbfiye223wf1w3b 
       foreign key (module) 
       references Module (id);

    alter table StudentProjectModule 
       add constraint FK4pewglcj9f0yyiclodk8a77ao 
       foreign key (studentProject) 
       references StudentProject (id);

    alter table StudentStudyEndReason 
       add constraint FK8duxuxnxjwknnu7rqqm5odgu7 
       foreign key (parentReason) 
       references StudentStudyEndReason (id);

    alter table StudentStudyEndReasonProperties 
       add constraint FKhearyd9lyawwlob2866wcrbc8 
       foreign key (studentStudyEndReason_id) 
       references StudentStudyEndReason (id);

    alter table StudentStudyPeriod 
       add constraint FKbf4xdl2ckiur2ace515uexm3o 
       foreign key (student) 
       references Student (id);

    alter table StudentSubjectGrade 
       add constraint FKaimsm1iwlcc82epfp3fjw8cg 
       foreign key (grade) 
       references Grade (id);

    alter table StudentSubjectGrade 
       add constraint FKhj9tk5bhyamvombymiktqiuq8 
       foreign key (gradeApprover) 
       references StaffMember (id);

    alter table StudentSubjectGrade 
       add constraint FKqomnuvo1y0nvbhesxr7gboyfg 
       foreign key (issuer) 
       references StaffMember (id);

    alter table StudentSubjectGrade 
       add constraint FK15t5764698cvve67dbs9c7a9w 
       foreign key (student) 
       references Student (id);

    alter table StudentSubjectGrade 
       add constraint FK4kgv2jestup53o0ksc4yp1mhw 
       foreign key (subject) 
       references Subject (id);

    alter table StudyProgramme 
       add constraint FKgimkhx2ln9rt44dbru5lm8k2g 
       foreign key (category) 
       references StudyProgrammeCategory (id);

    alter table StudyProgramme 
       add constraint FKrnj28bvgb4gnh1g0t82ob5sis 
       foreign key (organization) 
       references Organization (id);

    alter table StudyProgrammeCategory 
       add constraint FKhyd2rfiqtm7l330bbjneptpls 
       foreign key (educationType) 
       references EducationType (id);

    alter table Subject 
       add constraint FK7qfufcn682y4h7brvvemkr16g 
       foreign key (educationType) 
       references EducationType (id);

    alter table TransferCredit 
       add constraint FKk75dl48moc6dabbvkkwm0b45u 
       foreign key (courseLength) 
       references EducationalLength (id);

    alter table TransferCredit 
       add constraint FKoquhmueo7btm82ccsb9k5cu4f 
       foreign key (curriculum_id) 
       references Curriculum (id);

    alter table TransferCredit 
       add constraint FK3fr38r7b8o352efvmp1p8xst6 
       foreign key (school) 
       references School (id);

    alter table TransferCredit 
       add constraint FKmhutslgr1j8cy4fo9ug0f059n 
       foreign key (student) 
       references Student (id);

    alter table TransferCredit 
       add constraint FK5y572hmo7277g77hldb6hip1n 
       foreign key (subject) 
       references Subject (id);

    alter table TransferCredit 
       add constraint FKgp4cd4ii46yl28ed15fhia417 
       foreign key (id) 
       references Credit (id);

    alter table TransferCreditTemplate 
       add constraint FKibhhwb00gojintd8vs4hw517p 
       foreign key (curriculum_id) 
       references Curriculum (id);

    alter table TransferCreditTemplateCourse 
       add constraint FKtmkg3v3qwtxy2slovoxitv127 
       foreign key (courseLength) 
       references EducationalLength (id);

    alter table TransferCreditTemplateCourse 
       add constraint FKjdscrjbh2f6mij8re2gdxwxxv 
       foreign key (curriculum_id) 
       references Curriculum (id);

    alter table TransferCreditTemplateCourse 
       add constraint FKouok7whq0748xri77rr6aymm0 
       foreign key (subject) 
       references Subject (id);

    alter table TransferCreditTemplateCourse 
       add constraint FKpa5q31ytwp76kpcd6gpv77nfn 
       foreign key (transferCreditTemplate) 
       references TransferCreditTemplate (id);

    alter table User 
       add constraint FKhcvd5kh2egntqmb6holpaajwv 
       foreign key (contactInfo) 
       references ContactInfo (id);

    alter table User 
       add constraint FKtjydbpktvifo7uc55n1fm2wet 
       foreign key (person_id) 
       references Person (id);

    alter table UserIdentification 
       add constraint FKk53tqk7xu4v8wsjwr46pxd88d 
       foreign key (person_id) 
       references Person (id);

    alter table UserVariable 
       add constraint FKd2l7yx3j2uben1aggvh2o0pn6 
       foreign key (variableKey) 
       references UserVariableKey (id);

    alter table UserVariable 
       add constraint FKnrtypu1if20nnqa3pbbuunwdb 
       foreign key (user) 
       references User (id);

    alter table WorklistItem 
       add constraint FKfwq7riu6jkui2ncrn5ubcummr 
       foreign key (courseAssessment_id) 
       references CourseAssessment (id);

    alter table WorklistItem 
       add constraint FKgvx1nce7kwxebrbfa0rqfxx8u 
       foreign key (creator_id) 
       references User (id);

    alter table WorklistItem 
       add constraint FKgl8x5thg1v9a7xppy2paf4xjt 
       foreign key (modifier_id) 
       references User (id);

    alter table WorklistItem 
       add constraint FK1n4cc4y5mnh7v05uwpfmtu1wt 
       foreign key (owner_id) 
       references User (id);

    alter table WorklistItem 
       add constraint FKg5k3g9tve372fqo2k249ar8a4 
       foreign key (template_id) 
       references WorklistItemTemplate (id);

    alter table WorkResource 
       add constraint FKrr2s3aj6rw577670ygl88gpbp 
       foreign key (id) 
       references Resource (id);
       
    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ClientApplicationAuthorizationCode',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('School',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseBase',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Plugin',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ComponentBase',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('BasicCourseResource',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('GradeCourseResource',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudyProgrammeCategory',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentContactLogEntryComment',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ContactURLType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CreditVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ProjectModule',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentGroupContactLogEntryComment',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Tag',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Webhook',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseEducationSubtype',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseDescriptionCategory',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Setting',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('EducationalLength',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ReportContext',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseEducationType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('UserVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ApplicationSignatures',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('AcademicTerm',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('MatriculationExamAttendance',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('GradingScale',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Application',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Report',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('PersonVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('EducationType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('TransferCreditTemplateCourse',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ContactURL',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ApplicationMailTemplate',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseState',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseStaffMemberRole',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ApplicationAttachment',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ContactType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentActivityType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('File',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('UserVariable',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CreditLink',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Credit',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentCourseResource',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('HelpPageContent',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Language',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Curriculum',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('RolePermission',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('HelpItem',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentProject',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentGroupContactLogEntry',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentExaminationType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('EducationalTimeUnit',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ChangeLogEntryProperty',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Person',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseBaseVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseStudentVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('RoomType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Room',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseAssessmentRequest',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentEducationalLevel',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseEnrolmentType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudyProgramme',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('FileType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Subject',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentContactLogEntry',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('EmailSignature',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseUser',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ClientApplication',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentStudyEndReason',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('TransferCreditTemplate',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ChangeLogEntryEntityProperty',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('OtherCost',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('FormDraft',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CreditVariable',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('SettingKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Municipality',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Grade',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('SchoolField',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ClientApplicationAccessToken',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('PluginRepository',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseBaseVariable',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ResourceCategory',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ChangeLogEntryEntity',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Permission',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('InternalAuth',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentGroupUser',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ApplicationLog',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentProjectModule',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('PersonVariable',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseParticipationType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('MagicKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('BillingDetails',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Address',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentGroupStudent',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Resource',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ApplicationNotification',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ContactInfo',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('AccessLogEntryPath',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseDescription',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('PasswordResetRequest',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ReportCategory',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('AccessLogEntry',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('EducationSubtype',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentImage',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('HelpItemTitle',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('User',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Email',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('LoginLog',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('MatriculationExamEnrollment',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('TrackedEntityProperty',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('SchoolVariableKey',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Project',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseStudentVariable',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('UserIdentification',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('MatriculationExam',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseType',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('CourseComponentResource',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('StudentGroup',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('Nationality',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('ChangeLogEntry',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('PhoneNumber',0);

    insert into hibernate_sequences(sequence_name, sequence_next_hi_value) values ('SchoolVariable',0);
    