package GUI;

import model.EV;
import DB_connection.DB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JButton btnNewButton_1;
    private JButton btnNewButton;
    private JTextField textField_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DB.getConnection();
                    login frame = new login();
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
    public login() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 351);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(81, 94, 267, 28);
        contentPane.add(textField);
        textField.setColumns(10);

        btnNewButton_1 = new JButton("로그인");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String 학번 = textField.getText();
                String 비밀번호 = textField_1.getText();

                // 로그인 검증
                if (validateLogin(학번, 비밀번호)) {
                    main mainFrame = new main(); // main 객체 생성
                    mainFrame.setVisible(true);
                    System.out.println(EV.ID);
                    dispose(); // 현재 login 창 닫기
                } else {
                    // 로그인 실패 시 메시지 출력
                    System.out.println("로그인 실패");
                }
            }
        });

        btnNewButton_1.setBounds(114, 188, 220, 23);
        contentPane.add(btnNewButton_1);

        btnNewButton = new JButton("회원가입");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signup signupFrame = new signup(); // signup 객체 생성
                signupFrame.setVisible(true);
            }
        });
        btnNewButton.setBounds(114, 236, 220, 23);
        contentPane.add(btnNewButton);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(81, 136, 267, 28);
        contentPane.add(textField_1);

        JLabel lblNewLabel = new JLabel("로그인");
        lblNewLabel.setBounds(199, 49, 39, 15);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("학번");
        lblNewLabel_1.setBounds(38, 100, 31, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("비밀번호");
        lblNewLabel_2.setBounds(19, 142, 50, 15);
        contentPane.add(lblNewLabel_2);
    }

    private boolean validateLogin(String 학번, String 비밀번호) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // DB 연결
            conn = DB.getConnection();
            String sql = "SELECT 학번, 이름, 이메일, 전화번호, 비밀번호 FROM 사용자 WHERE 학번 = ? AND 비밀번호 = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, 학번);
            stmt.setString(2, 비밀번호);

            rs = stmt.executeQuery();

            // 학번과 비밀번호가 일치하는 튜플이 있는지 확인
            if (rs.next()) {
                // EV 객체에 데이터 설정
                EV.ID = rs.getString("학번");
                EV.Na = rs.getString("이름");
                EV.email = rs.getString("이메일");
                EV.phone = rs.getString("전화번호");
                EV.pwd = rs.getString("비밀번호");
                return true; // 로그인 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 정리
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // 로그인 실패
    }
}
