package function;

import DB_connection.DB;
import java.sql.*;
import java.util.Scanner;

public class TransactionSearch {

    // 거래 기록 조회 기능
    public void searchTransactionsByDateRange() {
        Scanner scanner = new Scanner(System.in);

        // 사용자로부터 조회할 기간 입력받기
        System.out.print("조회할 시작 날짜 (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();  // 시작 날짜
        System.out.print("조회할 종료 날짜 (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();    // 종료 날짜

        // DB 연결 및 프로시저 호출
        try (Connection conn = DB.getConnection()) {
            // 거래 통계 조회 프로시저 호출
            String procedureCall = "{CALL 거래_통계_조회(?, ?, ?, ?)}";

            try (CallableStatement cstmt = conn.prepareCall(procedureCall)) {
                // IN 매개변수 설정
                cstmt.setDate(1, Date.valueOf(startDate));  // 시작 날짜
                cstmt.setDate(2, Date.valueOf(endDate));    // 종료 날짜

                // OUT 매개변수 등록
                cstmt.registerOutParameter(3, Types.INTEGER);  // 거래 수
                cstmt.registerOutParameter(4, Types.DOUBLE);   // 총 금액

                // 프로시저 실행
                cstmt.execute();

                // OUT 매개변수에서 값 가져오기
                int transactionCount = cstmt.getInt(3);
                double totalAmount = cstmt.getDouble(4);

                System.out.println("조회 기간 동안의 거래 통계:");
                System.out.println("총 거래 수: " + transactionCount);
                System.out.println("총 거래 금액: " + totalAmount);

                // 상세 거래 내역 조회
                String sql = "SELECT 거래번호, 거래날짜, 가격, 판매자학번, 구매자학번, 등록번호 " +
                        "FROM 거래내역 " +
                        "WHERE 거래날짜 BETWEEN ? AND ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // 날짜 값을 PreparedStatement에 바인딩
                    stmt.setString(1, startDate);  // 시작 날짜
                    stmt.setString(2, endDate);    // 종료 날짜

                    // 쿼리 실행
                    try (ResultSet rs = stmt.executeQuery()) {
                        System.out.println("거래 번호\t거래 날짜\t가격\t판매자 학번\t구매자 학번\t등록번호");
                        boolean hasResults = false;
                        while (rs.next()) {
                            int transactionID = rs.getInt("거래번호");
                            Date transactionDate = rs.getDate("거래날짜");
                            double price = rs.getDouble("가격");
                            int sellerID = rs.getInt("판매자학번");
                            int buyerID = rs.getInt("구매자학번");
                            int registrationNumber = rs.getInt("등록번호");

                            System.out.println(transactionID + "\t" + transactionDate + "\t" + price + "\t" +
                                    sellerID + "\t" + buyerID + "\t" + registrationNumber);
                            hasResults = true;
                        }

                        // 결과가 없을 경우 메시지 출력
                        if (!hasResults) {
                            System.out.println("검색 결과가 없습니다.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // 쿼리 실행 오류 처리
            System.out.println("쿼리 실행 오류: " + e.getMessage());
        }
    }
}
