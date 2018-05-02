<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="form-section section-line">

  <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="nettipk">
    <div class="application-description__line-header">
      Olet hakemassa <span class="application-description__line-label--nettiperuskoulu">nettiperuskouluun</span>
    </div>
    <div class="application-description__line-content">Nettiperuskoulussa voit opiskella kesken jääneen peruskoulun loppuun tai tehdä koko aikuisten perusopetuksen oppimäärän alusta asti. Nettiperuskoulu on tarkoitettu yli 18-vuotiaille opiskelijoille, joilta puuttuu perusopetuksen päättötodistus.</div>
  </div>

  <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
    <div class="application-description__line-header">
      Olet hakemassa <span class="application-description__line-label--nettilukio">nettilukioon</span>
    </div>
    <div class="application-description__line-content">Nettilukiossa opiskelet koko aikuislukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Nettilukiossa voit myös tehdä loppuun aiemmin kesken jääneet lukio-opinnot. Nettilukio on tarkoitettu yli 18-vuotiaille opiskelijoille. Toisessa oppilaitoksessa opiskelevat ja yksittäisiä lukiokursseja suorittavat voivat ilmoittautua aineopiskelijaksi.</div>
  </div>

  <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu">
    <div class="application-description__line-header">
      Olet hakemassa <span class="application-description__line-label--aineopiskelu">aineopiskelijaksi</span>
    </div>
    <div class="application-description__line-content">Aineopiskelijana voit opiskella yksittäisiä lukion ja perusopetuksen kursseja. Aineopiskelijaksi voit ilmoittautua, vaikka opiskelisit samaan aikaan toisessa oppilaitoksessa.</div>
  </div>

  <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio">
    <div class="application-description__line-header">
      Olet hakemassa <span class="application-description__line-label--aikuislukio">aikuislukioon</span>
    </div>
    <div class="application-description__line-content">Aikuislukiossa opiskelet koko lukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Aikuislukiossa voit myös tehdä loppuun aiemmin kesken jääneet toisessa päivä- tai aikuislukiossa aloittamasi lukio-opinnot.</div>
  </div>

  <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="mk">
    <div class="application-description__line-header">
      Olet hakemassa <span class="application-description__line-label--maahanmuuttajakoulutus">maahanmuuttajakoulutukseen</span>
    </div>
    <div class="application-description__line-content">Maahanmuuttajakoulutukset ovat sellaisia opiskelijoita varten, joiden äidinkieli ei ole suomi ja jotka tarvitsevat peruskoulun päättötodistuksen jatko-opintoja varten.</div>
  </div>

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

</section>