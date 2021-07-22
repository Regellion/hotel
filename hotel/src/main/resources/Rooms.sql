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