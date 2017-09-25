package fi.otavanopisto.pyramus.util;

import org.apache.commons.codec.binary.Hex;

public class MailAttachment {
  
  public MailAttachment(String fileName, String mimeType, byte[] content) {
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.content = content;
  }
  
  public String getMimeType() {
    return mimeType;
  }
  
  public byte[] getContent() {
    return content;
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String toString() {
    return "Type: " + mimeType + " Content: 0x" + Hex.encodeHexString(content);
  }

  private final String fileName;
  private final String mimeType;
  private final byte[] content;
}
