<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-line">

    <h3 class="application-form-section-header">Valitse hakukohteesi</h3>

    <select id="field-line" name="field-line" data-parsley-required="true" data-dependencies="true" data-preselect="${preselectLine}">
      <option value="">-- Valitse --</option>
      <option value="aineopiskelu" data-underage-support="true" data-attachment-support="false">Aineopiskelu</option>
      <option value="nettilukio" data-underage-support="true" data-attachment-support="true">Nettilukio</option>
      <option value="nettipk" data-underage-support="true" data-attachment-support="true">Nettiperuskoulu</option>
      <option value="aikuislukio" data-underage-support="true" data-attachment-support="true">Aikuislukio</option>
      <!--<option value="bandilinja" data-underage-support="true" data-attachment-support="true">Bändilinja</option>-->
      <!--<option value="kasvatustieteet" data-underage-support="false" data-attachment-support="false">Kasvatustieteen linja</option>-->
      <!--<option value="laakislinja" data-underage-support="false" data-attachment-support="false">Lääkislinja</option>-->
      <option value="mk" data-underage-support="false" data-attachment-support="true">Maahanmuuttajakoulutukset</option>
    </select>

    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu">
      <p>Aineopiskelijana voit opiskella yksittäisiä lukion ja perusopetuksen kursseja. Aineopiskelijaksi voit ilmoittautua, vaikka opiskelisit samaan aikaan toisessa oppilaitoksessa.</p>
    </div>

    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
      <p>Nettilukiossa opiskelet koko aikuislukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Nettilukiossa voit myös tehdä loppuun aiemmin kesken jääneet lukio-opinnot. Nettilukio on tarkoitettu yli 18-vuotiaille opiskelijoille. Toisessa oppilaitoksessa opiskelevat ja yksittäisiä lukiokursseja suorittavat voivat ilmoittautua aineopiskelijaksi.</p>
    </div>
    
    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="nettipk">
      <p>Nettiperuskoulussa voit opiskella kesken jääneen peruskoulun loppuun tai tehdä koko aikuisten perusopetuksen oppimäärän alusta asti. Nettiperuskoulu on tarkoitettu yli 18-vuotiaille opiskelijoille, joilta puuttuu perusopetuksen päättötodistus.</p>
    </div>
    
    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio">
      <p>Aikuislukiossa opiskelet koko lukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Aikuislukiossa voit myös tehdä loppuun aiemmin kesken jääneet toisessa päivä- tai aikuislukiossa aloittamasi lukio-opinnot.</p>
    </div>
    
    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="bandilinja">
      <p><!-- TODO Bändilinjan esittelyteksti --></p>
    </div>

    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="kasvatustieteet">
      <p>Otavan Opiston Kasvatustieteen linjalla tutustut kasvatustieteen viimeisimpiin suuntauksiin ja saat tukea tuleviin yliopisto-opintoihin. Opetus toteutetaan sekä verkossa että lähiopetuksena Otavan Opistolla Mikkelissä. Koulutuksen sisältöjä ovat sulautuva oppiminen, oppimisen tulevaisuus ja VAKAVA-valmennus, jotka on mahdollisuus opiskella myös erillisinä kokonaisuuksina. Opintojen yhteydessä voi suorittaa kasvatustieteen perusopinnot.</p>
    </div>

    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="laakislinja">
      <p>Lääkislinjalla kerrataan ja syvennetään lukion biologian, fysiikan ja kemian oppimäärää, kerrataan lukion matematiikkaa, valmistaudutaan lääketieteellisen pääsykokeisiin harjoittelemalla eri tehtävätyyppien vastaustekniikoita ja tehdää simuloitu pääsykoe.</p>
    </div>
    
    <div class="field-container dependent" data-dependent-field="field-line" data-dependent-values="mk">
      <p>Maahanmuuttajakoulutukset ovat sellaisia opiskelijoita varten, joiden äidinkieli ei ole suomi ja jotka tarvitsevat peruskoulun päättötodistuksen jatko-opintoja varten.</p>
    </div>

  </section>