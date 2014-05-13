package fi.pyramus.domainmodel.courses;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Entity
public class OtherCost {
  
  protected OtherCost() {
  }
  
  public OtherCost(Course course) {
    setCourse(course);
  }

  public Long getId() {
    return id;
  }

  protected void setCourse(Course course) {
    this.course = course;
  }

  public Course getCourse() {
    return course;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCost(MonetaryAmount cost) {
    this.cost = cost;
  }

  public MonetaryAmount getCost() {
    return cost;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="OtherCost")  
  @TableGenerator(name="OtherCost", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @Embedded  
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="cost_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="cost_currency"))
  })
  private MonetaryAmount cost;
  
  @Version
  @Column(nullable = false)
  private Long version;
}