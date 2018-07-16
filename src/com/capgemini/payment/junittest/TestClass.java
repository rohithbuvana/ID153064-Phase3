package com.capgemini.payment.junittest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exception.InsufficientBalanceException;
import com.capgemini.payment.exception.InvalidInputException;
import com.capgemini.payment.service.WalletService;
import com.capgemini.payment.service.WalletServiceImpl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Before;
public class TestClass {
WalletService service;
Connection con;
	@Before
	public  void initData(){
		service=new WalletServiceImpl();
		 Customer cust1=new Customer("Amit", "9900112212",new Wallet(new BigDecimal(9000)));
		 Customer cust2=new Customer("Ajay", "9963242422",new Wallet(new BigDecimal(6000)));
		 Customer cust3=new Customer("Yogini", "9922950519",new Wallet(new BigDecimal(7000)));
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("PaymentWithJPA");
		 EntityManager em=emf.createEntityManager();
			EntityTransaction tx=em.getTransaction();
			tx.begin();
			em.persist(cust1);
			em.persist(cust2);
			em.persist(cust3);
			tx.commit();
			em.close();
	}
	
	@Test
	public void testCreateAccount()
	{
		 Customer expectedCustomer=new Customer("Bachan", "9900112213",new Wallet(new BigDecimal(8000)));
		 Customer actualCustomer=service.createAccount("Bachan", "9900112213", new BigDecimal(8000));
		 assertEquals(expectedCustomer.getMobileNo(),actualCustomer.getMobileNo() );
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountMobileException()
	{
		 service.createAccount("Bachan", "9900112", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountMobileException2()
	{
		 service.createAccount("Bachan", "abcdefghij", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountMobileException3()
	{
		 service.createAccount("Bachan", "990011abc", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountNameException()
	{
		 service.createAccount("Bachan123", "990011221", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountNameException2()
	{
		 service.createAccount("Bachan@capgemini", "990011221", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountNameException3()
	{
		 service.createAccount("990011221", "990011221", new BigDecimal(8000));
	}
	@Test (expected=InvalidInputException.class)
	public void testCreateAccountAlreadyExistException()
	{
		 service.createAccount("Amit", "9900112212", new BigDecimal(9000));
	}
	@Test 
	public void testShowBalance()
	{
		Customer customer=service.showBalance("9900112212");
		 assertEquals("9000.00",customer.getWallet().getBalance().toString() );
	}
	@Test 
	public void testShowBalance2()
	{
		Customer customer=service.showBalance("9900112212");
		 assertEquals("Amit",customer.getName() );
	}
	@Test  (expected=InvalidInputException.class)
	public void testShowBalanceDoesNotException()
	{
		service.showBalance("9900112214");
	}
	@Test 
	public void testDepositAmount()
	{
		Customer customer=service.depositAmount("9900112212", new BigDecimal(9000));
		 assertEquals(service.showBalance("9900112212").getWallet().getBalance(),customer.getWallet().getBalance() );
	}
	@Test 
	public void testDepositAmount2()
	{
		Customer customer=service.depositAmount("9900112212", new BigDecimal(9000));
		 assertEquals(service.showBalance("9900112212").getName(),customer.getName());
	}
	@Test  (expected=InvalidInputException.class)
	public void testDepositAmountDoesNotExistsException()
	{
		service.depositAmount("9900112213", new BigDecimal(9000));
	}
	@Test 
	public void testWithdrawAmount()
	{
		Customer customer=service.withdrawAmount("9900112212", new BigDecimal(6000));
		 assertEquals(service.showBalance("9900112212").getWallet().getBalance(),customer.getWallet().getBalance() );
	}
	@Test  (expected=InvalidInputException.class)
	public void testWithdrawAmountDoesNotExistsException()
	{
		service.withdrawAmount("9900112213", new BigDecimal(9000));
	}
	@Test  (expected=InsufficientBalanceException.class)
	public void testWithdrawAmountInsufficientBalanceException()
	{
		service.withdrawAmount("9900112212", new BigDecimal(9001));
	}
	@Test
	public void testFundTransfer()
	{
		Customer customer=service.fundTransfer("9900112212", "9963242422",new BigDecimal(2000));
		assertEquals(service.showBalance("9900112212").getWallet().getBalance(),customer.getWallet().getBalance() );
	}
	@Test  (expected=InsufficientBalanceException.class)
	public void testFundTransferInsufficientBalanceException()
	{
		service.fundTransfer("9900112212", "9963242422",new BigDecimal(9001));
	}
	@Test  (expected=InvalidInputException.class)
	public void testFundTransferSourceNumberException()
	{
		service.fundTransfer("9900112213", "9963242422",new BigDecimal(2000));
	}
	@Test  (expected=InvalidInputException.class)
	public void testFundTransfertargetNumberException()
	{
		service.fundTransfer("9900112212", "9963242423",new BigDecimal(2000));
	}
	@Test
	public void testFundTransferDebitCheck()
	{
		service.fundTransfer("9900112212", "9963242422",new BigDecimal(2000));
		Customer customer=service.showBalance("9963242422");
		assertEquals("8000.00",customer.getWallet().getBalance().toString() );
	}
	@After
	public  void clear(){
		 Customer cust1=new Customer("Amit", "9900112212",new Wallet(new BigDecimal(9000)));
		 Customer cust2=new Customer("Ajay", "9963242422",new Wallet(new BigDecimal(6000)));
		 Customer cust3=new Customer("Yogini", "9922950519",new Wallet(new BigDecimal(7000)));
		 Customer cust4=new Customer("Bachan", "9900112213",new Wallet(new BigDecimal(8000)));
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("PaymentWithJPA");
		 EntityManager em=emf.createEntityManager();
			EntityTransaction tx=em.getTransaction();
			tx.begin();
			em.remove(em.merge(cust1));
			em.remove(em.merge(cust2));
			em.remove(em.merge(cust3));
			em.remove(em.merge(cust4));
			tx.commit();
			em.close();	
	}
}
