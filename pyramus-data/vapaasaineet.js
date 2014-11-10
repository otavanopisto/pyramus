({
  'run' : function(api){
    api.log('Importing subjects...');
    
    if (!api.educationTypes.findIdByCode('vapaasivistys')) {
      api.educationTypes.create('vapaasivistys', 'Vapaa sivistys');
    }
    
    var data = [
      {'name' : 'Biologia ja maantiede',  'educationType' : 'vapaasivistys'},
      {'name' : 'Fysiikka ja kemia sekä geo-, avaruus- ja tähtitieteet',  'educationType' : 'vapaasivistys'},
      {'name' : 'Graafinen ja viestintätekniikka',  'educationType' : 'vapaasivistys'},
      {'name' : 'Historia ja arkeologia',  'educationType' : 'vapaasivistys'},
      {'name' : 'Hygieniapassi',  'educationType' : 'vapaasivistys'},
      {'name' : 'Kansalais- ja järjestötoiminta',  'educationType' : 'vapaasivistys'},
      {'name' : 'Kirjallisuus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Kuvataide',  'educationType' : 'vapaasivistys'},
      {'name' : 'Käsi- ja taideteollisuus ja käden taidot',  'educationType' : 'vapaasivistys'},
      {'name' : 'Liiketalous ja kauppa',  'educationType' : 'vapaasivistys'},
      {'name' : 'Liikunta ja urheilu',  'educationType' : 'vapaasivistys'},
      {'name' : 'Luonto- ja ympäristöala',  'educationType' : 'vapaasivistys'},
      {'name' : 'Musiikki',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muu humanistisen ja kasvatusalan koulutus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muu kulttuurialan koulutus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muu tietotekniikan hyväksikäyttö',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muu yhteiskunnallisten aineiden, liiketalouden ja hallinnon alan koulutus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muu yleissivistävä koulutus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Muut kielet',  'educationType' : 'vapaasivistys'},
      {'name' : 'Opetus- ja kasvatustyö ja psykologia',  'educationType' : 'vapaasivistys'},
      {'name' : 'Oppimisvalmiuksien kehittäminen ja motivointi',  'educationType' : 'vapaasivistys'},
      {'name' : 'Sosiaaliala',  'educationType' : 'vapaasivistys'},
      {'name' : 'Sosiaalitieteet',  'educationType' : 'vapaasivistys'},
      {'name' : 'Teatteri ja tanssi',  'educationType' : 'vapaasivistys'},
      {'name' : 'Terveysala',  'educationType' : 'vapaasivistys'},
      {'name' : 'Tieto- ja tietoliikennetekniikka',  'educationType' : 'vapaasivistys'},
      {'name' : 'Tietojenkäsittely ja tietotekniikan hyväksikäyttö',  'educationType' : 'vapaasivistys'},
      {'name' : 'Tietokoneen ajokorttikoulutus',  'educationType' : 'vapaasivistys'},
      {'name' : 'Vapaa-aika- ja nuorisotyö',  'educationType' : 'vapaasivistys'},
      {'name' : 'Venäjä',  'educationType' : 'vapaasivistys'},
      {'name' : 'Viestintä- ja informaatioala', 'educationType' : 'vapaasivistys'}
   ];
    
    for (var i = 0; i < data.length;i++) {
      var subject = data[i];
      api.subjects.create('', subject.name, subject.educationType);
    } 
  }
})