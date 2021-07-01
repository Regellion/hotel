package repository;

import model.Room;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Room saveRoom(Room room) {
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(room);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
        }
        return room;
    }

    @Override
    public Room getRoomById(Long id) {
        Room room = null;
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            room = session.get(Room.class, id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            rooms = session.createQuery("FROM Room").getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return rooms;
    }

    @Override
    public void deleteRoomById(Long id) {
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Room room = session.get(Room.class, id);
            session.createQuery("UPDATE Room SET deleteTime = sysdate() WHERE id = " + room.getId()).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }

    @Override
    public void deleteAllRooms() {
        long start = System.currentTimeMillis();
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("UPDATE Room SET deleteTime = sysdate()").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }

    @Override
    public Room updateRoomById(Room room) {
        long start = System.currentTimeMillis();
        Room tempRoom = null;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(room);
            tempRoom = session.get(Room.class, room.getId());
            session.getTransaction().commit();
            return tempRoom;
        } catch (HibernateException e) {
            factory.getCurrentSession().getTransaction().rollback();
            System.err.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
        return tempRoom;
    }
}
