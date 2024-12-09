    package GUI;

    import model.EV;
    import DB_connection.DB;
    import java.awt.EventQueue;

    import javax.swing.*;
    import javax.swing.border.EmptyBorder;
    import java.awt.Color;
    import java.awt.Font;
    import java.awt.Choice;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.JTable;
    import java.awt.event.ItemEvent;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.time.LocalDate;


    public class selllist extends JFrame {

        private static final long serialVersionUID = 1L;
        private JPanel contentPane;
        private JTable table_1; // 판매 신청 목록
        private static JTable table_2; // 구매 신청 목록
        private JButton btnNewButton;
        private JLabel lblNewLabel_1;
        private Choice choice; // 판매 신청 정렬
        private JLabel lblNewLabel_2;
        private JLabel lblNewLabel_3;
        private JLabel lblNewLabel_4;
        private Choice choice_1; // 구매 신청 정렬

        /**
         * Launch the application.
         */
        public static void main(String[] args) {
            EventQueue.invokeLater(() -> {
                try {
                    selllist frame = new selllist();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        /**
         * Create the frame.
         */
        public selllist() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 1178, 730);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

            setContentPane(contentPane);
            contentPane.setLayout(null);

            table_1 = new JTable();
            JScrollPane scrollPane = new JScrollPane(table_1);  // JTable을 JScrollPane에 감싸기
            scrollPane.setBounds(69, 196, 479, 406);
            contentPane.add(scrollPane);

            table_2 = new JTable();
            JScrollPane scroll2 = new JScrollPane(table_2);
            scroll2.setBounds(600, 196, 479, 406);
            contentPane.add(scroll2);

            JPanel panel = new JPanel();
            panel.setBackground(new Color(0, 128, 255));
            panel.setBounds(0, 0, 1174, 36);
            contentPane.add(panel);

            JLabel lblNewLabel = new JLabel("동의책방");
            lblNewLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
            panel.add(lblNewLabel);

            btnNewButton = new JButton("수락");
            btnNewButton.setBounds(782, 632, 91, 23);
            contentPane.add(btnNewButton);

            btnNewButton.addActionListener(e -> {

                int selectedRow = table_2.getSelectedRow();

                if (selectedRow < 0) {

                    JOptionPane.showMessageDialog(contentPane, "먼저 행을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                insertTrade(table_2, selectedRow, EV.SelectN);

                JOptionPane.showMessageDialog(contentPane, "거래내역에 성공적으로 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            });

            lblNewLabel_1 = new JLabel("판매 신청 목록");
            lblNewLabel_1.setBounds(69, 171, 242, 15);
            contentPane.add(lblNewLabel_1);

            choice = new Choice();
            choice.add("오름차순");
            choice.add("내림차순");
            choice.setBounds(452, 170, 95, 20);
            contentPane.add(choice);

            lblNewLabel_2 = new JLabel("정렬");
            lblNewLabel_2.setBounds(377, 171, 50, 15);
            contentPane.add(lblNewLabel_2);

            lblNewLabel_3 = new JLabel("구매 신청 목록");
            lblNewLabel_3.setBounds(600, 171, 242, 15);
            contentPane.add(lblNewLabel_3);

            lblNewLabel_4 = new JLabel("정렬");
            lblNewLabel_4.setBounds(908, 171, 50, 15);
            contentPane.add(lblNewLabel_4);

            choice_1 = new Choice();
            choice_1.add("오름차순");
            choice_1.add("내림차순");
            choice_1.setBounds(983, 170, 95, 20);
            contentPane.add(choice_1);

            // 정렬 변경 이벤트 리스너
            choice.addItemListener(evt -> {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    String sortOption = choice.getSelectedItem();
                    loadSellist(table_1, EV.ID, sortOption);
                }
            });

            choice_1.addItemListener(evt -> {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    String sortOption = choice_1.getSelectedItem();
                    loadTableData(table_2, sortOption);
                }
            });

            table_1.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // 더블클릭인지 확인
                    if (evt.getClickCount() == 2) {
                        int selectedRow = table_1.getSelectedRow(); // 선택된 행의 인덱스 가져오기
                        if (selectedRow != -1) { // 유효한 행인지 확인
                            // 테이블에서 '등록번호' 열의 데이터 가져오기
                            Object registrationNumber = table_1.getValueAt(selectedRow, 0);

                            EV.SelectN = registrationNumber.toString();
                            System.out.println(EV.SelectN);
                            loadTableData(table_2, "신청일자 오름차순"); // 기본 정렬로 구매 신청 목록 로드
                        }
                    }
                }
            });

            // 기본 데이터 로드
            loadSellist(table_1, EV.ID, "오름차순");
        }

        public static void loadSellist(JTable table, String evId, String sortOption) {
            Connection connection = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                connection = DB.getConnection();

                String sortColumn = "등록내역.등록일자";
                String sortOrder = sortOption.contains("오름차순") ? "ASC" : "DESC";

                // SQL 쿼리 작성
                String query = "SELECT " +
                        "등록내역.등록번호 AS 등록번호, " +
                        "(SELECT 제목 FROM 책 WHERE 일련번호 = 등록내역.일련번호) AS 책제목, " +
                        "등록내역.등록가격 AS 등록가격, " +
                        "등록내역.판매여부 AS 판매, " +
                        "등록내역.등록일자 AS 등록일자, " +
                        "(SELECT COUNT(*) FROM 신청내역 WHERE 등록내역.등록번호 = 신청내역.등록번호) AS 신청자수 " +
                        "FROM 등록내역 " +
                        "WHERE 판매자학번 = ? " +
                        "ORDER BY " + sortColumn + " " + sortOrder;

                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, evId);

                rs = pstmt.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("등록번호");
                model.addColumn("책 제목");
                model.addColumn("등록가격");
                model.addColumn("판매");
                model.addColumn("등록일자");
                model.addColumn("신청자 수");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("등록번호"),
                            rs.getString("책제목"),
                            rs.getDouble("등록가격"),
                            rs.getString("판매"),
                            rs.getString("등록일자"),
                            rs.getInt("신청자수")
                    });
                }

                table.setModel(model);
                table.setDefaultEditor(Object.class, null);

            } catch (SQLException e) {
                System.out.println("데이터베이스 오류: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    System.out.println("자원 해제 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        public static void loadTableData(JTable table, String sortOption) {
            String query = """
                SELECT 
                    신청내역.신청번호 AS 신청번호,
                    책.제목 AS 책제목,
                    사용자.이름 AS 구매자,
                    거래장소.건물번호 AS 건물번호,
                    거래장소.강의실 AS 강의실,
                    신청내역.신청가격 AS 신청가격,
                    신청내역.신청일자 AS 신청일자
                FROM 신청내역
                JOIN 등록내역 ON 신청내역.등록번호 = 등록내역.등록번호
                JOIN 책 ON 등록내역.일련번호 = 책.일련번호
                JOIN 사용자 ON 신청내역.구매자학번 = 사용자.학번
                JOIN 거래장소 ON 신청내역.장소번호 = 거래장소.장소번호
                WHERE 신청내역.등록번호 = ?
                ORDER BY 신청내역.신청일자 """ + " " +(sortOption.contains("오름차순") ? "ASC" : "DESC");

            try (Connection conn = DB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, Integer.parseInt(EV.SelectN));
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                        new Object[][]{},
                        new String[]{"신청번호", "책제목", "구매자", "건물번호", "강의실", "신청가격", "신청일자"}
                );

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("신청번호"),
                            rs.getString("책제목"),
                            rs.getString("구매자"),
                            rs.getString("건물번호"),
                            rs.getString("강의실"),
                            rs.getDouble("신청가격"),
                            rs.getDate("신청일자")
                    });
                }
                table.setModel(model);
                table.setDefaultEditor(Object.class, null);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public static void insertTrade(JTable table_2, int selectedRow, String EV_SelectN) {
            if (selectedRow < 0) {
                System.out.println("행을 선택해주세요.");
                return;
            }

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DB.getConnection();
                conn.setAutoCommit(false);


                String sqlGetMaxTradeNumber = """
                SELECT NVL(MAX(거래번호), 0) + 1 AS 새거래번호 FROM 거래내역
            """;
                pstmt = conn.prepareStatement(sqlGetMaxTradeNumber);
                rs = pstmt.executeQuery();
                int newTradeNumber = rs.next() ? rs.getInt("새거래번호") : 1;
                pstmt.close();


                String requestNumber = table_2.getValueAt(selectedRow, 0).toString();


                String sqlGetRequestDetails = """
                SELECT 신청일자, 신청가격, 구매자학번, 장소번호 
                FROM 신청내역 
                WHERE 신청번호 = ?
            """;
                pstmt = conn.prepareStatement(sqlGetRequestDetails);
                pstmt.setString(1, requestNumber);
                rs = pstmt.executeQuery();

                LocalDate tradeDate = null;
                int tradePrice = 0;
                int buyerId = 0;
                int placeId = 0;

                if (rs.next()) {
                    tradeDate = rs.getDate("신청일자").toLocalDate();
                    tradePrice = rs.getInt("신청가격");
                    buyerId = rs.getInt("구매자학번");
                    placeId = rs.getInt("장소번호");
                }
                pstmt.close();


                String sqlGetSellerId = """
                SELECT 판매자학번 
                FROM 등록내역 
                WHERE 등록번호 = ?
            """;
                pstmt = conn.prepareStatement(sqlGetSellerId);
                pstmt.setString(1, EV_SelectN);
                rs = pstmt.executeQuery();

                int sellerId = 0;
                if (rs.next()) {
                    sellerId = rs.getInt("판매자학번");
                }
                pstmt.close();


                String sqlInsertTrade = """
                INSERT INTO 거래내역 (
                    거래번호, 거래날짜, 가격, 판매자학번, 구매자학번, 등록번호, 장소번호
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
                pstmt = conn.prepareStatement(sqlInsertTrade);
                pstmt.setInt(1, newTradeNumber);
                pstmt.setDate(2, java.sql.Date.valueOf(tradeDate));
                pstmt.setInt(3, tradePrice);
                pstmt.setInt(4, sellerId);
                pstmt.setInt(5, buyerId);
                pstmt.setString(6, EV_SelectN);
                pstmt.setInt(7, placeId);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("거래내역이 성공적으로 추가되었습니다.");
                }

                conn.commit();
            } catch (SQLException e) {
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
