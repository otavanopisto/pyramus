drop table if exists Organization;

create table Organization (id bigint not null auto_increment, archived bit not null, name varchar(255) not null, primary key (id));
