({
  'run' : function(api){
    api.log('Importing study programmes...');
    
    var programmes = [
      {code: "peruskoulu", name: "Peruskoulu"},
      {code: "lukio", name: "Lukio"},
      {code: "ammatillinen", name: "Ammatillinen"},
      {code: "vapaasivistys", name: "Vapaa sivistystyö"}
    ];
    
    for (var i = 0, l = programmes.length; i < l; i++) {
      var programme = programmes[i];
      
      var educationTypeId = api.educationTypes.findIdByCode(programme.code);
      if (!educationTypeId) {
        educationTypeId = api.educationTypes.create(programme.code, programme.name);
      }
      
      var categoryIds = api.studyProgrammeCategories.listIdsByName(programme.name);
      var categoryId = categoryIds.length > 0 ? categoryIds[0] : null;
      
      if (!categoryId) {
        categoryId = api.studyProgrammeCategories.create(programme.name, educationTypeId);
      }
      
      if (!api.studyProgrammes.findIdByCode(programme.code)) {
        api.studyProgrammes.create(programme.name, categoryId, programme.code);
      } else {
        api.log("StudyProgrammes '" + programme.code + "' already exists.");
      }
      
    }
 
  }
})