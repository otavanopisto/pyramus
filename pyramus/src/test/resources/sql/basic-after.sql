DELETE FROM Defaults;

DELETE FROM MagicKey;

DELETE FROM PluginRepository;

delete from __UserTags where user in (3, 4);   
delete from StudentContactLogEntry where student in (3);
delete from StudentGroupStudent where student in (3, 4);
delete from CourseStudent where student in (3, 4);
delete from CourseUser where id in (1,2);
delete from Student where id in (3, 4);
delete from User where id in (3, 4);
delete from AbstractStudent where id in (1,2);
delete from ContactURL where id in (3, 4);
delete from PhoneNumber where id in (3, 4);
delete from Email where id in (3, 4);
delete from Address where id in (3, 4);
delete from ContactInfo where id in (3, 4);
  
DELETE FROM hibernate_sequences where sequence_name = 'Defaults';

DELETE FROM hibernate_sequences where sequence_name = 'MagicKey';

DELETE FROM hibernate_sequences where sequence_name = 'PluginRepository';