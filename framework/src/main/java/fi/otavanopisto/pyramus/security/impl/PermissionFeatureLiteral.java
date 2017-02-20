package fi.otavanopisto.pyramus.security.impl;

import javax.enterprise.util.AnnotationLiteral;

public class PermissionFeatureLiteral extends AnnotationLiteral<PermissionFeature> implements PermissionFeature {

  private static final long serialVersionUID = 4425254138552441177L;

  public PermissionFeatureLiteral(String value) {
    this.value = value;
  }
  
  @Override
  public String value() {
    return value;
  }

  private String value;
}
