({
	'run' : function(api){
	
		var data = [
			{ 'name' : 'Äidinkieli ja kirjallisuus ', 'id' : 'A', 'level' : 'lukio'},
			{ 'name' : 'Venäjä', 'id' : 'VEA', 'level' : 'lukio'},
			{ 'name' : 'Saksa', 'id' : 'SAA', 'level' : 'lukio'},
			{ 'name' : 'Ruotsi', 'id' : 'RUA', 'level' : 'lukio'},
			{ 'name' : 'Englanti ', 'id' : 'ENA', 'level' : 'lukio'},
			{ 'name' : 'Ruotsi', 'id' : 'RUB', 'level' : 'lukio'},
			{ 'name' : 'Ranska', 'id' : 'RAB2', 'level' : 'lukio'},
			{ 'name' : 'Espanja', 'id' : 'EAB3', 'level' : 'lukio'},
			{ 'name' : 'Italia', 'id' : 'IAB3', 'level' : 'lukio'},
			{ 'name' : 'Ranska', 'id' : 'RAB3', 'level' : 'lukio'},
			{ 'name' : 'Saksa', 'id' : 'SAB3', 'level' : 'lukio'},
			{ 'name' : 'Venäjä', 'id' : 'VEB3', 'level' : 'lukio'},
			{ 'name' : 'Matematiikka, pitkä', 'id' : 'M', 'level' : 'lukio'},
			{ 'name' : 'matematiikka, lyhyt', 'id' : 'N', 'level' : 'lukio'},
			{ 'name' : 'Biologia', 'id' : 'BI', 'level' : 'lukio'},
			{ 'name' : 'Maantiede ', 'id' : 'GE', 'level' : 'lukio'},
			{ 'name' : 'Fysiikka', 'id' : 'FY', 'level' : 'lukio'},
			{ 'name' : 'Kemia', 'id' : 'KE', 'level' : 'lukio'},
			{ 'name' : 'Uskonto', 'id' : 'UE', 'level' : 'lukio'},
			{ 'name' : 'elämänkatsomustieto', 'id' : 'ET', 'level' : 'lukio'},
			{ 'name' : 'Filosofia ', 'id' : 'FI', 'level' : 'lukio'},
			{ 'name' : 'Psykologia', 'id' : 'PS', 'level' : 'lukio'},
			{ 'name' : 'Historia', 'id' : 'HI', 'level' : 'lukio'},
			{ 'name' : 'Yhteiskuntaoppi ', 'id' : 'YH', 'level' : 'lukio'},
			{ 'name' : 'Terveystieto ', 'id' : 'TE', 'level' : 'lukio'},
			{ 'name' : 'Liikunta ', 'id' : 'LI', 'level' : 'lukio'},
			{ 'name' : 'Musiikki', 'id' : 'MU', 'level' : 'lukio'},
			{ 'name' : 'Kuvataide', 'id' : 'KU', 'level' : 'lukio'},
			{ 'name' : 'Opinto-ohjaus ', 'id' : 'OP', 'level' : 'lukio'}
		];
		
		for(var i = 0; i < data.length;i++){
			api.subject.createSubject(data[i]["id"], data[i]["name"], data[i]["level"]);
		}	
	}
})
