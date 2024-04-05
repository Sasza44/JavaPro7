package second;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="exchangerate")
public class ExchangeRate { // курс валют (відносно гривні)
	@Id
	private String currency; // назва іноземної валюти
	private double exchangeRate; // власне сам курс
	public ExchangeRate() {
	}
	public ExchangeRate(String currency, double exchangeRate) {
		this.currency = currency;
		this.exchangeRate = exchangeRate;
	}
	public String getCurrency() {return currency;}
	public void setCurrency(String currency) {this.currency = currency;}
	public double getExchangeRate() {return exchangeRate;}
	public void setExchangeRate(double exchangeRate) {this.exchangeRate = exchangeRate;}
	@Override
	public String toString() {
		return "ExchangeRate [currency=" + currency + ", exchangeRate=" + exchangeRate + "]";
	}
}
