package com.hotel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DTOMaker {

    public static Integer setPrice() {
        return new Scanner(System.in).nextInt();
    }

    public static Long setId() {
        return new Scanner(System.in).nextLong();
    }

    public static Date setDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(new Scanner(System.in).nextLine());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date input: " + e.getMessage());
        }
    }

    public static String setName() {
        return new Scanner(System.in).nextLine();
    }

}
