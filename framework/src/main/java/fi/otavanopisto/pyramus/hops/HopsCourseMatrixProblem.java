package fi.otavanopisto.pyramus.hops;

public enum HopsCourseMatrixProblem {
  
  INCOMPATIBLE_STUDENT,           // Opiskelijalla ei ole OPSia, lukio-opiskelijan OPS ei ole 2021, perusopetuksen opiskelijan OPS ei ole 2018, tai opiskelija ei ole lukio/perusopetus
  NO_NATIVE_LANGUAGE,             // Ei ainevalintaa äidinkielelle
  NO_MATH,                        // Ei ainevalintaa matematiikalle
  NO_RELIGION,                    // Ei ainevalintaa uskonnolle
  NO_PRIMARY_FOREIGN_LANGUAGE,    // Ei ainevalintaa A-kielelle
  NO_SECONDARY_FOREIGN_LANGUAGE,  // Ei ainevalintaa B1-kielelle
  INTERNAL_ERROR                  // Esim. opetussuunnitelma-jsonissa häikkää

}
