    alter table Address 
        drop constraint FK1ED033D4C0329886;

    alter table Address 
        drop constraint FK1ED033D4C028436E;

    alter table BasicCourseResource 
        drop constraint FK87983CF75475C87B;

    alter table BasicCourseResource 
        drop constraint FK87983CF7A7C0C394;

    alter table ChangeLogEntry 
        drop constraint FK29D7737E2D667C2D;

    alter table ChangeLogEntry 
        drop constraint FK29D7737E62D248B;

    alter table ChangeLogEntryEntityProperty 
        drop constraint FKE9F8DDF62D667C2D;

    alter table ChangeLogEntryProperty 
        drop constraint FKE30B57733DEECE14;

    alter table ChangeLogEntryProperty 
        drop constraint FKE30B57735D7A51F9;

    alter table ComponentBase 
        drop constraint FK954D014E6270F1B1;

    alter table ContactURL 
        drop constraint FK7F280D4F58FFEF20;

    alter table ContactURL 
        drop constraint FK7F280D4FC028436E;

    alter table Course 
        drop constraint FK78A7CC3BB33F86B5;

    alter table Course 
        drop constraint FK78A7CC3B5D81ECA2;

    alter table Course 
        drop constraint FK78A7CC3BD7113A6E;

    alter table CourseAssessment 
        drop constraint FK9833115D6EEA9BD;

    alter table CourseAssessment 
        drop constraint FK9833115D55F637B;

    alter table CourseAssessmentRequest 
        drop constraint FKADBCA67255F637B;

    alter table CourseBase 
        drop constraint FKB40B1AC977F310A;

    alter table CourseBase 
        drop constraint FKB40B1AC76DFD82D;

    alter table CourseBase 
        drop constraint FKB40B1AC4344B8EC;

    alter table CourseBase 
        drop constraint FKB40B1AC2054118C;

    alter table CourseBaseVariable 
        drop constraint FK73597C4847BCDB06;

    alter table CourseBaseVariable 
        drop constraint FK73597C48322966AC;

    alter table CourseComponent 
        drop constraint FK4A4C03025F7117BB;

    alter table CourseComponent 
        drop constraint FK4A4C03025475C87B;

    alter table CourseComponentResource 
        drop constraint FK23A809306F64EBBF;

    alter table CourseComponentResource 
        drop constraint FK23A80930A7C0C394;

    alter table CourseDescription 
        drop constraint FKBB50FCC147BCDB06;

    alter table CourseDescription 
        drop constraint FKBB50FCC18177E998;

    alter table CourseEducationSubtype 
        drop constraint FK102A2B0DC9842B12;

    alter table CourseEducationSubtype 
        drop constraint FK102A2B0DED033D00;

    alter table CourseEducationType 
        drop constraint FK5F27B287A5E07316;

    alter table CourseEducationType 
        drop constraint FK5F27B28747BCDB06;

    alter table CourseStudent 
        drop constraint FK21572580EE2B836F;

    alter table CourseStudent 
        drop constraint FK215725805475C87B;

    alter table CourseStudent 
        drop constraint FK2157258023DFBF96;

    alter table CourseStudent 
        drop constraint FK21572580D2B417DC;

    alter table CourseStudent 
        drop constraint FK2157258075F2C3BC;

    alter table CourseStudentVariable 
        drop constraint FK99F8DC1C55F637B;

    alter table CourseStudentVariable 
        drop constraint FK99F8DC1CEDFABEB;

    alter table CourseUser 
        drop constraint FKB4996A65475C87B;

    alter table CourseUser 
        drop constraint FKB4996A657C009DE;

    alter table CourseUser 
        drop constraint FKB4996A6D5977E22;

    alter table Credit 
        drop constraint FK78CA9719D709AF89;

    alter table Credit 
        drop constraint FK78CA971979EF34E5;

    alter table CreditLink 
        drop constraint FK552B86B3B654479B;

    alter table CreditLink 
        drop constraint FK552B86B3EE2B836F;

    alter table CreditLink 
        drop constraint FK552B86B34344B8EC;

    alter table CreditVariable 
        drop constraint FKA051A4B5B654479B;

    alter table CreditVariable 
        drop constraint FKA051A4B592438EA4;

    alter table Defaults 
        drop constraint FK2A415672531DE567;

    alter table Defaults 
        drop constraint FK2A41567221BB8DB;

    alter table Defaults 
        drop constraint FK2A41567261817BA1;

    alter table Defaults 
        drop constraint FK2A415672572F2D3A;

    alter table EducationSubtype 
        drop constraint FKD17434D2A5E07316;

    alter table EducationalLength 
        drop constraint FK8E7A679278A621A;

    alter table Email 
        drop constraint FK3FF5B7CC0329886;

    alter table Email 
        drop constraint FK3FF5B7CC028436E;

    alter table File 
        drop constraint FK21699C76DFD82D;

    alter table File 
        drop constraint FK21699C4344B8EC;

    alter table File 
        drop constraint FK21699CDCDEF2CF;

    alter table FormDraft 
        drop constraint FKE1D2F71D4344B8EC;

    alter table Grade 
        drop constraint FK41DCFB7146934B5;

    alter table GradeCourseResource 
        drop constraint FK39880CE05475C87B;

    alter table GradeCourseResource 
        drop constraint FK39880CE0A7C0C394;

    alter table HelpFolder 
        drop constraint FKA9227F2F42CE482D;

    alter table HelpItem 
        drop constraint FKD4C29A1476DFD82D;

    alter table HelpItem 
        drop constraint FKD4C29A144344B8EC;

    alter table HelpItem 
        drop constraint FKD4C29A148774E157;

    alter table HelpItemTitle 
        drop constraint FKC5BFC4A442FFB625;

    alter table HelpItemTitle 
        drop constraint FKC5BFC4A476DFD82D;

    alter table HelpItemTitle 
        drop constraint FKC5BFC4A44344B8EC;

    alter table HelpPage 
        drop constraint FKD4C5819042CE482D;

    alter table HelpPageContent 
        drop constraint FKB5D068C94305851D;

    alter table HelpPageContent 
        drop constraint FKB5D068C976DFD82D;

    alter table HelpPageContent 
        drop constraint FKB5D068C94344B8EC;

    alter table MaterialResource 
        drop constraint FKE255BA75BC150CA1;

    alter table Module 
        drop constraint FK89B0928CB33F86B5;

    alter table ModuleComponent 
        drop constraint FKFAFEC7D15F7117BB;

    alter table ModuleComponent 
        drop constraint FKFAFEC7D1D7113A6E;

    alter table OtherCost 
        drop constraint FK36EBADD5475C87B;

    alter table PhoneNumber 
        drop constraint FK1C4E6237C0329886;

    alter table PhoneNumber 
        drop constraint FK1C4E6237C028436E;

    alter table Project 
        drop constraint FK50C8E2F9BE6D6F92;

    alter table Project 
        drop constraint FK50C8E2F976DFD82D;

    alter table Project 
        drop constraint FK50C8E2F94344B8EC;

    alter table ProjectAssessment 
        drop constraint FKA42D529B6EEA9BD;

    alter table ProjectAssessment 
        drop constraint FKA42D529B22B70CA1;

    alter table ProjectModule 
        drop constraint FKD8B796C536EE660D;

    alter table ProjectModule 
        drop constraint FKD8B796C5D7113A6E;

    alter table Report 
        drop constraint FK91B1415476DFD82D;

    alter table Report 
        drop constraint FK91B141544344B8EC;

    alter table Report 
        drop constraint FK91B1415426DDD5AE;

    alter table ReportContext 
        drop constraint FK336625BB47FBFEC6;

    alter table Resource 
        drop constraint FKEF86282E3857F7A2;

    alter table School 
        drop constraint FK93464794C028436E;

    alter table School 
        drop constraint FK93464794ECFDC92;

    alter table SchoolVariable 
        drop constraint FK58FEAA301BB391F6;

    alter table SchoolVariable 
        drop constraint FK58FEAA3092C708C4;

    alter table Setting 
        drop constraint FKD997A6301C9B8DAE;

    alter table Student 
        drop constraint FKF3371A1BEF70A856;

    alter table Student 
        drop constraint FKF3371A1B30ED0CC0;

    alter table Student 
        drop constraint FKF3371A1BFDB61D8B;

    alter table Student 
        drop constraint FKF3371A1BC655592A;

    alter table Student 
        drop constraint FKF3371A1BC028436E;

    alter table Student 
        drop constraint FKF3371A1B9F140BD4;

    alter table Student 
        drop constraint FKF3371A1B29206186;

    alter table Student 
        drop constraint FKF3371A1B8ECB763E;

    alter table Student 
        drop constraint FKF3371A1B44566106;

    alter table Student 
        drop constraint FKF3371A1B1BB391F6;

    alter table Student 
        drop constraint FKF3371A1B7CB521A2;

    alter table StudentContactLogEntry 
        drop constraint FKEBB80C33EE2B836F;

    alter table StudentContactLogEntryComment 
        drop constraint FKD11CEAAC4458F86C;

    alter table StudentCourseResource 
        drop constraint FKCF4D58445475C87B;

    alter table StudentCourseResource 
        drop constraint FKCF4D5844A7C0C394;

    alter table StudentFile 
        drop constraint FKEFB7FE373EE7CD5A;

    alter table StudentFile 
        drop constraint FKEFB7FE37EE2B836F;

    alter table StudentGroup 
        drop constraint FK75A052476DFD82D;

    alter table StudentGroup 
        drop constraint FK75A05244344B8EC;

    alter table StudentGroupStudent 
        drop constraint FKD427C477EE2B836F;

    alter table StudentGroupStudent 
        drop constraint FKD427C47786F746CF;

    alter table StudentGroupUser 
        drop constraint FK12F3330F86F746CF;

    alter table StudentGroupUser 
        drop constraint FK12F3330F62D248B;

    alter table StudentImage 
        drop constraint FK773B800EE2B836F;

    alter table StudentProject 
        drop constraint FK7500447EBE6D6F92;

    alter table StudentProject 
        drop constraint FK7500447EEE2B836F;

    alter table StudentProject 
        drop constraint FK7500447E36EE660D;

    alter table StudentProject 
        drop constraint FK7500447E76DFD82D;

    alter table StudentProject 
        drop constraint FK7500447E4344B8EC;

    alter table StudentProjectModule 
        drop constraint FK6FBF008A22B70CA1;

    alter table StudentProjectModule 
        drop constraint FK6FBF008A15319894;

    alter table StudentProjectModule 
        drop constraint FK6FBF008AD7113A6E;

    alter table StudentStudyEndReason 
        drop constraint FK9136D6316227D0F8;

    alter table StudentVariable 
        drop constraint FK1C1405B7EE2B836F;

    alter table StudentVariable 
        drop constraint FK1C1405B7B02EFAD2;

    alter table StudyProgramme 
        drop constraint FKB037EB733296F97D;

    alter table StudyProgrammeCategory 
        drop constraint FKFCB7E991A5E07316;

    alter table Subject 
        drop constraint FKF3E2ED0CA5E07316;

    alter table TransferCredit 
        drop constraint FKB1D3EE4977F310A;

    alter table TransferCredit 
        drop constraint FKB1D3EE46EEA9BD;

    alter table TransferCredit 
        drop constraint FKB1D3EE4EE2B836F;

    alter table TransferCredit 
        drop constraint FKB1D3EE42054118C;

    alter table TransferCredit 
        drop constraint FKB1D3EE41BB391F6;

    alter table TransferCreditTemplateCourse 
        drop constraint FK471C0139977F310A;

    alter table TransferCreditTemplateCourse 
        drop constraint FK471C013931068E25;

    alter table TransferCreditTemplateCourse 
        drop constraint FK471C01392054118C;

    alter table User 
        drop constraint FK285FEBC028436E;

    alter table UserVariable 
        drop constraint FK918A7B87E073D3C6;

    alter table UserVariable 
        drop constraint FK918A7B8762D248B;

    alter table WorkResource 
        drop constraint FKDD5DA25FBC150CA1;

    alter table __CourseTags 
        drop constraint FK271392D45475C87B;

    alter table __CourseTags 
        drop constraint FK271392D4F8D12EE6;

    alter table __HelpItemTags 
        drop constraint FK7314542DF8D12EE6;

    alter table __HelpItemTags 
        drop constraint FK7314542D13C4FCE6;

    alter table __ModuleTags 
        drop constraint FK5EBEC8A5F8D12EE6;

    alter table __ModuleTags 
        drop constraint FK5EBEC8A5D7113A6E;

    alter table __ProjectTags 
        drop constraint FK88F02ED236EE660D;

    alter table __ProjectTags 
        drop constraint FK88F02ED2F8D12EE6;

    alter table __ResourceTags 
        drop constraint FK46CF4547F8D12EE6;

    alter table __ResourceTags 
        drop constraint FK46CF4547A7C0C394;

    alter table __SchoolTags 
        drop constraint FK8CE2B9ADF8D12EE6;

    alter table __SchoolTags 
        drop constraint FK8CE2B9AD1BB391F6;

    alter table __StudentBillingDetails 
        drop constraint FK4F787262EE2B836F;

    alter table __StudentBillingDetails 
        drop constraint FK4F78726275F2C3BC;

    alter table __StudentGroupTags 
        drop constraint FKF41EC73DF8D12EE6;

    alter table __StudentGroupTags 
        drop constraint FKF41EC73D86F746CF;

    alter table __StudentProjectTags 
        drop constraint FK19E519722B70CA1;

    alter table __StudentProjectTags 
        drop constraint FK19E5197F8D12EE6;

    alter table __StudentTags 
        drop constraint FK4D4A04F4EE2B836F;

    alter table __StudentTags 
        drop constraint FK4D4A04F4F8D12EE6;

    alter table __UserBillingDetails 
        drop constraint FK5BD3FFF275F2C3BC;

    alter table __UserBillingDetails 
        drop constraint FK5BD3FFF262D248B;

    alter table __UserTags 
        drop constraint FKA09E4684F8D12EE6;

    alter table __UserTags 
        drop constraint FKA09E468462D248B;

    drop table AbstractStudent if exists;

    drop table AcademicTerm if exists;

    drop table Address if exists;

    drop table BasicCourseResource if exists;

    drop table BillingDetails if exists;

    drop table ChangeLogEntry if exists;

    drop table ChangeLogEntryEntity if exists;

    drop table ChangeLogEntryEntityProperty if exists;

    drop table ChangeLogEntryProperty if exists;

    drop table ComponentBase if exists;

    drop table ContactInfo if exists;

    drop table ContactType if exists;

    drop table ContactURL if exists;

    drop table ContactURLType if exists;

    drop table Course if exists;

    drop table CourseAssessment if exists;

    drop table CourseAssessmentRequest if exists;

    drop table CourseBase if exists;

    drop table CourseBaseVariable if exists;

    drop table CourseBaseVariableKey if exists;

    drop table CourseComponent if exists;

    drop table CourseComponentResource if exists;

    drop table CourseDescription if exists;

    drop table CourseDescriptionCategory if exists;

    drop table CourseEducationSubtype if exists;

    drop table CourseEducationType if exists;

    drop table CourseEnrolmentType if exists;

    drop table CourseParticipationType if exists;

    drop table CourseState if exists;

    drop table CourseStudent if exists;

    drop table CourseStudentVariable if exists;

    drop table CourseStudentVariableKey if exists;

    drop table CourseUser if exists;

    drop table CourseUserRole if exists;

    drop table Credit if exists;

    drop table CreditLink if exists;

    drop table CreditVariable if exists;

    drop table CreditVariableKey if exists;

    drop table Defaults if exists;

    drop table EducationSubtype if exists;

    drop table EducationType if exists;

    drop table EducationalLength if exists;

    drop table EducationalTimeUnit if exists;

    drop table Email if exists;

    drop table File if exists;

    drop table FileType if exists;

    drop table FormDraft if exists;

    drop table Grade if exists;

    drop table GradeCourseResource if exists;

    drop table GradingScale if exists;

    drop table HelpFolder if exists;

    drop table HelpItem if exists;

    drop table HelpItemTitle if exists;

    drop table HelpPage if exists;

    drop table HelpPageContent if exists;

    drop table InternalAuth if exists;

    drop table Language if exists;

    drop table MagicKey if exists;

    drop table MaterialResource if exists;

    drop table Module if exists;

    drop table ModuleComponent if exists;

    drop table Municipality if exists;

    drop table Nationality if exists;

    drop table OtherCost if exists;

    drop table PhoneNumber if exists;

    drop table Plugin if exists;

    drop table PluginRepository if exists;

    drop table Project if exists;

    drop table ProjectAssessment if exists;

    drop table ProjectModule if exists;

    drop table Report if exists;

    drop table ReportCategory if exists;

    drop table ReportContext if exists;

    drop table Resource if exists;

    drop table ResourceCategory if exists;

    drop table School if exists;

    drop table SchoolField if exists;

    drop table SchoolVariable if exists;

    drop table SchoolVariableKey if exists;

    drop table Setting if exists;

    drop table SettingKey if exists;

    drop table Student if exists;

    drop table StudentActivityType if exists;

    drop table StudentContactLogEntry if exists;

    drop table StudentContactLogEntryComment if exists;

    drop table StudentCourseResource if exists;

    drop table StudentEducationalLevel if exists;

    drop table StudentExaminationType if exists;

    drop table StudentFile if exists;

    drop table StudentGroup if exists;

    drop table StudentGroupStudent if exists;

    drop table StudentGroupUser if exists;

    drop table StudentImage if exists;

    drop table StudentProject if exists;

    drop table StudentProjectModule if exists;

    drop table StudentStudyEndReason if exists;

    drop table StudentVariable if exists;

    drop table StudentVariableKey if exists;

    drop table StudyProgramme if exists;

    drop table StudyProgrammeCategory if exists;

    drop table Subject if exists;

    drop table Tag if exists;

    drop table TrackedEntityProperty if exists;

    drop table TransferCredit if exists;

    drop table TransferCreditTemplate if exists;

    drop table TransferCreditTemplateCourse if exists;

    drop table User if exists;

    drop table UserVariable if exists;

    drop table UserVariableKey if exists;

    drop table WorkResource if exists;

    drop table __CourseTags if exists;

    drop table __HelpItemTags if exists;

    drop table __ModuleTags if exists;

    drop table __ProjectTags if exists;

    drop table __ResourceTags if exists;

    drop table __SchoolTags if exists;

    drop table __StudentBillingDetails if exists;

    drop table __StudentGroupTags if exists;

    drop table __StudentProjectTags if exists;

    drop table __StudentTags if exists;

    drop table __UserBillingDetails if exists;

    drop table __UserTags if exists;

    drop table hibernate_sequences if exists;