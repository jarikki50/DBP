package function;

import DB_connection.DB;
import java.sql.*;
import java.util.Scanner;

public class PurchaseRequestSearch {

    // 구매 신청 내역 검색 기능
    public void searchPurchaseRequests() {
        Scanner scanner = new Scanner(System.in);

        // 사용자로부터 학번 입력받기
        System.out.print("학번을 입력하세요: ");
        String studentID = scanner.nextLine();  // 사용자 학번

        // DB 연결 및 구매 신청 내역 조회
        try (Connection conn = DB.getConnection()) {
            // SQL 쿼리: 사용자 학번을 기준으로 신청 내역 조회
            String sql = "SELECT 신청내역.신청번호, 신청내역.신청일자, 신청내역.신청가격, 신청내역.등록번호 " +
                    "FROM 신청내역 " +
                    "JOIN 사용자 ON 사용자.학번 = 신청내역.구매자학번 " +
                    "WHERE 사용자.학번 = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // 학번 값을 PreparedStatement에 바인딩
                stmt.setString(1, studentID);

                // 쿼리 실행
                try (ResultSet rs = stmt.executeQuery()) {
                    System.out.println("신청 번호\t신청 일자\t신청 가격\t등록 번호");
                    boolean hasResults = false;
                    while (rs.next()) {
                        int requestID = rs.getInt("신청번호");
                        Date requestDate = rs.getDate("신청일자");
                        double requestPrice = rs.getDouble("신청가격");
                        int locationID = rs.getInt("등록번호");

                        System.out.println(requestID + "\t" + requestDate + "\t" + requestPrice + "\t" + locationID);
                        hasResults = true;
                    }

                    // 결과가 없을 경우 메시지 출력
                    if (!hasResults) {
                        System.out.println("검색 결과가 없습니다.");
                    }
                }
            }
        } catch (SQLException e) {
            // 쿼리 실행 오류 처리
            System.out.println("쿼리 실행 오류: " + e.getMessage());
        }
    }
}
