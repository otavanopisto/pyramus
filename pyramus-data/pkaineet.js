({
	'run' : function(api){
	
		var data = [
			{'name' : 'Biologia', 'id' : 'bi', 'level' : 'peruskoulu'},
			{'name' : 'Elämänkatsomustieto', 'id' : 'et', 'level' : 'peruskoulu'},
			{'name' : 'Englanti', 'id' : 'ena', 'level' : 'peruskoulu'},
			{'name' : 'Fysiikka', 'id' : 'fy', 'level' : 'peruskoulu'},
			{'name' : 'Historia', 'id' : 'hi', 'level' : 'peruskoulu'},
			{'name' : 'Kemia', 'id' : 'ke', 'level' : 'peruskoulu'},
			{'name' : 'Kuvataide', 'id' : 'ku', 'level' : 'peruskoulu'},
			{'name' : 'Liikunta', 'id' : 'li', 'level' : 'peruskoulu'},
			{'name' : 'Maantieto', 'id' : 'ge', 'level' : 'peruskoulu'},
			{'name' : 'Matematiikka', 'id' : 'ma', 'level' : 'peruskoulu'},
			{'name' : 'Musiikki', 'id' : 'mu', 'level' : 'peruskoulu'},
			{'name' : 'Opinto-ohjaus', 'id' : 'op', 'level' : 'peruskoulu'},
			{'name' : 'Ruotsi', 'id' : 'rub', 'level' : 'peruskoulu'},
			{'name' : 'Terveystieto', 'id' : 'te', 'level' : 'peruskoulu'},
			{'name' : 'Uskonto', 'id' : 'ue', 'level' : 'peruskoulu'},
			{'name' : 'yhteiskuntaoppi', 'id' : 'yh', 'level' : 'peruskoulu'},
			{'name' : 'Äidinkieli ja kirjallisuus (s2)', 'id' : 's2', 'level' : 'peruskoulu'},
			{'name' : 'Äidinkieli ja kirjallisuus, suomi äidinkielenä', 'id' : 'äi', 'level' : 'peruskoulu'}
		];
		
		for(var i = 0; i < data.length;i++){
			api.subject.createSubject(data[i]["id"], data[i]["name"], data[i]["level"]);
		}	
	}
})
