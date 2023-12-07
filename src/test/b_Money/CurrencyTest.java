package test.b_Money;

import static org.junit.Assert.*;

import b_Money.Currency;
import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() { //testing getting the name of currency
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());

	}
	
	@Test
	public void testGetRate() { //testing getting the rate of currency
		assertEquals(Double.valueOf(0.15), SEK.getRate());
		assertEquals(Double.valueOf(0.20), DKK.getRate());
		assertEquals(Double.valueOf(1.5), EUR.getRate());
	}
	
	@Test
	public void testSetRate() { //testing changing the rate of the currency
		SEK.setRate(0.25);
		assertEquals(Double.valueOf(0.25), SEK.getRate());
	}
	
	@Test
	public void testGlobalValue() { //testing converting the currency into global value
		assertEquals(Integer.valueOf(15), SEK.universalValue(100));
		assertEquals(Integer.valueOf(20), DKK.universalValue(100));
		assertEquals(Integer.valueOf(150), EUR.universalValue(100));
	}
	
	@Test
	public void testValueInThisCurrency() { //testing converting currencies
		assertEquals(Integer.valueOf(75), SEK.valueInThisCurrency(100, DKK));
		assertEquals(Integer.valueOf(13), DKK.valueInThisCurrency(100, EUR));
	}

}
