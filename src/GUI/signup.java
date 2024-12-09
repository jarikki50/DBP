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

public class signup extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_7;

    private int Q = 0;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    signup frame = new signup();
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
    public signup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 451, 372);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(69, 90, 206, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(69, 121, 206, 21);
        contentPane.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(69, 152, 334, 21);
        contentPane.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(69, 183, 180, 21);
        contentPane.add(textField_3);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(69, 214, 87, 21);
        contentPane.add(textField_4);

        JLabel lblNewLabel = new JLabel("학번");
        lblNewLabel.setBounds(12, 93, 50, 15);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("이름");
        lblNewLabel_1.setBounds(12, 124, 50, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("PWD");
        lblNewLabel_2.setBounds(12, 155, 50, 15);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("이메일");
        lblNewLabel_3.setBounds(7, 186, 50, 15);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("전화번호");
        lblNewLabel_4.setBounds(12, 217, 50, 15);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("@");
        lblNewLabel_5.setBounds(254, 186, 22, 15);
        contentPane.add(lblNewLabel_5);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(274, 183, 129, 21);
        contentPane.add(textField_5);

        JLabel lblNewLabel_6 = new JLabel("-");
        lblNewLabel_6.setBounds(168, 217, 14, 15);
        contentPane.add(lblNewLabel_6);

        textField_6 = new JTextField();
        textField_6.setColumns(10);
        textField_6.setBounds(185, 214, 96, 21);
        contentPane.add(textField_6);

        textField_7 = new JTextField();
        textField_7.setColumns(10);
        textField_7.setBounds(310, 214, 96, 21);
        contentPane.add(textField_7);

        JLabel lblNewLabel_6_1 = new JLabel("-");
        lblNewLabel_6_1.setBounds(294, 217, 14, 15);
        contentPane.add(lblNewLabel_6_1);

        JLabel lblNewLabel_7_1 = new JLabel("");
        lblNewLabel_7_1.setBounds(30, 250, 243, 15);
        contentPane.add(lblNewLabel_7_1);

        JButton btnNewButton = new JButton("중복 확인");
        btnNewButton.setBounds(294, 89, 91, 23);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 중복 확인 메소드 호출
                String studentId = textField.getText();
                try (Connection conn = DB.getConnection()) {
                    String sql = "SELECT * FROM 사용자 WHERE 학번 = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, studentId);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            Q = 0;  // 이미 존재하는 학번
                            lblNewLabel_7_1.setText("이미 가입된 학번입니다.");
                        } else {
                            Q = 1;  // 사용 가능한 학번
                            lblNewLabel_7_1.setText("사용 가능합니다.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("회원 가입");
        btnNewButton_1.setBounds(184, 275, 91, 23);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 중복 확인 후 회원가입 메소드 호출
                if (Q == 1) {
                    String studentId = textField.getText();
                    String name = textField_1.getText();
                    String password = textField_2.getText();
                    String email = textField_3.getText() + "@" + textField_5.getText();
                    String phone = textField_4.getText() + '-' + textField_6.getText() + '-' + textField_7.getText();

                    try (Connection conn = DB.getConnection()) {
                        String sql = "INSERT INTO 사용자 (학번, 이름, 비밀번호, 이메일, 전화번호) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, studentId);
                            pstmt.setString(2, name);
                            pstmt.setString(3, password);
                            pstmt.setString(4, email);
                            pstmt.setString(5, phone);
                            int result = pstmt.executeUpdate();

                            if (result > 0) {
                                lblNewLabel_7_1.setText("회원 가입 성공!");
                            } else {
                                lblNewLabel_7_1.setText("회원 가입 실패.");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        contentPane.add(btnNewButton_1);


        JLabel lblNewLabel_7 = new JLabel("회원 가입");
        lblNewLabel_7.setBounds(185, 32, 100, 15);
        contentPane.add(lblNewLabel_7);

    }
}
