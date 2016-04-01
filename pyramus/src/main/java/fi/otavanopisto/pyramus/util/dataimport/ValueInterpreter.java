package fi.otavanopisto.pyramus.util.dataimport;

public interface ValueInterpreter<T> {
  T interpret(Object o);
}
