package fi.otavanopisto.pyramus.rest.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.mailer.Mailer;

@Stateless
@Dependent
public class MatriculationController {
  
  /**
   * Sends email notification about the enrollment's state change. The message
   * is based on the current state of the given enrollment so make sure it is
   * updated before calling this.
   * 
   * @param request 
   * @param enrollmentEntity
   */
  public void sendNotificationOnStateChange(ServletRequest request, MatriculationExamEnrollment enrollmentEntity) {
    if (enrollmentEntity == null || enrollmentEntity.getState() == null || enrollmentEntity.getStudent() == null) {
      return;
    }
    
    String senderEmail;
    String recipientEmail;
    String subject;
    String content;
    Set<String> bcc = new HashSet<>();
    
    final String studentName = enrollmentEntity.getStudent().getFullName();
    final String enrollmentEditLink = String.format("%s/matriculation/edit.page?enrollment=%d", ApplicationUtils.getRequestURIRoot(request), enrollmentEntity.getId());

    switch (enrollmentEntity.getState()) {
      case PENDING:
        senderEmail = null;
        recipientEmail = enrollmentEntity.getStudent().getPrimaryEmail() != null ? enrollmentEntity.getStudent().getPrimaryEmail().getAddress() : null;
        bcc.add("yo-ilmoittautumiset@otavia.fi");
        subject = "Ilmoittautuminen ylioppilaskirjoituksiin";
        content =
            String.format("<p>Hei %s!</p>", studentName) + 
            "<p>Ilmoittautumisesi ylioppilaskirjoituksiin on tallennettu. Oma opinto-ohjaajasi käy ilmoittautumisesi läpi ja hyväksyy sen. Jos ilmoittautumisessa on jotain muutettavaa, hän pyytää siihen täydennystä. Kun opinto-ohjaaja on hyväksynyt ilmoittautumisen, sinun pitää vahvistaa ilmoittautumisesi vielä Muikussa.</p>" +
            "<p>Seuraa sähköpostiasi ja toimi viestin ohjeiden mukaisesti. Näet ilmoittautumisen tilan Muikun HOPSissa. Varmista, että olet vahvistanut ilmoittautumisen.</p>" +
            "<p>Tämä on automaattinen sähköpostiviesti Muikku-oppimisympäristöstä. Älä vastaa tähän viestiin. Jos sinulla on kysyttävää, ota yhteyttä opinto-ohjaajaasi.</p>";
      break;
      
      case SUPPLEMENTATION_REQUEST:
        senderEmail = (enrollmentEntity.getHandler() != null && enrollmentEntity.getHandler().getPrimaryEmail() != null) ? enrollmentEntity.getHandler().getPrimaryEmail().getAddress() : null;
        recipientEmail = enrollmentEntity.getStudent().getPrimaryEmail() != null ? enrollmentEntity.getStudent().getPrimaryEmail().getAddress() : null;
        subject = "Ylioppilaskirjoituksiin ilmoittautumista on täydennettävä";
        content =
            String.format("<p>Hei %s!</p>", studentName) + 
            "<p>Opinto-ohjaajasi on pyytänyt sinua täydentämään ylioppilaskirjoituksiin ilmoittautumista. Näet täydennyspyynnön tiedot Muikun HOPSissa. Tee täydennykset mahdollisimman pian.</p>" +
            "<p>Oma opinto-ohjaajasi tarkistaa täydennyksen ja hyväksyy sen. Jos ilmoittautumisessa on vielä jotain muutettavaa, hän pyytää uutta täydennystä. Kun opinto-ohjaaja on hyväksynyt ilmoittautumisen, sinun pitää vielä vahvistaa ilmoittautumisesi Muikussa.</p>" +
            "<p>Seuraa sähköpostiasi ja toimi viestin ohjeiden mukaisesti. Näet ilmoittautumisen tilan Muikun HOPSissa. Varmista, että olet vahvistanut ilmoittautumisen.";
        if (senderEmail == null) {
          content +=
              "<p>Tämä on automaattinen sähköpostiviesti Muikku-oppimisympäristöstä. Älä vastaa tähän viestiin. Jos sinulla on kysyttävää, ota yhteyttä opinto-ohjaajaasi.</p>";
        }
      break;

      case APPROVED:
        senderEmail = (enrollmentEntity.getHandler() != null && enrollmentEntity.getHandler().getPrimaryEmail() != null) ? enrollmentEntity.getHandler().getPrimaryEmail().getAddress() : null;
        recipientEmail = enrollmentEntity.getStudent().getPrimaryEmail() != null ? enrollmentEntity.getStudent().getPrimaryEmail().getAddress() : null;
        subject = "Ylioppilaskirjoituksiin ilmoittautuminen on vahvistettava";
        content = 
            String.format("<p>Hei %s!</p>", studentName) + 
            "<p>Opinto-ohjaajasi on hyväksynyt ylioppilaskirjoituksiin ilmoittautumisesi. Käy vahvistamassa ilmoittautumisesi Muikussa mahdollisimman pian. Vasta vahvistamisen jälkeen ilmoittautuminen voidaan välittää Ylioppilastutkintolautakuntaan.</p>";
        if (senderEmail == null) {
          content +=
              "<p>Tämä on automaattinen sähköpostiviesti Muikku-oppimisympäristöstä. Älä vastaa tähän viestiin. Jos sinulla on kysyttävää, ota yhteyttä opinto-ohjaajaasi.</p>";
        }
      break;
      
      case SUPPLEMENTED:
        senderEmail = null;
        recipientEmail = (enrollmentEntity.getHandler() != null && enrollmentEntity.getHandler().getPrimaryEmail() != null) ? enrollmentEntity.getHandler().getPrimaryEmail().getAddress() : null;
        subject = "Ylioppilaskirjoituksiin ilmoittautumista on täydennetty";
        content = String.format("<p>%s on täydentänyt yo-ilmoittautumistaan.</p>", studentName) +
            String.format("<p>Linkki ilmoittautumisen muokkaukseen:<br/><a href=\"%s\">%s</a></p>", enrollmentEditLink, enrollmentEditLink);
      break;
      
      case CONFIRMED:
        senderEmail = null;
        recipientEmail = (enrollmentEntity.getHandler() != null && enrollmentEntity.getHandler().getPrimaryEmail() != null) ? enrollmentEntity.getHandler().getPrimaryEmail().getAddress() : null;
        subject = "Ylioppilaskirjoituksiin ilmoittautuminen on vahvistettu";
        content = String.format("<p>%s on vahvistanut yo-ilmoittautumisen.</p>", studentName) +
            String.format("<p>Linkki ilmoittautumisen muokkaukseen:<br/><a href=\"%s\">%s</a></p>", enrollmentEditLink, enrollmentEditLink);
      break;
      
      // These states do not have notifications attached to them
      case FILLED_ON_BEHALF:
      case REJECTED:
      default:
        return;
    }
    
    // Default sender if it wasn't set by the different cases
    if (senderEmail == null) {
      senderEmail = PyramusConsts.NOREPLY_EMAIL;
    }
    
    if (StringUtils.isNotBlank(recipientEmail)) {
      Mailer.sendMail(
          Mailer.JNDI_APPLICATION,
          Mailer.HTML,
          senderEmail,
          Collections.singleton(recipientEmail),
          Collections.emptySet(),
          bcc,
          subject,
          content,
          Collections.emptyList(),
          null);
    }
  }
  
}
