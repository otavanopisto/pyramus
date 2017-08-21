<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta charset="UTF-8"/>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/scripts/parsley/parsley.css"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>

    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/application/application.js"></script>
    
    <style>
    </style>

  </head>
  <body>
    <form class="application-form">
    
      <input type="hidden" name="field-application-id" value="${applicationId}" data-parsley-excluded="true"/>
      <input type="hidden" id="field-studyprogramme-id" name="field-studyprogramme-id" data-parsley-excluded="true"/>
      
      <section class="form-section section-line">

        <h3>Hakukohde</h3>

        <label for="field-line">Valitse linja</label>
        <select name="field-line" data-parsley-required="true" data-dependencies="true">
          <option value="">-- Valitse --</option>
          <option value="nettilukio" data-studyprogramme="6" data-local-line="true">Nettilukio</option>
          <option value="nettipk" data-studyprogramme="7" data-local-line="true">Nettiperuskoulu</option>
          <option value="lahilukio" data-studyprogramme="1" data-local-line="true">Lähilukio</option>
          <option value="bandilinja" data-studyprogramme="8" data-local-line="true">Bändilinja</option>
          <option value="mklinja" data-studyprogramme="15" data-local-line="false">Monikulttuurinen peruskoululinja</option>
          <option value="apa" data-studyprogramme="29" data-local-line="false">Aikuisten perusopetuksen alkuvaiheen opetus</option>
          <option value="luva" data-studyprogramme="27" data-local-line="false">LUVA eli lukioon valmentava koulutus maahanmuuttajille</option>
        </select>

      </section>
      
      <section class="form-section section-personal-info">

        <h3>Henkilötiedot</h3>
        
        <div class="field-container field-last-name">
          <label for="field-last-name">Sukunimi</label>
          <input type="text" name="field-last-name" data-parsley-required="true">
        </div>
        
        <div class="field-container field-first-names">
          <label for="field-first-names">Etunimet</label>
          <input type="text" name="field-first-names" data-parsley-required="true">
        </div> 
        
        <div class="field-container field-birthday">
          <label for="field-birthday">Syntymäaika</label>
          <input type="text" name="field-birthday" data-parsley-required="true" data-parsley-birthday-format="">
          <span class="field-help">Esitysmuoto p.k.vvvv (esim. 15.3.1995)</span>
        </div>

        <div class="field-container field-ssn-end">
          <label for="field-ssn-end">Henkilötunnuksen loppuosa</label>
          <input type="text" name="field-ssn-end" maxlength="4" style="text-transform:uppercase;" data-parsley-required="true" data-parsley-ssn-end-format="">
          <span class="field-help">Esitysmuoto XXXX (ilman välimerkkiä A tai -)</span>
        </div>

        <div class="field-container field-sex">
          <label for="field-sex">Sukupuoli</label>
          <select name="field-sex" data-parsley-required="true" data-dependencies="true">
            <option value="">-- Valitse --</option>
            <option value="mies">Mies</option>
            <option value="nainen">Nainen</option>
            <option value="muu">Muu</option>
          </select>
        </div>

        <div class="field-container field-sex-other dependent" data-dependent-field="field-sex" data-dependent-values="muu">
          <label for="field-sex-other">Muu sukupuolesi</label>
          <input type="text" name="field-sex-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>

        <div class="field-container field-street-address">
          <label for="field-street-address">Lähiosoite</label>
          <input type="text" name="field-street-address" data-parsley-required="true">
        </div> 

        <div class="field-container field-zip-code">
          <label for="field-zip-code">Postinumero</label>
          <input type="text" name="field-zip-code" data-parsley-required="true">
        </div> 

        <div class="field-container field-city">
          <label for="field-city">Postitoimipaikka</label>
          <input type="text" name="field-city" data-parsley-required="true">
        </div> 

        <div class="field-container field-country">
          <label for="field-country">Maa</label>
          <input type="text" name="field-country" value="Suomi" data-parsley-required="true">
        </div> 

        <div class="field-container field-municipality">
          <label for="field-municipality">Kotikunta</label>
          <select name="field-municipality" data-parsley-required="true" data-source="/1/application/municipalities">
            <option value="">-- Valitse --</option>
          </select>
        </div>

        <div class="field-container field-nationality">
          <label for="field-nationality">Kansalaisuus</label>
          <select name="field-nationality" data-parsley-required="true" data-source="/1/application/nationalities" data-preselect="Suomi">
            <option value="">-- Valitse --</option>
          </select>
        </div>

        <div class="field-container field-language">
          <label for="field-language">Äidinkieli</label>
          <select name="field-language" data-parsley-required="true" data-source="/1/application/languages" data-preselect="suomi">
            <option value="">-- Valitse --</option>
          </select>
        </div>

        <div class="field-container field-phone">
          <label for="field-phone">Puhelinnumero</label>
          <input type="text" name="field-phone" data-parsley-required="true">
          <span class="field-help">Laita mukaan myös maakoodi, jos olet ulkomailla</span>
        </div>

        <div class="field-container field-email">
          <label for="field-email">Sähköpostiosoite</label>
          <input type="email" name="field-email" data-parsley-required="true">
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
          <label for="field-underage-last-name">Sukunimi</label>
          <input type="text" name="field-underage-last-name" data-parsley-required="true">
        </div> 

        <div class="field-container field-underage-first-name">
          <label for="field-underage-first-name">Sukunimi</label>
          <input type="text" name="field-underage-first-name" data-parsley-required="true">
        </div> 
        
        <div class="field-container field-underage-phone">
          <label for="field-underage-phone">Puhelinnumero</label>
          <input type="text" name="field-underage-phone" data-parsley-required="true">
        </div> 

        <div class="field-container field-underage-email">
          <label for="field-underage-email">Sähköpostiosoite</label>
          <input type="text" name="field-underage-email">
        </div> 
        
        <div class="field-container field-underage-street-address">
          <label for="field-underage-street-address">Lähiosoite</label>
          <input type="text" name="field-underage-street-address" data-parsley-required="true">
        </div> 

        <div class="field-container field-zip-code">
          <label for="field-underage-zip-code">Postinumero</label>
          <input type="text" name="field-underage-zip-code" data-parsley-required="true">
        </div> 

        <div class="field-container field-underage-city">
          <label for="field-underage-city">Postitoimipaikka</label>
          <input type="text" name="field-underage-city" data-parsley-required="true">
        </div> 

        <div class="field-container field-underage-country">
          <label for="field-underage-country">Maa</label>
          <input type="text" name="field-underage-country" value="Suomi" data-parsley-required="true">
        </div> 
        
      </section>

      <section class="form-section section-additional">

        <h3>Hakemiseen tarvittavat lisätiedot</h3>

        <div class="field-container field-previous-studies dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,lahilukio,bandilinja">
          <label for="field-previous-studies">Aiemmat opinnot</label>
          <select name="field-previous-studies" data-parsley-required="true" data-dependencies="true">
            <option value="">-- Valitse --</option>
            <option value="peruskoulu">Peruskoulu</option>
            <option value="kansakoulu">Kansakoulu</option>
            <option value="lukio">Lukio</option>
            <option value="perusopetus">Aikuisten perusopetus</option>
            <option value="ammatillinen">Ammatillinen 2. aste</option>
            <option value="korkeakoulu">Korkeakoulu</option>
            <option value="muu">Muu</option>
          </select>
        </div>

        <div class="field-container field-previous-studies-other dependent" data-dependent-field="field-previous-studies" data-dependent-values="muu">
          <label for="field-previous-studies-other">Kerro tarkemmin</label>
          <input type="text" name="field-previous-studies-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>

        <div class="field-container field-other-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,lahilukio">
          <label for="field-other-school">Opiskelen tällä hetkellä toisessa oppilaitoksessa</label>
          <select name="field-other-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
            <option value="">-- Valitse --</option>
            <option value="kylla">Kyllä</option>
            <option value="en">En</option>
          </select>
        </div>

        <div class="field-container field-other-school-name dependent" data-dependent-field="field-other-school" data-dependent-values="kylla">
          <label for="field-other-school-name">Missä oppilaitoksessa olet</label>
          <input type="text" name="field-other-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>

        <div class="field-container field-goals dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,lahilukio">
          <label for="field-goals">Opiskelutavoitteet</label>
          <select name="field-goals" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
            <option value="">-- Valitse --</option>
            <option value="lukio">Lukion päättötodistus</option>
            <option value="yo">YO-tutkinto</option>
            <option value="molemmat">Molemmat</option>
          </select>
        </div>

        <div class="field-container field-previous-foreign-studies dependent" data-dependent-field="field-line" data-dependent-values="mklinja">
          <label for="field-previous-foreign-studies">Aikaisemmat opinnot kotimaassasi ja Suomessa</label>
          <textarea name="field-previous-foreign-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
        </div>

        <div class="field-container field-job">
          <label for="field-job">Olen tällä hetkellä</label>
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
          <label for="field-job-other">Kerro tarkemmin</label>
          <input type="text" name="field-job-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>

        <div class="field-container field-residence-permit dependent" data-dependent-field="field-line" data-dependent-values="mklinja,apa,luva">
          <label for="field-residence-permit">Onko sinulla oleskelulupa Suomeen?</label>
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
          <label for="field-lodging">Tarvitsen asunnon opiston kampukselta</label>
          <input type="checkbox" name="field-lodging" value="kylla">
        </div>

      </section>

      <section class="form-section section-attachments">
        
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
            <input type="file" name="field-attachments" multiple="true" style="display:none;">
            <div class="field-attachments-selector">Lisää liitteitä klikkaamalla tästä tai raahaamalla niitä tähän laatikkoon (TODO raahaus)</div>
            <div class="field-attachments-files"></div>
          </div>
        </div>

      </section>

      <section class="form-section section-source">

        <h3>Mistä sait tiedon koulutuksesta</h3>
      
        <div class="field-container field-source">
          <label for="field-source">Valitse vähintään yksi</label><br/>
          <input type="checkbox" name="field-source" value="tuttu">Ennestään tuttu<br/>
          <input type="checkbox" name="field-source" value="google">Google<br/>
          <input type="checkbox" name="field-source" value="facebook">Facebook<br/>
          <input type="checkbox" name="field-source" value="instagram">Instagram<br/>
          <input type="checkbox" name="field-source" value="sanomalehti">Sanomalehti<br/>
          <input type="checkbox" name="field-source" value="tienvarsimainos">Tienvarsimainos<br/>
          <input type="checkbox" name="field-source" value="valotaulumainos">Valotaulumainos<br/>
          <input type="checkbox" name="field-source" value="elokuva">Elokuva- tai TV-mainos<br/>
          <input type="checkbox" name="field-source" value="radio">Radio<br/>
          <input type="checkbox" name="field-source" value="tuttava">Kuulin kaverilta, tuttavalta, tms.<br/>
          <input type="checkbox" name="field-source" value="te-toimisto">TE-toimisto<br/>
          <input type="checkbox" name="field-source" value="messut">Messut<br/>
          <input type="checkbox" name="field-source" value="nuorisotyo">Nuorisotyö<br/>
          <input type="checkbox" name="field-source" value="opot">Opot<br/>
          <input type="checkbox" name="field-source" value="muu" data-dependencies="true">Muu<br/>
        </div>

        <div class="field-container field-source-other dependent" data-dependent-field="field-source" data-dependent-values="muu">
          <label for="field-source-other">Kerro tarkemmin mistä</label>
          <input type="text" name="field-source-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
      
      </section>

      <section class="form-section section-summary">
        <div>Yhteenveto ja tietosuojaseloste?</div>
      </section>

      <section class="form-section section-done">
        <div>Kiitokset, muokkausohjeet</div>
      </section>

      <nav class="form-navigation">
        <div>TODO Progressbar osioiden mukaan?</div>
        <button type="button" class="previous btn btn-info pull-left">Edellinen</button>
        <button type="button" class="next btn btn-info pull-right">Seuraava</button>
        <span class="clearfix"></span>
      </nav>    

      <div>
        <button type="button" class="button-validate">Validoi</button>
        <button type="button" class="button-submit">Lähetä</button>
      </div>
 
    </form>

    <div class="application-file template" style="display:none;">
      <div class="application-file-details">
        <span class="applicaton-file-name"><a class="application-file-link" target="_blank"></a></span>
        <span class="applicaton-file-size"></span>
        <span class="applicaton-file-delete"></span>
      </div>
      <div class="application-file-progress"></div>
    </div>

  </body>
</html>

