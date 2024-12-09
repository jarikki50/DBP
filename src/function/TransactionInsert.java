package function;

import DB_connection.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionInsert {

    // 거래 삽입 기능
    public void insertTransaction() {
        Scanner scanner = new Scanner(System.in);

        // 사용자로부터 판매자 학번, 구매자 학번, 가격, 등록번호 입력받기
        System.out.print("판매자 학번을 입력하세요: ");
        String sellerID = scanner.nextLine();
        System.out.print("구매자 학번을 입력하세요: ");
        String buyerID = scanner.nextLine();
        System.out.print("거래 가격을 입력하세요: ");
        double price = scanner.nextDouble();
        System.out.print("등록번호를 입력하세요: ");
        int registrationID = scanner.nextInt();
        System.out.print("거래장소를 입력하세요: ");
        int locationNumber = scanner.nextInt();

        // DB 연결 및 거래 삽입
        try (Connection conn = DB.getConnection()) {
            // 거래번호 자동 증가 처리 및 거래날짜는 트리거에서 자동 처리되므로 생략
            String sql = "INSERT INTO 거래내역 (거래번호, 가격, 판매자학번, 구매자학번, 등록번호, 장소번호) " +
                    "VALUES ((SELECT NVL(MAX(거래번호), 0) + 1 FROM 거래내역), ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // PreparedStatement에 값 바인딩
                stmt.setDouble(1, price);  // 거래가격
                stmt.setString(2, sellerID);  // 판매자 학번
                stmt.setString(3, buyerID);  // 구매자 학번
                stmt.setInt(4, registrationID);  // 등록번호
                stmt.setInt(5, locationNumber);  // 장소번호

                // 쿼리 실행
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("거래가 성공적으로 등록되었습니다.");
                } else {
                    System.out.println("거래 등록에 실패했습니다.");
                }
            }
        } catch (SQLException e) {
            // 예외 처리
            System.out.println("쿼리 실행 오류: " + e.getMessage());
        }
    }
}
