drop table if exists StaffMember_AUD;
drop table if exists Student_AUD;
drop table if exists User_AUD;
drop table if exists PhoneNumber_AUD;
drop table if exists Person_AUD;
drop table if exists Email_AUD;
drop table if exists ContactInfo_PhoneNumber_AUD;
drop table if exists ContactInfo_Email_AUD;
drop table if exists ContactInfo_Address_AUD;
drop table if exists ContactInfo_AUD;
drop table if exists Address_AUD;

drop table if exists REVINFO;

create table Address_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, city varchar(255), country varchar(255), defaultAddress bit, name varchar(255), postalCode varchar(255), streetAddress varchar(255), primary key (id, REV));
create table ContactInfo_Address_AUD (REV integer not null, contactInfo bigint not null, id bigint not null, indexColumn integer not null, REVTYPE tinyint, primary key (REV, contactInfo, id, indexColumn));
create table ContactInfo_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, primary key (id, REV));
create table ContactInfo_Email_AUD (REV integer not null, contactInfo bigint not null, id bigint not null, indexColumn integer not null, REVTYPE tinyint, primary key (REV, contactInfo, id, indexColumn));
create table ContactInfo_PhoneNumber_AUD (REV integer not null, contactInfo bigint not null, id bigint not null, indexColumn integer not null, REVTYPE tinyint, primary key (REV, contactInfo, id, indexColumn));
create table Email_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, address varchar(255), defaultAddress bit, primary key (id, REV));
create table Person_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, socialSecurityNumber varchar(255), primary key (id, REV));
create table PhoneNumber_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, defaultNumber bit, number varchar(255), primary key (id, REV));
create table User_AUD (id bigint not null, REV integer not null, REVTYPE tinyint, archived bit, firstName varchar(255), lastName varchar(255), primary key (id, REV));
create table Student_AUD (id bigint not null, REV integer not null, nickname varchar(255), primary key (id, REV));
create table StaffMember_AUD (id bigint not null, REV integer not null, role varchar(255), primary key (id, REV));

create table REVINFO (id integer not null auto_increment, timestamp bigint not null, userId bigint, primary key (id));

alter table Address_AUD add constraint FKgwp5sek4pjb4awy66sp184hrv foreign key (REV) references REVINFO (id);
alter table ContactInfo_Address_AUD add constraint FK8nknf0wk0o51unaon8tjq0lj foreign key (REV) references REVINFO (id);
alter table ContactInfo_AUD add constraint FK7nb5ms3cc7s1ccd4248lrvq90 foreign key (REV) references REVINFO (id);
alter table ContactInfo_Email_AUD add constraint FKflls1x6o54ce7r58w6yqmlpj9 foreign key (REV) references REVINFO (id);
alter table ContactInfo_PhoneNumber_AUD add constraint FKgudo8lppewsl6vkhll6oyvpmr foreign key (REV) references REVINFO (id);
alter table Email_AUD add constraint FKclwj2oxb81muqkqko2p74iwm1 foreign key (REV) references REVINFO (id);
alter table Person_AUD add constraint FKqbm2y5o4elhanxeq26reu73yd foreign key (REV) references REVINFO (id);
alter table PhoneNumber_AUD add constraint FKqurt4rnn752x81igo7xl215oj foreign key (REV) references REVINFO (id);
alter table User_AUD add constraint FKilft2rdosb65jocpcoan7xnjq foreign key (REV) references REVINFO (id);
alter table Student_AUD add constraint FKiw83robuibsovckww0vd3419v foreign key (id, REV) references User_AUD (id, REV);
alter table StaffMember_AUD add constraint FKtg9st9uwbih6ltcaidfvs6p6v foreign key (id, REV) references User_AUD (id, REV);

