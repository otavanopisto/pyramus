package fi.pyramus.util;

import java.security.SecureRandom;
import java.util.Random;

public class OauthClientSecretGenerator {

  private static final String symbols = "abcdefghijklmnopqrstuwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";

  private final Random random = new SecureRandom();
  private final char[] buf;

  public OauthClientSecretGenerator(int length) {
    if (length < 1)
      throw new IllegalArgumentException("length < 1: " + length);
    buf = new char[length];
  }

  public String nextString() {
    for (int idx = 0; idx < buf.length; ++idx)
      buf[idx] = symbols.charAt(random.nextInt(symbols.length()));
    return new String(buf);
  }

}
