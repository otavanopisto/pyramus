<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/scripts/parsley/parsley.css"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>

    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/notificationqueue/notificationqueue.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/application/application.js"></script>

  </head>
  <body>
    <div class="notification-queue">
      <div class="notification-queue-items">
      </div>
    </div>
    <main>
	    <form class="application-form">
	    
        <input type="hidden" id="field-application-id" name="field-application-id" value="${applicationId}" data-preload="${preload}" data-parsley-excluded="true"/>
        <input type="hidden" id="field-studyprogramme-id" name="field-studyprogramme-id" data-parsley-excluded="true"/>
	      
	      <header class="application-logo-header"></header>
	      
	      <section class="form-section section-line">
	
	        <h3>Hakukohde</h3>
	
	        <label for="field-line" class="required">Valitse linja</label>
	        <select id="field-line" name="field-line" data-parsley-required="true" data-dependencies="true">
            <option value="">-- Valitse --</option>
            <option value="internetix" data-studyprogramme="13" data-underage-support="true" data-attachment-support="false">Aineopiskelu</option>
            <option value="nettilukio" data-studyprogramme="6" data-underage-support="true" data-attachment-support="true">Nettilukio</option>
            <option value="nettipk" data-studyprogramme="7" data-underage-support="true" data-attachment-support="true">Nettiperuskoulu</option>
            <option value="lahilukio" data-studyprogramme="1" data-underage-support="true" data-attachment-support="true">Lähilukio</option>
            <option value="bandilinja" data-studyprogramme="8" data-underage-support="true" data-attachment-support="true">Bändilinja</option>
            <option value="mklinja" data-studyprogramme="15" data-underage-support="false" data-attachment-support="false">Monikulttuurinen peruskoululinja</option>
            <option value="apa" data-studyprogramme="29" data-underage-support="false" data-attachment-support="false">Aikuisten perusopetuksen alkuvaiheen opetus</option>
            <option value="luva" data-studyprogramme="27" data-underage-support="false" data-attachment-support="false">LUVA eli lukioon valmentava koulutus maahanmuuttajille</option>
	        </select>

          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="internetix">
            <div>Aineopiskelun esittelyteksti</div>
          </div>

          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
            <div>Nettilukion esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="nettipk">
            <div>Nettiperuskoulun esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="lahilukio">
            <div>Lähilukion esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="bandilinja">
            <div>Bändilinjan esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="mklinja">
            <div>MK-linjan esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="apa">
            <div>APA-esittelyteksti</div>
          </div>
          
          <div class="field-container field-nettilukio-intro dependent" data-dependent-field="field-line" data-dependent-values="luva">
            <div>LUVA-esittelyteksti</div>
          </div>
	
	      </section>
        
        <section class="form-section section-internetix-school" data-skip="true">
          <h3>Oppilaitostiedot</h3>
          <p>Kurssimateriaalien käyttäminen itseopiskelussa on ilmaista. Voit siis rekisteröityä Muikun käyttäjäksi ja ilmoittautua kursseille, vaikka et haluaisikaan niistä arviointia tai kurssisuoritusta. Jos haluat, että opettaja arvioi kurssisuorituksesi, se on joissakin tapauksissa maksullista. <a href="http://opinnot.internetix.fi/fi/structure/kansio/ohje_arviointimaksuista" target="_blank">Lue lisää.</a></p>
          <p><b>Huom!</b> Oppilaitostieto tarkistetaan vielä jälkikäteen ja lähetämme laskun kurssin suorittamisesta, mikäli olet opiskelijana jossain toisen asteen oppilaitoksessa.</p>

          <div class="field-container field-internetix-school">
            <label for="field-internetix-school" class="required">Opiskelu muussa oppilaitoksessa</label>
            <select name="field-internetix-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
              <option value="">-- Valitse --</option>
              <option value="en">En opiskele missään oppilaitoksessa</option>
              <option value="kylla">Opiskelen toisessa oppilaitoksessa</option>
            </select>
          </div>

          <div class="field-container field-internetix-school-info dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla">
            <p>Alla olevassa alaspudotusvalikossa ovat ne oppilaitokset, joiden kanssa olemme tehneet sopimuksen. Jos et löydä omaa oppilaitostasi listasta, valitse kohta <i>Muu oppilaitos</i>. Valitse myös, mitä tutkintoa suoritat tällä hetkellä. Jos et suorita mitään tutkintoa tai tutkintoasi ei ole listassa, valitse kohta <i>Muu tutkinto</i>.</p>
          </div>

          <div class="field-container field-internetix-contract-school dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla">
            <label for="field-internetix-contract-school" class="required">Oppilaitokseni</label>
            <select name="field-internetix-contract-school" data-source="/1/application/contractschools" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
              <option value="">-- Valitse --</option>
              <option value="muu">Muu oppilaitos</option>
            </select>
          </div>

          <div class="field-container field-internetix-contract-school-name dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu">
            <label for="field-internetix-contract-school-name" class="required">Oppilaitos</label>
            <input type="text" name="field-internetix-contract-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
          </div>

          <div class="field-container field-internetix-contract-school-municipality dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu">
            <label for="field-internetix-contract-school-municipality" class="required">Opiskelupaikkakunta</label>
            <input type="text" name="field-internetix-contract-school-municipality" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
          </div>

          <div class="field-container field-internetix-contract-school-contact dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu">
            <label for="field-internetix-contract-school-contact" class="required">Oppilaitoksen yhteyshenkilö ja yhteystiedot</label>
            <textarea name="field-internetix-contract-school-contact" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
          </div>

          <div class="field-container field-internetix-contract-school-degree dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla">
            <label for="field-internetix-contract-school-degree" class="required">Suoritan</label>
            <select name="field-internetix-contract-school-degree" data-source="/1/application/contractschools" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
              <option value="">-- Valitse --</option>
              <option value="muu">Muu tutkinto</option>
              <option value="ammatillinen-perus">Ammatillinen perustutkinto</option>
              <option value="ammatillinen-korkea">Ammattikorkeakoulututkinto</option>
              <option value="kaksoistutkinto">Kaksoistutkinto</option>
              <option value="yo-tutkinto">YO-tutkinto / lukion oppimäärä</option>
              <option value="oppisopimus">Oppisopimuskoulutus</option>
            </select>
          </div>

        </section>
	      
	      <section class="form-section section-personal-info">
	
	        <h3>Henkilötiedot</h3>
	        
	        <div class="field-container field-last-name">
	          <label for="field-last-name" class="required">Sukunimi</label>
	          <input type="text" id="field-last-name" name="field-last-name" data-parsley-required="true">
	        </div>
	        
	        <div class="field-container field-first-names">
	          <label for="field-first-names" class="required">Etunimet</label>
	          <input type="text" id="field-first-names" name="field-first-names" data-parsley-required="true">
	        </div> 
	        
	        <div class="field-container field-birthday">
	          <label for="field-birthday" class="required">Syntymäaika</label>
	          <input type="text" id="field-birthday" name="field-birthday" data-parsley-required="true" data-parsley-birthday-format="">
	          <span class="field-help">Esitysmuoto p.k.vvvv (esim. 15.3.1995)</span>
	        </div>
	
	        <div class="field-container field-ssn-end">
	          <label for="field-ssn-end" class="required">Henkilötunnuksen loppuosa</label>
	          <input type="text" name="field-ssn-end" maxlength="4" style="text-transform:uppercase;" data-parsley-required="true" data-parsley-ssn-end-format="">
	          <span class="field-help">Esitysmuoto XXXX (ilman välimerkkiä A tai -)</span>
	        </div>
	
	        <div class="field-container field-sex">
	          <label for="field-sex" class="required">Sukupuoli</label>
	          <select name="field-sex" data-parsley-required="true" data-dependencies="true">
	            <option value="">-- Valitse --</option>
	            <option value="mies">Mies</option>
	            <option value="nainen">Nainen</option>
	            <option value="muu">Muu</option>
	          </select>
	        </div>
	
	        <div class="field-container field-sex-other dependent" data-dependent-field="field-sex" data-dependent-values="muu">
	          <label for="field-sex-other" class="required">Muu sukupuolesi</label>
	          <input type="text" name="field-sex-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div>
	
	        <div class="field-container field-street-address">
	          <label for="field-street-address" class="required">Lähiosoite</label>
	          <input type="text" name="field-street-address" data-parsley-required="true">
	        </div> 
	
	        <div class="field-container field-zip-code">
	          <label for="field-zip-code" class="required">Postinumero</label>
	          <input type="text" name="field-zip-code" data-parsley-required="true">
	        </div> 
	
	        <div class="field-container field-city">
	          <label for="field-city" class="required">Postitoimipaikka</label>
	          <input type="text" name="field-city" data-parsley-required="true">
	        </div> 
	
	        <div class="field-container field-country">
	          <label for="field-country" class="required">Maa</label>
	          <input type="text" name="field-country" value="Suomi" data-parsley-required="true">
	        </div> 
	
	        <div class="field-container field-municipality">
	          <label for="field-municipality" class="required">Kotikunta</label>
	          <select name="field-municipality" data-parsley-required="true" data-source="/1/application/municipalities">
	            <option value="">-- Valitse --</option>
              <option value="none">Ei kotikuntaa Suomessa</option>
	          </select>
	        </div>
	
	        <div class="field-container field-nationality">
	          <label for="field-nationality" class="required">Kansalaisuus</label>
	          <select name="field-nationality" data-parsley-required="true" data-source="/1/application/nationalities" data-preselect="Suomi">
	            <option value="">-- Valitse --</option>
	          </select>
	        </div>
	
	        <div class="field-container field-language">
	          <label for="field-language" class="required">Äidinkieli</label>
	          <select name="field-language" data-parsley-required="true" data-source="/1/application/languages" data-preselect="suomi">
	            <option value="">-- Valitse --</option>
	          </select>
	        </div>
	
	        <div class="field-container field-phone">
	          <label for="field-phone" class="required">Puhelinnumero</label>
	          <input type="text" id="field-phone" name="field-phone" data-parsley-required="true">
	          <span class="field-help">Laita mukaan myös maakoodi, jos olet ulkomailla</span>
	        </div>
	
	        <div class="field-container field-email">
	          <label for="field-email" class="required">Sähköpostiosoite</label>
	          <input type="email" id="field-email" name="field-email" data-parsley-required="true">
	        </div>
	      
	      </section>
	
	      <section class="form-section section-underage" data-skip="true">
	      
	        <h3>Huoltajan tiedot ja alaikäisen hakemusperusteet</h3>
	
	        <div class="field-container field-underage-grounds">
	          <label for="field-underage-grounds">Alaikäisen hakemusperusteet</label>
	          <textarea name="field-underage-grounds" rows="5" cols="40"></textarea>
	        </div>
	        
	        <div>Huoltajan tiedot</div>
	        
	        <div class="field-container field-underage-last-name">
	          <label for="field-underage-last-name" class="required">Sukunimi</label>
	          <input type="text" name="field-underage-last-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	
	        <div class="field-container field-underage-first-name">
	          <label for="field-underage-first-name" class="required">Etunimi</label>
	          <input type="text" name="field-underage-first-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	        
	        <div class="field-container field-underage-phone">
	          <label for="field-underage-phone" class="required">Puhelinnumero</label>
	          <input type="text" name="field-underage-phone" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	
	        <div class="field-container field-underage-email">
	          <label for="field-underage-email">Sähköpostiosoite</label>
	          <input type="text" name="field-underage-email">
	        </div> 
	        
	        <div class="field-container field-underage-street-address">
	          <label for="field-underage-street-address" class="required">Lähiosoite</label>
	          <input type="text" name="field-underage-street-address" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	
	        <div class="field-container field-zip-code">
	          <label for="field-underage-zip-code" class="required">Postinumero</label>
	          <input type="text" name="field-underage-zip-code" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	
	        <div class="field-container field-underage-city">
	          <label for="field-underage-city" class="required">Postitoimipaikka</label>
	          <input type="text" name="field-underage-city" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	
	        <div class="field-container field-underage-country">
	          <label for="field-underage-country" class="required">Maa</label>
	          <input type="text" name="field-underage-country" value="Suomi" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div> 
	        
	      </section>
	
	      <section class="form-section section-additional">
	
	        <h3>Hakemiseen tarvittavat lisätiedot</h3>
	
	        <div class="field-container field-previous-studies dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,lahilukio,bandilinja,internetix">
	          <label for="field-previous-studies" class="required">Aiemmat opinnot</label>
	          <select name="field-previous-studies" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
	            <option value="">-- Valitse --</option>
	            <option value="peruskoulu-kesken">Peruskoulu (kesken)</option>
              <option value="peruskoulu">Peruskoulun päättötodistus</option>
	            <option value="kansakoulu">Kansakoulu</option>
              <option value="lukio-kesken">Lukio (kesken)</option>
	            <option value="lukio-yo">Ylioppilastutkinto</option>
              <option value="lukio-ei-yo">Lukion päättötodistus (ei ylioppilastutkintoa)</option>
	            <option value="perusopetus">Aikuisten perusopetus</option>
	            <option value="ammatillinen">Ammatillinen 2. aste</option>
	            <option value="korkeakoulu">Korkeakoulu</option>
	            <option value="muu">Muu</option>
	          </select>
	        </div>
	
	        <div class="field-container field-previous-studies-other dependent" data-dependent-field="field-previous-studies" data-dependent-values="muu">
	          <label for="field-previous-studies-other" class="required">Kerro tarkemmin</label>
	          <input type="text" name="field-previous-studies-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div>
	
	        <div class="field-container field-other-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,lahilukio">
	          <label for="field-other-school" class="required">Opiskelen tällä hetkellä toisessa oppilaitoksessa</label>
	          <select name="field-other-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
	            <option value="">-- Valitse --</option>
	            <option value="kylla">Kyllä</option>
	            <option value="en">En</option>
	          </select>
	        </div>
	
	        <div class="field-container field-other-school-name dependent" data-dependent-field="field-other-school" data-dependent-values="kylla">
	          <label for="field-other-school-name" class="required">Missä oppilaitoksessa olet</label>
	          <input type="text" name="field-other-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div>
	
	        <div class="field-container field-goals dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,lahilukio">
	          <label for="field-goals" class="required">Opiskelutavoitteet</label>
	          <select name="field-goals" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	            <option value="">-- Valitse --</option>
	            <option value="lukio">Lukion päättötodistus</option>
	            <option value="yo">YO-tutkinto</option>
	            <option value="molemmat">Molemmat</option>
	          </select>
	        </div>
	
	        <div class="field-container field-previous-foreign-studies dependent" data-dependent-field="field-line" data-dependent-values="mklinja">
	          <label for="field-previous-foreign-studies" class="required">Aikaisemmat opinnot kotimaassasi ja Suomessa</label>
	          <textarea name="field-previous-foreign-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
	        </div>
	
	        <div class="field-container field-job">
	          <label for="field-job" class="required">Olen tällä hetkellä</label>
	          <select name="field-job" data-parsley-required="true" data-dependencies="true">
	            <option value="">-- Valitse --</option>
	            <option value="tyollinen">Työllinen</option>
	            <option value="tyoton">Työtön</option>
	            <option value="opiskelija">Opiskelija</option>
	            <option value="elakelainen">Eläkeläinen</option>
	            <option value="muu">Muu</option>
	          </select>
	        </div>
	
	        <div class="field-container field-job-other dependent" data-dependent-field="field-job" data-dependent-values="muu">
	          <label for="field-job-other" class="required">Kerro tarkemmin</label>
	          <input type="text" name="field-job-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div>
	
	        <div class="field-container field-residence-permit dependent" data-dependent-field="field-line" data-dependent-values="mklinja,apa,luva">
	          <label for="field-residence-permit" class="required">Onko sinulla oleskelulupa Suomeen?</label>
	          <select name="field-residence-permit" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	            <option value="">-- Valitse --</option>
	            <option value="kylla">Kyllä</option>
	            <option value="ei">Ei</option>
	          </select>
	        </div>
	
	        <div class="field-container field-info">
	          <label for="field-info">Haluan kertoa itsestäni ja opiskelutavoitteistani seuraavaa</label>
	          <textarea name="field-info" rows="5" cols="40"></textarea>
	        </div>
	
	        <div class="field-container field-lodging dependent" data-dependent-field="field-line" data-dependent-values="lahilukio,mklinja,apa,luva">
            <div class="field-row-flex">
              <div class="field-row-element">
                <input type="checkbox" id="field-lodging" name="field-lodging" value="kylla">
              </div>
              <div class="field-row-label">
                <label for="field-lodging">Tarvitsen asunnon opiston kampukselta</label>
              </div>
            </div>
	        </div>
	
	      </section>
	
	      <section class="form-section section-attachments" data-skip="true">
	        
	        <h3>Hakemuksen liitteet</h3>
	
	        <div class="field-container field-nettilukio-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
	          <div>Voit liittää tähän todistusjäljennökset sähköisesti. Voit toimittaa todistusjäljennökset myös sähköpostin liitetiedostoina eeva.lehikoinen@otavanopisto.fi tai postitse (Otavan Opisto / nettilukio, Otavantie 2 B, 50670, Otava)</div>
	        </div>
	
	        <div class="field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettipk">
	          <div>Voit liittää tähän todistusjäljennökset sähköisesti. Voit toimittaa todistusjäljennökset myös sähköpostin liitetiedostona elise.hokkanen@otavanopisto.fi tai postitse (Otavan Opisto / nettiperuskoulu, Otavantie 2 B, 50670, Otava)</div>
	        </div>
	
	        <div class="field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="lahilukio">
	          <div>Voit liittää tähän todistusjäljennökset sähköisesti. Voit toimittaa todistusjäljennökset myös sähköpostin liitetiedostona petri.louhivuori@otavanopisto.fi tai postitse (Otavan Opisto / nettilukio, Otavantie 2 B, 50670, Otava)</div>
	        </div>
	
	        <div class="field-container field-bandilinja-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="bandilinja">
	          <div>Voit liittää tähän musiikkinäytteitäsi sähköisesti. Voit toimittaa musiikkinäytteet myös sähköpostin liitetiedostona osoitteeseen jukka.tikkanen@otavanopisto.fi.</div>
	        </div>
	
	        <div class="field-container field-attachments">
	          <div class="field-attachments-uploader">
	            <div class="field-attachments-selector-container">
		            <input type="file" id="field-attachments" name="field-attachments" multiple="true" class="field-attachments-selector">
		            <div class="file-upload-description">Lisää liitteitä klikkaamalla tästä tai raahaamalla niitä tähän laatikkoon. Voit lisätä korkeintaan viisi liitettä, joiden yhteiskoon on oltava alle 10 MB</div>
	            </div>
	            <div id="field-attachments-files" class="field-attachments-files"></div>
	          </div>
	        </div>
	
	      </section>
	
	      <section class="form-section section-source">
	
	        <h3>Mistä sait tiedon koulutuksesta</h3>
	      
	        <div class="field-container field-source">
	          <div class="field-row-flex">
	            <div class="field-row-element">
	              <input id="known" type="checkbox" name="field-source" value="tuttu">
	            </div>
	            <div class="field-row-label">
	              <label for="known">Ennestään tuttu</label>
	            </div>
	          </div>
	          <div class="field-row-flex">
	            <div class="field-row-element">
	              <input id="google" type="checkbox" name="field-source" value="google">
	            </div>
	            <div class="field-row-label">
	              <label for="google">Google</label>
	            </div>
            </div>
	          <div class="field-row-flex">
	            <div class="field-row-element">
                <input id="facebook" type="checkbox" name="field-source" value="facebook">
              </div>
              <div class="field-row-label">
                <label for="facebook">Facebook</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="instagram" type="checkbox" name="field-source" value="instagram">
              </div>
              <div class="field-row-label">
                <label for="instagram">Instagram</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="sanomalehti" type="checkbox" name="field-source" value="sanomalehti">
              </div>
              <div class="field-row-label">
                <label for="sanomalehti">Sanomalehti</label>
              </div>
            </div>
	          <div class="field-row-flex">
	            <div class="field-row-element">
                <input id="tienvarsimainos" type="checkbox" name="field-source" value="tienvarsimainos">
              </div>
              <div class="field-row-label">
                <label for="tienvarsimainos">Tienvarsimainos</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="valotaulumainos" type="checkbox" name="field-source" value="valotaulumainos">
              </div>
              <div class="field-row-label">
                <label for="valotaulumainos">Valotaulumainos</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="elokuva" type="checkbox" name="field-source" value="elokuva">
              </div>
              <div class="field-row-label">
                <label for="elokuva">Elokuva- tai TV-mainos</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="radio" type="checkbox" name="field-source" value="radio">
              </div>
              <div class="field-row-label">
                <label for="radio">Radio</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="tuttava" type="checkbox" name="field-source" value="tuttava">
              </div>
              <div class="field-row-label">
                <label for="tuttava">Kuulin kaverilta, tuttavalta, tms.</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="te-toimisto" type="checkbox" name="field-source" value="te-toimisto">
              </div>
              <div class="field-row-label">
                <label for="te-toimisto">TE-toimisto</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="messut" type="checkbox" name="field-source" value="messut">
              </div>
              <div class="field-row-label">
                <label for="messut">Messut.</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="nuorisotyo" type="checkbox" name="field-source" value="nuorisotyo">
              </div>
              <div class="field-row-label">
                <label for="nuorisotyo">Nuorisotyö</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="opot" type="checkbox" name="field-source" value="opot">
              </div>
              <div class="field-row-label">
                <label for="opot">Opot</label>
              </div>
            </div>
            <div class="field-row-flex">
              <div class="field-row-element">
                <input id="muu" type="checkbox" name="field-source" value="muu" data-dependencies="true">
              </div>
              <div class="field-row-label">
                <label for="muu">Muu</label>
              </div>
            </div>
	        </div>
	
	        <div class="field-container field-source-other dependent" data-dependent-field="field-source" data-dependent-values="muu">
	          <label for="field-source-other" class="required">Kerro tarkemmin mistä</label>
	          <input type="text" name="field-source-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
	        </div>
	      
	      </section>
	
	      <section class="form-section section-summary">
          <h3>Tarkista vielä yhteystietosi mahdollisia yhteydenottoja varten</h3>

          <div class="summary-container">
            <label for="summary-name">Nimi</label>
            <div id="summary-name"></div>
          </div>
          <div class="summary-container">
            <label for="summary-phone">Puhelinnumero</label>
            <div id="summary-phone"></div>
          </div>
          <div class="summary-container">
            <label for="summary-email">Sähköpostiosoite</label>
            <div id="summary-email"></div>
          </div>

          <div class="summary-container field-privacy">
	          <div class="field-row-flex">
		          <div class="field-row-element">
		            <input type="checkbox" id="field-privacy" name="field-privacy" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
		          </div>
		          <div class="field-row-label">
		            <label for="field-privacy">Olen lukenut <a href="#" class="summary-privacy-link">tietosuojaselosteen</a> ja hyväksyn, että tietoni tallennetaan Otavan Opiston oppilashallintojärjestelmään sekä verkko-oppimisympäristöön. </label>
		          </div>
		        </div>
	        </div>
	        
	        <div class="privacy-policy-overlay" style="display: none;">
	        </div>
          <div class="privacy-policy-container" style="display: none;">
            <div class="privacy-policy-close"></div>
	          <h3>1. Rekisterin pitäjä</h3>
				    <p>
				      Otavan Opisto<br> Mikkelin kaupunki
				    </p>
				    <p>y-tunnus: 0165116-3</p>
				    <h3>2. Yhteyshenkilö</h3>
				    <p>
				      Riitta Kesonen<br> Otavantie 2 b<br> 50670 OTAVA<br> puh. 044 794 3552
				    </p>
				    <h3>3. Rekisterin nimi</h3>
				    <p>Pyramus -oppilashallintojärjestelmä</p>
				    <h3>4. Henkilötietojen käsittelyn tarkoitus</h3>
				    <p>Opetuksen ja koulutuksen järjestäminen</p>
				    <h3>5. Rekisterin tietosisältö</h3>
				    <ul>
				      <li>opiskelija nimi</li>
				      <li>henkilötunnus</li>
				      <li>valokuva</li>
				      <li>kansalaisuus</li>
				      <li>sukupuoli</li>
				      <li>äidinkieli</li>
				      <li>kotikunta ja tarpeelliset yhteystiedot (kotiosoite, puhelinnumero, sähköpostiosoite)&nbsp;</li>
				      <li>opiskelijan aikaisempaa koulutusta ja koulumenestystä koskevat tiedot</li>
				      <li>koulutukseen hakemista, hyväksymistä ja opiskelijaksi ottamista koskevat tiedot</li>
				      <li>opiskelijaksi ottamiseen vaikuttavat tiedot työkokemuksesta&nbsp;</li>
				      <li>haastatteluissa ja muissa yhteydenotoissa kertyvä aineisto</li>
				      <li>viitetieto opiskelijan järjestelmässä tekemiin sähköisiin allekirjoituksiin</li>
				      <li>oppilaitoksessa opiskeluun, opintojen etenemiseen ja&nbsp; suoritteisiin liittyvät tiedot</li>
				      <li>ylioppilastutkintoa koskevat tiedot</li>
				    </ul>
				    <h3>6. Säännönmukaiset&nbsp; tietolähteet</h3>
				    <ul>
				      <li>Opiskelija täyttää haku-/ilmoittautumislomakkeelle tiedot itse</li>
				      <li>Osa rekisteriin talletetuista tiedoista on koottu opiskelijan kanssa käytyjen keskustelujen perusteella</li>
				      <li>Opettajat ja ohjaajat tallentavat arviointitiedot järjestelmään</li>
				      <li>Oppimisympäristössä toteutettavat kurssi-ilmoittautumiset, saadut arvosanat, sekä muut opiskeluun liittyvät opiskelijan tekemät
				        tapahtumat kirjautuvat rekisteriin automaattisesti</li>
				    </ul>
				    <h3>7. Säännönmukaiset tietojen luovutukset</h3>
				    <p>Opiskelijatietoja luovutetaan eteenpäin toisille viranomaisille tilastointi- ja rahoitusteknisistä syistä. Tietoja luovutetaan
				      seuraaville tahoille:</p>
				    <ul>
				      <li>Opetushallitus</li>
				      <li>Tilastokeskus</li>
				      <li>Kansaneläkelaitos</li>
				      <li>Mikkelin kaupungin opetustoimi</li>
				      <li>Ylioppilastutkintolautakunta</li>
				    </ul>
				    <p>Opiskelijan yhteystiedot luovutetaan nuorisolain edellyttämissä tapauksissa opiskelijan kotikunnan etsivälle nuorisotyölle.</p>
				    <p>Lisäksi abiturienttien nimet, osoite ja oppilaitoksen nimi luovutetaan Akateemiselle kustannusliikkeelle Spes Patriae
				      -ylioppilaskuvastoa varten.</p>
				    <h3>8. Rekisterin suojauksen periaatteet</h3>
				    <p>Henkilötiedot suojataan asiattomalta pääsyltä ja laittomalta käsittelyltä (esim. hävittäminen, muuttaminen tai luovuttaminen).
				      Salassa pidettävien ja arkaluonteisten tietojen suojaamiseen kiinnitetään erityistä huomiota.</p>
				    <p>Suojaus perustuu järjestelmätasolla valvottujen käyttäjäoikeuksien käyttäjätileihin. Tietokanta varmuuskopioidaan
				      maantieteellisesti erillään sijaitsevaan konesaliin kiintolevypohjaiseen varmistusjärjestelmään. Levytallennusjärjestelmästä
				      tallennetaan tiedot vielä varmuuskopiointinauhoille kolmanteen, edelleen maantieteellisesti erillään sijaitsevaan paikkaan.
				      Varmistusnauha-asema sijaitsee lukitussa murtovalvonnan alaisena olevassa tilassa. Nauhat säilytetään kassakaapissa.</p>
				    <h3>9. Rekisteröidyn tarkastusoikeus&nbsp;</h3>
				    <p>Rekisteröidyllä on oikeus tarkastaa itseään koskevat rekisterin tiedot.</p>
				    <p>Tarkastuspyyntö tehdään henkilökohtaisen käynnin yhteydessä tai omakätisesti allekirjoitetulla tai muulla luotettavalla tavalla
				      varmennetulla asiakirjalla. Tarkastuspyyntö kohdistetaan rekisterin yhteyshenkilölle.</p>
				    <h3>10. Tiedon korjaaminen</h3>
				    <p>Rekisterissä olevien virheellisten, puutteellisten tai vanhentuneiden henkilötietojen korjauspyynnöt voidaan osoittaa rekisterin
				      yhteyshenkilölle. Henkilöllisyytensä varmistaneen henkilön vaatimat rekisteriä koskevat korjaukset ja muut muutokset tehdään viipymättä.</p>
				    <p>Jollei muutospyyntöä katsota perustelluksi, annetaan muutospyynnön esittäjälle kirjallinen todistus, jossa selvitetään syyt miksi
				      muutosvaatimusta ei olla hyväksytty. Rekisteröity voi saattaa asian tietosuojavaltuutetun käsiteltäväksi.</p>
          </div>
	      </section>
	      
	      <section class="form-section section-done" data-skip="true">
	        <div>Hakemuksesi on vastaanotettu. Hakemustietojen muokkaaminen jälkikäteen on mahdollista osoitteessa</div>
          <div><a href="/application/edit.page">https://pyramus.otavanopisto.fi/application/edit.page</a></div>
          <div>Tarvitset seuraavat tiedot hakemuksen muokkaamiseen</div>
          <div>Sukunimi <span id="edit-info-last-name"></span></div>
          <div>Hakemustunnus <span id="edit-info-reference-code"></span></div>
          <div>Nämä ohjeet on lähetetty myös antamaasi sähköpostiosoitteeseen <span id="edit-info-email"></span></div>
	      </section>
	
	      <nav class="form-navigation" style="display:none;">
	        <button id="button-previous-section" type="button" class="button-previous-section previous btn btn-info pull-left">Edellinen</button>
	        <div id="application-page-indicator" class="application-page-indicator"></div>
	        <button id="button-next-section" type="button" class="button-next-section next btn btn-info pull-right">Seuraava</button>
          <button id="button-save-application" type="button" class="button-save-application">Lähetä</button>
	      </nav>    
	
	    </form>
	
	    <div class="application-file template" style="display:none;">
	      <div class="application-file-details">
	        <span class="application-file-name"><a class="application-file-link" target="_blank"></a></span>
          <span class="application-file-size"></span>
	      </div>
	      <div class="application-file-delete"></div>
	    </div>
      
      <div class="application-file-upload-progress template" style="display:none;">
        <div class="application-file-upload-progress-text"></div>
        <div class="application-file-upload-progress-bar"></div>
      </div>
	    
    </main>

  </body>
</html>

