package second;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account { // банківський рахунок
	@Id
	private int id; // id банківського рахунку
	private double sum;
	@ManyToOne
	@JoinColumn(name = "currencyId")
	private ExchangeRate currency;
	@ManyToOne
	@JoinColumn(name = "clientId")
	private Client client;
	@OneToMany(mappedBy = "accountFrom")
	private List <TransactionFrom> transactionsFrom = new ArrayList<>(); // список транзакцій знімання з рахунку
	@OneToMany(mappedBy = "accountTo")
	private List <TransactionTo> transactionsTo = new ArrayList<>(); // список транзакцій поповнення рахунку
	public Account() {
	}
	public Account(double sum, ExchangeRate currency, Client client) {
		this.sum = sum;
		this.currency = currency;
		this.client = client;
	}
	public int getId() {return id;}
	public double getSum() {return sum;}
	public void setSum(double sum) {this.sum = sum;}
	public ExchangeRate getCurrency() {return currency;}
	public void setCurrency(ExchangeRate currency) {this.currency = currency;}
	public Client getClient() {return client;}
	public void setClient(Client client) {this.client = client;}
	public List<TransactionFrom> getTransactionsFrom() {return transactionsFrom;}
	public void setTransactionsFrom(List<TransactionFrom> transactionsFrom) {this.transactionsFrom = transactionsFrom;}
	public List<TransactionTo> getTransactionsTo() {return transactionsTo;}
	public void setTransactionsTo(List<TransactionTo> transactionsTo) {this.transactionsTo = transactionsTo;}
	@Override
	public String toString() {
		return "Account [id=" + id + ", sum=" + sum + ", currency=" + currency.getCurrency() + ", clientId=" + client.getId() + "]";
	}
	public void increase(double sum, ExchangeRate currency) { // додавання грошей на рахунок
		double sum1 = sum * currency.getExchangeRate() / this.currency.getExchangeRate();
		this.sum += sum1;
	}
	public boolean decrease(double sum, ExchangeRate currency) { // знімання грошей з рахунку (бітова змінна потрібна для дозволу оформлення транзакції)
		double sum1 = sum * currency.getExchangeRate() / this.currency.getExchangeRate();
		if(sum1 <= this.sum) {
			this.sum -= sum1;
			return true;
		}
		else {
			System.out.println("Недостатньо коштів на рахунку. Залишок = " + this.sum + " " + this.currency.getCurrency());
			return false;
		}
	}
}