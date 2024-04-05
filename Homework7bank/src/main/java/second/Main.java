package second;

import java.time.LocalDateTime;
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
			emf = Persistence.createEntityManagerFactory("Homework7bank");
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
				System.out.println(client);
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
			Client client = new Client(name, phone);
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
			System.out.println(name);
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
			String accountId = sc.nextLine();
			accountId = sc.nextLine(); // чомусь при одному записі не дає ввести ідентифікатор валюти
			ExchangeRate currency = em.find(ExchangeRate.class, accountId); // знаходимо курс валюти рахунку у таблиці
			System.out.println("Вкажіть початкову суму на рахунку (у вигляді десяткового дробу): ");
			double sum = sc.nextDouble();
			System.out.println("Вкажіть id клієнта: ");
			int clientId = sc.nextInt();
			Client client = em.find(Client.class, clientId); // знаходимо власника рахунку
			Account account = new Account(sum, currency.getCurrency(), client.getId()); // створюємо новий рахунок
			em.persist(account); // заводимо рахунок у базу
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
			boolean adding = true; // поповнення рахунку (знімання false)
			System.out.println("Якщо бажаєте поповнити рахунок, введіть 1 (за замовчуванням - знімання грошей): ");
			String sadding = sc.nextLine();
			sadding = sc.nextLine(); // чомусь при одному записі не дає ввести
			if(!sadding.equals("1")) adding = false;
			double sum = 0.00; // сума зміни рахунку
			Transaction tr = new Transaction(); // створюємо новий об'єкт транзакції, який описує зміну рахунку
			tr.setCurrencyId(account.getCurrencyId()); // валюта транзакції
			tr.setDateTime(LocalDateTime.now()); // вказуємо теперішній час
			if(adding == true) {
				System.out.println("Вкажіть суму поповнення рахунку: ");
				sum =  sc.nextDouble();
				account.setSum(account.getSum() + sum);
				tr.setFromAccountId(0);
				tr.setToAccountId(account.getId());
			}
			if(adding == false) {
				System.out.println("Вкажіть суму, яка знімається з рахунку: ");
				sum =  sc.nextDouble();
				account.setSum(account.getSum() - sum);
				tr.setFromAccountId(account.getId());
				tr.setToAccountId(0);
			}
			tr.setSum(sum); // сума транзакції
			em.persist(tr); // вставляємо нову транзакцію в базу
			em.merge(account); // оновлюємо дані рахунку в базі
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void move(Scanner sc) { // перекидання грошей з одного рахунку на інший
		em.getTransaction().begin();
		try {
			System.out.println("Вкажіть id рахунку, з якого знімаються гроші: ");
			int accountId1 = sc.nextInt();
			Account account1 = em.find(Account.class, accountId1); 
			System.out.println(account1);
			System.out.println("Вкажіть id рахунку, на який перекидаються гроші: ");
			int accountId2 = sc.nextInt();
			Account account2 = em.find(Account.class, accountId2); 
			System.out.println(account2);
			String currency1 = account1.getCurrencyId(); // id валюти 1 рахунку
			String currency2 = account2.getCurrencyId(); // id валюти 2 рахунку
			double sum = 0.00; // сума, яка перекидається (у валюті 1 рахунку)
			double sum2 = sum; // сума у валюті 2 рахунку
			do {
				System.out.println("Вкажіть суму, яку хочете перекинути: ");
				sum = sc.nextDouble();
				if(sum > account1.getSum()) System.out.println("Недостатньо грошей на рахунку.");
			} while(sum > account1.getSum()); // повертаємо назад, поки користувач не введе меншу суму
			if(!account1.getCurrencyId().equals(account2.getCurrencyId())) { // якщо валюти рахунків не співпадають
				ExchangeRate er1 = em.find(ExchangeRate.class, account1.getCurrencyId());
				ExchangeRate er2 = em.find(ExchangeRate.class, account2.getCurrencyId());
				sum2 = sum * er1.getExchangeRate() / er2.getExchangeRate(); // перетворюємо суму згідно курсів
			}
			Transaction tr = new Transaction(); // створюємо новий об'єкт транзакції, який описує зміну рахунку
			tr.setCurrencyId(account2.getCurrencyId()); // валюта транзакції
			tr.setSum(sum2); // сума транзакції
			tr.setDateTime(LocalDateTime.now()); // вказуємо теперішній час
			tr.setFromAccountId(account1.getId()); // вказуємо рахунок з якого знімаються кошти
			tr.setToAccountId(account2.getId()); // вказуємо рахунок на який перекидаються кошти
			account1.setSum(account1.getSum() - sum); // знімання коштів з 1 рахунку
			account2.setSum(account2.getSum() + sum2); // поповнення 2 рахунку
			em.merge(account1);
			em.merge(account2);
			em.persist(tr);
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
			ExchangeRate er1 = em.find(ExchangeRate.class, account.getCurrencyId()); // курс попередньої валюти
			account.setCurrencyId(currencyId2); // змінюємо валюту рахунку
			sum = sum * er1.getExchangeRate() / er2.getExchangeRate(); // перетворена сума
			account.setSum(sum); // зберігаємо перетворену суму
			em.merge(account); // перезаписуємо рахунок в базі
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
		}
	}
	
	public static void countAllSum(Scanner sc) { // підрахунок загальної суми на рахунках клієнта
		System.out.println("Введіть id клієнта: ");
		int clientId = sc.nextInt();
		List<Account> list = null; // список усіх рахунків бази
		try {
			Query query = em.createQuery("SELECT x FROM Account x", Account.class); // запит на отримання усіх транзакцій
			list = (List<Account>) query.getResultList();
		} catch (NoResultException ex) {
			System.out.println("Client not found!");
		} catch (NonUniqueResultException ex) {
			System.out.println("Non unique result!");
		}
		double s = 0; // загальна сума усіх рахунків клієнта (в перерахуванні на гривні)
		for(Account a: list) {
			if(a.getClientId() == clientId) {
				System.out.println(a); // виводимо усі рахунки клієнта
				ExchangeRate ex = em.find(ExchangeRate.class, a.getCurrencyId()); // курс валюти рахунку
				s += a.getSum() * ex.getExchangeRate(); // додаємо до загальної суму рахунку, перераховану на гривні
			}
		}
		System.out.println("Загальна сума = " + s + " грн.");
	}
	
	public static void getTransactionList(Scanner sc) { // виведення списку транзакцій
		System.out.println("Вкажіть id рахунку, для якого хочете вивести список транзакцій.");
		System.out.println("За замовчуванням виведеться список усіх транзакцій.");
		String saccountId = sc.nextLine();
		List<Transaction> list = null; // список усіх транзакцій у базі, з яких виберемо потрібні
		try {
			Query query = em.createQuery("SELECT x FROM Transaction x", Transaction.class); // запит на отримання усіх транзакцій
			list = (List<Transaction>) query.getResultList();
		} catch (NoResultException ex) {
			System.out.println("Client not found!");
		} catch (NonUniqueResultException ex) {
			System.out.println("Non unique result!");
		}
		if(saccountId.equals("")) {
			for(Transaction t: list) System.out.println(t); // за замовчуванням виводимо усі транзакції
		}
		else {
			int accountId = Integer.getInteger(saccountId);
			for(Transaction t: list) {
				if(t.getFromAccountId() == accountId || t.getToAccountId() == accountId) {
					System.out.println(t); // виводимо усі транзакції, які стосуються цього рахунку
				}
			}
		}
	}
}