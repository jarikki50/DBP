package GUI;

import model.EV;
import DB_connection.DB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class buylist extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private Choice sortChoice;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                buylist frame = new buylist();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public buylist() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1003, 700); // 높이를 더 늘림
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 1000, 36);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("동의책방");
        lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblNewLabel);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 185, 800, 356);
        contentPane.add(scrollPane);

        sortChoice = new Choice();
        sortChoice.add("빠른 순서");
        sortChoice.add("느린 순서");
        sortChoice.setBounds(546, 159, 97, 20);
        contentPane.add(sortChoice);

        JLabel lblNewLabel_1 = new JLabel("신청일자 정렬");
        lblNewLabel_1.setBounds(450, 160, 100, 15);
        contentPane.add(lblNewLabel_1);

        JButton btnSort = new JButton("정렬");
        btnSort.setBounds(650, 159, 91, 23);
        btnSort.addActionListener(e -> {
            String sortOrder = sortChoice.getSelectedItem().equals("빠른 순서") ? "ASC" : "DESC";
            loadTableData(table, EV.ID, sortOrder);
        });
        contentPane.add(btnSort);

        // Adjust positions of text fields and labels
        JLabel lblNewLabel_2 = new JLabel("건물 번호");
        lblNewLabel_2.setBounds(50, 570, 100, 15);
        contentPane.add(lblNewLabel_2);

        textField = new JTextField();
        textField.setBounds(150, 570, 200, 25);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_2_1 = new JLabel("강의실");
        lblNewLabel_2_1.setBounds(400, 570, 100, 15);
        contentPane.add(lblNewLabel_2_1);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(500, 570, 200, 25);
        contentPane.add(textField_1);

        JLabel lblNewLabel_2_1_1 = new JLabel("제시 금액");
        lblNewLabel_2_1_1.setBounds(50, 610, 100, 15);
        contentPane.add(lblNewLabel_2_1_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(150, 610, 200, 25);
        contentPane.add(textField_2);

        JLabel lblNewLabel_2_1_1_1 = new JLabel("제시 날짜");
        lblNewLabel_2_1_1_1.setBounds(400, 610, 100, 15);
        contentPane.add(lblNewLabel_2_1_1_1);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(500, 610, 200, 25);
        contentPane.add(textField_3);

        JLabel lblNewLabel_2_1_1_1_1 = new JLabel("입력 예시: YYYY-MM-DD");
        lblNewLabel_2_1_1_1_1.setBounds(720, 610, 200, 15);
        contentPane.add(lblNewLabel_2_1_1_1_1);

        JButton btnUpdate = new JButton("수정");
        btnUpdate.setBounds(725, 570, 91, 25);
        btnUpdate.addActionListener(e -> updateTransaction());
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("거래 취소");
        btnDelete.setBounds(825, 570, 91, 25);
        btnDelete.addActionListener(e -> deleteTransaction());
        contentPane.add(btnDelete);

        // 초기 데이터 로드
        loadTableData(table, EV.ID, "ASC");
    }

    private void loadTableData(JTable table, String studentId, String sortOrder) {
        String query = """
            SELECT
                신청내역.신청번호 AS 신청번호,
                책.제목 AS 책_제목,
                사용자.이름 AS 판매자,
                거래장소.건물번호 AS 건물번호,
                거래장소.강의실 AS 강의실,
                신청내역.신청가격 AS 신청가격,
                신청내역.신청일자 AS 신청일자
            FROM 신청내역
            LEFT JOIN 등록내역 ON 신청내역.등록번호 = 등록내역.등록번호
            LEFT JOIN 책 ON 등록내역.일련번호 = 책.일련번호
            LEFT JOIN 사용자 ON 등록내역.판매자학번 = 사용자.학번
            LEFT JOIN 거래장소 ON 신청내역.장소번호 = 거래장소.장소번호
            WHERE 신청내역.구매자학번 = ?
            ORDER BY 신청내역.신청일자 """ + " " + sortOrder;

        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                String[] columnNames = {
                        "신청번호",
                        "책 제목",
                        "판매자",
                        "건물번호",
                        "강의실",
                        "신청가격",
                        "신청일자"
                };
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                while (rs.next()) {
                    Object[] rowData = {
                            rs.getInt("신청번호"),
                            rs.getString("책_제목"),
                            rs.getString("판매자"),
                            rs.getString("건물번호"),
                            rs.getString("강의실"),
                            rs.getInt("신청가격"),
                            rs.getDate("신청일자")
                    };
                    model.addRow(rowData);
                }

                table.setModel(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터를 불러오는 중 오류가 발생했습니다.");
        }
    }

    private void updateTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "수정할 거래를 선택하세요.");
            return;
        }

        try {
            int 신청번호 = (int) table.getValueAt(selectedRow, 0);
            String 건물번호 = textField.getText();
            String 강의실 = textField_1.getText();
            int 신청가격 = Integer.parseInt(textField_2.getText());
            String 신청날짜 = textField_3.getText();

            int 장소번호 = getPlaceNumber(건물번호, 강의실);
            String updateQuery = """
                UPDATE 신청내역
                SET 장소번호 = ?, 신청가격 = ?, 신청일자 = ?
                WHERE 신청번호 = ?
            """;

            try (Connection conn = DB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, 장소번호);
                pstmt.setInt(2, 신청가격);
                pstmt.setString(3, 신청날짜);
                pstmt.setInt(4, 신청번호);

                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "거래가 성공적으로 수정되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "수정에 실패했습니다. 다시 시도해주세요.");
                }
            }
            loadTableData(table, EV.ID, "ASC");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "수정 중 오류가 발생했습니다.");
        }
    }

    private void deleteTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "취소할 거래를 선택하세요.");
            return;
        }

        int 신청번호 = (int) table.getValueAt(selectedRow, 0);

        String deleteQuery = "DELETE FROM 신청내역 WHERE 신청번호 = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, 신청번호);
            if (pstmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "거래가 성공적으로 취소되었습니다.");
            } else {
                JOptionPane.showMessageDialog(null, "취소에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "취소 중 오류가 발생했습니다.");
        }
        loadTableData(table, EV.ID, "ASC");
    }

    private int getPlaceNumber(String 건물번호, String 강의실) throws SQLException {
        String query = "SELECT 장소번호 FROM 거래장소 WHERE 건물번호 = ? AND 강의실 = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, 건물번호);
            pstmt.setString(2, 강의실);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("장소번호");
                } else {
                    return insertNewPlace(건물번호, 강의실);
                }
            }
        }
    }

    private int insertNewPlace(String 건물번호, String 강의실) throws SQLException {
        String selectMaxQuery = "SELECT MAX(장소번호) AS 장소번호 FROM 거래장소";
        String insertQuery = "INSERT INTO 거래장소 (장소번호, 건물번호, 강의실) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectMaxQuery);
             ResultSet rs = selectStmt.executeQuery()) {
            int newPlaceNumber = rs.next() ? rs.getInt("장소번호") + 1 : 1;

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, newPlaceNumber);
                insertStmt.setString(2, 건물번호);
                insertStmt.setString(3, 강의실);
                insertStmt.executeUpdate();
                return newPlaceNumber;
            }
        }
    }
}
