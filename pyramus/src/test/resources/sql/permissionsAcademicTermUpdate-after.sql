delete from AcademicTerm where id in (3);
delete from hibernate_sequences where sequence_name = 'AcademicTerm' AND sequence_next_hi_value in (select max(id) from AcademicTerm);