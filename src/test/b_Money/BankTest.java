package test.b_Money;

import static org.junit.Assert.*;

import b_Money.*;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;

	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() { //seeing if the name of the bank is returned correctly
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() { //tests if the currency of the bank is returned correctly
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException{ //tests if it's possible to open an account
		//tests for account that doesn't exist yet
		SweBank.openAccount("Alice");
		assertTrue(SweBank.accountExists("Alice"));

		//tests for account that exists already
		assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Bob"));


	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException{ //testing the depositing function
		//Existing Account
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertEquals(Double.valueOf(10), SweBank.getBalance("Ulrika"));

		// Testing for a non-existing account
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.deposit("Charlotte",
				new Money(1000, SEK)));

	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException, NotEnoughFundsException {
		//testing the withdrawing function

		// Testing existing account
		SweBank.deposit("Bob", new Money(2000, SEK));
		SweBank.withdraw("Bob", new Money(2000, SEK));
		assertEquals(Double.valueOf(0), SweBank.getBalance("Bob"));

		// Testing for a non-existing account
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.withdraw("Alice",
				new Money(1000, SEK)));

	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		//tests getting the current balance of the customer

		//If account exists
		assertEquals(Double.valueOf(0), SweBank.getBalance("Ulrika"));

		// If account doesn't exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.getBalance("Saruman"));

	}

	@Test
	public void testTransfer()
			throws AccountDoesNotExistException, NotEnoughFundsException { //testing transferring function

		//if account does exist
		SweBank.deposit("Bob", new Money(3000, SEK));
		SweBank.transfer("Bob", SweBank,"Ulrika", new Money(3000, SEK));
		assertEquals(Double.valueOf(30), SweBank.getBalance("Ulrika"));
		assertEquals(Double.valueOf(0), SweBank.getBalance("Bob"));

		//if account doesn't exist
		assertThrows(AccountDoesNotExistException.class, () ->
				SweBank.transfer("Alice", SweBank,"Bob", new Money(1000, SEK)));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, NotEnoughFundsException {

		//if account does exist
		SweBank.deposit("Bob", new Money(1000, SEK));
		SweBank.addTimedPayment("Bob", "Book club fee",3,
				1, new Money(1000, SEK), DanskeBank, "Gertrud");
		SweBank.tick();
		SweBank.tick();


		assertEquals(Double.valueOf(0), SweBank.getBalance("Bob"));
		assertEquals(Double.valueOf(7), DanskeBank.getBalance("Gertrud"));

		//if account does not exist
		SweBank.deposit("Bob", new Money(1000, SEK));
		assertThrows(AccountDoesNotExistException.class, () ->
				SweBank.addTimedPayment("Bob", "Book club fee",2,
						3, new Money(500, DKK), DanskeBank, "Agnes"));


	}
}

