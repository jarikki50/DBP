package function;

import DB_connection.DB;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookSearch {

    // 책 정보 검색 기능
    public DefaultTableModel searchBooks(String criteria, String searchTerm) {
        DefaultTableModel model = new DefaultTableModel();
        String[] columnNames = {"등록번호", "등록일자", "제목", "저자", "출판사", "강의명", "교수명", "책상태", "등록가격", "평균가격"};
        model.setColumnIdentifiers(columnNames);

        // 칼럼 이름 검증
        String columnName;
        if ("제목".equals(criteria)) {
            columnName = "책.제목";
        } else if ("저자".equals(criteria)) {
            columnName = "책.저자";
        } else {
            throw new IllegalArgumentException("잘못된 검색 기준입니다.");
        }

        // SQL 쿼리
        String sql = """
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
                    WHERE UPPER(%s) LIKE ?
                """.formatted(columnName);

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm.toUpperCase() + "%";
            stmt.setString(1, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }
}
