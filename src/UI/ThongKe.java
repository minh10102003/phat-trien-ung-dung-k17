package UI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import DAO.DAO_Ghe;
import DAO.DAO_Toa;
import entity.Ghe;
import entity.ToaEntity;

import javax.swing.*;

import java.awt.BorderLayout;
import java.sql.*;
import java.util.List;

public class ThongKe extends JPanel {

    private JPanel chartPanel;

    public ThongKe() {
        setLayout(new BorderLayout());
        chartPanel = new JPanel();
        add(chartPanel, BorderLayout.CENTER);

        // Vẽ biểu đồ từ số liệu trong cơ sở dữ liệu
        generateBarChart();
    }

    // Phương thức tạo biểu đồ cột hiển thị số lượng ghế theo toa tàu
    private void generateBarChart() {
        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Lấy danh sách toa tàu từ cơ sở dữ liệu
        List<ToaEntity> toas = DAO_Toa.getAllToa();  // Giả sử bạn có một phương thức DAO để lấy danh sách toa
        for (ToaEntity toa : toas) {
            // Lấy số lượng ghế trong toa từ DAO_Ghe
            List<Integer> gheList = DAO_Ghe.layDanhSachGheTheoMaToa(toa.getMaToa());
            int totalSeats = gheList.size();

            // Thêm dữ liệu vào dataset (Tên toa và số ghế)
            dataset.addValue(totalSeats, "Số ghế", toa.getTenToa());
        }

        // Tạo biểu đồ cột
        JFreeChart chart = ChartFactory.createBarChart(
                "Số Ghế Theo Từng Toa Tàu", // Tiêu đề biểu đồ
                "Tên Toa",  // Nhãn trục X
                "Số Ghế",   // Nhãn trục Y
                dataset,    // Dữ liệu biểu đồ
                PlotOrientation.VERTICAL,  // Hướng biểu đồ (cột dọc)
                true,       // Bao gồm chú thích
                true,       // Hiển thị công cụ
                false       // Hiển thị URL
        );

        // Hiển thị biểu đồ trên JPanel
        ChartPanel chartPanel1 = new ChartPanel(chart);
        chartPanel1.setPreferredSize(new java.awt.Dimension(800, 600));  // Đặt kích thước cho biểu đồ
        chartPanel.removeAll();
        chartPanel.add(chartPanel1);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // Phương thức tạo biểu đồ tròn (Pie Chart)
    private void generatePieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Lấy danh sách toa tàu và số ghế tương ứng
        List<ToaEntity> toas = DAO_Toa.getAllToa();
        for (ToaEntity toa : toas) {
            List<Integer> gheList = DAO_Ghe.layDanhSachGheTheoMaToa(toa.getMaToa());
            int totalSeats = gheList.size();

            // Thêm dữ liệu vào dataset
            dataset.setValue(toa.getTenToa(), totalSeats);
        }

        // Tạo biểu đồ tròn
        JFreeChart chart = ChartFactory.createPieChart(
                "Số Ghế Theo Từng Toa Tàu",  // Tiêu đề biểu đồ
                dataset,  // Dữ liệu biểu đồ
                true,     // Hiển thị chú thích
                true,     // Hiển thị công cụ
                false     // Hiển thị URL
        );

        // Hiển thị biểu đồ trên JPanel
        ChartPanel chartPanel1 = new ChartPanel(chart);
        chartPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));  // Đặt kích thước cho biểu đồ
        chartPanel.removeAll();
        chartPanel.add(chartPanel1);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
}

