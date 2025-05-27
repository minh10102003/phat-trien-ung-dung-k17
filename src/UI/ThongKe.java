package UI;

import util.Session;
import DAO.DAO_ThongKe;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.awt.Color;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

public class ThongKe extends JPanel {
    private final String role;
    private final String maNV;

    public ThongKe() {
        this.role = Session.currentRole;
        this.maNV = Session.currentUsername != null
                  ? DAO.DAO_NhanVien.getMaNVByUsername(Session.currentUsername)
                  : null;

        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        if ("nhanvien".equals(role)) {
            tabs.addTab("Lượt vé",     createTicketCountPanel());
            tabs.addTab("Doanh thu",  createRevenuePanel());
        }

        if ("admin".equals(role)) {
            tabs.addTab("Số vé đã bán",    createSoldTicketsPanel());
//            tabs.addTab("Vé trả lại",      createRefundsPanel());
            tabs.addTab("Thông tin cá nhân", createPersonalInfoPanel());
        }

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createTicketCountPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        JComboBox<String> cbPeriod = new JComboBox<>(new String[]{"Ngày","Tuần","Tháng"});
        JDateChooser dcFrom = new JDateChooser();
        JDateChooser dcTo   = new JDateChooser();
        JButton btnGo = new JButton("Thống kê");
        top.add(new JLabel("Từ:")); top.add(dcFrom);
        top.add(new JLabel("Đến:")); top.add(dcTo);
        top.add(new JLabel("Loại:")); top.add(cbPeriod);
        top.add(btnGo);
        p.add(top, BorderLayout.NORTH);

        ChartPanel chartPanel = new ChartPanel(null);
        p.add(chartPanel, BorderLayout.CENTER);

        btnGo.addActionListener(e -> {
            Date from = dcFrom.getDate(), to = dcTo.getDate();
            if (from == null || to == null) {
                JOptionPane.showMessageDialog(this,
                    "Chọn đủ ngày bắt đầu và kết thúc",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Object[]> data;
            try {
                data = DAO_ThongKe.countTickets((String)cbPeriod.getSelectedItem(), from, to);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
                chartPanel.setChart(null);
                return;
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] row : data) {
                dataset.addValue((Number)row[1], "Số vé", row[0].toString());
            }
            JFreeChart chart = ChartFactory.createBarChart(
                "Thống kê lượt vé", "Khoảng", "Số vé", dataset
            );
            
            //custom màu cột	
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(52, 58, 235));
            
            chartPanel.setChart(chart);
        });

        return p;
    }

    private JPanel createRevenuePanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        JComboBox<String> cbPeriod = new JComboBox<>(new String[]{"Ngày","Tuần","Tháng"});
        JDateChooser dcFrom = new JDateChooser();
        JDateChooser dcTo   = new JDateChooser();
        JButton btnGo = new JButton("Thống kê");
        top.add(new JLabel("Từ:")); top.add(dcFrom);
        top.add(new JLabel("Đến:")); top.add(dcTo);
        top.add(new JLabel("Loại:")); top.add(cbPeriod);
        top.add(btnGo);
        p.add(top, BorderLayout.NORTH);

        ChartPanel chartPanel = new ChartPanel(null);
        p.add(chartPanel, BorderLayout.CENTER);

        btnGo.addActionListener(e -> {
            Date from = dcFrom.getDate(), to = dcTo.getDate();
            if (from == null || to == null) {
                JOptionPane.showMessageDialog(this,
                    "Chọn đủ ngày bắt đầu và kết thúc",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Object[]> data;
            try {
                data = DAO_ThongKe.totalRevenue((String)cbPeriod.getSelectedItem(), from, to);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
                chartPanel.setChart(null);
                return;
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] row : data) {
                dataset.addValue((Number)row[1], "Doanh thu", row[0].toString());
            }
            JFreeChart chart = ChartFactory.createBarChart(
                "Thống kê doanh thu", "Khoảng", "Doanh thu", dataset
            );
            
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            // đặt series 0 (vì ta chỉ có một series) thành màu RGB(52,58,235)
            renderer.setSeriesPaint(0, new Color(52, 58, 235));
            
            chartPanel.setChart(chart);
        });

        return p;
    }

