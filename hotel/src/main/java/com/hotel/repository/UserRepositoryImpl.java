package com.hotel.repository;

import com.hotel.model.User;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public User saveUser(User user) {
        long start = System.currentTimeMillis();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
            user = null;
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        long start = System.currentTimeMillis();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            users = session.createQuery("FROM User").getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return users;
    }

    @Override
    public void deleteUserById(Long id) {
        long start = System.currentTimeMillis();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.createQuery("UPDATE Booking SET deleteTime = sysdate() WHERE user.id = " + user.getId()).executeUpdate();
            session.createQuery("UPDATE User SET deleteTime = sysdate() WHERE id = " + user.getId()).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }

    @Override
    public void deleteAllUsers() {
        long start = System.currentTimeMillis();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("UPDATE Booking SET deleteTime = sysdate()").executeUpdate();
            session.createQuery("UPDATE User SET deleteTime = sysdate()").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }

    @Override
    public User updateUserById(User user) {
        long start = System.currentTimeMillis();
        User tempUser = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.update(user);
            tempUser = session.get(User.class, user.getId());
            session.getTransaction().commit();
            return tempUser;
        } catch (HibernateException e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
        return tempUser;
    }
}
