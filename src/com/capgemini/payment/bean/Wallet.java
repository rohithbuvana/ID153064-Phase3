package com.capgemini.payment.bean;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
@Embeddable
public class Wallet {
	private BigDecimal balance;

	public Wallet(BigDecimal amount) {
		this.balance=amount;
	}

	public Wallet() {
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
		public String toString() {
		return ", balance="+balance;
	}
}
