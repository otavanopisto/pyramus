/*

TODO

- Kaikki 2005-opsiin liittyvä
- Diaarinumerot
- RUB

*/


async function parseKoskiTransferCredits(henkilo, curriculumId, studentSSNHash) {
  // Tulosobjektit

  var credits = [];
  var notes = [];
  var errors = [];
  var curriculumNotes = [];
  var curriculumErrors = [];
  
  var results = {
    credits: credits,
    notes: notes,
    errors: errors,
    curriculumNotes: curriculumNotes,
    curriculumErrors: curriculumErrors
  };

  // Perustarkistukset

  if (!henkilo) {
    errors.push(getLocale().getText("students.manageTransferCredits.tcm.noPersonJSONGiven"));
    return;
  }
  
  var opiskeluoikeudet = henkilo.opiskeluoikeudet;
  
  if (!opiskeluoikeudet) {
    errors.push(getLocale().getText("students.manageTransferCredits.tcm.noStudyPermits"));
    return;
  }

  if (henkilo["henkilö"].hetu && studentSSNHash) {
    var hetuhash = await sha256(henkilo["henkilö"].hetu);
    if (hetuhash != studentSSNHash) {
      errors.push(getLocale().getText("students.manageTransferCredits.tcm.ssnMismatch"));
    }
  }

  // Staattiset mäppäykset
  
  // https://koski.opintopolku.fi/koski/dokumentaatio/koodisto/opintojenlaajuusyksikko/latest
  const OPINTOJENLAAJUUSYKSIKKO = {
    "1": "ov",
    "2": "op",
    "3": "vuosiviikkotuntia",
    "4": "kurssia",
    "5": "tuntia",
    "6": "osp",
    "7": "vuotta",
    "8": "vk"
  };

  // Dynaamiset mäppäykset

  // Oppiaine-map: subjectCode -> subject
  const pyramusOppiaineet = {};
  var opetussuunnitelma = null;

  // TODO educationtype = lukion id
  
  await axios.get("/common/educationTypes/{0}/subjects".format("2"))
    .then(function (response) {
      var ret = response.data;
      
      if (ret) {
        for (const oppiaine of ret) {
          pyramusOppiaineet[oppiaine.code] = oppiaine;
        }
      }
    })
    .catch(function (error) {
      errors.push(getLocale().getText("students.manageTransferCredits.tcm.fetchingSubjectsFailed"));
      console.error(error);
    });

  if (curriculumId) {
    await axios.get("/common/curriculums/{0}/json".format(curriculumId))
      .then(function (response) {
        opetussuunnitelma = response.data;        
      })
      .catch(function (error) {
        errors.push(getLocale().getText("students.manageTransferCredits.tcm.fetchingStudentsCurriculumFailed"));
        console.error(error);
      });
  }

  // Oppilaitos-map: oppilaitoksen numerokoodi -> oppilaitos    
  const oppilaitosCache = {};

  // Opiskeluoikeuksien läpikäynti
  
  for (const opiskeluoikeus of opiskeluoikeudet) {
    var suoritukset = opiskeluoikeus.suoritukset;

    if (!suoritukset) {
      errors.push(getLocale().getText("students.manageTransferCredits.tcm.studyPermitMissingAThing"))
    }
    else {
      var oppilaitos = null;
  
      // Selvitetään oppilaitos oppilaitosnumeron perusteella
      
      if (opiskeluoikeus.oppilaitos && opiskeluoikeus.oppilaitos.oppilaitosnumero && opiskeluoikeus.oppilaitos.oppilaitosnumero.koodiarvo) {
        var oppilaitosnumero = opiskeluoikeus.oppilaitos.oppilaitosnumero.koodiarvo;
  
        if (oppilaitosCache[oppilaitosnumero]) {
          oppilaitos = oppilaitosCache[oppilaitosnumero];
        }
        else {
          await axios.get("/schools/schools?code={0}&filterArchived=true".format(oppilaitosnumero))
            .then(function (response) {
              const ret = response.data;
              
              if (ret) {
                if (ret.length == 0) {
                  errors.push(getLocale().getText("students.manageTransferCredits.tcm.schoolNotFoundWithNumber", oppilaitosnumero));
                }
                else {
                  if (ret.length > 1) {
                    errors.push(getLocale().getText("students.manageTransferCredits.tcm.multipleSchoolsWithNumber", oppilaitosnumero));
                  }
                  
                  oppilaitos = ret[0];
                  oppilaitosCache[oppilaitosnumero] = oppilaitos;
                }
              }
            })
            .catch(function (error) {
              errors.push(getLocale().getText("students.manageTransferCredits.tcm.fetchingSchoolFailed", oppilaitosnumero));
              console.error(error);
            });
        }
      }

      for (const suoritus of suoritukset) {
        const diaarinumero = suoritus.koulutusmoduuli.perusteenDiaarinumero;
        if (!diaarinumero) {
          errors.push(getLocale().getText("students.manageTransferCredits.tcm.journalNumberMissing"));
          break;
        }
        
        const diaarinumerotOPS2021 = [
          "OPH-2263-2019", 
          "OPH-2267-2019"
        ];
        
        var vastaavuustaulukko = null;

        // Jos diaarinumero viittaa 2021 opetussuunnitelmaan, ei käytetä vastaavuustaulukoita
        if (diaarinumerotOPS2021.indexOf(diaarinumero) != -1) {
          notes.push(getLocale().getText("students.manageTransferCredits.tcm.skipTranslationTables", diaarinumero));
        }
        else {
          vastaavuustaulukko = opsVastaavuustaulukko(diaarinumero);
          
          
          if (vastaavuustaulukko) {
            notes.push(getLocale().getText("students.manageTransferCredits.tcm.selectedTranslationTable", vastaavuustaulukko.nimi, diaarinumero));
          }
          else {
            errors.push(getLocale().getText("students.manageTransferCredits.tcm.noTranslationTable", diaarinumero));
            break;
          }
        }
        
        // Ensimmäinen osasuoritukset = oppiainetaso
        var oppiaineet = suoritus.osasuoritukset;
        
        if (!oppiaineet) {
          errors.push(getLocale().getText("students.manageTransferCredits.tcm.studyPermitHasNoSubjects"));
        }
        else {
          for (const oppiaine of oppiaineet) {
            // Toinen osasuoritukset = kurssitaso
            var kurssit = oppiaine.osasuoritukset;
            
            if (!kurssit) {
              errors.push(getLocale().getText("students.manageTransferCredits.tcm.subjectHasNoCourses"));
            }
            else {
              for (const kurssi of kurssit) {
                const kurssiKoodi = kurssi.koulutusmoduuli.tunniste.koodiarvo;
                const arviointi = parasArviointi(kurssi.arviointi, kurssiKoodi, results);

                if (arviointi == null) {
                  results.notes.push(getLocale().getText("students.manageTransferCredits.tcm.noApprovedGradeForCourse", kurssiKoodi));
                }
                else {
                  const arvosana = arviointi ? arviointi.arvosana.koodiarvo : "";
                  var laajuus = null;
                  var laajuusYksikko = null;

                  if (!kurssi.koulutusmoduuli.laajuus) {
                    errors.push(getLocale().getText("students.manageTransferCredits.tcm.noLengthForCourse", kurssiKoodi));
                  }
                  else {
                    laajuus = kurssi.koulutusmoduuli.laajuus.arvo;
                    
                    var yksikkoKoodi = kurssi.koulutusmoduuli.laajuus["yksikkö"].koodiarvo;
                    laajuusYksikko = yksikkoKoodi ? OPINTOJENLAAJUUSYKSIKKO[yksikkoKoodi] : null;
                  }
                  
                  var kurssinTyyppi = kurssi.koulutusmoduuli.kurssinTyyppi;
                  var pakollinen = (kurssinTyyppi && kurssinTyyppi.koodiarvo == "pakollinen" && kurssinTyyppi.koodistoUri == "lukionkurssintyyppi") ? true : false;
                  
                  var parsitutKurssiKoodit = parseKoskiCourseCode(kurssiKoodi, vastaavuustaulukko, opetussuunnitelma, results);
                  
                  for (const parsittuKurssiKoodi of parsitutKurssiKoodit) {
                    // pyrOppiaine = Pyramuksen Subject:n json-kuvaus, tarvitaan id, code ja nimi sieltä
                    var pyrOppiaine = parsittuKurssiKoodi.subject ? pyramusOppiaineet[parsittuKurssiKoodi.subject] : null;
                    
                    credits.push({
                      convertedOk: parsittuKurssiKoodi.convertedOk,
                      courseName: parsittuKurssiKoodi.courseName ? parsittuKurssiKoodi.courseName : "",
                      courseCode: parsittuKurssiKoodi.subject + (parsittuKurssiKoodi.courseNumber != null ? parsittuKurssiKoodi.courseNumber : ""),
                      subject: pyrOppiaine,
                      subjectCode: parsittuKurssiKoodi.subject,
                      courseNumber: parsittuKurssiKoodi.courseNumber,
                      grade: arvosana,
                      courseLength: parsittuKurssiKoodi.courseLength ? parsittuKurssiKoodi.courseLength : laajuus,
                      courseLengthUnit: parsittuKurssiKoodi.courseLengthUnit ? parsittuKurssiKoodi.courseLengthUnit : laajuusYksikko,
                      mandatory: (typeof parsittuKurssiKoodi.mandatory == "boolean") ? parsittuKurssiKoodi.mandatory : pakollinen,
                      school: oppilaitos
                    });
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  return results;
}

function parseKoskiCourseCode(courseCode, vastaavuustaulukko, opetussuunnitelma, tulosObjekti) {
  if (!courseCode || !(typeof courseCode == "string")) {
    return null;
  }
  
//  var doConvert = false;
  var convertedOk = false;
  var subject = null;
  var courseNumber = null;
  var courseName = null;
  var courseLength = null;
  var courseLengthUnit = null;
  var mandatory = null;

  // Jos vastaavuustaulukko on olemassa, tehdään sen avulla kurssin muunnos 
  // uuteen opetussuunnitelmaan. Jos vastaavuustaulukkoa ei ole annettu,
  // kurssikoodin oletetaan olevan uudesta opetussuunnitelmasta jo valmiiksi.
  
  if (vastaavuustaulukko) {
    const opsOppiaineet = Object.keys(vastaavuustaulukko.aineet);
  
    // Etsitään oppiaine, jonka koodi on annetun kurssikoodin alussa ja
    // katkaistaan se aineen koodiksi ja kurssinumeroksi.
    for (const opsOppiaine of opsOppiaineet) {
      if (courseCode.startsWith(opsOppiaine)) {
        subject = opsOppiaine;
        const numstart = subject.length;
        courseNumber = numstart != -1 ? Number(courseCode.slice(numstart)) : null;
        break;
      }
    }
  
    // Jos oppiainetta ei aiemmin löytynyt, kyseessä voi olla esim paikallinen 
    // kurssikooditus. Koitetaan katkaista ensimmäisestä numerosta.
    if (!subject) {
      var numstart = courseCode.search("[1-9]");
      subject = numstart != -1 ? courseCode.slice(0, numstart) : courseCode;
      courseNumber = numstart != -1 ? Number(courseCode.slice(numstart)) : null; 
    }
    
    var konversioAine = vastaavuustaulukko.aineet[subject];
    if (konversioAine) {
      const konversioKurssi = konversioAine[courseNumber];
      if (konversioKurssi) {
        switch (konversioKurssi.type) {
          case "OK": {
            // Suora konversio
            
            const opsmoduli = etsiOpsModuli(subject, courseNumber, opetussuunnitelma, tulosObjekti);
            if (opsmoduli) {
              if (opsmoduli.name) {
                courseName = subject + courseNumber + " " + opsmoduli.name;
              }

              return [{
                convertedOk: true,
                subject: subject,
                courseNumber: typeof courseNumber == "number" ? courseNumber : null,
                courseName: courseName,
                courseLength: opsmoduli.length,
                courseLengthUnit: opsmoduli.lengthUnitSymbol,
                mandatory: opsmoduli.mandatory
              }];
            }
            
//            doConvert = true;
          }
          break;
          
          case "KNRO": {
            // Muuttunut oppiaine / kurssinumero
            const uusiSubject = konversioKurssi.subject ? konversioKurssi.subject : subject;
            tulosObjekti.curriculumNotes.push(getLocale().getText("students.manageTransferCredits.tcm.courseTranslation", subject + courseNumber, uusiSubject + konversioKurssi.to));
            
            subject = uusiSubject;
            courseNumber = konversioKurssi.to;
            
            const opsmoduli = etsiOpsModuli(subject, courseNumber, opetussuunnitelma, tulosObjekti);
            if (opsmoduli) {
              if (opsmoduli.name) {
                courseName = subject + courseNumber + " " + opsmoduli.name;
              }

              return [{
                convertedOk: true,
                subject: subject,
                courseNumber: typeof courseNumber == "number" ? courseNumber : null,
                courseName: courseName,
                courseLength: opsmoduli.length,
                courseLengthUnit: opsmoduli.lengthUnitSymbol,
                mandatory: opsmoduli.mandatory
              }];
            }
//            doConvert = true;
          }
          break;
          
          case "MONI": {
            // Yhdestä kurssista useita hyväksilukuja
            
            if (konversioKurssi.moni) {
              var konversioKohde = "";
              for (const konversioModuli of konversioKurssi.moni) {
                const moduliAine = konversioModuli.subject ? konversioModuli.subject : subject;
                const moduliKnro = konversioModuli.no;

                if (konversioKohde != "") {
                  konversioKohde += ", ";
                }
                konversioKohde += moduliAine + moduliKnro;
              }
              
              tulosObjekti.curriculumNotes.push(getLocale().getText("students.manageTransferCredits.tcm.courseTranslation", subject + courseNumber, konversioKohde));

              const konversioModulit = [];
              for (const konversioModuli of konversioKurssi.moni) {
                const moduliAine = konversioModuli.subject ? konversioModuli.subject : subject;
                const moduliKnro = konversioModuli.no;
                
                const opsmoduli = etsiOpsModuli(moduliAine, moduliKnro, opetussuunnitelma, tulosObjekti);
                if (opsmoduli) {
                  if (opsmoduli.name) {
                    courseName = moduliAine + moduliKnro + " " + opsmoduli.name;
                  }
    
                  konversioModulit.push({
                    convertedOk: true,
                    subject: moduliAine,
                    courseNumber: typeof moduliKnro == "number" ? moduliKnro : null,
                    courseName: courseName,
                    courseLength: opsmoduli.length,
                    courseLengthUnit: opsmoduli.lengthUnitSymbol,
                    mandatory: opsmoduli.mandatory
                  });
                }
              }
              return konversioModulit;
            }
          }
          break;
          case "KORJAA_KÄSIN":
            tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.manualTranslation", courseCode));
          break;
        }
      }
      else {
        tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.noTranslationForCourse", courseCode));
      }
    }
    else {
      tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.noTranslationForSubject", courseCode));
    }
  }
  else {
    // Vastaavuustaulukkoa ei ole, haetaan tiedot (uudesta) opetussuunnitelmasta
    
    if (opetussuunnitelma && opetussuunnitelma.subjects) {
      for (const opsOppiaine of opetussuunnitelma.subjects) {
        if (courseCode.startsWith(opsOppiaine.code)) {
          subject = opsOppiaine.code;
          const numstart = subject.length;
          courseNumber = numstart != -1 ? Number(courseCode.slice(numstart)) : null;

          const opsmodule = opsOppiaine.modules.find(m => m.courseNumber == courseNumber);
          if (opsmodule) {
            if (opsmodule.name) {
              courseName = subject + courseNumber + " " + opsmodule.name;
            }
            mandatory = opsmodule.mandatory;
            courseLength = opsmodule.length;
            courseLengthUnit = opsmodule.lengthUnitSymbol;
            convertedOk = true;
          }
          else {
            // Kurssia ei löydy Pyramuksessa olevasta opetussuunnitelman kuvauksesta
            if (subject) {
              tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.noCurriculumMatchForCourse", subject + (courseNumber ? courseNumber : "")));
            }
            else {
              tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.noCurriculumMatchForCourseOriginal", courseCode));
            }
          }

          return [{
            convertedOk: convertedOk,
            subject: subject,
            courseNumber: typeof courseNumber == "number" ? courseNumber : null,
            courseName: courseName,
            courseLength: courseLength,
            courseLengthUnit: courseLengthUnit,
            mandatory: mandatory
          }];
        }
      }
    }
  }
  
  return [];
}

function etsiOpsModuli(subject, courseNumber, opetussuunnitelma, tulosObjekti) {
  // Etsitään opetussuunnitelmasta kurssin/modulin tiedot
  if (subject && courseNumber && opetussuunnitelma && opetussuunnitelma.subjects) {
    const opssubject = opetussuunnitelma.subjects.find(s => s.code == subject);
    if (opssubject && opssubject.modules) {
      const opsmodule = opssubject.modules.find(m => m.courseNumber == courseNumber);
      if (opsmodule) {
        return opsmodule;
      }
    }
  }
  
  // Kurssia ei löydy Pyramuksessa olevasta opetussuunnitelman kuvauksesta
  tulosObjekti.curriculumErrors.push(getLocale().getText("students.manageTransferCredits.tcm.noCurriculumMatchForCourse", subject + (courseNumber ? courseNumber : "")));
            
  return null;
}

/**
 * Palauttaa "parhaan" arvioinnin arvioinnit-listasta.
 *
 * Palauttaa null, jos yhtään läpäisevää arvosanaa ei pystytä selvittämään.
 */
function parasArviointi(arvioinnit, kurssiKoodi, results) {
  if (!arvioinnit || !arvioinnit.length) {
    results.errors.push(getLocale().getText("students.manageTransferCredits.tcm.courseHasNoAssessments", kurssiKoodi));
    return null;
  } 

  // Läpäisevät arvosanat. Kosken antamassa arvioinnin jsonissa olisi myös
  // property "hyväksytty", mutta tämä on tietoja syötettäessä optionaalinen
  // joten ei luoteta siihen.
  const lapaisevatArvosanat = [ '5', '6', '7', '8', '9', '10', 'O', 'S' ];

  var arviointi = arvioinnit[0];
  var arviointiArvosanaNum = parseInt(arviointi.arvosana.koodiarvo, 10);
  var arviointiLapaiseva = lapaisevatArvosanat.indexOf(arviointi.arvosana.koodiarvo) != -1;

  for (var i = 1; i < arvioinnit.length; i++) {
    const i_lapaiseva = lapaisevatArvosanat.indexOf(arvioinnit[i].arvosana.koodiarvo) != -1;
    
    // Hyväksytty arviointi > ei-hyväksytty
    if (!arviointiLapaiseva && i_lapaiseva) {
      arviointi = arvioinnit[i];
      arviointiArvosanaNum = parseInt(arviointi.arvosana.koodiarvo, 10);
      arviointiLapaiseva = i_lapaiseva;
    }
    else {
      var arvosanaNum = parseInt(arvioinnit[i].arvosana.koodiarvo, 10);
      // Jos i:nnen arvioinnin arvosana on suurempi, käytetään sitä
      if (!isNaN(arvosanaNum) && !isNaN(arviointiArvosanaNum) && arvosanaNum > arviointiArvosanaNum) {
        arviointi = arvioinnit[i];
        arviointiArvosanaNum = parseInt(arviointi.arvosana.koodiarvo, 10);
        arviointiLapaiseva = i_lapaiseva;
      }
    }
  }

  if (arvioinnit.length > 1) {
    results.notes.push(getLocale().getText("students.manageTransferCredits.tcm.courseHasMultipleAssessments", kurssiKoodi, arviointi ? arviointi.arvosana.koodiarvo : "?"));
  }

  return arviointiLapaiseva ? arviointi : null;
}

/**
 * Palauttaa vastaavuustaulukon (objektin), joka kuvaa, miten
 * annetun diaarinumeron mukaisesta opsista konvertoidaan
 * kursseja vuoden 2021 opetussuunnitelmaan.
 *
 * Eri tyypit:
 *  - OK = Suora konversio, sama kurssinumero
 *  - KNRO = Konversio, jossa kurssinumero muuttuu
 *  - KORJAA_KÄSIN = Käsin korjattava kurssi, ei koneellista vastaavuutta
 */
function opsVastaavuustaulukko(diaarinumero) {
  const OPS2015DIAARIT = [
    "60/011/2015",   // Nuorten ?
    "70/011/2015"
  ];

  // OPS 2015 -> OPS 2021
  if (OPS2015DIAARIT.indexOf(diaarinumero) != -1) {
    var ops2015yhteiset = {
      "ÄI": {
        1: { type: "OK" },
        2: { type: "MONI", moni: [
          { no: 2 },
          { no: 3 }
        ] },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 14 },
        6: { type: "KNRO", to: 15 },
        7: { type: "KNRO", to: 16 },
        8: { type: "KNRO", to: 6 },
        9: { type: "KNRO", to: 10 },
        11: { type: "KNRO", to: 17 },
      },
      "S2": {
        1: { type: "OK" },
        2: { type: "MONI", moni: [
          { no: 2 },
          { no: 3 }
        ] },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 12 },
        6: { type: "KNRO", to: 13 },
        7: { type: "MONI", moni: [
          { no: 7 },
          { no: 14 }
        ] },
        8: { type: "KNRO", to: 10 },
        9: { type: "KNRO", to: 11 }
      },
      "BI": {
        1: { type: "OK" },
        2: { type: "MONI", moni: [
          { no: 2 },
          { no: 3 }
        ] },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 6 }
      },
      "ENA": {
        1: { type: "MONI", moni: [
          { no: 1 },
          { no: 13 }
        ] },
        2: { type: "KNRO", to: 12 },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" },
        7: { type: "OK" },
        8: { type: "OK" },
        9: { type: "MONI", moni: [
          { no: 9 },
          { no: 10 }
        ] },
        11: { type: "OK" }
      },
      "MAY": {
        1: { type: "OK" },
        15: { type: "OK" }
      },
      "MAA": {
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "KORJAA_KÄSIN", to: 4 },
        5: { type: "KORJAA_KÄSIN", to: 5 },
        6: { type: "OK" },
        7: { type: "KNRO", to: 5 },
        8: { type: "KNRO", to: 18 },
        9: { type: "KNRO", to: 7 },
        10: { type: "KNRO", to: 8 },
        11: { type: "KORJAA_KÄSIN", to: 11 },
        12: { type: "KORJAA_KÄSIN", to: 12 },
        13: { type: "KNRO", to: 12 }
      },
      "MAB": {
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "MONI", moni: [
          { no: 6 },
          { no: 7 }
        ] },
        7: { type: "KNRO", to: 8 },
        8: { type: "KNRO", to: 9 },
        11: { type: "KNRO", to: 10 }        
      },
      "FY": {
        1: { type: "MONI", moni: [
          { no: 1 },
          { no: 9 }
        ] },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 6 },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "KNRO", to: 7 },
        7: { type: "KNRO", to: 8 }
      },
      "KE": {
        1: { type: "MONI", moni: [
          { no: 1 },
          { no: 2 }
        ] },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 6 }
      },
      "RUB1": {
        1: { type: "KORJAA_KÄSIN", to: 1 },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" },
        7: { type: "OK" },
        8: { type: "OK" },
        9: { type: "OK" },
        10: { type: "OK" }
      },
      "GE": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" }
      },
      "YH": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" }
      },
      "KU": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" }
      },
      "FI": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" }
      },
      "UE": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" }
      },
      "UO": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" }
      },
      "UI": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" }
      },
      "UJ": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "OK" }
      },
      "ET": {
        1: { type: "KNRO", to: 7 }, // S ?
        2: { type: "KNRO", to: 1 },
        3: { type: "KNRO", to: 2 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 },
        6: { type: "OK" }
      },
      "PS": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" }
      },
      "TE": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" }
      },
      "LI": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" }
      },
      "MU": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" }
      },
      "OP": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" }
      },
      "AT": {
        1: { type: "KORJAA_KÄSIN", to: 1 },
        2: { type: "OK" },
        3: { type: "KNRO", to: 6 },
        4: { type: "OK" },
        5: { type: "KNRO", to: 7 },
        6: { type: "KNRO", to: 8 },
        7: { type: "KNRO", to: 9 },
      },
      "TO": {
        1: { type: "OK", to: 1 }
      }
    };
    
    const kieliKoodit = [ "EN", "EA", "LA", "PO", "RA", "KI", "SM", "JP", "SA", "VE", "IA" ];
    for (const kieliKoodi of kieliKoodit) {
      ops2015yhteiset[kieliKoodi + "B3"] = {
        1: { type: "OK", to: 1 },
        2: { type: "OK", to: 2 },
        3: { type: "OK", to: 3 },
        4: { type: "OK", to: 4 },
        5: { type: "OK", to: 5 },
        6: { type: "OK", to: 6 },
        7: { type: "OK", to: 7 },
        8: { type: "OK", to: 8 }
      };
    }
    
    var ops2015aikuiset = {
      "HI": {
        1: { type: "OK", to: 1 },
        2: { type: "OK", to: 2 },
        3: { type: "OK", to: 3 },
        4: { type: "OK", to: 4 },
        5: { type: "OK", to: 5 },
        6: { type: "OK", to: 6 }
      }
    };
    
    var ops2015nuoret = {
      "HI": {
        1: { type: "OK", to: 1 },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 2 },
        4: { type: "OK", to: 4 },
        5: { type: "OK", to: 5 },
        6: { type: "OK", to: 6 }
      }
    };
    
    switch (diaarinumero) {
      case "70/011/2015": return {
        diaarinumero: "70/011/2015",
        nimi: "2016 Aikuiset",
        aineet: Object.assign(ops2015yhteiset, ops2015aikuiset)
      }
      case "60/011/2015": return {
        diaarinumero: "60/011/2015",
        nimi: "2016 Nuoret",
        aineet: Object.assign(ops2015yhteiset, ops2015nuoret)
      }
    }
  }
  else {
    // 2005 opsit
    
    var ops2005yhteiset = {
      "S2": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 5 },
        4: { type: "KNRO", to: 12 },
        5: { type: "KNRO", to: 4 }
      },
      "BI": {
        1: { type: "OK" },
        2: { type: "KNRO", to: 4 },
        3: { type: "KORJAA_KÄSIN", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 6 }
      },
      "ENA": {
        1: { type: "KORJAA_KÄSIN", to: 1 },
        2: { type: "KNRO", to: 12 },
        3: { type: "KNRO", to: 6 },
        4: { type: "OK" },
        5: { type: "KNRO", to: 3 },
        6: { type: "KNRO", to: 5 },
        7: { type: "OK" },
        8: { type: "OK" }
      },
      "MAA": {
        1: { type: "KNRO", to: 1, subject: "MAY" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "KORJAA_KÄSIN", to: 4 },
        5: { type: "KORJAA_KÄSIN", to: 5 },
        6: { type: "KNRO", to: 8 },
        7: { type: "KNRO", to: 6 },
        8: { type: "KNRO", to: 18 },
        9: { type: "KNRO", to: 5 },
        10: { type: "KNRO", to: 7 },
        11: { type: "KNRO", to: 11 },
        13: { type: "KNRO", to: 12 }
      },
      "MAB": {
        1: { type: "KNRO", to: 2 },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 8 },
        5: { type: "OK" },
        6: { type: "KNRO", to: 1, subject: "MAY" },
        7: { type: "KORJAA_KÄSIN", to: 8 },
        9: { type: "KNRO", to: 15, subject: "MAY" },
        10: { type: "KNRO", to: 9 }        
      },
      "FY": {
        1: { type: "OK" },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 5 },
        4: { type: "OK" },
        6: { type: "OK" },
        7: { type: "OK" },
        8: { type: "OK" }
      },
      "KE": {
        1: { type: "KNRO", to: 3 },
        2: { type: "KORJAA_KÄSIN", to: 3 },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 6 }
      },
      "RUB": {
        1: { type: "KNRO", to: 1, subject: "RUB1" },
        2: { type: "KNRO", to: 2, subject: "RUB1" },
        3: { type: "KNRO", to: 3, subject: "RUB1" },
        4: { type: "KNRO", to: 5, subject: "RUB1" },
        5: { type: "KNRO", to: 4, subject: "RUB1" },
        6: { type: "KNRO", to: 6, subject: "RUB1" },
        7: { type: "KNRO", to: 7, subject: "RUB1" },
        8: { type: "KNRO", to: 8, subject: "RUB1" },
        9: { type: "KNRO", to: 9, subject: "RUB1" }
      },
      "GE": {
        1: { type: "KNRO", to: 2 },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 1 },
        4: { type: "KNRO", to: 5 }
      },
      "YH": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 3 }
      },
      "FI": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 3 }
      },
      "UE": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 7 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 }
      },
      "UO": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 7 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 }
      },
      "UI": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 7 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 }
      },
      "UJ": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "KNRO", to: 7 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 }
      },
      "ET": {
        1: { type: "OK" },
        2: { type: "KNRO", to: 7 },
        3: { type: "KNRO", to: 2 },
        4: { type: "KNRO", to: 3 },
        5: { type: "KNRO", to: 4 }
      },
      "PS": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" }
      },
      "TE": {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" }
      }
    };
    
    const kieliKoodit = [ "EN", "EA", "LA", "PO", "RA", "KI", "SM", "JP", "SA", "VE", "IA" ];
    for (const kieliKoodi of kieliKoodit) {
      ops2005yhteiset[kieliKoodi + "B3"] = {
        1: { type: "OK" },
        2: { type: "OK" },
        3: { type: "OK" },
        4: { type: "OK" },
        5: { type: "OK" },
        6: { type: "KNRO", to: 7 },
        7: { type: "KNRO", to: 6 },
        8: { type: "OK" }
      };
    }
    
    var ops2005aikuiset = {
      "ÄI": {
        1: { type: "KNRO", to: 10 },
        2: { type: "KNRO", to: 4 },
        3: { type: "KNRO", to: 5 },
        4: { type: "KNRO", to: 14 },
        5: { type: "KNRO", to: 2 },
        6: { type: "OK", to: 6 },
        7: { type: "KNRO", to: 16 },
        8: { type: "KNRO", to: 1 },
        9: { type: "KNRO", to: 15 },
        11: { type: "KNRO", to: 17 },
      },
      "HI": {
        1: { type: "KNRO", to: 4 },
        2: { type: "KNRO", to: 3 },
        3: { type: "KNRO", to: 2 },
        4: { type: "KNRO", to: 1 },
        5: { type: "OK", to: 5 },
        6: { type: "OK", to: 6 }
      }
    };
    
    var ops2005nuoret = {
      "ÄI": {
        1: { type: "OK", to: 1 },
        2: { type: "KNRO", to: 10 },
        3: { type: "KNRO", to: 4 },
        4: { type: "KNRO", to: 5 },
        5: { type: "KNRO", to: 14 },
        6: { type: "KNRO", to: 2 },
        7: { type: "KNRO", to: 16 },
        8: { type: "KNRO", to: 6 },
        9: { type: "KNRO", to: 15 }
      },
      "HI": {
        1: { type: "OK" },
        2: { type: "KNRO", to: 4 },
        3: { type: "OK" },
        4: { type: "KNRO", to: 2 }
      }
    };

    /*
      TODO TODO TODO TODO TODO TODO TODO  Tarkista diaarinumerot, nuorten saattaa olla 30/011/2003 ?
    */
    
    switch (diaarinumero) {
      case "4/011/2004": return {
        diaarinumero: "4/011/2004",
        nimi: "2005 Aikuiset",
        aineet: Object.assign(ops2005yhteiset, ops2005aikuiset)
      }
      case "1/011/2004": return {
        diaarinumero: "1/011/2004",
        nimi: "2005 Nuoret",
        aineet: Object.assign(ops2005yhteiset, ops2005nuoret)
      }
    }
  }
  
  return null;
}

