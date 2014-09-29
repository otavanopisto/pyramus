    alter table AccessLogEntry 
        drop constraint FK_r031kiolhx2sg7hpdngdn026l if exists;

    alter table AccessLogEntry 
        drop constraint FK_pqnvv7lafgjgne4uxm2vqvqiu if exists;

    alter table Address 
        drop constraint FK_p90qwaeto30hrsskrw3vddgup if exists;

    alter table Address 
        drop constraint FK_o4ui1nisxpnjicn3ift35lawt if exists;

    alter table BasicCourseResource 
        drop constraint FK_6vyqcfts1vlur0nv0d05e4td2 if exists;

    alter table BasicCourseResource 
        drop constraint FK_gk3t0um3tiddm2goieocsqe3 if exists;

    alter table ChangeLogEntry 
        drop constraint FK_trl3pu52yuhr6jlus9epa7ild if exists;

    alter table ChangeLogEntry 
        drop constraint FK_kdh12wtrq0966fpwyjylcbwh1 if exists;

    alter table ChangeLogEntryEntityProperty 
        drop constraint FK_cf3qvc4k2fuqw17mo37kijm28 if exists;

    alter table ChangeLogEntryProperty 
        drop constraint FK_bd33yidoqd2jxs8n99p6tkpge if exists;

    alter table ChangeLogEntryProperty 
        drop constraint FK_qgkacalwos2pvununsghx60j if exists;

    alter table ClientApplicationAccessToken 
        drop constraint FK_j3nhydmgnfg8210e2gruxrh4w if exists;

    alter table ClientApplicationAccessToken 
        drop constraint FK_rwdu61j1a1rn11pqv0ii1qdgu if exists;

    alter table ClientApplicationAuthorizationCode 
        drop constraint FK_jkil4wujeq1r634wwut2vcs62 if exists;

    alter table ClientApplicationAuthorizationCode 
        drop constraint FK_ftbh7jxr3fram1cold5cuetdb if exists;

    alter table ComponentBase 
        drop constraint FK_n3636lg4sxh0jvounesk7wec8 if exists;

    alter table ContactURL 
        drop constraint FK_2s7xt7s92jscrxgn1x6lxp3t4 if exists;

    alter table ContactURL 
        drop constraint FK_b6cur2odxtvui687f979t3xoh if exists;

    alter table Course 
        drop constraint FK_hvlfik6l3xul7nju5i6mvr5pt if exists;

    alter table Course 
        drop constraint FK_4gv69tpplv1tn5l3caog1joy4 if exists;

    alter table Course 
        drop constraint FK_1djmp3hn9iwd9ad4m58qtv3mw if exists;

    alter table CourseAssessment 
        drop constraint FK_h2uf2bqevk8mcg4c209iimiii if exists;

    alter table CourseAssessment 
        drop constraint FK_fh8tfbpjnxx2vv360mjycnkl4 if exists;

    alter table CourseAssessmentRequest 
        drop constraint FK_8q7qimj1fjafgipdlghx21ihe if exists;

    alter table CourseBase 
        drop constraint FK_8n3d8qk4nbhisror3fa56mysa if exists;

    alter table CourseBase 
        drop constraint FK_qdbp18pq48wyynl5ckj9t3bg2 if exists;

    alter table CourseBase 
        drop constraint FK_5mf6i7k6y5kag8g3aovixs38y if exists;

    alter table CourseBase 
        drop constraint FK_f7792dwjjlr8yt0r7mgkpirrd if exists;

    alter table CourseBaseVariable 
        drop constraint FK_20wssfclcmxxxobjutc6o4mfw if exists;

    alter table CourseBaseVariable 
        drop constraint FK_g1q49gtou9k3jso650k5r37yc if exists;

    alter table CourseComponent 
        drop constraint FK_lt90xbqly93wnxsjdqolp83vn if exists;

    alter table CourseComponent 
        drop constraint FK_87f9ovvi3hy4roxtebo6hy5al if exists;

    alter table CourseComponentResource 
        drop constraint FK_iirdnay0h9fc63st5atv9i1ur if exists;

    alter table CourseComponentResource 
        drop constraint FK_jyfucsejwcj8hbxbl3v8b2rno if exists;

    alter table CourseDescription 
        drop constraint FK_d3gaxsqdh05g53olhgjl99eij if exists;

    alter table CourseDescription 
        drop constraint FK_77le1nirxo5yighwd96fyjxqf if exists;

    alter table CourseEducationSubtype 
        drop constraint FK_kt290qaxkhotm9jo7wb6hv0aj if exists;

    alter table CourseEducationSubtype 
        drop constraint FK_409elphhj1xbcuqo57tmypn96 if exists;

    alter table CourseEducationType 
        drop constraint FK_hq2hbvq3f81dmyymqa2j07bcb if exists;

    alter table CourseEducationType 
        drop constraint FK_70hkwva11c9btj1r8xdflt6y4 if exists;

    alter table CourseStudent 
        drop constraint FK_990n0w0581m3h4n0pvxyo09gs if exists;

    alter table CourseStudent 
        drop constraint FK_tdsd2ykmt3gl8al74vhvetp5p if exists;

    alter table CourseStudent 
        drop constraint FK_joq4rus28ls505so74el2sne2 if exists;

    alter table CourseStudent 
        drop constraint FK_6b304h022160fuidrq2n1l7g9 if exists;

    alter table CourseStudent 
        drop constraint FK_6syti63mb3rf5ohusl6hhxm1l if exists;

    alter table CourseStudentVariable 
        drop constraint FK_fdew52if7kx4x8t433jtwjbw8 if exists;

    alter table CourseStudentVariable 
        drop constraint FK_g1rwycwb6g3lm68lrpg0k3rbs if exists;

    alter table CourseUser 
        drop constraint FK_8m1wdnkvu7f4w38jxpb0pf7n6 if exists;

    alter table CourseUser 
        drop constraint FK_d71yo025tyvtm7sm6c8b33c7k if exists;

    alter table CourseUser 
        drop constraint FK_gesju89o5caq9d5ics49nkmda if exists;

    alter table Credit 
        drop constraint FK_qeujhkfa3cpfwvb48646agdch if exists;

    alter table Credit 
        drop constraint FK_fpl517x4owb3qt2nhit9oy8e7 if exists;

    alter table CreditLink 
        drop constraint FK_6idi337xbuq6w5wk75w8kwad9 if exists;

    alter table CreditLink 
        drop constraint FK_gmdwo9i5q09mbxs4idwgqfvxt if exists;

    alter table CreditLink 
        drop constraint FK_ju2x5va5e7votkdlghnmdc2gq if exists;

    alter table CreditVariable 
        drop constraint FK_iqmfjcic2boxjciqgamihkppj if exists;

    alter table CreditVariable 
        drop constraint FK_inaneefcy0mnigltdmt4fobdr if exists;

    alter table Defaults 
        drop constraint FK_dnkpit0q6wlkfl0b66akw84pt if exists;

    alter table Defaults 
        drop constraint FK_g86nby8edvpbde88tdeyb851 if exists;

    alter table Defaults 
        drop constraint FK_23wxeog8g1fyee0982ubptj3f if exists;

    alter table Defaults 
        drop constraint FK_s9uv5jk1xxljge95mksp4tfx0 if exists;

    alter table EducationSubtype 
        drop constraint FK_ljkp7gmuy3wgdl2nkxs316ow1 if exists;

    alter table EducationalLength 
        drop constraint FK_9i3s1om3re3kf4sciv2ay719f if exists;

    alter table Email 
        drop constraint FK_9beti0nhdq60m9llr30r7xrbk if exists;

    alter table Email 
        drop constraint FK_98itl0gnxl42v33d8wtim00ny if exists;

    alter table File 
        drop constraint FK_p39k3e3t8kff359g2rkhjjovt if exists;

    alter table File 
        drop constraint FK_6nyh3x9yrffgiau2lbkbxbl3k if exists;

    alter table File 
        drop constraint FK_i4xo8fnijunrxpmrslpsggjld if exists;

    alter table FormDraft 
        drop constraint FK_ubrbpijc1j5magqtp2davuyw if exists;

    alter table Grade 
        drop constraint FK_amgg5818eisglvsplo3vt68yj if exists;

    alter table GradeCourseResource 
        drop constraint FK_pr4wqos0fajw1wuqboieu8fmm if exists;

    alter table GradeCourseResource 
        drop constraint FK_3vhbap3vceorw22x4mh994n8g if exists;

    alter table HelpFolder 
        drop constraint FK_nuaikofqxx36tbnqva5w30dkr if exists;

    alter table HelpItem 
        drop constraint FK_4rajwxh9isb1hj1i0r51i77jk if exists;

    alter table HelpItem 
        drop constraint FK_gxcqfqdc3twretfmsbo5xg4we if exists;

    alter table HelpItem 
        drop constraint FK_kqa3kt0f5n4c6d50docm62tho if exists;

    alter table HelpItemTitle 
        drop constraint FK_a6mow7m5ul7cer67yf3i4xc8c if exists;

    alter table HelpItemTitle 
        drop constraint FK_2xxynq0ppm8ou0o3dcwkhxawq if exists;

    alter table HelpItemTitle 
        drop constraint FK_i1a0pr6j0cbeghcnv5qdlf6nc if exists;

    alter table HelpPage 
        drop constraint FK_9awkdrimomfve3wo73o7x9lrq if exists;

    alter table HelpPageContent 
        drop constraint FK_7aq0keo8uon28osm3biuxrxhd if exists;

    alter table HelpPageContent 
        drop constraint FK_qg5cfmrofk6kve7ffgpgm2nn if exists;

    alter table HelpPageContent 
        drop constraint FK_skw5bpma7gi8pm1i2yih5hsch if exists;

    alter table MaterialResource 
        drop constraint FK_pi7v6ltxwjfung4vhoricynqx if exists;

    alter table Module 
        drop constraint FK_8qdxvhwj9bps8ktupfem3m53j if exists;

    alter table ModuleComponent 
        drop constraint FK_tmd3hat9f93c34cxhvagf7b0f if exists;

    alter table ModuleComponent 
        drop constraint FK_r6r3bnkfpar0lqq1diqetqbac if exists;

    alter table OtherCost 
        drop constraint FK_6f2p8ujs7cleagmwfy226acqe if exists;

    alter table PhoneNumber 
        drop constraint FK_awul8v1e7o1lwrxpofjfyh4a0 if exists;

    alter table PhoneNumber 
        drop constraint FK_cxyslapwd99ifu2b4nv79pxtf if exists;

    alter table Project 
        drop constraint FK_1aik7d4w4m941okf0qa6i9uw8 if exists;

    alter table Project 
        drop constraint FK_kmrhsrdppvcr99du224dr82qp if exists;

    alter table Project 
        drop constraint FK_gifnakb4oeswbkdfj2j73yxym if exists;

    alter table ProjectAssessment 
        drop constraint FK_4o00eth6ajsmkruwvjx2qwwnl if exists;

    alter table ProjectAssessment 
        drop constraint FK_2edqobatxcdbfqmy418e9m781 if exists;

    alter table ProjectModule 
        drop constraint FK_lrt13xbhvibxnp1i60handpmr if exists;

    alter table ProjectModule 
        drop constraint FK_fklaynsnhmwilbwdpul0tykfu if exists;

    alter table Report 
        drop constraint FK_37x7inqemxi63kf9knphcemt7 if exists;

    alter table Report 
        drop constraint FK_nd95h1smw61bqoq7y1rknjvhr if exists;

    alter table Report 
        drop constraint FK_8hw8ykuibh000vu6qtllv67ps if exists;

    alter table ReportContext 
        drop constraint FK_crg8up29hxw2njcbgureswxch if exists;

    alter table Resource 
        drop constraint FK_t58ubqwynff69fm5tpco4ye47 if exists;

    alter table School 
        drop constraint FK_5wwaivsnowvhspfqdmqjrtrxj if exists;

    alter table School 
        drop constraint FK_jy0pdno5q9b79t2yg3ng3sn4q if exists;

    alter table SchoolVariable 
        drop constraint FK_g478tf5sv2iie0uf25ahblw89 if exists;

    alter table SchoolVariable 
        drop constraint FK_niktl5fwc9i5hmk0rwvqu8dny if exists;

    alter table Setting 
        drop constraint FK_i4lfreu085sgpg9qjjhesr2vi if exists;

    alter table Student 
        drop constraint FK_jkly5h0o7bfv241wftfpj5mh6 if exists;

    alter table Student 
        drop constraint FK_moxdtidtiop0frxnxal9unpt if exists;

    alter table Student 
        drop constraint FK_h8ypmoc31ra5j40x74esdy6x2 if exists;

    alter table Student 
        drop constraint FK_3yckb2qkc56r4by902wwci0n8 if exists;

    alter table Student 
        drop constraint FK_d97pguoxqr7ame3qnip13ekp1 if exists;

    alter table Student 
        drop constraint FK_ahq5fxxqkhcolav50hq9nh0q if exists;

    alter table Student 
        drop constraint FK_jdov17i07kqm1v2dyvey648xq if exists;

    alter table Student 
        drop constraint FK_bj6b1k4s9p44hqsrobb36tr86 if exists;

    alter table Student 
        drop constraint FK_6civiuvxkr0mt0xdewlk1p209 if exists;

    alter table Student 
        drop constraint FK_jos3vl724h8ln4toi52mn5b6f if exists;

    alter table Student 
        drop constraint FK_ohs43dct8k52ch2exlmf4bs3l if exists;

    alter table StudentContactLogEntry 
        drop constraint FK_gnx1l8oymwaqxbl4hlownd4s2 if exists;

    alter table StudentContactLogEntryComment 
        drop constraint FK_5cbk5okc1c4j3rbhgu8u5u0vm if exists;

    alter table StudentCourseResource 
        drop constraint FK_1wfh045c1ajeqiyuedcs1w8jo if exists;

    alter table StudentCourseResource 
        drop constraint FK_8swnhosc1ckobku19m5vvqohr if exists;

    alter table StudentFile 
        drop constraint FK_rf3myt039nl5b85a5vftd0ry8 if exists;

    alter table StudentFile 
        drop constraint FK_4p3ib0d8up3g526m3etu4x5jh if exists;

    alter table StudentGroup 
        drop constraint FK_4d8ydne169e6ueqyj0jtwlpmm if exists;

    alter table StudentGroup 
        drop constraint FK_7idqmgs5v42cv13kosncuo0fe if exists;

    alter table StudentGroupStudent 
        drop constraint FK_5l4cr2cte8wyiug3x0mkmpr6t if exists;

    alter table StudentGroupStudent 
        drop constraint FK_9l0e8ewb1nfi1vxsr4anqnjbj if exists;

    alter table StudentGroupUser 
        drop constraint FK_7qmfe5ac665syeij5k3x3vb7o if exists;

    alter table StudentGroupUser 
        drop constraint FK_a0t3yelwll2bbm4m123bs7int if exists;

    alter table StudentImage 
        drop constraint FK_a4fvsbpg1wd2fq74f8xqcwb6s if exists;

    alter table StudentProject 
        drop constraint FK_3piais5t058ytt7hjlvm2yixh if exists;

    alter table StudentProject 
        drop constraint FK_87nxxdtnf8dyn2x1e3h86b5rh if exists;

    alter table StudentProject 
        drop constraint FK_ke85ke770aj7jcbmjcoku5la2 if exists;

    alter table StudentProject 
        drop constraint FK_m0xgkc8vsti9bpb8j7hpg62kt if exists;

    alter table StudentProject 
        drop constraint FK_cac2lntddk7blosrxr9xofjsj if exists;

    alter table StudentProjectModule 
        drop constraint FK_dd1wmb18pl0s2d07y0oyoynbq if exists;

    alter table StudentProjectModule 
        drop constraint FK_aqcxytl2yuj8xnjrvlm6xu3i4 if exists;

    alter table StudentProjectModule 
        drop constraint FK_p4slb9gh4okk9uttkw09jwn7u if exists;

    alter table StudentStudyEndReason 
        drop constraint FK_2f8xkyc4p4muk488tcnc6gpqr if exists;

    alter table StudyProgramme 
        drop constraint FK_9ro57ucpum3wqatybrefu71it if exists;

    alter table StudyProgrammeCategory 
        drop constraint FK_lkbg43l77aimbt60q5tk1kdfm if exists;

    alter table Subject 
        drop constraint FK_2jvjpbje8i7rifw2cq5uhsplo if exists;

    alter table TransferCredit 
        drop constraint FK_9n6pwvfm0okxfwsiyf6fpx1p8 if exists;

    alter table TransferCredit 
        drop constraint FK_rk9k9w0b5oqlxohfnn5pgm146 if exists;

    alter table TransferCredit 
        drop constraint FK_hyn393ws6p0x42tnd9kw9d0ar if exists;

    alter table TransferCredit 
        drop constraint FK_5hw59qfrdyj1pg8xl6ipjed87 if exists;

    alter table TransferCredit 
        drop constraint FK_3kvav953yf312os4409cgmpqi if exists;

    alter table TransferCreditTemplateCourse 
        drop constraint FK_4g5726qoakm5036sjyj0xm58 if exists;

    alter table TransferCreditTemplateCourse 
        drop constraint FK_sqrxc1ecwi5ukroe3aesq4shw if exists;

    alter table TransferCreditTemplateCourse 
        drop constraint FK_ifciewbdasfs7ko6falijugdi if exists;

    alter table User 
        drop constraint FK_riou196egia4li7tw37luek46 if exists;

    alter table UserVariable 
        drop constraint FK_qaiiydgi6aj0x9exdsxf0st2d if exists;

    alter table UserVariable 
        drop constraint FK_spn3atobymho400p1sn47vira if exists;

    alter table WorkResource 
        drop constraint FK_2twof1hru8qnoib84e8h4ubmc if exists;

    alter table __CourseTags 
        drop constraint FK_tioxjjt07nmxu8d3wj7myco0k if exists;

    alter table __CourseTags 
        drop constraint FK_9jy0h9af4687ay9t3syd8n1tm if exists;

    alter table __HelpItemTags 
        drop constraint FK_mo6ixudlv35ltqe396cfdgxb5 if exists;

    alter table __HelpItemTags 
        drop constraint FK_r4j6p1twifvgvbq7fd3er8m1o if exists;

    alter table __ModuleTags 
        drop constraint FK_inga9109o64j42q32s2cainpt if exists;

    alter table __ModuleTags 
        drop constraint FK_3si3vhxd8bam4tqkxmlga9r45 if exists;

    alter table __ProjectTags 
        drop constraint FK_p4oey2c9m9iujqgjc3cskvi86 if exists;

    alter table __ProjectTags 
        drop constraint FK_p4bwrib9jhu1mx7ww2b9hop3o if exists;

    alter table __ResourceTags 
        drop constraint FK_nxqiot7237rkexinkncrvufe1 if exists;

    alter table __ResourceTags 
        drop constraint FK_dft9for4owmie0uc1npd17gs6 if exists;

    alter table __SchoolTags 
        drop constraint FK_7v02ry618vqfi225egm7ag2r8 if exists;

    alter table __SchoolTags 
        drop constraint FK_lbkqqkavgltyrrcjatg5eqh6w if exists;

    alter table __StudentGroupTags 
        drop constraint FK_44ub49nfa8oleq5fg66ur4fc0 if exists;

    alter table __StudentGroupTags 
        drop constraint FK_iobxoo1vl9qll999oui4744xv if exists;

    alter table __StudentProjectTags 
        drop constraint FK_dtx3jka5ns3h5ntixwifloqc2 if exists;

    alter table __StudentProjectTags 
        drop constraint FK_16emgsigygw6th8y8indckbjv if exists;

    alter table __UserBillingDetails 
        drop constraint FK_bj58cvcye8yvguabaacs0fw71 if exists;

    alter table __UserBillingDetails 
        drop constraint FK_51scxf6eoh95igh7qsvi083dp if exists;

    alter table __UserTags 
        drop constraint FK_r2nt0vyhb81p3a677ls3rskn5 if exists;

    alter table __UserTags 
        drop constraint FK_cd49dr1pn4dybqibkwedyfl77 if exists;

    drop table AbstractStudent if exists;

    drop table AcademicTerm if exists;

    drop table AccessLogEntry if exists;

    drop table AccessLogEntryPath if exists;

    drop table Address if exists;

    drop table BasicCourseResource if exists;

    drop table BillingDetails if exists;

    drop table ChangeLogEntry if exists;

    drop table ChangeLogEntryEntity if exists;

    drop table ChangeLogEntryEntityProperty if exists;

    drop table ChangeLogEntryProperty if exists;

    drop table ClientApplication if exists;

    drop table ClientApplicationAccessToken if exists;

    drop table ClientApplicationAuthorizationCode if exists;

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

    drop table __StudentGroupTags if exists;

    drop table __StudentProjectTags if exists;

    drop table __UserBillingDetails if exists;

    drop table __UserTags if exists;

    drop table hibernate_sequences if exists;