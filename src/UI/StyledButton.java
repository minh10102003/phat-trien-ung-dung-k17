package UI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class StyledButton extends JButton {
    private static final long serialVersionUID = 1L;
    
    private Color normalBg;
    private Color hoverBg;
    private Color pressedBg;
    private int arcRadius;
    
    public StyledButton(String text, 
                        Color normalBg, 
                        Color hoverBg, 
                        Color pressedBg, 
                        int arcRadius,
                        Dimension size) {
        super(text);
        this.normalBg  = normalBg;
        this.hoverBg   = hoverBg;
        this.pressedBg = pressedBg;
        this.arcRadius = arcRadius;
        
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setMargin(new Insets(4, 8, 4, 8));
        
        // 1) Thiết lập background ban đầu
        setBackground(normalBg);

        // 2) Mouse listener để đổi màu hover và pressed
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) setBackground(normalBg);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) setBackground(pressedBg);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    // Khi release, nếu chuột vẫn còn trên nút thì hover, ngược lại normal
                    Point p = e.getPoint();
                    if (contains(p)) setBackground(hoverBg);
                    else setBackground(normalBg);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        // Vẽ background bo tròn
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(
            0, 0, getWidth(), getHeight(), arcRadius, arcRadius));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // Khi disabled thì làm mờ nút
        setForeground(enabled ? Color.WHITE : Color.LIGHT_GRAY);
        setBackground(enabled ? normalBg : normalBg.darker().darker());
    }
}
