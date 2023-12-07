package test.b_Money;

import static org.junit.Assert.*;

import b_Money.Currency;
import b_Money.Money;
import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() { //tests getting the amount
		assertEquals(Double.valueOf(10000), SEK100.getAmount());
		assertEquals(Double.valueOf(1000), EUR10.getAmount());
	}

	@Test
	public void testGetCurrency() { //tests getting the currency
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
	}

	@Test
	public void testToString() { //testing getting the showing the info as string
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("1000.0 EUR", EUR10.toString());
		assertEquals("0.0 SEK", SEK0.toString());
	}

	@Test
	public void testGlobalValue() { // testing the conversion of this currency into the global one
		assertEquals(Integer.valueOf(1500), SEK100.universalValue());
		assertEquals(Integer.valueOf(1500), EUR10.universalValue());
	}

	@Test
	public void testEqualsMoney() { //testing comparing different currencies
		assertTrue(SEK100.equals(EUR10));
		assertFalse(SEK200.equals(EUR0));
	}

	@Test
	public void testAdd() { // testing addition of different money
		assertEquals(Double.valueOf(2000), SEK100.add(EUR10).getAmount());
		assertEquals(EUR, EUR0.add(SEK200).getCurrency());
	}

	@Test
	public void testSub() { //testing subtraction
		assertEquals(Double.valueOf(19000), SEK200.sub(EUR10).getAmount());
		assertEquals(EUR, EUR0.sub(SEK100).getCurrency());

	}

	@Test
	public void testIsZero() { //testing evaluation of 0
		assertTrue(SEK0.isZero());
		assertFalse(SEK100.isZero());
	}

	@Test
	public void testNegate() { //testing negation of money
		assertEquals(-1000.0, EUR10.negate().getAmount(), 0.01);
		assertEquals(EUR, EUR10.negate().getCurrency());
	}

	@Test
	public void testCompareTo() { //testing comparing different values of money
		assertTrue(SEK100.compareTo(SEK200) < 0);
		assertTrue(EUR10.compareTo(EUR20) < 0);
		assertTrue(SEK100.compareTo(EUR10) > 0);
		assertEquals(0, SEK100.compareTo(EUR10));
	}
}
