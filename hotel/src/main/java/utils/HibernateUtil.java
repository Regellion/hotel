package utils;

import model.Booking;
import model.Room;
import model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.SimpleDateFormat;

public class HibernateUtil {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory = null;
        long start = System.currentTimeMillis();
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(Room.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Booking.class)
                    .buildSessionFactory();
        } catch (HibernateException e) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            System.out.println(formatter.format(start) + " ERROR in " + HibernateUtil.class + ": Error creating a database connection. " + e);
        }
        return sessionFactory;
    }
}
