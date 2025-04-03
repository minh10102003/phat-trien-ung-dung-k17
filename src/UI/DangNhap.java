package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class DangNhap extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public DangNhap() {
        setTitle("N13 Express");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with white background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Logo panel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        
        // Custom train logo
        TrainLogoPanel trainLogo = new TrainLogoPanel();
        trainLogo.setPreferredSize(new Dimension(100, 60));
        trainLogo.setMaximumSize(new Dimension(100, 60));
        trainLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title label
        JLabel titleLabel = new JLabel("N13 Express");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add logo and title to logo panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(trainLogo);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(titleLabel);
        
        logoPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(logoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Username field
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new RoundedTextField(15);
        usernameField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Password field
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new RoundedPasswordField(15);
        passwordField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Add form components
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Login button
        JButton loginButton = new RoundedButton("Đăng nhập");
        loginButton.setBackground(new Color(66, 153, 225)); // Blue color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        
        // Register button
        JButton registerButton = new RoundedButton("Đăng ký");
        registerButton.setBackground(new Color(66, 153, 225)); // Blue color
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                System.out.println("Login attempt with: " + username + ", " + password);
                // Add authentication logic here
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register button clicked");
                // Add registration logic here
            }
        });
        
        // Add buttons to panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Forgot password link
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        linkPanel.setBackground(Color.WHITE);
        
        JLabel forgotPasswordLink = new JLabel("Quên mật khẩu?");
        forgotPasswordLink.setFont(new Font("Arial", Font.ITALIC, 12));
        forgotPasswordLink.setForeground(Color.DARK_GRAY);
        forgotPasswordLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        forgotPasswordLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Forgot password clicked");
                // Add forgot password logic here
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forgotPasswordLink.setForeground(Color.BLACK);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                forgotPasswordLink.setForeground(Color.DARK_GRAY);
            }
        });
        
        linkPanel.add(forgotPasswordLink);
        mainPanel.add(linkPanel);
        
        // Add the panel to the frame with a light gray background
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(240, 240, 240));
        containerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        
        setContentPane(containerPanel);
    }
    
    // Custom panel for train logo
    class TrainLogoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Draw train lines
            g2d.setColor(new Color(66, 153, 225)); // Blue color
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(width/10, height/2, width*9/10, height/2);
            g2d.drawLine(width/7, height*2/5, width*6/7, height*2/5);
            g2d.drawLine(width/5, height*3/10, width*4/5, height*3/10);
            
            // Draw train body
            g2d.setColor(Color.BLACK);
            RoundRectangle2D trainBody = new RoundRectangle2D.Float(
                width/4, height/5, width/2, height/3, 10, 10);
            g2d.fill(trainBody);
            
            // Draw wheels
            g2d.fillOval(width*3/10, height/2 - 5, 10, 10);
            g2d.fillOval(width*7/10 - 10, height/2 - 5, 10, 10);
            
            g2d.dispose();
        }
    }
    
    // Custom rounded JTextField
    class RoundedTextField extends JTextField {
        private Shape shape;
        
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
        
        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            return shape.contains(x, y);
        }
    }
    
    // Custom rounded JPasswordField
    class RoundedPasswordField extends JPasswordField {
        private Shape shape;
        
        public RoundedPasswordField(int size) {
            super(size);
            setOpaque(false);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
        
        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            return shape.contains(x, y);
        }
    }
    
    // Custom rounded JButton
    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            
            FontMetrics metrics = g2.getFontMetrics(getFont());
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DangNhap().setVisible(true);
            }
        });
    }
}