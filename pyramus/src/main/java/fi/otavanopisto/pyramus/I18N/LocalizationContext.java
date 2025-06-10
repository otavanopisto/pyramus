package fi.otavanopisto.pyramus.I18N;

import java.util.Locale;

public class LocalizationContext extends jakarta.servlet.jsp.jstl.fmt.LocalizationContext {
  
  public LocalizationContext(Locale locale) {
    super(Messages.getInstance().getResourceBundle(locale), locale);
  }
    
}
