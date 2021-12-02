package fi.otavanopisto.pyramus.util.ixtable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PyramusIxTableRowsFacade implements Iterable<PyramusIxTableRowFacade> {
  
  protected PyramusIxTableRowsFacade(PyramusIxTableFacade table) {
    this.table = table;
  }
  
  @Override
  public Iterator<PyramusIxTableRowFacade> iterator() {
    return new PyramusIxTableRowFacadeIterator(table.getRowCount());
  }
  
  private PyramusIxTableFacade table;
  
  public class PyramusIxTableRowFacadeIterator implements Iterator<PyramusIxTableRowFacade> {

    public PyramusIxTableRowFacadeIterator(int rowCount) {
      this.rowCount = rowCount;
      this.currentRow = 0;
    }
    
    @Override
    public boolean hasNext() {
      return currentRow < rowCount;
    }

    @Override
    public PyramusIxTableRowFacade next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      
      return new PyramusIxTableRowFacade(table, currentRow++);
    }

    private int currentRow;
    private int rowCount;
  }

}
