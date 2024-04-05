package first;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dish")
public class Dish {
	@Id
	private String name; // назва страви
	private int price; // ціна
	private int weight; // маса порції у грамах
	private int discount; // знижка у відсотках
	public Dish() {
	}
	public Dish(String name, int price, int weight, int discount) {
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.discount = discount;
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getPrice() {return price;}
	public void setPrice(int price) {this.price = price;}
	public int getWeight() {return weight;}
	public void setWeight(int weight) {this.weight = weight;}
	public int getDiscount() {return discount;}
	public void setDiscount(int discount) {this.discount = discount;}
	@Override
	public String toString() {
		return "Dish [name=" + name + ", price=" + price + ", weight=" + weight + ", discount=" + discount + "]";
	}
}