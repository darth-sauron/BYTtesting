package test.b_Money;

import b_Money.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK;
	Bank SweBank;
	Account testAccount;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account(SEK);
		testAccount.deposit(new Money(10000000, SEK));
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	@Test
	public void testAddRemoveTimedPayment() { //test add and remove timed payments
		Money paymentAmount = new Money(50, SEK);

		//adding
		testAccount.addTimedPayment("payment1", 100, 200, paymentAmount, SweBank,
				"Alice");
		assertTrue(testAccount.timedPaymentExists("payment1"));

		//removing
		testAccount.removeTimedPayment("payment1");
		assertFalse(testAccount.timedPaymentExists("payment1"));



	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, NotEnoughFundsException {
		testAccount.addTimedPayment("payment1", 1, 1, new Money(10000000, SEK),
				SweBank, "Alice");

		testAccount.tick();
		testAccount.tick();

		assertEquals(Double.valueOf(0), testAccount.getBalance().getAmount());

		//testing for when there's not enough money
		testAccount.addTimedPayment("payment2", 1, 1, new Money(10000000, SEK),
				SweBank, "Alice");
		fail("Not enough funds");

	}

	@Test
	public void testAddWithdraw() throws NotEnoughFundsException { //testing withdrawing money
		testAccount.withdraw(new Money(10000000, SEK));
		assertEquals(Double.valueOf(0), testAccount.getBalance().getAmount());

	}

	@Test
	public void testGetBalance() { //testing getting the balance of account
		assertEquals(Double.valueOf(100000), testAccount.getBalance().getAmount());
	}
}
