package function;

import DB_connection.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class BookRegister {

    // 책 등록 기능
    public void registerBook() {
        Scanner scanner = new Scanner(System.in);

        // 사용자로부터 책 정보 입력받기
        System.out.print("책 일련번호를 입력하세요: ");
        String bookID = scanner.nextLine();  // 책 일련번호는 문자열로 받기
        System.out.print("책 제목을 입력하세요: ");
        String title = scanner.nextLine();  // 책 제목
        System.out.print("책 저자를 입력하세요: ");
        String author = scanner.nextLine();  // 책 저자
        System.out.print("책 출판사를 입력하세요: ");
        String publisher = scanner.nextLine();  // 책 출판사

        // DB 연결 및 책 정보 삽입
        try (Connection conn = DB.getConnection()) {
            // 책 정보를 DB에 삽입하는 SQL 쿼리
            String sql = "INSERT INTO 책 (일련번호, 제목, 저자, 출판사) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // PreparedStatement에 값 바인딩
                stmt.setString(1, bookID);  // 책 일련번호
                stmt.setString(2, title);  // 책 제목
                stmt.setString(3, author);  // 책 저자
                stmt.setString(4, publisher);  // 책 출판사

                // 쿼리 실행
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("책이 성공적으로 등록되었습니다.");
                } else {
                    System.out.println("책 등록에 실패했습니다.");
                }
            }
        } catch (SQLException e) {
            // 예외 처리
            System.out.println("쿼리 실행 오류: " + e.getMessage());
        }
    }
}
