CREATE SCHEMA senla_traineeship DEFAULT CHARACTER SET utf8;

CREATE TABLE senla_traineeship.rooms
(
    id             INT     NOT NULL AUTO_INCREMENT,
    room_is_booked boolean NOT NULL,
    room_status    INT     NOT NULL,
    room_price     INT,
    PRIMARY KEY (id)
);

INSERT INTO senla_traineeship.rooms (room_is_booked, room_status, room_price)
VALUES (true, false, 1000);
INSERT INTO senla_traineeship.rooms (room_is_booked, room_status, room_price)
VALUES (true, true, 2000);
INSERT INTO senla_traineeship.rooms (room_is_booked, room_status, room_price)
VALUES (false, false, 3000);