package function;

import DB_connection.DB;
import java.sql.*;
import java.util.Scanner;

public class SaleRequestSearch {

    // 판매 신청 내역 검색 기능
    public void searchSaleRequests() {
        Scanner scanner = new Scanner(System.in);

        // 사용자로부터 학번 입력받기
        System.out.print("학번을 입력하세요: ");
        String studentID = scanner.nextLine();  // 사용자 학번

        // DB 연결 및 판매 신청 내역 조회
        try (Connection conn = DB.getConnection()) {
            // SQL 쿼리: 본인이 작성한 판매 글과 해당 판매 글에 신청한 구매자 내역을 조회
            String sql = "SELECT 등록내역.등록번호, 등록내역.등록일자, 등록내역.등록가격, 등록내역.책상태, " +
                    "책.일련번호, 신청내역.신청번호, 신청내역.신청일자, 신청내역.신청가격, 신청내역.구매자학번 " +
                    "FROM 등록내역 " +
                    "JOIN 책 ON 등록내역.일련번호 = 책.일련번호 " +
                    "JOIN 신청내역 ON 등록내역.등록번호 = 신청내역.등록번호 " +
                    "JOIN 사용자 ON 사용자.학번 = 등록내역.판매자학번 " +
                    "WHERE 사용자.학번 = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // 학번 값을 PreparedStatement에 바인딩
                stmt.setString(1, studentID);

                // 쿼리 실행
                try (ResultSet rs = stmt.executeQuery()) {
                    System.out.println("등록번호\t등록일자\t등록가격\t책상태\t일련번호\t신청번호\t신청일자\t신청가격\t구매자학번");
                    boolean hasResults = false;
                    while (rs.next()) {
                        int registrationID = rs.getInt("등록번호");
                        Date registrationDate = rs.getDate("등록일자");
                        double registrationPrice = rs.getDouble("등록가격");
                        String bookCondition = rs.getString("책상태");
                        String bookID = rs.getString("일련번호");
                        int requestID = rs.getInt("신청번호");
                        Date requestDate = rs.getDate("신청일자");
                        double requestPrice = rs.getDouble("신청가격");
                        int buyerID = rs.getInt("구매자학번");

                        // 판매 상태(책 상태)를 그대로 가져오기
                        System.out.println(registrationID + "\t" + registrationDate + "\t" + registrationPrice + "\t" +
                                bookCondition + "\t" + bookID + "\t" + requestID +
                                "\t" + requestDate + "\t" + requestPrice + "\t" + buyerID);
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
