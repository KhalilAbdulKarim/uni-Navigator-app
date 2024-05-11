//package DBConnection;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//
//public class DBHandler {
//    private static DBHandler instance;
//    private static Properties properties = new Properties();
//    private static String url;
//    private static String USER;
//    private static String PASSWORD;
//
//    static {
//        // Load the properties when the class is loaded
//        try (InputStream input = DBHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
//            if (input == null) {
//                throw new IOException("Unable to find config.properties");
//            }
//            properties.load(input);
//            url = properties.getProperty("db.url");
//            USER = properties.getProperty("db.user");
//            PASSWORD = properties.getProperty("db.password");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public DBHandler(){
//        connect();
//    }
//
//
//    public static synchronized DBHandler getInstance(){
//        if(instance == null){
//            instance = new DBHandler();
//        }
//        return instance;
//    }
//
//    public Connection connect(){
//        Connection connection = null;
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection(url, USER, PASSWORD);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Database connection failed. Please check the log for details.");
//        } catch (ClassNotFoundException e) {
//            System.out.println("MySQL JDBC Driver not found.");
//            e.printStackTrace();
//            throw new RuntimeException("JDBC Driver not found.");
//        }
//        return connection;
//    }
//
//
//    // create dyncamic function called query that receives sql
//
//    // Method to execute SELECT queries
//    public ResultSet executeQuery(String sql, Object... params) {
//        try (Connection connection = this.connect();
//             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, params);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//            // Clone the ResultSet because it will be closed after try-with-resources
//            return cloneResultSet(resultSet);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null; // Or handle it more gracefully
//        }
//    }
//
//    // Helper method to create a PreparedStatement
//    private PreparedStatement createPreparedStatement(Connection connection, String sql, Object... params) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        for (int i = 0; i < params.length; i++) {
//            preparedStatement.setObject(i + 1, params[i]);
//        }
//        return preparedStatement;
//    }
//
//    // Method to execute INSERT, UPDATE, DELETE
//    public int executeUpdate(String sql, Object... params) {
//        try (Connection connection = this.connect();
//             PreparedStatement preparedStatement = createPreparedStatement(connection, sql, params)) {
//            return preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1; // Or handle it more gracefully
//        }
//    }
//
//    private ResultSet cloneResultSet(ResultSet resultSet) {
//        // Implement the logic to clone the ResultSet data into a new object
//        throw new UnsupportedOperationException("Cloning a ResultSet is not implemented.");
//    }
//
//
//
//}
