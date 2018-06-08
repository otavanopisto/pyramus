/* Drops */
drop table if exists Organization;

alter table StudyProgramme drop foreign key FKrnj28bvgb4gnh1g0t82ob5sis;
alter table StudyProgramme drop column organization;

alter table StaffMember drop foreign key FK2rilq2fct1j64or28pvtxs2pt;
alter table StaffMember drop column organization;

/* Creates (turn these into updates later) */

create table Organization (id bigint not null auto_increment, archived bit not null, name varchar(255) not null, primary key (id));

alter table StudyProgramme add column organization bigint;
alter table StudyProgramme add constraint FKrnj28bvgb4gnh1g0t82ob5sis foreign key (organization) references Organization (id);

alter table StaffMember add column organization bigint;
alter table StaffMember add constraint FK2rilq2fct1j64or28pvtxs2pt foreign key (organization) references Organization (id);

/* Dev */

update StudyProgramme set organization = 1;