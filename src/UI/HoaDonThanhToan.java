package UI;

import DAO.DAO_HanhKhach;
import DAO.DAO_HoaDon;
import DAO.DAO_Ve;
import DAO.DAO_Ve.BookingResult;
import UI.TicketPaymentListener;
import entity.HanhKhach;
import util.QRCodeGenerator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.google.zxing.WriterException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import util.QRCodeGenerator;

public class HoaDonThanhToan extends JDialog {
    // Thông tin chuyến và giá gốc
    private final String maNV;
    private final String maHK;
    private final String maTau;
    private final String tenTau;
    private final String gioDi;
    private final String gioDen;
    private final String loaiVe;
    private final String maLoaiVe;
    private final String ngayDiStr;
    private final Date ngayDi;
    private final String loaiChuyen;
    private final String ngayVeStr;
    private int cho;
    private int soLuongVe;
    private double tongTien;
    private final double unitPrice;

    // **Mới**: ga đi / ga đến
    private final String originGa;
    private final String destGa;

    // Các thành phần nhập liệu
    private final JTextField txtTenKhach  = new JTextField();
    private final JTextField txtCccd      = new JTextField();
    private final JTextField txtSdt       = new JTextField();
    private final JTextField txtTienKhach = new JTextField();
    private final JTextField txtTienThua  = new JTextField();

    private final TicketPaymentListener listener;
    private DefaultTableModel previewModel;
    
    private JButton btnChonHanhKhach;

