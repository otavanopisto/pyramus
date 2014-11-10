package fi.pyramus.rest.model;

public class CourseComponent {
  
  public CourseComponent() {
  }

  public CourseComponent(Long id, String name, String description, Double length, Long lengthUnitId, Boolean archived) {
    this();
    this.id = id;
    this.name = name;
    this.description = description;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.archived = archived;
  }

  public CourseComponent(String name, String description, Double length, Long lengthUnitId, Boolean archived) {
    this(null, name, description, length, lengthUnitId, archived);
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLengthUnitId() {
    return lengthUnitId;
  }

  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }
  
  public Boolean getArchived() {
    return archived;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private String description;
  private Double length;
  private Long lengthUnitId;
  private Boolean archived;
}
