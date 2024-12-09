package DB_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";  // Oracle DB 연결 URL
    private static final String USER = "book";  // DB 사용자명
    private static final String PASSWORD = "1234";  // DB 비밀번호

    public static Connection getConnection() throws SQLException {
        try {
            // JDBC 드라이버 로드 (Oracle)
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Oracle JDBC 드라이버 로드 실패");
        }
    }
}
