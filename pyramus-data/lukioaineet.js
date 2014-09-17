({
	'run' : function(api){
	  api.log('Importing subjects...');
    
    if (!api.educationTypes.findIdByCode('lukio')) {
      api.educationTypes.create('lukio', 'Lukio');
    }
    
		var data = [
      { 'name' : 'Tietotekniikka', 'code' : 'AT', 'educationType' : 'lukio'},
      { 'name' : 'Äidinkieli ja kirjallisuus', 'code' : 'AI', 'educationType' : 'lukio'},
      { 'name' : 'Äidinkieli ja kirjallisuus, suomi toisena kielenä', 'code' : 'S2', 'educationType' : 'lukio'},
			{ 'name' : 'Venäjä', 'code' : 'VEA', 'educationType' : 'lukio'},
			{ 'name' : 'Saksa', 'code' : 'SAA', 'educationType' : 'lukio'},
			{ 'name' : 'Ruotsi', 'code' : 'RUA', 'educationType' : 'lukio'},
			{ 'name' : 'Englanti ', 'code' : 'ENA', 'educationType' : 'lukio'},
			{ 'name' : 'Ruotsi', 'code' : 'RUB', 'educationType' : 'lukio'},
			{ 'name' : 'Ranska', 'code' : 'RAB2', 'educationType' : 'lukio'},
			{ 'name' : 'Espanja', 'code' : 'EAB3', 'educationType' : 'lukio'},
			{ 'name' : 'Italia', 'code' : 'IAB3', 'educationType' : 'lukio'},
			{ 'name' : 'Ranska', 'code' : 'RAB3', 'educationType' : 'lukio'},
			{ 'name' : 'Saksa', 'code' : 'SAB3', 'educationType' : 'lukio'},
			{ 'name' : 'Venäjä', 'code' : 'VEB3', 'educationType' : 'lukio'},
			{ 'name' : 'Matematiikka, pitkä', 'code' : 'MAA', 'educationType' : 'lukio'},
			{ 'name' : 'Matematiikka, lyhyt', 'code' : 'MAB', 'educationType' : 'lukio'},
      { 'name' : 'Mediaopinnot', 'code' : 'ME', 'educationType' : 'lukio'},
			{ 'name' : 'Biologia', 'code' : 'BI', 'educationType' : 'lukio'},
			{ 'name' : 'Maantiede ', 'code' : 'GE', 'educationType' : 'lukio'},
			{ 'name' : 'Fysiikka', 'code' : 'FY', 'educationType' : 'lukio'},
			{ 'name' : 'Kemia', 'code' : 'KE', 'educationType' : 'lukio'},
      { 'name' : 'Uskonto, evankelisluterilainen', 'code' : 'UE', 'educationType' : 'lukio'},
      { 'name' : 'Uskonto, ortodoksinen', 'code' : 'UO', 'educationType' : 'lukio'},
			{ 'name' : 'Elämänkatsomustieto', 'code' : 'ET', 'educationType' : 'lukio'},
			{ 'name' : 'Filosofia ', 'code' : 'FI', 'educationType' : 'lukio'},
			{ 'name' : 'Psykologia', 'code' : 'PS', 'educationType' : 'lukio'},
			{ 'name' : 'Historia', 'code' : 'HI', 'educationType' : 'lukio'},
			{ 'name' : 'Yhteiskuntaoppi ', 'code' : 'YH', 'educationType' : 'lukio'},
			{ 'name' : 'Terveystieto ', 'code' : 'TE', 'educationType' : 'lukio'},
			{ 'name' : 'Liikunta ', 'code' : 'LI', 'educationType' : 'lukio'},
			{ 'name' : 'Musiikki', 'code' : 'MU', 'educationType' : 'lukio'},
			{ 'name' : 'Kuvataide', 'code' : 'KU', 'educationType' : 'lukio'},
			{ 'name' : 'Opinto-ohjaus ', 'code' : 'OP', 'educationType' : 'lukio'}
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
