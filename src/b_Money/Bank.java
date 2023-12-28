package b_Money;

import java.util.Hashtable;

public class Bank {
	private Hashtable<String, Account> accountlist = new Hashtable<>();
	private String name;
	private Currency currency;

	/**
	 * New Bank
	 * @param name Name of this bank
	 * @param currency Base currency of this bank (If this is a Swedish bank,
	 * this might be a currency class representing SEK)
	 */
    public Bank(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}

	/**
	 * Get the name of this bank
	 * @return Name of this bank
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the Currency of this bank
	 * @return The Currency of this bank
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Open an account at this bank.
	 * @param accountid The ID of the account
	 * @throws AccountExistsException If the account already exists
	 */
	public void openAccount(String accountid) throws AccountExistsException {
		if (accountlist.containsKey(accountid)) {
			throw new AccountExistsException();
		} else {
			accountlist.put(accountid, new Account(this.currency)); //didnt add the account before, now does
		}
	}

	/**
	 * Deposit money to an account
	 * @param accountid Account to deposit to
	 * @param money Money to deposit.
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void deposit(String accountid, Money money) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) { //should add "!" in the front to mean does *not* exist
			throw new AccountDoesNotExistException();
		} else {
			Account account = accountlist.get(accountid);
			account.deposit(money);
		}
	}

	/**
	 * Withdraw money from an account
	 * @param accountid Account to withdraw from
	 * @param money Money to withdraw
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void withdraw(String accountid, Money money) throws AccountDoesNotExistException, NotEnoughFundsException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		} else {
			Account account = accountlist.get(accountid);
			account.withdraw(money);
		}
	}

	/**
	 * Get the balance of an account
	 *
	 * @param accountid Account to get balance from
	 * @return Balance of the account
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public Double getBalance(String accountid) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		} else {
			return accountlist.get(accountid).getBalance().getAmount();
		}
	}

	/**
	 * Transfer money between two accounts within the same bank
	 * @param fromAccount Id of account to deduct from
	 * @param toAccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts does not exist
	 * @throws NotEnoughFundsException If there are not enough funds in the source account
	 */
	private void transferWithinBank(String fromAccount, String toAccount, Money amount)
			throws AccountDoesNotExistException, NotEnoughFundsException {
		if (!accountExists(fromAccount) || !accountExists(toAccount)) {
			throw new AccountDoesNotExistException();
		}

		accountlist.get(fromAccount).withdraw(amount);
		accountlist.get(toAccount).deposit(amount);
	}

	/**
	 * Transfer money between two accounts
	 * @param fromAccount Id of account to deduct from in this Bank
	 * @param toBank Bank where receiving account resides
	 * @param toAccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts does not exist
	 * @throws NotEnoughFundsException If there are not enough funds in the source account
	 */
	public void transfer(String fromAccount, Bank toBank, String toAccount, Money amount)
			throws AccountDoesNotExistException, NotEnoughFundsException {
		if (this == toBank) {
			transferWithinBank(fromAccount, toAccount, amount);
		} else {
			if (!accountExists(fromAccount) || !toBank.accountExists(toAccount)) {
				throw new AccountDoesNotExistException();
			}

			accountlist.get(fromAccount).withdraw(amount);
			toBank.accountlist.get(toAccount).deposit(amount);
		}
	}

	/**
	 * Add a timed payment
	 * @param accountid Id of account to deduct from in this Bank
	 * @param payid Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String accountid, String payid, Integer interval,
								Integer next, Money amount, Bank tobank, String toaccount)
			throws AccountDoesNotExistException {
		if(!tobank.accountExists(toaccount) || !this.accountlist.containsKey(accountid))
		    throw new AccountDoesNotExistException();
		Account account = accountlist.get(accountid);
		account.addTimedPayment(payid, interval, next, amount, tobank, toaccount);
	}

	/**
	 * Remove a timed payment
	 * @param accountid Id of account to remove timed payment from
	 * @param id Id of timed payment
	 */
	public void removeTimedPayment(String accountid, String id) {
		Account account = accountlist.get(accountid);
		account.removeTimedPayment(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() throws AccountDoesNotExistException, NotEnoughFundsException{
		for (Account account : accountlist.values()) {
			account.tick();
		}
	}

	/**
	 * Checks if an account with the provided is exists in this bank
	 * @param id Id of the desired account
	 * @return boolean value of True or False depending on whether this account exists
	 */
	public boolean accountExists(String id){
		//added this method to make checking for the existence of account easier
		return accountlist.containsKey(id);
	}
}
