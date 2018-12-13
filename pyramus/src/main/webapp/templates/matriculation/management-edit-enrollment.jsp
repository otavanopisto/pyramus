<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>YO-ilmoittautumiset</title>

		<jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
		<jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
		<jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
		<jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
		<jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
		<jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
		<jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
		
		<script type="text/javascript">
		
			function onLoad(event) {
				var termOptions = [
					{value: "SPRING2016", text: "Kevät 2016"},
					{value: "AUTUMN2016", text: "Syksy 2016"},
					{value: "SPRING2017", text: "Kevät 2017"},
					{value: "AUTUMN2017", text: "Syksy 2017"},
					{value: "SPRING2018", text: "Kevät 2018"},
					{value: "AUTUMN2018", text: "Syksy 2018"},
					{value: "SPRING2019", text: "Kevät 2019"},
					{value: "AUTUMN2019", text: "Syksy 2019"},
					{value: "SPRING2020", text: "Kevät 2020"},
					{value: "AUTUMN2020", text: "Syksy 2020"},
					{value: "SPRING2021", text: "Kevät 2021"},
					{value: "AUTUMN2021", text: "Syksy 2021"}
				];
				var subjectOptions = [
					{value:"AI", text:"Äidinkieli"},
					{value:"S2", text:"Suomi toisena kielenä"},
					{value:"ENA", text:"Englanti, A-taso"},
					{value:"RAA", text:"Ranska, A-taso"},
					{value:"ESA", text:"Espanja, A-taso"},
					{value:"SAA", text:"Saksa, A-taso"},
					{value:"VEA", text:"Venäjä, A-taso"},
					{value:"UE", text:"Uskonto"},
					{value:"ET", text:"Elämänkatsomustieto"},
					{value:"YO", text:"Yhteiskuntaoppi"},
					{value:"KE", text:"Kemia"},
					{value:"GE", text:"Maantiede"},
					{value:"TT", text:"Terveystieto"},
					{value:"ENC", text:"Englanti, C-taso"},
					{value:"RAC", text:"Ranska, C-taso"},
					{value:"ESC", text:"Espanja, C-taso"},
					{value:"SAC", text:"Saksa, C-taso"},
					{value:"VEC", text:"Venäjä, C-taso"},
					{value:"ITC", text:"Italia, C-taso"},
					{value:"POC", text:"Portugali, C-taso"},
					{value:"LAC", text:"Latina, C-taso"},
					{value:"SMC", text:"Saame, C-taso"},
					{value:"RUA", text:"Ruotsi, A-taso"},
					{value:"RUB", text:"Ruotsi, B-taso"},
					{value:"PS", text:"Psykologia"},
					{value:"FI", text:"Filosofia"},
					{value:"HI", text:"Historia"},
					{value:"FY", text:"Fysiikka"},
					{value:"BI", text:"Biologia"},
					{value:"MAA", text:"Matematiikka, pitkä"},
					{value:"MAB", text:"Matematiikka, lyhyt"}
				];
				var mandatorityOptions = [
					{value: "MANDATORY", text: "Pakollinen"},
					{value: "OPTIONAL", text: "Valinnainen"}
				];
				var repeatOptions = [
					{value: "FIRST_TIME", text: "Ensimmäinen suorituskerta"},
					{value: "REPEAT", text: "Uusinta"}
				];
				var gradeOptions = [
					{value: "IMPROBATUR", text: "Improbatur"},
					{value: "APPROBATUR", text: "Approbatur"},
					{value: "LUBENTER_APPROBATUR", text: "Lubenter approbatur"},
					{value: "CUM_LAUDE_APPROBATUR", text: "Cum laude approbatur"},
					{value: "MAGNA_CUM_LAUDE_APPROBATUR", text: "Magna cum laude approbatur"},
					{value: "EXIMIA_CUM_LAUDE_APPROBATUR", text: "Eximia cum laude approbatur"},
					{value: "LAUDATUR", text: "Laudatur"},
					{value: "UNKNOWN", text: "Ei tiedossa"},
				];
				var tabControl = new IxProtoTabs($('tabs'));

				var enrolledAttendances = new IxTable($('enrolledAttendancesTableContainer'), {
					id: 'enrolledAttendances',
					columns : [{
						dataType: 'hidden',
						paramName: 'attendanceId'
					}, {
						header : 'Aine',
						left: 0,
						width: 300,
						dataType : 'select',
						editable: true,
						paramName: 'subject',
						options: subjectOptions
					}, {
						header : 'Pakollisuus',
						width: 300,
						left: 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'mandatority',
						options: mandatorityOptions
					}, {
						header : 'Uusiminen',
						width: 200,
						left: 8 + 300 + 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'repeat',
						options: repeatOptions
					}, {
						width : 22,
						right : 8,
						dataType : 'button',
						imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
						tooltip : 'Muokkaa',
						onclick : function(event) {
							enrolledAttendances.deleteRow(event.row);
						}
					}]
				});
				enrolledAttendances.addRows(JSON.parse(JSDATA["enrolledAttendances"]));
				document.getElementById("addEnrolledTableRow").addEventListener('click', function() {
					enrolledAttendances.addRow([-1, 'ENA', 'MANDATORY', 'FIRST_TIME', '']);
				});

				var finishedAttendances = new IxTable($('finishedAttendancesTableContainer'), {
					id: 'finishedAttendances',
					columns : [{
						dataType: 'hidden',
						paramName: 'attendanceId'
					}, {
						header : 'Ajankohta',
						left: 0,
						width: 300,
						dataType : 'select',
						editable: true,
						paramName: 'term',
						options: termOptions
					}, {
						header : 'Aine',
						width: 300,
						left: 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'subject',
						options: subjectOptions
					}, {
						header : 'Pakollisuus',
						width: 200,
						left: 8 + 300 + 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'mandatority',
						options: mandatorityOptions
					}, {
						header : 'Arvosana',
						width: 200,
						left: 8 + 300 + 8 + 300 + 8 + 200,
						dataType : 'select',
						editable: true,
						paramName: 'grade',
						options: gradeOptions
					}, {
						width : 22,
						right : 8,
						dataType : 'button',
						imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
						tooltip : 'Muokkaa',
						onclick : function(event) {
							finishedAttendances.deleteRow(event.row);
						}
					}]
				});
				finishedAttendances.addRows(JSON.parse(JSDATA["finishedAttendances"]));
				document.getElementById("addFinishedTableRow").addEventListener('click', function() {
					finishedAttendances.addRow([-1, 'SPRING2019', 'ENA', 'MANDATORY', 'IMPROBATUR', '']);
				});

				var plannedAttendances = new IxTable($('plannedAttendancesTableContainer'), {
					id: 'plannedAttendances',
					columns : [{
						dataType: 'hidden',
						paramName: 'attendanceId'
					}, {
						header : 'Ajankohta',
						left: 0,
						width: 300,
						dataType : 'select',
						editable: true,
						paramName: 'term',
						options: termOptions
					}, {
						header : 'Aine',
						width: 300,
						left: 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'subject',
						options: subjectOptions
					}, {
						header : 'Pakollisuus',
						width: 200,
						left: 8 + 300 + 8 + 300,
						dataType : 'select',
						editable: true,
						paramName: 'mandatority',
						options: mandatorityOptions
					}, {
						width : 22,
						right : 8,
						dataType : 'button',
						imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
						tooltip : 'Muokkaa',
						onclick : function(event) {
							plannedAttendances.deleteRow(event.row);
						}
					}]
				});
				plannedAttendances.addRows(JSON.parse(JSDATA["plannedAttendances"]));
				document.getElementById("addPlannedTableRow").addEventListener('click', function() {
					plannedAttendances.addRow([-1, 'SPRING2019', 'ENA', 'MANDATORY', '']);
				});
			};
		</script>
	</head> 
	<body onload="onLoad(event);">
		<jsp:include page="/templates/generic/header.jsp"></jsp:include>

		<h1 class="genericPageHeader">Muokkaa YO-ilmoittautumisia</h1>
		
		<div class="genericFormContainer">
			<div class="tabLabelsContainer" id="tabs">
				<a class="tabLabel" href="#enrollment">Ilmoittautuminen</a>
			</div>
			<div id="enrollment" class="tabContent">
				<form method="post">
					<div>
						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.name"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.name.help"/>
							</jsp:include>						
							<input type="text" name="name" value="${name}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.ssn"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.ssn.help"/>
							</jsp:include>						
							<input type="text" name="ssn" value="${ssn}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.email"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.email.help"/>
							</jsp:include>						
							<input type="text" name="email" value="${email}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.phone"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.phone.help"/>
							</jsp:include>						
							<input type="text" name="phone" value="${phone}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.address"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.address.help"/>
							</jsp:include>						
							<input type="text" name="address" value="${address}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.postalCode"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.postalCode.help"/>
							</jsp:include>						
							<input type="text" name="postalCode" value="${postalCode}">
						</div>

						<div class="genericFormSection">	
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.postalOffice"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.postalOffice.help"/>
							</jsp:include>						
							<input type="text" name="postalOffice" value="${postalOffice}">
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.nationalStudentNumber"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.nationalStudentNumber.help"/>
							</jsp:include>						
							<input type="text" name="nationalStudentNumber" value="${nationalStudentNumber}">
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.guidanceCounselor"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.guidanceCounselor.help"/>
							</jsp:include>						
							<input type="text" name="guidanceCounselor" value="${guidanceCounselor}">
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.enrollAs"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.enrollAs.help"/>
							</jsp:include>						
							<select class="required" name="enrollAs">
								<option ${enrollAs=='UPPERSECONDARY' ? 'selected="selected"' : ''} value="UPPERSECONDARY">Lukio-opinnot</option>
								<option ${enrollAs=='VOCATIONAL' ? 'selected="selected"' : ''} value="VOCATIONAL">Ammatilliset opinnot</option>
							</select>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.numMandatoryCourses"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.numMandatoryCourses.help"/>
							</jsp:include>						
							<input type="text" name="numMandatoryCourses" value="${numMandatoryCourses}">
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.restartExam"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.restartExam.help"/>
							</jsp:include>						
							<input type="checkbox" name="restartExam" ${restartExam ? 'checked="checked"' : ''}/>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.location"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.location.help"/>
							</jsp:include>						
							<input type="text" name="location" value="${location}">
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.message"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.message.help"/>
							</jsp:include>						
							<textarea name="message">${message}</textarea>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.canPublishName"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.canPublishName.help"/>
							</jsp:include>						
							<input type="checkbox" name="canPublishName" ${canPublishName ? 'checked="checked"' : ''}/>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.state"/>
								<jsp:param name="helpLocale" value="matriculation.editEnrollment.state.help"/>
							</jsp:include>						
							<select class="required" name="state">
								<option ${state=='PENDING' ? 'selected="selected"' : ''} value="PENDING">Jätetty</option>
								<option ${state=='APPROVED' ? 'selected="selected"' : ''} value="APPROVED">Hyväksytty</option>
								<option ${state=='REJECTED' ? 'selected="selected"' : ''} value="REJECTED">Hylätty</option>
							</select>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.enrolled"/>
							</jsp:include>						
							<div class="genericTableAddRowContainer">
								<span class="genericTableAddRowLinkContainer" id="addEnrolledTableRow"><fmt:message key="matriculation.editEnrollment.enrolled.addRow"/></span>
							</div>
							<div id="enrolledAttendancesTableContainer"></div>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.finished"/>
							</jsp:include>						
							<div class="genericTableAddRowContainer">
								<span class="genericTableAddRowLinkContainer" id="addFinishedTableRow"><fmt:message key="matriculation.editEnrollment.finished.addRow"/></span>
							</div>
							<div id="finishedAttendancesTableContainer"></div>
						</div>

						<div class="genericFormSection">
							<jsp:include page="/templates/generic/fragments/formtitle.jsp">
								<jsp:param name="titleLocale" value="matriculation.editEnrollment.planned"/>
							</jsp:include>						
							<div class="genericTableAddRowContainer">
								<span class="genericTableAddRowLinkContainer" id="addPlannedTableRow"><fmt:message key="matriculation.editEnrollment.planned.addRow"/></span>
							</div>
							<div id="plannedAttendancesTableContainer"></div>
						</div>
						
						<button>Tallenna</button>
					</div>
				</form>
			</div>
		</div>
		
		<jsp:include page="/templates/generic/footer.jsp"></jsp:include>
	</body>
</html>