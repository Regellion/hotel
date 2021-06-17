package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class MysqlUtil {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Connection getConnection() {
        Connection connection;
        long start = System.currentTimeMillis();
        try {
            Properties properties = new Properties();
            properties.load(MysqlUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            String mysqlURL = properties.getProperty("mysql.host");
            String mysqlUser = properties.getProperty("mysql.login");
            String mysqlPass = properties.getProperty("mysql.pass");
            connection = DriverManager.getConnection(mysqlURL, mysqlUser, mysqlPass);
        } catch (Exception e) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            System.out.println(formatter.format(start) + " ERROR in " + MysqlUtil.class + ": Error creating a database connection. " + e);
            return null;
        }

        return connection;
    }
}