    private JPanel createSoldTicketsPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        JComboBox<String> cbCriteria = new JComboBox<>(new String[]{"Ngày","Tháng","Tuyến"});
        JDateChooser dcFrom = new JDateChooser();
        JDateChooser dcTo   = new JDateChooser();
        JButton btnGo = new JButton("Thống kê");
        top.add(new JLabel("Từ:")); top.add(dcFrom);
        top.add(new JLabel("Đến:")); top.add(dcTo);
        top.add(new JLabel("Tiêu chí:")); top.add(cbCriteria);
        top.add(btnGo);
        p.add(top, BorderLayout.NORTH);

        ChartPanel chartPanel = new ChartPanel(null);
        p.add(chartPanel, BorderLayout.CENTER);

        btnGo.addActionListener(e -> {
            Date from = dcFrom.getDate(), to = dcTo.getDate();
            String crit = (String)cbCriteria.getSelectedItem();
            if (from == null || to == null) {
                JOptionPane.showMessageDialog(this,
                    "Chọn đủ ngày bắt đầu và kết thúc",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Object[]> data = DAO_ThongKe.countTicketsByCriteria(crit, from, to);
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
                chartPanel.setChart(null);
                return;
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] row : data) {
                dataset.addValue((Number)row[1], "Số vé", row[0].toString());
            }
            JFreeChart chart = ChartFactory.createBarChart(
                "Thống kê số vé đã bán", "Tiêu chí", "Số vé", dataset
            );
            
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            // đặt series 0 (vì ta chỉ có một series) thành màu RGB(52,58,235)
            renderer.setSeriesPaint(0, new Color(52, 58, 235));
            
            chartPanel.setChart(chart);
        });

        return p;
    }

//    private JPanel createRefundsPanel() {
//        JPanel p = new JPanel(new BorderLayout(10,10));
//        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
//        JDateChooser dcFrom = new JDateChooser();
//        JDateChooser dcTo   = new JDateChooser();
//        JTextField txtReason = new JTextField(10);
//        JButton btnGo = new JButton("Thống kê");
//        top.add(new JLabel("Từ:")); top.add(dcFrom);
//        top.add(new JLabel("Đến:")); top.add(dcTo);
//        top.add(new JLabel("Lý do:")); top.add(txtReason);
//        top.add(btnGo);
//        p.add(top, BorderLayout.NORTH);
//
//        ChartPanel chartPanel = new ChartPanel(null);
//        p.add(chartPanel, BorderLayout.CENTER);
//
//        btnGo.addActionListener(e -> {
//            Date from = dcFrom.getDate(), to = dcTo.getDate();
//            String reason = txtReason.getText().trim();
//            List<Object[]> data;
//            try {
//                data = DAO_ThongKe.refundedTickets(from, to, reason);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return;
//            }
//            if (data.isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                    "Không có dữ liệu", "Thông báo",
//                    JOptionPane.INFORMATION_MESSAGE);
//                chartPanel.setChart(null);
//                return;
//            }
//            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            for (Object[] row : data) {
//                dataset.addValue((Number)row[1], (String)row[2], row[0].toString());
//            }
//            JFreeChart chart = ChartFactory.createBarChart(
//                "Thống kê vé trả lại", "Ngày", "Số vé trả", dataset
//            );
//            chartPanel.setChart(chart);
//        });
//
//        return p;
//    }

    private JPanel createPersonalInfoPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        Object[] summary;
        try {
            summary = DAO_ThongKe.personalSummary(maNV);
        } catch (Exception ex) {
            ex.printStackTrace();
            summary = null;
        }
        if (summary == null) {
            p.add(new JLabel("Chưa có giao dịch nào"), BorderLayout.CENTER);
        } else {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue((Number)summary[0], "Giá trị", "Tổng vé bán");
            dataset.addValue((Number)summary[1], "Giá trị", "Tổng doanh thu");
            JFreeChart chart = ChartFactory.createBarChart(
                "Thông tin cá nhân", "Tiêu chí", "Giá trị", dataset
            );
            ChartPanel chartPanel = new ChartPanel(chart);
            p.add(chartPanel, BorderLayout.CENTER);
        }
        return p;
    }
}
