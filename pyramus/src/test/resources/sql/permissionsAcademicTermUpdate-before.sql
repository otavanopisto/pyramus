insert into 
  AcademicTerm (id, name, startDate, endDate, archived, version)
values 
  (3, 'deletethis', PARSEDATETIME('4 9 2016', 'd M yyyy'), PARSEDATETIME('30 12 2016', 'd M yyyy'), false, 0);
  
insert into hibernate_sequences (sequence_name, sequence_next_hi_value) select 'AcademicTerm', max(id) + 1 from AcademicTerm;