package fi.otavanopisto.pyramus.util;

import org.apache.commons.lang3.Strings;

/**
 * StringUtils inherited from Apache's StringUtils to bring back some of the
 * much more readable methods that were deprecated from it.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

  @SuppressWarnings("deprecation")
  private StringUtils() {
  }

  /**
   * Performs case sensitive comparison on two CharSequences. Returns true if they
   * match.
   * 
   * @param cs1
   * @param cs2
   * @return
   */
  public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
    return Strings.CS.equals(cs1, cs2);
  }

  /**
   * Performs case insensitive comparison on two CharSequences. Returns true if
   * they match.
   * 
   * @param cs1
   * @param cs2
   * @return
   */
  public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
    return Strings.CI.equals(cs1, cs2);
  }

  /**
   * Performs case sensitive comparison checking if the first CharSequence equals
   * any of the following ones. Returns true if match is found.
   * 
   * @param string
   * @param searchStrings
   * @return
   */
  public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings) {
    return Strings.CS.equalsAny(string, searchStrings);
  }

  public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
    return Strings.CS.contains(seq, searchSeq);
  }

  public static String replace(final String text, final String searchString, final String replacement) {
    return Strings.CS.replace(text, searchString, replacement);
  }

  public static String replace(final String text, final String searchString, final String replacement, final int max) {
    return Strings.CS.replace(text, searchString, replacement, max);
  }

  public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
    return Strings.CS.startsWith(str, prefix);
  }

}
