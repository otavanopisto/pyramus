package fi.pyramus.I18N;

import java.util.Locale;

public class LocalizationContext extends javax.servlet.jsp.jstl.fmt.LocalizationContext {
  
  public LocalizationContext(Locale locale) {
    super(Messages.getInstance().getResourceBundle(locale), locale);
  }
    
}
