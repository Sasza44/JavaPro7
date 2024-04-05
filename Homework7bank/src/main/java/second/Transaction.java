package second;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="transaction")
public class Transaction { // транзакція
	@Id
	private int id; // id, номер транзакції
	private int fromAccountId; // id рахунку, з якого знімається певна сума
	private int toAccountId; // id рахунку, якому надходить певна сума
	private double sum; // сума, яка перекидається при транзакції
	private String currencyId; // id валюти, у якій відбувається транзакція
	private LocalDateTime dateTime; // дата та час транзакції
	public Transaction() {
	}
	public int getId() {return id;}
	public int getFromAccountId() {return fromAccountId;}
	public void setFromAccountId(int fromAccountId) {this.fromAccountId = fromAccountId;}
	public int getToAccountId() {return toAccountId;}
	public void setToAccountId(int toAccountId) {this.toAccountId = toAccountId;}
	public double getSum() {return sum;}
	public void setSum(double sum) {this.sum = sum;}
	public String getCurrencyId() {return currencyId;}
	public void setCurrencyId(String currencyId) {this.currencyId = currencyId;}
	public LocalDateTime getDateTime() {return dateTime;}
	public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", fromAccountId=" + fromAccountId + ", toAccountId=" + toAccountId + ", sum="
				+ sum + ", currencyId=" + currencyId + ", dateTime=" + dateTime + "]";
	}
}
