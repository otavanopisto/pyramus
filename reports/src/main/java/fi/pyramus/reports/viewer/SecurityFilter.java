package fi.pyramus.reports.viewer;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.base.MagicKeyScope;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;

public class SecurityFilter implements Filter {
  
  public void init(FilterConfig fc) throws ServletException {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();

    /**
     * If expire time is not set it defaults to 60000 milliseconds (1 minute)
     */
    long expireMills = 60000;
    SettingKey settingKey = settingKeyDAO.findByName("reports.magicKeyExpireMills");
    if (settingKey != null) {
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null && NumberUtils.isNumber(setting.getValue())) 
        expireMills = NumberUtils.createLong(setting.getValue());
    }
    
    Date expireThreshold = new Date(System.currentTimeMillis());
    expireThreshold.setTime(expireThreshold.getTime() - expireMills);
    String magicKeyName = request.getParameter("magicKey");
    
    MagicKey magicKey = magicKeyDAO.findByName(magicKeyName);
    if (magicKey != null) {
      try {
        if (magicKey.getScope() != MagicKeyScope.APPLICATION) {
          if (magicKey.getCreated().before(expireThreshold))
            throw new ServletException("Session expired");
        }

        chain.doFilter(request, response);
      } finally {
        if (magicKey.getScope() == MagicKeyScope.REQUEST)
          magicKeyDAO.delete(magicKey);
      }
    } else {
      throw new ServletException("Permission denied");
    }
  }

  public void destroy() {
  }

}