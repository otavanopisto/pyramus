package fi.pyramus.persistence.usertypes;

import java.io.Serializable;
import java.util.Currency;

public class MonetaryAmount implements Serializable {
  
  private static final long serialVersionUID = -621456219507124116L;

  public MonetaryAmount(Double amount) {
    this(amount == null ? 0.0 : amount, Currency.getInstance("EUR"));
  }
  
  public MonetaryAmount(Double amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }
  
  public Double getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }
  
  public static MonetaryAmount convert(MonetaryAmount monetaryAmount, Currency currency) {
    // TODO Currency conversions, if needed
    return new MonetaryAmount(monetaryAmount.getAmount(), monetaryAmount.getCurrency());
  }
  
  public MonetaryAmount convertTo(Currency currency) {
    // TODO Currency conversions, if needed
    return new MonetaryAmount(amount, currency);
  }
  
  public String toString() {
    return amount == null ? "" : amount.toString();
  }
  
  private Double amount;
  private Currency currency;

}
