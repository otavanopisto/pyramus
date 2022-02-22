<?xml version="1.0" encoding="UTF-8"?>
<update xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://www.ofw.fi/xml/2011/java-xmldb-updater/UpdaterSchema.xsd">

  <sql>drop table if exists AuditLog;</sql>
  <sql>create table AuditLog (id bigint not null auto_increment, authorId bigint, className varchar(255) not null, data varchar(255), date datetime not null, entityId bigint, field varchar(255), personId bigint, type varchar(255) not null, userId bigint, primary key (id));</sql>

</update>