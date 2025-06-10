package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.jsp.jstl.core.Config;

import org.apache.commons.lang.StringUtils;

public class LocaleFilter implements Filter {

  public void init(FilterConfig arg0) throws ServletException {
    
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    ServletRequest servletRequest = request;
    try {
      if (request instanceof HttpServletRequest) {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        String localeCode = "";
        if (cookies != null) {
          for (Cookie cookie : cookies) {
            if ("pyramusLocale".equals(cookie.getName())) {
              localeCode = cookie.getValue();
              break;
            }
          }
        }

        if (StringUtils.isBlank(localeCode)) {
          localeCode = servletRequest.getLocale().toString();
        }

        if (!localeCode.equals(request.getLocale().toString())) {
          Locale locale;
          String[] localeCodeS = localeCode.split("_");
          if (localeCodeS.length == 2)
            locale = new Locale(localeCodeS[0], localeCodeS[1]);
          else {
            locale = new Locale(localeCodeS[0]);
          }

          Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, new fi.otavanopisto.pyramus.I18N.LocalizationContext(locale));
          servletRequest = new LocaleRequestWrapper((HttpServletRequest) request, locale);
        } else {
          Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, new fi.otavanopisto.pyramus.I18N.LocalizationContext(request.getLocale()));
        }
      }
    } finally {
      filterChain.doFilter(servletRequest, response);
    }
  }

  public void destroy() {
  }

  public class LocaleRequestWrapper extends HttpServletRequestWrapper {
    public LocaleRequestWrapper(HttpServletRequest req, Locale locale) {
      super(req);
      this.locale = locale;
    }

    public Enumeration<Locale> getLocales() {
      Vector<Locale> v = new Vector<>(1);
      v.add(getLocale());
      return v.elements();
    }

    public Locale getLocale() {
      return locale;
    }

    private Locale locale;
  }

}

