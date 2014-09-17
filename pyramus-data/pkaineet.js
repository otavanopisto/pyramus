({
  'run' : function(api){
    api.log('Importing subjects...');
    
    if (!api.educationTypes.findIdByCode('peruskoulu')) {
      api.educationTypes.create('peruskoulu', 'Peruskoulu');
    }
    
    var data = [
      {'name' : 'Biologia', 'code' : 'bi', 'educationType' : 'peruskoulu'},
      {'name' : 'Elämänkatsomustieto', 'code' : 'et', 'educationType' : 'peruskoulu'},
      {'name' : 'Englanti', 'code' : 'ena', 'educationType' : 'peruskoulu'},
      {'name' : 'Fysiikka', 'code' : 'fy', 'educationType' : 'peruskoulu'},
      {'name' : 'Historia', 'code' : 'hi', 'educationType' : 'peruskoulu'},
      {'name' : 'Kemia', 'code' : 'ke', 'educationType' : 'peruskoulu'},
      {'name' : 'Kuvataide', 'code' : 'ku', 'educationType' : 'peruskoulu'},
      {'name' : 'Liikunta', 'code' : 'li', 'educationType' : 'peruskoulu'},
      {'name' : 'Maantieto', 'code' : 'ge', 'educationType' : 'peruskoulu'},
      {'name' : 'Matematiikka', 'code' : 'ma', 'educationType' : 'peruskoulu'},
      {'name' : 'Musiikki', 'code' : 'mu', 'educationType' : 'peruskoulu'},
      {'name' : 'Opinto-ohjaus', 'code' : 'op', 'educationType' : 'peruskoulu'},
      {'name' : 'Ruotsi', 'code' : 'rub', 'educationType' : 'peruskoulu'},
      {'name' : 'Terveystieto', 'code' : 'te', 'educationType' : 'peruskoulu'},
      {'name' : 'Uskonto', 'code' : 'ue', 'educationType' : 'peruskoulu'},
      {'name' : 'yhteiskuntaoppi', 'code' : 'yh', 'educationType' : 'peruskoulu'},
      {'name' : 'Äidinkieli ja kirjallisuus (s2)', 'code' : 's2', 'educationType' : 'peruskoulu'},
      {'name' : 'Äidinkieli ja kirjallisuus, suomi äidinkielenä', 'code' : 'äi', 'educationType' : 'peruskoulu'}
    ];
    
    for (var i = 0; i < data.length;i++) {
      var subject = data[i];
      if (!api.subjects.findIdByCode(subject.code)) {
        api.subjects.create(subject.code, subject.name, subject.educationType);
      } else {
        api.log("Subject '" + subject.code + "' already exists.");
      }
    } 
  }
})