    /**
     * Constructor
     */
    public HoaDonThanhToan(
            Frame owner,
            String maNV,
            String maHK,
            String maTau,
            String tenTau,
            String gioDi,
            String gioDen,
            String ngayDiStr,
            String loaiVe,
            String maLoaiVe,
            int cho,
            int soLuongVe,
            String tenKhach,
            String cccd,
            String sdt,
            double tongTien,
            String originGa,    // ← bổ sung
            String destGa,      // ← bổ sung
            String loaiChuyen,
            String ngayVeStr,
            TicketPaymentListener listener
    ) {
        super(owner, "Thanh Toán Vé", true);

        // 1) Gán tất cả dữ liệu
        this.maNV        = maNV;
        this.maHK        = maHK;
        this.maTau       = maTau;
        this.tenTau      = tenTau;
        this.gioDi       = gioDi;
        this.gioDen      = gioDen;
        this.ngayDiStr   = ngayDiStr;
        Date d;
        try {
            d = new SimpleDateFormat("dd/MM/yyyy").parse(ngayDiStr);
        } catch (ParseException ex) {
            d = new Date();
        }
        this.ngayDi      = d;
        this.loaiVe      = loaiVe;
        this.maLoaiVe    = maLoaiVe;
        this.cho         = cho;
        this.soLuongVe   = soLuongVe;
        this.tongTien    = tongTien;
        this.unitPrice   = (soLuongVe > 0) ? tongTien / soLuongVe : 0;
        this.originGa    = originGa;    // lưu ga đi
        this.destGa      = destGa;      // lưu ga đến
        this.loaiChuyen  = loaiChuyen;
        this.ngayVeStr   = ngayVeStr;
        this.listener    = listener;

        // 2) Thiết lập trước các trường đã biết
        txtTenKhach.setText(tenKhach);
        txtCccd    .setText(cccd);
        txtSdt     .setText(sdt);
        txtTienThua.setEditable(false);

        // 3) Xây dựng UI
        initUI();

        // 4) Sau khi initUI() khởi tạo xong, gán ga đi/ga đến vào form
        //    (nếu bạn muốn hiển thị chúng trong phần “Thông tin khách & hành trình”)
        //    Ví dụ bạn có thêm 2 JTextField txtGaDi, txtGaDen thì ở đây:
        // txtGaDi.setText(originGa);
        // txtGaDen.setText(destGa);

        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // 1) Preview vé
        previewModel = new DefaultTableModel(
            new String[]{"Tàu","Giờ đi","Giờ đến","Ngày đi","Loại vé","Chỗ","SL vé","Tổng tiền"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        };
        previewModel.addRow(new Object[]{
            tenTau,
            gioDi,
            gioDen,
            new SimpleDateFormat("dd/MM/yyyy").format(ngayDi),
            loaiVe,
            cho,
            soLuongVe,
            String.format("%.0f", tongTien)
        });
        previewModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
                try {
                    int newQty = Integer.parseInt(previewModel.getValueAt(0,6).toString());
                    soLuongVe = newQty;
                    tongTien  = unitPrice * newQty;
                    previewModel.setValueAt(String.format("%.0f", tongTien), 0, 7);
                } catch (Exception ignored) {}
            }
        });
        JTable tblPreview = new JTable(previewModel);
        tblPreview.setRowHeight(30);
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(new TitledBorder("Thông tin vé"));
        p1.add(new JScrollPane(tblPreview), BorderLayout.CENTER);
        add(p1, BorderLayout.NORTH);

        // 2) Form nhập liệu
        JPanel p2 = new JPanel(new GridLayout(9, 2, 5, 5));
        p2.setBorder(new TitledBorder("Thông tin khách & hành trình"));

        // Nếu bạn muốn hiển thị luôn originGa/destGa ở đây, thêm:
        // p2.add(new JLabel("Ga đi:"));    p2.add(new JTextField(originGa) {{ setEditable(false); }});
        // p2.add(new JLabel("Ga đến:"));   p2.add(new JTextField(destGa)   {{ setEditable(false); }});

        p2.add(new JLabel("Họ và tên:"));      p2.add(txtTenKhach);
        p2.add(new JLabel("CCCD:"));            p2.add(txtCccd);
        p2.add(new JLabel("SĐT:"));             p2.add(txtSdt);
        p2.add(new JLabel("Loại chuyến:"));     p2.add(new JTextField(loaiChuyen) {{ setEditable(false); }});
        p2.add(new JLabel("Ngày đi:"));         p2.add(new JTextField(ngayDiStr)   {{ setEditable(false); }});
        p2.add(new JLabel("Ngày về:"));         p2.add(new JTextField(ngayVeStr)   {{ setEditable(false); }});
        p2.add(new JLabel("Tiền khách đưa:"));  p2.add(txtTienKhach);
        p2.add(new JLabel("Tiền thừa:"));       p2.add(txtTienThua);
        btnChonHanhKhach = new JButton("Chọn hành khách đã đăng ký");
        p2.add(new JLabel()); p2.add(btnChonHanhKhach);

        add(p2, BorderLayout.CENTER);
        
        btnChonHanhKhach.addActionListener(e -> {
            HanhKhach selected = showChonHanhKhachDialog();
            if (selected != null) {
                txtTenKhach.setText(selected.getTen());
                txtCccd    .setText(selected.getCCCD());
                txtSdt     .setText(selected.getSdt());
            }
        });

        // 3) Document listener tính tiền thừa
        txtTienKhach.getDocument().addDocumentListener(new DocumentListener() {
            private void updateChange() {
                try {
                    double paid = Double.parseDouble(txtTienKhach.getText().trim());
                    double change = paid - tongTien;
                    txtTienThua.setText(change >= 0
                        ? String.format("%.0f", change)
                        : "Chưa đủ");
                } catch (Exception ex) {
                    txtTienThua.setText("");
                }
            }
            public void insertUpdate(DocumentEvent e) { updateChange(); }
            public void removeUpdate(DocumentEvent e) { updateChange(); }
            public void changedUpdate(DocumentEvent e) { updateChange(); }
        });

     // 4) Nút Thanh toán & QR Pay

     // Nút Thanh toán
     JButton btnPay = new JButton("Thanh toán");
     btnPay.addActionListener(e -> handlePayment());

     // Nút Thanh toán QR
     JButton btnQR = new JButton("Thanh toán QR");
     btnQR.addActionListener(e -> {
    	    showQRDialog(tongTien);
    	});

     // **Khởi tạo panel pBtn trước khi add nút**
     JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     // Thêm QR trước để nó xuất hiện bên trái, rồi đến Thanh toán
     pBtn.add(btnQR);
     pBtn.add(btnPay);

     // Cuối cùng add panel này vào dialog
     add(pBtn, BorderLayout.SOUTH);

    }
    
    /**
     * Dialog con hiển thị danh sách hành khách từ DB để chọn.
     */
    private HanhKhach showChonHanhKhachDialog() {
        JDialog dlg = new JDialog(this, "Chọn hành khách", true);
        DefaultTableModel m = new DefaultTableModel(new String[]{"Mã HK","Họ tên","CCCD","SĐT"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tbl = new JTable(m);
        try {
            List<HanhKhach> list = DAO_HanhKhach.getAll();
            for (HanhKhach hk : list) {
                m.addRow(new Object[]{ hk.getMaHK(), hk.getTen(), hk.getCCCD(), hk.getSdt() });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Không tải được danh sách hành khách:\n"+ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        dlg.getContentPane().add(new JScrollPane(tbl), BorderLayout.CENTER);
        JPanel btns = new JPanel();
        JButton ok = new JButton("OK"), cancel = new JButton("Cancel");
        btns.add(ok); btns.add(cancel);
        dlg.getContentPane().add(btns, BorderLayout.SOUTH);
        final HanhKhach[] sel = new HanhKhach[1];
        ok.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r >= 0) {
                String id = (String)m.getValueAt(r,0);
                try { sel[0] = DAO_HanhKhach.getById(id); } catch (SQLException ignore){}
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg,"Chọn một khách hàng!","Chưa chọn",JOptionPane.WARNING_MESSAGE);
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        dlg.setSize(500,400);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
        return sel[0];
    }

    private void handlePayment() {
        // 1) Kiểm tra nhập liệu
        String ten = txtTenKhach.getText().trim();
        String cccd = txtCccd.getText().trim();
        String sdt = txtSdt.getText().trim();
        if (ten.isEmpty() || cccd.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin khách.",
                "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 2) Kiểm tra tiền mặt
        double paid;
        try {
            paid = Double.parseDouble(txtTienKhach.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Tiền khách đưa phải là số",
                "Lỗi", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (paid < tongTien) {
            JOptionPane.showMessageDialog(this,
                "Tiền khách đưa chưa đủ",
                "Lỗi", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 3) Xử lý giao dịch
        processPayment();
    }
    
    /**
     * Thực hiện book vé, tạo hóa đơn, callback lên UI và đóng dialog.
     * Không làm bất cứ validate nào, chỉ chạy đúng luồng lưu giao dịch.
     */
    private void processPayment() {
        try {
            // 1) Thực hiện book vé
            BookingResult br = DAO_Ve.bookTicket(
                txtTenKhach.getText().trim(),
                txtCccd.getText().trim(),
                txtSdt.getText().trim(),
                maTau,
                gioDi,
                ngayDi,
                loaiVe,
                String.valueOf(cho),
                maLoaiVe,
                soLuongVe
            );

            // 2) Tạo hóa đơn
            String maHD = DAO_HoaDon.createInvoice(maNV, br.getMaHK());

            // 3) Callback lên panel in vé
            Menu menu = (Menu) SwingUtilities.getWindowAncestor(this);
            VeTau.DanhSachVePanel panel = menu.getDanhSachVePanel();
            panel.onTicketPaid(
                br, maHD, tenTau, maLoaiVe,
                cho, soLuongVe, tongTien,
                originGa, destGa,
                loaiChuyen, ngayVeStr
            );

            // 4) Chuyển tab và thông báo
            menu.getCardLayout().show(menu.getMainContentPanel(), "VeTau");
            menu.updateNavButtons("Vé tàu");
            JOptionPane.showMessageDialog(this,
                "Thanh toán thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi lưu dữ liệu: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    
    /**
     * Hiển thị một dialog chứa hình QR để người dùng quét.
     *
     * @param qrText Nội dung sẽ nhúng trong QR (ví dụ "nganhang://STK=0123456789;amount=50000")
     */
    private void showQRDialog(double amount) {
        // Đường dẫn đến file QR cá nhân trong resources
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/my_momo_qr.jpg"));
        JLabel lblQR = new JLabel(icon, SwingConstants.CENTER);

        // Hiển thị số tiền để người quét nhập vào
        JLabel lblAmount = new JLabel("Vui lòng chuyển: " + String.format("%,d", (long)amount) + " VND",
                                      SwingConstants.CENTER);

        JButton btnDone = new JButton("Tôi đã chuyển tiền");
        btnDone.addActionListener(e -> {
            // Đóng dialog QR
            Window w = SwingUtilities.getWindowAncestor(btnDone);
            if (w != null) w.dispose();
            // Tiếp tục luồng lưu vé và hoá đơn
            processPayment();
        });

        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(lblAmount, BorderLayout.NORTH);
        panel.add(lblQR,     BorderLayout.CENTER);
        panel.add(btnDone,   BorderLayout.SOUTH);

        JDialog dlg = new JDialog(this, "Quét QR Momo cá nhân", true);
        dlg.getContentPane().add(panel);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }


    

}
