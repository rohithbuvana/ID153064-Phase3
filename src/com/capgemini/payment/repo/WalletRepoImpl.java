package com.capgemini.payment.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.capgemini.payment.bean.Customer;


public class WalletRepoImpl implements WalletRepo {
	

	Connection con;
	public WalletRepoImpl() {
	}

	

	public boolean save(Customer customer) {
		if(findOne(customer.getMobileNo())==null)
		{
			EntityManagerFactory emf=Persistence.createEntityManagerFactory("PaymentWithJPA");
		 EntityManager em=emf.createEntityManager();
			EntityTransaction tx=em.getTransaction();
			tx.begin();
			em.persist(customer);
			tx.commit();
			em.close();
			return true;
			
		}
		else 
			{
			EntityManagerFactory emf=Persistence.createEntityManagerFactory("PaymentWithJPA");
			 EntityManager em=emf.createEntityManager();
				EntityTransaction tx=em.getTransaction();
				tx.begin();
				em.merge(customer);
				tx.commit();
				em.close();
				return true;
			}
			
	}
	

	public Customer findOne(String mobileNo) {
		Customer customer=new Customer();
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("PaymentWithJPA");
		 EntityManager em=emf.createEntityManager();
			EntityTransaction tx=em.getTransaction();
			tx.begin();
			customer=em.find(Customer.class,mobileNo);
			tx.commit();
			em.close();
			if (customer==null) {
				return null;
			}
		
			return customer;
		
	}
}

