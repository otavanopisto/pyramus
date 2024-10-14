INSERT INTO 
  Defaults (courseState, id, educationalTimeUnit, version, courseParticipationType, courseEnrolmentType) 
VALUES 
  (2, 1, 1, 3, 1, null);

INSERT INTO 
  MagicKey (created, name, id, version, scope) 
VALUES ('2014-09-08 12:33:50', '0793c5ee-3283-4628-b3b9-746da4e96d5f', 4, 0, 'APPLICATION');

insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'MagicKey', max(id) + 1 from MagicKey;
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'Defaults', max(id) + 1 from Defaults;