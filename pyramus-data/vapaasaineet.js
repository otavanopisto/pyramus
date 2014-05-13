({
	'run' : function(api){
	
		var data = [
			 {'name' : 'Biologia ja maantiede', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Fysiikka ja kemia sekä geo-, avaruus- ja tähtitieteet', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Graafinen ja viestintätekniikka', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Historia ja arkeologia', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Hygieniapassi', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Kansalais- ja järjestötoiminta', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Kirjallisuus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Kuvataide', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Käsi- ja taideteollisuus ja käden taidot', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Liiketalous ja kauppa', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Liikunta ja urheilu', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Luonto- ja ympäristöala', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Musiikki', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muu humanistisen ja kasvatusalan koulutus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muu kulttuurialan koulutus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muu tietotekniikan hyväksikäyttö', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muu yhteiskunnallisten aineiden, liiketalouden ja hallinnon alan koulutus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muu yleissivistävä koulutus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Muut kielet', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Opetus- ja kasvatustyö ja psykologia', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Oppimisvalmiuksien kehittäminen ja motivointi', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Sosiaaliala', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Sosiaalitieteet', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Teatteri ja tanssi', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Terveysala', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Tieto- ja tietoliikennetekniikka', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Tietojenkäsittely ja tietotekniikan hyväksikäyttö', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Tietokoneen ajokorttikoulutus', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Vapaa-aika- ja nuorisotyö', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Venäjä', 'id' : '', 'level' : 'vapaasivistys'},
			 {'name' : 'Viestintä- ja informaatioala', 'id' : '', 'level' : 'vapaasivistys'}
		];

		for(var i = 0; i < data.length;i++){
			api.subject.createSubject(data[i]["id"], data[i]["name"], data[i]["level"]);
		}	
	}
})
