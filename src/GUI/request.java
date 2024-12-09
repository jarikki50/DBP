package GUI;

import model.EV;
import DB_connection.DB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class request extends JFrame {

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
                request frame = new request();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public request() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 975, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 974, 36);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("동의책방");
        lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblNewLabel);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(52, 155, 567, 383);
        contentPane.add(scrollPane);

        sortChoice = new Choice();
        sortChoice.add("오름차순");
        sortChoice.add("내림차순");
        sortChoice.setBounds(554, 129, 100, 20);
        contentPane.add(sortChoice);

        JLabel lblNewLabel_1 = new JLabel("정렬");
        lblNewLabel_1.setBounds(520, 130, 28, 15);
        contentPane.add(lblNewLabel_1);

        JButton sortButton = new JButton("정렬");
        sortButton.setBounds(670, 129, 80, 20);
        sortButton.addActionListener(e -> {
            String sortOrder = sortChoice.getSelectedItem().equals("오름차순") ? "ASC" : "DESC";
            loadData(table, EV.SelectN, sortOrder);
        });
        contentPane.add(sortButton);

        textField = new JTextField();
        textField.setBounds(728, 195, 198, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("거래 건물");
        lblNewLabel_2.setBounds(644, 198, 72, 15);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_2_1 = new JLabel("제시 금액");
        lblNewLabel_2_1.setBounds(644, 297, 50, 15);
        contentPane.add(lblNewLabel_2_1);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(728, 294, 198, 21);
        contentPane.add(textField_1);

        JLabel lblNewLabel_2_1_1 = new JLabel("거래 날짜");
        lblNewLabel_2_1_1.setBounds(644, 348, 50, 15);
        contentPane.add(lblNewLabel_2_1_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(728, 345, 198, 21);
        contentPane.add(textField_2);

        JButton btnNewButton = new JButton("구매 신청");
        btnNewButton.setBounds(740, 455, 91, 23);
        contentPane.add(btnNewButton);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(728, 236, 198, 21);
        contentPane.add(textField_3);

        JLabel lblNewLabel_2_2 = new JLabel("거래 강의실");
        lblNewLabel_2_2.setBounds(644, 239, 84, 15);
        contentPane.add(lblNewLabel_2_2);

        JLabel lblNewLabel_3 = new JLabel("입력예시(YYYY-MM-DD)");
        lblNewLabel_3.setBounds(728, 376, 198, 15);
        contentPane.add(lblNewLabel_3);

        btnNewButton.addActionListener(e -> {
            String buildingNumber = textField.getText();
            String classroom = textField_3.getText();
            String price = textField_1.getText();
            String applyDate = textField_2.getText();

            // 입력된 값을 사용해 신청 내역 추가
            insertApplicationDetails(buildingNumber, classroom, price, applyDate);

            // 창 닫고 다시 열기
            java.awt.Window currentWindow = SwingUtilities.getWindowAncestor(btnNewButton);
            if (currentWindow != null) {
                currentWindow.dispose(); // 현재 창 닫기
            }

            // 새 창 열기
            EventQueue.invokeLater(() -> {
                try {
                    request newRequestWindow = new request(); // 새로운 창 인스턴스 생성
                    newRequestWindow.setVisible(true); // 새 창 표시
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        // 기본 데이터 로드
        loadData(table, EV.SelectN, "ASC");
    }

    public static void loadData(JTable table, String selectN, String sortOrder) {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        // 컬럼명 추가
        model.addColumn("책 제목");
        model.addColumn("구매자");
        model.addColumn("신청일자");
        model.addColumn("신청가격");
        model.addColumn("건물번호");
        model.addColumn("강의실");

        try (Connection conn = DB.getConnection()) {
            String query = """
                SELECT B.제목, U.이름, S.신청일자, S.신청가격, L.건물번호, L.강의실 
                FROM 신청내역 S 
                JOIN 등록내역 R ON S.등록번호 = R.등록번호 
                JOIN 책 B ON R.일련번호 = B.일련번호 
                JOIN 사용자 U ON S.구매자학번 = U.학번 
                JOIN 거래장소 L ON S.장소번호 = L.장소번호 
                WHERE S.등록번호 = ? 
                ORDER BY S.신청일자 """ + " " + sortOrder;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selectN);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] rowData = new Object[6];
                        rowData[0] = rs.getString("제목");      // 책 제목
                        rowData[1] = rs.getString("이름");      // 구매자
                        rowData[2] = rs.getString("신청일자"); // 신청일자
                        rowData[3] = rs.getInt("신청가격");    // 신청가격
                        rowData[4] = rs.getInt("건물번호");    // 건물번호
                        rowData[5] = rs.getString("강의실");   // 강의실

                        model.addRow(rowData); // 모델에 행 추가
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터를 로드하는 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertApplicationDetails(String buildingNumber, String classroom, String price, String applyDate) {
        int registrationNumber = Integer.parseInt(EV.SelectN);  // EV의 SelectN 값을 '등록번호'로 사용
        int studentID = Integer.parseInt(EV.ID);  // EV의 ID 값을 '구매자학번'으로 사용

        Connection conn = null;
        PreparedStatement psInsertApplication = null;
        PreparedStatement psInsertLocation = null;
        PreparedStatement psSelectLocation = null;
        ResultSet rsLocation = null;

        try {
            conn = DB.getConnection();

            Date applicationDate = Date.valueOf(applyDate);
            Date creationDate = new Date(System.currentTimeMillis());

            String selectLocationSQL = "SELECT 장소번호 FROM 거래장소 WHERE 건물번호 = ? AND 강의실 = ?";
            psSelectLocation = conn.prepareStatement(selectLocationSQL);
            psSelectLocation.setString(1, buildingNumber);
            psSelectLocation.setString(2, classroom);
            rsLocation = psSelectLocation.executeQuery();

            int locationId = -1;

            if (rsLocation.next()) {
                locationId = rsLocation.getInt("장소번호");
            } else {
                String getMaxLocationIdSQL = "SELECT NVL(MAX(장소번호), 0) + 1 AS 장소번호 FROM 거래장소";
                psSelectLocation = conn.prepareStatement(getMaxLocationIdSQL);
                rsLocation = psSelectLocation.executeQuery();

                int newLocationId = 1;
                if (rsLocation.next()) {
                    newLocationId = rsLocation.getInt("장소번호");
                }

                String insertLocationSQL = "INSERT INTO 거래장소 (장소번호, 건물번호, 강의실) VALUES (?, ?, ?)";
                psInsertLocation = conn.prepareStatement(insertLocationSQL);
                psInsertLocation.setInt(1, newLocationId);
                psInsertLocation.setString(2, buildingNumber);
                psInsertLocation.setString(3, classroom);
                psInsertLocation.executeUpdate();

                locationId = newLocationId;
            }

            String getMaxApplicationNumberSQL = "SELECT NVL(MAX(신청번호), 0) + 1 AS NEXT_ID FROM 신청내역";
            int nextApplicationNumber = 0;
            try (PreparedStatement psMax = conn.prepareStatement(getMaxApplicationNumberSQL);
                 ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) {
                    nextApplicationNumber = rsMax.getInt("NEXT_ID");
                }
            }

            String insertApplicationSQL = "INSERT INTO 신청내역 (신청번호, 등록번호, 신청일자, 작성일자, 신청가격, 구매자학번, 장소번호) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            psInsertApplication = conn.prepareStatement(insertApplicationSQL);
            psInsertApplication.setInt(1, nextApplicationNumber);
            psInsertApplication.setInt(2, registrationNumber);
            psInsertApplication.setDate(3, applicationDate);
            psInsertApplication.setDate(4, creationDate);
            psInsertApplication.setInt(5, Integer.parseInt(price));
            psInsertApplication.setInt(6, studentID);
            psInsertApplication.setInt(7, locationId);

            psInsertApplication.executeUpdate();
            System.out.println("신청내역이 성공적으로 추가되었습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL 오류가 발생했습니다.");
        } finally {
            try {
                if (rsLocation != null) rsLocation.close();
                if (psSelectLocation != null) psSelectLocation.close();
                if (psInsertLocation != null) psInsertLocation.close();
                if (psInsertApplication != null) psInsertApplication.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
