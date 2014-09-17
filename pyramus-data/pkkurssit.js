({
	'run' : function(api){
	  api.log('Importing courses...');
    
	  var data = [
      {"subject":"äi","cnum":1,"name":"Äidinkielen ja kirjallisuuden perusteita"},
      {"subject":"äi","cnum":2,"name":"Tekstien rakenteita ja merkityksiä"},
      {"subject":"äi","cnum":3,"name":"Tekstien erittelyä ja tulkintaa"},
      {"subject":"äi","cnum":4,"name":"Suomen kielen ja kirjallisuuden ominaispiirteitä"},
      {"subject":"ena","cnum":1,"name":"Perhe ja lähiympäristö"},
      {"subject":"ena","cnum":2,"name":"Sosiaaliset verkostot"},
      {"subject":"ena","cnum":3,"name":"Asioiminen ja palveluiden käyttö"},
      {"subject":"ena","cnum":4,"name":"Erilaisia elinympäristöjä"},
      {"subject":"ena","cnum":5,"name":"Vapaa-aika ja harrastukset"},
      {"subject":"ena","cnum":6,"name":"Työ ja opiskelu"},
      {"subject":"ena","cnum":7,"name":"Terveys ja hyvinvointi"},
      {"subject":"ena","cnum":8,"name":"Kulttuuri ja kansainvälisyys"},
      {"subject":"rub","cnum":1,"name":"Perhe ja lähiympäristö"},
      {"subject":"rub","cnum":2,"name":"Sosiaaliset verkostot"},
      {"subject":"rub","cnum":3,"name":"Asioiminen ja palveluiden käyttö"},
      {"subject":"rub","cnum":4,"name":"Erilaisia elinympäristöjä"},
      {"subject":"rub","cnum":5,"name":"Vapaa-aika ja harrastukset"},
      {"subject":"rub","cnum":6,"name":"Työ ja opiskelu"},
      {"subject":"ma","cnum":1,"name":"Luvut ja laskutoimitukset I"},
      {"subject":"ma","cnum":2,"name":"Luvut ja laskutoimitukset II"},
      {"subject":"ma","cnum":3,"name":"Lausekkeet ja yhtälöt"},
      {"subject":"ma","cnum":4,"name":"Yhtälöt ja lukujonot"},
      {"subject":"ma","cnum":5,"name":"Funktiot"},
      {"subject":"ma","cnum":6,"name":"Geometria"},
      {"subject":"ma","cnum":7,"name":"Geometria ja trigonometria"},
      {"subject":"ma","cnum":8,"name":"Todennäköisyys ja tilastot"},
      {"subject":"ue","cnum":3,"name":"Maailmanuskonnot"},
      {"subject":"et","cnum":1,"name":"Elämänkatsomuksen perusteet"},
      {"subject":"hi","cnum":1,"name":"Esihistoriasta Ranskan suureen vallankumoukseen"},
      {"subject":"hi","cnum":2,"name":"Wienin kongressista ensimmäiseen maailmansotaan"},
      {"subject":"hi","cnum":3,"name":"Versailles’sta nykypäivään"},
      {"subject":"yh","cnum":1,"name":"Yhteiskuntatiedon perusteet"},
      {"subject":"yh","cnum":2,"name":"Taloustiedon perusteet"},
      {"subject":"fy","cnum":1,"name":"Liike ja työ"},
      {"subject":"fy","cnum":2,"name":"Värähdysliike ja lämpö"},
      {"subject":"fy","cnum":3,"name":"Sähkö ja luonnon rakenteet"},
      {"subject":"ke","cnum":1,"name":"Ilma ja vesi"},
      {"subject":"ke","cnum":2,"name":"Raaka-aineet ja tuotteet"},
      {"subject":"ke","cnum":3,"name":"Elämän kemiaa"},
      {"subject":"bi","cnum":1,"name":"Elämä ja evoluutio"},
      {"subject":"bi","cnum":2,"name":"Ekosysteemit ja ympäristönsuojelu"},
      {"subject":"bi","cnum":3,"name":"Ihminen"},
      {"subject":"ge","cnum":1,"name":"Kotiplaneettamme Maa"},
      {"subject":"ge","cnum":2,"name":"Eurooppa"},
      {"subject":"ge","cnum":3,"name":"Suomi maailmassa"},
      {"subject":"op","cnum":1,"name":"Opintojen ohjaus"},
      {"subject":"ue","cnum":1,"name":"Kirkko ja suomalainen katsomusperinne"},
      {"subject":"ue","cnum":2,"name":"Raamattutieto"},
      {"subject":"ena","cnum":12,"name":"Tukikurssi"},
      {"subject":"s2","cnum":1,"name":"Suomen kielen perusteiden varmentaminen"},
      {"subject":"s2","cnum":4,"name":"Suomalainen kulttuuri ja yhteiskuntaelämä"},
      {"subject":"s2","cnum":2,"name":"Luonto ja vapaa-aika, tekniikka ja ympäristö"},
      {"subject":"s2","cnum":3,"name":"Suomen työelämän ja julkisen elämän käytänteitä"}
    ];
    
		for (var i = 0; i < data.length;i++) {
		  var course = data[i];
		  var moduleId = api.modules.listIdsBySubjectCodeAndCourseNumber(course.subject, course.cnum)[0];
		  
		  if (api.courses.listIdsByModuleId(moduleId).length == 0) {
		    api.courses.create(moduleId, course.name, "GEN", "Kurssi '" + course.name + "'", course.subject);
      } else {
        api.log("Course for module #" + moduleId + " already exists");
      }
		}	
	}
})