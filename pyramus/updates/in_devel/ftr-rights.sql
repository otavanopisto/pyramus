create table Permission (
	id bigint,
	name varchar(255),
	scope varchar(255),
	primary key (id)
);

create table RoleEntity (
	id bigint,
	name varchar(255),
	primary key (id)
);

create table SystemRoleEntity (
	id bigint,
	roleType varchar(255),
	primary key (id)
);

create table EnvironmentRoleEntity (
	id bigint,
	primary key (id)
);

create table RolePermission (
	id bigint,
	permission_id bigint,
	role_id bigint,
	primary key (id)
);

create table EnvironmentRolePermission (
	id bigint,
	primary key (id)
);

alter table User add column roleEntity_id bigint;

insert into RoleEntity values (1, "Everyone");
insert into SystemRoleEntity values (1, "EVERYONE");