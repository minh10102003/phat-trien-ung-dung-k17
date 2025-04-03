package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class ChuyenTau extends JPanel {
    
    public ChuyenTau() {
    	setLayout(new BorderLayout(10, 10));
        
        // Create main content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }
    
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Quản lý chuyến tàu", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Search filters panel
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filtersPanel.setBackground(new Color(240, 240, 240));
        filtersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add filter components
        String[] filterLabels = {"Bắt Đầu (SGO)", "Phan Thiết (PTH)", "Thứ bảy, 30-11-2024", "Thứ sáu, 06-12-2024"};
        for (String label : filterLabels) {
            JPanel filterItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            filterItem.setBackground(Color.WHITE);
            filterItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JLabel filterLabel = new JLabel(label);
            filterItem.add(filterLabel);
            
            filtersPanel.add(filterItem);
        }
        
        JButton searchButton = new JButton("Tìm");
        searchButton.setBackground(new Color(249, 115, 22));
        searchButton.setForeground(Color.WHITE);
        filtersPanel.add(searchButton);
        
        // Trip selection panel
        JPanel tripPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Outbound trip panel
        JPanel outboundPanel = createTripPanel("Chọn chiều đi", "Sài Gòn → Phan Thiết");
        tripPanel.add(outboundPanel);
        
        // Return trip panel
        JPanel returnPanel = createTripPanel("Chọn chiều về", "Phan Thiết → Sài Gòn");
        tripPanel.add(returnPanel);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.add(filtersPanel, BorderLayout.NORTH);
        mainPanel.add(tripPanel, BorderLayout.CENTER);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTripPanel(String title, String route) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Route
        JLabel routeLabel = new JLabel(route, JLabel.CENTER);
        routeLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        // Date selection (simplified)
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.add(new JLabel("30-11-2024"));
        
        // Trip details
        JPanel tripDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tripDetailsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tripDetailsPanel.setPreferredSize(new Dimension(0, 150));
        
        JPanel trainInfoPanel = new JPanel(new GridLayout(1, 3));
        
        // Train info
        JPanel trainPanel = new JPanel();
        trainPanel.setLayout(new BoxLayout(trainPanel, BoxLayout.Y_AXIS));
        JLabel trainTypeLabel = new JLabel("Tàu du lịch");
        JLabel trainCodeLabel = new JLabel("SPT2");
        trainCodeLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        JLabel seatTypeLabel = new JLabel("Ghế ngồi mềm");
        seatTypeLabel.setBackground(new Color(254, 240, 138));
        seatTypeLabel.setOpaque(true);
        trainPanel.add(trainTypeLabel);
        trainPanel.add(trainCodeLabel);
        trainPanel.add(seatTypeLabel);
        trainInfoPanel.add(trainPanel);
        
        // Departure info
        JPanel departurePanel = new JPanel();
        departurePanel.setLayout(new BoxLayout(departurePanel, BoxLayout.Y_AXIS));
        JLabel departureStationLabel = new JLabel("Ga Sài Gòn", JLabel.CENTER);
        JLabel departureTimeLabel = new JLabel("06:30", JLabel.CENTER);
        departureTimeLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        JLabel departureDateLabel = new JLabel("30 tháng 11", JLabel.CENTER);
        departurePanel.add(departureStationLabel);
        departurePanel.add(departureTimeLabel);
        departurePanel.add(departureDateLabel);
        trainInfoPanel.add(departurePanel);
        
        // Arrival info
        JPanel arrivalPanel = new JPanel();
        arrivalPanel.setLayout(new BoxLayout(arrivalPanel, BoxLayout.Y_AXIS));
        JLabel arrivalStationLabel = new JLabel("Ga Phan Thiết", JLabel.CENTER);
        JLabel arrivalTimeLabel = new JLabel("11:05", JLabel.CENTER);
        arrivalTimeLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        JLabel arrivalDateLabel = new JLabel("30 tháng 11", JLabel.CENTER);
        arrivalPanel.add(arrivalStationLabel);
        arrivalPanel.add(arrivalTimeLabel);
        arrivalPanel.add(arrivalDateLabel);
        trainInfoPanel.add(arrivalPanel);
        
        tripDetailsPanel.add(trainInfoPanel, BorderLayout.CENTER);
        
        // Price and select button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel priceLabel = new JLabel("204,000 VND");
        priceLabel.setForeground(Color.RED);
        priceLabel.setFont(new Font("ROboto", Font.BOLD, 14));
        
        JButton selectButton = new JButton("Chọn ghế");
        selectButton.setBackground(new Color(249, 115, 22));
        selectButton.setForeground(Color.WHITE);
        
        bottomPanel.add(priceLabel, BorderLayout.WEST);
        bottomPanel.add(selectButton, BorderLayout.EAST);
        
        tripDetailsPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Combine all
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.add(routeLabel, BorderLayout.NORTH);
        contentPanel.add(datePanel, BorderLayout.CENTER);
        contentPanel.add(tripDetailsPanel, BorderLayout.SOUTH);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
}