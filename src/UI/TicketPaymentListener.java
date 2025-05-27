// UI/TicketPaymentListener.java
package UI;

import DAO.DAO_Ve;
import DAO.DAO_Ve.BookingResult;
import java.sql.SQLException;

public interface TicketPaymentListener {
    /**
     * @param br     kết quả bookTicket (chứa maVe, maHK…)
     * @param maHD   mã hóa đơn vừa tạo
     * @param maLV   mã loại vé
     * @param c      số ghế
     * @param sl     số lượng vé
     * @param tt     tổng tiền
     */
	void onTicketPaid(
			BookingResult br,
	        String maHD,
	        String tenTau,
	        String maLoaiVe,
	        int cho,
	        int soLuongVe,
	        double tongTien,
	        String gaDi,
	        String gaDen,
	        String loaiChuyen,
	        String ngayVe
	    );
}
