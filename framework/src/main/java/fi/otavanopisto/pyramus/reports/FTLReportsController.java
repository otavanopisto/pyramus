package fi.otavanopisto.pyramus.reports;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;

import org.xhtmlrenderer.pdf.ITextRenderer;

import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportFileFormat;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.StudentTORController.StudentTORHandling;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Dependent
@Stateless
public class FTLReportsController {

  /**
   * Renders a report with the student's information.
   * 
   * This could use some optimization later:
   * - Add a Freemarker Template Loader so that the parsed templates can be cached
   * - Figure out if the data can be handled with streams hopefully reducing the
   *   memory footprint of storing the html and the byte data in memory.
   * 
   * @param report The report to render - this needs to be in FTL format
   * @param student The student who's information is passed to the FTL template
   * @param format The output format, either html or pdf.
   * @return The byte data of the rendered report, either html or pdf depending on the format.
   * @throws Exception
   */
  public byte[] renderFTLStudentReport(Report report, Student student, ReportFormat format) throws Exception {
    // This method only works with FTLs
    if (report.getFormat() != ReportFileFormat.FTL) {
      throw new IllegalArgumentException();
    }
    
    Configuration freemarkerConfig = new Configuration();
    freemarkerConfig.setDefaultEncoding("UTF-8");
    freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    
    // This should probably be a Template loading strategy so freemarker can cache the parsed templates
    // This approach forces the template to be parsed over every time
    Template freemarkerTemplate = new Template("temp", new StringReader(report.getData()), freemarkerConfig);

    // The information passed to the template
    
    StudentTOR tor = StudentTORController.constructStudentTOR(student, StudentTORHandling.CURRICULUM_MOVE_INCLUDED);
  
    Map<String, Object> studentInfo = new HashMap<>();
    studentInfo.put("student", student);
    studentInfo.put("studentTOR", tor);

    // Process the template
  
    StringWriter writer = new StringWriter();
    freemarkerTemplate.process(studentInfo, writer);
    String html = writer.toString();
  
    switch (format) {
      case HTML:
        return html.getBytes("UTF-8");
      
      case PDF:
        ITextRenderer renderer = new ITextRenderer();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // The added font shouldn't be hard coded
        renderer.getFontResolver().addFontDirectory("/usr/share/fonts/truetype/exo2", true);
        renderer.setDocumentFromString(html); //, baseUrl.toString());
        renderer.layout();
        renderer.createPDF(out);
        return out.toByteArray();
      default:
        throw new RuntimeException("Unknown format");
    }
  }

  public enum ReportFormat {
    HTML ("text/html"),
    PDF ("application/pdf");
    
    private final String contentType;

    ReportFormat(String contentType) {
      this.contentType = contentType;
    }

    public String getContentType() {
      return contentType;
    }
  }
}
