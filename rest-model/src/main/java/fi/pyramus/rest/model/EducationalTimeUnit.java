package fi.pyramus.rest.model;

public class EducationalTimeUnit {

  public EducationalTimeUnit() {
  }

  public EducationalTimeUnit(Long id, String name, String symbol, Double baseUnits, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.symbol = symbol;
    this.baseUnits = baseUnits;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Double getBaseUnits() {
    return baseUnits;
  }
  
  public void setBaseUnits(Double baseUnits) {
    this.baseUnits = baseUnits;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  private Long id;
  private String name;
  private String symbol;
  private Double baseUnits;
  private Boolean archived;
}
