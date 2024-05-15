package second;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	private int id;
	private double sum;
	@ManyToOne
	@JoinColumn(name = "currencyId")
	private ExchangeRate currency;
	private LocalDateTime dateTime = LocalDateTime.now();
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Account> accounts = new ArrayList<>(); //  гроші переходять з 0 на 1 рахунок
	@Column
	private int accountFrom = 0;
	@Column
	private int accountTo = 0;
	public Transaction() {
	}
	public Transaction(double sum, ExchangeRate currency) {
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
	public List<Account> getAccounts() {return accounts;}
	public void setAccounts(List<Account> accounts) {this.accounts = accounts;}
	public int getAccountFrom() {return accountFrom;}
	public void setAccountFrom(int accountFrom) {this.accountFrom = accountFrom;}
	public int getAccountTo() {return accountTo;}
	public void setAccountTo(int accountTo) {this.accountTo = accountTo;}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", sum=" + ", currency=" + currency.getCurrency() + sum + ", dateTime=" + dateTime +
				", accountFrom=" + accountFrom + ", accountTo=" + accountTo + "]";
	}
}