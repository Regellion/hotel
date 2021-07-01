package repository;

import model.Booking;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.*;


public class BookingRepositoryImpl implements BookingRepository {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Booking saveBooking(Booking booking) {
        long start = System.currentTimeMillis();
        Booking tempBooking = null;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(booking);
            tempBooking = session.get(Booking.class, booking.getId());
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
        }
        return tempBooking;
    }

    @Override
    public Booking getBookingById(Long id) {
        Booking booking = null;
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            booking = session.get(Booking.class, id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUserId(Long id) {
        List<Booking> bookings = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            bookings = session.createQuery("FROM Booking WHERE user.id = " + id).getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByRoomId(Long id) {
        List<Booking> bookings = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            bookings = session.createQuery("FROM Booking WHERE room.id = " + id).getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return bookings;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            bookings = session.createQuery("FROM Booking ").getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return bookings;
    }

    @Override
    public void deleteBookingById(Long id) {
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Booking booking = session.get(Booking.class, id);
            session.createQuery("UPDATE Booking SET deleteTime = sysdate() WHERE id = " + booking.getId()).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
        }
    }

    @Override
    public void deleteAllBookings() {
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("UPDATE Booking SET deleteTime = sysdate()").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }
}