/**
 * Palauttaa vastaavuustaulukon, jossa on kuvattu kaikki eri
 * OPS-vastaavuudet nykyisin käytössä olevaan opetussuunnitelmaan.
 */
function opsVastaavuusTaulukkoKaikki() {
  const vastaavuustaulukot = [
    opsVastaavuustaulukko("60/011/2015"),
    opsVastaavuustaulukko("70/011/2015"),
    opsVastaavuustaulukko("1/011/2004"),
    opsVastaavuustaulukko("4/011/2004")
  ];
  
  const aineet = {};
  const opsit = vastaavuustaulukot.map(vt => { return { nimi: vt.nimi, diaarinumero: vt.diaarinumero } });
  const kokotaulukko = {
    aineet: aineet,
    opsit: opsit
  };

  for (const vt of vastaavuustaulukot) {
    const vt_oppiaineet = Object.keys(vt.aineet);
    for (const vt_oppiaine of vt_oppiaineet) {
      const vt_oppiaine_kurssinrot = Object.keys(vt.aineet[vt_oppiaine]);
      for (const vt_kurssinro of vt_oppiaine_kurssinrot) {
        const vt_vastaavuus = vt.aineet[vt_oppiaine][vt_kurssinro];
        switch (vt_vastaavuus.type) {
          case "OK": {
            var kaikkioppiainevastaavuudet = kokotaulukko.aineet[vt_oppiaine];
            if (!kaikkioppiainevastaavuudet) {
              kaikkioppiainevastaavuudet = {};
              kokotaulukko.aineet[vt_oppiaine] = kaikkioppiainevastaavuudet;
            }

            if (!kaikkioppiainevastaavuudet[vt_kurssinro]) {
              kaikkioppiainevastaavuudet[vt_kurssinro] = {};
            }
            kaikkioppiainevastaavuudet[vt_kurssinro][vt.diaarinumero] = vt_oppiaine + vt_kurssinro;
          }
          break;
          
          case "KNRO": {
            // Jos vastaavuustaulukko vaihtaa oppiaineen (esim RUB -> RUB1), käytetään sitä
            const knro_oppiaine = vt_vastaavuus.subject ? vt_vastaavuus.subject : vt_oppiaine;
            
            var kaikkioppiainevastaavuudet = kokotaulukko.aineet[knro_oppiaine];
            if (!kaikkioppiainevastaavuudet) {
              kaikkioppiainevastaavuudet = {};
              kokotaulukko.aineet[knro_oppiaine] = kaikkioppiainevastaavuudet;
            }

            if (!kaikkioppiainevastaavuudet[vt_vastaavuus.to]) {
              kaikkioppiainevastaavuudet[vt_vastaavuus.to] = {};
            }
            kaikkioppiainevastaavuudet[vt_vastaavuus.to][vt.diaarinumero] = vt_oppiaine + vt_kurssinro;
          }
          break;
          
          case "MONI": {
            var konversioKohde = "";
            for (const konversioModuli of vt_vastaavuus.moni) {
              const moduliAine = konversioModuli.subject ? konversioModuli.subject : vt_oppiaine;
              const moduliKnro = konversioModuli.no;

              if (konversioKohde != "") {
                konversioKohde += " + ";
              }
              konversioKohde += moduliAine + moduliKnro;
            }

            for (const konversioModuli of vt_vastaavuus.moni) {
              const moduliAine = konversioModuli.subject ? konversioModuli.subject : vt_oppiaine;
              const moduliKnro = konversioModuli.no;
              
              var kaikkioppiainevastaavuudet = kokotaulukko.aineet[moduliAine];
              if (!kaikkioppiainevastaavuudet) {
                kaikkioppiainevastaavuudet = {};
                kokotaulukko.aineet[moduliAine] = kaikkioppiainevastaavuudet;
              }
  
              if (!kaikkioppiainevastaavuudet[moduliKnro]) {
                kaikkioppiainevastaavuudet[moduliKnro] = {};
              }
              kaikkioppiainevastaavuudet[moduliKnro][vt.diaarinumero] = konversioKohde;
            }            
          }
          break;
          
          case "KORJAA_KÄSIN": {
            var kaikkioppiainevastaavuudet = kokotaulukko.aineet[vt_oppiaine];
            if (!kaikkioppiainevastaavuudet) {
              kaikkioppiainevastaavuudet = {};
              kokotaulukko.aineet[vt_oppiaine] = kaikkioppiainevastaavuudet;
            }

            if (!kaikkioppiainevastaavuudet[vt_kurssinro]) {
              kaikkioppiainevastaavuudet[vt_kurssinro] = {};
            }
            kaikkioppiainevastaavuudet[vt_kurssinro][vt.diaarinumero] = "K";
          }
          break;
        }
      }
    }
  }

  return kokotaulukko;
}



async function sha256(message) {
  const encoder = new TextEncoder();
  const data = encoder.encode(message);
  const hashBuffer = await crypto.subtle.digest("SHA-256", data);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  return hashArray.map(b => b.toString(16).padStart(2, "0")).join(""); // Convert to hex
}