package repository;

import model.Room;
import utils.MysqlUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {
    private static final Connection connection = MysqlUtil.getConnection();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Room saveRoom(Room room) {
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO senla_traineeship.rooms (id, room_is_booked, room_status, room_price) VALUES ("
                    + room.getId() + ", " + room.isBlocked() + ", " + room.isUnderRenovation() + ", " + room.getPrice() + ")");
        } catch (SQLException e) {
            System.out.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error adding data to the database. " + e);
        }
        return room;
    }

    @Override
    public Room getRoomById(Integer id) {
        Room room = null;
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM senla_traineeship.rooms WHERE id = " + id);
            while (resultSet.next()) {
                room = new Room(id, resultSet.getBoolean("room_is_booked"), resultSet.getBoolean("room_status"), resultSet.getInt("room_price"));
            }
        } catch (SQLException e) {
            System.out.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM senla_traineeship.rooms");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Boolean isBooked = resultSet.getBoolean("room_is_booked");
                Boolean isUnderRenovation = resultSet.getBoolean("room_status");
                Integer price = resultSet.getInt("room_price");
                rooms.add(new Room(id, isBooked, isUnderRenovation, price));
            }
        } catch (SQLException e) {
            System.out.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error retrieving data from the database. " + e);
        }
        return rooms;
    }

    @Override
    public void deleteRoomById(Integer id) {
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM senla_traineeship.rooms WHERE id = " + id);
        } catch (SQLException e) {
            System.out.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }

    @Override
    public void deleteAllRooms() {
        long start = System.currentTimeMillis();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM senla_traineeship.rooms");
        } catch (SQLException e) {
            System.out.println(formatter.format(start) + " ERROR in class " + this.getClass().getName() + ": Error deleting data from the database. " + e);
        }
    }
}
