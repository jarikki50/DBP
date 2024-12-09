package function;

import DB_connection.DB;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BookSort {

    // 정렬된 데이터를 DefaultTableModel로 반환하는 메서드
    public DefaultTableModel sortBooks(String criteria, String order) {
        DefaultTableModel model = new DefaultTableModel();
        String[] columnNames = {
                "등록번호", "등록일자", "제목", "저자", "출판사",
                "강의명", "교수명", "책상태", "등록가격", "평균가격"
        };
        model.setColumnIdentifiers(columnNames);

        String sql = ""; // SQL 쿼리 초기화

        // 정렬 기준 설정
        switch (criteria) {
            case "책 제목":
                sql = """
                    SELECT 
                        등록내역.등록번호,
                        등록내역.등록일자,
                        책.제목, 책.저자, 책.출판사, 
                        강의.강의명, 교수.이름 AS 교수명, 
                        등록내역.책상태 AS 책상태, 
                        등록내역.등록가격, 
                        AVG(등록내역.등록가격) OVER (PARTITION BY 등록내역.일련번호) AS 평균가격
                    FROM 등록내역
                    INNER JOIN 책 ON 등록내역.일련번호 = 책.일련번호
                    INNER JOIN 사용 ON 등록내역.일련번호 = 사용.일련번호
                    INNER JOIN 강의 ON 사용.강의번호 = 강의.강의번호
                    INNER JOIN 교수 ON 강의.담당교수번호 = 교수.교수번호
                    WHERE 등록내역.판매여부 = '판매'
                    ORDER BY 책.제목 """ +  " " +order;
                break;
            case "등록일자":
                sql = """
                    SELECT 
                        등록내역.등록번호,
                        등록내역.등록일자,
                        책.제목, 책.저자, 책.출판사, 
                        강의.강의명, 교수.이름 AS 교수명, 
                        등록내역.책상태 AS 책상태, 
                        등록내역.등록가격, 
                        AVG(등록내역.등록가격) OVER (PARTITION BY 등록내역.일련번호) AS 평균가격
                    FROM 등록내역
                    INNER JOIN 책 ON 등록내역.일련번호 = 책.일련번호
                    INNER JOIN 사용 ON 등록내역.일련번호 = 사용.일련번호
                    INNER JOIN 강의 ON 사용.강의번호 = 강의.강의번호
                    INNER JOIN 교수 ON 강의.담당교수번호 = 교수.교수번호
                    WHERE 등록내역.판매여부 = '판매'
                    ORDER BY 등록내역.등록번호 """ + " " + order;
                break;
            case "제시 금액":
                sql = """
                    SELECT 
                        등록내역.등록번호,
                        등록내역.등록일자,
                        책.제목, 책.저자, 책.출판사, 
                        강의.강의명, 교수.이름 AS 교수명, 
                        등록내역.책상태 AS 책상태, 
                        등록내역.등록가격, 
                        AVG(등록내역.등록가격) OVER (PARTITION BY 등록내역.일련번호) AS 평균가격
                    FROM 등록내역
                    INNER JOIN 책 ON 등록내역.일련번호 = 책.일련번호
                    INNER JOIN 사용 ON 등록내역.일련번호 = 사용.일련번호
                    INNER JOIN 강의 ON 사용.강의번호 = 강의.강의번호
                    INNER JOIN 교수 ON 강의.담당교수번호 = 교수.교수번호
                    WHERE 등록내역.판매여부 = '판매'
                    ORDER BY 등록내역.등록가격 """ +  " " +order;
                break;
            default:
                throw new IllegalArgumentException("잘못된 정렬 기준입니다.");
        }

        // 데이터베이스 연결 및 데이터 가져오기
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                        rs.getString("등록번호"),
                        rs.getString("등록일자"),
                        rs.getString("제목"),
                        rs.getString("저자"),
                        rs.getString("출판사"),
                        rs.getString("강의명"),
                        rs.getString("교수명"),
                        rs.getString("책상태"),
                        rs.getDouble("등록가격"),
                        rs.getDouble("평균가격")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model; // 정렬된 데이터를 담은 모델 반환
    }
}
