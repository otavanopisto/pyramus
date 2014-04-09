package fi.pyramus.util.dataimport.scripting.api;

public class Handle {
  
  /* package */ Handle(Long id) {
    this.id = id;
  }
  
  /* package */ Long getId() {
    return id;
  }
  
  public static Handle empty() {
    return new Handle(null);
  }
  
  private Long id;

}
