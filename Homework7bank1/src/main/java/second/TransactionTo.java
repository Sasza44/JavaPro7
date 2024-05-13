package second;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_to")
public class TransactionTo {
	@Id
	private int id; // id, номер транзакції
	private double sum;
	@ManyToOne
	@JoinColumn(name = "currencyId")
	private ExchangeRate currency;
	private LocalDateTime dateTime = LocalDateTime.now();
	private int accountIdFrom;
	@ManyToOne
	@JoinColumn(name = "toAccountId")
	private Account accountTo;
	public TransactionTo() {
	}
	public TransactionTo(double sum, ExchangeRate currency) {
		this.sum = sum;
		this.currency = currency;
	}
	public int getId() {return id;}
	public double getSum() {return sum;}
	public void setSum(double sum) {this.sum = sum;}
	public ExchangeRate getCurrency() {return currency;}
	public void setCurrency(ExchangeRate currency) {this.currency = currency;}
	public LocalDateTime getDateTime() {return dateTime;}
	public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
	public Account getAccountTo() {return accountTo;}
	public void setAccountTo(Account accountTo) {this.accountTo = accountTo;}
	public int getAccountIdFrom() {return accountIdFrom;}
	public void setAccountIdFrom(int accountIdFrom) {this.accountIdFrom = accountIdFrom;}
	@Override
	public String toString() {
		return "TransactionTo [id=" + id + ", sum=" + sum + ", currency=" + currency.getCurrency() + ", dateTime=" + dateTime
				+ ", aacountFrom=" + accountIdFrom + ", accountTo=" + accountTo.getId() + "]";
	}
}