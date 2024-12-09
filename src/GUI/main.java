package GUI;

import function.BookSearch;
import function.BookSort;
import model.EV;
import DB_connection.DB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DB.getConnection();
                main frame = new main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1250, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 1246, 36);
        contentPane.add(panel);

        JLabel lblNewLabel = new JLabel("동의책방");
        lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblNewLabel);

        JButton btnMyPage = new JButton("마이 페이지");
        btnMyPage.setBounds(1034, 60, 190, 23);
        btnMyPage.addActionListener(e -> {
            mypage myPageFrame = new mypage();
            myPageFrame.setVisible(true);
        });
        contentPane.add(btnMyPage);

        Choice sortCriteriaChoice = new Choice();
        sortCriteriaChoice.add("책 제목");
        sortCriteriaChoice.add("등록일자");
        sortCriteriaChoice.add("제시 금액");
        sortCriteriaChoice.setBounds(96, 135, 110, 20);
        contentPane.add(sortCriteriaChoice);

        Choice sortOrderChoice = new Choice();
        sortOrderChoice.add("오름차순");
        sortOrderChoice.add("내림차순");
        sortOrderChoice.setBounds(250, 135, 110, 20);
        contentPane.add(sortOrderChoice);

        JButton sortButton = new JButton("정렬");
        sortButton.setBounds(400, 135, 91, 23);
        contentPane.add(sortButton);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(63, 165, 1109, 424);
        contentPane.add(scrollPane);

        Choice searchCriteriaChoice = new Choice();
        searchCriteriaChoice.add("제목");
        searchCriteriaChoice.add("저자");
        searchCriteriaChoice.setBounds(769, 135, 94, 20);
        contentPane.add(searchCriteriaChoice);

        textField = new JTextField();
        textField.setBounds(869, 134, 179, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton searchButton = new JButton("검색");
        searchButton.setBounds(1062, 132, 91, 23);
        contentPane.add(searchButton);

        JButton btnNewRegistration = new JButton("등록");
        btnNewRegistration.setBounds(1051, 617, 91, 23);
        btnNewRegistration.addActionListener(e -> {
            registration registrationPage = new registration();
            registrationPage.setVisible(true);
        });
        contentPane.add(btnNewRegistration);

        // BookSort 인스턴스 생성
        BookSort bookSort = new BookSort();

        // 정렬 버튼 클릭 이벤트 추가
        sortButton.addActionListener(e -> {
            String criteria = sortCriteriaChoice.getSelectedItem();
            String order = sortOrderChoice.getSelectedItem().equals("오름차순") ? "ASC" : "DESC";

            DefaultTableModel sortedModel = bookSort.sortBooks(criteria, order);
            table.setModel(sortedModel);
        });

        // BookSearch 인스턴스 생성
        BookSearch bookSearch = new BookSearch();

        // 검색 버튼 클릭 이벤트 추가
        searchButton.addActionListener(e -> {
            String searchCriteria = searchCriteriaChoice.getSelectedItem();
            String searchTerm = textField.getText().trim();

            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(contentPane, "검색어를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel searchResults = bookSearch.searchBooks(searchCriteria, searchTerm);
            if (searchResults.getRowCount() == 0) {
                JOptionPane.showMessageDialog(contentPane, "검색 결과가 없습니다.", "검색 결과 없음", JOptionPane.INFORMATION_MESSAGE);
            }
            table.setModel(searchResults);
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // 더블클릭인지 확인
                if (evt.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow(); // 선택된 행의 인덱스 가져오기
                    if (selectedRow != -1) { // 유효한 행인지 확인
                        // 테이블에서 '등록번호' 열의 데이터 가져오기
                        Object registrationNumber = table.getValueAt(selectedRow, 0); // '등록번호' 열의 인덱스 (여기서는 6번째 열)

                        // EV.SelectN에 값 저장 (EV는 Singleton이나 Static 클래스여야 합니다)
                        EV.SelectN = registrationNumber.toString();
                        System.out.println(EV.SelectN);  // 확인을 위한 출력

                        // request.java 열기
                        try {
                            request requestFrame = new request();
                            requestFrame.setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(contentPane, "request 창을 여는 중 오류가 발생했습니다.");
                        }
                    }
                }
            }
        });

        // 초기 데이터 로드
        loadBookUsageData();
    }

    private void loadBookUsageData() {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        table.setDefaultEditor(Object.class, null);

        String[] columnNames = {"등록번호", "등록일자", "제목", "저자", "출판사", "강의명", "교수명", "책상태", "등록가격", "평균가격"};
        model.setColumnIdentifiers(columnNames);

        try (Connection conn = DB.getConnection()) {
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
                WHERE 등록내역.판매여부 = '판매'
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[columnNames.length];
                row[0] = rs.getString("등록번호");
                row[1] = rs.getString("등록일자");
                row[2] = rs.getString("제목");
                row[3] = rs.getString("저자");
                row[4] = rs.getString("출판사");
                row[5] = rs.getString("강의명");
                row[6] = rs.getString("교수명");
                row[7] = rs.getString("책상태");
                row[8] = rs.getDouble("등록가격");
                row[9] = rs.getDouble("평균가격");

                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
