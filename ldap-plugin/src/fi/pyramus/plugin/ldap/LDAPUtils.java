package fi.pyramus.plugin.ldap;

import java.io.UnsupportedEncodingException;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;

public class LDAPUtils {

  public static final LDAPConnection getLDAPConnection() throws LDAPException {
    LDAPConnection connection = new LDAPConnection();
    
    connection.connect(System.getProperty("authentication.ldap.host"), Integer.parseInt(System.getProperty("authentication.ldap.port")));
  
    try {
      connection.bind(Integer.parseInt(System.getProperty("authentication.ldap.version")), System.getProperty("authentication.ldap.binddn"), System.getProperty("authentication.ldap.bindpw").getBytes("UTF8"));
    } catch (UnsupportedEncodingException exception) {
      exception.printStackTrace();
      throw new LDAPException();
    }
  
    return connection;
  }

  public static final String getAttributeBinaryValue(LDAPAttribute attribute) {
    String value = new String();
    byte[] GUID = attribute.getByteValue(); 
    for (int i = 0; i < GUID.length; i++)
      value += Integer.toString(GUID[i], 16);
    
    return value;
  }
}
