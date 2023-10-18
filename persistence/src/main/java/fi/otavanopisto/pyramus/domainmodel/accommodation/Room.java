package fi.otavanopisto.pyramus.domainmodel.accommodation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Room {
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public RoomType getType() {
    return type;
  }
  
  public void setType(RoomType type) {
    this.type = type;
  }
  
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Room")  
  @TableGenerator(name="Room", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String name;
  
  @ManyToOne (optional = false)
  private RoomType type;
  
}
