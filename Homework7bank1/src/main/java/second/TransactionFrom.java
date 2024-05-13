package second;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_from")
public class TransactionFrom {
	@Id
	private int id; // id, номер транзакції
	private double sum;
	@ManyToOne
	@JoinColumn(name = "currencyId")
	private ExchangeRate currency;
	private LocalDateTime dateTime = LocalDateTime.now();
	@ManyToOne
	@JoinColumn(name = "fromAccountId")
	private Account accountFrom;
	private int accountIdTo;
	public TransactionFrom() {
	}
	public TransactionFrom(double sum, ExchangeRate currency) {
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
	public int getAccountIdTo() {return accountIdTo;}
	public void setAccountIdTo(int accountIdTo) {this.accountIdTo = accountIdTo;}
	public Account getAccountFrom() {return accountFrom;}
	public void setAccountFrom(Account accountFrom) {this.accountFrom = accountFrom;}
	@Override
	public String toString() {
		return "TransactionFrom [id=" + id + ", sum=" + sum + ", currency=" + currency.getCurrency() + ", dateTime=" + dateTime
				+ ", accountFrom=" + accountFrom.getId() + ", accountTo=" + accountIdTo + "]";
	}
}