import model.Room;
import repository.RoomRepositoryImpl;
import service.RoomService;
import service.RoomServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final RoomService roomService = new RoomServiceImpl(new RoomRepositoryImpl());
    private static boolean isBlocked = true;
    private static boolean isUnderRenovation = true;

    public static void main(String[] args) {
        // Перед первым запуском обязательно создать схему senla_traineeship и таблицу rooms из соответствующего файла!

        System.out.println("Добро пожаловать в тест программы по созданию номеров. Введите числа от 1 до 6 где:\n" +
                "1 - создать новый номер.\n" +
                "2 - получить список всех номеров.\n" +
                "3 - получить номер по его id.\n" +
                "4 - обновить номер по его id.\n" +
                "5 - удалить номер по id.\n" +
                "6 - удалить все номера.\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int input = scanner.nextInt();

            if (input == 1) {
                create();
            } else if (input == 2) {
                getAllRooms().forEach(System.out::println);
            } else if (input == 3) {
                System.out.println(getRoomById());
            } else if (input == 4) {
                updateById();
            } else if (input == 5) {
                deleteById();
            } else if (input == 6) {
                deleteAll();
            } else {
                System.out.println("Ваше число не входит в заданный диапазон.");
            }
        }
    }

    private static Room getRoomById() {
        System.out.println("Введите id номер:");
        int id = new Scanner(System.in).nextInt();
        return roomService.getRoomById(id);
    }

    private static List<Room> getAllRooms() {
        System.out.println("Короткий список всех номеров:");
        return roomService.getAllRooms();
    }

    private static void updateById() {
        boolean isBlocked = true;
        boolean isUnderRenovation = true;
        System.out.println("Введите id нового номер:");
        int id = new Scanner(System.in).nextInt();

        System.out.println("Номер свободен? y/n:");
        String blocked = new Scanner(System.in).nextLine().toLowerCase();
        if (blocked.equals("y")) {
            isBlocked = false;
        }

        System.out.println("Номер на ремонте? y/n:");
        String underRenovation = new Scanner(System.in).nextLine().toLowerCase();
        if (underRenovation.equals("n")) {
            isUnderRenovation = false;
        }

        System.out.println("Введите цену номера:");
        int price = new Scanner(System.in).nextInt();

        roomService.updateRoomById(new Room(id, isBlocked, isUnderRenovation, price));
        System.out.println(roomService.getRoomById(id));
    }

    private static void deleteAll() {
        roomService.deleteAllRooms();
        System.out.println("Все номера удалены.");
    }

    private static void deleteById() {
        System.out.println("Введите id номера:");
        int id = new Scanner(System.in).nextInt();
        roomService.deleteRoomById(id);
    }

    private static void create() {
        System.out.println("Введите id нового номер:");
        int id = new Scanner(System.in).nextInt();

        System.out.println("Номер свободен? y/n:");
        String blocked = new Scanner(System.in).nextLine().toLowerCase();
        if (blocked.equals("y")) {
            isBlocked = false;
        }

        System.out.println("Номер на ремонте? y/n:");
        String underRenovation = new Scanner(System.in).nextLine().toLowerCase();
        if (underRenovation.equals("n")) {
            isUnderRenovation = false;
        }

        System.out.println("Введите цену номера:");
        int price = new Scanner(System.in).nextInt();

        if (roomService.createRoom(new Room(id, isBlocked, isUnderRenovation, price)) != null) {
            System.out.println("Комната успешно создана.");
        }
    }
}
