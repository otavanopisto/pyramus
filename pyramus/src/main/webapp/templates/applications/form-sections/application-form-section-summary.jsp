<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-summary" style="display:none;">

    <div class="application-line"></div>

    <h3 class="form-section__header">Tarkista vielä yhteystietosi</h3>

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
          <label for="field-privacy" class="required">Olen lukenut <a href="http://otavanopisto.fi/resources/public/tietosuojaselosteet/opiskelijarekisteri_tietosuojaseloste.pdf" target="top" class="summary-privacy-link">tietosuojaselosteen</a> ja hyväksyn, että tietoni tallennetaan Otavan Opiston oppilashallintojärjestelmään sekä verkko-oppimisympäristöön.</label>
        </div>
      </div>
    </div>

    <div class="summary-container field-changes">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-changes" name="field-changes" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-changes" class="required">Vakuutan, että antamani tiedot ovat oikein. Lupaan ilmoittaa, jos antamissani tiedoissa tapahtuu muutoksia.</label>
        </div>
      </div>
    </div>

    <div class="summary-container field-nettilukio-promise dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-nettilukio-promise" name="field-nettilukio-promise" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-nettilukio-promise" class="required">Ymmärrän, että verkko-oppimisympäristö Muikussa tekemäni itsenäiset verkkokurssisuoritukset ovat näyttöjä omasta osaamisestani ja sitoudun tekemään tehtävät itsenäisesti. Ymmärrän myös, että opiskeluoikeus edellyttää lukiolain nojalla opintojen säännöllistä etenemistä (Lukiolaki 714/2018, 24 § ja 25 §). <a href="#" class="nettilukio-promise-link">Opiskelun säännöt Nettilukiossa</a>.</label>
        </div>
      </div>
    </div>

    <div class="summary-container field-aineopiskelu-promise dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-aineopiskelu-promise" name="field-aineopiskelu-promise" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-aineopiskelu-promise" class="required">Ymmärrän, että verkko-oppimisympäristö Muikussa tekemäni itsenäiset verkkokurssisuoritukset ovat näyttöjä omasta osaamisestani, ja sitoudun tekemään tehtävät itsenäisesti. <a href="#" class="aineopiskelu-promise-link">Opiskelun säännöt Nettilukiossa</a>.</label>
        </div>
      </div>
    </div>

    <div class="summary-container field-nettipk-promise dependent" data-dependent-field="field-line" data-dependent-values="nettipk" style="display:none;">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-nettipk-promise" name="field-nettipk-promise" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-nettipk-promise" class="required">Ymmärrän, että verkko-oppimisympäristö Muikussa tekemäni itsenäiset verkkokurssisuoritukset ovat näyttöjä omasta osaamisestani, ja sitoudun tekemään tehtävät itsenäisesti. Ymmärrän myös, että opiskeluoikeus edellyttää opintojen säännöllistä etenemistä. <a href="#" class="nettipk-promise-link">Opiskelun säännöt Nettiperuskoulussa</a>.</label>
        </div>
      </div>
    </div>

    <div class="nettilukio-promise-overlay" style="display:none;">
    </div>
    <div class="nettilukio-promise" style="display:none;">
      <div class="nettilukio-promise__close"></div>
      <h3>Kurssin itsenäinen suorittaminen</h3>
      <p>Nettilukion kurssien arvioitavia tehtäviä tehdessä on tärkeä muistaa, että vastauksesi osoittavat juuri sinun henkilökohtaista osaamistasi: vastauksiin sisältyvät asiat, esimerkit, tekstin rakentaminen ovat näyttöjä siitä, miten sinä olet asian hahmottanut ja ymmärtänyt. Arvioitavat tehtävät muodostavat pohjan henkilökohtaisen kurssisuorituksesi arvioinnille.</p>
      <p>Arvioitavien tehtävien osalta yhteistyö toisen opiskelijan kanssa ei lähtökohtaisesti ole mahdollista, ellei opettaja ole erikseen tehtävänannossa näin ohjeistanut tai ellei opettajan kanssa sovita tästä ennen kurssin suorittamista.</p>
      <p>Mikäli sinulla kuitenkin olisi mahdollisuus opiskella verkkokursseja toisen opiskelijan kanssa yhdessä, se on tietyin reunaehdoin sallittua. Tällaisessa tilanteessa on tärkeää että ilmaiset tämän opettajalle jo kurssin alussa ja tuot tämän esiin esim. oppimispäiväkirjassa erittelemällä tekemistä siten, että sinun osuutesi oppimisesta tulee näkyviin. Arvioinnin kannalta oppimisprosessin läpinäkyvyys on tärkeää.</p>
      <p>Kurssin materiaalien läpikäyminen ja harjoitustehtävien tekeminen jonkun toisen tuella on mahdollista. Jos tarvitset apua arvioitavien tehtävien tekemisessä, käänny opettajan puoleen tai kysy apua oppiainekohtaisessa FB-ryhmässä. Siten opettajan on mahdollista nähdä, millaista apua olet saanut. HUOM! Avun pyytäminen ei vaikuta arvosanaasi alentavasti.</p>
      <p><b>Muista, että kurssilla annettavat näytöt osaamisesta (arvioitavat tehtävät, oppimispäiväkirja) ovat henkilökohtaisia!</b></p>
      <h3>Opintojen eteneminen</h3>
      <p>Tärkeää on, että opintosi meillä etenevät. Suositus on, että jätät ensimmäisen kurssin arvioitavaksi kolmen kuukauden kuluessa opintojesi aloituksesta ja vuosittain opintosi etenevät vähintään viiden kurssin verran. Opintojesi tulee mielellään edetä tasaisesti, niin ettei välillä ole useita kuukausia ilman kurssisuoritusta.</p>
      <p>Jos sinulle tulee jostakin syystä tilanne, ettet pysty opiskelemaan tai haluat erota nettilukiosta, ota heti yhteyttä omaan ohjaajaasi.</p>
    </div>

    <div class="aineopiskelu-promise-overlay" style="display:none;">
    </div>
    <div class="aineopiskelu-promise" style="display:none;">
      <div class="aineopiskelu-promise__close"></div>
      <h3>Kurssin itsenäinen suorittaminen</h3>
      <p>Nettilukion kurssien arvioitavia tehtäviä tehdessä on tärkeä muistaa, että vastauksesi osoittavat juuri sinun henkilökohtaista osaamistasi: vastauksiin sisältyvät asiat, esimerkit, tekstin rakentaminen ovat näyttöjä siitä, miten sinä olet asian hahmottanut ja ymmärtänyt. Arvioitavat tehtävät muodostavat pohjan henkilökohtaisen kurssisuorituksesi arvioinnille.</p>
      <p>Arvioitavien tehtävien osalta yhteistyö toisen opiskelijan kanssa ei lähtökohtaisesti ole mahdollista, ellei opettaja ole erikseen tehtävänannossa näin ohjeistanut tai ellei opettajan kanssa sovita tästä ennen kurssin suorittamista.</p>
      <p>Mikäli sinulla kuitenkin olisi mahdollisuus opiskella verkkokursseja toisen opiskelijan kanssa yhdessä, se on tietyin reunaehdoin sallittua. Tällaisessa tilanteessa on tärkeää että ilmaiset tämän opettajalle jo kurssin alussa JA tuot tämän esiin esim. oppimispäiväkirjassa erittelemällä tekemistä siten, että sinun osuutesi oppimisesta tulee näkyviin. Arvioinnin kannalta oppimisprosessin läpinäkyvyys on tärkeää.</p>
      <p>Kurssin materiaalien läpikäyminen ja harjoitustehtävien tekeminen jonkun toisen tuella on mahdollista. Jos tarvitset apua arvioitavien tehtävien tekemisessä, käänny opettajan puoleen tai kysy apua oppiainekohtaisessa FB-ryhmässä. Siten opettajan on mahdollista nähdä, millaista apua olet saanut. HUOM! Avun pyytäminen ei vaikuta arvosanaasi alentavasti.</p>
      <p><b>Muista, että kurssilla annettavat näytöt osaamisesta (arvioitavat tehtävät, oppimispäiväkirja) ovat henkilökohtaisia!</b></p>
    </div>

    <div class="nettipk-promise-overlay" style="display:none;">
    </div>
    <div class="nettipk-promise" style="display:none;">
      <div class="nettipk-promise__close"></div>
      <h3>Kurssin itsenäinen suorittaminen</h3>
      <p>Nettiperuskoulun kurssien arvioitavia tehtäviä tehdessä on tärkeä muistaa, että vastauksesi osoittavat juuri sinun henkilökohtaista osaamistasi: vastauksiin sisältyvät asiat, esimerkit, tekstin rakentaminen ovat näyttöjä siitä, miten sinä olet asian hahmottanut ja ymmärtänyt. Arvioitavat tehtävät muodostavat pohjan henkilökohtaisen kurssisuorituksesi arvioinnille.</p>
      <p>Arvioitavien tehtävien osalta yhteistyö toisen opiskelijan kanssa ei lähtökohtaisesti ole mahdollista, ellei opettaja ole erikseen tehtävänannossa näin ohjeistanut tai ellei opettajan kanssa sovita tästä ennen kurssin suorittamista.</p>
      <p>Mikäli sinulla kuitenkin olisi mahdollisuus opiskella verkkokursseja toisen opiskelijan kanssa yhdessä, se on tietyin reunaehdoin sallittua. Tällaisessa tilanteessa on tärkeää että ilmaiset tämän opettajalle jo kurssin alussa JA tuot tämän esiin esim. oppimispäiväkirjassa erittelemällä tekemistä siten, että sinun osuutesi oppimisesta tulee näkyviin. Arvioinnin kannalta oppimisprosessin läpinäkyvyys on tärkeää.</p>
      <p>Kurssin materiaalien läpikäyminen ja harjoitustehtävien tekeminen jonkun toisen tuella on mahdollista. Jos tarvitset apua arvioitavien tehtävien tekemisessä, käänny opettajan puoleen tai kysy apua oppiainekohtaisessa FB-ryhmässä. Siten opettajan on mahdollista nähdä, millaista apua olet saanut. HUOM! Avun pyytäminen ei vaikuta arvosanaasi alentavasti.</p>
      <p><b>Muista, että kurssilla annettavat näytöt osaamisesta (arvioitavat tehtävät, oppimispäiväkirja) ovat henkilökohtaisia!</b></p>
      <h3>Opintojen eteneminen</h3>
      <p>Tärkeää on, että opintosi meillä etenevät. Suositus on, että jätät ensimmäisen kurssin arvioitavaksi kolmen kuukauden kuluessa opintojesi aloituksesta ja vuosittain opintosi etenevät vähintään viiden kurssin verran. Opintojesi tulee mielellään edetä tasaisesti, niin ettei välillä ole useita kuukausia ilman kurssisuoritusta.</p>
      <p>Jos sinulle tulee jostakin syystä tilanne, ettet pysty opiskelemaan tai haluat erota nettiperuskoulusta, ota heti yhteyttä omaan ohjaajaasi.</p>
    </div>
  
  </section>