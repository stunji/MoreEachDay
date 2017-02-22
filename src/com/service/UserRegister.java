package com.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.hibernate.HibernateUtil;
import com.model.User;

public class UserRegister {

	public static boolean addUser(String username, String email, String password) {
		
		System.err.println("asjlDADJASKLFJADSKLFJSDKLFJASDFKLASDJFKLSDFJSDKLFJSDKLFJ");
		System.out.println(username);

		Session session = HibernateUtil.openSession();
		if (userExists(username, email) || !validateEmail(email)) {
			return false;
		}

		Transaction t = null;
		try {
			t = session.beginTransaction();

			session.saveOrUpdate(new User(username, email, password));
			t.commit();

		} catch (HibernateException e) {
			if (t != null) {
				t.rollback();
			}
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	private static boolean validateEmail(String email) {
		return email.matches("\\S+@\\S+\\.\\S+") && email.length() >= 5;
	}

	private static boolean userExists(String username, String email) {
		Session session = HibernateUtil.openSession();
		boolean result = false;

		Transaction t = null;
		try {
			t = session.beginTransaction();
			Query query = session.createQuery("from User where username='" + username + "'");
			Object u = (Object) query.uniqueResult();
			t.commit();
			if (u != null) {
				result = true;
			}
		} catch (HibernateException e) {
			if (t != null) {
				t.rollback();
			}
		} finally {
			session.close();
		}
		return result;
	}
}