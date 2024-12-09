package GUI;

import model.EV;
import DB_connection.DB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class registration extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private Panel panel_1;
    private JLabel lblNewLabel_1;
    private JTextField textField_5;
    private JTextField textField_4;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;
    private JLabel lblNewLabel_5;
    private JLabel lblNewLabel_7;
    private JButton btnNewButton;
    private JLabel lblNewLabel_11;
    private JTextField textField_9;
    private JTextField textField_10;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    registration frame = new registration();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public registration() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 834, 428);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(109, 69, 268, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(109, 116, 268, 21);
        contentPane.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(109, 166, 268, 21);
        contentPane.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(109, 218, 268, 21);
        contentPane.add(textField_3);

        Panel panel = new Panel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 830, 36);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("동의책방 ");
        lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblNewLabel);

        panel_1 = new Panel();
        panel_1.setBackground(new Color(0, 0, 0));
        panel_1.setBounds(383, 47, 3, 277);
        contentPane.add(panel_1);

        lblNewLabel_1 = new JLabel("강의명");
        lblNewLabel_1.setBounds(11, 72, 86, 15);
        contentPane.add(lblNewLabel_1);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(524, 69, 268, 21);
        contentPane.add(textField_5);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(524, 166, 268, 21);
        contentPane.add(textField_4);

        lblNewLabel_2 = new JLabel("교수 이름");
        lblNewLabel_2.setBounds(11, 119, 86, 15);
        contentPane.add(lblNewLabel_2);

        lblNewLabel_3 = new JLabel("책 제목");
        lblNewLabel_3.setBounds(11, 169, 86, 15);
        contentPane.add(lblNewLabel_3);

        lblNewLabel_4 = new JLabel("저자");
        lblNewLabel_4.setBounds(11, 221, 86, 15);
        contentPane.add(lblNewLabel_4);

        lblNewLabel_5 = new JLabel("출판사");
        lblNewLabel_5.setBounds(410, 72, 86, 15);
        contentPane.add(lblNewLabel_5);

        lblNewLabel_7 = new JLabel("등록 금액");
        lblNewLabel_7.setBounds(410, 167, 86, 18);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_8 = new JLabel("일련번호");
        lblNewLabel_8.setBounds(410, 221, 86, 15);
        contentPane.add(lblNewLabel_8);

        textField_10 = new JTextField();
        textField_10.setBounds(524, 221, 268, 21);
        contentPane.add(textField_10);

        btnNewButton = new JButton("등록");
        btnNewButton.addActionListener(e -> {
            try {
                // 텍스트 필드에서 입력값 가져오기
                String 강의명 = textField.getText();
                String 이름 = textField_1.getText();
                String 제목 = textField_2.getText();
                String 저자 = textField_3.getText();
                String 출판사 = textField_5.getText();
                String 책상태 = textField_9.getText();
                String 등록가격 = textField_4.getText();
                String 일련번호 = textField_10.getText();

                // 입력값 검증
                if (강의명.isEmpty() || 이름.isEmpty() || 제목.isEmpty() || 저자.isEmpty() || 출판사.isEmpty() || 책상태.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "모든 필드를 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String 판매자학번 = EV.ID; // 현재 사용자의 ID를 가져옵니다.

                processRegistration(강의명, 이름, 제목, 저자, 출판사, 등록가격, 책상태, 판매자학번, 일련번호);
                JOptionPane.showMessageDialog(contentPane, "등록이 완료되었습니다.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(contentPane, "등록 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnNewButton.setBounds(569, 354, 91, 23);
        contentPane.add(btnNewButton);


        lblNewLabel_11 = new JLabel("책 상태");
        lblNewLabel_11.setBounds(410, 119, 86, 15);
        contentPane.add(lblNewLabel_11);

        textField_9 = new JTextField();
        textField_9.setColumns(10);
        textField_9.setBounds(524, 116, 268, 21);
        contentPane.add(textField_9);
    }

    public static void processRegistration(String 강의명, String 이름, String 제목, String 저자,
                                           String 출판사, String 등록가격, String 책상태, String 판매자학번, String 일련번호) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            // Step 1: Check if the book exists
            pstmt = conn.prepareStatement("SELECT 일련번호 FROM 책 WHERE 제목 = ? AND 저자 = ?");
            pstmt.setString(1, 제목);
            pstmt.setString(2, 저자);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                일련번호 = rs.getString("일련번호");
            } else {
                // Insert new book
                pstmt.close();
                pstmt = conn.prepareStatement("INSERT INTO 책 (일련번호, 제목, 저자, 출판사) VALUES (?, ?, ?, ?)");
                pstmt.setString(1, 일련번호);
                pstmt.setString(2, 제목);
                pstmt.setString(3, 저자);
                pstmt.setString(4, 출판사);
                pstmt.executeUpdate();
            }

            // Step 2: Check if the lecture exists
            String 강의번호 = null;

            pstmt = conn.prepareStatement("SELECT 강의번호 FROM 강의 WHERE 강의명 = ?");
            pstmt.setString(1, 강의명);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                강의번호 = rs.getString("강의번호");
            } else {
                // Insert new lecture if not exists
                String 교수번호 = null;
                pstmt.close();
                pstmt = conn.prepareStatement("SELECT 교수번호 FROM 교수 WHERE 이름 = ?");
                pstmt.setString(1, 이름);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    교수번호 = rs.getString("교수번호");
                } else {
                    throw new SQLException("교수 정보를 찾을 수 없습니다.");
                }

                pstmt.close();
                pstmt = conn.prepareStatement("INSERT INTO 강의 (강의번호, 강의명, 담당교수번호) VALUES ((SELECT NVL(MAX(강의번호), 0) + 1 FROM 강의), ?, ?)");
                pstmt.setString(1, 강의명);
                pstmt.setString(2, 교수번호);
                pstmt.executeUpdate();

                // Retrieve the newly inserted lecture number
                pstmt.close();
                pstmt = conn.prepareStatement("SELECT 강의번호 FROM 강의 WHERE 강의명 = ?");
                pstmt.setString(1, 강의명);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    강의번호 = rs.getString("강의번호");
                }
            }

            pstmt.close();
            pstmt = conn.prepareStatement("SELECT 사용번호 FROM 사용 WHERE 강의번호 = ? AND 일련번호 = ?");
            pstmt.setString(1, 강의번호);
            pstmt.setString(2, 일련번호);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                // Insert new record into 사용 테이블
                pstmt.close();
                pstmt = conn.prepareStatement("INSERT INTO 사용 (사용번호, 강의번호, 일련번호) VALUES ((SELECT NVL(MAX(사용번호), 0) + 1 FROM 사용), ?, ?)");
                pstmt.setString(1, 강의번호);
                pstmt.setString(2, 일련번호);
                pstmt.executeUpdate();
            }


            // Step 3: Insert into 등록내역 테이블
            pstmt.close();
            pstmt = conn.prepareStatement(
                    "INSERT INTO 등록내역 (등록번호, 등록일자, 판매여부, 등록가격, 책상태, 일련번호, 판매자학번) " +
                            "VALUES ((SELECT NVL(MAX(등록번호), 0) + 1 FROM 등록내역), SYSDATE, '판매', ?, ?, ?, ?)");
            pstmt.setString(1, 등록가격);
            pstmt.setString(2, 책상태);
            pstmt.setString(3, 일련번호);
            pstmt.setString(4, 판매자학번);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}