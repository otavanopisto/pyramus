package fi.pyramus.util.dataimport;

public interface ValueInterpreter<T> {
  T interpret(Object o);
}
