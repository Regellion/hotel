CREATE SCHEMA senla_traineeship DEFAULT CHARACTER SET utf8;

CREATE TABLE senla_traineeship.rooms
(
    id          INT NOT NULL AUTO_INCREMENT,
    room_status INT NOT NULL,
    room_price  INT,
    delete_time DATE,
    PRIMARY KEY (id)
);

CREATE TABLE senla_traineeship.users
(
    id          INT          NOT NULL AUTO_INCREMENT,
    user_name   VARCHAR(200) NOT NULL,
    login       VARCHAR(200) NOT NULL,
    password    VARCHAR(200) NOT NULL,
    role        VARCHAR(200) NOT NULL,
    delete_time DATE,
    PRIMARY KEY (ID)
);

CREATE TABLE senla_traineeship.bookings
(
    id          INT  NOT NULL AUTO_INCREMENT,
    room_id     INT  NOT NULL,
    user_id     INT  NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    delete_time DATE,
    PRIMARY KEY (ID),
    FOREIGN KEY (room_id) REFERENCES senla_traineeship.rooms (id),
    FOREIGN KEY (user_id) REFERENCES senla_traineeship.users (id)
);

-- Пример вставки комнаты -->
INSERT INTO senla_traineeship.rooms (room_status, room_price)
VALUES (false, 1000);
INSERT INTO senla_traineeship.rooms (room_status, room_price)
VALUES (true, 2000);
INSERT INTO senla_traineeship.rooms (room_status, room_price)
VALUES (false, 3000);
-- Пример вставки пользователя -->
INSERT INTO senla_traineeship.users (user_name) value ('test name');


CREATE DEFINER=`root`@`localhost` PROCEDURE `validate_booking_date`(IN room_id INT, IN start_booking_date DATE, IN end_booking_date DATE)
BEGIN
    declare validate INT default 0;
    declare done INT default false;
    declare start_date date;
    declare end_date date;

    DECLARE cur1 CURSOR FOR SELECT b.start_date, b.end_date FROM senla_traineeship.bookings b where b.room_id = room_id and b.delete_time IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;

    open cur1;

    for_loop: loop
        fetch cur1 into start_date, end_date;
        if done then
            leave for_loop;
        end if;
        if (start_booking_date > start_date and start_booking_date < end_date)
            OR (end_booking_date > start_date and end_booking_date < end_date)
            OR (start_date > start_booking_date and start_date < end_booking_date)
            OR (end_date > start_booking_date and end_date < end_booking_date)
            OR (start_date = start_booking_date OR start_date = end_booking_date)
            OR (end_date = start_booking_date OR end_date = end_booking_date)
        then
            set validate = 1;
        end if;

    end loop;
    close cur1;
    select validate;
END;