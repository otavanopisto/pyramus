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

</section>