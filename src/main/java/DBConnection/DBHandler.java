package DBConnection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DBHandler {
    private static DBHandler instance;
    private static Properties properties = new Properties();
    private static String url;
    private static String USER;
    private static String PASSWORD;

    static {
        // Load the properties when the class is loaded
        try (InputStream input = DBHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
            url = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private DBHandler(){
        connect();
    }


    public static synchronized DBHandler getInstance(){
        if(instance == null){
            instance = new DBHandler();
        }
        return instance;
    }

    public Connection connect(){
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("-Database connected successfully-");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
        return connection;
    }


    public boolean doesUserExist (int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE UserID = ?";

        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }














}
