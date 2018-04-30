<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-done submitted" data-skip="true">
    <h3 class="form-section__header">Hakemuksesi on vastaanotettu</h3>
    <p>Hakemustietojen muokkaaminen jälkikäteen on mahdollista osoitteessa <a href="/applications/edit.page">https://pyramus.otavanopisto.fi/applications/edit.page</a></p>
    <p>Tarvitset seuraavat tiedot hakemuksen muokkaamiseen:</p>
    <p class="important"><span class="important__label">Sukunimi</span><span class="important__data" id="edit-info-last-name"></span></p>
    <p class="important"><span class="important__label">Hakemustunnus</span><span class="important__data" id="edit-info-reference-code"></span></p>
    <p>Uusien hakemuksien tapauksessa nämä ohjeet on lähetetty myös antamaasi sähköpostiosoitteeseen <span class="email" id="edit-info-email"></span></p>
  </section>

  <section class="form-section section-done internetix-submitted" data-skip="true">
    <h3 class="form-section__header">Hakemuksesi on tallennettu</h3>
    <p>Koska olet tietojemme mukaan opiskellut Otavan Opistossa aiemminkin, käsittelemme hakemuksesi manuaalisesti. Lähetämme sinulla sähköpostia antamaasi osoitteeseen, kun hakemus on käsitelty.</p>
  </section>

  <section class="form-section section-done registered" data-skip="true">
    <h3 class="form-section__header">Hakemuksesi on tallennettu</h3>
    <p>Lähetimme antamaasi sähköpostiosoitteeseen ohjeet, joilla voit luoda haluamasi tunnukset Otavan Opiston Muikku-oppimisympäristöön.</p>
  </section>