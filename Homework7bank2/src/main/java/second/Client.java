package second;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client { // клієнт банку
	@Id
	private int id; // id клієнта
	private String name;
	private String phoneNumber;
	@OneToMany(mappedBy = "client")
	private List<Account> accounts = new ArrayList<>();
	public Client() {
	}
	public Client(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	public int getId() {return id;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getPhoneNumber() {return phoneNumber;}
	public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
	public List<Account> getAccounts() {return accounts;}
	public void setAccounts(List<Account> accounts) {this.accounts = accounts;}
	@Override
	public String toString() {
		List<Integer> accountsId = new ArrayList<>(); // новий список цілих чисел
		for(Account a: accounts) accountsId.add(a.getId()); // у нього складаємо лише номери рахунків клієнта для компактного відображення
		return "Client [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", accounts=" + accountsId + "]";
	}
}