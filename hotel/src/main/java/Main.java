import dto.BookingDto;
import dto.RoomDto;
import dto.UserDto;
import service.*;
import utils.DTOMaker;

import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final RoomService roomService = new RoomServiceImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final BookingService bookingService = new BookingServiceImpl();

    public static void main(String[] args) {


        printInfo("Добро пожаловать в тест программы по созданию номеров.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printInfo("Введите числа от 1 до 19 где:\n" +
                    "1 - создать новый номер.\n" +
                    "2 - получить список всех номеров.\n" +
                    "3 - получить номер по его id.\n" +
                    "4 - обновить номер по его id.\n" +
                    "5 - удалить номер по id.\n" +
                    "6 - удалить все номера.\n" +
                    "7 - создать нового пользователя.\n" +
                    "8 - получить список всех пользователей.\n" +
                    "9 - получить пользователя по его id.\n" +
                    "10 - обновить информацию о пользователе.\n" +
                    "11 - удалить пользователя по его id.\n" +
                    "12 - удалить всех пользователей.\n" +
                    "13 - забронировать номер.\n" +
                    "14 - посмотреть все бронирования.\n" +
                    "15 - очистить список бронирования.\n" +
                    "16 - получить список всех бронирований для номера.\n" +
                    "17 - получить список всех бронирований для пользователя.\n" +
                    "18 - получить бронирование по id.\n" +
                    "19 - удалить бронирование по id.\n");

            int input = scanner.nextInt();

            if (input == 1) {
                createRoom();
            } else if (input == 2) {
                getAllRooms();
            } else if (input == 3) {
                getRoomById();
            } else if (input == 4) {
                updateRoomById();
            } else if (input == 5) {
                deleteRoomById();
            } else if (input == 6) {
                deleteAllRooms();
            } else if (input == 7) {
                createUser();
            } else if (input == 8) {
                getAllUser();
            } else if (input == 9) {
                getUserById();
            } else if (input == 10) {
                updateUserById();
            } else if (input == 11) {
                deleteUserById();
            } else if (input == 12) {
                deleteAllUsers();
            } else if (input == 13) {
                createBooking();
            } else if (input == 14) {
                getAllBookings();
            } else if (input == 15) {
                deleteAllBookings();
            } else if (input == 16) {
                getBookingsByRoomId();
            } else if (input == 17) {
                getBookingsByUserId();
            } else if (input == 18) {
                getBookingById();
            } else if (input == 19) {
                deleteBookingById();
            } else {
                printInfo("Ваше число не входит в заданный диапазон.");
            }
        }
    }

    private static void deleteBookingById() {
        printInfo("Введите id бронирования:");
        long id = DTOMaker.setId();
        try {
            bookingService.deleteBookingById(id);
            printInfo("Бронирование с id " + id + " успешно удалено.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Бронирования с id " + id + " не существует!");
        }
    }

    private static void getBookingById() {
        printInfo("Введите id бронирования:");
        long id = DTOMaker.setId();
        try {
            printInfo(bookingService.getBookingById(id).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("бронирования с id " + id + " не существует!");
        }
    }

    private static void getBookingsByUserId() {
        printInfo("Введите id пользователя:");
        long userId = DTOMaker.setId();
        try {
            bookingService.getBookingByUserId(userId).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Для пользователя с id " + userId + " нет бронирований.");
        }
    }

    private static void getBookingsByRoomId() {
        printInfo("Введите номер комнаты:");
        long roomId = DTOMaker.setId();
        try {
            bookingService.getBookingByRoomId(roomId).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Для номера с id " + roomId + " нет бронирований.");
        }
    }

    private static void deleteAllBookings() {
        try {
            bookingService.deleteAllBookings();
            printInfo("Все бронирования успешно удалены.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список бронирований уже пуст.");
        }
    }

    private static void getAllBookings() {
        try {
            printInfo("Короткий список всех бронирований:");
            bookingService.getAllBookings().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список бронирований пуст");
        }
    }

    private static void createBooking() {
        printInfo("Введите id пользователя: ");
        long userId = DTOMaker.setId();
        printInfo("Введите номер комнаты, которую вы хотите забронировать: ");
        long roomId = DTOMaker.setId();

        printInfo("Введите дату заезда в ввиде: dd/mm/yyyy");
        try {
            Date startDate = DTOMaker.setDate();
            printInfo("Введите дату выезда:");
            Date endDate = DTOMaker.setDate();

            BookingDto bookingDto = new BookingDto(roomId, userId, startDate, endDate);
            try {
                bookingService.saveBooking(bookingDto);
                printInfo("Бронирование прошло успешно.");
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //todo Понимаю что говнокод, и надо сделать свои exception'ы
                if (e.getMessage().equals("The room is unavailable.")) {
                    printInfo("Номер недоступен.");
                }
                if (e.getMessage().equals("Start date is after end date.")) {
                    printInfo("Дата отезда раньше даты заезда.");
                }
                if (e.getMessage().equals("Incorrect booking dates.")) {
                    printInfo("Номер занят! Выберите другие даты.");
                }
                printInfo("Бронирование не удалось! Попробуйте заново.");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            printInfo("Ввод даты неудался!");
        }
    }

    private static void deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            printInfo("Все пользователи успешно удалены.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список пользователей уже пуст.");
        }
    }

    private static void deleteUserById() {
        printInfo("Введите id пользователя:");
        long id = DTOMaker.setId();
        try {
            userService.deleteUserById(id);
            printInfo("Пользователь с id " + id + " успешно удален.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Пользователя с id " + id + " не существует!");
        }
    }

    private static void updateUserById() {
        printInfo("Введите id пользователя, информацию о котором нужно обновить:");
        long id = DTOMaker.setId();

        printInfo("Введите новое имя пользователя: ");
        String name = DTOMaker.setName();
        try {
            userService.updateUserById(id, new UserDto(name));
            printInfo("Обновление прошло успешно.");
            printInfo(userService.getUserById(id).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("пользователя с id " + id + " не существует!");
        }
    }

    private static void getUserById() {
        printInfo("Введите id пользователя:");
        long id = DTOMaker.setId();
        try {
            printInfo(userService.getUserById(id).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Пользователя с id " + id + " не существует!");
        }
    }

    private static void getAllUser() {
        try {
            userService.getAllUsers().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список пользователей пуст");
        }
    }

    private static void createUser() {
        printInfo("Введите имя пользователя: ");
        String name = DTOMaker.setName();
        try {
            userService.createUser(new UserDto(name));
            printInfo("Пользователь успешно создан.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Не удалось создать пользователя.");
        }
    }


    private static void getRoomById() {
        printInfo("Введите id номер:");
        long id = DTOMaker.setId();
        try {
            printInfo(roomService.getRoomById(id).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Комната под номером " + id + " не существует!");
        }
    }

    private static void getAllRooms() {
        try {
            printInfo("Короткий список всех номеров:");
            roomService.getAllRooms().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список номеров пуст");
        }
    }

    private static void updateRoomById() {
        boolean isUnderRenovation = true;
        printInfo("Введите id номера, информацию о котором нужно обновить:");
        long id = DTOMaker.setId();

        printInfo("Номер на ремонте? y/n:");
        String underRenovation = DTOMaker.setName().toLowerCase();
        if (underRenovation.equals("n")) {
            isUnderRenovation = false;
        }

        printInfo("Введите цену номера:");
        int price = DTOMaker.setPrice();
        try {
            roomService.updateRoomById(id, new RoomDto(isUnderRenovation, price));
            printInfo("Обновление прошло успешно.");
            printInfo(roomService.getRoomById(id).toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Комната под номером " + id + " не существует!");
        }
    }

    private static void deleteAllRooms() {
        try {
            roomService.deleteAllRooms();
            printInfo("Все номера успешно удалены.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printInfo("Список номеров уже пуст.");
        }
    }

    private static void deleteRoomById() {
        printInfo("Введите id номера:");
        long id = DTOMaker.setId();
        try {
            roomService.deleteRoomById(id);
            printInfo("Комната под номером " + id + " успешно удалена.");
        } catch (Exception e) {
            printError(e.getMessage());
            printInfo("Комната под номером " + id + " не существует!");
        }
    }

    private static void createRoom() {
        printInfo("Номер на ремонте? y/n:");
        String underRenovation = DTOMaker.setName().toLowerCase();
        boolean isUnderRenovation = !underRenovation.equals("n");

        printInfo("Введите цену номера:");
        int price = DTOMaker.setPrice();
        try {
            roomService.createRoom(new RoomDto(isUnderRenovation, price));
            printInfo("Комната успешно создана.");
        } catch (Exception e) {
            printError(e.getMessage());
            printInfo("Не удалось создать комнату.");
        }
    }

    private static void printInfo(String info) {
        System.out.println(info);
    }

    private static void printError(String errorText) {
        System.err.println(errorText);
    }
}
