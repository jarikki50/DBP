package GUI;

import model.EV;
import DB_connection.DB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class deallist extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textFieldStartDate;
    private JTextField textFieldEndDate;
    private static JLabel lblBuyerCount;
    private static JLabel lblTotalBuyerAmount;
    private static JLabel lblSellerCount;
    private static JLabel lblTotalSellerAmount;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                deallist frame = new deallist();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public deallist() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1028, 610);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 255));
        panel.setBounds(0, 0, 1014, 36);
        contentPane.add(panel);

        JLabel lblTitle = new JLabel("동의책방");
        lblTitle.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 18));
        panel.add(lblTitle);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(45, 141, 642, 402);
        contentPane.add(scrollPane);

        JLabel lblTransactionList = new JLabel("거래 목록");
        lblTransactionList.setBounds(45, 116, 135, 15);
        contentPane.add(lblTransactionList);

        textFieldStartDate = new JTextField();
        textFieldStartDate.setBounds(370, 110, 96, 21);
        contentPane.add(textFieldStartDate);
        textFieldStartDate.setColumns(10);

        textFieldEndDate = new JTextField();
        textFieldEndDate.setColumns(10);
        textFieldEndDate.setBounds(488, 110, 96, 21);
        contentPane.add(textFieldEndDate);

        JButton btnSearch = new JButton("검색");
        btnSearch.setBounds(596, 112, 91, 23);
        contentPane.add(btnSearch);

        JLabel lblTo = new JLabel("~");
        lblTo.setBounds(473, 116, 25, 15);
        contentPane.add(lblTo);

        JLabel lblPeriod = new JLabel("기간");
        lblPeriod.setBounds(319, 116, 50, 15);
        contentPane.add(lblPeriod);

        JLabel lblBuyerCountLabel = new JLabel("구매 횟수:");
        lblBuyerCountLabel.setBounds(716, 227, 108, 15);
        contentPane.add(lblBuyerCountLabel);

        lblBuyerCount = new JLabel("0");
        lblBuyerCount.setBounds(836, 227, 108, 15);
        contentPane.add(lblBuyerCount);

        JLabel lblTotalBuyerAmountLabel = new JLabel("총 구매 금액:");
        lblTotalBuyerAmountLabel.setBounds(716, 298, 108, 15);
        contentPane.add(lblTotalBuyerAmountLabel);

        lblTotalBuyerAmount = new JLabel("0");
        lblTotalBuyerAmount.setBounds(836, 298, 108, 15);
        contentPane.add(lblTotalBuyerAmount);

        JLabel lblSellerCountLabel = new JLabel("판매 횟수:");
        lblSellerCountLabel.setBounds(716, 382, 108, 15);
        contentPane.add(lblSellerCountLabel);

        lblSellerCount = new JLabel("0");
        lblSellerCount.setBounds(836, 382, 108, 15);
        contentPane.add(lblSellerCount);

        JLabel lblTotalSellerAmountLabel = new JLabel("총 판매 금액:");
        lblTotalSellerAmountLabel.setBounds(716, 448, 108, 15);
        contentPane.add(lblTotalSellerAmountLabel);

        lblTotalSellerAmount = new JLabel("0");
        lblTotalSellerAmount.setBounds(836, 448, 108, 15);
        contentPane.add(lblTotalSellerAmount);

        // 초기 데이터 로드
        fetchData(EV.ID, table);

        // 검색 버튼 클릭 이벤트
        btnSearch.addActionListener(e -> {
            String startDate = textFieldStartDate.getText();
            String endDate = textFieldEndDate.getText();
            fetchDataByDate(EV.ID, startDate, endDate);
        });

        // 총 구매 및 판매 금액 계산
        calculateTotalAmounts(EV.ID);
    }

    public static void fetchData(String userId, JTable table) {
        String query = """
            SELECT 
                거래내역.거래번호 AS 거래번호,
                책.제목 AS 책제목,
                거래장소.건물번호 AS 건물번호,
                거래장소.강의실 AS 강의실,
                거래내역.가격 AS 거래가격,
                거래내역.거래날짜 AS 거래날짜,
                거래내역.판매자학번 AS 판매자학번,
                거래내역.구매자학번 AS 구매자학번
            FROM 거래내역
            LEFT JOIN 등록내역 ON 거래내역.등록번호 = 등록내역.등록번호
            LEFT JOIN 책 ON 등록내역.일련번호 = 책.일련번호
            LEFT JOIN 거래장소 ON 거래내역.장소번호 = 거래장소.장소번호
            WHERE 거래내역.판매자학번 = ? OR 거래내역.구매자학번 = ?
        """;

        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("거래번호");
                model.addColumn("책 제목");
                model.addColumn("건물번호");
                model.addColumn("강의실");
                model.addColumn("거래가격");
                model.addColumn("거래날짜");

                int buyerCount = 0;
                int sellerCount = 0;

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("거래번호"));
                    row.add(rs.getString("책제목"));
                    row.add(rs.getString("건물번호"));
                    row.add(rs.getString("강의실"));
                    row.add(rs.getInt("거래가격"));
                    row.add(rs.getDate("거래날짜"));
                    model.addRow(row);

                    // 구매 및 판매 횟수 계산
                    String sellerId = rs.getString("판매자학번");
                    String buyerId = rs.getString("구매자학번");

                    if (userId.equals(sellerId)) {
                        sellerCount++;
                    }
                    if (userId.equals(buyerId)) {
                        buyerCount++;
                    }
                }

                table.setModel(model);

                // 구매 및 판매 횟수 업데이트
                lblBuyerCount.setText(String.valueOf(buyerCount));
                lblSellerCount.setText(String.valueOf(sellerCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 로드 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void calculateTotalAmounts(String userId) {
        String callProcedure = "{call Get_User_Trade_Summary(?, ?, ?)}";

        try (Connection conn = DB.getConnection();
             CallableStatement cstmt = conn.prepareCall(callProcedure)) {

            cstmt.setInt(1, Integer.parseInt(userId)); // 사용자 ID 설정
            cstmt.registerOutParameter(2, Types.NUMERIC); // 총 구매 금액
            cstmt.registerOutParameter(3, Types.NUMERIC); // 총 판매 금액

            cstmt.execute();

            // 프로시저 결과 가져오기
            int totalPurchase = cstmt.getInt(2);
            int totalSales = cstmt.getInt(3);

            lblTotalBuyerAmount.setText(String.valueOf(totalPurchase));
            lblTotalSellerAmount.setText(String.valueOf(totalSales));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "총 구매 및 판매 금액 계산 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchDataByDate(String userId, String startDate, String endDate) {
        String query = """
            SELECT 
                거래내역.거래번호 AS 거래번호,
                책.제목 AS 책제목,
                거래장소.건물번호 AS 건물번호,
                거래장소.강의실 AS 강의실,
                거래내역.가격 AS 거래가격,
                거래내역.거래날짜 AS 거래날짜
            FROM 거래내역
            LEFT JOIN 등록내역 ON 거래내역.등록번호 = 등록내역.등록번호
            LEFT JOIN 책 ON 등록내역.일련번호 = 책.일련번호
            LEFT JOIN 거래장소 ON 거래내역.장소번호 = 거래장소.장소번호
            WHERE (거래내역.판매자학번 = ? OR 거래내역.구매자학번 = ?)
              AND 거래내역.거래날짜 BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')
        """;

        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("거래번호");
                model.addColumn("책 제목");
                model.addColumn("건물번호");
                model.addColumn("강의실");
                model.addColumn("거래가격");
                model.addColumn("거래날짜");

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("거래번호"));
                    row.add(rs.getString("책제목"));
                    row.add(rs.getString("건물번호"));
                    row.add(rs.getString("강의실"));
                    row.add(rs.getInt("거래가격"));
                    row.add(rs.getDate("거래날짜"));
                    model.addRow(row);
                }
                table.setModel(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 검색 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
