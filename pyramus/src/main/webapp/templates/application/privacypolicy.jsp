<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
  <meta charset="UTF-8" />
  </head>
  <body>
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
  </body>
</html>

