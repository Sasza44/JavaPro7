package second;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="client")
public class Client { // клієнт банку
	@Id
	private int id; // id клієнта
	private String name; // ім'я клієнта
	private String phone; // номер телефону клієнта
	public Client() {
	}
	public Client(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	public int getId() {return id;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getPhone() {return phone;}
	public void setPhone(String phone) {this.phone = phone;}
	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}
}