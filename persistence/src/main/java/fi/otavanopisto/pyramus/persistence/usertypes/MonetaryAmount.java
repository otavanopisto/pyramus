package fi.otavanopisto.pyramus.persistence.usertypes;

import java.io.Serializable;
import java.util.Currency;

import javax.persistence.Embeddable;

@Embeddable
public class MonetaryAmount implements Serializable {
  
  private static final long serialVersionUID = -621456219507124116L;

  public Double getAmount() {
		return amount;
	}
  
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	private Double amount;
  private Currency currency;
}
