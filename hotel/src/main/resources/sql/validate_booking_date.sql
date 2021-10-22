CREATE
    DEFINER = `root`@`localhost` PROCEDURE `validate_booking_date`(IN room_id INT, IN start_booking_date DATE, IN end_booking_date DATE)
BEGIN
    declare validate INT default 0;
    declare done INT default false;
    declare start_date date;
    declare end_date date;

    DECLARE cur1 CURSOR FOR SELECT b.start_date, b.end_date
        FROM test.bookings b
        where b.room_id = room_id
            and b.delete_time IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;

    open cur1;

    for_loop:
    loop
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