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
import java.sql.SQLException;

public class mypage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    mypage frame = new mypage();
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
    public mypage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 714, 445);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 710, 36);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("동의책방");
        lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("학번");
        lblNewLabel_1.setBounds(351, 107, 35, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel(EV.ID);
        lblNewLabel_2.setBounds(421, 107, 100, 15);
        contentPane.add(lblNewLabel_2);

        JButton btnNewButton = new JButton("나의 구매 신청 보기");
        btnNewButton.setBounds(39, 347, 196, 23);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // buylist.java 열기
                buylist buylist = new buylist();
                buylist.setVisible(true); // 창 보이게 설정
            }
        });

        JButton btnNewButton_1 = new JButton("나의 판매 신청 보기");
        btnNewButton_1.setBounds(247, 347, 196, 23);
        contentPane.add(btnNewButton_1);

        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // buylist.java 열기
                selllist selllist = new selllist();
                selllist.setVisible(true); // 창 보이게 설정
            }
        });

        JButton btnNewButton_2 = new JButton("나의 거래 목록 보기");
        btnNewButton_2.setBounds(457, 347, 196, 23);
        contentPane.add(btnNewButton_2);

        btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // buylist.java 열기
                deallist deallist = new deallist();
                deallist.setVisible(true); // 창 보이게 설정
            }
        });

        JLabel lblNewLabel_3 = new JLabel("PWD");
        lblNewLabel_3.setBounds(351, 164, 58, 15);
        contentPane.add(lblNewLabel_3);

        textField = new JTextField();
        textField.setBounds(421, 161, 164, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_3_1 = new JLabel("이메일");
        lblNewLabel_3_1.setBounds(351, 199, 61, 15);
        contentPane.add(lblNewLabel_3_1);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(421, 196, 118, 21);
        contentPane.add(textField_1);

        JLabel lblNewLabel_4 = new JLabel("@");
        lblNewLabel_4.setBounds(539, 199, 16, 15);
        contentPane.add(lblNewLabel_4);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(557, 196, 108, 21);
        contentPane.add(textField_2);

        JLabel lblNewLabel_5 = new JLabel("전화번호");
        lblNewLabel_5.setBounds(336, 234, 73, 15);
        contentPane.add(lblNewLabel_5);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(421, 231, 61, 21);
        contentPane.add(textField_3);

        JLabel lblNewLabel_4_1 = new JLabel("-");
        lblNewLabel_4_1.setBounds(494, 234, 16, 15);
        contentPane.add(lblNewLabel_4_1);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(512, 231, 61, 21);
        contentPane.add(textField_4);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(604, 231, 61, 21);
        contentPane.add(textField_5);

        JLabel lblNewLabel_4_1_1 = new JLabel("-");
        lblNewLabel_4_1_1.setBounds(587, 234, 16, 15);
        contentPane.add(lblNewLabel_4_1_1);

        JButton btnNewButton_3 = new JButton("정보 수정");
        btnNewButton_3.setBounds(421, 281, 91, 23);
        contentPane.add(btnNewButton_3);

        JLabel lblNewLabel_1_1 = new JLabel("학번");
        lblNewLabel_1_1.setBounds(82, 107, 35, 15);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_3_2 = new JLabel("PWD");
        lblNewLabel_3_2.setBounds(82, 167, 62, 15);
        contentPane.add(lblNewLabel_3_2);

        JLabel lblNewLabel_3_1_1 = new JLabel("전화번호");
        lblNewLabel_3_1_1.setBounds(82, 231, 62, 15);
        contentPane.add(lblNewLabel_3_1_1);

        JLabel lblNewLabel_5_1 = new JLabel("이메일");
        lblNewLabel_5_1.setBounds(82, 199, 77, 15);
        contentPane.add(lblNewLabel_5_1);

        JLabel lblNewLabel_2_1 = new JLabel(EV.ID);
        lblNewLabel_2_1.setBounds(156, 107, 100, 15);
        contentPane.add(lblNewLabel_2_1);

        JLabel lblNewLabel_6 = new JLabel(EV.pwd);
        lblNewLabel_6.setBounds(156, 164, 106, 15);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_6_1 = new JLabel(EV.phone);
        lblNewLabel_6_1.setBounds(156, 231, 106, 15);
        contentPane.add(lblNewLabel_6_1);

        JLabel lblNewLabel_6_2 = new JLabel(EV.email);
        lblNewLabel_6_2.setBounds(156, 196, 106, 15);
        contentPane.add(lblNewLabel_6_2);

        JLabel lblNewLabel_7 = new JLabel("현재 정보");
        lblNewLabel_7.setBounds(101, 63, 100, 15);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_7_1 = new JLabel("정보 수정");
        lblNewLabel_7_1.setBounds(450, 63, 100, 15);
        contentPane.add(lblNewLabel_7_1);

        JLabel lblNewLabel_5_1_1 = new JLabel("이름");
        lblNewLabel_5_1_1.setBounds(82, 139, 77, 15);
        contentPane.add(lblNewLabel_5_1_1);

        JLabel lblNewLabel_6_1_1 = new JLabel(EV.Na);
        lblNewLabel_6_1_1.setBounds(156, 139, 106, 15);
        contentPane.add(lblNewLabel_6_1_1);

        JLabel lblNewLabel_3_3 = new JLabel("이름");
        lblNewLabel_3_3.setBounds(351, 132, 58, 15);
        contentPane.add(lblNewLabel_3_3);

        textField_6 = new JTextField();
        textField_6.setColumns(10);
        textField_6.setBounds(421, 132, 164, 21);
        contentPane.add(textField_6);

        btnNewButton_3.addActionListener(e -> {

            updateUserInfo();

            refreshFrame();
        });
    }

    private void updateUserInfo() {
        String password = textField.getText();
        String email = textField_1.getText() + "@" + textField_2.getText();
        String phoneNumber = textField_3.getText() + "-" + textField_4.getText() + "-" + textField_5.getText();
        String name = textField_6.getText();

        try (Connection conn = DB.getConnection()) {
            String updateQuery = "UPDATE 사용자 SET 비밀번호 = ?, 이메일 = ?, 전화번호 = ?, 이름 = ? WHERE 학번 = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, password);
                stmt.setString(2, email);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, name);
                stmt.setString(5, EV.ID);  // EV.ID로 학번 값을 설정
                int rowsUpdated = stmt.executeUpdate();
                EV.pwd = password;
                EV.email = email;
                EV.phone = phoneNumber;
                EV.Na = name;
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "정보가 수정되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "정보 수정에 실패했습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 오류");
        }
    }

    private void refreshFrame() {
        // 현재 창 닫기
        this.dispose();

        // 새 창 열기
        EventQueue.invokeLater(() -> {
            try {
                mypage frame = new mypage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}