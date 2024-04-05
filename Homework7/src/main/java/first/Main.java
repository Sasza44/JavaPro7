package first;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
	static EntityManagerFactory emf;
	static EntityManager em;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			emf = Persistence.createEntityManagerFactory("Homework7");
			em = emf.createEntityManager();
			System.out.println("Оберіть дію: ");
			System.out.println("1 - Заповнити таблицю меню.");
			System.out.println("2 - Додати запис у таблицю.");
			System.out.println("3 - Зробити вибірку з таблиці.");
			System.out.println("4 - Вибрати набір страв до 1 кг.");
			System.out.println("5 - Нічого не робити.");
			System.out.println("-> ");
			int selection = sc.nextInt();
			doSelection(selection, sc);
		} finally {
			sc.close();
			em.close();
			emf.close();
		}
	}
	
	public static void doSelection(int selection, Scanner sc) {
		switch(selection) {
			case(1):
				fill(); // Заповнити таблицю меню
				break;
			case(2):
				add(sc); // Додати запис у таблицю
				break;
			case(3):
				getObjectsFromTable(sc); // Зробити вибірку з таблиці
				break;
			case(4):
				get1kg(); // Вибрати набір страв до 1 кг
				break;
			case(5): // Нічого не робити
				break;
		}
	}

	public static void fill() { // заповнити таблицю
		Dish d1 = new Dish("Домашній льох", 170, 300, 0);
		Dish d2 = new Dish("Квашена капуста", 85, 200, 0);
		Dish d3 = new Dish("Вінігрет", 95, 200, 10);
		Dish d4 = new Dish("Олів'є", 140, 200, 0);
		Dish d5 = new Dish("Борщ український", 120, 300, 10);
		Dish d6 = new Dish("Пательня з грибами", 210, 250, 0);
		Dish d7 = new Dish("Деруни зі сметаною", 175, 250, 10);
		Dish d8 = new Dish("Реберця гриль з медом", 220, 200, 0);
		Dish d9 = new Dish("Стейк зі свинини", 220, 200, 0);
		Dish d10 = new Dish("Картопля смажена", 105, 250, 0);
		Dish d11 = new Dish("Локшина", 75, 200, 0);
		Dish d12 = new Dish("Шашлик", 220, 200, 0);
		Dish d13 = new Dish("Вареники з картоплею", 140, 250, 5);
		Dish d14 = new Dish("Млинці з яблуками", 85, 200, 0);
		Dish d15 = new Dish("Морозиво з шоколадом", 98, 150, 0);
		List<Dish> listOfDishes = new ArrayList<>(List.of(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15));
		
		em.getTransaction().begin();
		try {
			for(Dish d: listOfDishes) {
				em.merge(d); // щоб не було записів з однаковими назвами
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void add(Scanner sc) { // додати об'єкт в базу
		System.out.println("Введіть назву страви: ");
		String name = sc.nextLine();
		name = sc.nextLine(); // чомусь тут за один запис не дає ввести назву
		System.out.println("Введіть ціну: ");
		int price = sc.nextInt();
		System.out.println("Введіть масу в грамах: ");
		int weight = sc.nextInt();
		System.out.println("Введіть знижку у відсотках: ");
		int discount = sc.nextInt();
		
		em.getTransaction().begin();
		try {
			Dish d = new Dish(name, price, weight, discount);
//			em.persist(d);
			em.merge(d); // якщо запис з такою назвою є в таблиці
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void getObjectsFromTable(Scanner sc) { // Зробити вибірку з таблиці
		System.out.println("Введіть мінімальну ціну страви: ");
		int price1 = 0;
		String sPrice1 = sc.nextLine();
		sPrice1 = sc.nextLine(); // чомусь тут за один запис не дає ввести мінімальної ціни
		if(!sPrice1.equals("")) price1 = Integer.parseInt(sPrice1); // по замовчуванню обираємо 0
		
		System.out.println("Введіть максимальну ціну страви: ");
		int price2 = 1000;
		String sPrice2 = sc.nextLine();
		if(!sPrice2.equals("")) price2 = Integer.parseInt(sPrice2); // по замовчуванню обираємо 1000
		
		System.out.println("Введіть \"1\", якщо хочете обрати лише страви зі знижками: ");
		String sIsDiscount = sc.nextLine();
		boolean isDiscount = false;
		if(sIsDiscount.equals("1")) isDiscount = true;
		
		em.getTransaction().begin();
		try {
			StringBuilder sb1 = new StringBuilder();
			sb1.append("SELECT x FROM Dish x WHERE (x.price BETWEEN " + price1 + " AND " + price2 + ")");
			if(isDiscount == true) {
				sb1.append(" AND x.discount > 0");
			}
			Query query = em.createQuery(sb1.toString(), Dish.class);
			List<Dish> listMenu = (List<Dish>) query.getResultList();			
			for(Dish d: listMenu) System.out.println(d);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void get1kg() { // Вибрати набір страв до 1 кг
		em.getTransaction().begin();
		try {			
			Query query = em.createQuery("SELECT x FROM Dish x", Dish.class);
			List<Dish> listMenu = (List<Dish>) query.getResultList(); // записуємо у список усю таблицю
			Random r = new Random();
			int w1 = 0; // загальна вага набору
			HashSet<Dish> s1 = new HashSet<>(); // набір, який не допускає однакових елементів
			while(true) {
				int n1 = r.nextInt(listMenu.size() + 1); // випадкове число, яке не перевищує кількість рядків таблиці
				Dish d1 = listMenu.get(n1); // випадковий елемент зі списку
				if(!s1.contains(d1)) {
					s1.add(d1); // додаємо випадково обраний елемент у набір
					w1 += d1.getWeight();
					if(w1 < 1000) System.out.println(d1);
				}
				if(w1 >= 1000) {
					s1.remove(d1); // якщо вага набору перевищує 1 кг, видаляємо останній елемент
					w1 -= d1.getWeight();
					break;
				}
			}	
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
}