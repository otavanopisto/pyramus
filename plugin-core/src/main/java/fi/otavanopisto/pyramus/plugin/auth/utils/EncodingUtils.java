package fi.otavanopisto.pyramus.plugin.auth.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

/** A class containing miscellaneus utilities for encoding and decoding.
 *
 */
public class EncodingUtils {

  private EncodingUtils() {
  }

  /** Calculate the MD5 sum of a string.
   * 
   * @param s The string to encode.
   * @return The MD5 sum of the string, in hexadecimal format.
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   */
  public static String md5EncodeString(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    if (s == null)
      return null;

    if (StringUtils.isBlank(s))
      return "";

    MessageDigest algorithm = MessageDigest.getInstance("MD5");
    algorithm.reset();
    algorithm.update(s.getBytes("UTF-8"));
    byte messageDigest[] = algorithm.digest();

    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < messageDigest.length; i++) {
      String hex = Integer.toHexString(0xFF & messageDigest[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

}
