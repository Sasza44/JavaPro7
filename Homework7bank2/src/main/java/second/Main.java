package second;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
	static EntityManagerFactory emf;
	static EntityManager em;


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			emf = Persistence.createEntityManagerFactory("Homework7bank2");
			em = emf.createEntityManager();
			System.out.println("Оберіть дію: "); // програма не враховує банківську комісію
			System.out.println("1 - Заповнити таблицю курси валют.");
			System.out.println("2 - Створити клієнта.");
			System.out.println("3 - Знайти клієнта, якщо відоме повне ім'я.");
			System.out.println("4 - Створити банківський рахунок.");
			System.out.println("5 - Поповнення (зняття з) рахунку.");
			System.out.println("6 - Перекидання коштів з одного рахунку на інший.");
			System.out.println("7 - Конвертувати рахунок в іншу валюту.");
			System.out.println("8 - Отримати сумарний баланс рахунків одного клієнта (в перерахуванні на гривні).");
			System.out.println("9 - Отримати список транзакцій.");
			System.out.println("10 - Нічого не робити.");
			System.out.println("-> ");
			int selection = sc.nextInt();
			select(sc, selection);
		} finally {
			sc.close();
			em.close();
			emf.close();
		}
	}
	
	public static void select(Scanner sc, int selection) {
		switch(selection) {
			case(1):
				fillExchangeRate(); // заповнити таблицю курсів валют
				break;
			case(2):
				createClient(sc); // створити клієнта
				break;
			case(3):
				Client client = findClient(sc);  // знайти клієнта в базі, коли відоме повне ім'я
				System.out.println(client.getName() + ", id = " + client.getId());
				break;
			case(4):
				createAccount(sc); // створити рахунок, коли відоме id клієнта
				break;
			case(5):
				changeAccount(sc); // змінити стан рахунку, у якого відоме id
				break;
			case(6):
				move(sc); // перекидання грошей з одного рахунку на інший
				break;
			case(7):
				convert(sc); // зміна валюти на рахунку
				break;
			case(8):
				countAllSum(sc); // підрахунок загальної суми на рахунках клієнта
				break;
			case(9):
				getTransactionList(sc); // виведення списку транзакцій
				break;
			case(10):
				break;
		}
	}
	
	public static void fillExchangeRate() { // заповнити таблицю курсів валют
		ExchangeRate usd = new ExchangeRate("USD", 39.1953); // Доллар США
		ExchangeRate eur = new ExchangeRate("EUR", 42.2643); // Євро
		ExchangeRate uah = new ExchangeRate("UAH", 1.0000); // введемо гривню для полегшення розрахунків
		em.getTransaction().begin();
		try {
			em.merge(usd); // якщо є записи таких валют у таблиці, то курси оновлюються
			em.merge(eur); // значеннями, вказаними у конструкторах
			em.merge(uah);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void createClient(Scanner sc) { // створити клієнта
		em.getTransaction().begin();
		try {
			System.out.println("Напишіть прізвище, ім'я та по-батькові клієнта: ");
			String name = sc.nextLine();
			name = sc.nextLine(); // чомусь тут при одному записі не дає ввести імені
			System.out.println("Напишіть номер мобільника клієнта: ");
			String phone = sc.nextLine();
			Client client = new Client(name, phone); // створення нового об'єкту класу Client
			em.persist(client);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static Client findClient(Scanner sc) { // знайти клієнта в базі, коли відоме повне ім'я
		Client c = null;
		try {
			System.out.println("Напишіть прізвище, ім'я та по-батькові клієнта: ");
			String name = sc.nextLine();
			name = sc.nextLine(); // чомусь тут при одному записі не дає ввести імені
			Query query = em.createQuery("SELECT x FROM Client x WHERE x.name = :name", Client.class);
			query.setParameter("name", name);
			c = (Client) query.getSingleResult();
		} catch (NoResultException ex) {
			System.out.println("Client not found!");
		} catch (NonUniqueResultException ex) {
			System.out.println("Non unique result!");
		}
		return c;
	}
	
	public static void createAccount(Scanner sc) { // створити рахунок, коли відоме id клієнта
		em.getTransaction().begin();
		try {
			System.out.println("Вкажіть id валюти рахунку (великими літерами): ");
			String currencyId = sc.nextLine();
			currencyId = sc.nextLine(); // чомусь при одному записі не дає ввести ідентифікатор валюти
			ExchangeRate currency = em.find(ExchangeRate.class, currencyId); // знаходимо курс валюти рахунку у таблиці
			System.out.println("Вкажіть початкову суму на рахунку (у вигляді десяткового дробу з комою): ");
			double sum = sc.nextDouble();
			System.out.println("Вкажіть id клієнта: ");
			int clientId = sc.nextInt();
			Client client = em.find(Client.class, clientId); // знаходимо власника рахунку
			Account account = new Account(sum, currency, client); // створюємо новий рахунок
			em.persist(account);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void changeAccount(Scanner sc) { // змінити стан рахунку, у якого відоме id
		em.getTransaction().begin();
		try {
			System.out.println("Вкажіть id рахунку: ");
			int accountId = sc.nextInt();
			Account account = em.find(Account.class, accountId);
			System.out.println(account);
			System.out.println("Вкажіть валюту операції: ");
			String sCurrency = sc.nextLine();
			sCurrency = sc.nextLine(); // чомусь при одному записі не дає ввести
			ExchangeRate currency = em.find(ExchangeRate.class, sCurrency);
			boolean adding = true; // поповнення рахунку (знімання false)
			System.out.println("Якщо бажаєте поповнити рахунок, введіть 1 (за замовчуванням - знімання грошей): ");
			String sAdding = sc.nextLine();
			if(!sAdding.equals("1")) adding = false;
			if(adding == true) increaseAccount(sc, account, currency); // поповнення рахунку
			if(adding == false) decreaseAccount(sc, account, currency); // знімання з рахунку
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	public static void increaseAccount(Scanner sc, Account account, ExchangeRate currency) { // поповнення рахунку
		System.out.println("Вкажіть суму поповнення рахунку: ");
		double sum =  sc.nextDouble(); // сума зміни рахунку
		account.increase(sum, currency); // змінюємо суму на рахунку (метод в класі Account)
		em.merge(account);
		Transaction transaction = new Transaction(sum, currency); // створюємо новий об'єкт транзакції, який описує зміну рахунку
		transaction.setAccountTo(account.getId());
		em.persist(transaction);
	}
	public static void decreaseAccount(Scanner sc, Account account, ExchangeRate currency) { // знімання з рахунку
		System.out.println("Вкажіть суму, яка знімається з рахунку: ");
		double sum =  sc.nextDouble(); // сума зміни рахунку
		if(account.decrease(sum, currency)) { // змінюємо суму на рахунку (метод в класі Account), якщо на рахунку достатня сума
			em.merge(account);
			Transaction transaction = new Transaction(sum, currency); // створюємо новий об'єкт транзакції, який описує зміну рахунку
			transaction.setAccountFrom(account.getId());
			em.persist(transaction);
		}
	}
	
	public static void move(Scanner sc) { // перекидання грошей з одного рахунку на інший
		em.getTransaction().begin();
		try {
			System.out.println("Вкажіть id рахунку, з якого знімаються гроші: ");
			int accountFromId = sc.nextInt();
			Account accountFrom = em.find(Account.class, accountFromId); 
			System.out.println(accountFrom);
			System.out.println("Вкажіть id рахунку, на який перекидаються гроші: ");
			int accountToId = sc.nextInt();
			Account accountTo = em.find(Account.class, accountToId); 
			System.out.println(accountTo);
			System.out.println("Вкажіть валюту операції: ");
			String sCurrency = sc.nextLine();
			sCurrency = sc.nextLine(); // чомусь при одному записі не дає ввести
			ExchangeRate currency = em.find(ExchangeRate.class, sCurrency);
			System.out.println("Вкажіть суму, яку хочете перекинути: ");
			double sum = sc.nextDouble();
			if(accountFrom.decrease(sum, currency)) { // змінюємо суму на рахунку (метод в класі Account), якщо на рахунку достатня сума
				accountTo.increase(sum, currency); // поповнюємо інший рахунок
				em.merge(accountFrom); // оновлюємо дані рахунку в базі
				em.merge(accountTo); // оновлюємо дані рахунку в базі
				Transaction transaction = new Transaction(sum, currency); // створюємо новий об'єкт транзакції, який описує зміну рахунку
				transaction.setAccountFrom(accountFromId); // додаємо в транзакцію id рахунку, з якого знімаємо гроші
				transaction.setAccountTo(accountToId); // додаємо в транзакцію id рахунку, на який перекидаємо гроші
				em.persist(transaction);
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void convert(Scanner sc) { // зміна валюти на рахунку
		em.getTransaction().begin();
		try {
			System.out.println("Вкажіть id рахунку, на якому змінюється валюта: ");
			int accountId = sc.nextInt();
			Account account = em.find(Account.class, accountId); // знаходимо об'єкт рахунку
			double sum = account.getSum(); // сума на рахунку
			System.out.println(account);
			System.out.println("Вкажіть id валюти, на яку хочете перетворити рахунок (великими літерами): ");
			String currencyId2 = sc.nextLine();
			currencyId2 = sc.nextLine(); // чомусь при одному записі не дає можливості введення
			ExchangeRate er2 = em.find(ExchangeRate.class, currencyId2); // змінена валюта рахунку
			ExchangeRate er1 = account.getCurrency(); // курс попередньої валюти
			sum = sum * er1.getExchangeRate() / er2.getExchangeRate(); // перетворена сума
			account.setSum(sum); // зберігаємо перетворену суму
			account.setCurrency(er2); // зберігаємо іншу валюту
			em.merge(account); // перезаписуємо рахунок в базі
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void countAllSum(Scanner sc) { // підрахунок загальної суми на рахунках клієнта
		System.out.println("Введіть id клієнта: ");
		int clientId = sc.nextInt();
		Client client = em.find(Client.class, clientId);
		System.out.println(client);
		System.out.println("Вкажіть id валюти, на яку хочете перетворити рахунки (великими літерами): ");
		String currencyId = sc.nextLine();
		currencyId = sc.nextLine(); // чомусь при одному записі не дає можливості введення
		ExchangeRate currency = em.find(ExchangeRate.class, currencyId);
		double sum = 0.00; // загальна сума рахунків клієнта
		for(Account a: client.getAccounts()) { // перебираємо усі рахунки клієнта
			sum += a.getSum() * a.getCurrency().getExchangeRate() / currency.getExchangeRate();
		}
		System.out.println("Загальна сума = " + sum + " грн.");
	}
	
	public static void getTransactionList(Scanner sc) { // виведення списку транзакцій
		System.out.println("Вкажіть id рахунку, для якого хочете вивести список транзакцій: ");
		int accountId = sc.nextInt();
		Query query = em.createQuery("SELECT t FROM Transaction t", Transaction.class); // отримуємо список усіх транзакцій
		List<Transaction> transactionList = (List<Transaction>) query.getResultList();
		// фільтруємо лише ті, які відбуваються з цим рахунком, використовуючи інтерфейс Predicate
		transactionList.removeIf(t -> !(t.getAccountFrom() == accountId || t.getAccountTo() == accountId));
		for(Transaction t: transactionList) System.out.println(t);
	}
}