package second;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class Account { // банківський рахунок
	@Id
	private int id; // id банківського рахунку
	private double sum; // сума, яка зберігається на рахунку
	private String currencyId; // валюта рахунку
	private int clientId;
	public Account() {
	}
	public Account(double sum, String currencyId, int clientId) {
		this.sum = sum;
		this.currencyId = currencyId;
		this.clientId = clientId;
	}
	public int getId() {return id;}
	public double getSum() {return sum;}
	public void setSum(double sum) {this.sum = sum;}
	public String getCurrencyId() {return currencyId;}
	public void setCurrencyId(String currencyId) {this.currencyId = currencyId;}
	public int getClientId() {return clientId;}
	public void setClientId(int clientId) {this.clientId = clientId;}
	@Override
	public String toString() {
		return "Account [id=" + id + ", sum=" + sum + ", currencyId=" + currencyId + ", clientId=" + clientId + "]";
	}
}